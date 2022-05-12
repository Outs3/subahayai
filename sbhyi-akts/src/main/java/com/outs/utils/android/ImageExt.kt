package com.outs.utils.android

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.blankj.utilcode.util.ImageUtils
import java.io.File

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/5/12 9:30
 * desc:
 */

/**
 * 将一个图片Uri压缩为一个文件
 * @param this 图片Uri
 *  @param context 上下文
 *  @param maxByteSize 图片最大长度
 */
fun Uri.imageUriZipAsFile(
    context: Context = appInstance, maxByteSize: Long = 1024 * 1024 * 10
): File? = readImageAsBytes(context)
    ?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    ?.use { bitmap -> ImageUtils.compressByQuality(bitmap, maxByteSize * 8 / 10) }
    ?.asTempFile(context, ext = "jpg")

/**
 * 压缩图片根据质量
 *
 * @param context     上下文
 * @param src         源图片文件
 * @param maxByteSize 最大图片长度
 * @return 压缩后的图片文件
 */
fun File.compressImage(context: Context, maxByteSize: Long): File? {
    //文件名不含后缀
    val name = nameWithoutExtension
    //解码图片为bmp
    val bitmap = BitmapFactory.decodeFile(absolutePath)
    //压缩图片
    val buf = ImageUtils.compressByQuality(bitmap, maxByteSize)
    //回收bmp内存
    bitmap.recycle()
    //将压缩后的图片字节流存入文件
    return buf.asTempFile(context, name, "jpg")
}