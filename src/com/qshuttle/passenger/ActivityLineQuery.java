/**
 * 
 */
package com.qshuttle.passenger;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author wangpeifeng
 *
 */
public class ActivityLineQuery extends Activity {
	
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
	
	private RadioGroup radioGroup;
	
	private Button 	btnHub,
					btnArea,
					btnDate,
					btnQuery;
	
	
	private int 	hub_serial,
					area_serial;
	
	private String 	hub_name,
					area_name;
	
	private String line_type;
	
	private Date date;
	private DatePicker datePicker;

	/////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	private static final int ACTIVITY_HUB_SELECT		= 1;
	public static final int ACTIVITY_AREA_SELECT		= 1 + ACTIVITY_HUB_SELECT;
	public static final int ACTIVITY_DATE_SELECT		= 1 + ACTIVITY_AREA_SELECT;
	private static final int ACTIVITY_TICKET_QUERY		= 1 + ACTIVITY_DATE_SELECT;
	
	/**
	 * Hub info
	 */
	public static final String KEY_HUB_NAME							= "hub_name";
	public static final String KEY_HUB_SERIAL						= "hub_serial";
	public static final String VAL_DEF_HUB_NAME						= "";
	public static final int VAL_DEF_HUB_SERIAL						= 0;

	/**
	 * Area info
	 */
	public static final String KEY_AREA_NAME						= "area_name";
	public static final String KEY_AREA_SERIAL						= "area_serial";
	public static final String VAL_DEF_AREA_NAME					= "";
	public static final int VAL_DEF_AREA_SERIAL						= 0;

	/**
	 * Line Type
	 */
	public static final String VAL_LINE_TYPE_TO						= "送站";
	public static final String VAL_LINE_TYPE_FROM					= "接站";
	public static final String KEY_LINE_TYPE						= "line_type";
	
	/**
	 * date info
	 */
	public static final String KEY_DATE								= "date";
	public static final String KEY_MONTH							= "month";
	public static final String KEY_YEAR								= "year";
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.booking_line);
		
		findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
		
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(lsrRadioGroup);
		radioGroup.check(R.id.radioTo);
		line_type = VAL_LINE_TYPE_TO;
		
		btnHub = (Button)findViewById(R.id.buttonHub);
		btnHub.setOnClickListener(lsrButton);
		setHub("",0);
		
		btnArea = (Button)findViewById(R.id.buttonArea);
		btnArea.setOnClickListener(lsrButton);
		setArea("",0);
		
		btnDate = (Button)findViewById(R.id.buttonDate);
		btnDate.setOnClickListener(lsrButton);
		
		date = new Date();
		date.setTime(System.currentTimeMillis()+1000*60*60*24);
		datePicker = new DatePicker(context);
		datePicker.updateDate(date.getYear()+1900, date.getMonth(), date.getDate());
		setDate(datePicker);
		btnQuery = (Button)findViewById(R.id.buttonQuery);
		btnQuery.setOnClickListener(lsrButton);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case ACTIVITY_HUB_SELECT:
				setHub( data.getStringExtra(KEY_HUB_NAME),
						data.getIntExtra(KEY_HUB_SERIAL, VAL_DEF_HUB_SERIAL));
				setArea( data.getStringExtra(KEY_AREA_NAME),
						data.getIntExtra(KEY_AREA_SERIAL, VAL_DEF_AREA_SERIAL));
				break;
				
			case ACTIVITY_AREA_SELECT:
				setArea( data.getStringExtra(KEY_AREA_NAME),
						data.getIntExtra(KEY_AREA_SERIAL, VAL_DEF_AREA_SERIAL));
				
				break;
				
			case ACTIVITY_DATE_SELECT:
				datePicker.updateDate(data.getIntExtra(KEY_YEAR, datePicker.getYear()),
						data.getIntExtra(KEY_MONTH, datePicker.getMonth()), 
						data.getIntExtra(KEY_DATE, datePicker.getDayOfMonth()));
				setDate(datePicker);
				
				break;
				
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}



	public OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonHub:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityHubSelect.class);
				intent.putExtra(KEY_HUB_NAME, hub_name);
				intent.putExtra(KEY_HUB_SERIAL, hub_serial);
				intent.putExtra(KEY_AREA_NAME, area_name);
				intent.putExtra(KEY_AREA_SERIAL, area_serial);
				intent.putExtra(KEY_LINE_TYPE, line_type);
				startActivityForResult(intent,ACTIVITY_HUB_SELECT);
				
				break;
				
			case R.id.buttonArea:
				
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityAreaSelect.class);
				intent.putExtra(KEY_HUB_NAME, hub_name);
				intent.putExtra(KEY_HUB_SERIAL, hub_serial);
				intent.putExtra(KEY_AREA_NAME, area_name);
				intent.putExtra(KEY_AREA_SERIAL, area_serial);
				intent.putExtra(KEY_LINE_TYPE, line_type);
				startActivityForResult(intent,ACTIVITY_AREA_SELECT);
				
				break;
				
			case R.id.buttonDate:
				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityDateSelect.class);
				intent.putExtra(KEY_DATE, datePicker.getDayOfMonth());
				intent.putExtra(KEY_MONTH, datePicker.getMonth());
				intent.putExtra(KEY_YEAR, datePicker.getYear());
				startActivityForResult(intent,ACTIVITY_DATE_SELECT);
					
				break;
				
			case R.id.buttonQuery:

				intent = new Intent();
				intent.setClass(getApplicationContext(), ActivityTicketQuery.class);
				intent.putExtra(KEY_HUB_NAME, hub_name);
				intent.putExtra(KEY_HUB_SERIAL, hub_serial);
				intent.putExtra(KEY_AREA_NAME, area_name);
				intent.putExtra(KEY_AREA_SERIAL, area_serial);
				intent.putExtra(KEY_LINE_TYPE, line_type);
				intent.putExtra(KEY_DATE, datePicker.getDayOfMonth());
				intent.putExtra(KEY_MONTH, datePicker.getMonth());
				intent.putExtra(KEY_YEAR, datePicker.getYear());
				startActivityForResult(intent,ACTIVITY_TICKET_QUERY);
				
				break;
			}
		}
		
	};
	
	
	public OnCheckedChangeListener lsrRadioGroup = new OnCheckedChangeListener(){

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.radioFrom:
				line_type = VAL_LINE_TYPE_FROM;
				break;
				
			case R.id.radioTo:
				line_type = VAL_LINE_TYPE_TO;
				break;
			}
		}
		
	};
	
	
	private void setHub(String name, int serial){
		
		btnHub.setText(name);
		PrefProxy.putString(context,KEY_HUB_NAME , name);
		hub_name = name;
		
		PrefProxy.putInt(context, KEY_HUB_SERIAL, serial);
		hub_serial = serial;
		
	}
	
	private void setArea(String name, int serial){
		btnArea.setText(name);
		PrefProxy.putString(context, KEY_AREA_NAME, name);
		area_name = name;
		
		PrefProxy.putInt(context, KEY_AREA_SERIAL, serial);
		area_serial = serial;
	}
	
	private void setDate(DatePicker datePicker){
		btnDate.setText(datePicker.getYear()+"年"
					+ (datePicker.getMonth()+1)+"月"
					+ datePicker.getDayOfMonth()+"日");
	}
}
