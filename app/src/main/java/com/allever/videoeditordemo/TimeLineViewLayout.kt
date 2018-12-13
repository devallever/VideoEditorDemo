package com.allever.videoeditordemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast

//import org.greenrobot.eventbus.EventBus

class TimeLineViewLayout : LinearLayout, TimeLineView.MovingCallback, TimeLineView.OnOptinListener/*, View.OnDragListener*/ , DragHelper.DragListener ,
View.OnLongClickListener{


    override fun onDragStarted() {
        Log.d(TAG, "onDragStarted()")

    }

    override fun onDragEnded() {
        Log.d(TAG, "onDragEnded()")
    }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }


    companion object {
        private val TAG = TimeLineViewLayout::class.java.simpleName

        private const val MESSAGE_SCROLL_TO_SCREEN_MID_FROM_RIGHT = 0x01
        private const val MESSAGE_AUTO_SCROLL_TO_RIGHT = 0x02
        private const val MESSAGE_AUTO_SCROLL_TO_LEFT = 0x03
        private const val MESSAGE_SCROLL_TO_SCREEN_LEFT = 0x04
        private const val MESSAGE_SCROLL_TO_SCREEN_RIGHT = 0x05
        private const val MESSAGE_SCROLL_TO_SCREEN_MID_FROM_LEFT = 0x06
        private const val FRAME_COUNT = 30

    }


    private var mSelectedTimeLineView: TimeLineView? = null

    private var mHalfScreenWidth = 0
    private var mScreenWidth = 0

    private var mCallback: Callback? = null


    private var mCount = 0
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MESSAGE_SCROLL_TO_SCREEN_MID_FROM_RIGHT -> {
                    mCount++
                    if (mCount <= FRAME_COUNT){
                        val frequency = msg.arg1
                        val message = Message()
                        message.what = MESSAGE_SCROLL_TO_SCREEN_MID_FROM_RIGHT
                        message.arg1 = frequency
                        modifyMarginStart(-frequency)
                        sendMessageDelayed(message, 6)
                    }else{
                        mCount = 0
                    }
                }

                MESSAGE_AUTO_SCROLL_TO_RIGHT -> {
                    //修改控件内容器宽度
                    val contentContainer = mSelectedTimeLineView?.mContentContainer ?: return
                    val containerWidth = contentContainer.width
                    val lp = contentContainer.layoutParams
                    lp?.width = containerWidth
                    val width = lp?.width!! + 10

                    if (width > mSelectedTimeLineView?.mIvStartMaxTranslationX?:0){
                        return
                    }

                    lp.width = width
                    contentContainer.layoutParams = lp

                    val parent = parent as? ViewGroup
                    val parentLp = parent?.layoutParams as? MarginLayoutParams
                    val martinRight = parentLp?.rightMargin ?: 0
                    if (martinRight < mHalfScreenWidth){
                        modifyMarginEnd(10 )
                    }

                    sendEmptyMessageDelayed(MESSAGE_AUTO_SCROLL_TO_RIGHT, 10)
                }

                MESSAGE_AUTO_SCROLL_TO_LEFT -> {
                    //修改控件内容器宽度
                    val contentContainer = mSelectedTimeLineView?.mContentContainer ?: return
                    val containerWidth = contentContainer.width
                    val lp = contentContainer.layoutParams
                    lp?.width = containerWidth
                    val width = lp?.width!! + 10

                    if (width > mSelectedTimeLineView?.mIvEndMaxTranslationX?:0){
                        return
                    }

                    lp.width = width
                    contentContainer.layoutParams = lp

                    modifyMarginStart(-10 )
                    sendEmptyMessageDelayed(MESSAGE_AUTO_SCROLL_TO_LEFT, 10)
                }

                MESSAGE_SCROLL_TO_SCREEN_LEFT -> {
                    val current = msg.arg1
                    val total = msg.arg2
                    if (current <= total){
                        modifyMarginStart(10)
                        val message = Message()
                        message.arg1 = current + 10
                        message.arg2 = total
                        message.what = MESSAGE_SCROLL_TO_SCREEN_LEFT
                        sendMessageDelayed(message, 10)
                    }else{
                        modifyMarginStart( total - current)
                        removeMessages(MESSAGE_SCROLL_TO_SCREEN_LEFT)
                    }
                }


                MESSAGE_SCROLL_TO_SCREEN_RIGHT -> {
                    val current = msg.arg1
                    val total = msg.arg2
                    if (current <= total){
                        modifyMarginEnd(10)
                        val message = Message()
                        message.arg1 = current + 10
                        message.arg2 = total
                        message.what = MESSAGE_SCROLL_TO_SCREEN_RIGHT
                        sendMessageDelayed(message, 10)
                    }else{
                        modifyMarginEnd(total - current)
                        removeMessages(MESSAGE_SCROLL_TO_SCREEN_RIGHT)
                    }
                }


                MESSAGE_SCROLL_TO_SCREEN_MID_FROM_LEFT -> {
                    val current = msg.arg1
                    val total = msg.arg2
                    Log.d(TAG, "message current = $current")
                    Log.d(TAG, "message total = $total")
                    if (current <= total){
                        modifyMarginEnd(-10)
                        val message = Message()
                        message.arg1 = current + 10
                        message.arg2 = total
                        message.what = MESSAGE_SCROLL_TO_SCREEN_MID_FROM_LEFT
                        sendMessageDelayed(message, 10)
                    }else{
                        modifyMarginEnd(total - current)
                        removeMessages(MESSAGE_SCROLL_TO_SCREEN_MID_FROM_LEFT)
                    }
                }
            }
        }
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

    private fun initView() {
        mScreenWidth = DeviceUtil.getScreenWidthPx(context)
        mHalfScreenWidth = mScreenWidth / 2
//        setOnDragListener(this)
//        DragHelper.setupDragSort(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent() return $result")
        return result
    }

    fun setCallback(callback: Callback?){
        mCallback = callback
    }

    fun addTimeLineView(timeLineView: TimeLineView?, heightDp: Float){
        timeLineView?: return
        val lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DeviceUtil.dip2px(context, heightDp))
        lp.gravity = Gravity.CENTER_VERTICAL
//        MyDragHelper.setupDragSort(timeLineView)
        MyDragHelper.setLongClick(timeLineView, mCallback)
        timeLineView.setMovingCallback(this)
        timeLineView.setOptionListener(this)
        addView(timeLineView, lp)
    }

    override fun onLongClick(view: View?): Boolean {
        val parent = parent as? ViewGroup
        val index = parent?.indexOfChild(view)
        Log.d(TAG, "index = $index")
        mCallback?.onItemLongClick(index)
        return true
    }

    override fun onStartMoving(timeLineView: TimeLineView, offsetX: Int, isMoveToLeft: Boolean) {
        mSelectedTimeLineView = timeLineView
        modifyMarginStart(offsetX)
    }

    override fun onEndMoving(timeLineView: TimeLineView, offsetX: Int, isMoveToLeft: Boolean) {
        mSelectedTimeLineView = timeLineView
        modifyMarginEnd(offsetX)
    }

    override fun onStartMoveToLeft(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
    }

    override fun onStartMoveToRight(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
    }

    override fun onEndMoveToLeft(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
    }

    override fun onEndMoveToRight(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
    }

    override fun onStartMoveToScreenLeft(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
        if (mHandler.hasMessages(MESSAGE_AUTO_SCROLL_TO_RIGHT)) {
            mHandler.removeMessages(MESSAGE_AUTO_SCROLL_TO_RIGHT)
        }
        mHandler.sendEmptyMessageDelayed(MESSAGE_AUTO_SCROLL_TO_RIGHT, 10)
    }

    override fun onEndMoveToScreenRight(timeLineView: TimeLineView) {
        mSelectedTimeLineView = timeLineView
        if (mHandler.hasMessages(MESSAGE_AUTO_SCROLL_TO_LEFT)) {
            mHandler.removeMessages(MESSAGE_AUTO_SCROLL_TO_LEFT)
        }
        mHandler.sendEmptyMessageDelayed(MESSAGE_AUTO_SCROLL_TO_LEFT, 10)
    }

    override fun onStartUp(timeLineView: TimeLineView, isLeftTranslation: Boolean) {
        mSelectedTimeLineView = timeLineView
        if (mHandler.hasMessages(MESSAGE_AUTO_SCROLL_TO_RIGHT)) {
            mHandler.removeMessages(MESSAGE_AUTO_SCROLL_TO_RIGHT)
        }

        val lp = layoutParams as? MarginLayoutParams
        val marginLeft = lp?.leftMargin ?:0
        if (!isLeftTranslation){
            //右移
            if (marginLeft > mHalfScreenWidth){
                //动画滚动到中间
                Log.d(TAG, "action up 动画滚动到中间")

                val msg = Message()
                msg.what = MESSAGE_SCROLL_TO_SCREEN_MID_FROM_RIGHT
                val frequency = (marginLeft - mHalfScreenWidth) / FRAME_COUNT
                msg.arg1 = frequency.toInt()
                mHandler.sendMessageDelayed(msg, 30)

//                                val timeLineViewEvent = TimeLineViewEvent()
//                                EventBus.getDefault().post(timeLineViewEvent)

            }else{
                Log.d(TAG, "action up 不需要滚动")
            }
        }

        Log.d(TAG, "marginLeft = $marginLeft")
        if (marginLeft < 0){
            val msg = Message()
            msg.arg1 = 0
            msg.arg2 = (0 - marginLeft)
            msg.what = MESSAGE_SCROLL_TO_SCREEN_LEFT
            mHandler.sendMessageDelayed(msg, 10)
//            val timeLineViewEvent = TimeLineViewEvent()
//            EventBus.getDefault().post(timeLineViewEvent)
        }
    }

    override fun onEndUp(timeLineView: TimeLineView, isLeftTranslation: Boolean) {
        mSelectedTimeLineView = timeLineView
        if (mHandler.hasMessages(MESSAGE_AUTO_SCROLL_TO_LEFT)) {
            mHandler.removeMessages(MESSAGE_AUTO_SCROLL_TO_LEFT)
        }

        val lp = layoutParams as? MarginLayoutParams
        val marginRight = lp?.rightMargin ?: 0
        if (marginRight < 0){
            Log.d(TAG, "action up 滚动到最右")
            val msg = Message()
            msg.arg1 = 0
            msg.arg2 = (0 - marginRight)
            msg.what = MESSAGE_SCROLL_TO_SCREEN_RIGHT
            mHandler.sendMessageDelayed(msg, 10)
        }else{
            Log.d(TAG, "action up 不滚动")
        }

        if (marginRight > mHalfScreenWidth){
            val msg = Message()
            msg.arg1 = 0
            msg.arg2 = (marginRight - mHalfScreenWidth)
            msg.what = MESSAGE_SCROLL_TO_SCREEN_MID_FROM_LEFT
            mHandler.sendMessageDelayed(msg, 10)
        }
    }

    override fun onClick(timeLineView: TimeLineView) {
    }

    override fun onLongClick(timeLineView: TimeLineView) {
        //drag
        timeLineView.startDrag(null, View.DragShadowBuilder(timeLineView), null, 0)
    }

//    private var mIsInDragDestination = false
//    override fun onDrag(v: View?, dragEvent: DragEvent?): Boolean {
//        val action = dragEvent?.action
//        when (action) {
//            DragEvent.ACTION_DRAG_STARTED -> {
//                Toast.makeText(context, "开始拖动\n x = " + dragEvent?.x + "\ny = " + dragEvent?.y, Toast.LENGTH_LONG).show()
//                mIsInDragDestination = false
//            }
//            DragEvent.ACTION_DRAG_ENTERED -> {
//                Toast.makeText(context, "进入目标区域", Toast.LENGTH_LONG).show()
//                mIsInDragDestination = true
//            }
//
//            DragEvent.ACTION_DRAG_EXITED -> {
//                Toast.makeText(context, "离开", Toast.LENGTH_LONG).show()
//                mIsInDragDestination = false
//            }
//
//            DragEvent.ACTION_DROP -> {
//                Toast.makeText(context, "放手\n x = " + dragEvent.getX() + "\ny = " + dragEvent.getY(), Toast.LENGTH_LONG).show()
//                val dragView = dragEvent.localState as? View
//                if (v !== dragView) {
//                    swapViewGroupChildren(this, v, dragView)
//                }
//            }
//
//            DragEvent.ACTION_DRAG_LOCATION -> {
//            }
//        }
//        return true
//    }

    private fun modifyMarginStart(marginStartOffsetX:Int){
        val lp = layoutParams as? MarginLayoutParams ?: return
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

        layoutParams = lp
    }

    private fun modifyMarginEnd(marginEndOffsetX:Int){
        val lp = layoutParams as? MarginLayoutParams ?: return
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

        layoutParams = lp
    }

    public interface Callback{
        fun onItemLongClick(index: Int?)
    }
}
