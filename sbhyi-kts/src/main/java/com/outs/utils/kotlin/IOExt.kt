package com.outs.utils.kotlin

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/17 17:03
 * desc:
 */
fun InputStream.toByteArray(): ByteArray? {
    try {
        val output = ByteArrayOutputStream()
        var ch: Int
        while (read().also { ch = it } != -1) {
            output.write(ch)
        }
        val imgdata: ByteArray = output.toByteArray()
        output.close()
        return imgdata
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun InputStream.copyTo(
    output: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    onGetTotal: IGetTotal? = null,
    onProgress: ((Float) -> Unit)? = null
) {
    val getTotal = onGetTotal ?: TotalFactory.totalByInputStreamAvailable(this)
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        output.write(buffer, 0, bytes)
        bytesCopied += bytes
        onProgress?.invoke(bytesCopied.toFloat().div(getTotal()))
        bytes = read(buffer)
    }
}

typealias IGetTotal = () -> Int

object TotalFactory {
    fun totalByInt(total: Int): IGetTotal = { total }
    fun totalByInputStreamAvailable(input: InputStream): IGetTotal = input::available
}