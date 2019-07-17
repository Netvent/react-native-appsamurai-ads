package com.appsamurai.ads.rnbridge

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import java.util.*

class RNAppSamuraiAdsPackage : ReactPackage {

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