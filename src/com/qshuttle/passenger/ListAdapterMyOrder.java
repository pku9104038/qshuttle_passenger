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
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ListAdapterMyOrder extends BaseAdapter {
	
	private Context context;
	
	private JSONArray arrayItems;
	
	

	/**
	 * @param context
	 * @param arrayItems
	 */
	public ListAdapterMyOrder(Context context, JSONArray arrayItems) {
		super();
		this.context = context;
		this.arrayItems = arrayItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayItems.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		JSONObject item = null;
		try{
			item = arrayItems.getJSONObject(position);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		long id = 0;
		try{
			JSONObject item = (JSONObject) getItem(position);
			id = item.getLong(WebApi.API_RESP_ORDER_SERIAL);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try{
			
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_myorder, null);
			
			((TextView)(convertView.findViewById(R.id.textViewSerial))).setText(""+getItemSerial(position));
			((TextView)(convertView.findViewById(R.id.textViewStatus))).setText(getItemStatus(position));
			((TextView)(convertView.findViewById(R.id.textViewPrice))).setText(""+getItemPrice(position));
			((TextView)(convertView.findViewById(R.id.textViewUpdateTime))).setText(getItemUpdateTime(position));
			
			
			return convertView;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private long getItemSerial(int position){
		return getItemId(position);
	}
	
	private String getItemStatus(int position){
		JSONObject item = (JSONObject) getItem(position);
		String status = "";
		try{
			status = item.getString(WebApi.API_RESP_ORDER_STATE);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	private int getItemPrice(int position){
		int price = 0;
		try{
			JSONObject item = (JSONObject) getItem(position);
			price = item.getInt(WebApi.API_RESP_ORDER_PRICE);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return price;
	}
	
	private String getItemUpdateTime(int position){
		String time = "";
		try{
			JSONObject item = (JSONObject)getItem(position);
			time = item.getString(WebApi.API_RESP_ORDER_UPDATETIME);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return time;
	}
	

}
