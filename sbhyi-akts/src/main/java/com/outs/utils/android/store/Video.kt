package com.outs.utils.android.store

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/25 17:08
 * desc:
 */
data class Video(
    @Column(name = MediaStore.Video.VideoColumns._ID)
    val id: Long? = null,
    @Column(name = MediaStore.Video.VideoColumns.ARTIST)
    val artist: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.AUTHOR)
    val author: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.BITRATE)
    val bitrate: Int? = null,
    @Column(name = MediaStore.Video.VideoColumns.DATA)
    val data: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.DURATION)
    val duration: Int? = null,
    @Column(name = MediaStore.Video.VideoColumns.DISPLAY_NAME)
    val displayName: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.MIME_TYPE)
    val mimeType: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.RELATIVE_PATH)
    val relativePath: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.SIZE)
    val size: Int? = null,
    @Column(name = MediaStore.Video.VideoColumns.TITLE)
    val title: String? = null,
    @Column(name = MediaStore.Video.VideoColumns.VOLUME_NAME)
    val volumeName: String? = null,
) : IRow {

    val CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    fun getUri(): Uri? = id?.let { id -> ContentUris.withAppendedId(CONTENT_URI, id) }
}
