/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Fragment, Component} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import {
  AppSamuraiBanner,
  AppSamuraiRewarded,
  AppSamuraiInterstitial,
} from 'react-native-appsamurai-ads';


export default class App extends Component<{}> {

  showInterstitial = () => {
    AppSamuraiInterstitial.showAd();
  }

  loadInterstitial = () => {
    var adUnitIDs = {
      '0': 'appsamurai-sample-android-interstitial-ad-id',
      '1': 'ca-app-pub-3940256099942544/1033173712',
    }
    var testDeviceIDs = [
      '026A278EFB88853437C158A1AB023B9E',
      'YXBwc20tNzliNDU5YzVlZWM3NzA4Zg==',
      'test-device-id-3'
    ]

    AppSamuraiInterstitial.setTestDevices(testDeviceIDs);

    AppSamuraiInterstitial.setAdUnitIDs(adUnitIDs);
    AppSamuraiInterstitial.addEventListener('adLoaded',
      // () => this.setLog('AppSamuraiInterstitial adLoaded')
    );
    AppSamuraiInterstitial.addEventListener('adFailedToLoad',
      // () => this.setLog('AppSamuraiInterstitial adFailedToLoad')
    );
    AppSamuraiInterstitial.addEventListener('adOpened',
      // () => this.setLog('AppSamuraiInterstitial adOpened')
    );
    AppSamuraiInterstitial.addEventListener('adClosed',
      // () => this.setLog('AppSamuraiInterstitial adClosed')
    );
    AppSamuraiInterstitial.addEventListener('adLeftApplication',
      // () => this.setLog('AppSamuraiInterstitial adLeftApplication')
    );

    AppSamuraiInterstitial.requestAd()
    .then(() => 
     AppSamuraiInterstitial.showAd()
    )
    .catch(error => {
        console.warn("An error occurred while requesting ad")
        console.warn(error)
      }
    );
  }

  render() {
    return(
      <Fragment>
        <StatusBar barStyle="dark-content" />
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            style={styles.scrollView}>
            <Header />
            {global.HermesInternal == null ? null : (
              <View style={styles.engine}>
                <Text style={styles.footer}>Engine: Hermes</Text>
              </View>
            )}
            <View style={styles.body}>
              <Button
                onPress={this.loadInterstitial}
                title="Load Interstitial Ad"
                color="#841584"
              />
              <Button
                onPress={this.showInterstitial}
                title="Show Interstitial Ad"
                color="#841584"
              />              
            </View>
          </ScrollView>
        </SafeAreaView>
      </Fragment>
    )
  }
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

// export default App;
