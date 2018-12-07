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

        mMyView = findViewById(R.id.id_my_view)
        val bitmapList = mutableListOf<Bitmap>()
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        bitmapList.add(bitmap)
        mMyView?.setData(bitmapList)

//        mMyView?.setPadding(10f)

//        mMyView?.setBitmapWHSize(60F, 60F)

        findViewById<Button>(R.id.id_add_bitmap).setOnClickListener(this)

        val timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view)
        val bcv = BitmapContentView(this)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        bcv.addData(bitmap2)
        bcv.addData(bitmap2)
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

}
