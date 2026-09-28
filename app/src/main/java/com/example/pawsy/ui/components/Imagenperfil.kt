package com.example.pawsy.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorBlanco

@Composable
fun ImagenPerfil(
    imagenSeleccionada: Uri?,
    onImagenSeleccionada: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImagenSeleccionada(uri)
    }

    Box(
        modifier = modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(colorEditTextAgregarMascota)
            .clickable {
                selectorImagen.launch("image/*")
            },
        contentAlignment = Alignment.Center
    ) {

        if (imagenSeleccionada != null) {

            AsyncImage(
                model = imagenSeleccionada,
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        } else {

            Text(
                text = "Agregar\nimagen",
                color = colorBlanco,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}