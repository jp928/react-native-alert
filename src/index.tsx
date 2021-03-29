import { NativeModules } from 'react-native';

type ReactNativeAlertType = {
  multiply(a: number, b: number): Promise<number>;
};

const { ReactNativeAlert } = NativeModules;

export default ReactNativeAlert as ReactNativeAlertType;
