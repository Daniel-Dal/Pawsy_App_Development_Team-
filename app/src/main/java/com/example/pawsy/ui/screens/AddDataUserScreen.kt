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
import com.example.pawsy.ui.theme.pawsyColors
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.pawsy.ui.components.ImagenPerfil
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.runtime.rememberCoroutineScope
import com.example.pawsy.subirImagen
import kotlinx.coroutines.launch

@Composable
fun DataUser(navController: NavController) {
    PawsyTheme {

        var fechaNacimiento by remember { mutableStateOf("") }
        var imagenPerfil by remember { mutableStateOf<Uri?>(null) }
        var guardando by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.pawsyColors.recuadroInicioSesion)
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Agregar datos nuevo usuario",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(R.drawable.persona),
                contentDescription = null,
                modifier = Modifier
                    .size(230.dp)
                    .align(Alignment.CenterHorizontally)

            )

            Text(
                text = "Fecha de nacimiento",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.pawsyColors.blanco,
                modifier = Modifier.padding(top = 20.dp)
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
                    .padding(top = 20.dp),
                    textAlign = TextAlign.Center
            )
            ImagenPerfil(
                imagenSeleccionada = imagenPerfil,
                onImagenSeleccionada = { uri ->
                    imagenPerfil = uri
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colorBorde = MaterialTheme.pawsyColors.blanco,
                tamanio = 120.dp
            )


            ContinueBotton(
                onClick = {
                    if (fechaNacimiento.isBlank()) {
                        Toast.makeText(
                            navController.context,
                            "Selecciona tu fecha de nacimiento.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@ContinueBotton
                    }

                    val formatoFecha = SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).apply {
                        isLenient = false
                        timeZone = TimeZone.getTimeZone("America/Bogota")
                    }

                    val fechaDate: Date? = try {
                        formatoFecha.parse(fechaNacimiento)
                    } catch (e: Exception) {
                        null
                    }

                    if (fechaDate == null) {
                        Toast.makeText(
                            navController.context,
                            "La fecha de nacimiento no es válida.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@ContinueBotton
                    }

                    val usuario = FirebaseAuth.getInstance().currentUser

                    if (usuario == null) {
                        Toast.makeText(
                            navController.context,
                            "Debes crear una cuenta o iniciar sesión.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@ContinueBotton
                    }

                    guardando = true

                    scope.launch {
                        try {
                            val urlImagen = if (imagenPerfil != null) {
                                subirImagen(
                                    context = navController.context,
                                    imagenUri = imagenPerfil!!
                                )
                            } else {
                                ""
                            }

                            val datosUsuario = hashMapOf(
                                "nombre" to (usuario.displayName ?: ""),
                                "correo" to (usuario.email ?: ""),
                                "fecha_nacimiento" to Timestamp(fechaDate),
                                "fecha_registro" to Timestamp.now(),
                                "urlimagen" to urlImagen
                            )

                            FirebaseFirestore.getInstance()
                                .collection("usuarios")
                                .document(usuario.uid)
                                .set(datosUsuario)
                                .addOnSuccessListener {
                                    guardando = false

                                    Toast.makeText(
                                        navController.context,
                                        "Datos guardados correctamente.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("addPet")
                                }
                                .addOnFailureListener { error ->
                                    guardando = false

                                    Toast.makeText(
                                        navController.context,
                                        "No se pudieron guardar los datos: ${error.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } catch (e: Exception) {
                            guardando = false

                            Toast.makeText(
                                navController.context,
                                "No se pudo subir la imagen: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally),
                textColor = MaterialTheme.pawsyColors.blanco,
                borderColor = MaterialTheme.pawsyColors.blanco,
                borderWidth = 1.dp
            ) {
                Text(text = stringResource(R.string.boton_continuar))
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
