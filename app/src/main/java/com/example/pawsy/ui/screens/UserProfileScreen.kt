package com.example.pawsy.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorFondoPerfil
import com.example.pawsy.ui.theme.colorLetraInicioApp
import com.example.pawsy.ui.theme.colorTextEditTextAgregarMascota
import com.example.pawsy.ui.components.PawsyButton

@Composable
fun MyProfileText() {

    Column( modifier = Modifier
        .fillMaxSize()
        .background(colorFondoPerfil)
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp)) {

        Text(text = stringResource(id = R.string.título_pantalla6),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 48.dp),
            color = colorTextEditTextAgregarMascota
            )

        PawsyButton(
            text = stringResource(id = R.string.boton_datosUsuario),
            onClick = {/* Nothing yet */}
        )

        PawsyButton(
            text = stringResource(id = R.string.boton_idioma),
            onClick = {/* Nothing yet */}
        )

        PawsyButton(
            text = stringResource(id = R.string.boton_notificacion),
            onClick = {/* Nothing yet */}
        )


    }


}


@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    showSystemUi = true
)
@Composable
fun MyProfileTextPreview(){
    MyProfileText()
}