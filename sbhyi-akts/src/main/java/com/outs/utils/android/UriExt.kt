package com.outs.utils.android

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.outs.utils.android.store.Image
import com.outs.utils.android.store.readRows
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.*


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/4 16:21
 * desc:
 */

/**
 * 读取Uri的输入流内容
 */
fun Uri.read(context: Context): ByteArray? = context.contentResolver.read(this)

/**
 * 从一个来自MediaStore的Image uri中取图片内容，会自动处理旋转角度
 *  @param context 上下文
 *  @return 图片内容
 */
fun Uri.readImageAsBytes(context: Context): ByteArray? = asFileByInputStream(context)?.readBytes()

fun Uri.asFileByPathOrNull(): File? =
    if (scheme.isNullOrEmpty()) File(toString()) else path?.let(::File)

/**
 * 将一个来自MediaStore的Image uri转化为文件，会自动处理旋转角度，自动保存到cache路径
 *  @param context 上下文
 *  @return 图片文件
 */
fun Uri.readImageAsFile(context: Context): File? = asFileByPathOrNull()
    ?.takeIf(File::canRead)
    ?.rotationTo0(context)
    ?: asFileByInputStream(context)

private fun Uri.asFileByInputStream(context: Context): File? {
    val data = context.contentResolver.read(this) ?: return null
    val image = context.contentResolver.query(this, null, null, null, null)
        ?.use { it.readRows<Image>() }
        ?.firstOrNull()
        ?: return null
    val cacheFile = data.asTempFile(context, name = image.getFileName())
    val orientation = image.orientation ?: cacheFile.orientationByExif()
    //如果该图片有旋转角度 将该角度生效到图片
    return if (0 == orientation) cacheFile else {
        val source = BitmapFactory.decodeByteArray(data, 0, data.size)
        val target = source.use { bitmap -> bitmap.rotate(orientation.toFloat()) }
        val out = target.toByteArray()
        newFile(context.cacheDir, cacheFile.name, out)
    }
}

private fun Image.getFileName(file: File? = null): String = displayName
    ?: title
    ?: file?.name
    ?: mimeType?.toMediaTypeOrNull()?.generateFileName()
    ?: UUID.randomUUID().toString()

private fun MediaType.generateFileName() = "${UUID.randomUUID()}.${subtype}"