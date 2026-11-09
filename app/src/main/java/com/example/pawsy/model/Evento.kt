package com.example.pawsy.model

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.UUID

private val NOMBRES_MESES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
)


fun nombreMes(mes: Int): String = NOMBRES_MESES[mes - 1]

data class FechaEvento(val anio: Int, val mes: Int, val dia: Int) : Comparable<FechaEvento> {

    override fun compareTo(other: FechaEvento): Int =
        compareValuesBy(this, other, { it.anio }, { it.mes }, { it.dia })

    fun etiqueta(): String = "$dia de ${nombreMes(mes).lowercase()}"

    companion object {
        fun hoy(): FechaEvento {
            val c = GregorianCalendar()
            return FechaEvento(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}

data class MesAnio(val anio: Int, val mes: Int) {

    val titulo: String get() = "${nombreMes(mes)} $anio"

    val diasEnMes: Int
        get() = GregorianCalendar(anio, mes - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH)

    val desfaseLunes: Int
        get() = (GregorianCalendar(anio, mes - 1, 1).get(Calendar.DAY_OF_WEEK) + 5) % 7

    fun masMeses(cantidad: Int): MesAnio {
        val total = anio * 12 + (mes - 1) + cantidad
        return MesAnio(total / 12, total % 12 + 1)
    }

    fun dia(numero: Int): FechaEvento = FechaEvento(anio, mes, numero)

    companion object {
        fun de(fecha: FechaEvento): MesAnio = MesAnio(fecha.anio, fecha.mes)
        fun actual(): MesAnio = de(FechaEvento.hoy())
    }
}

data class Evento(
    val fecha: FechaEvento,
    val titulo: String,
    val hora: String = "",
    val id: String = UUID.randomUUID().toString(),
    val calendarId: Long? = null
)
