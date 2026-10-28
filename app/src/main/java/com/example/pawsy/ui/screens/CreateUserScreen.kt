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

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import androidx.compose.foundation.layout.imePadding

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction


import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect


import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateUserScreen(navController: NavController) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.fondoPerfil)
            .imePadding()
    ){
        var nombre by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var aceptado by remember { mutableStateOf(false) }

        var mostrarTerminos by remember { mutableStateOf(false) }
        var llegoAlFinal by remember { mutableStateOf(false) }

        val scrollState = rememberScrollState()
        BasicCircleBotton (
            onClick = {navController.navigate("menu") },
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
            painter = painterResource(id = R.drawable.pawsy_paw),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(85.dp)
                .align(Alignment.TopEnd)
        )

        Text(
            text = stringResource(R.string.pawsy),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 100.dp
                ),
            fontSize = 60.sp,
            style = MaterialTheme.typography.titleSmall,
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
        ){


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
                painter = painterResource(id = R.drawable.pawsy_paw),
                contentDescription = "Mascotas",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 54.dp,
                        end = 40.dp
                    )
                    .size(50.dp),
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
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                singleLine = true,
                keyboardType = KeyboardType.Email
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
                textColor =MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                singleLine = true,
                keyboardType = KeyboardType.Email
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
                onValueChange = { nuevoTexto ->
                    contrasena = nuevoTexto.filter { !it.isWhitespace() }
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 350.dp)
                    .width(330.dp),
                containerColor = MaterialTheme.pawsyColors.fondoAgregarMascota,
                textColor = MaterialTheme.pawsyColors.textEditTextAgregarMascota,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
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
                    onCheckedChange = { marcado ->
                        if (marcado && !aceptado) {
                            mostrarTerminos = true
                            llegoAlFinal = false
                        } else if (!marcado) {
                            aceptado = false
                        }
                    },
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
                    val nombreIngresado = nombre.trim()
                    val correoIngresado = correo.trim()

                    when {
                        nombreIngresado.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Ingresa tu nombre",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        correoIngresado.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Ingresa tu correo electrónico",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(correoIngresado)
                            .matches() -> {
                            Toast.makeText(
                                context,
                                "Ingresa un correo electrónico válido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        contrasena.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Ingresa tu contraseña",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        contrasena.length < 8 -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener mínimo 8 caracteres",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !contrasena.any { it.isUpperCase() } -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener una mayúscula",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !contrasena.any { it.isDigit() } -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener al menos un número",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !contrasena.any {
                            !it.isLetterOrDigit() && !it.isWhitespace()
                        } -> {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener un carácter especial, por ejemplo @ o #",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        !aceptado -> {
                            Toast.makeText(
                                context,
                                "Debes aceptar los términos y condiciones",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                        else -> {
                            val auth = FirebaseAuth.getInstance()

                            auth.createUserWithEmailAndPassword(
                                correo.trim(),
                                contrasena
                            ).addOnCompleteListener registro@{ task ->

                                if (!task.isSuccessful) {
                                    val error = task.exception

                                    val mensaje = when (error) {
                                        is FirebaseAuthUserCollisionException ->
                                            "Este correo ya está registrado. Utiliza otro correo."

                                        is FirebaseAuthInvalidCredentialsException ->
                                            "El correo electrónico no es válido."

                                        else ->
                                            "No se pudo crear la cuenta. Inténtalo nuevamente."
                                    }

                                    Toast.makeText(
                                        context,
                                        mensaje,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    return@registro
                                }

                                val usuario = auth.currentUser

                                if (usuario == null) {
                                    Toast.makeText(
                                        context,
                                        "No se pudo obtener la información de la cuenta.",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    return@registro
                                }

                                val perfil = com.google.firebase.auth.UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(nombreIngresado)
                                    .build()

                                usuario.updateProfile(perfil)
                                    .addOnCompleteListener actualizacion@{ resultado ->

                                        if (!resultado.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "La cuenta se creó, pero no se pudo guardar el nombre del perfil.",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            return@actualizacion
                                        }

                                        usuario.sendEmailVerification()
                                            .addOnCompleteListener verificacion@{ envio ->

                                                if (envio.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Cuenta creada. Revisa tu correo para verificarla.",
                                                        Toast.LENGTH_LONG
                                                    ).show()

                                                    navController.navigate("VerifyEmail")
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "La cuenta se creó, pero no se pudo enviar el correo. Intenta reenviar la verificación.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    }
                            }
                        }

                    }
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
        if (mostrarTerminos) {

        LaunchedEffect(
            scrollState.value,
            scrollState.maxValue
        ) {
            if (
                scrollState.maxValue > 0 &&
                scrollState.value >= scrollState.maxValue
            ) {
                llegoAlFinal = true
            }
        }

        AlertDialog(
            onDismissRequest = {
                mostrarTerminos = false
            },
            title = {
                Text(
                    text = "Términos y condiciones"

                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .height(350.dp)
                        .verticalScroll(scrollState)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = """
                        TÉRMINOS Y CONDICIONES DE PAWSY

                        1. Aceptación
                        Al utilizar Pawsy, aceptas estos términos y condiciones.

                        2. Uso de la aplicación
                        El usuario se compromete a proporcionar información
                        correcta y utilizar la aplicación de forma responsable.

                        3. Registro de usuario
                        El usuario es responsable de mantener la seguridad
                        de su cuenta y de la información que proporciona.

                        4. Protección de datos
                        La información personal debe tratarse de acuerdo
                        con la política de privacidad de Pawsy y la normativa
                        aplicable.

                        5. Responsabilidades
                        El usuario debe utilizar las funcionalidades de
                        Pawsy respetando las reglas de la plataforma.

                        6. Modificaciones
                        Pawsy podrá actualizar estos términos cuando sea
                        necesario, informando los cambios según corresponda.

                        7. Aceptación final
                        Al llegar al final de este documento, podrás
                        seleccionar el botón Aceptar para confirmar que
                        has leído los términos y condiciones.
                    """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (llegoAlFinal) {
                            "Has llegado al final de los términos."
                        } else {
                            "Desplázate hasta el final para habilitar la aceptación."
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = llegoAlFinal,
                    onClick = {
                        aceptado = true
                        mostrarTerminos = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarTerminos = false
                        llegoAlFinal = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
        Image(
            painter = painterResource(R.drawable.dog_and_cat),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 480.dp)
                .size(180.dp)
        )

        }


}


@Preview(showBackground = true)
@Composable
fun CreateUserScreenPreview() {
    val navController = rememberNavController()
    CreateUserScreen(navController)
}