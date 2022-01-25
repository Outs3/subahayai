package com.outs.utils.android

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.FileIOUtils
import com.outs.utils.kotlin.IGetTotal
import com.outs.utils.kotlin.copyTo
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/21 9:19
 * desc:
 */
fun InputStream.copyTo(
    output: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    onGetTotal: IGetTotal? = null,
    onProgress: MutableLiveData<Float>? = null
) = copyTo(output, bufferSize, onGetTotal) { onProgress?.postOnNot(it) }

fun InputStream.writeToFile(file: File): File {
    FileIOUtils.writeFileFromIS(file, this)
    return file
}
