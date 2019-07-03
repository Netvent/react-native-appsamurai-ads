package com.rnbridgetest

import android.app.Application

import com.appsamurai.waterfall.ad.MobileAds
import com.appsamurai.ads.data.AdNetwork
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader

import java.util.Arrays
import java.util.HashMap

class MainApplication : Application(), ReactApplication {

    private val mReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            return Arrays.asList(
                    MainReactPackage(),
                    RNAlertPackage(),
                    //          new RNInterstitialPackage(),
                    RNAppSamuraiPackage()
            )
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, /* native exopackage */ false)
        //    MobileAds.initialize(getApplicationContext(), MobileAds.SAMPLE_APP_ID);

        val appIdMap = HashMap<AdNetwork, String>()
        appIdMap[AdNetwork.GOOGLE] = "ca-app-pub-3940256099942544~3347511713"
        appIdMap[AdNetwork.APPSAMURAI] = "appsamurai-sample-android-app-id"

        MobileAds.initialize(this, appIdMap)
        //    MobileAds.initialize(this, appIdMap);
    }
}
