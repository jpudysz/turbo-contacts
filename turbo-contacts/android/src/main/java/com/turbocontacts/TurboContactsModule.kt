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

abstract class TurboContactsModule(reactContext: ReactApplicationContext) : NativeTurboContactsSpec(reactContext), PermissionListener {
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
                val nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val contactName = if (nameColumnIndex >= 0) getString(nameColumnIndex) else ""
                val phoneNumber = if (phoneNumberColumnIndex >= 0) getString(phoneNumberColumnIndex) else null

                val nameParts = contactName.split(" ")
                val firstName = nameParts.firstOrNull() ?: ""
                val lastName = nameParts.getOrNull(1) ?: ""

                val contactMap: WritableMap = Arguments.createMap()

                contactMap.putString("firstName", firstName)
                contactMap.putString("lastName", lastName)
                contactMap.putString("phoneNumber", phoneNumber)

                contactsArray.pushMap(contactMap)
            }

            close()
        }

        return contactsArray
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
