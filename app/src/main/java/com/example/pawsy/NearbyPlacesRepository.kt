package com.example.pawsy.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

enum class PlaceKind { VET, PET_STORE }

data class NearbyPlace(
    val name: String?,     // null si el lugar no tiene nombre
    val lat: Double,
    val lon: Double,
    val kind: PlaceKind
)

/**
 * Lugares cercanos usando Places API (New) de Google.
 * Requisitos en Google Cloud: "Places API (New)" habilitada y agregada
 * a las restricciones de API de la key.
 */
object NearbyPlacesRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .callTimeout(20, TimeUnit.SECONDS)
        .build()

    private const val URL = "https://places.googleapis.com/v1/places:searchNearby"
    private val JSON_TYPE = "application/json; charset=utf-8".toMediaType()

    suspend fun nearby(
        context: Context,
        lat: Double,
        lon: Double,
        radiusMeters: Int = 3000
    ): Result<List<NearbyPlace>> = withContext(Dispatchers.IO) {
        runCatching {
            val body = JSONObject()
                .put("includedTypes", JSONArray(listOf("veterinary_care", "pet_store")))
                .put("maxResultCount", 20)
                .put("languageCode", "es")
                .put(
                    "locationRestriction",
                    JSONObject().put(
                        "circle",
                        JSONObject()
                            .put(
                                "center",
                                JSONObject().put("latitude", lat).put("longitude", lon)
                            )
                            .put("radius", radiusMeters.toDouble())
                    )
                )
                .toString()

            // La key está restringida a "Apps para Android", así que en llamadas REST
            // hay que enviar el paquete y el SHA-1 del certificado con el que se firmó la app.
            val request = Request.Builder()
                .url(URL)
                .header("X-Goog-Api-Key", apiKey(context))
                .header("X-Goog-FieldMask", "places.displayName,places.location,places.types")
                .header("X-Android-Package", context.packageName)
                .header("X-Android-Cert", signatureSha1(context))
                .post(body.toRequestBody(JSON_TYPE))
                .build()

            val text = client.newCall(request).execute().use { resp ->
                val respBody = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) {
                    android.util.Log.e("Places", "HTTP ${resp.code}: $respBody")
                    error("Places error (${resp.code})")
                }
                respBody
            }
            parse(text)
        }
    }

    private fun parse(text: String): List<NearbyPlace> {
        // Si no hay resultados, la respuesta viene sin el campo "places"
        val places = JSONObject(text).optJSONArray("places") ?: return emptyList()
        return (0 until places.length()).mapNotNull { i ->
            val p = places.getJSONObject(i)
            val loc = p.optJSONObject("location") ?: return@mapNotNull null
            val types = p.optJSONArray("types")
            val isVet = (0 until (types?.length() ?: 0)).any { types!!.getString(it) == "veterinary_care" }

            NearbyPlace(
                name = p.optJSONObject("displayName")?.optString("text")?.ifBlank { null },
                lat = loc.getDouble("latitude"),
                lon = loc.getDouble("longitude"),
                kind = if (isVet) PlaceKind.VET else PlaceKind.PET_STORE
            )
        }
    }

    /** Lee la misma key que ya está en el Manifest (meta-data de Google Maps). */
    private fun apiKey(context: Context): String {
        val info = context.packageManager.getApplicationInfo(
            context.packageName, PackageManager.GET_META_DATA
        )
        return info.metaData?.getString("com.google.android.geo.API_KEY").orEmpty()
            .ifBlank { error("Falta MAPS_API_KEY") }
    }

    /** SHA-1 (hex en mayúsculas, sin ":") del certificado con el que está firmada la app. */
    @Suppress("DEPRECATION")
    private fun signatureSha1(context: Context): String {
        val pm = context.packageManager
        val signature = if (Build.VERSION.SDK_INT >= 28) {
            pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                .signingInfo?.apkContentsSigners?.firstOrNull()
        } else {
            pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                .signatures?.firstOrNull()
        } ?: error("No se pudo leer la firma de la app")

        return MessageDigest.getInstance("SHA-1")
            .digest(signature.toByteArray())
            .joinToString("") { "%02X".format(it) }
    }
}