package com.example.pawsy.data

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import com.example.pawsy.model.Evento

object AgendaEventos {
    val eventos = mutableStateListOf<Evento>()

    fun agregar(context: Context, evento: Evento) {
        val calendarId = CalendarioDispositivo.insertar(context, evento)
        eventos.add(if (calendarId != null) evento.copy(calendarId = calendarId) else evento)
        if (calendarId == null) {
            Toast.makeText(
                context,
                "Evento guardado solo en la app (sin acceso al calendario del teléfono)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun eliminar(context: Context, evento: Evento) {
        evento.calendarId?.let { CalendarioDispositivo.eliminar(context, it) }
        eventos.removeAll { it.id == evento.id }
    }

    fun cargarDesdeCalendario(context: Context) {
        val leidos = CalendarioDispositivo.leerEventosPawsy(context)
        eventos.removeAll { it.calendarId != null }
        eventos.addAll(leidos)
    }
}
