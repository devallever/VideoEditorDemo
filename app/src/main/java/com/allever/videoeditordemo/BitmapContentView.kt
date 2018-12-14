package com.allever.videoeditordemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.allever.videoeditordemo.test.DevideUtils

class BitmapContentView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val TAG = BitmapContentView::class.java.simpleName

        private const val DEFAULT_BITMAP_DP_SIZE = 60F

        private const val DEFAULT_DP_PADDING = 0F

    }

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    private var mPadding = 0

    private var mBitmapPaint: Paint = Paint()

    private var mBitmapList = mutableListOf<Bitmap>()

    init {
        mBitmapWidth = DevideUtils.dip2px(context, DEFAULT_BITMAP_DP_SIZE)
        mBitmapHeight = mBitmapWidth

        mPadding = DevideUtils.dip2px(context, DEFAULT_DP_PADDING)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val dataCount = mBitmapList.size
        val width = mBitmapWidth * dataCount + paddingLeft + paddingRight + mPadding * (dataCount - 1)
        val height = mBitmapHeight + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged()")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw()")
        super.onDraw(canvas)

        for (position in 0 until mBitmapList.size){
            val left = mBitmapWidth * position.toFloat() + paddingLeft + mPadding * position
            val top = 0f + paddingTop
            canvas?.drawBitmap(mBitmapList[position], left, top, mBitmapPaint)
        }

    }

    fun setData(bitmapList: MutableList<Bitmap>?){
        Log.d(TAG, "setData()")
        mBitmapList = bitmapList?: return
        requestLayout()
    }

    fun addData(bitmap: Bitmap?){
        mBitmapList.add(bitmap?: return)
        requestLayout()
    }

    fun getFirst(): Bitmap?{
        if (mBitmapList.isEmpty()){
            return null
        }
        return mBitmapList[0]
    }


    /***
     * 单位：dp
     */
    fun setBitmapWHSize(width: Float = DEFAULT_BITMAP_DP_SIZE, height: Float = DEFAULT_BITMAP_DP_SIZE){
        mBitmapWidth = DevideUtils.dip2px(context, width)
        mBitmapHeight = DevideUtils.dip2px(context, height)
        requestLayout()
    }

    /***
     * 单位：dp
     */
    fun setPadding(padding: Float = DEFAULT_DP_PADDING){
        mPadding = DevideUtils.dip2px(context, padding)
        requestLayout()
    }


}