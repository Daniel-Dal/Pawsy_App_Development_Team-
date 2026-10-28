package com.example.pawsy.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.pawsy.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

data class RecordFile(val path: String, val name: String, val sizeBytes: Long)

object MedicalRecordsStorage {
    private const val BUCKET = "historia-medica"
    private const val FOLDER = "demo"           // later: the user's UID
    private const val MAX_BYTES = 5L * 1024 * 1024
    private val ALLOWED = setOf("image/jpeg", "image/png", "application/pdf")

    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val anonKey = BuildConfig.SUPABASE_ANON_KEY
    private val client = OkHttpClient()

    private fun Request.Builder.auth() = this
        .addHeader("apikey", anonKey)
        .addHeader("Authorization", "Bearer $anonKey")

    private fun displayName(context: Context, uri: Uri): String =
        context.contentResolver.query(uri, null, null, null, null)?.use { c ->
            val i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (i >= 0 && c.moveToFirst()) c.getString(i) else null
        } ?: "file"

    suspend fun upload(context: Context, uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val mime = context.contentResolver.getType(uri) ?: error("Unknown file type")
            require(mime in ALLOWED) { "Only JPG, PNG or PDF files are allowed" }

            val bytes = context.contentResolver.openInputStream(uri)!!.use { stream ->
                val out = ByteArrayOutputStream()
                val buf = ByteArray(8192)
                while (true) {
                    val n = stream.read(buf)
                    if (n < 0) break
                    out.write(buf, 0, n)
                    require(out.size() <= MAX_BYTES) { "File exceeds 5 MB" }
                }
                out.toByteArray()
            }

            val safeName = displayName(context, uri).replace(Regex("[^A-Za-z0-9._-]"), "_")
            val path = "$FOLDER/${System.currentTimeMillis()}_$safeName"

            val request = Request.Builder()
                .url("$supabaseUrl/storage/v1/object/$BUCKET/$path")
                .auth()
                .post(bytes.toRequestBody(mime.toMediaType()))
                .build()

            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) error("Upload failed (${resp.code}): ${resp.body?.string()}")
            }
            path
        }
    }

    suspend fun list(): Result<List<RecordFile>> = withContext(Dispatchers.IO) {
        runCatching {
            val body = JSONObject()
                .put("prefix", FOLDER)
                .put("limit", 100)
                .put("sortBy", JSONObject().put("column", "created_at").put("order", "desc"))
                .toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("$supabaseUrl/storage/v1/object/list/$BUCKET")
                .auth()
                .post(body)
                .build()

            client.newCall(request).execute().use { resp ->
                val text = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) error("List failed (${resp.code}): $text")
                val arr = JSONArray(text)
                (0 until arr.length()).map { arr.getJSONObject(it) }
                    .filter { !it.isNull("id") }   // skip folder placeholders
                    .map {
                        val name = it.getString("name")
                        RecordFile(
                            path = "$FOLDER/$name",
                            name = name.substringAfter('_'),
                            sizeBytes = it.optJSONObject("metadata")?.optLong("size") ?: 0L
                        )
                    }
            }
        }
    }

    suspend fun getSignedUrl(path: String, expiresInSeconds: Int = 3600): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val body = JSONObject().put("expiresIn", expiresInSeconds).toString()
                    .toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url("$supabaseUrl/storage/v1/object/sign/$BUCKET/$path")
                    .auth()
                    .post(body)
                    .build()

                client.newCall(request).execute().use { resp ->
                    val text = resp.body?.string().orEmpty()
                    if (!resp.isSuccessful) error("Sign failed (${resp.code}): $text")
                    "$supabaseUrl/storage/v1" + JSONObject(text).getString("signedURL")
                }
            }
        }
}