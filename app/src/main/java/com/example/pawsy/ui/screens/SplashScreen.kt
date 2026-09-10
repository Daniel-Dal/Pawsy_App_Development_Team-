package com.example.pawsy.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.pawsy.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.example.pawsy.ui.theme.PawsyTheme
import androidx.compose.ui.text.style.TextAlign
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.DisposableEffect
import kotlin.random.Random

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) //
        onFinished()
    }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val soundToPlay = if (Random.nextBoolean()) {
            R.raw.splash_guau
        } else {
            R.raw.splash_meow
        }

        val mediaPlayer = MediaPlayer.create(context, soundToPlay)
        mediaPlayer.start()

        onDispose {
            mediaPlayer.release()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F0)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.pawsy_logo),
            contentDescription = "Logo de la app"
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
        PawsyTheme {
            Text(
                text = stringResource(R.string.tu_mascota_siempre_en_buenas_patas),
                modifier = Modifier.alpha(textAlpha.value),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color(0xFFFE854F)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onFinished = {})
}