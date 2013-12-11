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

public class ListAdapterAreaSelect extends BaseAdapter {

	/////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private Context context;
	private JSONArray arrayItems;
	private int selectedItem;
	private String key_name,key_serial;
	
	
	/**
	 * @param context
	 * @param arrayItems
	 * @param selectedItem
	 */
	public ListAdapterAreaSelect(Context context, JSONArray arrayItems,String key_name,
			String key_serial, int selectedItem) {
		super();
		this.context = context;
		this.arrayItems = arrayItems;
		this.selectedItem = selectedItem;
		this.key_name = key_name;
		this.key_serial = key_serial;
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
			return ((long)(item.getInt(key_serial)));
		}
		catch(JSONException e){
			return -1;
		}
	}

	public String getItemName(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject item = (JSONObject) arrayItems.get(position);
			return item.getString(key_name);
		}
		catch(JSONException e){
			e.printStackTrace();
			return "";
		}
	}
	
	public void setSelectedHub(int serial){
		selectedItem = serial;
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
			if(this.selectedItem == getItemId(position)){
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
