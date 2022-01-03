package com.outs.utils.android.store

import android.provider.ContactsContract

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/1/3 20:02
 * desc:
 */
data class ContactStructuredPostal(
    @Column(name = ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID)
    val contactId: Int? = null,
    @Column(name = ContactsContract.CommonDataKinds.StructuredPostal.DATA)
    val data: String? = null,
) : IRow {

    val CONTENT_URI = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI

}