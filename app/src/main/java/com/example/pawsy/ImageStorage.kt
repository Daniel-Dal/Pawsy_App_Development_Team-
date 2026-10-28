package com.example.pawsy

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

private const val BUCKET = "Imagenes"   // cambia por el nombre real de tu bucket
private const val MAX_BYTES = 5L * 1024 * 1024
private val TIPOS_PERMITIDOS = mapOf(
    "image/jpeg" to "jpg",
    "image/png" to "png",
    "image/webp" to "webp"
)
private val clienteHttp = OkHttpClient()

/**
 * Sube una imagen a Supabase Storage y devuelve su URL pública.
 * Lanza una excepción si algo falla (por eso se llama dentro de try/catch).
 */
suspend fun subirImagen(context: Context, imagenUri: Uri): String =
    withContext(Dispatchers.IO) {
        val mime = context.contentResolver.getType(imagenUri)
            ?: error("Tipo de archivo desconocido")
        val extension = TIPOS_PERMITIDOS[mime]
            ?: error("Solo se permiten imágenes JPG, PNG o WEBP")

        val bytes = context.contentResolver.openInputStream(imagenUri)!!.use { stream ->
            val out = ByteArrayOutputStream()
            val buf = ByteArray(8192)
            while (true) {
                val n = stream.read(buf)
                if (n < 0) break
                out.write(buf, 0, n)
                require(out.size() <= MAX_BYTES) { "La imagen supera los 5 MB" }
            }
            out.toByteArray()
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "anonimo"
        val path = "$uid/${System.currentTimeMillis()}.$extension"

        val request = Request.Builder()
            .url("${BuildConfig.SUPABASE_URL}/storage/v1/object/$BUCKET/$path")
            .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
            .addHeader("Authorization", "Bearer ${BuildConfig.SUPABASE_ANON_KEY}")
            .post(bytes.toRequestBody(mime.toMediaType()))
            .build()

        clienteHttp.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) {
                val cuerpo = resp.body?.string()
                android.util.Log.e("SUBIR_IMAGEN", "code=${resp.code} cuerpo=$cuerpo")
                error("Error al subir (${resp.code}): $cuerpo")
            }
        }

        "${BuildConfig.SUPABASE_URL}/storage/v1/object/public/$BUCKET/$path"
    }