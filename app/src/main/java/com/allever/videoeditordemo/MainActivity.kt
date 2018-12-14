package com.allever.videoeditordemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : AppCompatActivity() , View.OnClickListener, TimeLineViewLayout.Callback, DragRVCallBack{
//
//    private var textTimeLine: TextTimeLine? = null
//    private var videoTimeLine: ListTimeLine? = null

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        private const val TIME_LINE_VIEW_HEIGHT_DP = 66F
    }

    private var mMyView: BitmapContentView? = null

    private var mScroller: Scroller? = null

    private var mScrollerView: HorizontalScrollView? = null

    private var mTimeLineViewLayout: TimeLineViewLayout? = null

    private var mRvDrag: RecyclerView? = null
    private var mDragAdapter: DragRvAdapter? = null
    private var mDragBitmap = mutableListOf<Bitmap>()
    private var mItemTouchHelper: ItemTouchHelper? = null

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


        mTimeLineViewLayout = findViewById(R.id.id_ll_time_line_container)
        mTimeLineViewLayout?.setCallback(this)



        //1
//        var timeLineView = findViewById<TimeLineView>(R.id.id_time_line_view)
        var timeLineView = TimeLineView(this)
        var bcv = BitmapContentView(this)
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_4)
        bcv.addData(bitmap)
//        bcv.addData(bitmap)
//        timeLineView.setPadding(0,0,0,0)
        timeLineView.addContentView(bcv)
        mTimeLineViewLayout?.addTimeLineView(timeLineView, TIME_LINE_VIEW_HEIGHT_DP)
        var firstBitmap = bcv.getFirst()
        if (firstBitmap != null){
            mDragBitmap.add(firstBitmap)
        }


        //2
        timeLineView = TimeLineView(this)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_2)
        bcv.addData(bitmap)
//        bcv.addData(bitmap)
//        bcv.addData(bitmap)
//        bcv.addData(bitmap)
//        bcv.addData(bitmap)
        timeLineView.setPadding(-162,0,0,0)
        timeLineView.addContentView(bcv)
        mTimeLineViewLayout?.addTimeLineView(timeLineView, TIME_LINE_VIEW_HEIGHT_DP)
        firstBitmap = bcv.getFirst()
        if (firstBitmap != null){
            mDragBitmap.add(firstBitmap)
        }

        //3
        timeLineView = TimeLineView(this)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test_4)
        bcv.addData(bitmap)
//        bcv.addData(bitmap)
        timeLineView.setPadding(-162,0,0,0)
        timeLineView.addContentView(bcv)
        mTimeLineViewLayout?.addTimeLineView(timeLineView, TIME_LINE_VIEW_HEIGHT_DP)
        firstBitmap = bcv.getFirst()
        if (firstBitmap != null){
            mDragBitmap.add(firstBitmap)
        }

        //3
        timeLineView = TimeLineView(this)
        bcv = BitmapContentView(this)
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.logo)
        bcv.addData(bitmap)
//        bcv.addData(bitmap)
        timeLineView.setPadding(-162,0,0,0)
        timeLineView.addContentView(bcv)
        mTimeLineViewLayout?.addTimeLineView(timeLineView, TIME_LINE_VIEW_HEIGHT_DP)
        firstBitmap = bcv.getFirst()
        if (firstBitmap != null){
            mDragBitmap.add(firstBitmap)
        }

//        val scrollView = findViewById<MyHScrollview>(R.id.id_scroll_view)
//        mTimeLineViewLayout?.setContainerScrollView(scrollView)

        //initRv
        mRvDrag = findViewById(R.id.id_drag_rv)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRvDrag?.layoutManager = layoutManager
        mDragAdapter = DragRvAdapter(this, mDragBitmap, object : DragRvAdapter.Callback{
            override fun onClick(position: Int?) {
                var childView = mRvDrag?.getChildAt(position!!)
                mRvDrag?.bringChildToFront(childView)
//                val holder = mRvDrag?.getChildViewHolder(childView!!)
//                holder?.itemView?.bringToFront()
//                mTimeLineViewLayout?.visibility = View.VISIBLE
//                mRvDrag?.visibility = View.GONE
            }


            override fun onLongClick(position: Int?) {
                Toast.makeText(this@MainActivity, "position = $position",Toast.LENGTH_SHORT).show()
            }

        }, this)
        mRvDrag?.adapter = mDragAdapter
        mItemTouchHelper = ItemTouchHelper(DragItemCallBack(this))
        mItemTouchHelper?.attachToRecyclerView(mRvDrag)


//        val timeLineViewSingle = findViewById<TimeLineView>(R.id.id_time_line_view_single)
//        val bcv2 = BitmapContentView(this)
//        bcv2.addData(BitmapFactory.decodeResource(resources, R.mipmap.logo))
//        bcv2.addData(BitmapFactory.decodeResource(resources, R.mipmap.logo))
//        timeLineViewSingle.addContentView(bcv2)


        val iv1 = findViewById<ImageView>(R.id.id_iv_test_1)
        val iv2 = findViewById<ImageView>(R.id.id_iv_test_2)

        iv1.setOnClickListener {
            it.bringToFront()
        }

        iv2.setOnClickListener {
            it.bringToFront()
        }
    }

    override fun onMove(from: Int, to: Int) {
        Log.d(TAG, "onMove from $from to $to")

        synchronized(this) {
            if (from > to) {
                val count = from - to
                for (i in 0 until count) {
                    Collections.swap(mDragBitmap, from - i, from - i - 1)
                }
            }
            if (from < to) {
                val count = to - from
                for (i in 0 until count) {
                    Collections.swap(mDragBitmap, from + i, from + i + 1)
                }
            }
            mDragAdapter?.setData(mDragBitmap)
            mDragAdapter?.notifyItemMoved(from, to)
            mDragAdapter?.show?.clear()
            mDragAdapter?.show?.put(to, to)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemLongClick(index: Int?) {
        mRvDrag?.visibility = View.VISIBLE
        mTimeLineViewLayout?.visibility = View.GONE
//        mItemTouchHelper?.startDrag(mRvDrag?.getChildViewHolder(mRvDrag?.getChildAt(index?:return)?: return)?: return)
//        val childView = mRvDrag?.getChildAt(index?:return)
//        childView?.performLongClick(100f, 100f)
//        childView?.getLocationOnScreen()
//        mItemTouchHelper?.startDrag()

        Toast.makeText(this, "拖动$index", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun obtainScrollToFirstEvent(event: TimeLineViewScrollToEndEvent){
        //滚动到底部
        mScrollerView?.fullScroll(HorizontalScrollView.FOCUS_DOWN)
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
