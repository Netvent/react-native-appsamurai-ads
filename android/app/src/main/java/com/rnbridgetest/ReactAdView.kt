package com.rnbridgetest

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.appsamurai.ads.common.AdRequest
import com.appsamurai.ads.common.AdSize
import com.appsamurai.ads.data.AdNetwork
import com.appsamurai.waterfall.ad.BannerAd
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import java.util.HashMap

class ReactAdView(context: Context) : ReactViewGroup(context) {

    private var mBannerAd: BannerAd? = null

    var adUnitID: String? = null
        set(value) {
            field = value
        }
    var gadAdUnitID: String? = null
    var testDevices: Array<String> = arrayOf()
    var adSize = AdSize.BANNER
    lateinit var adContainer: LinearLayout

    init {
        this.createAdView()
    }

    private fun createContainer(context: Context) {
        adContainer = LinearLayout(context)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //        params.gravity = Gravity.CENTER_HORIZONTAL;
        adContainer.layoutParams = params
        adContainer.setBackgroundColor(Color.CYAN)
        adContainer.orientation = LinearLayout.VERTICAL
    }

    private fun createAdView() {
        val context = context

        this.createContainer(context)
        mBannerAd = BannerAd(context, adContainer)

        mBannerAd?.setAdListener(object : com.appsamurai.ads.common.AdListener() {
            override fun onAdClosed() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_CLOSED, null)
            }

            override fun onAdFailedToLoad(n: Int) {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_FAILED_TO_LOAD, null)
            }

            override fun onAdLeftApplication() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LEFT_APPLICATION, null)
            }

            override fun onAdLoaded() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LOADED, null)
                val width = adSize.getWidthInPixels(null)// 960; //mBannerAd.getAdSize().getWidth();
                val height = adSize.getHeightInPixels(null) // 150; //mBannerAd.getAdSize().getHeight();
                val left = 0
                val top = 0
                adContainer.measure(width, height)
                adContainer.layout(left, top, left + width, top + height)
                sendOnSizeChangeEvent()
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LOADED, null)
            }

            override fun onAdOpened() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_OPENED, null)
            }

            override fun onAdExpanded() {
                super.onAdExpanded()
            }

            override fun onAdCollapsed() {
                super.onAdCollapsed()
            }
        })
        this.addView(this.adContainer)
    }

    private fun sendOnSizeChangeEvent() {
        val width: Int
        val height: Int
        val reactContext = context as ReactContext
        val event = Arguments.createMap()
        width = adSize.width
        height = adSize.height
        event.putDouble("width", width.toDouble())
        event.putDouble("height", height.toDouble())
        sendEvent(RNAppSamuraiBannerViewManager.EVENT_SIZE_CHANGE, event)
    }

    private fun sendEvent(name: String, event: WritableMap?) {
        val reactContext = context as ReactContext
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
                id,
                name,
                event)
    }

    fun loadBanner() {
        val map = HashMap<AdNetwork, String>()
        adUnitID?.let {
            map[AdNetwork.APPSAMURAI] = it
        }
        gadAdUnitID?.let {
            map[AdNetwork.GOOGLE] = it
        }
        this.mBannerAd?.adUnitIds = map
        this.mBannerAd?.adSize = adSize
        val adRequestBuilder = AdRequest.Builder()
        for (testDevice in testDevices) {
            adRequestBuilder.addTestDevice(testDevice)
        }
        val adRequest = adRequestBuilder.build()
        Log.d(Utils.LOGTAG, "TestDevices ${adRequest.testDevices}")
        this.mBannerAd?.loadAd(adRequest)
    }
}