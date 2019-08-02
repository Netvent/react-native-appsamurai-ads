package com.appsamurai.ads.rnbridge

import android.os.Handler
import android.os.Looper

import com.appsamurai.ads.common.AdRequest
import com.appsamurai.ads.data.AdNetwork
import com.appsamurai.ads.reward.RewardedVideoAdListener
import com.appsamurai.waterfall.ad.RewardedAd
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

import java.util.ArrayList
import java.util.HashMap

class RNAppSamuraiRewardedVideoAdModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val REACT_CLASS = "RNAppSamuraiRewarded"

        const val EVENT_AD_LOADED = "rewardedVideoAdLoaded"
        const val EVENT_AD_FAILED_TO_LOAD = "rewardedVideoAdFailedToLoad"
        const val EVENT_AD_OPENED = "rewardedVideoAdOpened"
        const val EVENT_AD_CLOSED = "rewardedVideoAdClosed"
        const val EVENT_AD_LEFT_APPLICATION = "rewardedVideoAdLeftApplication"
        const val EVENT_REWARDED = "rewardedVideoAdRewarded"
        const val EVENT_VIDEO_STARTED = "rewardedVideoAdVideoStarted"
        const val EVENT_VIDEO_COMPLETED = "rewardedVideoAdVideoCompleted"
    }

    private var mRewardedAd: RewardedAd? = null

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
            mRewardedAd = RewardedAd(currentActivity!!)
            mRewardedAd?.setAdListener(object : RewardedVideoAdListener() {
                override fun onRewardedVideoAdLoaded() {
                    sendEvent(EVENT_AD_LOADED, null)
                    mRequestAdPromise!!.resolve(null)
                }

                override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
                    var errorString = "ERROR_UNKNOWN"
                    var errorMessage = "Unknown error"
                    when (errorCode) {
                        AdRequest.ERROR_CODE_INTERNAL_ERROR -> {
                            errorString = "ERROR_CODE_INTERNAL_ERROR"
                            errorMessage = "Internal error, an invalid response was received from the ad server."
                        }
                        AdRequest.ERROR_CODE_INVALID_REQUEST -> {
                            errorString = "ERROR_CODE_INVALID_REQUEST"
                            errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given."
                        }
                        AdRequest.ERROR_CODE_NETWORK_ERROR -> {
                            errorString = "ERROR_CODE_NETWORK_ERROR"
                            errorMessage = "The ad request was unsuccessful due to network connectivity."
                        }
                        AdRequest.ERROR_CODE_NO_FILL -> {
                            errorString = "ERROR_CODE_NO_FILL"
                            errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory."
                        }
                    }
                    val event = Arguments.createMap()
                    val error = Arguments.createMap()
                    event.putString("message", errorMessage)
                    sendEvent(EVENT_AD_FAILED_TO_LOAD, event)
                    mRequestAdPromise!!.reject(errorString, errorMessage)
                }

                override fun onRewardedVideoAdOpened() {
                    sendEvent(EVENT_AD_OPENED, null)
                }

                override fun onRewardedVideoStarted() {
                    sendEvent(EVENT_VIDEO_STARTED, null)
                }

                override fun onRewardedVideoAdClosed() {
                    sendEvent(EVENT_AD_CLOSED, null)
                }

                override fun onRewarded() {
                    sendEvent(EVENT_REWARDED, null)
                }

                override fun onRewardedVideoAdLeftApplication() {
                    sendEvent(EVENT_AD_LEFT_APPLICATION, null)
                }

                override fun onRewardedVideoCompleted() {
                    sendEvent(EVENT_VIDEO_COMPLETED, null)
                }
            })

            if (mRewardedAd?.isLoaded!!) {
                promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.")
            } else {
                mRequestAdPromise = promise

                val adRequestBuilder = AdRequest.Builder()
                for (testDevice in testDevices) {
                    adRequestBuilder.addTestDevice(testDevice)
                }

                val adRequest = adRequestBuilder.build()
                mRewardedAd?.adUnitIds = createAdUnitIdMap(adUnitId = adUnitID, gadAdUnitId = gadAdUnitID)
                mRewardedAd?.loadAd(adRequest)
            }
        }
    }

    @ReactMethod
    fun showAd(promise: Promise) {
        Handler(Looper.getMainLooper()).post {
            if (mRewardedAd?.isLoaded!!) {
                mRewardedAd?.show()
                promise.resolve(null)
            } else {
                promise.reject("E_AD_NOT_READY", "Ad is not ready.")
            }
        }
    }

    @ReactMethod
    fun isReady(callback: Callback) {
        Handler(Looper.getMainLooper()).post { callback.invoke(mRewardedAd?.isLoaded) }
    }
}
