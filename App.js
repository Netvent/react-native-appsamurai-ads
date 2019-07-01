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

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class App extends Component<{}> {
  state = { 
    log: ""
  };

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>React Native Bridge Test App!</Text>
        <Button
          onPress={this.onPress}
          title="Press Me"
          color="#841584"
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
    this.setLog(`OS type is ${platform}`);
    this.showAlert()
  }

  setLog(log){
    this.setState({
      log: log
    })

    console.log(log)
  }

  showAlert = () => {
    NativeModules.AlertModule.showAlert('Alert', 'Are you sure?', 'Yes', 'No', 
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
