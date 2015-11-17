package com.grid.cuiletian.library;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
    private static DisplayMetrics mMetrics = new DisplayMetrics();

    public ScreenUtil(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    public ScreenUtil(WindowManager windowManager) {
        windowManager.getDefaultDisplay().getMetrics(mMetrics);
    }

    public ScreenUtil(Context context) {
        mMetrics = context.getResources().getDisplayMetrics();
    }

    public int getScreenHeight() {
        return mMetrics.heightPixels;
    }

    public int getScreenWidth() {
        return mMetrics.widthPixels;
    }

    public int dip2px(float dpValue) {
        final float scale = mMetrics.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        DisplayMetrics c = context.getResources().getDisplayMetrics();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return manager.isScreenOn();
    }
}
