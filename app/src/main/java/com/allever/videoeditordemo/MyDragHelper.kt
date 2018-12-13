package com.allever.videoeditordemo

import android.animation.ObjectAnimator
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import com.allever.videoeditordemo.AppUtils.postOnPreDraw


// TODO: Provide scrolling at edge, see
// https://github.com/justasm/DragLinearLayout/blob/master/library/src/main/java/com/jmedeisis/draglinearlayout/DragLinearLayout.java
// and
// https://github.com/nhaarman/ListViewAnimations/blob/master/lib-manipulation/src/main/java/com/nhaarman/listviewanimations/itemmanipulation/dragdrop/DragAndDropHandler.java

//https://gist.github.com/DreaminginCodeZH/b0c42a56e49ca907a9cb

//参考
//https://blog.zhanghai.me/drag-and-drop-with-animation-on-linearlayout/

object MyDragHelper {

    private var TAG = MyDragHelper::class.java.simpleName

//    fun setLongClick(view: View?, callback: TimeLineViewLayout.Callback?){
//        view?.setOnLongClickListener {
//            val index = (view.parent as? ViewGroup)?.indexOfChild(view)?: 0
//            Log.d(TAG, "index = $index")
//            callback?.onItemLongClick(index)
//            false
//
//        }
//    }

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
                    if (targetView != dragView) {
                        val viewGroup = targetView?.parent as? ViewGroup
                        //接受拖拽事件view的下标
                        val targetIndex = viewGroup?.indexOfChild(targetView)?: return@setOnDragListener true
                        val dragIndex = dragState?.index
                        //1/2范围有点小
                        val halfTargetHeight = targetView.height / 2
                        val dragEvenY = dragEvent.y
                        //dragEvenY > halfTargetHeight: 触控点Y坐标，小于目标区域高度的1/2，注意是越往上，y值越小
//                        if ((targetIndex > dragIndex?:0 && dragEvenY > halfTargetHeight)
//                            || (targetIndex < dragIndex?:0 && dragEvenY < halfTargetHeight)) {
                        if (dragIndex != targetIndex && dragEvenY > halfTargetHeight) {
                            swapViews(viewGroup, targetView, targetIndex, dragState)
                            Log.d(TAG, "ACTION_DRAG_LOCATION swapViews()")
                        } else {
                            swapViewsBetweenIfNeeded(viewGroup, targetIndex, dragState)
                            Log.d(TAG, "ACTION_DRAG_LOCATION swapViewsBetweenIfNeeded()")
                        }
                    }
                }

                DragEvent.ACTION_DRAG_ENDED ->{
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
//                    Log.d(TAG, "ACTION_DROP")
                    //放手
                    //交换两个item
//                    if (targetView != dragView){
//                        swapItem(parent, targetView, dragView)
//                    }
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

    /***
     * 思想：从相邻的一致交换到目标位置
     * 判断是否相邻，不相邻则交换前一个/后一个
     */
    private fun swapViewsBetweenIfNeeded(viewGroup: ViewGroup?, targetIndex:Int,
                                         dragState: DragState?){
        dragState?: return
        val dragIndex = dragState.index
        //例如：交换0和3， A -> C ，先A和B交换，再A和C交换, 反方向类似
        if (targetIndex - dragIndex> 1) {
            val indexAbove = targetIndex - 1 //1
            swapViews(viewGroup, viewGroup?.getChildAt(indexAbove), indexAbove, dragState)
        } else if (dragIndex - targetIndex > 1) {
            val indexBelow = targetIndex + 1
            swapViews(viewGroup, viewGroup?.getChildAt(indexBelow), indexBelow, dragState)
        }
    }

    private fun swapViews(viewGroup: ViewGroup?, targetView: View?, targetIndex: Int,
                          dragState: DragState?){
        //处理相邻问题
        swapViewsBetweenIfNeeded(viewGroup, targetIndex, dragState)
        val targetViewY = targetView?.y
        swapItem(viewGroup, targetView, dragState?.view)
        dragState?.index = targetIndex

        postOnPreDraw(targetView, object :Runnable{
            override fun run() {
                val anim = ObjectAnimator.ofFloat(targetView, View.Y, targetViewY?:0f, targetView?.top?.toFloat()?:0f)
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