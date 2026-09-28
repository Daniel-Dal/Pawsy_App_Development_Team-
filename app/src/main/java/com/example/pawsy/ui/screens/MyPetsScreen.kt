package com.example.pawsy.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
import com.example.pawsy.ui.theme.colorPataSeleccionMascota
import com.example.pawsy.model.Mascota
import androidx.compose.foundation.lazy.grid.items




@Composable
fun MyPetsScreen() {
    val mascotas = listOf(
        Mascota("1", "Max", R.drawable.perro1),
        Mascota("2", "Luna", R.drawable.gato),
        Mascota("3", "Rocky", R.drawable.perro2)
    )
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
        Image(
            imageVector = Icons.Filled.Pets,
            contentDescription = "Mascotas",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 180.dp,
                    end = 40.dp
                )
                .size(80.dp),
            colorFilter = ColorFilter.tint(colorPataSeleccionMascota)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 300.dp,
                    start = 30.dp,
                    end = 30.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            items(mascotas) { mascota ->

                BasicCircleBotton(
                    onClick = {
                        // Abrir perfil de la mascota
                    },
                    size = 120.dp,
                    backgroundColor = colorBlanco
                ) {
                    Image(
                        painter = painterResource(mascota.imagen),
                        contentDescription = mascota.nombre,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Botón +
            item {
                BasicCircleBotton(
                    onClick = {
                        // Ir a agregar mascota
                    },
                    size = 120.dp,
                    backgroundColor = colorFondoAgregarMascota
                ) {
                    Text(
                        text = "+",
                        fontSize = 60.sp,
                        color = colorFondoSeleccionMascotaApp
                    )
                }
            }
        }



    }
}


@Preview(showBackground = true)
@Composable
fun MyPetsScreenPreview() {
    MyPetsScreen()
}