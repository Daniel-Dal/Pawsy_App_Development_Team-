
package com.example.pawsy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

import com.example.pawsy.ui.theme.pawsyColors
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign


import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.pawsy.R
import com.example.pawsy.ui.components.ContinueBotton

@Composable
fun VerifyEmailScreen(navController: NavController) {
    val context = LocalContext.current

    VerifyEmailContent(
        onVerifyClick = {
            val auth = FirebaseAuth.getInstance()
            val usuario = auth.currentUser

            if (usuario == null) {
                Toast.makeText(
                    context,
                    "Inicia sesión para continuar.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                usuario.reload().addOnCompleteListener { recarga ->
                    if (!recarga.isSuccessful) {
                        Toast.makeText(
                            context,
                            "No se pudo comprobar la verificación.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (auth.currentUser?.isEmailVerified == true) {
                        navController.navigate("AddDataUser") {
                            popUpTo("VerifyEmail") {
                                inclusive = true
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Tu correo aún no está verificado.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    )
}

@Composable
private fun VerifyEmailContent(
    onVerifyClick: () -> Unit
) {

    val context = LocalContext.current

    val onResendEmailClick: () -> Unit = {
        val usuario = FirebaseAuth.getInstance().currentUser

        if (usuario != null && !usuario.isEmailVerified) {
            usuario.sendEmailVerification()
                .addOnSuccessListener {
                    Toast.makeText(
                        context,
                        "Correo de confirmación reenviado",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(
                        context,
                        error.localizedMessage
                            ?: "No se pudo reenviar el correo",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            Toast.makeText(
                context,
                "No hay una cuenta pendiente de verificación",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.recuadroInicioSesion)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.dogs_and_cats),
            contentDescription = "Perros y gatos",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .height(500.dp)
        )

        // Texto debajo de la ilustración
        Text(
            text = "Verifica tu correo electrónico",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center

        )

        Text(
            text = "Revisa tu bandeja de Spam y abre el enlace que te enviamos para verificar tu cuenta.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 5.dp)
        )

        // Botón debajo del texto
        ContinueBotton(
            onClick = onVerifyClick,
            modifier = Modifier.padding(top = 15.dp),
            textColor = MaterialTheme.pawsyColors.blanco,
            borderColor = MaterialTheme.pawsyColors.blanco,
            borderWidth = 2.dp
        ) {
            Text("Ya verifiqué mi correo")
        }

        ContinueBotton(
            onClick = onResendEmailClick,
            modifier = Modifier.padding(top = 15.dp),
            textColor = MaterialTheme.pawsyColors.blanco,
            borderColor = MaterialTheme.pawsyColors.blanco,
            borderWidth = 1.dp
        ) {
            Text(
                text = "Reenviar correo de confirmación",
                fontSize = 10.sp
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun VerifyEmailScreenPreview() {
    VerifyEmailContent(onVerifyClick = {})
}
