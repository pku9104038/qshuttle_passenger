/**
 * 
 */
package com.qshuttle.passenger;

import android.graphics.drawable.Drawable;

import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;

/**
 * @author wangpeifeng
 *
 */
public class PickupPointItemizedOverlay extends ItemizedOverlay{

	private OverlayItem item;
	public PickupPointItemizedOverlay(Drawable marker, OverlayItem item) {
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

}
