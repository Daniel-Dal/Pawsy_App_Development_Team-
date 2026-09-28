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
import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorFondoAgregarMascota
import android.net.Uri
import com.example.pawsy.ui.components.ImagenPerfil


@Composable
fun DataUser() {
    PawsyTheme {
        var nickname by remember { mutableStateOf("") }
        var fechaNacimiento by remember { mutableStateOf("") }
        var edad by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("") }
        var imagenPerfil by remember { mutableStateOf<Uri?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondoAgregarMascota)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Agregar datos nuevo usuario",
                style = MaterialTheme.typography.titleLarge,
                color = colorBlanco,
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = "Nickname",
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )

            PawsyTextField(value = nickname, onValueChange = { nickname = it })

            Text(
                text = "Fecha de nacimiento",
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco,
                modifier = Modifier.padding(top = 40.dp)
            )
            Calendario(
                fecha = fechaNacimiento,
                onFechaSeleccionada = { fecha ->
                    fechaNacimiento = fecha
                }
            )

            Text(
                text = "Imagen perfil",
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco,
                modifier = Modifier.padding(top = 40.dp, start = 90.dp)
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

                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally),
                textColor = colorBlanco,
                borderColor = colorBlanco,
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
    DataUser()
}
