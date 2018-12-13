package de.vogella.android.draganddrop;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.allever.videoeditordemo.R;

public class HoDragActivity extends Activity {

	private LinearLayout main;

	private GestureDetector mGestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_cc);

		main = (LinearLayout) findViewById(R.id.main);

		bindDrapListener(R.id.myimage1);
		bindDrapListener(R.id.myimage2);
		bindDrapListener(R.id.myimage3);
		bindDrapListener(R.id.myimage4);
		bindDrapListener(R.id.myimage5);
		bindDrapListener(R.id.myimage6);
		bindDrapListener(R.id.myimage7);
		bindDrapListener(R.id.myimage8);
		bindDrapListener(R.id.myimage9);
		bindDrapListener(R.id.myimage10);
		bindDrapListener(R.id.myimage11);
		bindDrapListener(R.id.myimage12);

		mGestureDetector = new GestureDetector(this, new DrapGestureListener());
	}

	private View mDrapView;

	private void bindDrapListener(int id) {
		View v = findViewById(id);
		v.setOnTouchListener(mOnTouchListener);
		v.setOnDragListener(mOnDragListener);
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mDrapView = v;

			if (mGestureDetector.onTouchEvent(event))
				return true;

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_UP:

				break;
			}

			return false;
		}
	};

	private OnDragListener mOnDragListener = new OnDragListener() {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				v.setAlpha(0.5F);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setAlpha(1F);
				break;
			case DragEvent.ACTION_DROP:
				View view = (View) event.getLocalState();
				for (int i = 0, j = main.getChildCount(); i < j; i++) {
					if (main.getChildAt(i) == v) {
						// 当前位置
						main.removeView(view);
						main.addView(view, i);
						break;
					}
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setAlpha(1F);
			default:
				break;
			}
			return true;
		}
	};

	private class DrapGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return super.onSingleTapConfirmed(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			ClipData data = ClipData.newPlainText("", "");
			MyDragShadowBuilder shadowBuilder = new MyDragShadowBuilder(
					mDrapView);
			mDrapView.startDrag(data, shadowBuilder, mDrapView, 0);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	}

	private class MyDragShadowBuilder extends DragShadowBuilder {

		private final WeakReference<View> mView;

		public MyDragShadowBuilder(View view) {
			super(view);
			mView = new WeakReference<View>(view);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			canvas.scale(1.5F, 1.5F);
			super.onDrawShadow(canvas);
		}

		@Override
		public void onProvideShadowMetrics(Point shadowSize,
				Point shadowTouchPoint) {
			// super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

			final View view = mView.get();
			if (view != null) {
				shadowSize.set((int) (view.getWidth() * 1.5F),
						(int) (view.getHeight() * 1.5F));
				shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
			} else {
				// Log.e(View.VIEW_LOG_TAG,
				// "Asked for drag thumb metrics but no view");
			}
		}
	}
}