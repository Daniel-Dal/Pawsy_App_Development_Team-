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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import com.example.pawsy.ui.components.PawsyTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.ui.components.BasicCircleBotton
import com.example.pawsy.ui.theme.pawsyColors

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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
            focusedContainerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
            unfocusedContainerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota,
            unfocusedTextColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyText(navController: NavController) {
    PawsyTheme {
        var nombre by remember { mutableStateOf("") }
        var especie by remember { mutableStateOf("") }
        var edad by remember { mutableStateOf("") }
        var raza by remember { mutableStateOf("") }
        var peso by remember { mutableStateOf("") }
        var especieExpandible by remember { mutableStateOf(false) }
        var unidadPeso by remember { mutableStateOf("Kg") }
        var unidadExpandible by remember { mutableStateOf(false) }

        val especieOpciones = stringArrayResource(id = R.array.opciones_especie)
        val unidades = stringArrayResource(id = R.array.opciones_unidad_peso)

    Box(modifier = Modifier
        .fillMaxSize()
        ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.fondoAgregarMascota)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
                verticalAlignment = Alignment.CenterVertically) {

                Text(text = stringResource(id = R.string.titulo_pantalla5),
                style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.pawsyColors.blanco
                )
                Image(
                    painter = painterResource(id = R.drawable.pawsy_paw),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(105f)
                        .padding(end = 20.dp, start = 30.dp)
                        .size(80.dp)
                )

            }


            Text(
                text = stringResource(id = R.string.subtitulo_nombreMascota),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(value = nombre, onValueChange = { nombre = it })

            Text(
                text = stringResource(id = R.string.subtitulo_especieMascota),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco
            )
            ExposedDropdownMenuBox(
                expanded = especieExpandible,
                onExpandedChange = { especieExpandible = !especieExpandible }
            ) {
                OutlinedTextField(
                    value = especie,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = especieExpandible) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
                        unfocusedContainerColor = MaterialTheme.pawsyColors.editTextAgregarMascota,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                        unfocusedTextColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                    )
                )

                ExposedDropdownMenu(
                    expanded = especieExpandible,
                    onDismissRequest = { especieExpandible = false }
                ) {
                    especieOpciones.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                especie = option
                                especieExpandible = false
                            }
                        )
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.subtitulo_edadMascota),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(
                value = edad,
                onValueChange = { newValue -> if (newValue.all { it.isDigit() }) edad = newValue },
                keyboardType = KeyboardType.Number
            )

            Text(
                text = stringResource(id = R.string.subtitulo_razaMascota),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco
            )
            PawsyTextField(value = raza, onValueChange = { raza = it })

            Text(
                text = stringResource(id = R.string.subtitulo_pesoMascota),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco

            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                PawsyTextField(
                    value = peso,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) peso = newValue
                    },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    Text(
                        text = unidadPeso,
                        color = MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                        modifier = Modifier
                            .clickable { unidadExpandible = true }
                            .background(
                                MaterialTheme.pawsyColors.editTextAgregarMascota, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    DropdownMenu(
                        expanded = unidadExpandible,
                        onDismissRequest = { unidadExpandible = false }
                    ) {
                        unidades.forEach { unidad ->
                            DropdownMenuItem(
                                text = { Text(unidad) },
                                onClick = {
                                    unidadPeso = unidad
                                    unidadExpandible = false
                                }
                            )
                        }
                    }
                }
            }


            OutlinedButton(
                onClick = {
                /* nothing yet */
                    navController.navigate("myPets")
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(50),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.pawsyColors.letraInicioApp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.pawsyColors.letraInicioApp
                )
            ) {
                Text(text = stringResource(id = R.string.boton_continuar))
            }
        }
        BasicCircleBotton (
            onClick = { },
            size = 50.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 20.dp,
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
    val navController = rememberNavController()
    MyText(navController)
}
