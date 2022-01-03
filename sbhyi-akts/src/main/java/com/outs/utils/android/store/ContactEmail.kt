package com.outs.utils.android.store

import android.provider.ContactsContract

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/1/3 20:01
 * desc:
 */
data class ContactEmail(
    @Column(name = ContactsContract.CommonDataKinds.Email.CONTACT_ID)
    val contactId: Int? = null,
    @Column(name = ContactsContract.CommonDataKinds.Email.DATA)
    val emailAddress: String? = null,
) : IRow {

    val CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI

}