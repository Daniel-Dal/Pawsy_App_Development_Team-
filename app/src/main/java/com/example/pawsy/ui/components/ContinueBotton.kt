package com.example.pawsy.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ContinueBotton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color,
    borderColor: Color,
    borderWidth: Dp = 5.dp,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = borderWidth,
            color = borderColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor
        )
    ) {
        content()
    }
}