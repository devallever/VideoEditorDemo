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
import java.lang.ref.SoftReference
import java.util.HashMap
import kotlin.math.log

class DragRvAdapter: RecyclerView.Adapter<DragRvAdapter.MyViewHolder> {


    private var mBitmapListList = mutableListOf<MutableList<Bitmap>>()
    private var mCallback: Callback? = null
    private var mContext: Context? = null

    private var cacheThumb = HashMap<String, SoftReference<Bitmap>>()
    private var mDragRVCallBack: DragRVCallBack? = null
    public var show = SparseArray<Int>()

    private var mRecyclerView: RecyclerView? = null

    private var mFocusPosition = 0

    constructor(context: Context, bitmapList: MutableList<MutableList<Bitmap>>,  callback: Callback, dragRVCallBack: DragRVCallBack?, recyclerView: RecyclerView?){
        mCallback = callback
        mContext = context
        mBitmapListList = bitmapList
        mDragRVCallBack = dragRVCallBack
        mRecyclerView = recyclerView
    }

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        var itemView = LayoutInflater.from(mContext).inflate(R.layout.item_drag, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = mBitmapListList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bitmap = mBitmapListList[position]

        val timeLineView = holder.timeLineView


        if (timeLineView?.getContentViewCount() == 0){
            //add view
            val bcv = BitmapContentView(mContext)
            bcv.setData(mBitmapListList[position])
            timeLineView?.addContentView(bcv)
        }
//        else{
//            timeLineView?.removeAllview()
//        }


        timeLineView?.setOptionListener(object : TimeLineView.OnOptinListener{
            override fun onClick(timeLineView: TimeLineView) {
                mFocusPosition = position
            }

            override fun onLongClick(timeLineView: TimeLineView) {
                mCallback?.onLongClick(position, holder)
            }

        })








//        holder.imageView?.setImageBitmap(bitmap)
//        holder.imageView?.setOnClickListener {
//            mCallback?.onClick(position)
//        }
//        holder.imageView?.setOnLongClickListener { view ->
//            mCallback?.onLongClick(position)
//            true
//        }
    }

    fun setData(data: MutableList<MutableList<Bitmap>>){
        mBitmapListList = data
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

//        var imageView: ImageView? = null
        var timeLineView: TimeLineView? = null
        init {
//            imageView = itemView.findViewById(R.id.id_item_drag_iv)
            timeLineView = itemView.findViewById(R.id.id_item_drag_time_line_view)

            timeLineView?.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    Log.d("focus", "focus has focus")
                    v?.scaleX = 1.5f
                    v?.scaleY = 1.5f
                    mRecyclerView?.invalidate()
                } else {
                    v?.scaleX = 1.0f
                    v?.scaleY = 1.0f
                    Log.d("focus", "focus has no focus")
                }
            }
        }
    }

    fun getFocusPosition(): Int{
        return mFocusPosition
    }

    public interface Callback{
        fun onClick(position: Int?)
        fun onLongClick(position: Int?, holder: MyViewHolder)
    }
}