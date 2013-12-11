package com.qshuttle.passenger;

import java.util.Calendar;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {
	
	private static final int ACTIVITY_PASSENGER_LOGIN		= 1;
	private static final int ACTIVITY_ORDER_LOGIN			= 1 + ACTIVITY_PASSENGER_LOGIN;
	private static final int ACTIVITY_MYCAR_LOGIN			= 1 + ACTIVITY_ORDER_LOGIN;
	
	private static final int HANDLER_MSG_WAITING			= 1;
	private static final int HANDLER_MSG_RUNNING			= 1 + HANDLER_MSG_WAITING;
	
	private Context context;
	private ProgressBar progressBar;
	private boolean waiting = false;

	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_MSG_WAITING:
				progressBar.setVisibility(View.VISIBLE);
				waiting = true;
				break;
				
			case HANDLER_MSG_RUNNING:
				progressBar.setVisibility(View.GONE);
				waiting = false;
				break;
			}
		}
		
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        
        ((RelativeLayout)findViewById(R.id.layoutCall)).setOnClickListener(lsrClick);
        findViewById(R.id.imageViewBooking).setOnClickListener(lsrClick);
        findViewById(R.id.imageViewMyCar).setOnClickListener(lsrClick);
        findViewById(R.id.imageViewMyOrder).setOnClickListener(lsrClick);
        findViewById(R.id.imageViewMyPassenger).setOnClickListener(lsrClick);
        findViewById(R.id.imageViewLogin).setOnClickListener(lsrClick);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        if(!this.isDataOnline()){
        	Toast.makeText(context, getResources().getString(R.string.online_required), Toast.LENGTH_LONG).show();
        }
    }
    
    

    /* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		stopWaiting();
		if(resultCode==Activity.RESULT_OK){
			switch(requestCode){
			case ACTIVITY_PASSENGER_LOGIN:
				startActivityForResult(new Intent(context, ActivityPassengerManager.class),0);
				
				break;
				
			case ACTIVITY_ORDER_LOGIN:
				startActivityForResult(new Intent(context, ActivityMyOrder.class),0);
				
				break;
			case ACTIVITY_MYCAR_LOGIN:
				startActivityForResult(new Intent(context, ActivityMyCar.class),0);
				
				break;
			}
		}
		
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    
    /* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case R.id.itemHostSetting:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), HostSettingActivity.class);
			startActivityForResult(intent,0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			exitConfirm();
		  
		    
		}
			

		return super.onKeyDown(keyCode, event);
	}
	
	private void exitConfirm(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
		builder.setMessage(context.getResources().getString(R.string.exit_confirm))//+Data.APP_NAME+"？") 
				.setCancelable(false) 
				.setPositiveButton(context.getResources().getString(R.string.set), new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int id) { 
						Toast.makeText(context, context.getResources().getString(R.string.welcome_back), Toast.LENGTH_LONG).show();
						MainActivity.this.finish(); 
					} 
				}) 
				.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int id) { 
						dialog.cancel(); 
		            } 
		        }); 
		    
		builder.create().show(); 		
	}
	public OnClickListener lsrClick = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.layoutCall:
				startWaiting();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + "4008721333"));
				
				startActivityForResult(intent, 0);

				break;
				
			case R.id.imageViewBooking:
				startWaiting();
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLineQuery.class);
				startActivityForResult(intent,0);
				
				break;
				
			case R.id.imageViewMyOrder:
				/*
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,0);
				*/
				startWaiting();
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseMyOrder);
				webApi.loginDevice(PrefProxy.getAccount(context), ActivityLogin.signMd5(PrefProxy.getPwd(context)));
				
				break;
				
			case R.id.imageViewMyPassenger:
				/*
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,0);
				*/
				startWaiting();
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseMyPassenger);
				webApi.loginDevice(PrefProxy.getAccount(context), ActivityLogin.signMd5(PrefProxy.getPwd(context)));
				
				break;
				
			case R.id.imageViewMyCar:
				startWaiting();
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseMyCar);
				webApi.loginDevice(PrefProxy.getAccount(context), ActivityLogin.signMd5(PrefProxy.getPwd(context)));
				
				break; 
				
			case R.id.imageViewLogin:
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,ACTIVITY_PASSENGER_LOGIN);
				
				break;
			}
			
		}
    	
    };
   
	private OnHttpResponse onHttpResponseMyPassenger = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				startActivityForResult(new Intent(context, ActivityPassengerManager.class),0);
			}
			else{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,ACTIVITY_PASSENGER_LOGIN);
			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseMyOrder = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				startActivityForResult(new Intent(context, ActivityMyOrder.class),0);
			}
			else{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,ACTIVITY_ORDER_LOGIN);
			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseMyCar = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				startActivityForResult(new Intent(context, ActivityMyCar.class),0);
			}
			else{
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityLogin.class);
				startActivityForResult(intent,ACTIVITY_MYCAR_LOGIN);
			}
		}
		
	};
	
	private void startWaiting(){
		waiting = true;
		handler.sendEmptyMessage(HANDLER_MSG_WAITING);
	}
	
	private void stopWaiting(){
		waiting = false;
		handler.sendEmptyMessage(HANDLER_MSG_RUNNING);
	}
	
	
	public boolean isDataOnline(){
		boolean bool = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null){
			bool = info.isConnected();
		}
		return bool;
	}
}
