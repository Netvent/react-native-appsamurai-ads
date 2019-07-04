package com.rnbridgetest;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.appsamurai.ads.common.AdSize;
import com.appsamurai.ads.data.AdNetwork;
import com.appsamurai.waterfall.ad.BannerAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReactAdView extends ReactViewGroup {

//    protected AdView adView;
    protected BannerAd mBannerAd;

    String adUnitID;
    String[] testDevices;
    AdSize adSize;
    LinearLayout adContainer;

    public ReactAdView(final Context context) {
        super(context);
        this.createAdView();
    }

    private void createContainer(final Context context) {
        adContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER_HORIZONTAL;
        adContainer.setLayoutParams(params);
        adContainer.setBackgroundColor(Color.CYAN);
        adContainer.setOrientation(LinearLayout.VERTICAL);
    }

    private void createAdView() {
        final Context context = getContext();

        this.createContainer(context);
        mBannerAd = new BannerAd(context, adContainer);
        mBannerAd.setAdListener(new com.appsamurai.ads.common.AdListener() {
            @Override
            public void onAdClosed() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_CLOSED, null);
            }

            @Override
            public void onAdFailedToLoad(int n) {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_FAILED_TO_LOAD, null);
            }

            @Override
            public void onAdLeftApplication() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LEFT_APPLICATION, null);
            }

            @Override
            public void onAdLoaded() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LOADED, null);
                int width = 960; //mBannerAd.getAdSize().getWidth();
                int height = 150; //mBannerAd.getAdSize().getHeight();
                int left = 0;
                int top = 0;
                adContainer.measure(width, height);
                adContainer.layout(left, top, left + width, top + height);
                sendOnSizeChangeEvent();
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_LOADED, null);
            }

            @Override
            public void onAdOpened() {
                sendEvent(RNAppSamuraiBannerViewManager.EVENT_AD_OPENED, null);
            }

            @Override
            public void onAdExpanded() {
                super.onAdExpanded();
            }

            @Override
            public void onAdCollapsed() {
                super.onAdCollapsed();
            }
        });
        this.addView(this.adContainer);
    }

    private void sendOnSizeChangeEvent() {
        int width;
        int height;
        ReactContext reactContext = (ReactContext) getContext();
        WritableMap event = Arguments.createMap();
        width = adSize.getWidth();
        height = adSize.getHeight();
        event.putDouble("width", width);
        event.putDouble("height", height);
        sendEvent(RNAppSamuraiBannerViewManager.EVENT_SIZE_CHANGE, event);
    }

    private void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }

    public void loadBanner() {
//            AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
//            if (testDevices != null) {
//                for (int i = 0; i < testDevices.length; i++) {
//                    String testDevice = testDevices[i];
//                    if (testDevice == "SIMULATOR") {
//                        testDevice = AdRequest.DEVICE_ID_EMULATOR;
//                    }
//                    adRequestBuilder.addTestDevice(testDevice);
//                }
//            }
//            AdRequest adRequest = adRequestBuilder.build();
//            this.adView.loadAd(adRequest);

            this.mBannerAd.loadAd(new com.appsamurai.ads.common.AdRequest.Builder().addTestDevice("YXBwc20tNzliNDU5YzVlZWM3NzA4Zg==").build());
    }

    public void setAdUnitID(String adUnitID) {
        if (this.adUnitID != null) {
            // We can only set adUnitID once, so when it was previously set we have
            // to recreate the view
            this.createAdView();
        }
        this.adUnitID = adUnitID;

        HashMap<AdNetwork, String> map = new HashMap<>();
//        map.put(AdNetwork.GOOGLE, adUnitID);
        map.put(AdNetwork.APPSAMURAI, adUnitID);
        this.mBannerAd.setAdUnitIds(map);
    }

    public void setTestDevices(String[] testDevices) {
        this.testDevices = testDevices;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;

        this.mBannerAd.setAdSize(adSize);
    }
}

public class RNAppSamuraiBannerViewManager extends ViewGroupManager<ReactAdView> {

    public static final String REACT_CLASS = "RNASBannerView";

    public static final String PROP_AD_SIZE = "adSize";
    public static final String PROP_AD_UNIT_ID = "adUnitID";
    public static final String PROP_TEST_DEVICES = "testDevices";

    public static final String EVENT_SIZE_CHANGE = "onSizeChange";
    public static final String EVENT_AD_LOADED = "onAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "onAdOpened";
    public static final String EVENT_AD_CLOSED = "onAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "onAdLeftApplication";

    public static final int COMMAND_LOAD_BANNER = 1;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReactAdView createViewInstance(ThemedReactContext themedReactContext) {
        ReactAdView adView = new ReactAdView(themedReactContext);
        return adView;
    }

    @Override
    public void addView(ReactAdView parent, View child, int index) {
        throw new RuntimeException("RNASBannerView cannot have subviews");
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                EVENT_SIZE_CHANGE,
                EVENT_AD_LOADED,
                EVENT_AD_FAILED_TO_LOAD,
                EVENT_AD_OPENED,
                EVENT_AD_CLOSED,
                EVENT_AD_LEFT_APPLICATION
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    @ReactProp(name = PROP_AD_SIZE)
    public void setPropAdSize(final ReactAdView view, final String sizeString) {
        AdSize adSize = getAdSizeFromString(sizeString);
        view.setAdSize(adSize);
    }

    @ReactProp(name = PROP_AD_UNIT_ID)
    public void setPropAdUnitID(final ReactAdView view, final String adUnitID) {
        view.setAdUnitID(adUnitID);
    }

    @ReactProp(name = PROP_TEST_DEVICES)
    public void setPropTestDevices(final ReactAdView view, final ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        view.setTestDevices(list.toArray(new String[list.size()]));
    }

    private AdSize getAdSizeFromString(String adSize) {
        switch (adSize) {
            case "banner":
                return AdSize.BANNER;
            case "mediumRectangle":
                return AdSize.MEDIUM_RECTANGLE;
            default:
                return AdSize.BANNER;
        }
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("loadBanner", COMMAND_LOAD_BANNER);
    }

    @Override
    public void receiveCommand(ReactAdView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_LOAD_BANNER:
                root.loadBanner();
                break;
        }
    }
}
