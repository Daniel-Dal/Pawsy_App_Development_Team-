package com.example.pawsy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import android.net.Uri
import com.example.pawsy.ui.components.ImagenPerfil
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import com.example.pawsy.guardarMascota
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

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


        val unidades = stringArrayResource(id = R.array.opciones_unidad_peso)
        val especieOpciones = remember { mutableStateListOf<String>() }
        var cargandoEspecies by remember { mutableStateOf(true) }
        var errorEspecies by remember { mutableStateOf(false) }

        var razaExpandible by remember { mutableStateOf(false) }
        val razaOpciones = remember { mutableStateListOf<String>() }
        var cargandoRazas by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        var guardando by remember { mutableStateOf(false) }

        LaunchedEffect(especie) {
            raza = ""
            razaOpciones.clear()

            if (especie.isNotBlank()) {
                cargandoRazas = true

                val db = FirebaseFirestore.getInstance()

                db.collection("especies")
                    .whereEqualTo("nombre", especie)
                    .get()
                    .addOnSuccessListener { especies ->
                        val idEspecie = especies.documents.firstOrNull()?.id

                        if (idEspecie != null) {
                            db.collection("razas")
                                .whereEqualTo("idespecie", idEspecie)
                                .get()
                                .addOnSuccessListener { razas ->
                                    razaOpciones.clear()
                                    razaOpciones.addAll(
                                        razas.documents.mapNotNull { documento ->
                                            documento.getString("nombre")
                                        }.sorted()
                                    )

                                    cargandoRazas = false

                                    Log.d(
                                        "PAWSY_RAZAS",
                                        "ID especie: $idEspecie; razas encontradas: ${razas.size()}"
                                    )
                                }
                                .addOnFailureListener { error ->
                                    Log.e("PAWSY_RAZAS", "Error consultando razas", error)
                                    cargandoRazas = false
                                }
                        } else {
                            Log.e("PAWSY_RAZAS", "No se encontró la especie: $especie")
                            cargandoRazas = false
                        }
                    }
                    .addOnFailureListener { error ->
                        Log.e("PAWSY_RAZAS", "Error consultando especies", error)
                        cargandoRazas = false
                    }
            }
        }


        LaunchedEffect(Unit) {
            FirebaseFirestore.getInstance()
                .collection("especies")
                .get()
                .addOnSuccessListener { resultado ->
                    especieOpciones.clear()

                    especieOpciones.addAll(
                        resultado.documents.mapNotNull { documento ->
                            documento.getString("nombre")
                        }.sorted()
                    )

                    cargandoEspecies = false
                    errorEspecies = false
                }
                .addOnFailureListener {
                    cargandoEspecies = false
                    errorEspecies = true
                }
        }

    Box(modifier = Modifier
        .fillMaxSize()
        ) {
        var imagenPerfil by remember { mutableStateOf<Uri?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.fondoAgregarMascota)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )  {
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
                onExpandedChange = {
                    especieExpandible = !especieExpandible
                }
            ) {
                OutlinedTextField(
                    value = especie,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = when {
                                cargandoEspecies -> "..."
                                errorEspecies -> "Error al cargar especies"
                                especieOpciones.isEmpty() -> "No hay especies disponibles"
                                else -> "Selecciona una especie"
                            },
                            color = MaterialTheme.pawsyColors.textEditTextAgregarMascota
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = especieExpandible
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor =
                            MaterialTheme.pawsyColors.editTextAgregarMascota,
                        unfocusedContainerColor =
                            MaterialTheme.pawsyColors.editTextAgregarMascota,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor =
                            MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                        unfocusedTextColor =
                            MaterialTheme.pawsyColors.textEditTextAgregarMascota
                    )
                )

                ExposedDropdownMenu(
                    expanded = especieExpandible,
                    onDismissRequest = {
                        especieExpandible = false
                    }
                ) {
                    when {
                        cargandoEspecies -> {
                            DropdownMenuItem(
                                text = { Text("Cargando especies...") },
                                onClick = {},
                                enabled = false
                            )
                        }

                        errorEspecies -> {
                            DropdownMenuItem(
                                text = { Text("No se pudieron cargar las especies") },
                                onClick = {},
                                enabled = false
                            )
                        }

                        especieOpciones.isEmpty() -> {
                            DropdownMenuItem(
                                text = { Text("No hay especies registradas") },
                                onClick = {},
                                enabled = false
                            )
                        }

                        else -> {
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
            ExposedDropdownMenuBox(
                expanded = razaExpandible,
                onExpandedChange = {
                    if (especie.isNotBlank() && !cargandoRazas) {
                        razaExpandible = !razaExpandible
                    }
                }
            ) {
                OutlinedTextField(
                    value = raza,
                    onValueChange = {},
                    readOnly = true,
                    enabled = especie.isNotBlank() && !cargandoRazas,
                    placeholder = {
                        Text(
                            when {
                                especie.isBlank() -> "Primero selecciona una especie"
                                cargandoRazas -> "Cargando razas..."
                                razaOpciones.isEmpty() -> "No hay razas disponibles"
                                else -> "Selecciona una raza"
                            }
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = razaExpandible
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = especie.isNotBlank() && !cargandoRazas
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor =
                            MaterialTheme.pawsyColors.editTextAgregarMascota,
                        unfocusedContainerColor =
                            MaterialTheme.pawsyColors.editTextAgregarMascota,
                        disabledContainerColor =
                            MaterialTheme.pawsyColors.editTextAgregarMascota,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedTextColor =
                            MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                        unfocusedTextColor =
                            MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                        disabledTextColor =
                            MaterialTheme.pawsyColors.textEditTextAgregarMascota
                    )
                )

                ExposedDropdownMenu(
                    expanded = razaExpandible,
                    onDismissRequest = { razaExpandible = false }
                ) {
                    razaOpciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                raza = opcion
                                razaExpandible = false
                            }
                        )
                    }
                }
            }

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

            Text(
                text = "Imagen perfil",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco,
                textAlign = TextAlign.Center
            )

            ImagenPerfil(
                imagenSeleccionada = imagenPerfil,
                onImagenSeleccionada = { uri ->
                    imagenPerfil = uri
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colorBorde = MaterialTheme.pawsyColors.letraInicioApp,
                colorTexto = Color.White,
                tamanio = 220.dp
            )


            OutlinedButton(
                onClick = {
                    if (nombre.isBlank()) {
                        Toast.makeText(
                            navController.context,
                            "Escribe el nombre de tu mascota.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@OutlinedButton
                    }

                    guardando = true

                    scope.launch {
                        try {
                            guardarMascota(
                                context = navController.context,
                                nombre = nombre,
                                especie = especie,
                                raza = raza,
                                edad = edad,
                                peso = peso,
                                imagenUri = imagenPerfil
                            )

                            Toast.makeText(
                                navController.context,
                                "Mascota guardada correctamente.",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigate("myPets")
                        } catch (e: Exception) {
                            Toast.makeText(
                                navController.context,
                                "No se pudo guardar la mascota: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            guardando = false
                        }
                    }
                },
                enabled = !guardando,
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
        BasicCircleBotton(
            onClick = { navController.navigate("myPets") },
            size = 50.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 645.dp,
                    start = 300.dp
                ),
            backgroundColor = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
        ) {
            Icon(
                painter = painterResource(R.drawable.return_arrow),
                contentDescription = "Mascota",
                modifier = Modifier
                    .size(40.dp)
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
