package com.example.pawsy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class PlaceKind { VET, PET_STORE }

data class NearbyPlace(
    val name: String?,     // null si el lugar no tiene nombre en OpenStreetMap
    val lat: Double,
    val lon: Double,
    val kind: PlaceKind
)

object NearbyPlacesRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .callTimeout(45, TimeUnit.SECONDS)
        .build()

    // Si el primer servidor falla o tarda, se prueba el siguiente
    private val ENDPOINTS = listOf(
        "https://overpass-api.de/api/interpreter",
        "https://overpass.private.coffee/api/interpreter"
    )

    private const val USER_AGENT = "PawsyApp/1.0 (contactopawsyapp@gmail.com)"

    suspend fun nearby(lat: Double, lon: Double, radiusMeters: Int = 5000): Result<List<NearbyPlace>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val query = """
                    [out:json][timeout:25];
                    (
                      nwr["amenity"="veterinary"](around:$radiusMeters,$lat,$lon);
                      nwr["shop"="pet"](around:$radiusMeters,$lat,$lon);
                    );
                    out center 40;
                """.trimIndent()

                var lastError: Exception? = null
                for (endpoint in ENDPOINTS) {
                    try {
                        val request = Request.Builder()
                            .url(endpoint)
                            .header("User-Agent", USER_AGENT)
                            .post(FormBody.Builder().add("data", query).build())
                            .build()

                        val text = client.newCall(request).execute().use { resp ->
                            val body = resp.body?.string().orEmpty()
                            if (!resp.isSuccessful) error("Overpass error (${resp.code})")
                            body
                        }
                        return@runCatching parse(text)
                    } catch (e: Exception) {
                        lastError = e
                    }
                }
                throw lastError ?: IllegalStateException("Overpass no disponible")
            }
        }

    private fun parse(text: String): List<NearbyPlace> {
        val elements = JSONObject(text).getJSONArray("elements")
        return (0 until elements.length()).mapNotNull { i ->
            val e = elements.getJSONObject(i)
            val tags = e.optJSONObject("tags") ?: return@mapNotNull null
            val center = e.optJSONObject("center")
            val pLat = if (e.has("lat")) e.getDouble("lat") else center?.optDouble("lat")
            val pLon = if (e.has("lon")) e.getDouble("lon") else center?.optDouble("lon")
            if (pLat == null || pLon == null || pLat.isNaN() || pLon.isNaN()) return@mapNotNull null

            NearbyPlace(
                name = tags.optString("name").ifBlank { null },
                lat = pLat,
                lon = pLon,
                kind = if (tags.optString("amenity") == "veterinary") PlaceKind.VET else PlaceKind.PET_STORE
            )
        }
    }
}