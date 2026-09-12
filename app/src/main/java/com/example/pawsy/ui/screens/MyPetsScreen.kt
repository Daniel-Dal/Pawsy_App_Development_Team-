package com.example.pawsy.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawsy.R
import com.example.pawsy.ui.components.BasicCircleBotton

import com.example.pawsy.ui.theme.PawsyFontFamily

import com.example.pawsy.ui.theme.colorBlanco
import com.example.pawsy.ui.theme.colorFondoAgregarMascota

import com.example.pawsy.ui.theme.colorFondoSeleccionMascotaApp



@Composable
fun MyPetsScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondoSeleccionMascotaApp)
    ) {

        BasicCircleBotton (
            onClick = { },
            size = 60.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 30.dp,
                    start = 20.dp
                ),
            backgroundColor = colorFondoAgregarMascota
        ) {
            Icon(
                painter = painterResource(R.drawable.return_arrow),
                contentDescription = "Regreso",
                modifier = Modifier.size(40.dp)
                    .offset(x = (-5).dp),
                tint = colorFondoSeleccionMascotaApp
            )
        }

        Text(
            text = "Tus ",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 150.dp,
                    start = 30.dp
                ),
            fontSize = 50.sp,
            fontFamily = PawsyFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = colorBlanco
        )

        Text(
            text = "Mascotas",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 200.dp,
                    start = 30.dp
                ),
            fontSize = 50.sp,
            fontFamily = PawsyFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = colorBlanco
        )




    }
}


@Preview(showBackground = true)
@Composable
fun MyPetsScreenPreview() {
    MyPetsScreen()
}