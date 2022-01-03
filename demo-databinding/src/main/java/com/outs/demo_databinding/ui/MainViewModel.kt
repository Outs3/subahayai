package com.outs.demo_databinding.ui

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.android.*
import com.outs.utils.android.store.ContactData
import com.outs.utils.kotlin.emptyToNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/4 14:54
 * desc:
 */
class MainViewModel : BaseViewModel() {

    private data class Contact(val id: Int, val name: String?, var phoneNumber: String? = null)

    private data class ContactPhone(val contactId: Int, val number: String?)

    private data class ContactEmail(val contactId: Int, val email: String?)

    private data class ContactPlus(
        val contactId: Int,
        val name: String,
        val phones: List<String>,
        val emails: List<String>,
        val birthday: String? = null,
        val organTitle: String? = null
    )

    fun readContacts(context: Context) {
        launchOnUI(true) {
            withContext(Dispatchers.Main) {
                topLaunchOwner?.permissionOrThrow(Manifest.permission.READ_CONTACTS)
            }
            val data = withContext(Dispatchers.IO) {
                val contentResolver = context.contentResolver
                measureTimeMillis("组合数据") {
                    val contacts =
                        measureTimeMillis("SBHYI-基础数据") { contentResolver.queryContacts() }
                            ?.mapNotNull {
                                val id = it.id?.toInt()
                                val name = it.displayName?.emptyToNull()
                                if (null == id || null == name) null else id to name
                            }
                            ?.also { "contacts: $it".d() }
                    val phones =
                        measureTimeMillis("SBHYI-电话号码数据") { contentResolver.queryContactPhones() }
                            ?.mapNotNull {
                                val id = it.contactId
                                val number = it.number?.emptyToNull()
                                if (null == id || null == number) null else id to number
                            }
                            ?.groupingBy { it.first }
                            ?.aggregate { key, accumulator: ArrayList<String>?, element, first ->
                                (accumulator ?: ArrayList())
                                    .also { list ->
                                        element.second.also(list::add)
                                    }
                            }
                            ?.also { "phones: $it".d() }
                    val emails =
                        measureTimeMillis("SBHYI-邮件地址数据") { contentResolver.queryContactEmails() }
                            ?.mapNotNull {
                                val id = it.contactId
                                val emailAddress = it.emailAddress?.emptyToNull()
                                if (null == id || null == emailAddress) null else id to emailAddress
                            }
                            ?.groupingBy { it.first }
                            ?.aggregate { key, accumulator: ArrayList<String>?, element, first ->
                                (accumulator ?: ArrayList())
                                    .also { list ->
                                        element.second.also(list::add)
                                    }
                            }
                            ?.also { "emails: $it".d() }
                    val postals =
                        measureTimeMillis("SBHYI-邮寄地址") { contentResolver.queryContactStructuredPostal() }
                            ?.mapNotNull {
                                val id = it.contactId
                                val data = it.data?.emptyToNull()
                                if (null == id || null == data) null else id to data
                            }
                            ?.groupingBy { it.first }
                            ?.aggregate { key, accumulator: ArrayList<String>?, element, first ->
                                (accumulator ?: ArrayList())
                                    .also { list ->
                                        element.second.also(list::add)
                                    }
                            }
                            ?.also { "postals: $it".d() }
                    val extras =
                        measureTimeMillis("SBHYI-额外数据") { contentResolver.queryContactData() }
                            ?.mapNotNull {
                                val id = it.contactId
                                if (null == id) null else id to it
                            }
                            ?.groupingBy { it.first }
                            ?.aggregate { key, accumulator: ArrayList<ContactData>?, element, first ->
                                (accumulator ?: ArrayList())
                                    .also { list ->
                                        element.second.also(list::add)
                                    }
                            }
                            ?.also { "extras: $it".d() }

                    contacts?.mapIndexed { index, base ->
                        val contactId = base.first
                        val name = base.second
                        val phoneList = phones?.get(contactId) ?: emptyList()
                        val emailList = emails?.get(contactId) ?: emptyList()
                        val postalList = postals?.get(contactId) ?: emptyList()
                        val extraList = extras?.get(contactId) ?: emptyList()
                        "$index - $contactId - $name [$phoneList][$emailList][$postalList][$extraList]".d()
                        ContactPlus(contactId, name, phoneList, emailList)
                    }
                }


                //同时读取基础与电话太久了 不采用
//                val contacts2 = measureTimeMillis("同时读取基础数据与电话号码数据") { contentResolver.readContactsBaseAndPhone() }
                val ret = 1
            }
        }
    }

    private fun <T> measureTimeMillis(name: String, block: () -> List<T>?): List<T>? {
        val ret: List<T>?
        measureTimeMillis { ret = block() }.also { timeMillis ->
            "通讯录-${name}读取完毕，共计${ret?.size ?: 0}条数据，花费了${timeMillis}ms".dToast()
        }
        return ret
    }

    private fun ContentResolver.readContactsBase(
        uri: Uri = ContactsContract.Contacts.CONTENT_URI,
        projection: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
    ): ArrayList<Contact>? = query(
        uri,
        projection,
        null,
        null,
        null
    )
        ?.use { cursor ->
            val ret = ArrayList<Contact>(cursor.count)
            if (0 != cursor.count && cursor.moveToFirst()) {
                val indexOfId = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val indexOfName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                do {
                    val id = cursor.getIntOrNull(indexOfId) ?: -1
                    val name = cursor.getStringOrNull(indexOfName)
                    ret.add(Contact(id, name))
                } while (cursor.moveToNext())
            }
            ret
        }

    private fun ContentResolver.readContactsPhone(
        uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection: Array<String> = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
    ) = query(
        uri,
        projection,
        null,
        null,
        null
    )
        ?.use { cursor ->
            val ret = ArrayList<ContactPhone>(cursor.count)
            if (0 != cursor.count && cursor.moveToFirst()) {
                val indexOfId =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val indexOfNumber =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                do {
                    val id = cursor.getIntOrNull(indexOfId) ?: -1
                    val number = cursor.getStringOrNull(indexOfNumber)
                    ret.add(ContactPhone(id, number))
                } while (cursor.moveToNext())
            }
            ret
        }

    private fun ContentResolver.readContactsEmail(
        uri: Uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI,
        projection: Array<String> = arrayOf(
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.DATA
        )
    ): ArrayList<ContactEmail>? = query(
        uri,
        projection,
        null,
        null,
        null
    )
        ?.use { cursor ->
            val ret = ArrayList<ContactEmail>(cursor.count)
            if (0 != cursor.count && cursor.moveToFirst()) {
                val indexOfId =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
                val indexOfData = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                do {
                    val id = cursor.getIntOrNull(indexOfId) ?: -1
                    val data = cursor.getStringOrNull(indexOfData)
                    val contact = ContactEmail(id, data)
                    ret.add(contact)
                } while (cursor.moveToNext())
            }
            ret
        }

    private fun ContentResolver.readContactsBaseAndPhone(
        projection: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
    ): ArrayList<Contact>? = query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        null,
        null,
        null
    )
        ?.use { cursor ->
            val ret = ArrayList<Contact>(cursor.count)
            if (0 != cursor.count && cursor.moveToFirst()) {
                val indexOfId = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val indexOfName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                do {
                    val id = cursor.getIntOrNull(indexOfId) ?: -1
                    val name = cursor.getStringOrNull(indexOfName)
                    val phoneNumber = readContactPhoneByContactId(this, id)
                    val contact = Contact(id, name, phoneNumber)
                    ret.add(contact)
                } while (cursor.moveToNext())
            }
            ret
        }

    private fun readContactPhoneByContactId(
        resolver: ContentResolver,
        contactId: Int
    ): String? {
        val projection: Array<String> = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        return resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = $contactId",
            null,
            null
        )
            ?.use {
                it.takeIf { 0 != it.count && it.moveToFirst() }?.let { cursor ->
                    val indexOfNumber =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    cursor.getStringOrNull(indexOfNumber)
                }
            }
    }

}