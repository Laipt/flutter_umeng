import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class Flutterumeng {
  static const MethodChannel _channel = const MethodChannel('flutterumeng');

  static Future<bool> init({
    @required String androidKey,
    @required String wxAppKey,
    @required String wxAppSecret,
    String channel,
    bool logEnabled = false,
  }) async {
    Map<String, dynamic> map = {
      'androidKey': androidKey,
      'channel': channel,
      'logEnabled': logEnabled,
      'wxAppKey': wxAppKey,
      'wxAppSecret': wxAppSecret,
    };
    return await _channel.invokeMethod<bool>('init', map);
  }

  static Future<Map> get loginWechat async {
    final Map resultMap = await _channel.invokeMethod('loginWx');
    return resultMap;
  }
}
