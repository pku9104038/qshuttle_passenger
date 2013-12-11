/**
 * 
 */
package com.qshuttle.passenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ListAdapterInstanceSelect extends BaseAdapter{

	/////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private Context context;
	private JSONArray arrayItems;
	
	
	/**
	 * @param context
	 * @param arrayItems
	 * @param selectedItem
	 */
	public ListAdapterInstanceSelect(Context context, JSONArray arrayItems) {
		super();
		this.context = context;
		this.arrayItems = arrayItems;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayItems.length();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return arrayItems.get(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			return ((long)(item.getInt(WebApi.API_RESP_INSTANCE_SERIAL)));
		}
		catch(JSONException e){
			return -1;
		}
	}

	public String getItemDeparture(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			String time = item.getString(WebApi.API_RESP_TIME_DEPARTURE);
			return time.substring(0, "HH:MM".length());
			
		}
		catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public String getItemArrive(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			String time =  item.getString(WebApi.API_RESP_TIME_ARRIVE);
			return time.substring(0, "HH:MM".length());
		}
		catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}

	public int getItemPrice(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			return item.getInt(WebApi.API_RESP_PRICE);
		}
		catch(JSONException e){
			e.printStackTrace();
			return 0;
		}
	}

	public int getItemTickets(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			return item.getInt(WebApi.API_RESP_TICKETS);
		}
		catch(JSONException e){
			e.printStackTrace();
			return 0;
		}
	}

	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		try{
			
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_instance, null);
			
			((TextView)(convertView.findViewById(R.id.textViewTimeDeparture))).setText(getItemDeparture(position));
			((TextView)(convertView.findViewById(R.id.textViewTimeArrive))).setText(getItemArrive(position));
			((TextView)(convertView.findViewById(R.id.textViewPrice))).setText(""+getItemPrice(position));
			((TextView)(convertView.findViewById(R.id.textViewTickets))).setText(""+getItemTickets(position));
			
			
			return convertView;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

		
	}


}
