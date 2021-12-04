package com.outs.utils.android.store

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 13:43
 * desc:
 */
data class Image(
    @Column(name = MediaStore.Images.ImageColumns._ID)
    val id: Long? = null,
    @Column(name = MediaStore.Images.ImageColumns.TITLE)
    val title: String? = null,
    @Column(name = MediaStore.Images.ImageColumns.DISPLAY_NAME)
    val displayName: String? = null,
    @Column(name = MediaStore.Images.ImageColumns.MIME_TYPE)
    val mimeType: String? = null,
    @Column(name = MediaStore.Images.ImageColumns.SIZE)
    val size: Int? = null,
    @Column(name = MediaStore.Images.ImageColumns.ORIENTATION)
    val orientation: Int? = null,
) : IRow {

    val CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    fun getUri(): Uri? = id?.let { id -> ContentUris.withAppendedId(CONTENT_URI, id) }
}
