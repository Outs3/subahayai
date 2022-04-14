package com.outs.utils.android.http

import android.content.ContentResolver
import android.net.Uri
import com.outs.utils.android.mimeTypeOrNull
import com.outs.utils.android.queryOpenable
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.*
import java.io.File
import java.io.IOException
import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/14 17:28
 * desc:
 */
open class DelegateRequestBody(protected val delegate: RequestBody) : RequestBody() {
    override fun contentType(): MediaType? = delegate.contentType()

    override fun contentLength(): Long = delegate.contentLength()

    override fun writeTo(sink: BufferedSink) = delegate.writeTo(sink)

    override fun isDuplex(): Boolean = delegate.isDuplex()

    override fun isOneShot(): Boolean = delegate.isOneShot()
}

open class CountingSink(
    delegate: BufferedSink,
    protected val total: Long,
    protected val onProgress: (current: Long, total: Long) -> Unit,
) : ForwardingSink(delegate) {

    private var current: Long = 0

    override fun write(source: Buffer, byteCount: Long) {
        super.write(source, byteCount)
        current += byteCount
        onProgress.invoke(current, total)
    }
}

open class CountingRequestBody(
    requestBody: RequestBody,
    protected val onProgress: ((current: Long, total: Long) -> Unit)? = null
) : DelegateRequestBody(requestBody) {

    override fun writeTo(sink: BufferedSink) {
        if (null == onProgress) {
            super.writeTo(sink)
        } else {
            val bufferedSink = CountingSink(sink, contentLength(), onProgress).buffer()
            delegate.writeTo(bufferedSink)
            bufferedSink.flush()
        }
    }

}

class ContentUriRequestBody(
    val contentResolver: ContentResolver,
    val uri: Uri
) : RequestBody() {

    override fun contentType(): MediaType? = contentResolver.getType(uri)?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        contentResolver.openInputStream(uri)
            ?.source()
            ?.use(sink::writeAll)
            ?: throw IOException("Couldn't open content URI for reading")
    }

}

fun Uri.asRequestBody(contentResolver: ContentResolver) =
    ContentUriRequestBody(contentResolver, this)

fun Uri.asFormPart(
    contentResolver: ContentResolver,
    fileName: String = contentResolver.queryOpenable(this)?.firstOrNull()?.displayName
        ?: UUID.randomUUID().toString(),
    formName: String = "file"
): MultipartBody.Part = MultipartBody.Part.createFormData(
    formName,
    fileName,
    asRequestBody(contentResolver)
)

fun File.asFormPart(
    formName: String = "file",
    contentType: MediaType? = mimeTypeOrNull()?.toMediaTypeOrNull(),
): MultipartBody.Part = MultipartBody.Part.createFormData(
    formName,
    name,
    asRequestBody(contentType)
)

fun File.asProgressFormPart(
    formName: String = "file",
    contentType: MediaType? = mimeTypeOrNull()?.toMediaTypeOrNull(),
    onProgress: (current: Long, total: Long) -> Unit,
): MultipartBody.Part = MultipartBody.Part.createFormData(
    formName,
    name,
    asRequestBody(contentType).withProgress(onProgress)
)

fun RequestBody.withProgress(onProgress: (current: Long, total: Long) -> Unit) =
    CountingRequestBody(this, onProgress)