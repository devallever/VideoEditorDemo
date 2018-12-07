package com.allever.videoeditordemo.test

//package com.allever.videoeditordemo
//
//import android.content.Context
//import android.graphics.Color
//import android.os.Build
//import android.support.annotation.RequiresApi
//import android.support.v4.content.res.ResourcesCompat
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.util.AttributeSet
//import android.util.Log
//import android.view.*
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//
///***
// * 文本时间轴
// */
//class TextTimeLine: BaseTimeLineView {
//    constructor(context: Context) : this(context, null) {}
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
//            : super(context, attrs, defStyleAttr, defStyleRes) {
//    }
//
//    private var textView: TextView? = null
//
//    override fun initView() {
//        super.initView()
//        textView = TextView(context)
//        textView?.textSize = 16f
//        textView?.text = "hello"
//        textView?.gravity = Gravity.CENTER_VERTICAL
//        textView?.setTextColor(Color.parseColor("#FFFFFF"))
//        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        mContentContainer?.addView(textView, lp)
//
//        height = 120
//
//        textView?.setOnTouchListener(this)
//        hideFrame(true)
//    }
//
////    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////        super.onTouch(mRootView, event)
////        return true
////    }
//
//    fun setContent(content: String){
//        textView?.text = content
//    }
//
//}
//
///***
// * 视频时间轴
// */
//class ListTimeLine: BaseTimeLineView, View.OnTouchListener{
//    constructor(context: Context) : this(context, null) {}
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
//            : super(context, attrs, defStyleAttr, defStyleRes) {
//    }
//
//    private var adapter: RVAdapter? = null
//    private var recyclerView: RecyclerView? = null
//
//    override fun initView() {
//        super.initView()
//        recyclerView = RecyclerView(context)
//        adapter = RVAdapter(context)
//        val layoutManager = LinearLayoutManager(context)
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        recyclerView?.layoutManager = layoutManager
//        recyclerView?.adapter = adapter
//        adapter?.setClickListener(this)
////        recyclerView?.setOnTouchListener(this)
//        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        mContentContainer?.addView(recyclerView, lp)
//        hideFrame(true)
//
//
////        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
////
////            }
////
////            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
////                Log.d("test", "dx = $dx")
////            }
////        })
//    }
//
////    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
////        when(v){
////            recyclerView -> {
////            }
////        }
////        super.onTouch(mRootView, event)
////        return true
////    }
//
//    override fun onClick(v: View?) {
////        recyclerView?.background = ResourcesCompat.getDrawable(resources, R.drawable.time_line_bg, null)
//        super.onClick(mContentContainer)
//    }
//
//    fun setData(resIdList: MutableList<Int>){
//        adapter?.setData(resIdList)
//    }
//
//
//    private class RVAdapter(val context: Context): RecyclerView.Adapter<MyViewHolder>() {
//        private var mDatas = mutableListOf<Int>()
//        private var mClickListener: OnClickListener? = null
//        override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
//            val itemView = LayoutInflater.from(context).inflate(R.layout.item_video_time_line, parent, false)
//            return MyViewHolder(itemView)
//        }
//
//        override fun getItemCount(): Int {
//            return mDatas.size
//        }
//
//        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            holder.ivBitmapFrame?.setImageResource(mDatas[position])
//            holder.itemView.setOnClickListener(mClickListener)
//        }
//
//        fun setData(resIdList: MutableList<Int>){
//            mDatas = resIdList
//            notifyDataSetChanged()
//        }
//
//        fun setClickListener(onClickListener: OnClickListener){
//            mClickListener = onClickListener
//        }
//
//    }
//
//    private class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        var ivBitmapFrame: ImageView? = null
//        init {
//            ivBitmapFrame = itemView.findViewById(R.id.id_item_video_time_line_iv)
//        }
//
//    }
//}