/**
 * 
 */
package com.qshuttle.passenger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityResetPwd extends Activity{
	
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
	
	private EditText 		etAccount,
							etPwd;
	
	private String 			strAccount, strPassword;
	
	private Button			btnLogin,
							btnRegister,
							btnPwd;
    
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	private static final int HANDLER_MSG_TOAST							= 1;
	//private static final int HANDLER_MSG_LOGIN_FAILED					= 1;
	
	
	private static final String BUNDLE_KEY_TOAST						= "toast";
	
	private static final String BUNDLE_DEFAULT_TOAST					= "Login Error!";
	
	private ProgressBar progressBar;
	
    /////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////
	
    /////////////////////////////////////////////////
    // METHODS, CREATE
    /////////////////////////////////////////////////
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_resetpwd);
		
		etAccount = (EditText)findViewById(R.id.editTextAccount);
		etAccount.setText(PrefProxy.getAccount(context));
		etPwd = (EditText)findViewById(R.id.editTextPwd);
		etPwd.setText(PrefProxy.getPwd(context));
		
		//((Button)findViewById(R.id.buttonLogin)).setOnClickListener(lsrButton);
		//((Button)findViewById(R.id.buttonRegister)).setOnClickListener(lsrButton);
		((Button)findViewById(R.id.buttonReset)).setOnClickListener(lsrButton);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
	}

	
    /////////////////////////////////////////////////
    // METHODS, ACTION
    /////////////////////////////////////////////////
	
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
	
	private OnClickListener lsrButton = new OnClickListener()
	{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonLogin:
				String account = etAccount.getText().toString();
				String pwd = etPwd.getText().toString();
				progressBar.setVisibility(View.VISIBLE);
				WebApi webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseLogin);
				webApi.loginDevice(account, signMd5(pwd));
				strAccount = account;
				strPassword = pwd;
				break;
//				
			case R.id.buttonRegister:
				account = etAccount.getText().toString();
				pwd = etPwd.getText().toString();
				progressBar.setVisibility(View.VISIBLE);
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseRegister);
				webApi.registerUser(account, signMd5(pwd));
				strAccount = account;
				strPassword = pwd;
				
				break;
			case R.id.buttonReset:
				account = etAccount.getText().toString();
				progressBar.setVisibility(View.VISIBLE);
				webApi = new WebApi(context);
				webApi.setOnHttpResponse(onHttpResponseGetBackPwd);
				webApi.getbackPwd(account);
				break;
			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseLogin = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				
				try{
					JSONObject obj = new JSONObject(response);
					obj.getString(PrefProxy.KEY_LOGIN_USER_NAME);
					
					PrefProxy.setAccount(context, strAccount);
					PrefProxy.setPwd(context, strPassword);
					}
				catch(Exception e){
					e.printStackTrace();
				}
				/*
				Intent intent = new Intent();
				//intent.setClass(getApplicationContext(), ApkToolsActivity.class);
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivityForResult(intent,0);
				*/
				setResult(Activity.RESULT_OK,null);
				finish();


			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_TOAST;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseRegister = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				/*
				Intent intent = new Intent();
				//intent.setClass(getApplicationContext(), ApkToolsActivity.class);
				intent.setClass(getApplicationContext(), ActivityTabMain.class);
				startActivityForResult(intent,0);
				*/
				PrefProxy.setAccount(context, strAccount);
				PrefProxy.setPwd(context, strPassword);

				setResult(Activity.RESULT_OK,null);
				finish();


			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_TOAST;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
		
	};
	
	private OnHttpResponse onHttpResponseGetBackPwd = new OnHttpResponse()
	{

		@Override
		public void doHttpResponse(String response) {
			// TODO Auto-generated method stub
			
			if (WebApi.isRespSuccess(response)){
				/*
				Intent intent = new Intent();
				//intent.setClass(getApplicationContext(), ApkToolsActivity.class);
				intent.setClass(getApplicationContext(), ActivityTabMain.class);
				startActivityForResult(intent,0);
				*/
				//setResult(Activity.RESULT_CANCELED,null);
				//finish();
				Message msg = new Message();
				msg.what = HANDLER_MSG_TOAST;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
			else{
				Message msg = new Message();
				msg.what = HANDLER_MSG_TOAST;
				Bundle bundle = new Bundle();
				bundle.putString(BUNDLE_KEY_TOAST, new WebApi(context).getRespMsg(response));
				msg.setData(bundle);
				handler.sendMessage(msg);

			}
		}
		
	};	

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case HANDLER_MSG_TOAST:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				break;
			}
			super.handleMessage(msg);
		}
		
		
	};
	
	
	public static String signMd5(String strParams){
		
		byte[] bytes = null;
		
		try {
			//bytes = (new String("432229e00595840cb6d5f80d7b72f359appkey2012032298formatxmlsign_methodmd5sourceapi.tool.vancl.comt20120322111810ttidapi.tool.vancl.com2012032298apitool_wap_2.0uidA9FFDE3559430A7B1E23A03F24FCDA05ver1.0")).getBytes("utf-8");
			//bytes = (new String("432229e00595840cb6d5f80d7b72f359appkey2012032298formatxmlsign_methodmd5sourceapi.tool.vancl.comt20120322111810ttidvob_shsm_android_001@2012032298@iSopping_android_1.0.0_namouid2012032298ver1.0")).getBytes("utf-8");
		
			bytes = strParams.getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
		try {
                    MessageDigest algorithm = MessageDigest.getInstance("MD5");
                    algorithm.reset();
                    algorithm.update(bytes);
                    return toHexString(algorithm.digest(), "");
    
		} catch (NoSuchAlgorithmException e) {
                    Log.v("he--------------------------------ji", "toMd5(): " + e);
                    throw new RuntimeException(e);
                    // 05-20 09:42:13.697: ERROR/hjhjh(256):
                    // 5d5c87e61211ab7a4847f7408f48ac
    
		}
        	
	}
	
	
	private static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
           // editTextResponse.append(Integer.toString(0xFF & b)+" ");  
           // editTextResponse.append(Integer.toHexString(0xFF & b)+" | ");  
            String hex = Integer.toHexString(0xFF & b);
            if(hex.length()==1)
            	hex = "0"+hex;
        	hexString.append(hex).append(separator);
        }
        return hexString.toString();
	}


}
