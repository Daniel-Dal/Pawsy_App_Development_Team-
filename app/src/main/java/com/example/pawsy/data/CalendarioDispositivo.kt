package com.example.pawsy.data

import android.Manifest
import android.accounts.Account
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events
import androidx.core.content.ContextCompat
import com.example.pawsy.model.Evento
import com.example.pawsy.model.FechaEvento
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

object CalendarioDispositivo {

    private const val MARCA = "[Pawsy]"
    private const val NOMBRE_CALENDARIO_LOCAL = "Pawsy"
    private const val CUENTA_LOCAL = "Pawsy"

    fun tienePermisos(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED

    fun insertar(context: Context, evento: Evento): Long? {
        if (!tienePermisos(context)) return null
        return try {
            val calendarioId = calendarioDestino(context) ?: return null
            val valores = ContentValues().apply {
                put(Events.CALENDAR_ID, calendarioId)
                put(Events.TITLE, evento.titulo)
                put(Events.DESCRIPTION, "$MARCA Evento de mascota creado desde Pawsy")

                val partes = evento.hora.split(":")
                val h = partes.getOrNull(0)?.toIntOrNull()
                val m = partes.getOrNull(1)?.toIntOrNull()

                if (h == null || m == null) {
                    val inicio = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
                        clear()
                        set(evento.fecha.anio, evento.fecha.mes - 1, evento.fecha.dia)
                    }.timeInMillis
                    put(Events.ALL_DAY, 1)
                    put(Events.EVENT_TIMEZONE, "UTC")
                    put(Events.DTSTART, inicio)
                    put(Events.DTEND, inicio + DIA_MS)
                } else {
                    val inicio = GregorianCalendar(
                        evento.fecha.anio, evento.fecha.mes - 1, evento.fecha.dia, h, m
                    ).timeInMillis
                    put(Events.ALL_DAY, 0)
                    put(Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                    put(Events.DTSTART, inicio)
                    put(Events.DTEND, inicio + HORA_MS)
                }
            }
            context.contentResolver.insert(Events.CONTENT_URI, valores)?.let { ContentUris.parseId(it) }
        } catch (e: Exception) {
            null
        }
    }


    fun eliminar(context: Context, calendarId: Long): Boolean {
        if (!tienePermisos(context)) return false
        return try {
            context.contentResolver.delete(
                ContentUris.withAppendedId(Events.CONTENT_URI, calendarId), null, null
            ) > 0
        } catch (e: Exception) {
            false
        }
    }

    fun leerEventosPawsy(context: Context): List<Evento> {
        if (!tienePermisos(context)) return emptyList()
        val resultado = mutableListOf<Evento>()
        try {
            context.contentResolver.query(
                Events.CONTENT_URI,
                arrayOf(Events._ID, Events.TITLE, Events.DTSTART, Events.ALL_DAY),
                "${Events.DESCRIPTION} LIKE ? AND ${Events.DELETED} = 0",
                arrayOf("%$MARCA%"),
                null
            )?.use { c ->
                while (c.moveToNext()) {
                    val id = c.getLong(0)
                    val titulo = c.getString(1).orEmpty()
                    val inicio = c.getLong(2)
                    val todoElDia = c.getInt(3) == 1

                    val cal = if (todoElDia) GregorianCalendar(TimeZone.getTimeZone("UTC"))
                    else GregorianCalendar()
                    cal.timeInMillis = inicio

                    val fecha = FechaEvento(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    val hora = if (todoElDia) "" else
                        "%02d:%02d".format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))

                    resultado.add(Evento(fecha, titulo, hora, id = "cal-$id", calendarId = id))
                }
            }
        } catch (e: Exception) {

        }
        return resultado
    }


    private fun calendarioDestino(context: Context): Long? {
        var primeroEditable: Long? = null
        context.contentResolver.query(
            Calendars.CONTENT_URI,
            arrayOf(Calendars._ID, Calendars.ACCOUNT_TYPE, Calendars.IS_PRIMARY),
            "${Calendars.CALENDAR_ACCESS_LEVEL} >= ?",
            arrayOf(Calendars.CAL_ACCESS_CONTRIBUTOR.toString()),
            null
        )?.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(0)
                val esGoogle = c.getString(1) == "com.google"
                val esPrincipal = c.getInt(2) == 1
                if (esGoogle && esPrincipal) return id          // el mejor caso
                if (primeroEditable == null) primeroEditable = id
            }
        }
        return primeroEditable ?: crearCalendarioLocal(context)
    }

    /** Calendario local (sin cuenta de Google): permite probar en emuladores sin sesión. */
    private fun crearCalendarioLocal(context: Context): Long? = try {
        val uri = Calendars.CONTENT_URI.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(Calendars.ACCOUNT_NAME, CUENTA_LOCAL)
            .appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            .build()
        val valores = ContentValues().apply {
            put(Calendars.ACCOUNT_NAME, CUENTA_LOCAL)
            put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            put(Calendars.NAME, NOMBRE_CALENDARIO_LOCAL)
            put(Calendars.CALENDAR_DISPLAY_NAME, NOMBRE_CALENDARIO_LOCAL)
            put(Calendars.CALENDAR_COLOR, 0xFFFE854F.toInt())
            put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER)
            put(Calendars.OWNER_ACCOUNT, CUENTA_LOCAL)
            put(Calendars.VISIBLE, 1)
            put(Calendars.SYNC_EVENTS, 1)
            put(Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().id)
        }
        context.contentResolver.insert(uri, valores)?.let { ContentUris.parseId(it) }
    } catch (e: Exception) {
        null
    }

    private const val HORA_MS = 60L * 60L * 1000L
    private const val DIA_MS = 24L * HORA_MS
}
