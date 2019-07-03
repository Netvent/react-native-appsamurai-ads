package com.rnbridgetest;

import android.app.Application;

import com.appsamurai.waterfall.ad.MobileAds;
import com.appsamurai.ads.data.AdNetwork;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNAlertPackage(),
//          new RNInterstitialPackage(),
          new RNAppSamuraiPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
//    MobileAds.initialize(getApplicationContext(), MobileAds.SAMPLE_APP_ID);

    HashMap<AdNetwork, String> appIdMap = new HashMap<>();
    appIdMap.put(AdNetwork.GOOGLE, "ca-app-pub-3940256099942544~3347511713");
    appIdMap.put(AdNetwork.APPSAMURAI, "appsamurai-sample-android-app-id");

    MobileAds.Companion.initialize(this, appIdMap);
//    MobileAds.initialize(this, appIdMap);
  }
}
