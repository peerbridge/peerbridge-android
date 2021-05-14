package com.peerbridge.android.http

import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

internal object HttpClient {
    private val client = OkHttpClient()

    private val MediaTypeJson = "application/json; charset=utf-8".toMediaType()

    @Throws(IOException::class)
    suspend fun get(
        url: String,
        builderAction: Request.Builder.() -> Request.Builder = {
            addHeader("Accept", "application/json")
        },
    ): Response {
        val request = Request.Builder()
            .url(url)
            .builderAction()
            .build()

        return client.newCall(request).await()
    }

    suspend fun post(
        url: String,
        body: String,
        builderAction: Request.Builder.() -> Request.Builder = {
            addHeader("Accept", "application/json")
            addHeader("Content-Type", "application/json")
        },
    ): Response {
        val request = Request.Builder()
            .url(url)
            .post(body.toRequestBody(MediaTypeJson))
            .builderAction()
            .build()

        return client.newCall(request).await()
    }
}

internal suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if (continuation.isCancelled) return
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) continuation.resumeWithException(IOException(response.toString()))
            else continuation.resume(response)
        }
    })

    continuation.invokeOnCancellation {
        try {
            cancel()
        } catch (_: Throwable) {
            /* Ignore */
        }
    }
}
