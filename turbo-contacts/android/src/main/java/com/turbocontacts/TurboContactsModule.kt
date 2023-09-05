package com.turbocontacts

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableArray
import com.turbocontacts.NativeTurboContactsSpec

class TurboContactsModule(reactContext: ReactApplicationContext) : NativeTurboContactsSpec(reactContext) {
    override fun getName() = NAME
    override fun hasContactsPermission(): Boolean {
        return false
    }

    override fun requestContactsPermission(promise: Promise) {
        promise.resolve(false)
    }

    override fun getAllContacts(): WritableArray {
        return Arguments.createArray()
    }

    companion object {
        const val NAME = "TurboContacts"
    }
}
