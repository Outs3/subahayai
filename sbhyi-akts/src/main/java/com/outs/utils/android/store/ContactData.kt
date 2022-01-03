package com.outs.utils.android.store

import android.provider.ContactsContract

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/1/3 20:03
 * desc:
 */
data class ContactData(
    @Column(name = ContactsContract.Data.CONTACT_ID)
    val contactId: Int? = null,
    @Column(name = ContactsContract.Data.MIMETYPE)
    val mimeType: String? = null,
    @Column(name = ContactsContract.CommonDataKinds.Organization.DATA)
    val organizationData: String? = null,
    @Column(name = ContactsContract.CommonDataKinds.Organization.TITLE)
    val organizationTitle: String? = null,
    @Column(name = ContactsContract.CommonDataKinds.Event.TYPE)
    val eventType: String? = null,
    @Column(name = ContactsContract.CommonDataKinds.Event.START_DATE)
    val eventStartDate: String? = null,
) : IRow {

    val CONTENT_URI = ContactsContract.Data.CONTENT_URI

}