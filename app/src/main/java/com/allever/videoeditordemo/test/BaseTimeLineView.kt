package com.allever.videoeditordemo.test

//package com.allever.videoeditordemo
//
//import android.content.Context
//import android.graphics.Color
//import android.os.Build
//import android.support.annotation.RequiresApi
//import android.support.v4.content.res.ResourcesCompat
//import android.util.AttributeSet
//import android.util.Log
//import android.view.Gravity
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.RelativeLayout
//import android.widget.TextView
//
//open class BaseTimeLineView: RelativeLayout, View.OnClickListener{
//
//    protected var mContentContainer: ViewGroup? = null
//    protected var mRootView: View? = null
//    protected var mTvDuration: TextView? = null
//
//    protected var mSelect: Boolean = false
//
//    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
//        initView()
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
//        initView()
//    }
//
//    protected open fun initView() {
//        View.inflate(context, R.layout.base_time_line, this)
//
//    }
//
//    fun setContentBackgroundColor(color: Int){
////        mContentContainer?.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
//    }
//
//    fun setWidth(width: Int){
////        val lp = mContentContainer?.layoutParams
////        lp?.width = width
////        mContentContainer?.layoutParams = lp
//    }
//
//    fun setHeight(height: Int){
////        val lp = mContentContainer?.layoutParams
////        lp?.height = height
////        mContentContainer?.layoutParams = lp
//    }
//
//    override fun onClick(v: View?) {
//
//    }
//}
//
////import android.content.Context
////import android.graphics.Color
////import android.os.Build
////import android.support.annotation.RequiresApi
////import android.support.v4.content.res.ResourcesCompat
////import android.util.AttributeSet
////import android.util.Log
////import android.view.Gravity
////import android.view.MotionEvent
////import android.view.View
////import android.view.ViewGroup
////import android.widget.ImageView
////import android.widget.LinearLayout
////import android.widget.RelativeLayout
////import android.widget.TextView
////
////open class BaseTimeLineView: RelativeLayout, View.OnClickListener, View.OnTouchListener{
////
//////    protected var mIvStart: ImageView? = null
//////    protected var mIvEnd: ImageView? = null
////    protected var mContentContainer: ViewGroup? = null
////    protected var mRootView: View? = null
////    protected var mTvDuration: TextView? = null
////
////    protected var mSelect: Boolean = false
////
////    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
////        initView()
////    }
////
////    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
////    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
////        initView()
////    }
////
////    protected open fun initView() {
////        View.inflate(context, R.layout.base_time_line, this)
//////        mIvStart = findViewById(R.id.id_iv_start_point)
//////        mIvEnd = findViewById(R.id.id_iv_end_point)
////        mContentContainer = findViewById(R.id.id_content_container)
////        mRootView = findViewById(R.id.id_root)
////        mTvDuration = findViewById(R.id.id_tv_duration)
////
////        mContentContainer?.setOnClickListener(this)
////
//////        mRootView?.setOnTouchListener(this)
//////        findViewById<View>(R.id.fl_content).setOnTouchListener(this)
//////        findViewById<View>(R.id.fl_content).setOnClickListener(this)
////    }
////
////    fun setContentBackgroundColor(color: Int){
////        mContentContainer?.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
////    }
////
////    fun setWidth(width: Int){
////        val lp = mContentContainer?.layoutParams
////        lp?.width = width
////        mContentContainer?.layoutParams = lp
////    }
////
////    fun setHeight(height: Int){
////        val lp = mContentContainer?.layoutParams
////        lp?.height = height
////        mContentContainer?.layoutParams = lp
////    }
////
////    override fun onClick(v: View?) {
////        when(v?.id){
//////            R.id.id_iv_start_point -> {
//////
//////            }
//////
//////            R.id.id_iv_end_point -> {
//////
//////            }
////
////            R.id.fl_content,
////            R.id.id_content_container -> {
////                mSelect = !mSelect
////                hideFrame(mSelect)
////            }
////        }
////    }
////
////    private var lastX: Int = 0
////    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////        when(v?.id){
////            R.id.id_root -> {
////                //获取到手指处的横坐标和纵坐标
////                val x = event?.x?.toInt() ?: 0
//////                val y = event?.y?.toInt() ?: 0
////
////                when (event?.action) {
////                    MotionEvent.ACTION_DOWN -> {
////                        lastX = x
//////                lastY = y
////                    }
////
////                    MotionEvent.ACTION_MOVE -> {
////                        //计算移动的距离
////                        val offsetX = x - lastX
//////                val offsetY = y - lastY
////                        //调用layout方法来重新放置它的位置
////                        layout(
////                            left +offsetX, top ,
////                            right +offsetX , bottom)
////                        return true
////                    }
////                }
////
////            }
////
////        }
////
////        return false
////    }
////
////    fun hideFrame(hide: Boolean){
////        if (hide){
////            mRootView?.background = null
//////            mIvEnd?.visibility = View.GONE
//////            mIvStart?.visibility = View.GONE
////        }else{
////            mRootView?.background = ResourcesCompat.getDrawable(resources, R.drawable.time_line_bg, null)
//////            mIvEnd?.visibility = View.GONE
//////            mIvStart?.visibility = View.GONE
////        }
////    }
////}
//
