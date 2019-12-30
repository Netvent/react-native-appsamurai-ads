# React Native App Samurai Ads
React Native App Samurai Ads is a project for React Native to enable usage of App Samurai Ads SDK with Google AdMob waterfall flow. It supports Banner, Interstitial and Rewarded Video ad formats. Banner format is implemented as components while Interstitial and Rewarded Video hava an imperative API.
## Native AppSamurai Ad SDKs
App Samurai Ads has native SDKs both for iOS and Android.
### iOS
[`AppSamurai Ads iOS SDK`](https://github.com/Netvent/appsamurai-adsdk-ios)
### Android
[`AppSamurai Ads Android SDK`](https://github.com/Netvent/appsamurai-adsdk-android)
## Getting Started
Add react-native-appsamurai-ads to your dependencies with one of the options below(yarn or npm);
``` shell
yarn add react-native-appsamurai-ads
```
``` shell
npm install --save react-native-appsamurai-ads
```
*Linking library*
react-native 0.60+ handles autolinking as it mentioned in [autolinking in react-native](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md).
For react-native 0.60- version auto linking needs to be done to use libraries with native dependencies correctly. Please refer detailed explanation from [Linking Libraries in iOS](https://facebook.github.io/react-native/docs/linking-libraries-ios.html)
``` shell
react-native link react-native-appsamurai-ads
```
### iOS Platform Notes
Projects that use CocoaPods do not forget to run `pod install`

You need to initialize ASMobileAds to use App Samurai Ads. You need to add to application’s `didFinishLaunchingWithOptions` method following snippet;
```objc
[ASMobileAds initialize:@"appsamurai-sample-ios-app-id"];
```
You need to import related modules as well;
```objc
@import AppSamuraiAdSDK;
```
### Android Platform Notes
You need to add `multidex` support to your Android project. You can do this by adding following line to app’s gradle.
```groovy
defaultConfig {
    ....
    multiDexEnabled true
    ...
}
```

This is a Waterfall SDK that uses Google AdMob to increase publisher’s fill rate. You need to handle Google AdMob initialization process as well, please follow guides on [Google AdMob Update your AndroidManifest.xml](https://developers.google.com/admob/android/quick-start#update_your_androidmanifestxml) documentation.
You need to initialize ASMobileAds to use App Samurai Ads. You need to add to application’s `onCreate` method following snippet's related parts;
```java
@Override
public void onCreate() {
    super.onCreate();
    // initialize MobileAds with suitable parameters
    HashMap<AdNetwork, String> appIdMap = new HashMap<>();
    appIdMap.put(AdNetwork.GOOGLE, "ca-app-pub-3940256099942544~3347511713");
    appIdMap.put(AdNetwork.APPSAMURAI, "appsamurai-sample-android-app-id");

    MobileAds.Companion.initialize(this, appIdMap);
}
```
Add `dependencies` following lines in app/build.gradle
```groovy
implementation 'com.appsamurai.adsdk:core:1.4.1'
implementation('com.appsamurai.adsdk:waterfall:0.1.2') {
    exclude module: 'core'
}
```
## Usage
### Banner
Importing AppSamuraiBanner
``` js
import {
  AppSamuraiBanner
} from 'react-native-appsamurai-ads';
```
Adding as react element
``` js
<AppSamuraiBanner
    adSize="banner"
    adUnitID= {bannerAdUnitId}
    gadAdUnitID= {gadBannerAdUnitId}
    testDevices={[
        'test-device-id-1',
        'test-device-id-2',
        'test-device-id-3'
    ]}
    onAdLoaded={() => {
        this.setLog('AppSamuraiBanner adLoaded');
    }}
    onAdFailedToLoad={() => {
        this.setLog('AppSamuraiBanner onAdFailedToLoad');
    }}
    onAdLeftApplication={() => {
        this.setLog('AppSamuraiBanner onAdLeftApplication');
    }}
/>
```
You need to use test ids for testing purposes
```js
const bannerAdUnitId = Platform.OS === 'ios' ? 'appsamurai-sample-ios-banner-ad-id' : 'appsamurai-sample-android-banner-ad-id'
const gadBannerAdUnitId = Platform.OS === 'ios' ? '/6499/example/banner' : '/6499/example/banner'
```
`adUnitID`: AppSamurai Ads Ad Unit ID
`gadAdUnitID`: Google AdMob Ad Unit ID
`testDevices`: Array of test device IDs both for AppSamurai Ads and Google AdMob
### Supported Banner Sizes
Use `adSize` property for banner size. Default size is banner (320x50)
`banner`: 320x50, Standard Banner for Phones and Tablets
`mediumRectangle`: 300x250, IAB Medium Rectangle for Phones and Tablets
### Callback Methods
`onAdLoaded`: Called when an ad is received.
`onAdFailedToLoad`: Called when an ad request failed.
`onAdLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.
### Interstitial
Importing AppSamuraiInterstitial
``` js
import {
    AppSamuraiInterstitial
} from 'react-native-appsamurai-ads';
```
Requesting Ad
``` js
var interstitialAdUnitId = Platform.OS === 'ios' ? 'appsamurai-sample-ios-banner-ad-id' : 'appsamurai-sample-android-banner-ad-id'
var gadInterstitialAdUnitId = Platform.OS === 'ios' ? '/6499/example/interstitial' : '/6499/example/interstitial'

var testDeviceIDs = [
    'test-device-id-1',
    'test-device-id-2',
    'test-device-id-3'
];

AppSamuraiInterstitial.setTestDevices(testDeviceIDs);
AppSamuraiInterstitial.setAdUnitID(interstitialAdUnitId);
AppSamuraiInterstitial.setGADAdUnitID(gadInterstitialAdUnitId);
AppSamuraiInterstitial.addEventListener('adLoaded',
    () => this.setLog('AppSamuraiInterstitial adLoaded')
);
AppSamuraiInterstitial.addEventListener('adFailedToLoad',
    () => this.setLog('AppSamuraiInterstitial adFailedToLoad')
);
AppSamuraiInterstitial.addEventListener('adOpened',
    () => this.setLog('AppSamuraiInterstitial adOpened')
);
AppSamuraiInterstitial.addEventListener('adClosed',
    () => this.setLog('AppSamuraiInterstitial adClosed')
);
AppSamuraiInterstitial.addEventListener('adLeftApplication',
    () => this.setLog('AppSamuraiInterstitial adLeftApplication')
);
AppSamuraiInterstitial.requestAd();
```
Please note that, you need to use test ids for testing purposes
Showing ad when it is ready
``` js
AppSamuraiInterstitial.showAd();
```
Also you can show ad with promise when it is ready
``` js
AppSamuraiInterstitial.requestAd()
.then(() =>
    AppSamuraiInterstitial.showAd()
)
.catch(error => {
        console.warn("An error occurred while requesting ad");
    }
);
```
### Callback Methods
`adLoaded`: Called when an ad is received.
`adFailedToLoad`: Called when an ad request failed.
`adOpened`: Called when an ad opens an overlay that covers the screen.
`adClosed`: Called when the user is about to return to the application after clicking on an ad.
`adLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.
### Methods
`setAdUnitIDs(adUnitIDs)`: Sets the AdUnit IDs for both AppSamurai Ads and Google AdMob
`setTestDevices(testDeviceIDs)`: Sets the devices which are served test ads.
`requestAd`: Requests an interstitial and returns a promise, which resolves on load and rejects on error.
`showAd`: Shows an interstitial and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.
`isReady(callback)`: Calls callback with a boolean value whether the interstitial is ready to be shown.
`addEventListener`: Adds an event to listener
`removeEventListener`: Removes an event from listener
`removeAllListeners`: Removes all events from listener
### Rewarded Video
Importing AppSamuraiRewarded
``` js
import {
    AdNetwork,
    AppSamuraiRewarded
} from 'react-native-appsamurai-ads';
```
Requesting Ad
``` js
var rewardedAdUnitId = Platform.OS === 'ios' ? 'appsamurai-sample-ios-rewardbasedvideo-ad-id' : 'appsamurai-sample-android-rewardbasedvideo-ad-id'
var gadRewardedAdUnitId = Platform.OS === 'ios' ? '/6499/example/rewarded-video' : '/6499/example/rewarded-video'

var testDeviceIDs = [
    'test-device-id-1',
    'test-device-id-2',
    'test-device-id-3'
];

AppSamuraiRewarded.setTestDevices(testDeviceIDs);
AppSamuraiRewarded.setAdUnitID(rewardedAdUnitId);
AppSamuraiRewarded.setGADAdUnitID(gadRewardedAdUnitId);
AppSamuraiRewarded.addEventListener('adLoaded',
    () => this.setLog('AppSamuraiRewarded adLoaded')
);
AppSamuraiRewarded.addEventListener('adFailedToLoad',
    () => this.setLog('AppSamuraiRewarded adFailedToLoad')
);
AppSamuraiRewarded.addEventListener('adOpened',
    () => this.setLog('AppSamuraiRewarded adOpened')
);
AppSamuraiRewarded.addEventListener('adClosed',
    () => this.setLog('AppSamuraiRewarded adClosed')
);
AppSamuraiRewarded.addEventListener('adLeftApplication',
    () => this.setLog('AppSamuraiRewarded adLeftApplication')
);
AppSamuraiRewarded.addEventListener('rewarded',
    () => this.setLog('AppSamuraiRewarded rewarded')
);
AppSamuraiRewarded.addEventListener('videoStarted',
    () => this.setLog('AppSamuraiRewarded videoStarted')
);
AppSamuraiRewarded.addEventListener('videoCompleted',
    () => this.setLog('AppSamuraiRewarded videoCompleted')
);

AppSamuraiRewarded.requestAd();
```
Please note that, you need to use test ids for testing purposes
Showing ad when it is ready
``` js
AppSamuraiRewarded.showAd();
```
Also you can show ad with promise when it is ready
``` js
AppSamuraiRewarded.requestAd()
.then(() =>
    AppSamuraiRewarded.showAd()
)
.catch(error => {
        console.warn("An error occurred while requesting ad");
    }
);
```
### Callback Methods
`adLoaded`: Called when an ad is received.
`adFailedToLoad`: Called when an ad request failed.
`adOpened`: Called when an ad opens an overlay that covers the screen.
`adClosed`: Called when the user is about to return to the application after clicking on an ad.
`adLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.
`rewarded`: Called when the user eard reward.
`videoStarted`: Called when the video is started.
`videoCompleted`: Called when the video is completed.
### Methods
`setAdUnitIDs(adUnitIDs)`: Sets the AdUnit IDs for both AppSamurai Ads and Google AdMob
`setTestDevices(testDeviceIDs)`: Sets the devices which are served test ads.
`requestAd`: Requests an interstitial and returns a promise, which resolves on load and rejects on error.
`showAd`: Shows an interstitial and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.
`isReady(callback)`: Calls callback with a boolean value whether the interstitial is ready to be shown.
`addEventListener`: Adds an event to listener
`removeEventListener`: Removes an event from listener
`removeAllListeners`: Removes all events from listener
## Credits
[`react-native-admob`](_https://github.com/sbugert/react-native-admob_) has been a great source of inspiration for this project.
## Author
App Samurai Mobile Team, mobile@appsamurai.com
## License
MIT
[![alt text](https://appsamurai.com/wp-content/uploads/2014/12/web_home_cta_2.png "AppSamurai")](https://www.appsamurai.com)
