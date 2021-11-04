package com.outs.utils.kotlin

import com.outs.utils.kotlin.http.LoggingInterceptor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/17 17:02
 * desc:
 */
fun String.readUrl(): ByteArray? {
    try {
        val url = URL(this)
        val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val responseCode: Int = httpConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return httpConnection.inputStream.readBytes()
        }
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

suspend fun String.suspendReadUrl(): ByteArray = withContext(Dispatchers.IO) {
    suspendCoroutine { continuation ->
        try {
            val url = URL(this@suspendReadUrl)
            val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            val responseCode: Int = httpConnection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw RuntimeException("Http failed: $responseCode")
            }
            continuation.resumeOrException(httpConnection.inputStream.readBytes())
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }
}

suspend fun downloadByOkHttp(client: OkHttpClient = OkHttpClient(), url: String): ByteArray? =
    withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            try {
                val request = Request.Builder().get().url(url).build()
                val call = client.newCall(request)
                call.execute().let { response ->
                    if (!response.isSuccessful) {
                        throw RuntimeException("Http failed: ${response.code} ${response.message}")
                    }
                    continuation.resumeOrException(response.body?.bytes())
                }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }
    }

fun emptyTrustManager(): X509TrustManager = object : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}

fun simpleOkHttpClientBuilder(
    withLog: Boolean = true,
    interceptors: List<Interceptor>? = null
): OkHttpClient.Builder {
    val trustManager = emptyTrustManager()
    val trustAllCerts = arrayOf<TrustManager>(trustManager)
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustAllCerts, SecureRandom())

    return OkHttpClient.Builder()
        .also { interceptors?.forEach(it::addInterceptor) }
        .also { if (withLog) it.addInterceptor(LoggingInterceptor) }
        .sslSocketFactory(sslContext.socketFactory, trustManager)
        .hostnameVerifier { _, _ -> true }
}

fun simpleOkHttpClient(withLog: Boolean = true): OkHttpClient =
    simpleOkHttpClientBuilder(withLog)
        .build()

suspend fun OkHttpClient.suspendCall(request: Request): Response =
    suspendCoroutine { continuation ->
        newCall(request).enqueue(object :
            Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (call.isCanceled()) {
                    continuation.resumeWithException(CancellationException())
                } else {
                    continuation.resume(response)
                }
            }

        })
    }