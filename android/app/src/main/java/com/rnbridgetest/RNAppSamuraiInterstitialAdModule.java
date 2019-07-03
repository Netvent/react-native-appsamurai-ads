package com.rnbridgetest;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appsamurai.ads.common.AdListener;
import com.appsamurai.ads.common.AdRequest;
import com.appsamurai.ads.data.AdNetwork;
import com.appsamurai.waterfall.ad.InterstitialAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.HashMap;


public class RNAppSamuraiInterstitialAdModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "RNAppSamuraiInterstitial";

    public static final String EVENT_AD_LOADED = "interstitialAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "interstitialAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "interstitialAdOpened";
    public static final String EVENT_AD_CLOSED = "interstitialAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "interstitialAdLeftApplication";


    InterstitialAd mInterstitialAd;
    String[] testDevices;

    private Promise mRequestAdPromise;
    HashMap<AdNetwork, String> adUnitIDs = new HashMap<>();

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNAppSamuraiInterstitialAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void setAdUnitIDs(ReadableMap adUnitIDs) {
        this.adUnitIDs = convertAdUnitIdMap(adUnitIDs);
        Log.d(Utils.LOGTAG, "AdUnitIDs: " + this.adUnitIDs.toString());
    }

    @ReactMethod
    public void setTestDevices(ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        this.testDevices = list.toArray(new String[list.size()]);
    }

    @ReactMethod
    public void requestAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if ( mInterstitialAd == null ){
                mInterstitialAd = new InterstitialAd(getCurrentActivity());

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        sendEvent(EVENT_AD_CLOSED, null);
                    }
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        String errorString = "ERROR_UNKNOWN";
                        String errorMessage = "Unknown error";
                        WritableMap event = Arguments.createMap();
                        WritableMap error = Arguments.createMap();
                        event.putString("message", errorMessage);
                        sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
                        mRequestAdPromise.reject(errorString, errorMessage);
                    }
                    @Override
                    public void onAdLeftApplication() {
                        sendEvent(EVENT_AD_LEFT_APPLICATION, null);
                    }
                    @Override
                    public void onAdLoaded() {
                        sendEvent(EVENT_AD_LOADED, null);
                        mRequestAdPromise.resolve(null);
                    }
                    @Override
                    public void onAdOpened() {
                        sendEvent(EVENT_AD_OPENED, null);
                    }
                });
            }

                if (mInterstitialAd.isLoaded()) {
                    promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.");
                } else {
                    mRequestAdPromise = promise;
                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            String testDevice = testDevices[i];
//                            if (testDevice == "SIMULATOR") {
//                                testDevice = AdRequest.DEVICE_ID_EMULATOR;
//                            }
                            adRequestBuilder.addTestDevice(testDevice);
                        }
                    }
                    AdRequest adRequest = adRequestBuilder.build();
                    mInterstitialAd.setAdUnitIds(adUnitIDs);
                    mInterstitialAd.loadAd(adRequest);
                }
            });
    }

    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                promise.resolve(null);
            } else {
                promise.reject("E_AD_NOT_READY", "Ad is not ready.");
            }
        });
    }

    @ReactMethod
    public void isReady(final Callback callback) {
        new Handler(Looper.getMainLooper()).post(() -> callback.invoke(mInterstitialAd.isLoaded()));
    }

    private HashMap<AdNetwork, String> convertAdUnitIdMap(ReadableMap adUnitIDs){
        HashMap<AdNetwork, String> map = new HashMap<>();
        if ( adUnitIDs.hasKey("0") ) {
            map.put(AdNetwork.APPSAMURAI, adUnitIDs.getString("0"));
        }

        if ( adUnitIDs.hasKey("1") ) {
            map.put(AdNetwork.GOOGLE, adUnitIDs.getString("1"));
        }

        return map;
    }
}
