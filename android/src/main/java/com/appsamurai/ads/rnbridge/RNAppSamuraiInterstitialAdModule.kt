package com.appsamurai.ads.rnbridge

import android.os.Handler
import android.os.Looper

import com.appsamurai.ads.common.AdListener
import com.appsamurai.ads.common.AdRequest
import com.appsamurai.ads.data.AdNetwork
import com.appsamurai.waterfall.ad.InterstitialAd
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableNativeArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

import java.util.ArrayList
import java.util.HashMap


class RNAppSamuraiInterstitialAdModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val REACT_CLASS = "RNAppSamuraiInterstitial"

        const val EVENT_AD_LOADED = "interstitialAdLoaded"
        const val EVENT_AD_FAILED_TO_LOAD = "interstitialAdFailedToLoad"
        const val EVENT_AD_OPENED = "interstitialAdOpened"
        const val EVENT_AD_CLOSED = "interstitialAdClosed"
        const val EVENT_AD_LEFT_APPLICATION = "interstitialAdLeftApplication"
    }


    private var mInterstitialAd: InterstitialAd? = null

    private var adUnitID: String? = null
    private var gadAdUnitID: String? = null
    private var testDevices: ArrayList<String> = arrayListOf()
    private var mRequestAdPromise: Promise? = null

    override fun getName(): String {
        return REACT_CLASS
    }

    private fun sendEvent(eventName: String, params: WritableMap?) {
        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, params)
    }

    @ReactMethod
    fun setAdUnitID(adUnitID: String) {
        this.adUnitID = adUnitID
    }

    @ReactMethod
    fun setGADAdUnitID(gadAdUnitID: String) {
        this.gadAdUnitID = gadAdUnitID
    }

    @ReactMethod
    fun setTestDevices(testDevices: ReadableArray) {
        val nativeArray = testDevices as ReadableNativeArray
        this.testDevices = nativeArray.toArrayList() as ArrayList<String>
    }

    @ReactMethod
    fun requestAd(promise: Promise) {
        Handler(Looper.getMainLooper()).post {
            if (mInterstitialAd == null) {
                mInterstitialAd = InterstitialAd(currentActivity!!)

                mInterstitialAd!!.setAdListener(object : AdListener() {
                    override fun onAdClosed() {
                        sendEvent(EVENT_AD_CLOSED, null)
                    }

                    override fun onAdFailedToLoad(errorCode: Int) {
                        val errorString = "ERROR_UNKNOWN"
                        val errorMessage = "Unknown error"
                        val event = Arguments.createMap()
                        val error = Arguments.createMap()
                        event.putString("message", errorMessage)
                        sendEvent(EVENT_AD_FAILED_TO_LOAD, event)
                        mRequestAdPromise!!.reject(errorString, errorMessage)
                    }

                    override fun onAdLeftApplication() {
                        sendEvent(EVENT_AD_LEFT_APPLICATION, null)
                    }

                    override fun onAdLoaded() {
                        sendEvent(EVENT_AD_LOADED, null)
                        mRequestAdPromise!!.resolve(null)
                    }

                    override fun onAdOpened() {
                        sendEvent(EVENT_AD_OPENED, null)
                    }
                })
            }

            if (mInterstitialAd!!.isLoaded) {
                promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.")
            } else {
                mRequestAdPromise = promise
                val adRequestBuilder = AdRequest.Builder()
                for (testDevice in testDevices) {
                    adRequestBuilder.addTestDevice(testDevice)
                }
                val adRequest = adRequestBuilder.build()
                mInterstitialAd?.adUnitIds = createAdUnitIdMap(adUnitId = adUnitID, gadAdUnitId = gadAdUnitID)
                mInterstitialAd?.loadAd(adRequest)
            }
        }
    }

    @ReactMethod
    fun showAd(promise: Promise) {
        Handler(Looper.getMainLooper()).post {
            if (mInterstitialAd!!.isLoaded) {
                mInterstitialAd!!.show()
                promise.resolve(null)
            } else {
                promise.reject("E_AD_NOT_READY", "Ad is not ready.")
            }
        }
    }

    @ReactMethod
    fun isReady(callback: Callback) {
        Handler(Looper.getMainLooper()).post {
            callback.invoke(mInterstitialAd!!.isLoaded)
        }
    }
}
