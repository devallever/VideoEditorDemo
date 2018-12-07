package com.allever.videoeditordemo.test;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TestLinear extends LinearLayout {
    public TestLinear(Context context) {
        super(context);

    }

    public TestLinear(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLinear(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestLinear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
