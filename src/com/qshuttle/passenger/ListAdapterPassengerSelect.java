/**
 * 
 */
package com.qshuttle.passenger;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ListAdapterPassengerSelect extends BaseAdapter{
	
	/*
	 * PROPERTIES,PRIVATE 
	 */
	private Context context;
	private ArrayList<PassengerInfos> lstItems;
	private OnCheckedChangeListener lsrCheckBox;
	private OnClickListener lsrButton;
	
	/*
	 * CONSTRUCTOR
	 */
	public ListAdapterPassengerSelect(Context context, ArrayList<PassengerInfos> lstItems,
			OnCheckedChangeListener lsrCheckBox, OnClickListener lsrButton){
		this.context = context;
		this.lstItems = lstItems;
		this.lsrCheckBox = lsrCheckBox;
		this.lsrButton = lsrButton;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lstItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lstItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try{
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_passenger, null);
			
			((TextView)(convertView.findViewById(R.id.textViewPassengerName))).setText(lstItems.get(position).getName());
			((TextView)(convertView.findViewById(R.id.textViewPassengerPhone))).setText(lstItems.get(position).getPhone());
			((TextView)(convertView.findViewById(R.id.textViewPassengerAddress))).setText(lstItems.get(position).getAddressInfos().getAddress());
			
			CheckBox ckBox = (CheckBox)(convertView.findViewById(R.id.checkBoxPassenger));
			if(lstItems.get(position).isSelected()){
				ckBox.setChecked(true);
			}
			else{
				ckBox.setChecked(false);
			}
			ckBox.setTag(""+position);
			ckBox.setOnCheckedChangeListener(this.lsrCheckBox);
			
			ImageButton ibtnEdit = (ImageButton)convertView.findViewById(R.id.imageButtonEditPassenger);
			ibtnEdit.setTag(""+position);
			ibtnEdit.setOnClickListener(lsrButton);
			
			convertView.setTag(""+position);
			
			return convertView;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}

	}

}
