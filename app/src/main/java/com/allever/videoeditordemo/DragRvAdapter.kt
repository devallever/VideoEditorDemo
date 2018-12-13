package com.allever.videoeditordemo

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.lang.ref.SoftReference
import java.util.HashMap

class DragRvAdapter: RecyclerView.Adapter<DragRvAdapter.MyViewHolder> {


    private var mBitmapList = mutableListOf<Bitmap>()
    private var mCallback: Callback? = null
    private var mContext: Context? = null

    private var cacheThumb = HashMap<String, SoftReference<Bitmap>>()
    private var mDragRVCallBack: DragRVCallBack? = null
    public var show = SparseArray<Int>()

    constructor(context: Context, bitmapList: MutableList<Bitmap>,  callback: Callback, dragRVCallBack: DragRVCallBack?){
        mCallback = callback
        mContext = context
        mBitmapList = bitmapList
        mDragRVCallBack = dragRVCallBack
    }

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.item_drag, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = mBitmapList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = mBitmapList[position]
        holder.imageView?.setImageBitmap(bitmap)
        holder.imageView?.setOnClickListener {
            mCallback?.onClick(position)
        }
        holder.imageView?.setOnLongClickListener { view ->
            mCallback?.onLongClick(position)
            true
        }
    }

    fun setData(data: MutableList<Bitmap>){
        mBitmapList = data
    }


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DragHolderCallBack {
        private val TAG = MyViewHolder::class.java.simpleName
        override fun onSelect() {
            Log.d(TAG, "onSelect()")
            show.clear()
            show.put(adapterPosition, adapterPosition)
            val duration = 100
            val pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f)
            val pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f)
            ObjectAnimator.ofPropertyValuesHolder(itemView, pvh2, pvh3).setDuration(duration.toLong()).start()
        }

        override fun onUnSelect() {
            Log.d(TAG, "onUnSelect()")
            val duration = 100
            val pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1.1f, 1f)
            val pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1.1f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(itemView, pvh2, pvh3).setDuration(duration.toLong()).start()
        }

        override fun onClear() {
            Log.d(TAG, "onClear()")
            onUnSelect()
            notifyDataSetChanged()
        }

        var imageView: ImageView? = null
        init {
            imageView = itemView.findViewById(R.id.id_item_drag_iv)
        }
    }

    public interface Callback{
        fun onClick(position: Int?)
        fun onLongClick(position: Int?)
    }
}