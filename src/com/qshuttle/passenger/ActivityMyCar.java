/**
 * 
 */
package com.qshuttle.passenger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
public class ActivityMyCar extends MapActivity{
	
	/*
	 * 
	 */
	private static final int HANDLER_MSG_REFRESH		= 1;
	private static final int HANDLER_MSG_CARMOVE		= 1 + HANDLER_MSG_REFRESH;
	private static final int HANDLER_MSG_NOCAR			= 1 + HANDLER_MSG_CARMOVE;
	
	private static final String BUNDLE_TOAST			= "bundle_toast";
	
	private Context context;
	private MapView mapView;
	private MapController mapController;
	private MyCarItemizedOverlay myCarOverlay;
	
	private String carNumber, driverName, driverCellPhone;
	private long carPosLatE6, carPosLongE6;
	private TextView tvCarNumber, tvDriverName;
	
	private boolean running = false;
	
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_REFRESH:
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseMyCar);
				webApi.queryMyCar(PrefProxy.getAccount(context));
				
				break;
				
			case HANDLER_MSG_CARMOVE:
				try{
					if(myCarOverlay != null){
						if(mapView.getOverlays().contains(myCarOverlay)){
					
							mapView.getOverlays().remove(myCarOverlay);
						}
					}
					
					Bundle bundle = msg.getData();
					GeoPoint point = new GeoPoint(carPosLatE6,
							carPosLongE6);
					
					OverlayItem item = new OverlayItem(point, StampToHHMM(System.currentTimeMillis()), " " +carNumber + " " +driverName);
					
					Drawable marker = context.getResources().getDrawable(R.drawable.marker_mycar);
					
					myCarOverlay = new MyCarItemizedOverlay(marker, item);
					
					mapView.getOverlays().add(myCarOverlay);
					
					tvCarNumber.setText(carNumber);
					tvDriverName.setText(driverName);
					
					mapView.invalidate();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				break;
				
			case HANDLER_MSG_NOCAR:
				
				Toast.makeText(context, msg.getData().getString(BUNDLE_TOAST), Toast.LENGTH_LONG).show();
				
				break;
				
			}
		}
		
		
	};
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonCallDriver:
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + driverCellPhone));
				
				startActivityForResult(intent, 0);

				
				break;
				
			case R.id.buttonCallService:
				intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + "4008721333"));
				
				startActivityForResult(intent, 0);

					
				break;
			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseMyCar = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				Log.i(this.getClass().getName(), response);
				try{
					JSONObject obj = new JSONObject(response);
					carNumber = obj.getString(WebApi.API_RESP_CAR_NUMBER);
					driverName = obj.getString(WebApi.API_RESP_DRIVER_NAME);
					driverCellPhone = obj.getString(WebApi.API_RESP_DRIVER_PHONE);
					carPosLatE6 = obj.getLong(WebApi.API_RESP_CAR_POS_LATE6);
					carPosLongE6 = obj.getLong(WebApi.API_RESP_CAR_POS_LONGE6);
					handler.sendEmptyMessage(HANDLER_MSG_CARMOVE);
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_NOCAR;
				String resp_msg = new WebApi(context).getRespMsg(response);
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_TOAST, resp_msg);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
		
	};

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		this.context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mycar);
		
		mapView = (MapView)findViewById(R.id.mapView);
	    mapView.setVectorMap(true);
	    mapView.setTraffic(false);
	    mapView.setBuiltInZoomControls(true);
	        
	    mapController = mapView.getController();
	    GeoPoint myPoint = new GeoPoint(PrefProxy.getMyLate6(context),PrefProxy.getMyLonge6(context));
	    mapController.setCenter(myPoint);
	    mapController.setZoom(12);
	    
	    
	    tvCarNumber = (TextView) findViewById(R.id.textViewCarNumber);
	    tvDriverName = (TextView) findViewById(R.id.textViewDriverName);
	    
	    ((Button)findViewById(R.id.buttonCallDriver)).setOnClickListener(lsrButton);
	    ((Button)findViewById(R.id.buttonCallService)).setOnClickListener(lsrButton);
	    
//	    handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
	    
	    thread.start();

	}
	
	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		running = false;
	}


	private Thread thread = new Thread(){

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			running = true;
			while(running){
				try{
					handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
					sleep(60000);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
		
	};
	
	
	public  String StampToHHMM(long stamp){
		
		String strStamp = StampToString(stamp);
		
		String strHHMM = strStamp.substring("yyyy-mm-dd ".length(),"yyyy-mm-dd hh:mm".length());
		
		String strHH = strHHMM.substring(0,"HH".length());
		
		String strMM = strHHMM.substring("HH:".length());
		
		int hh = Integer.parseInt(strHH);
		/*
		if(hh > 12){
			hh -= 12;
		}
		*/
		strHHMM = hh + ":" + strMM;
		
		return strHHMM;
		
    }

	public static String StampToString(long stamp){
		
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		date =  calendar.getTime();
		
//    	String format = "yyyy-MM-dd HH:mm:ss.SSSZ"; 
    	String format = "yyyy-MM-dd HH:mm:ss.SSSZ"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));	
		sdf.setTimeZone(TimeZone.getTimeZone("PRC"));	
		
		return sdf.format(date);
		
    }

	

}
