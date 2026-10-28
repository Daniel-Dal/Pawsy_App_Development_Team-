package com.example.pawsy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pawsy.R
import com.example.pawsy.ui.components.BasicCircleBotton
import com.example.pawsy.ui.theme.PawsyFontFamily
import com.example.pawsy.ui.theme.colorPataSeleccionMascota
import com.example.pawsy.ui.theme.pawsyColors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

/** Mascota tal como se guarda en Firestore (la foto es una URL). */
data class MascotaItem(
    val id: String,
    val nombre: String,
    val urlImagen: String
)

/**
 * Lee el arreglo "mascotas" del documento del usuario y trae cada mascota
 * de la colección "mascotas". Si el usuario no tiene el arreglo, devuelve una lista vacía.
 */
suspend fun cargarMascotasUsuario(): List<MascotaItem> {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return emptyList()
    val db = FirebaseFirestore.getInstance()

    val usuarioDoc = db.collection("usuarios").document(uid).get().await()
    val ids = (usuarioDoc.get("mascotas") as? List<*>)
        ?.filterIsInstance<String>()
        ?: emptyList()

    return coroutineScope {
        ids.map { id ->
            async { db.collection("mascotas").document(id).get().await() }
        }
            .awaitAll()
            .filter { it.exists() }
            .map {
                MascotaItem(
                    id = it.id,
                    nombre = it.getString("nombre") ?: "",
                    urlImagen = it.getString("urlimagen") ?: ""
                )
            }
    }
}

@Composable
fun MyPetsScreen(navController: NavController) {

    var mascotas by remember { mutableStateOf<List<MascotaItem>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            mascotas = cargarMascotasUsuario()
        } catch (e: Exception) {
            mensajeError = e.message
        } finally {
            cargando = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.fondoSeleccionMascotaApp)
    ) {

        BasicCircleBotton(
            onClick = { },
            size = 60.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 645.dp,
                    start = 300.dp
                ),
            backgroundColor = MaterialTheme.pawsyColors.fondoAgregarMascota
        ) {
            Icon(
                painter = painterResource(R.drawable.return_arrow),
                contentDescription = "Regreso",
                modifier = Modifier
                    .size(40.dp)
                    .offset(x = (-5).dp),
                tint = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
            )
        }

        Text(
            text = "Tus ",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 150.dp,
                    start = 30.dp
                ),
            fontSize = 50.sp,
            fontFamily = PawsyFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.pawsyColors.blanco
        )

        Text(
            text = "Mascotas",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 200.dp,
                    start = 30.dp
                ),
            fontSize = 50.sp,
            fontFamily = PawsyFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.pawsyColors.blanco
        )

        Image(
            painter = painterResource(id = R.drawable.pawsy_paw_white),
            contentDescription = "Mascotas",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 180.dp,
                    end = 40.dp
                )
                .size(80.dp),
            colorFilter = ColorFilter.tint(colorPataSeleccionMascota)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 300.dp,
                    start = 30.dp,
                    end = 30.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            items(mascotas) { mascota ->

                BasicCircleBotton(
                    onClick = {
                        MascotaSeleccionada.id = mascota.id
                        navController.navigate("petProfile/${mascota.id}")
                    },
                    size = 120.dp,
                    backgroundColor = MaterialTheme.pawsyColors.blanco
                ) {
                    if (mascota.urlImagen.isNotBlank()) {
                        AsyncImage(
                            model = mascota.urlImagen,
                            contentDescription = mascota.nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            onError = { estado ->
                                android.util.Log.e("IMG_MASCOTA", "url=${mascota.urlImagen} error=${estado.result.throwable}")
                            }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.pawsy_paw_white),
                            contentDescription = mascota.nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp),
                            colorFilter = ColorFilter.tint(colorPataSeleccionMascota)
                        )
                    }
                }
            }

            // Botón +
            item {
                BasicCircleBotton(
                    onClick = {
                        navController.navigate("addPet")
                    },
                    size = 120.dp,
                    backgroundColor = MaterialTheme.pawsyColors.fondoAgregarMascota
                ) {
                    Text(
                        text = "+",
                        fontSize = 60.sp,
                        color = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
                    )
                }
            }
        }

        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.pawsyColors.blanco
            )
        }

        mensajeError?.let {
            Text(
                text = "No se pudieron cargar las mascotas: $it",
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPetsScreenPreview() {
    val navController = rememberNavController()
    MyPetsScreen(navController)
}