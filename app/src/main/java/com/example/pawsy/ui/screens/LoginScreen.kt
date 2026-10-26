package com.example.pawsy.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pawsy.ui.components.PawsyTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.BorderStroke
import com.example.pawsy.ui.theme.pawsyColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.time.Duration.Companion.milliseconds
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun LoginScreen(navController: NavController) {
    PawsyTheme {
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }

        // 2. Obtener el contexto
        val context = LocalContext.current
        val scope = rememberCoroutineScope()




        var mostrarDialogoRecuperacion by remember { mutableStateOf(false) }
        var correoRecuperacion by remember { mutableStateOf("") }
        var enviandoRecuperacion by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.pawsy_paw),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 20.dp)
                    .align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Bienvenido a",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.pawsy),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.primary
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = 35.dp,
                            topEnd = 35.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                contentAlignment = Alignment.TopCenter

            ) {

                Image(
                    painter = painterResource(id = R.drawable.pawsy_doggy),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val textAlpha = remember { Animatable(0f) }

                LaunchedEffect(Unit) {
                    delay(600.milliseconds)// espera a que el logo ya haya aparecido
                    textAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 500)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 130.dp, start = 60.dp, end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.iniciar_sesi_n),
                        modifier = Modifier.alpha(textAlpha.value),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.pawsy_paw_white),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.correo),
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 200.dp, end = 200.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                PawsyTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier
                        .padding(top = 230.dp)
                        .fillMaxWidth(0.8f),
                    singleLine = true,
                    keyboardType = KeyboardType.Email
                )
                Text(
                    text = stringResource(R.string.contrase_a),
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 290.dp, end = 150.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                PawsyTextField(
                    value = contrasena,
                    onValueChange = { nuevoTexto ->
                        contrasena = nuevoTexto.filter { !it.isWhitespace() }
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 320.dp)
                        .fillMaxWidth(0.8f),
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

                Image(

                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 320.dp)
                        .size(180.dp)
                        .clickable(

                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                try {
                                    val idToken = obtenerTokenGoogle(context)
                                    val auth = FirebaseAuth.getInstance()
                                    val firebaseCredential =
                                        GoogleAuthProvider.getCredential(idToken, null)

                                    auth.signInWithCredential(firebaseCredential)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val usuario = auth.currentUser
                                                val esNuevoUsuario =
                                                    task.result?.additionalUserInfo?.isNewUser == true

                                                Toast.makeText(
                                                    context,
                                                    "Bienvenido ${usuario?.displayName ?: usuario?.email}",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                if (esNuevoUsuario) {
                                                    // Primera vez: completar los datos del usuario
                                                    navController.navigate("addDataUser") {
                                                        popUpTo("login") {
                                                            inclusive = true
                                                        }
                                                    }
                                                } else {
                                                    // Usuario existente: ir a Mis Mascotas
                                                    navController.navigate("myPets") {
                                                        popUpTo("login") {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    task.exception?.localizedMessage
                                                        ?: "No se pudo iniciar sesión",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        e.localizedMessage ?: "No se pudo seleccionar la cuenta",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                )
                Text(
                    text = stringResource(R.string.olvidaste_tu_contrase_a),
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 430.dp)
                        .clickable {
                            mostrarDialogoRecuperacion = true
                        },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )

                OutlinedButton(
                    onClick = {
                        val correoIngresado = correo.trim()
                        val contrasenaIngresada = contrasena
                        val db = FirebaseFirestore.getInstance()

                        when {
                            // Ambos campos están vacíos
                            correoIngresado.isBlank() &&
                                    contrasenaIngresada.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    "Ingresa tu correo y contraseña",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // Falta el correo
                            correoIngresado.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    "Falta el correo electrónico",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // Falta la contraseña
                            contrasenaIngresada.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    "Falta la contraseña",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // El formato del correo es incorrecto
                            !android.util.Patterns.EMAIL_ADDRESS
                                .matcher(correoIngresado)
                                .matches() -> {
                                Toast.makeText(
                                    context,
                                    "El correo electrónico no es válido",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // Consultar si el correo existe en Firestore
                            else -> {
                                db.collection("usuarios")
                                    .whereEqualTo("correo", correoIngresado)
                                    .get()
                                    .addOnSuccessListener { resultado ->
                                        if (resultado.isEmpty) {
                                            Toast.makeText(
                                                context,
                                                "No encontramos una cuenta con ese correo",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Correo encontrado",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Aquí continúa la autenticación.
                                            // Todavía falta verificar la contraseña.
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "No pudimos comprobar el correo. Inténtalo de nuevo.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 35.dp),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.5.dp,
                        MaterialTheme.pawsyColors.blanco
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.pawsyColors.blanco
                    )
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.boton_continuar
                        ),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Text(
                    text = "No tienes cuenta? Crear cuenta",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 520.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            navController.navigate("createUser")
                        },
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Cuadro de recuperación de contraseña

        if (mostrarDialogoRecuperacion) {
            AlertDialog(
                onDismissRequest = {
                    if (!enviandoRecuperacion) {
                        mostrarDialogoRecuperacion = false
                    }
                },
                title = {
                    Text(
                        text = "Recuperar contraseña",
                        style = MaterialTheme.typography.titleMedium
                    )
                },

                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Ingresa el correo de tu cuenta. " +
                                    "Te enviaremos un enlace para crear una nueva contraseña.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        OutlinedTextField(
                            value = correoRecuperacion,
                            onValueChange = { correoRecuperacion = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = "Correo electrónico",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "ejemplo@correo.com",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            singleLine = true,
                            enabled = !enviandoRecuperacion,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            )
                        )
                    }
                }
                ,
                confirmButton = {
                    TextButton(
                        enabled = correoRecuperacion.isNotBlank() &&
                                !enviandoRecuperacion,
                        onClick = {
                            val correo = correoRecuperacion.trim()
                            val auth = FirebaseAuth.getInstance()
                            val db = FirebaseFirestore.getInstance()
                            if (!android.util.Patterns.EMAIL_ADDRESS
                                    .matcher(correo).matches()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Ingresa un correo válido",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                enviandoRecuperacion = true

                                // Primero comprobar el correo en Firestore
                                db.collection("usuarios")
                                    .whereEqualTo("correo", correo)
                                    .get()
                                    .addOnSuccessListener { resultado ->

                                        if (resultado.isEmpty) {
                                            enviandoRecuperacion = false

                                            Toast.makeText(
                                                context,
                                                "No encontramos ese correo en usuarios",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            // Si existe en Firestore,
                                            // solicitar el correo de recuperación
                                            auth.sendPasswordResetEmail(correo)
                                                .addOnCompleteListener { task ->
                                                    enviandoRecuperacion = false

                                                    if (task.isSuccessful) {
                                                        Toast.makeText(
                                                            context,
                                                            "Si la cuenta está habilitada " +
                                                                    "en Firebase Authentication, " +
                                                                    "recibirás el enlace.",
                                                            Toast.LENGTH_LONG
                                                        ).show()

                                                        mostrarDialogoRecuperacion = false
                                                        correoRecuperacion = ""
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "No se pudo enviar el correo. " +
                                                                    "Verifica la cuenta e inténtalo de nuevo.",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                        }
                                    }
                                    .addOnFailureListener {
                                        enviandoRecuperacion = false

                                        Toast.makeText(
                                            context,
                                            "No pudimos consultar el correo en la base de datos",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        }
                    ) {
                        if (enviandoRecuperacion) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Enviar",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        enabled = !enviandoRecuperacion,
                        onClick = {
                            mostrarDialogoRecuperacion = false
                        }
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        }


    }
}

private suspend fun obtenerTokenGoogle(context: Context): String {
    val credentialManager = CredentialManager.create(context)

    fun crearSolicitud(soloAutorizadas: Boolean): GetCredentialRequest {
        val googleOption = GetGoogleIdOption.Builder()
            .setServerClientId(
                context.getString(R.string.default_web_client_id)
            )
            .setFilterByAuthorizedAccounts(soloAutorizadas)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()
    }

    val resultado = try {
        credentialManager.getCredential(
            context = context,
            request = crearSolicitud(true)
        )
    } catch (e: NoCredentialException) {
        // Si no hay cuentas autorizadas, permitir elegir otra.
        credentialManager.getCredential(
            context = context,
            request = crearSolicitud(false)
        )
    }

    val credential = resultado.credential

    if (
        credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        return GoogleIdTokenCredential
            .createFrom(credential.data)
            .idToken
    }

    throw IllegalStateException(
        "No se recibió una credencial válida de Google."
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()

    LoginScreen(navController = navController)
}
