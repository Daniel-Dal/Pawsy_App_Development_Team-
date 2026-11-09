package com.example.pawsy.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Evento(
    @DocumentId val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val ciudad: String = "",
    val direccion: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val imagenUrl: String = "",
    val organizador: String = "",
    val destacado: Boolean = false
)


val TIPOS_EVENTO = listOf(
    "adopcion" to "Adopción",
    "vacunacion" to "Vacunación",
    "concurso" to "Concurso",
    "feria" to "Feria",
    "otro" to "Otro"
)

fun etiquetaTipo(tipo: String): String =
    TIPOS_EVENTO.firstOrNull { it.first == tipo }?.second ?: "Evento"

fun Timestamp.formatear(): String =
    SimpleDateFormat("EEE d MMM · h:mm a", Locale.forLanguageTag("es-CO"))
        .format(toDate())
        .replaceFirstChar { it.uppercase() }


class EventosViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null
    private val uid: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    var cargando by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private var todos by mutableStateOf<List<Evento>>(emptyList())


    var tipoSeleccionado by mutableStateOf<String?>(null)
    var ciudadSeleccionada by mutableStateOf<String?>(null)


    var eventoSeleccionado by mutableStateOf<Evento?>(null)
        private set
    var guardado by mutableStateOf(false)
        private set

    val ciudades: List<String>
        get() = todos.map { it.ciudad }.filter { it.isNotBlank() }.distinct().sorted()

    val eventos: List<Evento>
        get() = todos.filter { e ->
            (tipoSeleccionado == null || e.tipo == tipoSeleccionado) &&
                    (ciudadSeleccionada == null || e.ciudad == ciudadSeleccionada)
        }

    init {
        escuchar()
    }

    private fun escuchar() {

        listener = db.collection("eventos")
            .orderBy("fecha")
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    error = e.localizedMessage ?: "No se pudieron cargar los eventos"
                    cargando = false
                    return@addSnapshotListener
                }
                val desde = inicioDeHoy()
                todos = snap?.documents
                    ?.mapNotNull { doc ->
                        runCatching { doc.toObject(Evento::class.java) }
                            .onFailure { android.util.Log.e("Eventos", "Evento inválido: ${doc.id}", it) }
                            .getOrNull()
                    }
                    ?.filter { it.fecha.toDate().time >= desde }   // solo hoy en adelante
                    ?.sortedWith(
                        compareByDescending<Evento> { it.destacado }
                            .thenBy { it.fecha }
                    )
                    ?: emptyList()
                error = null
                cargando = false
            }
    }

    private fun inicioDeHoy(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis


    fun abrirDetalle(evento: Evento) {
        eventoSeleccionado = evento
        guardado = false
        refGuardado(evento.id)?.get()?.addOnSuccessListener { guardado = it.exists() }
    }

    fun cerrarDetalle() {
        eventoSeleccionado = null
    }


    private fun refGuardado(id: String) = uid?.let {
        db.collection("usuarios").document(it)
            .collection("eventosGuardados").document(id)
    }

    fun alternarGuardado(onSinSesion: () -> Unit) {
        val id = eventoSeleccionado?.id ?: return
        val ref = refGuardado(id)
        if (ref == null) {
            onSinSesion()
            return
        }
        if (guardado) {
            guardado = false                       // actualiza la UI al instante
            ref.delete().addOnFailureListener { guardado = true }
        } else {
            guardado = true
            ref.set(mapOf("guardadoEn" to Timestamp.now()))
                .addOnFailureListener { guardado = false }
        }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }
}
