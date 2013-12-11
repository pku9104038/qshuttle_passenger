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
import android.widget.EditText;

/**
 * @author wangpeifeng
 *
 */
public class ActivityDateSelect extends Activity {
	
	/////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
	private Context 	context;
	
	private Button		btnSet,
						btnDate;

	private DatePicker 	datePicker;
	
	private Date date;
	/////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.booking_date);
		
		Intent intent = getIntent();
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		
		datePicker = (DatePicker)findViewById(R.id.datePicker);
		datePicker.updateDate(intent.getIntExtra(ActivityLineQuery.KEY_YEAR, date.getYear()+1900)
					, intent.getIntExtra(ActivityLineQuery.KEY_MONTH, date.getMonth())
					, intent.getIntExtra(ActivityLineQuery.KEY_DATE, date.getDate()));

		btnDate = (Button)findViewById(R.id.buttonDate);
		setDate(datePicker);
		//datePicker.setMinDate(System.currentTimeMillis());
		//datePicker.setMaxDate(System.currentTimeMillis()+1000*60*60*24*15);
		btnSet = (Button)findViewById(R.id.buttonSet);
		btnSet.setOnClickListener(lsrButton);
		
		
	}
	
	private OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.buttonSet:
				setDate(datePicker);
				Intent intent = new Intent();
				intent.putExtra(ActivityLineQuery.KEY_YEAR,datePicker.getYear());
				intent.putExtra(ActivityLineQuery.KEY_MONTH, datePicker.getMonth());
				intent.putExtra(ActivityLineQuery.KEY_DATE, datePicker.getDayOfMonth());
				setResult(Activity.RESULT_OK, intent);
				
				finish();
				
				break;
			}
		}
		
	};

	private void setDate(DatePicker datePicker){
		btnDate.setText(datePicker.getYear()+"年"
					+ (datePicker.getMonth()+1)+"月"
					+ datePicker.getDayOfMonth()+"日");
	}
}
