package com.allever.videoeditordemo

import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup


object MyDragHelper {

    fun setupDragSort(targetView: View?) {
        targetView?.setOnDragListener { view, dragEvent ->
            val dragedView = dragEvent?.localState as? View
            val parent = view?.parent as? ViewGroup
            when(dragEvent?.action){

                DragEvent.ACTION_DRAG_STARTED ->{

                }

                DragEvent.ACTION_DRAG_ENDED ->{

                }

                DragEvent.ACTION_DROP ->{
                    //放手
                    //交换两个item
                    if (view != dragedView){
                        swapItem(parent, view, dragedView)
                    }
                }
            }
            true
        }

        targetView?.setOnLongClickListener {
            val dragState = DragState(targetView)
            targetView.startDrag(null, View.DragShadowBuilder(targetView), targetView, 0)
            true
        }
    }

    private fun swapItem(viewGroup: ViewGroup?, firstView: View?, secondView: View?){
        viewGroup?: return
        val firstIndex = viewGroup.indexOfChild(firstView)
        val secondIndex = viewGroup.indexOfChild(secondView)
        if (firstIndex < secondIndex) {
            viewGroup.removeViewAt(secondIndex)
            viewGroup.removeViewAt(firstIndex)
            viewGroup.addView(secondView, firstIndex)
            viewGroup.addView(firstView, secondIndex)
        } else {
            viewGroup.removeViewAt(firstIndex)
            viewGroup.removeViewAt(secondIndex)
            viewGroup.addView(firstView, secondIndex)
            viewGroup.addView(secondView, firstIndex)
        }
    }

    private class DragState(view: View){
        var index: Int = 0
        init {
            index = (view.parent as? ViewGroup)?.indexOfChild(view)?: 0
        }
    }

}