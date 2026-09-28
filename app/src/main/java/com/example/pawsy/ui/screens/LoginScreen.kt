package com.example.pawsy.ui.screens

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
import androidx.compose.material3.ripple
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.BorderStroke
import com.example.pawsy.ui.theme.pawsyColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues

@Composable
fun LoginScreen() {
    PawsyTheme {
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
              //  .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.pawsy_paw),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
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
                    delay(600) // espera a que el logo ya haya aparecido
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
                PawsyTextField(value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier
                        .padding(top = 230.dp)
                        .fillMaxWidth(0.8f)
                )
                Text(
                    text = stringResource(R.string.contrase_a),
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 290.dp, end = 150.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                PawsyTextField(value = contrasena,
                    onValueChange = { contrasena = it },
                    modifier = Modifier
                        .padding(top = 320.dp)
                        .fillMaxWidth(0.8f)
                )
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 320.dp)
                        .size(180.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                bounded = true,
                                radius = 30.dp
                            )
                        ) {

                        }
                )
                Text(
                    text = stringResource(R.string.olvidaste_tu_contrase_a),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 430.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .width(100.dp)
                        .align(Alignment.BottomCenter) // posición dentro del Box: TopStart, Center, BottomEnd, etc.
                        .padding(bottom = 35.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp), // ahora sí con .dp, radio fijo
                    border = BorderStroke(1.5.dp, MaterialTheme.pawsyColors.blanco),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.pawsyColors.blanco
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.boton_continuar),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Text(
                    text = "No tienes cuenta? Crear cuenta",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(top = 520.dp),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

