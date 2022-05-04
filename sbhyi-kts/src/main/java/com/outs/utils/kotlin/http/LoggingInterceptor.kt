package com.outs.utils.kotlin.http

import com.outs.utils.kotlin.d
import okhttp3.*
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/12 10:11
 * desc:
 */
object LoggingInterceptor : Interceptor {

    // 换行符
    private val BR = System.getProperty("line.separator")

    // 空格
    private const val SPACE = "\t"

    private val UTF8 = Charset.forName("UTF-8")

    private const val bodyCount = 1024L * 1024

    private var contentMaxLength = 0x4000L

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        request.convToString().d(tag = "Http")
        val response: Response = chain.proceed(request)
        response.convToString().d(tag = "Http")
        return response
    }

    private fun MediaType.isText(): Boolean = "text" == type ||
            "json" == subtype ||
            "xml" == subtype ||
            "html" == subtype ||
            "webviewhtml" == subtype

    private fun Headers.convToString(): String {
        val builder = StringBuilder(javaClass.simpleName)
        builder.append(" [").append(BR)
        for (name in names()) {
            builder.append(name).append("=").append(get(name)).append(BR)
        }
        return builder.append(']').toString()
    }

    private fun Request.bodyToString(): String = try {
        val copy = newBuilder().build()
        val buffer = Buffer()
        val body = copy.body
        body?.writeTo(buffer)
        buffer.readUtf8()
    } catch (e: IOException) {
        "something error when show requestBody."
    }

    private fun Buffer.isPlaintext(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (size < 64) size else 64
            copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun Request.convToString(): String {
        val builder = StringBuilder()
        builder.append("method = ").append(method).append(BR)
        builder.append("url = ").append(url.toString()).append(BR)
        builder.append("header = ").append(headers.convToString())
            .append(BR)
        try {
            val requestBody = body
            if (requestBody != null) {
                val charset: Charset?
                val mediaType = requestBody.contentType()
                if (mediaType == null) {
                    return builder.toString()
                } else {
                    charset = mediaType.charset(UTF8)
                    builder.append("requestBody's contentType  = ").append(mediaType.toString())
                        .append(BR)
                }
                if (mediaType.isText()) {
                    builder.append("requestBody's content  = ").append(bodyToString())
                        .append(BR)
                } else if (requestBody.contentLength() < contentMaxLength) {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    if (buffer.isPlaintext()) {
                        builder.append("requestBody's content  = ")
                            .append(buffer.readString(charset ?: UTF8))
                            .append(BR)
                    }
                } else {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    builder.append("requestBody's content  = ")
                        .append(buffer.readString(contentMaxLength, charset ?: UTF8))
                        .append(BR)
                }
            }
        } catch (e: IOException) {
            e.d()
        }
        return builder.toString()
    }

    private fun Response.convToString(): String {
        val builder = StringBuilder()
        builder.append("code = ").append(code).append(BR)
        builder.append("isSuccessful = ").append(isSuccessful).append(BR)
        builder.append("url = ").append(request.url).append(BR)
        builder.append("message = ").append(message).append(BR)
        builder.append("protocol = ").append(protocol).append(BR)
        builder.append("header = ").append(headers.convToString()).append(BR)
        try {
            val body = peekBody(bodyCount)
            val bodyText =
                body.string()
                    .let { if (it.length < contentMaxLength) it else it.substring(0 until contentMaxLength.toInt()) }
            builder.append("body = ")
                .append(bodyText)
                .append(BR)
        } catch (e: IOException) {
            e.d()
        }
        return builder.toString()
    }

    fun setContentMaxLength(contentMaxLength: Long) {
        this.contentMaxLength = contentMaxLength
    }

}