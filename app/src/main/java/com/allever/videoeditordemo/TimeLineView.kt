package com.allever.videoeditordemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Scroller
import org.greenrobot.eventbus.EventBus

class TimeLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnTouchListener {

    companion object {
        private val TAG = TimeLineView::class.java.simpleName
        private const val MESSAGE_SCROLL_TO_SCREEN_MID = 0x01
        private const val FRAME_COUNT = 30
    }

    private var mCount = 0
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MESSAGE_SCROLL_TO_SCREEN_MID -> {
                    mCount++
                    if (mCount <= FRAME_COUNT){
                        val parent = parent as? ViewGroup
                        val frequency = msg.arg1
                        val message = Message()
                        message.what = MESSAGE_SCROLL_TO_SCREEN_MID
                        message.arg1 = frequency
                        modifyMarginStart(parent, -frequency)
                        sendMessageDelayed(message, 6)
                    }else{
                        mCount = 0
                    }
                }
            }
        }
    }

    private var mRootView: View? = null
    private var mIvStart: ImageView? = null
    private var mIvEnd: ImageView? = null
    private var mContentContainer: LinearLayout? = null

    private var mBackground: Drawable? = null

    private var mHalfScreenWidth = 0

    init {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        val rootView = LayoutInflater.from(context).inflate(R.layout.time_time_line, this, true)
        rootView.setOnClickListener(this)
        mRootView = rootView
        mContentContainer = rootView.findViewById(R.id.id_content_container)
        mIvStart = rootView.findViewById(R.id.id_iv_start)
        mIvEnd = rootView.findViewById(R.id.id_iv_end)

        mIvStart?.setOnTouchListener(this)
        mIvEnd?.setOnTouchListener(this)

        mBackground = ResourcesCompat.getDrawable(resources, R.drawable.time_line_bg, null)

        mHalfScreenWidth = DeviceUtil.getScreenWidthPx(context)

        //
        mIvStart?.post {
            showFrame(false)
        }


    }


    private var mShowFrame = false
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
            }
        }
    }

    private var mIvStartMaxTranslationX = -1
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mContentContainerWidth = mContentContainer?.width
        Log.d(TAG, "onMeasure() mContentContainerWidth = $mContentContainerWidth")
        if (mIvStartMaxTranslationX == -1 && mContentContainerWidth != 0){
            mIvStartMaxTranslationX = mContentContainerWidth?: 0
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
                            lp.width = width
                            mContentContainer?.layoutParams = lp


                            //修改控件位置
//                            modifyMarginStart(this, offsetX.toInt())

                            //修改父控件MarginStart，实现整体左移，不需要修改该控件的位置了
                            val parent = parent as? ViewGroup
                            val parentLp = parent?.layoutParams as? MarginLayoutParams
                            val martinRight = parentLp?.rightMargin ?: 0
                            Log.d(TAG, "parent margin End = $martinRight")
                            modifyMarginEnd(parent,-offsetX )
                        }
                        mLastRawX = currentRawX
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
//                        if (isLeftTranslation){
//                            Log.d(TAG, "onTouch() 左移")
//                        }else{
//                            Log.d(TAG, "onTouch() 右移")
//                        }
                            //如果是向左移动，需要判断是否超过向左移动的最大值
                            if (isLeftTranslation && width > mIvStartMaxTranslationX){
                                //左移
                                parent.requestDisallowInterceptTouchEvent(false)
                                return false
                            }
                            lp.width = width
                            mContentContainer?.layoutParams = lp
                            //修改控件位置
//                            modifyMarginStart(this, offsetX.toInt())

                            //修改父控件MarginStart，实现整体左移，不需要修改该控件的位置了
                            val parent = parent as? ViewGroup
                            val parentLp = parent?.layoutParams as? MarginLayoutParams
                            val marginStart = parentLp?.leftMargin ?: 0
                            Log.d(TAG, "parent margin Start = $marginStart")
                            modifyMarginStart(parent,offsetX)
                        }
                        mLastRawX = currentRawX
                    }

                    MotionEvent.ACTION_UP -> {
                        val currentRawX = event.rawX
                        //判断， 并返回中间位置，动画效果
                        val parent = parent as? ViewGroup
                        val parentLp = parent?.layoutParams as? MarginLayoutParams
                        val marginLeft = parentLp?.leftMargin ?: 0
                        Log.d(TAG, "onTouch() action up marginLeft = $marginLeft")
                        val screenWidth = DeviceUtil.getScreenWidthPx(context)
                        Log.d(TAG, "onTouch action up screenWidth $screenWidth")

                        val halfScreenWidth = screenWidth * 0.5f

                        val isLeftTranslation = (mLastRawX - currentRawX) > 0
                        if (!isLeftTranslation){
                            //右移
                            if (marginLeft > halfScreenWidth){
                                //动画滚动到中间
                                Log.d(TAG, "action up 动画滚动到中间")

                                val msg = Message()
                                msg.what = MESSAGE_SCROLL_TO_SCREEN_MID
                                val frequency = (marginLeft - halfScreenWidth) / FRAME_COUNT
                                msg.arg1 = frequency.toInt()
                                mHandler.sendMessageDelayed(msg, 30)

                                val timeLineViewEvent = TimeLineViewEvent()
                                EventBus.getDefault().post(timeLineViewEvent)

                            }else{
                                Log.d(TAG, "action up 不需要滚动")
                            }
                        }

                    }
                }
            }
        }
        //处理滑动冲突，屏蔽父控件拦截onTouch事件
        parent.requestDisallowInterceptTouchEvent(true)
        Log.d(TAG, "onTouch() return true")
        return true
    }

    fun addContentView(view: View?){
        mContentContainer?.addView(view)
    }

    fun showFrame(show: Boolean = false){
        if (show){
            //显示边框
            mIvEnd?.visibility = View.VISIBLE
            mIvStart?.visibility = View.VISIBLE
            mContentContainer?.background = mBackground

            //修改控件位置
//            modifyMarginStart( - (mIvStart?.width?:0))

        }else{
            //隐藏边框
            mIvEnd?.visibility = View.INVISIBLE
            mIvStart?.visibility = View.INVISIBLE
            mContentContainer?.background = null

//            modifyMarginStart(mIvStart?.width?: 0)
        }
    }

    /***
     * 修改控件的marginStart
     */
    private fun modifyMarginStart(view: View?, marginStartOffsetX:Int){
        val lp = view?.layoutParams as? MarginLayoutParams ?: return
        var marginStart = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            //修改控件位置
            marginStart = (lp.marginStart  + marginStartOffsetX)
            Log.d(TAG, "marginStartOffsetX = $marginStartOffsetX")
            lp.marginStart = marginStart
        }else{
            marginStart = (lp.leftMargin  + marginStartOffsetX)
            lp.leftMargin = marginStart
        }

        Log.d(TAG, "marginStart = $marginStart")

        view.layoutParams = lp

    }


    /***
     * 修改控件的marginStart
     */
    private fun modifyMarginEnd(view: View?, marginEndOffsetX:Int){
        val lp = view?.layoutParams as? MarginLayoutParams ?: return
        var marginEnd = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            //修改控件位置
            marginEnd = (lp.marginEnd  + marginEndOffsetX)
            Log.d(TAG, "marginEndOffsetX = $marginEndOffsetX")
            lp.marginEnd = marginEnd
        }else{
            marginEnd = (lp.rightMargin  + marginEndOffsetX)
            lp.rightMargin = marginEnd
        }

        Log.d(TAG, "marginStart = $marginEnd")

        view.layoutParams = lp

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent() return $result")
        return result
    }

    public interface Callback{
        fun onScrollToMiddle(fromX: Int, toX: Int)
    }

}