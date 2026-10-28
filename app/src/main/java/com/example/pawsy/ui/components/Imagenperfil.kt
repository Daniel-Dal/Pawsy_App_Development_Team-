package com.example.pawsy.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pawsy.ui.theme.pawsyColors

@Composable
fun ImagenPerfil(
    imagenSeleccionada: Uri?,
    onImagenSeleccionada: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
    calendarColor: Color = MaterialTheme.pawsyColors.fondoAgregarMascota,
    colorBorde: Color = Color(0xFFBFE3FF),
    colorTexto: Color = Color.White,
    tamanio: Dp = 220.dp
) {

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImagenSeleccionada(uri)
    }

    val forma = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(tamanio)
            .clip(forma)
            .background(calendarColor)
            .border(
                width = 1.5.dp,
                color = colorBorde,
                shape = forma
            )
            .clickable {
                selectorImagen.launch("image/*")
            },
        contentAlignment = Alignment.Center
    ) {

        if (imagenSeleccionada != null) {

            AsyncImage(
                model = imagenSeleccionada,
                contentDescription = "Imagen de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Distintivo para indicar que se puede cambiar la foto
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(calendarColor.copy(alpha = 0.9f))
                    .border(1.5.dp, colorBorde, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Cambiar foto",
                    tint = colorTexto,
                    modifier = Modifier.size(20.dp)
                )
            }

        } else {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(colorTexto.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PhotoCamera,
                        contentDescription = null,
                        tint = colorTexto,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Agregar foto",
                    color = colorTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Una foto hace su perfil especial",
                    color = colorTexto.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}