package com.example.pawsy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.pawsyColors


@Composable
fun RegistroPaseoActividad(
    modifier: Modifier = Modifier,
    dias: List<String> = listOf("L", "M", "M", "J", "V", "S", "D"),
    juegos: String = "",
    walkIcon: Painter = painterResource(R.drawable.pawsy_r_p_a_app),
    pawImage: Painter = painterResource(R.drawable.ic_paw_outline)
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val colors = MaterialTheme.pawsyColors

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.fondoPerfil)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                        .background(colors.recuadroInicioSesion)
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Paseos y\nactividad",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Image(
                            painter = pawImage,
                            contentDescription = "Abrir menú",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable(onClick = { menuExpanded = !menuExpanded })
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {

                    Text(
                        text = "Registro paseos",
                        color = colors.textEditTextAgregarMascota,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(colors.recuadroInicioSesion)
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = walkIcon,
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .size(26.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            dias.forEach { dia ->
                                Text(
                                    text = dia,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Juegos",
                        color = colors.textEditTextAgregarMascota,
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo de solo lectura, mismo estilo píldora que en ProfileScreen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(colors.recuadroInicioSesion)
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = juegos.ifBlank { "—" },
                            color = colors.blanco,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.weight(1f))

                    // Botón flotante inferior derecho (huella) — círculo simple con borde
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = pawImage,
                            contentDescription = "Pawsy",
                            modifier = Modifier
                                .size(44.dp)
                                .clickable(onClick = { /* pendiente: acción del botón */ })
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = menuExpanded,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                PetSideMenu(onItemClick = { menuExpanded = false })
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 700)
@Composable
private fun RegistroPaseoActividadPreview() {
    PawsyTheme {
        RegistroPaseoActividad()
    }
}