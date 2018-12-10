package com.allever.videoeditordemo

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Scroller
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MyLinearLayout : LinearLayout {

    private var mScroller: Scroller? = null

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }


    companion object {
        private val TAG = MyLinearLayout::class.java.simpleName
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView()
    }

//    private var timeLineViewCallback = object :TimeLineView.Callback{
//        override fun onScrollToMiddle(fromX: Int, toX: Int) {
//            mScroller?.startScroll(mScroller?.currX ?:0, 0, toX, 0, 500)
//            postInvalidate()
//        }
//
//    }


    override fun computeScroll() {
        super.computeScroll()
        if(mScroller?.computeScrollOffset() == true) {
            scrollTo(mScroller?.currX ?:0,0)
            invalidate()
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onObtainMessage(timeLineViewEvent: TimeLineViewEvent?){
//        Log.d(TAG, "onObtainMessage()")
//        val fromX = timeLineViewEvent?.fromX
//        val toX = timeLineViewEvent?.toX
//        mScroller?.startScroll( mScroller?.currX ?:0, 0, toX?:0, 0, 500)
//        postInvalidate()
//    }

    private fun initView() {
        mScroller = Scroller(context)
//        EventBus.getDefault().register(this)
    }

    fun onDestroy(){
//        EventBus.getDefault().unregister(this)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged()")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent() return $result")
        return result
    }
}
