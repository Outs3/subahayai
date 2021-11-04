package com.outs.utils.android.store

import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/17 14:16
 * desc:
 */
data class Contacts(
    @Column(name = ContactsContract.Contacts._ID)
    val id: Long? = null,
    @Column(name = ContactsContract.Contacts.DISPLAY_NAME)
    var displayName: String? = null,

    var phones: List<String>? = null
) : IRow {

    val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI

    fun getUri(): Uri? = id?.let { id -> ContentUris.withAppendedId(CONTENT_URI, id) }

}