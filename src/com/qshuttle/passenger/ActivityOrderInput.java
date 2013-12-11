/**
 * 
 */
package com.qshuttle.passenger;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityOrderInput extends Activity{
	
	
	/////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private Context context;
	private ProgressBar progressBar;
	
	private int 	hub_serial,area_serial;
	private String 	hub_name,area_name,line_type, from, to;
	
	
	private Date date;
	private String  instance_date;
	private Calendar calendar;
	
	private LinearLayout lytPassenger,lytAccount;
	private RelativeLayout lytPay;
	private long clickPayStamp;
	
	private TextView tvPassengers,tvAccount,tvUserName;
	private TextView tvHubName, tvLineType, tvInstanceDate,
						tvDepartureTime, tvArriveTime, tvPrice,
						tvPriceTotal, tvFrom,tvTo;
	

	/////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	private static final int HANDLER_MSG_SUCCESS			= 1;
	private static final int HANDLER_MSG_FAILED				= 1 + HANDLER_MSG_SUCCESS;
	private static final int HANDLER_MSG_NULL				= 1 + HANDLER_MSG_FAILED;
	private static final int HANDLER_MSG_WAITING			= 1 + HANDLER_MSG_NULL;
	private static final int HANDLER_MSG_RUNNING			= 1 + HANDLER_MSG_WAITING;
	
	
	private static final String BUNDLE_KEY_TOAST			= "toast";
	
	private static final int ACTIVITY_PASSENGER_SELECT		= 1;
	private static final int ACTIVITY_LOGIN					= 1 + ACTIVITY_PASSENGER_SELECT;
	
	private static final int CLICK_DEBOUNCE					= 1000;
	
	
	private static final int HANDLER_MSG_ORDER_SUCCESS		= 1;
	private static final int HANDLER_MSG_ORDER_FAILED		= 1 + HANDLER_MSG_ORDER_SUCCESS;
	
	private boolean waiting = false;
	/*
	 * PROPERTIES, BEHAVIAR
	 */
	public OnClickListener lsrPassenger = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerSelect.class);
			startActivityForResult(intent,ACTIVITY_PASSENGER_SELECT);
			
		}
		
	};
	
	
	public OnClickListener lsrAccount = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityLogin.class);
			startActivityForResult(intent,ACTIVITY_LOGIN);
			
		}
		
	};
	
	public OnClickListener lsrPay = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(!waiting && System.currentTimeMillis() - clickPayStamp > CLICK_DEBOUNCE){
				if(PrefProxy.getMyOrderPriceTotal(context) > 
					PrefProxy.getMyInstancePrice(context)*PrefProxy.getMyInstanceTickets(context)){
					Toast.makeText(context, context.getResources().getString(R.string.too_much_passenger), Toast.LENGTH_SHORT).show();
				}
				else{
					WebApi webApi = new WebApi(context);
					webApi.setOnHttpResponse(onHttpResponsePay);
					webApi.commitOrder(PrefProxy.getMyOrder(context));
					clickPayStamp = System.currentTimeMillis();
					handler.sendEmptyMessage(HANDLER_MSG_WAITING);
				}
			}
			
		}
		
	};
	
	private OnHttpResponse onHttpResponsePay = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			if (WebApi.isRespSuccess(response)){
				Message msg = new Message();
				msg.what = HANDLER_MSG_ORDER_SUCCESS;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_ORDER_FAILED;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
			handler.sendEmptyMessage(HANDLER_MSG_RUNNING);
			
		}
		
	};

	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch(msg.what){
			case HANDLER_MSG_ORDER_SUCCESS:
				String toast = msg.getData().getString(BUNDLE_KEY_TOAST);
				Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
				setResult(Activity.RESULT_OK,null);
				finish();
				break;
				
			case HANDLER_MSG_ORDER_FAILED:
				toast = msg.getData().getString(BUNDLE_KEY_TOAST);
				Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
				
				break;
				
			case HANDLER_MSG_WAITING:
				progressBar.setVisibility(View.VISIBLE);
				waiting = true;
				
				break;
			
			case HANDLER_MSG_RUNNING:
				progressBar.setVisibility(View.INVISIBLE);
				waiting = false;
			}
			super.handleMessage(msg);
			
		}
		
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.booking_order);
		
		tvHubName =  (TextView)findViewById(R.id.textViewHub);
		tvHubName.setText(PrefProxy.getMyInstanceHubName(context));
		
		tvLineType =  (TextView)findViewById(R.id.textViewLineType);
		tvLineType.setText(PrefProxy.getMyInstanceLineType(context));
		
		tvDepartureTime =  (TextView)findViewById(R.id.textViewTimeDeparture);
		tvDepartureTime.setText(PrefProxy.getMyInstanceDepartureTime(context));
		
		tvArriveTime =  (TextView)findViewById(R.id.textViewTimeArrive);
		tvArriveTime.setText(PrefProxy.getMyInstanceArriveTime(context));
		
		tvPrice =  (TextView)findViewById(R.id.textViewPrice);
		tvPrice.setText(""+PrefProxy.getMyInstancePrice(context));
		
		((TextView)findViewById(R.id.textViewTicket)).setText(""+PrefProxy.getMyInstanceTickets(context));
		
		tvPriceTotal =  (TextView)findViewById(R.id.textViewPriceTotal);
		tvPriceTotal.setText(""+PrefProxy.getMyOrderPriceTotal(context));
		
		tvFrom =  (TextView)findViewById(R.id.textViewFrom);
		tvFrom.setText(PrefProxy.getMyInstanceFrom(context));
		
		tvTo =  (TextView)findViewById(R.id.textViewTo);
		tvTo.setText(PrefProxy.getMyInstanceTo(context));
		
		tvInstanceDate =  (TextView)findViewById(R.id.textViewDate);
		tvInstanceDate.setText(PrefProxy.getMyInstanceDate(context));
		
		
		lytPassenger = (LinearLayout)findViewById(R.id.layoutPassenger);
		lytPassenger.setOnClickListener(lsrPassenger);
		
		lytAccount = (LinearLayout)findViewById(R.id.layoutContact);
		lytAccount.setOnClickListener(lsrAccount);
	
		lytPay = (RelativeLayout)findViewById(R.id.layoutPay);
		lytPay.setOnClickListener(lsrPay);
		clickPayStamp = System.currentTimeMillis();
		
		tvAccount = (TextView)findViewById(R.id.textViewAccountATOrder);
		tvAccount.setText(PrefProxy.getAccount(context));
		tvUserName = (TextView)findViewById(R.id.textViewUserName);
		tvUserName.setText(PrefProxy.getUserName(context));
		
		tvPassengers = (TextView)findViewById(R.id.textViewPassengerInfo);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==Activity.RESULT_OK){
			switch(requestCode){
			case ACTIVITY_LOGIN:
				tvAccount.setText(PrefProxy.getAccount(context));
				tvUserName.setText(PrefProxy.getUserName(context));
				
				break;
				
			case ACTIVITY_PASSENGER_SELECT:
				
				String strPassengers = data.getStringExtra(ActivityPassengerSelect.EXTRA_PASSENGERS);
				
				tvPassengers.setText(strPassengers);
				
				tvPriceTotal.setText(""+PrefProxy.getMyOrderPriceTotal(context));
				if(PrefProxy.getMyOrderPriceTotal(context) > 
					PrefProxy.getMyInstancePrice(context)*PrefProxy.getMyInstanceTickets(context)){
					Toast.makeText(context, context.getResources().getString(R.string.too_much_passenger), Toast.LENGTH_SHORT).show();
				}
				
				break;
			}
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}
	
}
