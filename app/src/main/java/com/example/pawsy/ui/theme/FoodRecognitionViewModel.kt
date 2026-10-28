package com.example.pawsy.ui.screens

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface RecognitionState {
    data object Idle : RecognitionState
    data object Loading : RecognitionState
    data class Success(val text: String) : RecognitionState
    data class Error(val message: String) : RecognitionState
}

class FoodRecognitionViewModel : ViewModel() {

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-3.5-flash")

    var bitmap by mutableStateOf<Bitmap?>(null)
        private set
    var state by mutableStateOf<RecognitionState>(RecognitionState.Idle)
        private set

    private val prompt = """
        Eres Pawsy, un asistente amable para dueños de mascotas.
        Analiza la imagen. Si es comida para perros o gatos, indica qué tipo es
        (BARF, croquetas, comida húmeda, casera, etc.), si parece una opción saludable
        y si hay ingredientes peligrosos visibles. Si NO es comida o no se distingue,
        dilo con amabilidad y pide otra foto.
        Responde en español, máximo 60 palabras, tono cercano, con 1 o 2 emojis.
    """.trimIndent()

    fun analizarUri(resolver: ContentResolver, uri: Uri) {
        viewModelScope.launch {
            state = RecognitionState.Loading
            val bmp = withContext(Dispatchers.IO) {
                runCatching {
                    resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
                }.getOrNull()
            }
            if (bmp == null) {
                state = RecognitionState.Error("No pude leer la imagen 😿")
            } else {
                analizar(bmp)
            }
        }
    }

    fun analizarBitmap(bmp: Bitmap) {
        viewModelScope.launch {
            state = RecognitionState.Loading
            analizar(bmp)
        }
    }

    private suspend fun analizar(original: Bitmap) {
        val bmp = withContext(Dispatchers.Default) { original.reducir(1024) }
        bitmap = bmp
        state = try {
            val response = model.generateContent(
                content {
                    image(bmp)
                    text(prompt)
                }
            )
            val texto = response.text?.trim()
            if (texto.isNullOrEmpty()) RecognitionState.Error("No obtuve respuesta, intenta de nuevo 🐾")
            else RecognitionState.Success(texto)
        } catch (e: Exception) {
            RecognitionState.Error("Error al analizar la imagen: ${e.localizedMessage ?: "desconocido"}")
        }
    }

    private fun Bitmap.reducir(maxSide: Int): Bitmap {
        val ratio = maxSide.toFloat() / maxOf(width, height)
        if (ratio >= 1f) return this
        return Bitmap.createScaledBitmap(this, (width * ratio).toInt(), (height * ratio).toInt(), true)
    }
}