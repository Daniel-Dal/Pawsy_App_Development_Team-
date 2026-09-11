package com.example.pawsy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Alignment
import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorFondoAgregarMascota
import com.example.pawsy.ui.theme.colorLetraInicioApp
import com.example.pawsy.ui.theme.colorTextEditTextAgregarMascota

@Composable
fun PawsyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorEditTextAgregarMascota,
            unfocusedContainerColor = colorEditTextAgregarMascota,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = colorTextEditTextAgregarMascota,
            unfocusedTextColor = colorTextEditTextAgregarMascota
        )
    )
}

@Composable
fun MyText() {
    PawsyTheme {
        var nombre by remember { mutableStateOf("") }
        var especie by remember { mutableStateOf("") }
        var edad by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("") }
        var peso by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondoAgregarMascota)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.titulo_pantalla5),
                style = MaterialTheme.typography.titleLarge,
                color = colorBlanco,
                modifier = Modifier.padding(top = 48.dp)
            )

            Text(
                text = stringResource(id = R.string.subtitulo_nombreMascota),
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )
            PawsyTextField(value = nombre, onValueChange = { nombre = it })

            Text(
                text = stringResource(id = R.string.subtitulo_especieMascota),
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )
            PawsyTextField(value = especie, onValueChange = { especie = it })

            Text(
                text = stringResource(id = R.string.subtitulo_edadMascota),
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )
            PawsyTextField(
                value = edad,
                onValueChange = { newValue -> if (newValue.all { it.isDigit() }) edad = newValue },
                keyboardType = KeyboardType.Number
            )

            Text(
                text = stringResource(id = R.string.subtitulo_razaMascota),
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )
            PawsyTextField(value = raza, onValueChange = { raza = it })

            Text(
                text = stringResource(id = R.string.subtitulo_pesoMascota),
                style = MaterialTheme.typography.labelSmall,
                color = colorBlanco
            )

            PawsyTextField(value = peso, onValueChange = { peso = it })
            OutlinedButton(
                onClick = { /* nothing yet */ },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, colorLetraInicioApp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorLetraInicioApp
                )
            ) {
                Text(text = stringResource(id = R.string.boton_continuar))
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
fun MyTextPreview() {
    MyText()
}
