/* eslint-disable global-require */
module.exports = {
  get AppSamuraiBanner() {
    return require('./RNAppSamuraiBanner').default;
  },
  get AppSamuraiInterstitial() {
    return require('./RNAppSamuraiInterstitial').default;
  },
  get AppSamuraiRewarded() {
    return require('./RNAppSamuraiRewarded').default;
  },
  get AdNetwork() {
    return require('./Constants').AdNetwork;
  },
};