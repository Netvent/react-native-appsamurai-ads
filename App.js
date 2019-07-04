/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Button} from 'react-native';
import {NativeModules} from 'react-native';
import AppSamuraiInterstitial from './RNAppSamuraiInterstitial';
import AppSamuraiRewarded from './RNAppSamuraiRewarded';
import AppSamuraiBanner from './RNAppSamuraiBanner';


export default class App extends Component<{}> {
  state = { 
    log: ""
  };

  componentDidMount() {
  }

  componentWillUnmount() {
    AppSamuraiInterstitial.removeAllListeners();
    AppSamuraiRewarded.removeAllListeners();
  }

  showInterstitial = () => {
    AppSamuraiInterstitial.showAd();
  }

  loadInterstitial = () => {
    var adUnitIDs = {
      "1": 'ca-app-pub-3940256099942544/1033173712',
      "0": 'appsamurai-sample-android-interstitial-ad-id'
    }
    AppSamuraiInterstitial.setAdUnitIDs(adUnitIDs);
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

    AppSamuraiInterstitial.requestAd().catch(error => console.warn(error));
  }

  showRewarded = () => {
    AppSamuraiRewarded.showAd();
  }

  loadRewarded = () => {
    var adUnitIDs = {
      "1": 'ca-app-pub-3940256099942544/5224354917',
      "0": 'appsamurai-sample-android-rewardbasedvideo-ad-id'
    }
    AppSamuraiRewarded.setAdUnitIDs(adUnitIDs);
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

    AppSamuraiRewarded.requestAd().catch(error => console.warn(error));
  }

  loadBanner = () => {

  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>React Native Test Bridge</Text>
        <Button
          onPress={this.onPress}
          title="Press Me"
          color="#841584"
        />
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
        <Button
          onPress={this.loadRewarded}
          title="Load Rewarded Ad"
          color="#841584"
        />
        <Button
          onPress={this.showRewarded}
          title="Show Rewarded Ad"
          color="#841584"
        />
        <Button
          title="Reload"
          onPress={this.loadBanner}
          />
        <AppSamuraiBanner
          adSize="banner"
          onAdLoaded={() => {
            this.setLog('AppSamuraiBanner adLoaded')
          }}
          adUnitID="ca-app-pub-3940256099942544/6300978111"
        />

        <Text style={styles.instructions}>{this.state.log}</Text>
      </View>
    );
  }

  onPress = () => {
    var platform = "Unknown"
    if ( Platform.OS === 'android' ) {
      platform = 'Android'
    } else if (Platform.OS === 'ios') {
      platform = 'iOS'
    }
    this.showAlert()
  }

  setLog(log){
    this.setState({
      log: log
    })

    console.log(log)
  }

  loadAd = () => {
    this.setLog("Load ad pressed");
    if ( Platform.OS === 'android' ) {
      this.loadInterstitial()
    }
  }

  showAd = () => {
    this.setLog("Show ad pressed");    
    if ( Platform.OS === 'android' ) {
      this.showInterstitial()
    }
  }

  showAlert = () => {
    this.setLog(`Showing alert view`);

    NativeModules.Alert.show('Alert', 'Are you sure?', 'Yes', 'No', 
    () => {
      this.setLog(`Pressed Yes`);
    }, 
    () => {
      this.setLog(`Pressed No`);
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
