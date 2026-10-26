package com.example.pawsy

import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun probarConexionSupabase(): String {
    return withContext(Dispatchers.IO) {
        try {
            val respuesta = SupabaseClient.client
                .storage
                .from("mascotas")
                .list()

            "Conexión correcta. Archivos encontrados: ${respuesta.size}"
        } catch (e: Exception) {
            "Error de conexión: ${e.message}"
        }
    }
}