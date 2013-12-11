/**
 * 
 */
package com.qshuttle.passenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.InflateException;
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
public class ListAdapterHubSelect extends BaseAdapter {
	
	/////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private Context context;
	private JSONArray arrayHubs;
	private int selectedHub;

	
	
	/**
	 * @param context
	 * @param arrayHubs
	 * @param selectedHub
	 */
	public ListAdapterHubSelect(Context context, JSONArray arrayHubs,
			int selectedHub) {
		super();
		this.context = context;
		this.arrayHubs = arrayHubs;
		this.selectedHub = selectedHub;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayHubs.length();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		try {
			return arrayHubs.get(position);
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
			JSONObject item = (JSONObject) arrayHubs.get(position);
			return ((long)(item.getInt(WebApi.API_RESP_HUB_SERIAL)));
		}
		catch(JSONException e){
			return -1;
		}
	}

	public String getItemName(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayHubs.get(position);
			return item.getString(WebApi.API_RESP_HUB_NAME);
		}
		catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public void setSelectedHub(int serial){
		selectedHub = serial;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		try{
			
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_terminal, null);
			
			TextView tv = (TextView)(convertView.findViewById(R.id.textView));
			tv.setText(getItemName(position));
			
			ImageView iv = (ImageView)(convertView.findViewById(R.id.imageView));
			if(this.selectedHub == getItemId(position)){
				iv.setVisibility(View.VISIBLE);
			}
			else{
				iv.setVisibility(View.INVISIBLE);
			}
			
			
			return convertView;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

		
	}

}
