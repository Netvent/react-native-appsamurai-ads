package com.appsamurai.ads.rnbridge

import android.view.View

import com.appsamurai.ads.common.AdSize
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableNativeArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.ThemedReactContext


class RNAppSamuraiBannerViewManager : ViewGroupManager<ReactAdView>() {

    override fun getName(): String {
        return REACT_CLASS
    }

    override fun createViewInstance(themedReactContext: ThemedReactContext): ReactAdView {
        return ReactAdView(themedReactContext)
    }

    override fun addView(parent: ReactAdView, child: View, index: Int) {
        throw RuntimeException("RNASBannerView cannot have subviews")
    }

    override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
        val builder = MapBuilder.builder<String, Any>()
        val events = arrayOf(EVENT_SIZE_CHANGE, EVENT_AD_LOADED, EVENT_AD_FAILED_TO_LOAD, EVENT_AD_OPENED, EVENT_AD_CLOSED, EVENT_AD_LEFT_APPLICATION)
        for (i in events.indices) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]))
        }
        return builder.build()
    }

    @ReactProp(name = PROP_AD_SIZE)
    fun setPropAdSize(view: ReactAdView, sizeString: String) {
        val adSize = getAdSizeFromString(sizeString)
        view.adSize = adSize

    }

    @ReactProp(name = PROP_AD_UNIT_ID)
    fun setPropAdUnitID(view: ReactAdView, adUnitID: String) {
        view.adUnitID = adUnitID
    }

    @ReactProp(name = PROP_GAD_AD_UNIT_ID)
    fun setPropGadAdUnitID(view: ReactAdView, gadAdUnitID: String) {
        view.gadAdUnitID = gadAdUnitID
    }

    @ReactProp(name = PROP_TEST_DEVICES)
    fun setPropTestDevices(view: ReactAdView, testDevices: ReadableArray) {
        val nativeArray = testDevices as ReadableNativeArray
        val list = nativeArray.toArrayList()

        val testDevices: ArrayList<String> = arrayListOf()
        for (testDevice in list){
            testDevices.add(testDevice as String)
        }

        view.testDevices = testDevices.toTypedArray()
    }

    private fun getAdSizeFromString(adSize: String): AdSize {
        return when (adSize) {
            "banner" -> AdSize.BANNER
            "mediumRectangle" -> AdSize.MEDIUM_RECTANGLE
            else -> AdSize.BANNER
        }
    }

    override fun getCommandsMap(): Map<String, Int>? {
        return MapBuilder.of("loadBanner", COMMAND_LOAD_BANNER)
    }

    override fun receiveCommand(root: ReactAdView, commandId: Int, args: ReadableArray?) {
        when (commandId) {
            COMMAND_LOAD_BANNER -> {
                root.loadBanner()
            }
        }
    }

    companion object {

        const val REACT_CLASS = "RNASBannerView"

        const val PROP_AD_SIZE = "adSize"
        const val PROP_AD_UNIT_ID = "adUnitID"
        const val PROP_GAD_AD_UNIT_ID = "gadAdUnitID"
        const val PROP_TEST_DEVICES = "testDevices"

        const val EVENT_SIZE_CHANGE = "onSizeChange"
        const val EVENT_AD_LOADED = "onAdLoaded"
        const val EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad"
        const val EVENT_AD_OPENED = "onAdOpened"
        const val EVENT_AD_CLOSED = "onAdClosed"
        const val EVENT_AD_LEFT_APPLICATION = "onAdLeftApplication"

        const val COMMAND_LOAD_BANNER = 1
    }
}
