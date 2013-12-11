/**
 * 
 */
package com.qshuttle.passenger;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author wangpeifeng
 *
 */
public class ActivityTicketQuery extends Activity {
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
	private ListAdapterInstanceSelect listAdapter;
	private JSONArray arrayInstance;
	private JSONObject instance;
//    private int selectedHub = 1;
    
	private int 	hub_serial,area_serial;
	private String 	hub_name,area_name,line_type, from, to;
	
	private Date date;
	private DatePicker datePicker;
	private String  instance_date;
	private Calendar calendar;
	
	

	/////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	private static final int HANDLER_MSG_SUCCESS	= 1;
	private static final int HANDLER_MSG_FAILED		= 1 + HANDLER_MSG_SUCCESS;
	private static final int HANDLER_MSG_NULL		= 1 + HANDLER_MSG_FAILED;
	
	private static final String BUNDLE_KEY_TOAST	= "toast";
	
	
	
	private static final int ACTIVITY_ORDER_INPUT		= 1;
	private static final int ACTIVITY_LOGIN				= 1 + ACTIVITY_ORDER_INPUT;
	
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
		
		context = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.booking_ticket);
		tvDate = (TextView)findViewById(R.id.textViewDate);
		
		Intent intent = getIntent();
		hub_name = intent.getStringExtra(ActivityLineQuery.KEY_HUB_NAME);
		hub_serial = intent.getIntExtra(ActivityLineQuery.KEY_HUB_SERIAL, ActivityLineQuery.VAL_DEF_HUB_SERIAL);
		
		area_name = intent.getStringExtra(ActivityLineQuery.KEY_AREA_NAME);
		area_serial = intent.getIntExtra(ActivityLineQuery.KEY_AREA_SERIAL, ActivityLineQuery.VAL_DEF_HUB_SERIAL);
		
		line_type = intent.getStringExtra(ActivityLineQuery.KEY_LINE_TYPE);
		
		date = new Date();
		date.setTime(System.currentTimeMillis()+1000*60*60*24);
		
		calendar = Calendar.getInstance();
		
		this.setCalendar(intent.getIntExtra(ActivityLineQuery.KEY_YEAR, date.getYear()+1900)
				, intent.getIntExtra(ActivityLineQuery.KEY_MONTH, date.getMonth())
				, intent.getIntExtra(ActivityLineQuery.KEY_DATE, date.getDate()));
		
		
		this.setInstanceDate(calendar);
		
		
		if(WebApi.API_VAL_LINE_TYPE_TO.equals(line_type)){
			from = area_name;
			to = hub_name;
		}
		else{
			from = hub_name;
			to = area_name;
		}
		((TextView)findViewById(R.id.textViewFrom)).setText(from);
		((TextView)findViewById(R.id.textViewTo)).setText(to);
		
		lytBack = (RelativeLayout)findViewById(R.id.layoutBack);
		lytBack.setOnClickListener(lsrLayout);
		
		lytForward = (RelativeLayout)findViewById(R.id.layoutForward);
		lytForward.setOnClickListener(lsrLayout);
		
		lytDate = (RelativeLayout)findViewById(R.id.layoutDate);
		lytDate.setOnClickListener(lsrLayout);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		listView = (ListView)findViewById(R.id.listView);
		
		PrefProxy.setMyInstance(context, "");
		this.queryInstance();
				
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
				
			case ActivityLineQuery.ACTIVITY_DATE_SELECT:
				setCalendar(data.getIntExtra(ActivityLineQuery.KEY_YEAR, calendar.get(Calendar.YEAR)),
						data.getIntExtra(ActivityLineQuery.KEY_MONTH, calendar.get(Calendar.MONTH)), 
						data.getIntExtra(ActivityLineQuery.KEY_DATE, calendar.get(Calendar.DAY_OF_MONTH)));
				setInstanceDate(calendar);
				queryInstance();
				
				break;
			case ACTIVITY_LOGIN:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityOrderInput.class);
				intent.putExtra(ActivityLineQuery.KEY_DATE, calendar.get(Calendar.DAY_OF_MONTH));
				intent.putExtra(ActivityLineQuery.KEY_MONTH, calendar.get(Calendar.MONTH));
				intent.putExtra(ActivityLineQuery.KEY_YEAR, calendar.get(Calendar.YEAR));
				startActivityForResult(intent,ACTIVITY_ORDER_INPUT);
				
				break;
				
			case ACTIVITY_ORDER_INPUT:
				
				this.queryInstance();
				
				break;
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}


	
	private OnHttpResponse onHttpResponseQuery = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				
						
				Message msg = new Message();
				
				arrayInstance = WebApi.getRespArray(response);

				
				if(arrayInstance!=null){
					msg.what = HANDLER_MSG_SUCCESS;
				}
				else{
					msg.what = HANDLER_MSG_NULL;
					Bundle bundle = new Bundle();
					//bundle.putString(BUNDLE_KEY_TOAST, WebApi.getRespMsg(response));
					bundle.putString(BUNDLE_KEY_TOAST, "Area Null!");
					msg.setData(bundle);
				}
				handler.sendMessage(msg);

			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_FAILED;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				//bundle.putString(BUNDLE_KEY_TOAST, "Query Failed!");
				msg.setData(bundle);
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
			case HANDLER_MSG_SUCCESS:
				progressBar.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
				
				//Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				listAdapter = new ListAdapterInstanceSelect(context, arrayInstance);
				listView.setAdapter(listAdapter);
				listView.setOnItemClickListener(lsrItemClick);
				listView.invalidate();
				break;
				
			case HANDLER_MSG_FAILED:
				progressBar.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.INVISIBLE);
				
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				//finish();
				break;

			case HANDLER_MSG_NULL:
				progressBar.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.INVISIBLE);
				
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				//finish();
				break;
			}
			
			super.handleMessage(msg);
		}
		
		
	};
	
	
	public OnItemClickListener lsrItemClick = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			try{
				
				JSONObject obj = arrayInstance.getJSONObject(position);
				instance = new JSONObject();
				instance.put(WebApi.API_PARAM_HUB_SERIAL, hub_serial);
				instance.put(WebApi.API_PARAM_LINE_TYPE, line_type);
				instance.put(WebApi.API_PARAM_AREA_SERIAL, area_serial);
				instance.put(WebApi.API_RESP_HUB_NAME, hub_name);
				instance.put(WebApi.API_RESP_AREA_NAME, area_name);
				instance.put(WebApi.API_PARAM_INSTANCE_DATE, instance_date);
				instance.put(WebApi.API_RESP_LINE_FROM, from);
				instance.put(WebApi.API_RESP_LINE_TO, to);
				instance.put(WebApi.API_RESP_TIME_DEPARTURE, obj.getString(WebApi.API_RESP_TIME_DEPARTURE));
				instance.put(WebApi.API_RESP_TIME_ARRIVE, obj.getString(WebApi.API_RESP_TIME_ARRIVE));
				instance.put(WebApi.API_RESP_INSTANCE_SERIAL, obj.getInt(WebApi.API_RESP_INSTANCE_SERIAL));
				instance.put(WebApi.API_RESP_PRICE, obj.getInt(WebApi.API_RESP_PRICE));
				instance.put(WebApi.API_RESP_SEATS, obj.getInt(WebApi.API_RESP_SEATS));
				instance.put(WebApi.API_RESP_TICKETS, obj.getInt(WebApi.API_RESP_TICKETS));
				
				PrefProxy.setMyInstance(context, instance.toString());
				
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseRegister);
				webApi.loginDevice(PrefProxy.getAccount(context), ActivityLogin.signMd5(PrefProxy.getPwd(context)));
				
			}
			catch(JSONException e){
				e.printStackTrace();
			}
							
		}



		
	};

	
	public OnClickListener lsrLayout = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.layoutBack:
				calendar.setTimeInMillis(calendar.getTimeInMillis()-1000*60*60*24);
				setInstanceDate(calendar);
				queryInstance();
				break;
				
			case R.id.layoutForward:
				calendar.setTimeInMillis(calendar.getTimeInMillis()+1000*60*60*24);
				setInstanceDate(calendar);
				queryInstance();
				
				break;
				
			case R.id.layoutDate:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityDateSelect.class);
				intent.putExtra(ActivityLineQuery.KEY_DATE, calendar.get(Calendar.DAY_OF_MONTH));
				intent.putExtra(ActivityLineQuery.KEY_MONTH, calendar.get(Calendar.MONTH));
				intent.putExtra(ActivityLineQuery.KEY_YEAR, calendar.get(Calendar.YEAR));
				startActivityForResult(intent,ActivityLineQuery.ACTIVITY_DATE_SELECT);
				break;
			}
		}
		
	};

	

	private void setCalendar(int year, int month, int day){

		calendar.set(year, month, day);
			
	};
	
	private void setInstanceDate(Calendar calendar){
		instance_date = calendar.get(Calendar.YEAR)+"-"
				+ ( calendar.get(Calendar.MONTH)+1)+"-"
				+  calendar.get(Calendar.DAY_OF_MONTH);
		tvDate.setText(instance_date);
				
	}
	
	private void queryInstance(){
		WebApi webApi = new WebApi(context);
		webApi.setOnHttpResponse(onHttpResponseQuery);
		webApi.queryTicket(hub_serial,line_type,area_serial,instance_date);

		progressBar.setVisibility(View.VISIBLE);
		
	}
	
	private OnHttpResponse onHttpResponseRegister = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityOrderInput.class);
				intent.putExtra(ActivityLineQuery.KEY_DATE, calendar.get(Calendar.DAY_OF_MONTH));
				intent.putExtra(ActivityLineQuery.KEY_MONTH, calendar.get(Calendar.MONTH));
				intent.putExtra(ActivityLineQuery.KEY_YEAR, calendar.get(Calendar.YEAR));
				startActivityForResult(intent,ACTIVITY_ORDER_INPUT);
			}
			else{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,ACTIVITY_LOGIN);
			}
		}
		
	};
	


}
