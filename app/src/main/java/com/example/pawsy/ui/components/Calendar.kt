package com.example.pawsy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
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
import com.example.pawsy.ui.theme.pawsyColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun Calendario(
    fecha: String,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.pawsyColors.editTextAgregarMascota,
    calendarColor: Color = MaterialTheme.pawsyColors.editTextAgregarMascota
) {
    var mostrarCalendario by remember {
        mutableStateOf(false)
    }

    // Última fecha permitida: hoy menos 10 años.
    val fechaMaxima = remember {
        Calendar.getInstance().apply {
            add(Calendar.YEAR, -10)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // Impide seleccionar fechas posteriores al límite.
    val fechasPermitidas = remember(fechaMaxima) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= fechaMaxima
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = fechasPermitidas
    )

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
                disabledContainerColor = calendarColor,
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

                        if (millis != null && millis <= fechaMaxima) {
                            val fechaSeleccionada =
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(Date(millis))

                            onFechaSeleccionada(fechaSeleccionada)
                        }

                        mostrarCalendario = false
                    },
                    enabled = datePickerState.selectedDateMillis
                        ?.let { it <= fechaMaxima } == true
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
