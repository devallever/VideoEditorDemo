package com.allever.videoeditordemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout

class MyRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {


    companion object {
        private val TAG = MyRelativeLayout::class.java.simpleName
    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.onTouchEvent(ev)
        Log.d(TAG, "onTouchEvent() return $result")
        return result
    }
}