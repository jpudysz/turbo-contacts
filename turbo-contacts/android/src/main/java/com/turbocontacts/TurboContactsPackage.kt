package com.turbocontacts;

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class TurboContactsPackage : TurboReactPackage() {
    override fun getModule(name: String?, reactContext: ReactApplicationContext): NativeModule? {
        return if (name == TurboContactsModule.NAME) {
            TurboContactsModule(reactContext)
        } else {
            null
        }
    }

    override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
        mapOf(
            TurboContactsModule.NAME to ReactModuleInfo(
                TurboContactsModule.NAME,
                TurboContactsModule.NAME,
                false,
                false,
                true,
                false,
                true
            )
        )
    }
}
