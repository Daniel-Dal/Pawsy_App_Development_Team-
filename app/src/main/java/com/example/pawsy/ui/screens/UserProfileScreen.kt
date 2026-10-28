package com.example.pawsy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.pawsy.R
import com.example.pawsy.ui.components.PawsyButton
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.ui.components.PetSideMenu
import androidx.compose.foundation.layout.Box
@Composable
fun MyProfileText(navController: NavController,  mascotaId: String? = null,onEditClick: () -> Unit = {}) {
    var menuExpanded by remember { mutableStateOf(false) }
    PawsyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.pawsyColors.fondoPerfil)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(id = R.string.título_pantalla6),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                    )

                    Image(
                        painter = painterResource(id = R.drawable.pawsy_paw),
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(105f)
                            .offset(y = (-150).dp, x = (-40).dp)
                            .size(80.dp)
                    )
                }

                Text(
                    text = stringResource(id = R.string.boton_datosUsuario),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                )

                PawsyButton(

                    onClick = { /* Nothing yet */ }
                )

                Text(
                    text = stringResource(id = R.string.boton_idioma),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                )

                PawsyButton(

                    onClick = { /* Nothing yet */ }
                )

                Text(
                    text = stringResource(id = R.string.boton_notificacion),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                )

                PawsyButton(


                    onClick = { /* Nothing yet */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PawSubmitButton(
                        onClick = { menuExpanded = !menuExpanded }
                    )
                }

            }
            AnimatedVisibility(
                visible = menuExpanded,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                PetSideMenu(
                    onItemClick = { item ->
                        when (item) {
                            "Perfil mascota" -> {
                                val id = mascotaId ?: MascotaSeleccionada.id
                                if (id != null) {
                                    navController.navigate("petProfile/$id")
                                } else {
                                    navController.navigate("myPets")
                                }
                            }
                            "Paseos y actividad" -> navController.navigate("pya")
                            "Recomendaciones" -> navController.navigate("recom")
                            "Veterinarias cercanas" -> navController.navigate("nearbyVets")
                            "Reconocimiento visual" -> navController.navigate("food")
                            "Eventos" -> navController.navigate("eventos")
                        }
                    },
                    onDismiss = { menuExpanded = false }
                )
            }
        }
    }
}



@Composable
private fun PawSubmitButton(
    onClick: () -> Unit,
    pawImage: Painter = painterResource(R.drawable.ic_paw_menu)
) {
    Image(
        painter = pawImage,
        contentDescription = "Guardar perfil",
        modifier = Modifier
            .size(70.dp)
            .clickable(onClick = onClick)
    )
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    showSystemUi = true
)
@Composable
fun MyProfileTextPreview() {
    val navController = rememberNavController()
    MyProfileText(navController = navController)
}