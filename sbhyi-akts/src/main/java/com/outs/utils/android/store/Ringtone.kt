package com.outs.utils.android.store

import android.content.ContentUris
import android.media.RingtoneManager
import android.net.Uri

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 14:39
 * desc:
 */
data class Ringtone(
    @Column(RingtoneManager.ID_COLUMN_INDEX)
    val id: Long? = null,
    @Column(RingtoneManager.TITLE_COLUMN_INDEX)
    val title: String? = null,
    @Column(RingtoneManager.URI_COLUMN_INDEX)
    val uri: String? = null
) : IRow {

    fun getUri(): Uri? = if (null == id || null == uri) null else ContentUris.withAppendedId(
        Uri.parse(uri),
        id
    )
}