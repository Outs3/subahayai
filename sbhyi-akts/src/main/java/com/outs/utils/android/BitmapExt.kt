package com.outs.utils.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.FaceDetector
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
fun Bitmap?.recycleOnNot() {
    if (false == this?.isRecycled)
        recycle()
}

fun Bitmap.hasFace(withRecycle: Boolean = true): Boolean {
    return 0 != faceCount(1, withRecycle)
}

fun Bitmap.faceCount(maxFaces: Int = 1, withRecycle: Boolean = true): Int {
    val src = this
    val faces: Array<FaceDetector.Face?> = arrayOfNulls(maxFaces)
    val detector = FaceDetector(src.width, src.height, maxFaces)
    val faceCount = detector.findFaces(src, faces)
    if (withRecycle) src.recycle()
    return faceCount
}

fun ByteArray.asBitmap(
    offset: Int = 0,
    len: Int = size,
    options: BitmapFactory.Options? = null
): Bitmap? =
    BitmapFactory.decodeByteArray(this, offset, len, options)

fun Bitmap.toByteArray(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): ByteArray = ByteArrayOutputStream()
    .also { out -> use { bmp -> bmp.compress(format, quality, out) } }
    .also(OutputStream::flush)
    .toByteArray()

fun Bitmap.compressToFile(
    file: File,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): File = file.also {
    FileOutputStream(file).use {
        compress(format, quality, it)
        it.flush()
    }
}

fun Bitmap.compressToTempFile(context: Context): File {
    val file = context.newTempFile(ext = "jpg")
    return compressToFile(file)
}

fun Bitmap.insertImage(context: Context) {
    compressToTempFile(context).saveToThumb(context)
}

/**
 * 旋转图片
 *  @param degrees 角度
 */
fun Bitmap.rotate(degrees: Float): Bitmap =
    Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { setRotate(degrees) }, true)

inline fun <reified R> Bitmap.use(block: (Bitmap) -> R): R {
    val ret = block(this)
    recycle()
    return ret
}

fun Context.getDrawableBitmap(drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
    val canvas = Canvas()
    val bmp = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    ) ?: return null
    canvas.setBitmap(bmp)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bmp
}

fun Bitmap.scaled(dstWidth: Int, dstHeight: Int = dstWidth, filter: Boolean = true): Bitmap =
    Bitmap.createScaledBitmap(this, dstWidth, dstHeight, filter)