package com.example.pawsy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorTextEditTextAgregarMascota
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringArrayResource


@Composable
fun MyRecommText() {
    PawsyTheme {
        val razaEjemplo = stringResource(id = R.string.raza_ejemplo)
        val recomendaciones = stringArrayResource(id = R.array.recomendaciones_ejemplo)

        Box(
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(id = R.string.titulo_pantalla9),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorBlanco
                    )

                    Image(
                        painter = painterResource(id = R.drawable.pawsy_paw_white),
                        contentDescription = null,
                        modifier = Modifier
                            .rotate(105f)
                            .offset(y = (-10).dp, x = (-10).dp)
                            .size(80.dp)

                    )

                }


                Text(
                    text = stringResource(R.string.subtitulo_razaRecomm, razaEjemplo),
                    style = MaterialTheme.typography.labelSmall,
                    color = colorTextEditTextAgregarMascota
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // takes all the remaining space below the subtitle
                        .clip(RoundedCornerShape(24.dp))
                        .background(colorEditTextAgregarMascota)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(50.dp)
                    ) {
                        recomendaciones.forEach { recomendacion ->
                            Text(
                                text = recomendacion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = colorBlanco
                            )
                        }
                    }
                }
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