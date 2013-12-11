/**
 * 
 */
package com.qshuttle.passenger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityMyOrder extends Activity{
	
	private static final int HANDLER_MSG_SUCCESS	= 1;
	private static final int HANDLER_MSG_FAILED		= 1 + HANDLER_MSG_SUCCESS;
	private static final int HANDLER_MSG_NULL		= 1 + HANDLER_MSG_FAILED;
	
	private static final String BUNDLE_KEY_TOAST	= "toast";
	
	private Context context;
	
	private ProgressBar  progressBar;
	private ListView listView;
	private ListAdapterMyOrder listAdapter;
	private JSONArray arrayOrders;

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
				listAdapter = new ListAdapterMyOrder(context, arrayOrders);
				listView.setAdapter(listAdapter);
				listView.setOnItemClickListener(lsrItemClick);
				listView.invalidate();
				break;
				
			case HANDLER_MSG_FAILED:
				progressBar.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.INVISIBLE);
				
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				
				break;

			case HANDLER_MSG_NULL:
				progressBar.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.INVISIBLE);
				
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				
				break;
			}
			
			super.handleMessage(msg);
		}
		
		
	};
	
	
	private OnHttpResponse onHttpResponseQuery = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				
						
				Message msg = new Message();
				
				arrayOrders = WebApi.getRespArray(response);

				
				if(arrayOrders!=null){
					msg.what = HANDLER_MSG_SUCCESS;
					Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
					msg.setData(bundle);
				}
				else{
					msg.what = HANDLER_MSG_NULL;
					Bundle bundle = new Bundle();
					bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
					msg.setData(bundle);
				}
	
				handler.sendMessage(msg);

			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_FAILED;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseQueryOrderDetail = new OnHttpResponse(){

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			if (WebApi.isRespSuccess(response)){
				PrefProxy.setMyOrder(context, response);
				
				Intent intent = new Intent(context,ActivityOrderChange.class);
				startActivityForResult(intent,0);
				
				/*
				JSONObject order = new JSONObject();
				
				try{
					JSONArray array = WebApi.getRespArray(response);
					
					order.put(WebApi.API_PARAM_ACCOUNT, PrefProxy.getAccount(context));
					order.put(WebApi.API_RESP_INSTANCE_SERIAL, PrefProxy.getMyInstanceSeriale(context));
					order.put(WebApi.API_PARAM_ORDER_PRICE, PrefProxy.getMyInstancePrice(context)*i);
					order.put(WebApi.API_RESP_ARRAY, array);
					
					PrefProxy.setMyOrder(context, order.toString());
					
				
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				*/

			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_FAILED;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
				
		}
		
	};
	
	
	
	private OnItemClickListener lsrItemClick = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			
			WebApi webapi = new WebApi(context);
			webapi.setOnHttpResponse(onHttpResponseQueryOrderDetail);
			webapi.queryOrderDetail((int)listAdapter.getItemId(position));
			
	}
		
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.context = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myorder_list);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		listView = (ListView)findViewById(R.id.listView);
		WebApi webApi = new WebApi(context);
		webApi.setOnHttpResponse(onHttpResponseQuery);
		long dueStamp = 1000;
		dueStamp *= 60 * 60 * 24 * 90;
		/*
		Log.i(this.getClass().getName(), ""+dueStamp);
		dueStamp *= 60;
		Log.i(this.getClass().getName(), ""+dueStamp);
		dueStamp *=60;
		Log.i(this.getClass().getName(), ""+dueStamp);
		dueStamp *= 24;
		Log.i(this.getClass().getName(), ""+dueStamp);
		dueStamp *= 90;
		Log.i(this.getClass().getName(), ""+dueStamp);
		*/
		String dueDate = StampToDate(System.currentTimeMillis()-dueStamp);
		//Log.i(this.getClass().getName(), dueDate);
		//Log.i(this.getClass().getName(), ""+System.currentTimeMillis());
		//Log.i(this.getClass().getName(), ""+(System.currentTimeMillis()-dueStamp));
		
		webApi.queryMyOrders(PrefProxy.getAccount(context),dueDate);

		progressBar.setVisibility(View.VISIBLE);
		
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
	
	public static String StampToDate(long stamp){
		
		return StampToString(stamp).substring(0, "yyyy-mm-dd".length());
		
    }
	

	

}
