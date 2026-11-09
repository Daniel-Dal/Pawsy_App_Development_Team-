package com.example.pawsy.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.maps.android.compose.rememberMarkerState
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.pawsy.R
import com.example.pawsy.data.NearbyPlace
import com.example.pawsy.data.NearbyPlacesRepository
import com.example.pawsy.data.PlaceKind
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private val BOGOTA = LatLng(4.7110, -74.0721)

private const val HUE_GREEN = BitmapDescriptorFactory.HUE_GREEN
private const val HUE_RED = BitmapDescriptorFactory.HUE_RED

@SuppressLint("MissingPermission")
private fun lastKnownLocation(context: Context): LatLng? {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val best = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
        .maxByOrNull { it.time }
    return best?.let { LatLng(it.latitude, it.longitude) }
}

@Composable
fun NearbyVetsScreen(navController: NavController) {
    PawsyTheme {
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        val myLocationText = stringResource(R.string.marcador_mi_ubicacion)
        val vetText = stringResource(R.string.lugar_veterinaria)
        val storeText = stringResource(R.string.lugar_tienda_mascotas)
        val noNameText = stringResource(R.string.lugar_sin_nombre)
        val errorText = stringResource(R.string.error_buscando_lugares)

        var center by remember { mutableStateOf(BOGOTA) }
        var places by remember { mutableStateOf<List<NearbyPlace>>(emptyList()) }
        var loading by remember { mutableStateOf(false) }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(BOGOTA, 15f)
        }

        fun search(point: LatLng) = scope.launch {
            loading = true
            NearbyPlacesRepository.nearby(context, point.latitude, point.longitude)
                .onSuccess { places = it }
                .onFailure {
                    Toast.makeText(context, it.message ?: errorText, Toast.LENGTH_LONG).show()
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

        // Mover la cámara cuando cambia el centro
        LaunchedEffect(center) {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(center, 15f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.fondoPerfil)
        ) {
            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())

            GoogleMap(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState
            ) {
                // Tu ubicación (verde)
                val myMarkerState = rememberMarkerState(position = center)
                LaunchedEffect(center) { myMarkerState.position = center }

                Marker(
                    state = myMarkerState,
                    title = myLocationText,
                    icon = BitmapDescriptorFactory.defaultMarker(HUE_GREEN)
                )

                // Veterinarias y tiendas (rojo)
                places.forEach { p ->
                    key(p.lat, p.lon) {
                        Marker(
                            state = rememberMarkerState(position = LatLng(p.lat, p.lon)),
                            title = p.name ?: noNameText,
                            snippet = if (p.kind == PlaceKind.VET) vetText else storeText,
                            icon = BitmapDescriptorFactory.defaultMarker(HUE_RED)
                        )
                    }
                }
            }
        }
    }
}