package com.allever.videoeditordemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() , View.OnClickListener{
//
//    private var textTimeLine: TextTimeLine? = null
//    private var videoTimeLine: ListTimeLine? = null

    private var mMyView: BitmapContentView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        mMyView = findViewById(R.id.id_my_view)
        val bitmapList = mutableListOf<Bitmap>()
        val aBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
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
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        bcv.addData(bitmap)
        bcv.addData(bitmap)
        timeLineView.addContentView(bcv)

//        val timeLineView2 = findViewById<TimeLineView>(R.id.id_time_line_view2)
//        val bcv2 = BitmapContentView(this)
//        bcv2.addData(BitmapFactory.decodeResource(resources, R.mipmap.logo))
//        timeLineView2.addContentView(bcv2)
    }

    override fun onClick(v: View?) {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        mMyView?.addData(bitmap)
    }

    private fun initData(){

    }

}
