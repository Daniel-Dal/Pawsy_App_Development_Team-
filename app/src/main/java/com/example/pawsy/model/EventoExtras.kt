package com.example.pawsy.model

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class Categoria(
    val etiqueta: String,
    val emoji: String,
    val color: Color,
    val grupo: String
) {
    CITA_SALUD("Cita / procedimiento", "🩺", Color(0xFFE57373), "Salud"),
    MEDICAMENTO("Medicamento", "💊", Color(0xFFF06292), "Salud"), // misma familia que Salud
    ALIMENTACION("Alimentación", "🍽️", Color(0xFF81C784), "Alimentación"),
    PASEO("Paseo", "🐕", Color(0xFF64B5F6), "Paseo"),
    OTRO("Otro", "📌", Color(0xFF9E9E9E), "Otro") // eventos viejos o del calendario
}

enum class TipoFrecuencia(val texto: String) {
    UNA_VEZ("Una vez"),
    DIARIA("Todos los días"),
    DIAS_SEMANA("Días de la semana"),
    FINES_DE_SEMANA("Fines de semana"),
    CADA_N_DIAS("Cada X días"),
    CADA_N_HORAS("Cada X horas")
}

data class Recurrencia(
    val tipo: TipoFrecuencia = TipoFrecuencia.UNA_VEZ,
    val diasSemana: Set<Int> = emptySet(), // 1 = lunes ... 7 = domingo
    val cada: Int = 1,
    val fin: FechaEvento? = null
) {
    fun resumen(): String {
        val base = when (tipo) {
            TipoFrecuencia.UNA_VEZ -> ""
            TipoFrecuencia.DIARIA -> "Todos los días"
            TipoFrecuencia.FINES_DE_SEMANA -> "Fines de semana"
            TipoFrecuencia.DIAS_SEMANA ->
                diasSemana.sorted().joinToString(" ") { listOf("L", "M", "X", "J", "V", "S", "D")[it - 1] }
            TipoFrecuencia.CADA_N_DIAS -> "Cada $cada días"
            TipoFrecuencia.CADA_N_HORAS -> "Cada $cada h"
        }
        val hasta = fin?.let { "hasta ${it.dia}/${it.mes}" } ?: ""
        return listOf(base, hasta).filter { it.isNotBlank() }.joinToString(" · ")
    }
}

// Supone que FechaEvento.mes va de 1 a 12. Si en tu clase va de 0 a 11, ajusta aquí.
fun FechaEvento.aLocal(): LocalDate = LocalDate.of(anio, mes, dia)
fun LocalDate.aFecha(): FechaEvento = FechaEvento(year, monthValue, dayOfMonth)

/** ¿El evento ocurre en este día? El evento se guarda una vez y se expande al mostrar. */
fun Evento.ocurreEn(dia: FechaEvento): Boolean {
    val d = dia.aLocal()
    val inicio = fecha.aLocal()
    if (d.isBefore(inicio)) return false
    recurrencia.fin?.let { if (d.isAfter(it.aLocal())) return false }
    return when (recurrencia.tipo) {
        TipoFrecuencia.UNA_VEZ -> d == inicio
        TipoFrecuencia.DIARIA, TipoFrecuencia.CADA_N_HORAS -> true
        TipoFrecuencia.DIAS_SEMANA -> d.dayOfWeek.value in recurrencia.diasSemana
        TipoFrecuencia.FINES_DE_SEMANA ->
            d.dayOfWeek == DayOfWeek.SATURDAY || d.dayOfWeek == DayOfWeek.SUNDAY
        TipoFrecuencia.CADA_N_DIAS ->
            ChronoUnit.DAYS.between(inicio, d) % recurrencia.cada.coerceAtLeast(1) == 0L
    }
}
