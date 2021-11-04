package com.outs.utils.android.store

import android.content.ContentUris
import android.net.Uri
import android.provider.Telephony

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/25 13:58
 * desc:
 */
data class Sms(
    @Column(name = Telephony.Sms._ID)
    val id: Long? = null,
    @Column(name = Telephony.Sms.TYPE)
    val type: Int? = null,
    @Column(name = Telephony.Sms.THREAD_ID)
    val threadId: Int? = null,
    @Column(name = Telephony.Sms.ADDRESS)
    val address: String? = null,
    @Column(name = Telephony.Sms.DATE)
    val date: Long? = null,
    @Column(name = Telephony.Sms.DATE_SENT)
    val dateSent: Long? = null,
    @Column(name = Telephony.Sms.READ)
    val read: Int? = null,
    @Column(name = Telephony.Sms.SEEN)
    val seen: Int? = null,
    @Column(name = Telephony.Sms.STATUS)
    val status: Int? = null,
    @Column(name = Telephony.Sms.SUBJECT)
    val subject: String? = null,
    @Column(name = Telephony.Sms.BODY)
    val body: String? = null,
    @Column(name = Telephony.Sms.PERSON)
    val person: Int? = null,
    @Column(name = Telephony.Sms.PROTOCOL)
    val protocal: Int? = null,
    @Column(name = Telephony.Sms.REPLY_PATH_PRESENT)
    val replyPathPresent: Boolean? = null,
    @Column(name = Telephony.Sms.SERVICE_CENTER)
    val serviceCenter: String? = null,
    @Column(name = Telephony.Sms.LOCKED)
    val locked: Boolean? = null,
//    @Column(name = Telephony.Sms.SUBSCRIPTION_ID)
//    val subscriptionId: Long? = null,
) : IRow {

    val CONTENT_URI = Telephony.Sms.CONTENT_URI

    fun getUri(): Uri? = id?.let { id -> ContentUris.withAppendedId(CONTENT_URI, id) }
}
