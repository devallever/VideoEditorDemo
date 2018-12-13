package com.allever.videoeditordemo.test;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Property;
import android.view.View;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initD(){
        View view = new View(getContext());
        ObjectAnimator.ofFloat(view, View.Y, 0f, view.getTop());
    }



}
