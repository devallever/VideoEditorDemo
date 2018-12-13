package com.allever.videoeditordemo

import android.animation.ObjectAnimator
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import com.allever.videoeditordemo.AppUtils.postOnPreDraw


object MyDragHelper {

    private var TAG = MyDragHelper::class.java.simpleName

    fun setLongClick(view: View?, callback: TimeLineViewLayout.Callback?){
        view?.setOnLongClickListener {
            val index = (view.parent as? ViewGroup)?.indexOfChild(view)?: 0
            Log.d(TAG, "index = $index")
            callback?.onItemLongClick(index)
            false

        }
    }

    fun setupDragSort(view: View?) {
        //The View that received the drag event.
        view?.setOnDragListener { targetView, dragEvent ->
            //view是接收拖拽事件的view
            //dragState存放的是被拖的view状态
            val dragState = dragEvent?.localState as? DragState
            var dragView = dragState?.view
            val parent = targetView?.parent as? ViewGroup
            when(dragEvent?.action){
                DragEvent.ACTION_DRAG_STARTED ->{
                    if (targetView == dragView) {
//                        targetView.visibility = View.INVISIBLE
                        Log.d(TAG, "set invisible")
                    }
                }

                DragEvent.ACTION_DRAG_LOCATION ->{
                    //拖动过程
                    Log.d(TAG, "ACTION_DRAG_LOCATION")
                    if (targetView != dragView) {
                        val viewGroup = targetView?.parent as? ViewGroup
                        //接受拖拽事件view的下标
                        val targetIndex = viewGroup?.indexOfChild(targetView)?: return@setOnDragListener true
                        if ((targetIndex > dragState?.index?:0 && dragEvent.y > targetView.height / 2)
                            || (targetIndex < dragState?.index?:0 && dragEvent.y < targetView.height / 2)) {
                            swapViews(viewGroup, targetView, targetIndex, dragState)
                        } else {
                            swapViewsBetweenIfNeeded(viewGroup, targetIndex, dragState)
                        }
                    }
                }

                DragEvent.ACTION_DRAG_ENDED ->{
                    Log.d(TAG, "ACTION_DRAG_ENDED")
//                    view.visibility = View.VISIBLE
//                    dragView?.visibility = View.VISIBLE
                    if (targetView == dragView) {
                        targetView.visibility = View.VISIBLE
                        Log.d(TAG, "view == dragState?.view")
                        Log.d(TAG, "set visible")
                    }else{
                        Log.d(TAG, "view != dragState?.view")
                    }
                }

                DragEvent.ACTION_DROP ->{
                    //先放手再结束
                    Log.d(TAG, "ACTION_DROP")
                    //放手
                    //交换两个item
                    if (targetView != dragView){
                        swapItem(parent, targetView, dragView)
                    }
                }
            }
            true
        }

        view?.setOnLongClickListener {
            val dragState = DragState(view)
            view.startDrag(null, View.DragShadowBuilder(view), dragState, 0)
            true
        }
    }

    private fun swapViewsBetweenIfNeeded(viewGroup: ViewGroup?, index:Int,
                                         dragState: DragState?){
        Log.d(TAG, "swap swapViewsBetweenIfNeeded()")
        dragState?: return
        if (index - dragState.index> 1) {
            val indexAbove = index - 1
            swapViews(viewGroup, viewGroup?.getChildAt(indexAbove), indexAbove, dragState)
        } else if (dragState.index - index > 1) {
            val indexBelow = index + 1
            swapViews(viewGroup, viewGroup?.getChildAt(indexBelow), indexBelow, dragState)
        }
    }

    private fun swapViews(viewGroup: ViewGroup?, view: View?, index: Int,
                          dragState: DragState?){
        Log.d(TAG, "swap swapViews()")
        swapViewsBetweenIfNeeded(viewGroup, index, dragState)
        val viewY = view?.y
        swapItem(viewGroup, view, dragState?.view)
        dragState?.index = index

        postOnPreDraw(view, object :Runnable{
            override fun run() {
                val anim = ObjectAnimator.ofFloat(view, View.Y, viewY?:0f, view?.top?.toFloat()?:0f)
                anim.duration = 500
                anim.start()
            }
        })
    }

    /***
     * @param dragView 拖动的View
     * @param targetView 接收事件的View， 目标区的View
     */

    private fun swapItem(viewGroup: ViewGroup?, targetView: View?, dragView: View?) {
        if (viewGroup == null || targetView == null || dragView == null){
            return
        }
        val targetIndex = viewGroup.indexOfChild(targetView)
        val dragIndex = viewGroup.indexOfChild(dragView)

        Log.d(TAG, "swapItem() dragIndex = $dragIndex")
        Log.d(TAG, "swapItem() targetIndex = $targetIndex")
        if (targetIndex < dragIndex) {
            viewGroup.removeViewAt(dragIndex)
            viewGroup.removeViewAt(targetIndex)
            viewGroup.addView(dragView, targetIndex)
            viewGroup.addView(targetView, dragIndex)
        } else {
            viewGroup.removeViewAt(targetIndex)
            viewGroup.removeViewAt(dragIndex)
            viewGroup.addView(targetView, dragIndex)
            viewGroup.addView(dragView, targetIndex)
        }
    }

    private class DragState(var view: View){
        var index: Int = 0
        init {
            index = (view.parent as? ViewGroup)?.indexOfChild(view)?: 0
            Log.d(TAG, "index = $index")
        }
    }

}