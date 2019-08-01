
import React, { Component } from 'react';
import {
  requireNativeComponent,
  UIManager,
  findNodeHandle,
  ViewPropTypes,
} from 'react-native';
import { string, func, arrayOf, any } from 'prop-types';

// import { createErrorFromErrorData } from './utils';

class AppSamuraiBanner extends Component {

  constructor() {
    super();
    this.handleSizeChange = this.handleSizeChange.bind(this);
    this.handleAdFailedToLoad = this.handleAdFailedToLoad.bind(this);
    this.state = {
      style: {},
    };
  }

  componentDidMount() {
    this.loadBanner();
  }

  loadBanner() {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this._bannerView),
      UIManager.getViewManagerConfig('RNASBannerView').Commands.loadBanner,
      null,
    );
  }

  handleSizeChange(event) {
    const { height, width } = event.nativeEvent;
    this.setState({ style: { width, height } });
    if (this.props.onSizeChange) {
      this.props.onSizeChange({ width, height });
    }
  }

  handleAdFailedToLoad(event) {
    if (this.props.onAdFailedToLoad) {
      // this.props.onAdFailedToLoad(createErrorFromErrorData(event.nativeEvent.error));
    }
  }

  render() {
    return (
      <RNASBannerView
        {...this.props}
        style={[this.props.style, this.state.style]}
        onSizeChange={this.handleSizeChange}
        onAdFailedToLoad={this.handleAdFailedToLoad}
        ref={el => (this._bannerView = el)}
      />
    );
  }
}

AppSamuraiBanner.propTypes = {
  ...ViewPropTypes,

  /**
   * banner size constants
   * banner (320x50, Standard Banner for Phones and Tablets)
   * mediumRectangle (300x250, IAB Medium Rectangle for Phones and Tablets)
   *
   * banner is default
   */
  adSize: string,

  /**
   * AppSamurai ad unit ID
   */
  adUnitID: string,

  /**
   * Google Ads ad unit ID
   */

  gadAdUnitID: string,

  /**
   * Array of test devices.
   */
  testDevices: arrayOf(string),

  /**
   * iOS library events
   */
  onSizeChange: func,
  onAdLoaded: func,
  onAdFailedToLoad: func,
  onAdOpened: func,
  onAdClosed: func,
  onAdLeftApplication: func,
};

const RNASBannerView = requireNativeComponent('RNASBannerView', AppSamuraiBanner);

export default AppSamuraiBanner;
