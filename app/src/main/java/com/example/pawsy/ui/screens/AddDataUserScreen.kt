package com.example.pawsy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import androidx.compose.ui.Alignment
import com.example.pawsy.ui.components.Calendario
import com.example.pawsy.ui.components.ContinueBotton
import com.example.pawsy.ui.components.PawsyTextField
import com.example.pawsy.ui.theme.pawsyColors
import android.net.Uri
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.style.TextAlign
import com.example.pawsy.ui.components.ImagenPerfil

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DataUser(navController: NavController) {
    PawsyTheme {
        var nickname by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("") }
        var imagenPerfil by remember { mutableStateOf<Uri?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.recuadroInicioSesion)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Agregar datos nuevo usuario",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = "Nickname",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco
            )

            PawsyTextField(value = nickname, onValueChange = { nickname = it },
                modifier = Modifier.
                    width(350.dp),
                containerColor = MaterialTheme.pawsyColors.fondoAgregarMascota,
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota)

            Text(
                text = "Fecha de nacimiento",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier.padding(top = 50.dp)
            )
            Calendario(
                fecha = fechaNacimiento,
                onFechaSeleccionada = { fecha ->
                    fechaNacimiento = fecha
                } ,
                calendarColor = MaterialTheme.pawsyColors.fondoAgregarMascota
            )

            Text(
                text = "Imagen perfil",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                    textAlign = TextAlign.Center
            )
            ImagenPerfil(
                imagenSeleccionada = imagenPerfil,
                onImagenSeleccionada = { uri ->
                    imagenPerfil = uri
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            ContinueBotton(
                onClick = {
                    // Acción del botón
                    navController.navigate("addPet")
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally),
                textColor = MaterialTheme.pawsyColors.blanco,
                borderColor = MaterialTheme.pawsyColors.blanco,
                borderWidth = 1.dp
            ) {
                Text(
                    text = stringResource(R.string.boton_continuar)
                )
            }


        }
    }
}





@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    showSystemUi = true
)
@Composable
fun DataUserpreview() {
    val navController = rememberNavController()

    DataUser(navController)
}
