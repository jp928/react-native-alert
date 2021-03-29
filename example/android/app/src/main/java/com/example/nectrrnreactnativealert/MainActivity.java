package com.example.nectrrnreactnativealert;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.UiThreadUtil;

public class MainActivity extends ReactActivity {
  private static final int UI_FLAG_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    // Set the content to appear under the system bars so that the
    // content doesn't resize when the system bars hide and show.
    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    // Hide the nav bar and status bar
    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    | View.SYSTEM_UI_FLAG_FULLSCREEN;
  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "ReactNativeAlertExample";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    this._setImmersive();
    super.onCreate(null);

  }

  @Override
  protected void onResume() {
    this._setTransparentStatusBar();
    this._setImmersive();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
        visibility -> {
          if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            getWindow().getDecorView()
              .setSystemUiVisibility(UI_FLAG_IMMERSIVE);
          }
        }
      );
    }

    super.onResume();
  }

  private void _setImmersive() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      UiThreadUtil.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          getWindow().getDecorView().setSystemUiVisibility(UI_FLAG_IMMERSIVE);
        }
      });
    }
  }

  private void _setTransparentStatusBar() {
    setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
    getWindow().setStatusBarColor(Color.TRANSPARENT);
  }

  public static void setWindowFlag(Activity activity, final int bits, boolean on) {
    Window win = activity.getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }
}
