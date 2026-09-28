package com.example.pawsy.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.example.pawsy.ui.theme.pawsyColors

@Composable
fun PawsyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    containerColor: Color = MaterialTheme.pawsyColors.editTextAgregarMascota,
    contentColor: Color = MaterialTheme.pawsyColors.blanco
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
    }
}