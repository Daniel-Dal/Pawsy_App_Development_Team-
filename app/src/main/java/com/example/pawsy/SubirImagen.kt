
package com.example.pawsy

import android.content.Context
import android.net.Uri
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

suspend fun subirImagen(
    context: Context,
    imagenUri: Uri
): String = withContext(Dispatchers.IO) {
    android.util.Log.d("PawsyToken", comprobarRolFirebase())
    val nombreArchivo = "${UUID.randomUUID()}.jpg"

    val bytes = context.contentResolver
        .openInputStream(imagenUri)
        ?.use { it.readBytes() }
        ?: throw Exception("No se pudo leer la imagen seleccionada")

    SupabaseClient.client.storage
        .from("pawsy")
        .upload(nombreArchivo, bytes)

    SupabaseClient.client.storage
        .from("pawsy")
        .publicUrl(nombreArchivo)
}
