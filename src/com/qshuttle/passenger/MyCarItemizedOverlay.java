/**
 * 
 */
package com.qshuttle.passenger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;

/**
 * @author wangpeifeng
 *
 */
public class MyCarItemizedOverlay extends ItemizedOverlay{
	
	private OverlayItem item;
	public MyCarItemizedOverlay(Drawable marker, OverlayItem item) {
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
		this.item = item;
		populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		// TODO Auto-generated method stub
		return this.item;
	}

	

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.Overlay#draw(android.graphics.Canvas, com.amap.mapapi.map.MapView, boolean, long)
	 */
	@Override
	public boolean draw(Canvas canvas, MapView view, boolean shadow, long arg3) {
		// TODO Auto-generated method stub
		drawTitle(canvas,view,shadow);
		return super.draw(canvas, view, shadow, arg3);
		
	}
	
	private void drawTitle(Canvas canvas, MapView mapview, boolean shadow){
		
		GeoPoint point = item.getPoint();
		Point out = new Point();
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setTextSize((float) 20.0);

		mapview.getProjection().toPixels(point, out);
		canvas.drawText(item.getTitle()+item.getSnippet(),out.x, out.y, paint);
		Log.i(this.getClass().getName(), item.getTitle()+":"+point.getLatitudeE6() + "/" + point.getLongitudeE6() +","+out.x+"/"+out.y);
		
	}
	
	
}
