package com.outs.utils.android.store

import android.provider.ContactsContract

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/17 14:17
 * desc:
 */
data class ContactPhone(
    @Column(name = ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
    val contactId: Int? = null,
    @Column(name = ContactsContract.CommonDataKinds.Phone.NUMBER)
    val number: String? = null,
) : IRow {

    val CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

}