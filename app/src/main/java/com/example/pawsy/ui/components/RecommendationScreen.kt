package com.example.pawsy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pawsy.R
import com.example.pawsy.ui.theme.PawsyTheme
import com.example.pawsy.ui.theme.colorFondoPerfil
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorTextEditTextAgregarMascota


@Composable
fun MyRecommText() {
    PawsyTheme {
        Box(                  // <- Box is now the layout structure, replacing your old top-level Column
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize().background(colorEditTextAgregarMascota))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(colorFondoPerfil)

            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {

                Text(text = stringResource(id = R.string.titulo_pantalla9),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 35.dp),
                    color = colorBlanco
                )

                Text(text = stringResource(id = R.string.subtitulo_razaRecomm),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorTextEditTextAgregarMascota
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
fun MyRecommTextPreview(){
    MyRecommText()
}