package com.allever.videoeditordemo

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.Scroller
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() , View.OnClickListener{
//
//    private var textTimeLine: TextTimeLine? = null
//    private var videoTimeLine: ListTimeLine? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var mMyView: BitmapContentView? = null

    private var mScroller: Scroller? = null

    private var mScrollerView: HorizontalScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)

        initData()

        mScrollerView = findViewById(R.id.id_scroll_view)

        mMyView = findViewById(R.id.id_my_view)
        val bitmapList = mutableListOf<Bitmap>()
        val aBitmap = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        bitmapList.add(aBitmap)
        mMyView?.setData(bitmapList)

//        mMyView?.setPadding(10f)

//        mMyView?.setBitmapWHSize(60F, 60F)

        findViewById<Button>(R.id.id_add_bitmap).setOnClickListener(this)


        //1
        var timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view)
        var bcv = BitmapContentView(this)
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_4)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        timeLineView.addContentView(bcv)


        //2
        timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view_2)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_2)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        timeLineView.addContentView(bcv)

        //3
        timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view_3)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_4)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        timeLineView.addContentView(bcv)

        //3
        timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view_4)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        timeLineView.addContentView(bcv)

//        val timeLineView2 = findViewById<TimeLineView>(R.id.id_time_line_view2)
//        val bcv2 = BitmapContentView(this)
//        bcv2.addData(BitmapFactory.decodeResource(resources, R.mipmap.logo))
//        timeLineView2.addContentView(bcv2)



    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun obtainScrollToFirstEvent(event: TimeLineViewEvent){
        Log.d(TAG, "obtainMessage()")
        mScrollerView?.smoothScrollTo(0, 0)
//        if (mScrollerView == null) {
//            return
//        }
//        var anim = ObjectAnimator.ofFloat(mScrollerView, "translationX", mScrollerView!!.left.toFloat(), 0f)
//        anim.duration = 500
//        anim.start()
    }

    override fun onClick(v: View?) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        mMyView?.addData(bitmap)
    }

    private fun initData(){
        mScroller = Scroller(this)

    }


}
