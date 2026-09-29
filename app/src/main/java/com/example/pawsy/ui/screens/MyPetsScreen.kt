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
import com.example.pawsy.ui.theme.colorPataSeleccionMascota
import com.example.pawsy.model.Mascota
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pawsy.ui.theme.pawsyColors


@Composable
fun MyPetsScreen(navController: NavController) {
    val mascotas = listOf(
        Mascota("1", "Max", R.drawable.perro1),
        Mascota("2", "Luna", R.drawable.gato),
        Mascota("3", "Rocky", R.drawable.perro2)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.pawsyColors.fondoSeleccionMascotaApp)
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
            backgroundColor = MaterialTheme.pawsyColors.fondoAgregarMascota
        ) {
            Icon(
                painter = painterResource(R.drawable.return_arrow),
                contentDescription = "Regreso",
                modifier = Modifier.size(40.dp)
                    .offset(x = (-5).dp),
                tint = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
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
            color = MaterialTheme.pawsyColors.blanco
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
            color = MaterialTheme.pawsyColors.blanco
        )
        Image(
            painter = painterResource(id = R.drawable.pawsy_paw_white),
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
                        navController.navigate("petProfile")
                    },
                    size = 120.dp,
                    backgroundColor = MaterialTheme.pawsyColors.blanco
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
                        navController.navigate("addPet")
                    },
                    size = 120.dp,
                    backgroundColor = MaterialTheme.pawsyColors.fondoAgregarMascota
                ) {
                    Text(
                        text = "+",
                        fontSize = 60.sp,
                        color = MaterialTheme.pawsyColors.fondoSeleccionMascotaApp
                    )
                }
            }
        }



    }
}


@Preview(showBackground = true)
@Composable
fun MyPetsScreenPreview() {
    val navController = rememberNavController()
    MyPetsScreen(navController)
}