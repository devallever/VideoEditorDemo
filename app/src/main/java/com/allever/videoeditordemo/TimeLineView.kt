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
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnTouchListener {

    companion object {
        private val TAG = TimeLineView::class.java.simpleName
    }

    private var mRootView: View? = null
    private var mIvStart: ImageView? = null
    private var mIvEnd: ImageView? = null
    private var mContentContainer: LinearLayout? = null

    private var mBackground: Drawable? = null

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

        //
        mIvStart?.post {
            showFrame(false)
        }


    }

    private var mShowFrame = false
    override fun onClick(v: View?) {
        when (v){
            mRootView -> {
                //debug
                mShowFrame = !mShowFrame
                showFrame(mShowFrame)
            }
        }
    }

    private var mOffsetX = 0
    private var mLastRawX = 0F
    private var mOriginRawX = 0F
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v){
            mIvEnd -> {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        mOriginRawX = event.rawX
                        mLastRawX  = mOriginRawX
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val currentRawX = event.rawX
                        val offsetX = currentRawX - mLastRawX

//                        mOffsetX = (currentRawX - mOriginRawX).toInt()
//                        mOffsetX = offsetX.toInt()

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
                            lp.width = width.toInt()
                            mContentContainer?.layoutParams = lp
                        }

                        mLastRawX = currentRawX
                    }
                }
            }

            mIvStart -> {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> {
                        mOriginRawX = event.rawX
                        mLastRawX  = mOriginRawX
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val currentRawX = event.rawX
                        val offsetX = currentRawX - mLastRawX

//                        //修改控件内容器宽度
                        val containerWidth = mContentContainer?.width
                        val lp = mContentContainer?.layoutParams

                        lp?.width = containerWidth
                        //-offsetX：与右边箭头相反
                        val width = lp?.width!! + (-offsetX)
                        //处理滑动到右边箭头后禁止继续滑动
                        if (width > 0){
                            lp.width = width.toInt()
                            mContentContainer?.layoutParams = lp
                            //修改控件位置
//                            modifyMarginStart(this, offsetX.toInt())

                            //修改父控件MarginStart，实现整体左移，不需要修改该控件的位置了
                            val parent = parent as? ViewGroup
                            val parentLp = parent?.layoutParams as? MarginLayoutParams
                            val marginStart = parentLp?.leftMargin ?: 0
                            Log.d(TAG, "parent margin Start = $marginStart")
                            modifyMarginStart(parent,offsetX.toInt() )

                        }

                        mLastRawX = currentRawX
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


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent() return $result")
        return result
    }

}