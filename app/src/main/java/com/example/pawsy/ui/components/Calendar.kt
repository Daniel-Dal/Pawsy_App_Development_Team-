package com.example.pawsy.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pawsy.ui.theme.colorEditTextAgregarMascota
import com.example.pawsy.ui.theme.colorTextEditTextAgregarMascota
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Calendario(
    fecha: String,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = colorEditTextAgregarMascota,
    textColor: Color = colorTextEditTextAgregarMascota
) {
    var mostrarCalendario by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                mostrarCalendario = true
            }
    ) {

        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),

            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = containerColor,
                disabledBorderColor = Color.Transparent,
                disabledTextColor = textColor
            )
        )
    }

    if (mostrarCalendario) {

        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        val millis = datePickerState.selectedDateMillis

                        if (millis != null) {

                            val fechaSeleccionada =
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(Date(millis))

                            onFechaSeleccionada(fechaSeleccionada)
                        }

                        mostrarCalendario = false
                    }
                ) {
                    Text("Aceptar")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}