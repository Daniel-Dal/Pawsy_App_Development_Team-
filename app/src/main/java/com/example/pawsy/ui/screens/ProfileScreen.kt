package com.example.pawsy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import com.example.pawsy.ui.components.PetSideMenu

data class PetProfile(
    val nombre: String = "",
    val especie: String = "",
    val edad: String = "",
    val raza: String = "",
    val peso: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    petProfile: PetProfile = PetProfile(),
    onEditClick: () -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }

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

                Spacer(modifier = Modifier.height(32.dp))

                // Campos de solo lectura: muestran los datos recibidos en petProfile
                PetProfileField(label = "Nombre", value = petProfile.nombre)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Especie", value = petProfile.especie)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Edad", value = petProfile.edad)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Raza", value = petProfile.raza)

                Spacer(modifier = Modifier.height(20.dp))

                PetProfileField(label = "Peso", value = petProfile.peso)

                Spacer(modifier = Modifier.weight(1f))

                // Botón flotante inferior derecho (huella) — ej. para ir a editar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PawSubmitButton(onClick = { menuExpanded = !menuExpanded })
                }
            }

            // Menú lateral desplegable. Sin funcionalidad todavía: solo muestra las opciones.
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
        ProfileScreen(
            navController = navController,
            petProfile = PetProfile(
                nombre = "",
                especie = "",
                edad = "",
                raza = "",
                peso = ""
            )
        )
    }
}