package com.example.pawsy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.R
import com.example.pawsy.ui.components.PawsyButton
import com.example.pawsy.ui.components.PetSideMenu
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class PetProfile(
    val nombre: String = "",
    val especie: String = "",
    val edad: String = "",
    val raza: String = "",
    val peso: String = ""
)

/** Convierte números o texto de Firestore a texto para mostrar. */
private fun valorATexto(valor: Any?): String = when (valor) {
    null -> ""
    is Double -> if (valor % 1.0 == 0.0) valor.toLong().toString() else valor.toString()
    else -> valor.toString()
}

private suspend fun cargarPerfilMascota(mascotaId: String): PetProfile? {
    val doc = FirebaseFirestore.getInstance()
        .collection("mascotas")
        .document(mascotaId)
        .get()
        .await()

    if (!doc.exists()) return null

    return PetProfile(
        nombre = valorATexto(doc.get("nombre")),
        especie = valorATexto(doc.get("especie")),
        edad = valorATexto(doc.get("edad")),
        raza = valorATexto(doc.get("raza")),
        peso = valorATexto(doc.get("peso"))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    mascotaId: String? = null,
    onEditClick: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

    var petProfile by remember { mutableStateOf(PetProfile()) }
    var cargando by remember { mutableStateOf(mascotaId != null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(mascotaId) {
        if (mascotaId == null) {
            cargando = false
            return@LaunchedEffect
        }
        cargando = true
        mensajeError = null
        try {
            val perfil = cargarPerfilMascota(mascotaId)
            if (perfil != null) {
                petProfile = perfil
            } else {
                mensajeError = "No encontramos esta mascota."
            }
        } catch (e: Exception) {
            mensajeError = "No se pudo cargar el perfil: ${e.message}"
        } finally {
            cargando = false
        }
    }

    val colors = MaterialTheme.pawsyColors

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.fondoPerfil)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {

                // Encabezado: título + ícono de huella
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Perfil\nmascota",
                        color = colors.letraInicioApp,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    PawIconBadge(onClick = { menuExpanded = !menuExpanded })
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    if (cargando) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.blanco)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    mensajeError?.let {
                        Text(
                            text = it,
                            color = colors.blanco,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Campos de solo lectura con los datos traídos de Firestore
                    PetProfileField(label = "Nombre", value = petProfile.nombre)

                    Spacer(modifier = Modifier.height(20.dp))

                    PetProfileField(label = "Especie", value = petProfile.especie)

                    Spacer(modifier = Modifier.height(20.dp))

                    PetProfileField(label = "Edad", value = petProfile.edad)

                    Spacer(modifier = Modifier.height(20.dp))

                    PetProfileField(label = "Raza", value = petProfile.raza)

                    Spacer(modifier = Modifier.height(20.dp))

                    PetProfileField(label = "Peso", value = petProfile.peso)

                    Spacer(modifier = Modifier.height(48.dp))

                    // Botón de historias médicas
                    Box(contentAlignment = Alignment.Center) {
                        PawsyButton(
                            onClick = {
                                if (mascotaId != null) navController.navigate("historias/$mascotaId")
                            },
                            modifier = Modifier.height(56.dp)
                        )
                        Text(
                            text = "Historia médica",
                            color = colors.blanco,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón flotante inferior derecho (huella)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        PawSubmitButton(onClick = { menuExpanded = !menuExpanded })
                    }
                }
            }
        }

        // Menú lateral desplegable
        AnimatedVisibility(
            visible = menuExpanded,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            PetSideMenu(
                onItemClick = { item ->
                    when (item) {
                        "Perfil usuario" -> navController.navigate("userProfile")
                        "Paseos y actividad" -> navController.navigate("pya")
                        "Recomendaciones" -> navController.navigate("recom")
                    }
                },
                onDismiss = { menuExpanded = false }
            )
        }
    }
}

@Composable
private fun PetProfileField(
    label: String,
    value: String
) {

    val colors = MaterialTheme.pawsyColors

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = colors.textEditTextAgregarMascota,
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(colors.recuadroInicioSesion)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = value.ifBlank { "—" },
                color = colors.blanco,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun PawIconBadge(
    onClick: () -> Unit,
    pawImage: Painter = painterResource(R.drawable.ic_paw_orange)
) {
    Image(
        painter = pawImage,
        contentDescription = "Abrir menú",
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
private fun PawSubmitButton(
    onClick: () -> Unit,
    pawImage: Painter = painterResource(R.drawable.ic_paw_outline)
) {
    Image(
        painter = pawImage,
        contentDescription = "Guardar perfil",
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick)
    )
}

@Preview(showBackground = true, widthDp = 320, heightDp = 700, name = "Perfil mascota")
@Composable
private fun ProfileScreenPreview() {
    val navController = rememberNavController()
    PawsyTheme {
        ProfileScreen(navController = navController)
    }
}