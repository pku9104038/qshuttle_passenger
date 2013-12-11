/**
 * 
 */
package com.qshuttle.passenger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wangpeifeng
 *
 */
public class TabBookingFragmentActivity extends FragmentActivity{
	
	
	public static class TabBookingFragment extends Fragment{
		
		
		

		/**
		 * 
		 */
		public TabBookingFragment() {
			super();
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			//return super.onCreateView(inflater, container, savedInstanceState);
			
			View v = inflater.inflate(R.layout.tab_booking, container, false);
			
			return v;
		}
		
		
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentById(android.R.id.content) == null){
			
			TabBookingFragment tabFragment = new TabBookingFragment();
			
			fm.beginTransaction().add(android.R.id.content,tabFragment).commit();
			
		}
	}
	
	

}
