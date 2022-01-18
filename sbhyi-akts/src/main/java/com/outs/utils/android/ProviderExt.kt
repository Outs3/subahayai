package com.outs.utils.android

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Telephony
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.outs.utils.android.store.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 10:32
 * desc:
 */
fun getFileProviderAuthority(
    packageName: String = AppUtils.getAppPackageName(),
    name: String = "fileProvider"
) =
    "$packageName.$name"

fun File.provide(
    context: Context = Utils.getApp(),
    authority: String = getFileProviderAuthority()
) =
    FileProvider.getUriForFile(context, authority, this)

fun ContentResolver.insert(uri: Uri, contentValues: ContentValues? = null, data: ByteArray): Uri? =
    insert(uri, contentValues)?.also { it ->
        openOutputStream(it)?.use {
            it.write(data)
            it.flush()
        }
    }

fun ContentResolver.read(uri: Uri) = openInputStream(uri)?.use(InputStream::readBytes)

fun Uri.toByteArray(context: Context) = context.contentResolver.read(this)

fun ContentResolver.write(uri: Uri, data: ByteArray) =
    openFileDescriptor(uri, "w")?.use {
        FileOutputStream(it.fileDescriptor).use { out ->
            out.write(data)
        }
    }

inline fun <reified T : IRow> ContentResolver.query(uri: Uri) =
    query(uri, null, null, null, null)?.use { it.readRows<T>() }

fun ContentResolver.queryAudio(uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) =
    query<Audio>(uri)

fun ContentResolver.queryImage(uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI) =
    query<Image>(uri)

fun ContentResolver.queryVideo(uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI) =
    query<Video>(uri)

fun ContentResolver.querySms(uri: Uri = Telephony.Sms.CONTENT_URI) = query<Sms>(uri)

fun ContentResolver.queryCallLog(uri: Uri = CallLog.Calls.CONTENT_URI) = query<Call>(uri)

fun ContentResolver.queryOpenable(uri: Uri) = query<Openable>(uri)

fun ContentResolver.queryContacts(
    uri: Uri = ContactsContract.Contacts.CONTENT_URI,
    withPhone: Boolean = false
) =
    query<Contacts>(uri)
        ?.also {
            if (withPhone) {
                it.onEach { contact ->
                    contact.phones =
                        contact.id?.let { id -> queryPhone(contactId = id)?.mapNotNull { it.number } }
                }
            }
        }

fun ContentResolver.queryContactPhones(
    uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
) = query<ContactPhone>(uri)

fun ContentResolver.queryContactEmails(
    uri: Uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
) = query<ContactEmail>(uri)

fun ContentResolver.queryContactStructuredPostal(
    uri: Uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI
) = query<ContactStructuredPostal>(uri)

fun ContentResolver.queryContactData(
    uri: Uri = ContactsContract.Data.CONTENT_URI
) = query<ContactData>(uri)

fun ContentResolver.queryPhone(
    uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
    contactId: Long
) = query(
    uri,
    null,
    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
    arrayOf(contactId.toString()),
    null
)
    ?.use { it.readRows<ContactPhone>() }
