/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Fragment, Component } from 'react';
import {
    SafeAreaView,
    StyleSheet,
    ScrollView,
    View,
    Text,
    StatusBar,
    Button,
    Platform
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
    AppSamuraiInterstitial
} from 'react-native-appsamurai-ads';

const bannerAdUnitId = Platform.OS === 'ios' ? 'appsamurai-sample-ios-banner-ad-id' : 'appsamurai-sample-android-banner-ad-id'
const gadBannerAdUnitId = Platform.OS === 'ios' ? '/6499/example/banner' : '/6499/example/banner'

export default class App extends Component<{}> {
    state = {
        log: ""
    };

    render() {
        return (
            <Fragment>
                <StatusBar barStyle="dark-content" />
                <SafeAreaView>
                    <ScrollView
                        contentInsetAdjustmentBehavior="automatic"
                        style={styles.scrollView}>
                        {global.HermesInternal == null ? null : (
                            <View style={styles.engine}>
                                <Text style={styles.footer}>Engine: Hermes</Text>
                            </View>
                        )}
                        <View style={styles.body}>
                            <Text style={styles.log}>{this.state.log}</Text>
                            <View style={styles.sectionContainer}>
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
                            <View style={styles.sectionContainer}>
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
                            </View>
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
                        </View>
                    </ScrollView>
                </SafeAreaView>
            </Fragment>
        )
    }

    showInterstitial = () => {
        AppSamuraiInterstitial.showAd();
    }

    loadInterstitial = () => {
        var interstitialAdUnitId = Platform.OS === 'ios' ? 'appsamurai-sample-ios-interstitial-ad-id' : 'appsamurai-sample-android-interstitial-ad-id'
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

        AppSamuraiInterstitial.requestAd()
            .then(() =>
                AppSamuraiInterstitial.showAd()
            )
            .catch(error => {
                console.warn("An error occurred while requesting ad");
                console.warn(error);
            }
            );
    }

    showRewarded = () => {
        AppSamuraiRewarded.showAd();
    }

    loadRewarded = () => {
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

        AppSamuraiRewarded.requestAd().catch(error => console.warn(error));
    }

    setLog(log) {
        this.setState({
            log: log
        })

        console.log(log)
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
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    sectionContainer: {
        marginTop: 32,
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
    log: {
        flex: 1,
        justifyContent: 'flex-start',
    }
});

// export default App;
