package com.example.pawsy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pawsy.model.FechaEvento
import com.example.pawsy.model.MesAnio
import com.example.pawsy.ui.theme.pawsyColors

private val LETRAS_SEMANA = listOf("L", "M", "M", "J", "V", "S", "D")

@Composable
fun CalendarioMensual(
    mes: MesAnio,
    seleccionado: FechaEvento,
    diasConEventos: Set<FechaEvento>,
    onMesAnterior: () -> Unit,
    onMesSiguiente: () -> Unit,
    onDiaClick: (FechaEvento) -> Unit,
    modifier: Modifier = Modifier,
    encabezadoExtra: @Composable () -> Unit = {}
) {
    val colors = MaterialTheme.pawsyColors
    val hoy = FechaEvento.hoy()
    val diasDelMes = mes.diasEnMes
    val desfase = mes.desfaseLunes
    val filas = (desfase + diasDelMes + 6) / 7

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.recuadroInicioSesion)
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        // Mes y año, flechas y espacio extra
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mes.titulo,
                color = colors.blanco,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onMesAnterior, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Mes anterior",
                    tint = colors.blanco
                )
            }
            IconButton(onClick = onMesSiguiente, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Mes siguiente",
                    tint = colors.blanco
                )
            }
            Spacer(modifier = Modifier.size(6.dp))
            encabezadoExtra()
            Spacer(modifier = Modifier.size(6.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Cabecera: L M M J V S D
        Row(modifier = Modifier.fillMaxWidth()) {
            LETRAS_SEMANA.forEach { letra ->
                Text(
                    text = letra,
                    color = colors.blanco.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Cuadrícula de días
        for (fila in 0 until filas) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (columna in 0 until 7) {
                    val numero = fila * 7 + columna - desfase + 1
                    if (numero in 1..diasDelMes) {
                        val fecha = mes.dia(numero)
                        DiaCelda(
                            numero = numero,
                            esSeleccionado = fecha == seleccionado,
                            esHoy = fecha == hoy,
                            tieneEventos = fecha in diasConEventos,
                            onClick = { onDiaClick(fecha) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f).height(CELDA_ALTO))
                    }
                }
            }
        }
    }
}

private val CELDA_ALTO = 38.dp

@Composable
private fun DiaCelda(
    numero: Int,
    esSeleccionado: Boolean,
    esHoy: Boolean,
    tieneEventos: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.pawsyColors

    Box(
        modifier = modifier.height(CELDA_ALTO),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .then(
                    when {
                        esSeleccionado -> Modifier.background(colors.blanco)
                        esHoy -> Modifier.border(1.5.dp, colors.blanco, CircleShape)
                        else -> Modifier
                    }
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = numero.toString(),
                color = if (esSeleccionado) colors.recuadroInicioSesion else colors.blanco,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (esSeleccionado || esHoy) FontWeight.Bold else FontWeight.Normal
            )

            if (tieneEventos) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 3.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (esSeleccionado) colors.recuadroInicioSesion else colors.blanco
                        )
                )
            }
        }
    }
}
