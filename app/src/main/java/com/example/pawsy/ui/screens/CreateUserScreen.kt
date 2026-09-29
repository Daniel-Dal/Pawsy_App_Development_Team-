package com.example.pawsy.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawsy.R
import com.example.pawsy.ui.components.BasicCircleBotton
import com.example.pawsy.ui.theme.PawsyFontFamily
import com.example.pawsy.ui.theme.pawsyColors
import com.example.pawsy.ui.components.PawsyTextField
import com.example.pawsy.ui.components.ContinueBotton

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun CreateUserScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.fondoPerfil)
    ) {
        var nombre by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var aceptado by remember { mutableStateOf(false) }

        BasicCircleBotton (
            onClick = { },
            size = 60.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 30.dp,
                    start = 20.dp
                ),
            backgroundColor = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
        ) {
            Icon(
                painter = painterResource(R.drawable.return_arrow),
                contentDescription = "Mascota",
                modifier = Modifier.size(40.dp)
                    .offset(x = (-5).dp),
                tint = MaterialTheme.pawsyColors.fondoAgregarMascota
            )
        }

        Image(
            painter = painterResource(id = R.drawable.pawsy_paw_white),
            contentDescription = "Mascotas",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(
                    y = (0).dp,
                    x = (30).dp
                )
                .size(110.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.pawsyColors.fondoSeleccionMascotaApp)
        )

        Text(
            text = "Pawsy",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 100.dp
                ),
            fontSize = 60.sp,
            fontFamily = PawsyFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.pawsyColors.letraInicioNombreApp
        )


        // Cuadro inferior rojo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.pawsyColors.recuadroInicioSesion,
                    shape = RoundedCornerShape(
                        topStart = 35.dp,
                        topEnd = 35.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {


            Text(
                text = "Crear cuenta",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = 44.dp,
                        top = 60.dp
                    ),
                fontSize = 28.sp,
                fontFamily = PawsyFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.pawsyColors.blanco
            )
            Image(
                painter = painterResource(id = R.drawable.pawsy_paw_white),
                contentDescription = "Mascotas",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 54.dp,
                        end = 40.dp
                    )
                    .size(50.dp)
                    .rotate(90f),
                colorFilter = ColorFilter.tint(MaterialTheme.pawsyColors.blanco)
            )
            Text(
                text = "Nombre",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = 55.dp,
                        top = 110.dp
                    ),
                fontSize = 20.sp,
                fontFamily = PawsyFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 150.dp
                    ).width(330.dp),
                containerColor = MaterialTheme.pawsyColors.fondoAgregarMascota,
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota
            )

            Text(
                text = "Correo",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = 58.dp,
                        top = 215.dp
                    ),
                fontSize = 20.sp,
                fontFamily = PawsyFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 250.dp
                    ).width(330.dp),
                containerColor = MaterialTheme.pawsyColors.fondoAgregarMascota,
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota
            )
            Text(
                text = "Contraseña",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        start = 58.dp,
                        top = 315.dp
                    ),
                fontSize = 20.sp,
                fontFamily = PawsyFontFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 350.dp
                    ).width(330.dp),
                containerColor = MaterialTheme.pawsyColors.fondoAgregarMascota,
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 420.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = aceptado,
                    onCheckedChange = { aceptado = it },
                    modifier = Modifier.size(24.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.Black
                    )
                )

                Text(
                    text = " Aceptar ",
                    color = MaterialTheme.pawsyColors.blanco,
                    fontSize = 16.sp
                )
                Text(
                    text = "Términos y condiciones",
                    modifier = Modifier
                        .clickable {
                            // Acción al hacer clic
                        },
                    color = MaterialTheme.pawsyColors.blanco,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 16.sp
                )
            }
            ContinueBotton(
                onClick = {
                    // Acción del botón

                    navController.navigate("AddDataUser")
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.TopCenter)
                    .padding(top = 460.dp),
                textColor = MaterialTheme.pawsyColors.blanco,
                borderColor = MaterialTheme.pawsyColors.blanco,
                borderWidth = 5.dp
            ) {
                Text(
                    text = stringResource(R.string.boton_continuar)
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.dog_and_cat),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 500.dp)
                .size(200.dp)
        )

        }

}


@Preview(showBackground = true)
@Composable
fun CreateUserScreenPreview() {
    val navController = rememberNavController()
    CreateUserScreen(navController)
}