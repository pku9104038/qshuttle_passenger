/**
 * 
 */
package com.qshuttle.passenger;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ActivityOrderChange  extends Activity{
	
	
	private Context context;
	private ListView listView;
	private ListAdapterPassengerManager listAdapter;
	private ArrayList<PassengerInfos> lstTickets;
	private TextView tvHubName, tvLineType, tvInstanceDate,
	tvDepartureTime, tvArriveTime, tvPrice,
	tvPriceTotal, tvFrom,tvTo;

	
	
	private OnClickListener lsrListItemEdit = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt((String) v.getTag());
			PassengerInfos passenger = lstTickets.get(position);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerEdit.class);
			intent.putExtra(ActivityPassengerEdit.EXTRA_SERIAL, passenger.getSerial());
			intent.putExtra(ActivityPassengerEdit.EXTRA_NAME, passenger.getName());
			intent.putExtra(ActivityPassengerEdit.EXTRA_PHONE, passenger.getPhone());
			intent.putExtra(ActivityPassengerEdit.EXTRA_ADDRESS, passenger.getAddressInfos().getAddress());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LATE6, passenger.getAddressInfos().getLatE6());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LONGE6, passenger.getAddressInfos().getLongE6());
			
//			startActivityForResult(intent,ACTIVITY_PASSENGER_EDIT);
			
			
		}
		
	};

	private OnClickListener lsrListItemDelete = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt((String) v.getTag());
			PassengerInfos passenger = lstTickets.get(position);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ActivityPassengerDelete.class);
			intent.putExtra(ActivityPassengerEdit.EXTRA_SERIAL, passenger.getSerial());
			intent.putExtra(ActivityPassengerEdit.EXTRA_NAME, passenger.getName());
			intent.putExtra(ActivityPassengerEdit.EXTRA_PHONE, passenger.getPhone());
			intent.putExtra(ActivityPassengerEdit.EXTRA_ADDRESS, passenger.getAddressInfos().getAddress());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LATE6, passenger.getAddressInfos().getLatE6());
			intent.putExtra(ActivityPassengerEdit.EXTRA_LONGE6, passenger.getAddressInfos().getLongE6());
			
//			startActivityForResult(intent,ACTIVITY_PASSENGER_DELETE);
			
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
        setContentView(R.layout.myorder_edit);
		
		tvHubName =  (TextView)findViewById(R.id.textViewHub);
		tvHubName.setText(PrefProxy.getMyOrderHubName(context));
		
		tvLineType =  (TextView)findViewById(R.id.textViewLineType);
		String linetype = PrefProxy.getMyOrderLineType(context);
		tvLineType.setText(linetype);
		
		tvDepartureTime =  (TextView)findViewById(R.id.textViewTimeDeparture);
		tvDepartureTime.setText(PrefProxy.getMyOrderStartTime(context));
		
		tvArriveTime =  (TextView)findViewById(R.id.textViewTimeArrive);
		tvArriveTime.setText(PrefProxy.getMyOrderStopTime(context));
		
		
		tvPriceTotal =  (TextView)findViewById(R.id.textViewPriceTotal);
		tvPriceTotal.setText(""+PrefProxy.getMyOrderPriceTotal(context));
		String from,to;
		if(WebApi.API_VAL_LINE_TYPE_TO.equals(linetype)){
			from = PrefProxy.getMyOrderAreaName(context);
			to = PrefProxy.getMyOrderHubName(context);
					
		}
		else{
			from = PrefProxy.getMyOrderHubName(context);
			to = PrefProxy.getMyOrderAreaName(context);
		}
		
		tvFrom =  (TextView)findViewById(R.id.textViewFrom);
		tvFrom.setText(from);
		
		tvTo =  (TextView)findViewById(R.id.textViewTo);
		tvTo.setText(to);
		
		tvInstanceDate =  (TextView)findViewById(R.id.textViewDate);
		tvInstanceDate.setText(PrefProxy.getMyInstanceDate(context));
		
		
		
		listView = (ListView) findViewById(R.id.listViewTickets);
		lstTickets = getTicketsList();
		listAdapter = new ListAdapterPassengerManager(context, lstTickets,this.lsrListItemDelete,this.lsrListItemEdit);
		listView.setAdapter(listAdapter);
		
	}
	
	
	private ArrayList<PassengerInfos> getTicketsList(){
		ArrayList<PassengerInfos> lstTickets = new ArrayList<PassengerInfos>();
		try{
			Log.i(this.getClass().getName(), PrefProxy.getMyOrder(context));
			JSONObject order = new JSONObject(PrefProxy.getMyOrder(context));
			JSONArray array =order.getJSONArray(WebApi.API_RESP_ARRAY);
			for(int i=0; i<array.length(); i++){
				JSONObject ticket = array.getJSONObject(i);
				PassengerInfos passenger = new PassengerInfos();
				passenger.setName(ticket.getString(WebApi.API_RESP_TICKET_PASSENGER_NAME));
				passenger.setPhone(ticket.getString(WebApi.API_RESP_TICKET_PASSENGER_PHONE));
				//passenger.getAddressInfos().setAddress("");
				
				passenger.setSerial(ticket.getInt(WebApi.API_RESP_TICKET_SERIAL));
				passenger.getAddressInfos().setAddress(ticket.getString(WebApi.API_RESP_TICKET_PASSENGER_ADDRESS));
				passenger.getAddressInfos().setPosition(ticket.getInt(WebApi.API_RESP_TICKET_PASSENGER_LATE6),
						ticket.getInt(WebApi.API_RESP_TICKET_PASSENGER_LONGE6));
				
				lstTickets.add(passenger);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return lstTickets;
	}
	
	

}
