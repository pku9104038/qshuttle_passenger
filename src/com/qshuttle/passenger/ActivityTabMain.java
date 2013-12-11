/**
 * 
 */
package com.qshuttle.passenger;


import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

/**
 * @author wangpeifeng
 *
 */
public class ActivityTabMain extends FragmentActivity {
	

	public static class TabManager implements TabHost.OnTabChangeListener{

		static final class TabInfo{
			private final String tag;
			private final Class<?> cls;
			private final Bundle args;
			private Fragment fragment;
			
			TabInfo(String _tag, Class<?> _cls, Bundle _args){
				tag = _tag;
				cls = _cls;
				args = _args;
			}
		}
		
		static class DummyTabFactory implements TabHost.TabContentFactory{
			
			private Context mContext;
			
			/**
			 * @param mContext
			 */
			public DummyTabFactory(Context mContext) {
				super();
				this.mContext = mContext;
			}

			
			public View createTabContent(String tag) {
				// TODO Auto-generated method stub
				//return null;
				View v = new View(mContext);
				v.setMinimumHeight(0);
				v.setMinimumWidth(0);
				return v;
			}
			
		}
		
		private final FragmentActivity mActivity;
		private final TabHost mTabHost;
		private final int mContainerId;
		private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
		private TabInfo mLastTab;
		
		/**
		 * @param mActivity
		 * @param mTabHost
		 * @param mContainerId
		 */
		public TabManager(FragmentActivity mActivity, TabHost mTabHost,
				int mContainerId) {
			super();
			this.mActivity = mActivity;
			this.mTabHost = mTabHost;
			this.mContainerId = mContainerId;
			this.mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> cls, Bundle args){
			
			tabSpec.setContent(new DummyTabFactory(mActivity));
			String tag = tabSpec.getTag();
			
			TabInfo info = new TabInfo(tag, cls, args);
			
			info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
			if(info.fragment != null && !info.fragment.isDetached()){
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}
			
			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
			
		}
		
		
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
			TabInfo newTab = mTabs.get(tabId);
			
			if(mLastTab != newTab){
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				if(mLastTab != null){
					if(mLastTab.fragment != null){
						ft.detach(mLastTab.fragment);
					}
				}
				
				if(newTab != null){
					if(newTab.fragment == null){
						newTab.fragment = Fragment.instantiate(mActivity, newTab.cls.getName(),newTab.args);
						ft.add(mContainerId, newTab.fragment, newTab.tag);
					}
					else{
						ft.attach(newTab.fragment);
					}
				}
				
				mLastTab = newTab;
				ft.commit();
				mActivity.getSupportFragmentManager().executePendingTransactions();
			}
			
		}
		
		
	}

	/////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
    
	private TabHost tabHost;
	private TabManager tabManager;
	
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.fragment_tabs);
		
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
		
		tabManager.addTab(tabHost.newTabSpec("Booking").setIndicator("Booking"),
				TabBookingFragmentActivity.TabBookingFragment.class, null);

		tabManager.addTab(tabHost.newTabSpec("MyOrder").setIndicator("MyOder"),
				TabMyOrderFragmentActivity.TabMyOrderFragment.class, null);
		if(savedInstanceState!=null){
			tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("tab", tabHost.getCurrentTabTag());
	}
	
	
	
	

}
