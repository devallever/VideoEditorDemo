package de.vogella.android.draganddrop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalScrollViewVideoPart extends HorizontalScrollView {

	private OnScrollListener mOnScrollListener;

	public HorizontalScrollViewVideoPart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HorizontalScrollViewVideoPart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollListener(OnScrollListener l) {
		this.mOnScrollListener = l;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollListener != null)
			mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
	}

	public static interface OnScrollListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
}
