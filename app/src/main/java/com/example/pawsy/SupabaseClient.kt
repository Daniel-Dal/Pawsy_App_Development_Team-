

package com.example.pawsy

import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.tasks.await

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        accessToken = {
            FirebaseAuth.getInstance()
                .currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
        }

        install(Storage)
    }
}

suspend fun comprobarRolFirebase(): String {
    val usuario = FirebaseAuth.getInstance().currentUser
        ?: return "No hay usuario conectado"

    // true = fuerza un token nuevo, no el que está en caché
    val token = usuario.getIdToken(true).await().token
        ?: return "No se pudo obtener el token"

    val partes = token.split(".")
    if (partes.size < 2) return "Token no válido"

    val payload = android.util.Base64.decode(
        partes[1],
        android.util.Base64.URL_SAFE or
                android.util.Base64.NO_WRAP or
                android.util.Base64.NO_PADDING
    )

    val json = org.json.JSONObject(String(payload, Charsets.UTF_8))

    return "iss=${json.optString("iss", "NO EXISTE")} | " +
            "aud=${json.optString("aud", "NO EXISTE")} | " +
            "role=${json.optString("role", "NO CONFIGURADO")}"
}