import { default as AlertAndroid } from './lib/Alert';
import { Alert as AlertIOS, Platform } from 'react-native';

export default Platform.select({
  android: AlertAndroid as any,
  ios: AlertIOS,
});
