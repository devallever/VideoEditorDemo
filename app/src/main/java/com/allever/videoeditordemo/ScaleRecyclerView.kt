package com.allever.videoeditordemo

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View


class ScaleRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mSelectedPosition = 0

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onDraw(c: Canvas?) {
        if (adapter is DragRvAdapter){
            val adapter = adapter as DragRvAdapter
            mSelectedPosition = adapter.getFocusPosition()
            Log.d("RV", "RV focus position = $mSelectedPosition")
        }else{
            mSelectedPosition = getChildAdapterPosition(focusedChild)
        }
        super.onDraw(c)
    }


    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        var position = mSelectedPosition
        Log.d("RV", "i = $i,  mSelectedPosition = $mSelectedPosition")
        if (position < 0) {
            Log.d("RV", "最后绘制 i = $i")
            return i
        } else {
            if (i == childCount - 1) {
                if (position > i) {
                    position = i
                }
                Log.d("RV", "最后绘制 position = $position")
                return position
            }
            if (i == position) {
                Log.d("RV", "最后绘制 childCount - 1 = ${childCount - 1}")
                return childCount - 1
            }
        }
        Log.d("RV", "最后绘制 i = $i")
        return i
    }
}