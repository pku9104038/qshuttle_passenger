/**
 * 
 */
package com.qshuttle.passenger;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityHubSelect extends Activity {
	
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
	private Button btnHub;
	private ProgressBar progressBar;
	private ListView listView;
	private ListAdapterHubSelect listAdapter;
	private JSONArray arrayHubs;
//    private int selectedHub = 1;
    
	private int 	hub_serial,area_serial;
	private String 	hub_name,area_name,line_type;
	

	/////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private static final int HANDLER_MSG_SUCCESS	= 1;
	private static final int HANDLER_MSG_FAILED		= 1 + HANDLER_MSG_SUCCESS;
	private static final int HANDLER_MSG_NULL		= 1 + HANDLER_MSG_FAILED;
	
	private static final String BUNDLE_KEY_TOAST	= "toast";
	
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		
		Intent intent = getIntent();
		hub_name = intent.getStringExtra(ActivityLineQuery.KEY_HUB_NAME);
		hub_serial = intent.getIntExtra(ActivityLineQuery.KEY_HUB_SERIAL, ActivityLineQuery.VAL_DEF_HUB_SERIAL);

		area_name = intent.getStringExtra(ActivityLineQuery.KEY_AREA_NAME);
		area_serial = intent.getIntExtra(ActivityLineQuery.KEY_AREA_SERIAL, ActivityLineQuery.VAL_DEF_HUB_SERIAL);
		
		line_type = intent.getStringExtra(ActivityLineQuery.KEY_LINE_TYPE);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.booking_hub);
		
		btnHub = (Button)findViewById(R.id.buttonHub);
		setHub(hub_name,hub_serial);
		
		((Button)findViewById(R.id.buttonSet)).setOnClickListener(lsrButton);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		listView = (ListView)findViewById(R.id.listView);
		
		WebApi webApi = new WebApi(context);
		webApi.setOnHttpResponse(onHttpResponseQuery);
		webApi.queryHub();
		progressBar.setVisibility(View.VISIBLE);
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
				
			case ActivityLineQuery.ACTIVITY_AREA_SELECT:
				area_name = data.getStringExtra(ActivityLineQuery.KEY_AREA_NAME);
				area_serial = data.getIntExtra(ActivityLineQuery.KEY_AREA_SERIAL, ActivityLineQuery.VAL_DEF_AREA_SERIAL);

				Intent intent = new Intent();
				intent.putExtra(ActivityLineQuery.KEY_HUB_NAME, hub_name);
				intent.putExtra(ActivityLineQuery.KEY_HUB_SERIAL,hub_serial);
				intent.putExtra(ActivityLineQuery.KEY_AREA_NAME, area_name);
				intent.putExtra(ActivityLineQuery.KEY_AREA_SERIAL,area_serial);
				setResult(Activity.RESULT_OK, intent);
				finish();

				
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
				
				arrayHubs = WebApi.getRespArray(response);
				if(arrayHubs!=null){
					msg.what = HANDLER_MSG_SUCCESS;
					
				}
				else{
					msg.what = HANDLER_MSG_NULL;
					Bundle bundle = new Bundle();
					//bundle.putString(BUNDLE_KEY_TOAST, WebApi.getRespMsg(response));
					bundle.putString(BUNDLE_KEY_TOAST, "Hubs Null!");
					msg.setData(bundle);
				}
				handler.sendMessage(msg);

			}
			else{
				
				Message msg = new Message();
				msg.what = HANDLER_MSG_FAILED;
				Bundle bundle = new Bundle();
				//bundle.putString(BUNDLE_KEY_TOAST, WebApi.getRespMsg(response));
				bundle.putString(BUNDLE_KEY_TOAST, "Query Failed!");
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
				//Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				listAdapter = new ListAdapterHubSelect(context, arrayHubs,hub_serial);
				listView.setAdapter(listAdapter);
				listView.setOnItemClickListener(lsrItemClick);
				break;
				
			case HANDLER_MSG_FAILED:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				finish();
				break;

			case HANDLER_MSG_NULL:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(context, msg.getData().getCharSequence(BUNDLE_KEY_TOAST), Toast.LENGTH_LONG).show();
				finish();
				break;
			}
			
			super.handleMessage(msg);
		}
		
		
	};
	
	
	public OnItemClickListener lsrItemClick = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			setHub(listAdapter.getItemName(position),(int)listAdapter.getItemId(position));
			listAdapter.setSelectedHub((int)listAdapter.getItemId(position));
			listAdapter.notifyDataSetChanged();
			listView.invalidate();
		}



		
	};

	public OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonSet:
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityAreaSelect.class);
				intent.putExtra(ActivityLineQuery.KEY_HUB_NAME, hub_name);
				intent.putExtra(ActivityLineQuery.KEY_HUB_SERIAL, hub_serial);
				intent.putExtra(ActivityLineQuery.KEY_AREA_NAME, area_name);
				intent.putExtra(ActivityLineQuery.KEY_AREA_SERIAL, area_serial);
				intent.putExtra(ActivityLineQuery.KEY_LINE_TYPE, line_type);
				startActivityForResult(intent, ActivityLineQuery.ACTIVITY_AREA_SELECT);
				
/*				
				
				Intent intent = new Intent();
				intent.putExtra(ActivityLineQuery.KEY_HUB_NAME, hub_name);
				intent.putExtra(ActivityLineQuery.KEY_HUB_SERIAL,hub_serial);
				setResult(Activity.RESULT_OK, intent);
				finish();
*/				
				break;
				
			}
			
		}
		
	};
	
	private void setHub(String name, int serial){
		
		btnHub.setText(name);
		PrefProxy.putString(context,ActivityLineQuery.KEY_HUB_NAME , name);
		hub_name = name;
		
		PrefProxy.putInt(context, ActivityLineQuery.KEY_HUB_SERIAL, serial);
		hub_serial = serial;
		
	}

}
