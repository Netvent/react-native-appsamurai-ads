A react-native module for AppSamurai Ads. 

With this module you can use AppSamurai Ads and Google AdMob together without an extra configuration. Supported ad formats are Banner, Interstitial and Rewarded Video. The banner type is implemented as components while the interstitial and rewarded video have an imperative API.

## Native AppSamurai Ad SDKs
AppSamurai Ads has native SDKs both for iOS and Android.
### iOS
[`AppSamurai Ads iOS SDK`](https://github.com/Netvent/appsamurai-adsdk-ios)
### Android
[`AppSamurai Ads Android SDK`](https://github.com/Netvent/appsamurai-adsdk-android)

## Installation
You can use npm to install this npm package
``` shell
npm i --save react-native-appsamurai-ads
```

Linking library
``` shell
react-native link react-native-appsamurai-ads
```

In order to use this library, you have to link it to your project first. There's excellent documentation on how to do this in the [React Native Docs](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#content).

### iOS
### Android

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
    adUnitID="appsamurai-sample-android-banner-ad-id"
    gadAdUnitID="ca-app-pub-3940256099942544/6300978111"
    testDevices={[
      '<test-device-id-1>',
      '<test-device-id-2>',
      '<test-device-id-3>'
    ]}
    onAdLoaded={()=> {
      this.setLog('AppSamuraiBanner adLoaded');
    }}
    onAdFailedToLoad={()=> {
      this.setLog('AppSamuraiBanner onAdFailedToLoad');
    }}
    onAdOpened={()=> {
      this.setLog('AppSamuraiBanner onAdOpened');
    }}
    onAdClosed={()=> {
      this.setLog('AppSamuraiBanner onAdClosed');
    }}
    onAdLeftApplication={()=> {
      this.setLog('AppSamuraiBanner onAdLeftApplication');
    }}
/>
```

`adUnitID`: AppSamurai Ads Ad Unit ID 

`gadAdUnitID`: Google AdMob Ad Unit ID 

`testDevices`: Array of test device IDs both for AppSamurai Ads and Google AdMob

#### Supported Banner Sizes
Use `adSize` property for banner size. Default size is banner (320x50)

`banner`: 320x50, Standard Banner for Phones and Tablets

`mediumRectangle`: 300x250, IAB Medium Rectangle for Phones and Tablets

#### Callback Methods
`onAdLoaded`: Called when an ad is received.

`onAdFailedToLoad`: Called when an ad request failed.

`onAdOpened`: Called when an ad opens an overlay that covers the screen.

`onAdClosed`: Called when the user is about to return to the application after clicking on an ad.

`onAdLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.

### Interstitial
Importing AppSamuraiInterstitial and AdNetwork
``` js
import {
    AdNetwork,
    AppSamuraiInterstitial
} from 'react-native-appsamurai-ads';
```
Requesting Ad
``` js
var adUnitIDs = {
  [AdNetwork.APPSAMURAI]: 'appsamurai-sample-android-interstitial-ad-id',
  [AdNetwork.GOOGLE]: 'ca-app-pub-3940256099942544/1033173712',
}
var testDeviceIDs = [
  '026A278EFB88853437C158A1AB023B9E',
  'YXBwc20tNzliNDU5YzVlZWM3NzA4Zg==',
  'test-device-id-3'
]

AppSamuraiInterstitial.setTestDevices(testDeviceIDs);
AppSamuraiInterstitial.setAdUnitIDs(adUnitIDs);
AppSamuraiInterstitial.addEventListener('adLoaded',
  () => this.setLog('AppSamuraiInterstitial adLoaded');
);
AppSamuraiInterstitial.addEventListener('adFailedToLoad',
  () => this.setLog('AppSamuraiInterstitial adFailedToLoad');
);
AppSamuraiInterstitial.addEventListener('adOpened',
  () => this.setLog('AppSamuraiInterstitial adOpened');
);
AppSamuraiInterstitial.addEventListener('adClosed',
  () => this.setLog('AppSamuraiInterstitial adClosed');
);
AppSamuraiInterstitial.addEventListener('adLeftApplication',
  () => this.setLog('AppSamuraiInterstitial adLeftApplication');
);

AppSamuraiInterstitial.requestAd();
```

Showing ad when it is ready
``` js
AppSamuraiInterstitial.showAd();
```
Also you can show ad with promise when it is ready
``` js
AppSamuraiInterstitial.requestAd()
.then(() => 
    AppSamuraiInterstitial.showAd();
)
.catch(error => {
        console.warn("An error occurred while requesting ad");
    }
);
```

#### Event lists
`adLoaded`: Called when an ad is received.

`adFailedToLoad`: Called when an ad request failed.

`adOpened`: Called when an ad opens an overlay that covers the screen.

`adClosed`: Called when the user is about to return to the application after clicking on an ad.

`adLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.

#### Methods
`setAdUnitIDs(adUnitIDs)`: Sets the AdUnit IDs for both AppSamurai Ads and Google AdMob

`setTestDevices(testDeviceIDs)`: Sets the devices which are served test ads.

`requestAd`: Requests an interstitial and returns a promise, which resolves on load and rejects on error.

`showAd`: Shows an interstitial and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.

`isReady(callback)`: Calls callback with a boolean value whether the interstitial is ready to be shown.

`addEventListener`: Adds an event to listener

`removeEventListener`: Removes an event from listener

`removeAllListeners`: Removes all events from listener

### Rewarded Video
Importing AppSamuraiRewarded and AdNetwork
``` js
import {
    AdNetwork,
    AppSamuraiRewarded
} from 'react-native-appsamurai-ads';
```

Requesting Ad
``` js
var adUnitIDs = {
  [AdNetwork.APPSAMURAI]: 'appsamurai-sample-android-rewardbasedvideo-ad-id',
  [AdNetwork.GOOGLE]: 'ca-app-pub-3940256099942544/5224354917'
};

var testDeviceIDs = [
  '026A278EFB88853437C158A1AB023B9E',
  'YXBwc20tNzliNDU5YzVlZWM3NzA4Zg==',
  'test-device-id-3'
];

AppSamuraiRewarded.setTestDevices(testDeviceIDs);
AppSamuraiRewarded.setAdUnitIDs(adUnitIDs);
AppSamuraiRewarded.addEventListener('adLoaded',
  () => this.setLog('AppSamuraiRewarded adLoaded');
);
AppSamuraiRewarded.addEventListener('adFailedToLoad',
  () => this.setLog('AppSamuraiRewarded adFailedToLoad');
);
AppSamuraiRewarded.addEventListener('adOpened',
  () => this.setLog('AppSamuraiRewarded adOpened');
);
AppSamuraiRewarded.addEventListener('adClosed',
  () => this.setLog('AppSamuraiRewarded adClosed');
);
AppSamuraiRewarded.addEventListener('adLeftApplication',
  () => this.setLog('AppSamuraiRewarded adLeftApplication');
);
AppSamuraiRewarded.addEventListener('rewarded',
  () => this.setLog('AppSamuraiRewarded rewarded');
);
AppSamuraiRewarded.addEventListener('videoStarted',
  () => this.setLog('AppSamuraiRewarded videoStarted');
);
AppSamuraiRewarded.addEventListener('videoCompleted',
  () => this.setLog('AppSamuraiRewarded videoCompleted');
);

AppSamuraiRewarded.requestAd();
```

Showing ad when it is ready
``` js
AppSamuraiRewarded.showAd();
```

Also you can show ad with promise when it is ready
``` js
AppSamuraiRewarded.requestAd()
.then(() => 
    AppSamuraiRewarded.showAd();
)
.catch(error => {
        console.warn("An error occurred while requesting ad");
    }
);
```

#### Event lists
`adLoaded`: Called when an ad is received.

`adFailedToLoad`: Called when an ad request failed.

`adOpened`: Called when an ad opens an overlay that covers the screen.

`adClosed`: Called when the user is about to return to the application after clicking on an ad.

`adLeftApplication`: Called when a user click will open another app (such as the App Store), backgrounding the current app.

`rewarded`: Called when the user eard reward.

`videoStarted`: Called when the video is started.

`videoCompleted`: Called when the video is completed.

#### Methods
`setAdUnitIDs(adUnitIDs)`: Sets the AdUnit IDs for both AppSamurai Ads and Google AdMob

`setTestDevices(testDeviceIDs)`: Sets the devices which are served test ads.

`requestAd`: Requests an interstitial and returns a promise, which resolves on load and rejects on error.

`showAd`: Shows an interstitial and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.

`isReady(callback)`: Calls callback with a boolean value whether the interstitial is ready to be shown.

`addEventListener`: Adds an event to listener

`removeEventListener`: Removes an event from listener

`removeAllListeners`: Removes all events from listener

## Credits
[`react-native-admob`](https://github.com/sbugert/react-native-admob) has been a great source of inspiration for this project.

## License
MIT
