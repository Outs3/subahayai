package com.outs.utils.android.http

import com.outs.utils.android.mimeTypeOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/14 17:28
 * desc:
 */
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
