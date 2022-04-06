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
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.IOException
import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/14 17:28
 * desc:
 */
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
    this.asRequestBody(contentResolver)
)

fun asFormPart(
    file: File,
    formName: String = "file",
    contentType: MediaType? = file.mimeTypeOrNull()?.toMediaTypeOrNull()
): MultipartBody.Part = MultipartBody.Part.createFormData(
    formName,
    file.name,
    file.asRequestBody(contentType)
)

fun File.asFormPart(formName: String = "file"): MultipartBody.Part = asFormPart(this, formName)
