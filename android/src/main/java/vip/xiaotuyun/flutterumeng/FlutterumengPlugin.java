package vip.xiaotuyun.flutterumeng;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.UMAuthListener;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterumengPlugin */
public class FlutterumengPlugin implements MethodCallHandler {
  private Context context;
  private Registrar registrar;

  private FlutterumengPlugin(Registrar registrar, Context context) {
    this.registrar = registrar;
    this.context = context;
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutterumeng");
    channel.setMethodCallHandler(new FlutterumengPlugin(registrar, registrar.context()));
  }

  @Override
  public void onMethodCall(MethodCall call, MethodChannel.Result result) {
    switch (call.method) {
      case "init":
        init(call, result);
        break;
      case "loginWx":
        login(SHARE_MEDIA.WEIXIN, result);
        break;
      default:
        result.notImplemented();
    }
  }

  private void init(MethodCall call, MethodChannel.Result result) {
    Boolean logEnabled = call.argument("logEnabled");
    if (logEnabled == null) {
      logEnabled = false;
    }
    UMConfigure.setLogEnabled(logEnabled);
    final String androidKey = call.argument("androidKey");
    final String channel = call.argument("channel");
    final String wxAppKey = call.argument("wxAppKey");
    final String wxAppSecret = call.argument("wxAppSecret");
    UMConfigure.init(context, androidKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
    PlatformConfig.setWeixin(wxAppKey, wxAppSecret);

    result.success(true);
  }
// vip.xiaotuyun.xtylive
  private void login(SHARE_MEDIA platform, final MethodChannel.Result result) {
    UMShareAPI.get(context).getPlatformInfo(registrar.activity(), platform, new UMAuthListener() {
      @Override
      public void onStart(SHARE_MEDIA share_media) {

      }

      @Override
      public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        map.put("status", "success");
        result.success(map);

      }

      @Override
      public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        result.success(map);
      }

      @Override
      public void onCancel(SHARE_MEDIA share_media, int i) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        result.success(map);
      }
    });
  }
}
