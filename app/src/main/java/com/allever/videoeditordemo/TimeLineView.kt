package com.allever.videoeditordemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

class TimeLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    companion object {
        private val TAG = TimeLineView::class.java.simpleName
    }

    private var mRootView: View? = null
    var mIvStart: ImageView? = null
    var mIvEnd: ImageView? = null
    var mContentContainer: LinearLayout? = null

    private var mBackground: Drawable? = null

    private var mHalfScreenWidth = 0
    private var mScreenWidth = 0

    private var mMovingCallback: MovingCallback? = null

    private var mOptionListener: OnOptinListener? = null

    init {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        val rootView = LayoutInflater.from(context).inflate(R.layout.time_time_line, this, true)
//        rootView.setOnClickListener(this)
//        rootView.setOnLongClickListener(this)
        mRootView = rootView
        mContentContainer = rootView.findViewById(R.id.id_content_container)
        mRootView?.setOnClickListener(this)
        mRootView?.setOnLongClickListener(this)
        mIvStart = rootView.findViewById(R.id.id_iv_start)
        mIvEnd = rootView.findViewById(R.id.id_iv_end)

        mIvStart?.setOnTouchListener(this)
        mIvEnd?.setOnTouchListener(this)

        mBackground = ResourcesCompat.getDrawable(resources, R.drawable.time_line_bg, null)

        mScreenWidth = DeviceUtil.getScreenWidthPx(context)
        mHalfScreenWidth = mScreenWidth / 2


        //
        mIvStart?.post {
            showFrame(false)
        }
    }


    var mShowFrame = false
    override fun onClick(v: View?) {
        when (v){
            mRootView -> {
                val parent = parent as? ViewGroup
                val childCount = parent?.childCount ?: 0
                for (i in 0 until childCount){
                    val child = parent?.getChildAt(i)
                    if (child is TimeLineView){
                        if (child.hashCode() == this.hashCode()){
                            //自身
                            child.mShowFrame = !child.mShowFrame
                            child.showFrame(child.mShowFrame)
                        }else{
                            //其他
                            child.showFrame(false)
                            child.mShowFrame = false
                        }
                    }
                }
                mOptionListener?.onClick(this)
            }
        }
    }


    private fun hideAllFrame(){
        val parent = parent as? ViewGroup
        val childCount = parent?.childCount ?: 0
        for (i in 0 until childCount){
            val child = parent?.getChildAt(i)
            if (child is TimeLineView){
                child.showFrame(false)
                child.mShowFrame = false
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        mOptionListener?.onLongClick(this)
        when(v){
            mRootView ->{
                hideAllFrame()
                return true
            }
        }
        return false
    }


    //左箭头移动最大距离，默认为容器的宽度
    var mIvStartMaxTranslationX = -1
    //右箭头移动最大距离，默认为容器的宽度, 测量后赋值
    var mIvEndMaxTranslationX = -1
    //外部设置可允许拖动最大值
    private var mMaxTranslationX = -1
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mContentContainerWidth = mContentContainer?.width
        Log.d(TAG, "onMeasure() mContentContainerWidth = $mContentContainerWidth")
        if (mIvStartMaxTranslationX == -1 && mContentContainerWidth != 0){
            mIvStartMaxTranslationX = mContentContainerWidth?: 0
        }

        if (mIvEndMaxTranslationX == -1 && mContentContainerWidth != 0){
            mIvEndMaxTranslationX = mContentContainerWidth?: 0
        }

        Log.d(TAG, "onMeasure() mIvStartMaxTranslationX = $mIvStartMaxTranslationX")

    }

    private var mLastRawX = 0
    private var mOriginRawX = 0
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v){
            mIvEnd -> {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        mOriginRawX = event.rawX.toInt()
                        mLastRawX  = mOriginRawX
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val currentRawX = event.rawX.toInt()
                        val offsetX = currentRawX - mLastRawX

                        Log.d(TAG, "offsetX = $offsetX")

                        //修改控件内容器宽度
                        val containerWidth = mContentContainer?.width
                        Log.d(TAG, "container width = $containerWidth")

                        val lp = mContentContainer?.layoutParams
                        Log.d(TAG, "container lp width = ${lp?.width}")

                        lp?.width = containerWidth
                        val width = lp?.width!! + offsetX
                        //处理滑动到左边箭头后禁止继续滑动
                        if (width > 0){
                            val isRightTranslation = (mLastRawX - currentRawX) < 0
//                            //如果是向右移动，需要判断是否超过向右移动的最大值
                            if (isRightTranslation && width > mIvEndMaxTranslationX){
                                //右移
                                parent.requestDisallowInterceptTouchEvent(false)
                                return false
                            }

                            val ivEndLocation = IntArray(2)
                            mIvEnd?.getLocationOnScreen(ivEndLocation)
                            val ivEndX = ivEndLocation[0]
                            //如果右箭头移动到右边屏幕边缘，停止移动，向反方向, 即修改左
                            Log.d(TAG, "iv end local x = $ivEndX")
                            if (isRightTranslation && ivEndX > (mScreenWidth - 90)){
                                //自动滚动，即使手指触摸到屏幕但没移动，也能匀速滚动
                                mMovingCallback?.onEndMoveToScreenRight(this)
                            }else{
                                lp.width = width
                                mContentContainer?.layoutParams = lp

                                mMovingCallback?.onEndMoving(this, -offsetX, !isRightTranslation)
                            }
                        }
                        mLastRawX = currentRawX
                    }

                    MotionEvent.ACTION_UP -> {
                        val currentRawX = event.rawX
                        val isRightTranslation = (mLastRawX - currentRawX) < 0
                        mMovingCallback?.onEndUp(this, !isRightTranslation)
                    }
                }
            }

            mIvStart -> {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        mOriginRawX = event.rawX.toInt()
                        mLastRawX  = mOriginRawX
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val currentRawX = event.rawX.toInt()
                        val offsetX = currentRawX - mLastRawX
                        Log.d(TAG, "mIvStart offsetX = $offsetX")
//                        //修改控件内容器宽度
                        val containerWidth = mContentContainer?.width
                        val lp = mContentContainer?.layoutParams
                        lp?.width = containerWidth
                        //-offsetX：与右边箭头相反
                        val width = lp?.width!! + (-offsetX)

                        //处理滑动到右边箭头后禁止继续滑动
                        if (width > 0 ){
                            val isLeftTranslation = (mLastRawX - currentRawX) > 0

                            //如果是向左移动，需要判断是否超过向左移动的最大值
                            if (isLeftTranslation && width > mIvStartMaxTranslationX){
                                //左移
                                parent.requestDisallowInterceptTouchEvent(false)
                                return false
                            }

                            val ivStartLocation = IntArray(2)
                            mIvStart?.getLocationOnScreen(ivStartLocation)
                            val ivStartX = ivStartLocation[0]
                            //如果左箭头移动到左边屏幕边缘，停止移动，向反方向, 即修改you
                            if (isLeftTranslation && ivStartX < 0){
                                //自动滚动，即使手指触摸到屏幕但没移动，也能匀速滚动
                                mMovingCallback?.onStartMoveToScreenLeft(this)

                            }else{

                                lp.width = width
                                mContentContainer?.layoutParams = lp
//
                                mMovingCallback?.onStartMoving(this, offsetX, false)
                            }

                        }
                        mLastRawX = currentRawX
                    }

                    MotionEvent.ACTION_UP -> {
                        val currentRawX = event.rawX
                        //判断， 并返回中间位置，动画效果
                        val isLeftTranslation = (mLastRawX - currentRawX) > 0
                        mMovingCallback?.onStartUp(this, isLeftTranslation)
                    }
                }
            }
        }
        //处理滑动冲突，屏蔽父控件拦截onTouch事件
        parent.requestDisallowInterceptTouchEvent(true)
        return true
    }

    fun addContentView(view: View?){
        mContentContainer?.addView(view)
    }

    fun removeContentView(view: View?){
        mContentContainer?.removeView(view)
    }

    fun removeAllview(){
        mContentContainer?.removeAllViews()
    }

    fun getContentViewCount(): Int{
        return mContentContainer?.childCount?: 0
    }

    fun showFrame(show: Boolean = false){
        if (show){
            //显示边框
            mIvEnd?.visibility = View.VISIBLE
            mIvStart?.visibility = View.VISIBLE
            mContentContainer?.background = mBackground
        }else{
            //隐藏边框
            mIvEnd?.visibility = View.INVISIBLE
            mIvStart?.visibility = View.INVISIBLE
            mContentContainer?.background = null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        return result
    }

    /***
     * 设置向右移动最大值
     * -1 ：无限延申
     */
    fun setMaxTranslationX(max: Int = -1){
        mIvEndMaxTranslationX = max
    }

    fun setMovingCallback(movingCallback: MovingCallback?){
        mMovingCallback = movingCallback
    }

    fun setOptionListener(opListener: OnOptinListener){
        mOptionListener = opListener
    }

    public interface MovingCallback{
        //箭头在可移动区域内移动
        fun onStartMoving(timeLineView: TimeLineView, offsetX: Int, isMoveToLeft: Boolean)
        fun onEndMoving(timeLineView: TimeLineView, offsetX: Int, isMoveToLeft: Boolean)


        //箭头在可移动区域内移动
//        fun onStartMovingLeft(timeLineView: TimeLineView, offsetX: Int)
//        fun onStartMovingRight(timeLineView: TimeLineView, offsetX: Int)
//        fun onEndMovingLeft(timeLineView: TimeLineView, offsetX: Int)
//        fun onEndMovingRight(timeLineView: TimeLineView, offsetX: Int)

        //箭头移动到最左.最右
        fun onStartMoveToLeft(timeLineView: TimeLineView)
        fun onStartMoveToRight(timeLineView: TimeLineView)
        fun onEndMoveToLeft(timeLineView: TimeLineView)
        fun onEndMoveToRight(timeLineView: TimeLineView)

        //箭头移动到屏幕边缘
        fun onStartMoveToScreenLeft(timeLineView: TimeLineView)
        fun onEndMoveToScreenRight(timeLineView: TimeLineView)

        //手指离开箭头
        fun onStartUp(timeLineView: TimeLineView, isLeftTranslation: Boolean)
        fun onEndUp(timeLineView: TimeLineView, isLeftTranslation: Boolean)

    }

    public interface OnOptinListener{
        fun onClick(timeLineView: TimeLineView)
        fun onLongClick(timeLineView: TimeLineView)
    }

}