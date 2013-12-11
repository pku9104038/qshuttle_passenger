/**
 * 
 */
package com.qshuttle.passenger;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;

/**
 * @author wangpeifeng
 *
 */
public class MapGestureDetectorOverlay extends Overlay implements OnGestureListener {

	private OnGestureListener listener;
	private GestureDetector detector;
	
	
	/**
	 * @param listener
	 */
	public MapGestureDetectorOverlay(Context context,OnGestureListener listener) {
		super();
		this.listener = listener;
		this.detector = new GestureDetector(context, listener);
	}

	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.Overlay#onTouchEvent(android.view.MotionEvent, com.amap.mapapi.map.MapView)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// TODO Auto-generated method stub
		if(this.detector.onTouchEvent(event)){
			Log.i(this.getClass().getName(), "touch event");
			return true;
		}
		else{
			return false;
		}
		//return super.onTouchEvent(arg0, arg1);
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return this.listener.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return this.listener.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		listener.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return this.listener.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		this.listener.onShowPress(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return this.listener.onSingleTapUp(e);
	}

	public void setIsLongpressEnabled(boolean isLongpressEnabled) {
		this.detector.isLongpressEnabled();
	}
	
	public boolean isLongpressEnabled(){
		return this.detector.isLongpressEnabled();
	}
}
