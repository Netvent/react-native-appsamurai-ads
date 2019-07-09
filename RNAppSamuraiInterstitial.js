import {
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const AppSamuraiInterstitial = NativeModules.RNAppSamuraiInterstitial;

const eventEmitter = new NativeEventEmitter(AppSamuraiInterstitial);

const eventMap = {
  adLoaded: 'interstitialAdLoaded',
  adFailedToLoad: 'interstitialAdFailedToLoad',
  adOpened: 'interstitialAdOpened',
  adClosed: 'interstitialAdClosed',
  adLeftApplication: 'interstitialAdLeftApplication',
};

const _subscriptions = new Map();

const addEventListener = (event, handler) => {
  const mappedEvent = eventMap[event];
  if (mappedEvent) {
    let listener;
    if (event === 'adFailedToLoad') {
      listener = eventEmitter.addListener(mappedEvent, error => handler(error));
    } else {
      listener = eventEmitter.addListener(mappedEvent, handler);
    }
    _subscriptions.set(handler, listener);
    return {
      remove: () => removeEventListener(event, handler),
    };
  } else {
    console.warn(`Trying to subscribe to unknown event: "${event}"`);
    return {
      remove: () => {},
    };
  }
};

const removeEventListener = (type, handler) => {
  const listener = _subscriptions.get(handler);
  if (!listener) {
    return;
  }
  listener.remove();
  _subscriptions.delete(handler);
};

const removeAllListeners = () => {
  _subscriptions.forEach((listener, key, map) => {
    listener.remove();
    map.delete(key);
  });
};

export default {
  ...AppSamuraiInterstitial,
  addEventListener,
  removeEventListener,
  removeAllListeners,
  simulatorId: 'SIMULATOR',
};
