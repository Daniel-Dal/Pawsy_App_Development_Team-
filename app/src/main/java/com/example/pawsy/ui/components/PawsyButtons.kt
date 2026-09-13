package com.example.pawsy.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorBlanco

@Composable
fun PawsyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    fontSize: TextUnit =TextUnit.Unspecified,
    containerColor: Color = colorEditTextAgregarMascota,
    contentColor: Color = colorBlanco
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
        Text(text = text, fontSize = fontSize)
    }
}