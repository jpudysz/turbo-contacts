package com.turbocontacts

import android.Manifest
import android.content.pm.PackageManager
import android.provider.ContactsContract
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import com.turbocontacts.NativeTurboContactsSpec

class TurboContactsModule(reactContext: ReactApplicationContext) : NativeTurboContactsSpec(reactContext), PermissionListener {
    private var promise: Promise? = null
    private val requestCode: Int = 101

    override fun getName() = NAME
    override fun hasContactsPermission(): Boolean {
        val permission = reactApplicationContext.checkSelfPermission(Manifest.permission.READ_CONTACTS)

        return permission == PackageManager.PERMISSION_GRANTED
    }

    override fun requestContactsPermission(promise: Promise) {
        if (this.promise != null) {
            return
        }

        val activity = reactApplicationContext.currentActivity as PermissionAwareActivity

        this.promise = promise
        activity.requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), this.requestCode, this)
    }

    override fun getAllContacts(): WritableArray {
        val contactsArray = Arguments.createArray()
        val contentResolver = reactApplicationContext.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        ) ?: return contactsArray

        cursor.run {
            while (moveToNext()) {
                val contactIdColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val hasPhoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                val contactName = if (nameColumnIndex >= 0) getString(nameColumnIndex) else ""
                val hasPhoneNumber = if (hasPhoneNumberColumnIndex >= 0) getInt(hasPhoneNumberColumnIndex) else 0
                val contactId = if (contactIdColumnIndex >= 0) getString(contactIdColumnIndex) else null

                val nameParts = contactName.split(" ")
                val firstName = nameParts.firstOrNull() ?: ""
                val lastName = nameParts.getOrNull(1) ?: ""

                val contactMap: WritableMap = Arguments.createMap()

                contactMap.putString("firstName", firstName)
                contactMap.putString("lastName", lastName)
                contactMap.putString("phoneNumber", getPhoneNumber(hasPhoneNumber, contactId))

                contactsArray.pushMap(contactMap)
            }

            close()
        }

        return contactsArray
    }

    private fun getPhoneNumber(hasPhoneNumber: Int, contactId: String?): String? {
        if (hasPhoneNumber == 0) {
            return null
        }

        val contentResolver = reactApplicationContext.contentResolver
        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        ) ?: return null

        var phoneNumber: String? = null

        phoneCursor.use {
            if (phoneCursor.moveToFirst()) {
                val phoneNumberColumnIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                phoneNumber =  if (phoneNumberColumnIndex >= 0) phoneCursor.getString(phoneNumberColumnIndex) else null
            }
        }

        phoneCursor.close()

        return phoneNumber
    }

    companion object {
        const val NAME = "TurboContacts"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, results: IntArray?): Boolean {
        if (requestCode != this.requestCode || this.promise == null || results == null || results.isEmpty()) {
            return false
        }

        val readContactsPermissionResult = results[0]

        this.promise?.resolve(readContactsPermissionResult == PackageManager.PERMISSION_GRANTED)

        return true
    }
}
