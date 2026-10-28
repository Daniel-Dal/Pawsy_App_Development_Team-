package com.example.pawsy.data

import com.example.pawsy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request

object SupabaseClient {
    private val supabaseUrl = BuildConfig.SUPABASE_URL
    private val anonKey = BuildConfig.SUPABASE_ANON_KEY

    private val http = OkHttpClient()

    fun requestBuilder(path: String): Request.Builder =
        Request.Builder()
            .url("$supabaseUrl/rest/v1/$path")
            .addHeader("apiKey", anonKey)
            .addHeader("Authorization", "Bearer $anonKey")
            .addHeader("Content-Type", "application/json")

    val client: OkHttpClient get() = http
}