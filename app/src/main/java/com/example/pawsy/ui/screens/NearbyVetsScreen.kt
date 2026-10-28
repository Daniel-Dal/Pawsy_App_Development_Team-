package com.example.pawsy.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.pawsy.R
import com.example.pawsy.data.NearbyPlace
import com.example.pawsy.data.NearbyPlacesRepository
import com.example.pawsy.data.PlaceKind
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import com.example.pawsy.BuildConfig
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex

private val BOGOTA = GeoPoint(4.7110, -74.0721)

private val MapTilerSource = object : OnlineTileSourceBase(
    "MapTiler", 0, 20, 256, ".png",
    arrayOf("https://api.maptiler.com/maps/streets-v2/256/")
) {
    override fun getTileURLString(pMapTileIndex: Long): String =
        baseUrl +
                MapTileIndex.getZoom(pMapTileIndex) + "/" +
                MapTileIndex.getX(pMapTileIndex) + "/" +
                MapTileIndex.getY(pMapTileIndex) + mImageFilenameEnding +
                "?key=" + BuildConfig.MAPTILER_KEY
}

@SuppressLint("MissingPermission")
private fun lastKnownLocation(context: Context): GeoPoint? {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val best = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
        .maxByOrNull { it.time }
    return best?.let { GeoPoint(it.latitude, it.longitude) }
}

@Composable
fun NearbyVetsScreen(navController: NavController) {
    PawsyTheme {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        // Textos desde strings.xml (se leen aquí porque `update` del mapa no es composable)

        val myLocationText = stringResource(R.string.marcador_mi_ubicacion)
        val vetText = stringResource(R.string.lugar_veterinaria)
        val storeText = stringResource(R.string.lugar_tienda_mascotas)
        val noNameText = stringResource(R.string.lugar_sin_nombre)
        val errorText = stringResource(R.string.error_buscando_lugares)

        var center by remember { mutableStateOf(BOGOTA) }
        var places by remember { mutableStateOf<List<NearbyPlace>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }

        // Configuración obligatoria de osmdroid: identificar la app
        remember {
            Configuration.getInstance().load(
                context,
                context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
            )
            Configuration.getInstance().userAgentValue = "PawsyApp/1.0 (contactopawsyapp@gmail.com)"
            true
        }

        fun search(point: GeoPoint) = scope.launch {
            loading = true
            NearbyPlacesRepository.nearby(point.latitude, point.longitude)
                .onSuccess { places = it }
                .onFailure {
                    Toast.makeText(
                        context,
                        it.message ?: errorText,
                        Toast.LENGTH_LONG
                    ).show()
                }
            loading = false
        }

        val permission = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            val point = if (granted) lastKnownLocation(context) ?: BOGOTA else BOGOTA
            center = point
            search(point)
        }

        LaunchedEffect(Unit) { permission.launch(Manifest.permission.ACCESS_FINE_LOCATION) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.fondoPerfil)
        ) {


            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())

            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(MapTilerSource)
                        setMultiTouchControls(true)
                        controller.setZoom(15.0)
                    }
                },
                update = { map ->
                    map.controller.setCenter(center)
                    map.overlays.clear()

                    // Tu ubicación
                    map.overlays.add(Marker(map).apply {
                        position = center
                        title = myLocationText
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    })

                    // Veterinarias y tiendas
                    places.forEach { p ->
                        map.overlays.add(Marker(map).apply {
                            position = GeoPoint(p.lat, p.lon)
                            title = p.name ?: noNameText
                            snippet = if (p.kind == PlaceKind.VET) vetText else storeText
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        })
                    }
                    map.invalidate()
                },
                onRelease = { it.onDetach() }
            )
        }
    }
}