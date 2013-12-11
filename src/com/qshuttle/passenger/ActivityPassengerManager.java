/**
 * 
 */
package com.qshuttle.passenger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author wangpeifeng
 *
 */
public class ActivityPassengerManager extends Activity{

	/////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	private static final int HANDLER_MSG_SUCCESS		= 1;
	private static final int HANDLER_MSG_FAILED			= 1 + HANDLER_MSG_SUCCESS;
	private static final int HANDLER_MSG_NULL			= 1 + HANDLER_MSG_FAILED;
	private static final int HANDLER_MSG_REFRESH		= 1 + HANDLER_MSG_NULL;
	private static final int HANDLER_MSG_STOP_PROGRESS	= 1 + HANDLER_MSG_REFRESH;
	private static final int HANDLER_MSG_START_PROGRESS	= 1 + HANDLER_MSG_STOP_PROGRESS;
	private static final int HANDLER_TOAST				= 1 + HANDLER_MSG_START_PROGRESS;
	
	private static final String BUNDLE_KEY_TOAST	= "toast";
	
	private static final int ACTIVITY_PASSENGER_ADD			= 1;
	private static final int ACTIVITY_PASSENGER_EDIT		= 1 + ACTIVITY_PASSENGER_ADD;
	private static final int ACTIVITY_PASSENGER_DELETE		= 1 + ACTIVITY_PASSENGER_EDIT;
	
	public static final String EXTRA_PASSENGERS	= "extra_passengers";
	
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
	private RelativeLayout lytBack, lytForward,lytDate;
	private TextView tvDate;
	private ProgressBar progressBar;
	private ListView listView;
	private ListAdapterPassengerManager listAdapter;
	private JSONArray arrayInstance;
//    private int selectedHub = 1;
    
	private int 	hub_serial,area_serial;
	private String 	hub_name,area_name,line_type, from, to;
	
	private Date date;
	private DatePicker datePicker;
	private String  instance_date;
	private Calendar calendar;
	
	
	private ArrayList<PassengerInfos> lstPassenger;
	//private ListAdapterPassengerManager adapter;
	
	private int resultCode;
	private Intent intentResult;
	
	private JSONArray arrayPassengers;
	
	/*
	 * PROPERTIES,BEHAVIAR
	 */
	/**
	 * 
	 */
	public OnClickListener lsrPassengerAdd = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerAdd.class);
			startActivityForResult(intent,ACTIVITY_PASSENGER_ADD);
			
		}
		
	};

	
	private OnCheckedChangeListener lsrListItemCheckBox = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			lstPassenger.get(Integer.parseInt((String)(buttonView.getTag()))).setSelected(isChecked);
			handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
		}
		
	};
	
	
	private OnClickListener lsrListItemEdit = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt((String) v.getTag());
			PassengerInfos passenger = lstPassenger.get(position);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerEdit.class);
			intent.putExtra(ActivityPassengerEdit.EXTRA_SERIAL, passenger.getSerial());
			intent.putExtra(ActivityPassengerEdit.EXTRA_NAME, passenger.getName());
			intent.putExtra(ActivityPassengerEdit.EXTRA_PHONE, passenger.getPhone());
			intent.putExtra(ActivityPassengerEdit.EXTRA_ADDRESS, passenger.getAddressInfos().getAddress());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LATE6, passenger.getAddressInfos().getLatE6());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LONGE6, passenger.getAddressInfos().getLongE6());
			
			startActivityForResult(intent,ACTIVITY_PASSENGER_EDIT);
			
			
		}
		
	};

	private OnClickListener lsrListItemDelete = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt((String) v.getTag());
			PassengerInfos passenger = lstPassenger.get(position);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerDelete.class);
			intent.putExtra(ActivityPassengerEdit.EXTRA_SERIAL, passenger.getSerial());
			intent.putExtra(ActivityPassengerEdit.EXTRA_NAME, passenger.getName());
			intent.putExtra(ActivityPassengerEdit.EXTRA_PHONE, passenger.getPhone());
			intent.putExtra(ActivityPassengerEdit.EXTRA_ADDRESS, passenger.getAddressInfos().getAddress());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LATE6, passenger.getAddressInfos().getLatE6());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LONGE6, passenger.getAddressInfos().getLongE6());
			
			startActivityForResult(intent,ACTIVITY_PASSENGER_DELETE);
			
		}
		
	};
	
	private OnClickListener lsrButton = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.imageButtonBack:
				
				break;
				
			}
			
			
		}
		
	};
	
	private OnHttpResponse onHttpResponseQuery = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(HANDLER_MSG_STOP_PROGRESS);
			if (WebApi.isRespSuccess(response)){
				
				
				Message msg = new Message();
				
				arrayPassengers = WebApi.getRespArray(response);
				if(arrayPassengers!=null){
					lstPassenger.removeAll(lstPassenger);
					
					for(int i = 0; i<arrayPassengers.length(); i++){
						PassengerInfos passenger = new PassengerInfos();
						try{
							JSONObject obj = arrayPassengers.getJSONObject(i);
							passenger.setSerial(obj.getInt(WebApi.API_RESP_ADDRESS_SERIAL));
							passenger.setName(obj.getString(WebApi.API_RESP_PASSENGER_NAME));
							passenger.setPhone(obj.getString(WebApi.API_RESP_PASSENGER_PHONE));
							passenger.setRecordDesc(obj.getString(WebApi.API_RESP_RECORD_DESC));
							passenger.getAddressInfos().setAddress(obj.getString(WebApi.API_RESP_ADDRESS_INFO));
							passenger.getAddressInfos().setPosition(obj.getInt(WebApi.API_RESP_ADDRESS_LATE6),
									obj.getInt(WebApi.API_RESP_ADDRESS_LONGE6));
							passenger.setSelected(false);
							
							lstPassenger.add(passenger);
							
						}
						catch(JSONException e){
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(HANDLER_MSG_STOP_PROGRESS);
				}
				
				
			}
			
		}
		
	};
	

	private OnHttpResponse onHttpResponseAddPassenger = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
			if (WebApi.isRespSuccess(response)){
				
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new  WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				msg.what = HANDLER_TOAST;
				handler.sendMessage(msg);
				
			}
			
		}
		
	};

	private OnHttpResponse onHttpResponseDeletePassenger = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
			if (WebApi.isRespSuccess(response)){
				
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new  WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				msg.what = HANDLER_TOAST;
				handler.sendMessage(msg);
				
				
			}
			
		}
		
	};

	private OnHttpResponse onHttpResponseEditPassenger = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(HANDLER_MSG_REFRESH);
			if (WebApi.isRespSuccess(response)){
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new  WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				msg.what = HANDLER_TOAST;
				handler.sendMessage(msg);
				
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
			switch(msg.what){
			case HANDLER_MSG_REFRESH:
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseQuery);
				webApi.queryPassengers(PrefProxy.getAccount(context));
				
				progressBar.setVisibility(View.VISIBLE);
				break;
				
			case HANDLER_MSG_STOP_PROGRESS:
				progressBar.setVisibility(View.INVISIBLE);
				//listAdapter = new ListAdapter
				listAdapter.notifyDataSetChanged();
				listView.invalidate();
				break;
			
			case HANDLER_MSG_START_PROGRESS:
				progressBar.setVisibility(View.VISIBLE);
								
				break;
				
			case HANDLER_TOAST:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				break;

			}
			
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
        setContentView(R.layout.booking_passenger_select);
				
		
		((RelativeLayout)findViewById(R.id.layoutPassengerAdd)).setOnClickListener(lsrPassengerAdd);
		((Button)findViewById(R.id.buttonSelect)).setOnClickListener(lsrButton);
		
		listView = (ListView)findViewById(R.id.listViewPassengerSelect);
		
		lstPassenger = new ArrayList<PassengerInfos>();
		
		listAdapter = new ListAdapterPassengerManager(context, lstPassenger,this.lsrListItemDelete,this.lsrListItemEdit);
		
		listView.setAdapter(listAdapter);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		
		WebApi webApi = new WebApi(context);
		webApi.setOnHttpResponse(onHttpResponseQuery);
		webApi.queryPassengers(PrefProxy.getAccount(context));
		
		
				
	}




	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case ACTIVITY_PASSENGER_ADD:
				
				PassengerInfos passenger = new PassengerInfos(
						0,
						data.getStringExtra(ActivityPassengerAdd.EXTRA_NAME),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_PHONE),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_ADDRESS),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LATE6, 0),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LONGE6, 0),
						true
						);
				
				lstPassenger.add(passenger);
				
				
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseAddPassenger);
				webApi.addPassenger(PrefProxy.getAccount(context),passenger);
				
				
				break;
			case ACTIVITY_PASSENGER_EDIT:
				
				passenger = new PassengerInfos(
						data.getIntExtra(ActivityPassengerEdit.EXTRA_SERIAL, 0),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_NAME),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_PHONE),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_ADDRESS),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LATE6, 0),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LONGE6, 0),
						true
						);
				
				//lstPassenger.add(passenger);
				
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseEditPassenger);
				webApi.editPassenger(PrefProxy.getAccount(context),passenger);
				
				
				break;
			case ACTIVITY_PASSENGER_DELETE:
				
				passenger = new PassengerInfos(
						data.getIntExtra(ActivityPassengerEdit.EXTRA_SERIAL, 0),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_NAME),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_PHONE),
						data.getStringExtra(ActivityPassengerAdd.EXTRA_ADDRESS),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LATE6, 0),
						data.getIntExtra(ActivityPassengerAdd.EXTRA_LONGE6, 0),
						true
						);
				
				//lstPassenger.add(passenger);
				
				
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseDeletePassenger);
				webApi.deletePassenger(PrefProxy.getAccount(context),passenger);
				
				
				break;
			}
			handler.sendEmptyMessage(HANDLER_MSG_START_PROGRESS);
			
		}
		
				//super.onActivityResult(requestCode, resultCode, data);
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
