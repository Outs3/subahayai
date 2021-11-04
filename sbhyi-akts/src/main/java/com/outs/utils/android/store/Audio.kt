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
data class Audio(
    @Column(name = MediaStore.Audio.AudioColumns._ID)
    val id: Long? = null,
    @Column(name = MediaStore.Audio.AudioColumns.ARTIST)
    val artist: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.AUTHOR)
    val author: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.BITRATE)
    val bitrate: Int? = null,
    @Column(name = MediaStore.Audio.AudioColumns.DATA)
    val data: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.DURATION)
    val duration: Int? = null,
    @Column(name = MediaStore.Audio.AudioColumns.DISPLAY_NAME)
    val displayName: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.MIME_TYPE)
    val mimeType: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.RELATIVE_PATH)
    val relativePath: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.SIZE)
    val size: Int? = null,
    @Column(name = MediaStore.Audio.AudioColumns.TITLE)
    val title: String? = null,
    @Column(name = MediaStore.Audio.AudioColumns.VOLUME_NAME)
    val volumeName: String? = null,
) : IRow {

    val CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    fun getUri(): Uri? = id?.let { id -> ContentUris.withAppendedId(CONTENT_URI, id) }
}
