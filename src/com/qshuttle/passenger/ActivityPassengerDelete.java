/**
 * 
 */
package com.qshuttle.passenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;

/**
 * @author wangpeifeng
 *
 */
public class ActivityPassengerDelete extends MapActivity{
	
	/*
	 * CONTSTANTS, PRIVATE 
	 */
	private static final int HANDLER_MSG_WHAT_LONG_PRESS 	= 1;
	private static final int HANDLER_MSG_WHAT_NOT_AVAILABLE	= 1 + HANDLER_MSG_WHAT_LONG_PRESS;
	
	
	
	private static final String BUNDLE_KEY_LATE6 			= "bundle_key_late6";
	private static final String BUNDLE_KEY_LONGE6 			= "bundle_key_longe6";
	
	/*
	 * CONSTANTS, PUBLIC 
	 */
	public static final String EXTRA_SERIAL					= "extra_serial";
	public static final String EXTRA_NAME					= "extra_name";
	public static final String EXTRA_PHONE					= "extra_phone";
	public static final String EXTRA_ADDRESS				= "extra_address";
	public static final String EXTRA_LATE6					= "extra_late6";
	public static final String EXTRA_LONGE6					= "extra_longe6";
	
	
	/*
	 * PROPERTIES, PRIVATE
	 */
	
	private Context context;
	private MapView mapView;
	private MapController mapController;
	private PickupPointItemizedOverlay pickupOverlay;
	private MapGestureDetectorOverlay mapGestureDetectorOverlay;
	
	private PassengerInfos passenger;
	private EditText etName, etPhone, etAddress;
	
	private RelativeLayout lytPassengerInfo;
	private boolean mapEnlarged;
	private ImageButton btnEnlarge;
	
	private int resultCode;
	private Intent intentResult;
	
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	
	private OnGestureListener lsrGesture = new OnGestureListener(){

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
 			GeoPoint point = mapView.getProjection().fromPixels((int)(e.getX()), (int)(e.getY()));
 			
 			passenger.getAddressInfos().setPosition(point.getLatitudeE6(), point.getLongitudeE6());
 			
 			Message msg = new Message();
 			msg.what = HANDLER_MSG_WHAT_LONG_PRESS;
 			
 			Bundle bundle = new Bundle();
 			bundle.putInt(BUNDLE_KEY_LATE6, point.getLatitudeE6());
 			bundle.putInt(BUNDLE_KEY_LONGE6, point.getLongitudeE6());
 			msg.setData(bundle);
 			
 		    handler.sendMessage(msg);
 		    
 		    
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	};
	
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.imageButtonBack:
				resultCode = Activity.RESULT_CANCELED;
				finish();
				break;
				
			case R.id.buttonAdd:
				passenger.setName(etName.getText().toString());
				passenger.setPhone(etPhone.getText().toString());
				passenger.getAddressInfos().setAddress(etAddress.getText().toString());
				
				if(passenger.isAvailable()){
					intentResult = new Intent();
					intentResult.putExtra(EXTRA_SERIAL, passenger.getSerial());
					intentResult.putExtra(EXTRA_NAME, passenger.getName());
					intentResult.putExtra(EXTRA_PHONE, passenger.getPhone());
					intentResult.putExtra(EXTRA_ADDRESS, passenger.getAddressInfos().getAddress());
					intentResult.putExtra(EXTRA_LATE6, passenger.getAddressInfos().getLatE6());
					intentResult.putExtra(EXTRA_LONGE6, passenger.getAddressInfos().getLongE6());
					resultCode = Activity.RESULT_OK;
					finish();
				}
				else{
					handler.sendEmptyMessage(HANDLER_MSG_WHAT_NOT_AVAILABLE);
				}
				break;
				
			case R.id.imageButtonMapSwitch:
				if(mapEnlarged){
					lytPassengerInfo.setVisibility(View.VISIBLE);
					btnEnlarge.setBackgroundResource(R.drawable.btn_enlarge);
					btnEnlarge.setImageResource(R.drawable.btn_enlarge);
				}
				else{
					lytPassengerInfo.setVisibility(View.GONE);
					btnEnlarge.setBackgroundResource(R.drawable.btn_narrow);
					btnEnlarge.setImageResource(R.drawable.btn_narrow);
				}
				mapEnlarged = !mapEnlarged;
				break;
			}
			
		}
		
	};
	
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			
			case HANDLER_MSG_WHAT_LONG_PRESS:
				if(pickupOverlay != null && mapView.getOverlays().contains(pickupOverlay)){
					mapView.getOverlays().remove(pickupOverlay);
				}
				
				Bundle bundle = msg.getData();
				GeoPoint pickupPoint = new GeoPoint(bundle.getInt(BUNDLE_KEY_LATE6),
						bundle.getInt(BUNDLE_KEY_LONGE6));
				
				OverlayItem pickupItem = new OverlayItem(pickupPoint, "", "");
				
				Drawable marker = context.getResources().getDrawable(R.drawable.stop_car);
				
				pickupOverlay = new PickupPointItemizedOverlay(marker, pickupItem);
				
				mapView.getOverlays().add(pickupOverlay);
				
				mapController.animateTo(pickupPoint);
				
				break;
				
			case HANDLER_MSG_WHAT_NOT_AVAILABLE:
				
				Toast.makeText(context, 
						context.getResources().getText(R.string.passeger_info_not_available), 
						Toast.LENGTH_LONG).show();
				break;
			}
		}
		
		
		
	};
	
	/*
	 * METHODS
	 */
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.context = this;
		
		this.passenger = new PassengerInfos();
		Intent intent = getIntent();
		passenger.setSerial(intent.getIntExtra(ActivityPassengerEdit.EXTRA_SERIAL,0));
		passenger.setName(intent.getStringExtra(ActivityPassengerEdit.EXTRA_NAME));
		passenger.setPhone(intent.getStringExtra(ActivityPassengerEdit.EXTRA_PHONE));
		passenger.getAddressInfos().setAddress(intent.getStringExtra(ActivityPassengerEdit.EXTRA_ADDRESS));
		
		passenger.getAddressInfos().setPosition(intent.getIntExtra(ActivityPassengerEdit.EXTRA_LATE6,0),
				intent.getIntExtra(ActivityPassengerEdit.EXTRA_LONGE6,0));
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.booking_passenger_add);
		
		((Button)findViewById(R.id.buttonAdd)).setOnClickListener(lsrButton);
		((ImageButton)findViewById(R.id.imageButtonBack)).setOnClickListener(lsrButton);
		btnEnlarge = (ImageButton)findViewById(R.id.imageButtonMapSwitch);
		btnEnlarge.setOnClickListener(lsrButton);
		lytPassengerInfo = (RelativeLayout)findViewById(R.id.layoutPassengerInfo);
		mapEnlarged = false;
		
		etName = (EditText)findViewById(R.id.editTextName);
		etPhone = (EditText)findViewById(R.id.editTextPhone);
		etAddress = (EditText)findViewById(R.id.editTextAddress);

		etName.setText(passenger.getName());
		etPhone.setText(passenger.getPhone());
		etAddress.setText(passenger.getAddressInfos().getAddress());
		
		
		mapView = (MapView)findViewById(R.id.mapView);
	    mapView.setVectorMap(true);
	    mapView.setTraffic(false);
	    mapView.setBuiltInZoomControls(true);
	        
	    mapController = mapView.getController();
	    GeoPoint myPoint = new GeoPoint(passenger.getAddressInfos().getLatE6(),
	    		passenger.getAddressInfos().getLongE6());
	    
	    mapController.setCenter(myPoint);
	    mapController.setZoom(12);
	        	
	    mapGestureDetectorOverlay = new MapGestureDetectorOverlay(context, lsrGesture);
		mapView.getOverlays().clear();
        mapView.getOverlays().add(mapGestureDetectorOverlay);
  
			Message msg = new Message();
			msg.what = HANDLER_MSG_WHAT_LONG_PRESS;
			
			Bundle bundle = new Bundle();
			bundle.putInt(BUNDLE_KEY_LATE6, passenger.getAddressInfos().getLatE6());
			bundle.putInt(BUNDLE_KEY_LONGE6, passenger.getAddressInfos().getLongE6());
			msg.setData(bundle);
			
		    handler.sendMessage(msg);

		//LinearLayout lytPassengerAdd = (LinearLayout)findViewById(R.id.layoutPassengerAdd);
		//lytPassengerAdd.setOnClickListener(lsrPassengerAdd);
				
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		setResult(this.resultCode,this.intentResult);
		super.finish();
	}
	
	

}
