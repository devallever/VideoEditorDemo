package com.allever.videoeditordemo.test;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public class DevideUtils {

    public static final int dip2px(Context context, float dpValue) {
        if (context == null){
            return 0;
        }
        Intrinsics.checkParameterIsNotNull(context, "context");
        float scale = getScreenDensity(context);
        return (int)(dpValue * scale + 0.5F);
    }

    public static final float getScreenDensity(Context context) {
        if (context == null){
            return 0;
        }
        Intrinsics.checkParameterIsNotNull(context, "context");
        return getDisplayMetrics(context).density;
    }


    public static final DisplayMetrics getDisplayMetrics(Context context) {
        if (context == null){
            return null;
        }
        Intrinsics.checkParameterIsNotNull(context, "context");
        Resources var10000 = context.getResources();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "context.resources");
        DisplayMetrics var1 = var10000.getDisplayMetrics();
        Intrinsics.checkExpressionValueIsNotNull(var1, "context.resources.displayMetrics");
        return var1;
    }
}
