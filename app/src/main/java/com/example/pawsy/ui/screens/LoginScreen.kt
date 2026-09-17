package com.example.pawsy.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.example.pawsy.ui.theme.colorRecuadroInicioSesion

@Composable
fun LoginScreen() {
    PawsyTheme {
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
                        color = colorRecuadroInicioSesion,
                        shape = RoundedCornerShape(
                            topStart = 35.dp,
                            topEnd = 35.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            ) {

                Image(
                    painter = painterResource(id = R.drawable.pawsy_doggy),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
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
            }
        }
    }
}