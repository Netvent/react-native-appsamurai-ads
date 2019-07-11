package com.rnbridgetest

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.*

class RNAppSamuraiPackage : ReactPackage {

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return listOf(RNAppSamuraiBannerViewManager())
    }

    override fun createNativeModules(
            reactContext: ReactApplicationContext): List<NativeModule> {
        val modules = ArrayList<NativeModule>()

        modules.add(RNAppSamuraiInterstitialAdModule(reactContext))
        modules.add(RNAppSamuraiRewardedVideoAdModule(reactContext))

        return modules
    }
}