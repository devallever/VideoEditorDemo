package com.allever.videoeditordemo;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class DeviceUtil {
    private DeviceUtil(){}

    /***
     * 是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        if (context == null){
            return false;
        }

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public static int getNavigationBarHeight(Context context) {
        if (context == null){
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    @JvmStatic
    @NotNull
    public static final DisplayMetrics getDisplayMetrics(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Resources var10000 = context.getResources();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "context.resources");
        DisplayMetrics var1 = var10000.getDisplayMetrics();
        Intrinsics.checkExpressionValueIsNotNull(var1, "context.resources.displayMetrics");
        return var1;
    }

    @JvmStatic
    public static final int getScreenWidthPx(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return getDisplayMetrics(context).widthPixels;
    }


}
