package com.outs.utils.android

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
fun List<Bitmap>.generatePDF(
    //pdf每页的宽度
    pageWidth: Int = 1000,
    //pdf每页的高度
    pageHeight: Int = 1000,
    //展示图片的最大宽度/高度（自动适配）
    display: Int = 800,
    onSuccess: (document: PdfDocument) -> Unit
) {
    // create a new document
    val document = PdfDocument()
    val paint = Paint()
    this
        .map { bmp ->
            val width = bmp.width
            val height = bmp.height
            val scale: Float = display.toFloat().div(if (width >= height) width else height)
            val matrix = Matrix().apply {
                postScale(scale, scale)
            }
            Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true)
        }
        .mapIndexed { index, bitmap ->
            // create a page description
            val pageInfo =
                PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1 + index).create()

            // start a page
            val page = document.startPage(pageInfo)

            // draw something on the page
            val marginLeft = pageWidth.toFloat().minus(bitmap.width).div(2)
            val marginTop = pageHeight.toFloat().minus(bitmap.height).div(2)
            page.canvas.drawBitmap(bitmap, marginLeft, marginTop, paint)

            // finish the page
            document.finishPage(page)
        }

    // write the document content
    onSuccess(document)

    // close the document
    document.close()
}