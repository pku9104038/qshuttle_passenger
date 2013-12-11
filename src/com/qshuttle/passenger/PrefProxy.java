/**
 *
 */
package com.qshuttle.passenger;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 * PrefProxy - SharedPreferences proxy class
 *
 * @author wangpeifeng
 */
public class PrefProxy {

    /////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
    
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////
	/**
	 * FILE_SHAREDPREFERENCES 
	 * 
	 */
	private static final String FILE_SHAREDPREFERENCES 				= "myPreferences";
	
	/**
	 * Passenger
	 */
	private static final String KEY_PASSENGER_ARRAY					= "passenger_array";
	private static final String VAL_DEF_PASSENGER_ARRAY				= "";
	
	/**
	 * Hub
	 */
	private static final String KEY_HUB_NAME						= "hub_name";
	private static final String KEY_HUB_SZERIAL						= "hub_serial";
	private static final String VAL_DEF_HUB_NAME					= "";
	private static final int VAL_DEF_HUB_SERIAL						= 0;

	/**
	 * SETTING_PARAM 
	 */
	private static final String KEY_SETTING_HOST					= "setting_host";
	private static final String KEY_SETTING_PORT					= "setting_port";
	private static final String KEY_SETTING_API_ROOT				= "setting_api_root";
	private static final String KEY_SETTING_SOCKETPORT				= "setting_socketport";
	private static final int VAL_DEFAULT_SOCKETPORT					= 8787;
	private static final String VAL_DEFAULT_HOST					= "www.q-shuttle.com";
	private static final String VAL_DEFAULT_PORT					= "8080";
	private static final String VAL_DEFAULT_API_ROOT				= "/qk_shuttle/api/";
	//private static final String VAL_DEFAULT_HOST					= "192.168.0.108";
	//private static final String VAL_DEFAULT_PORT					= "8088";
	
	/**
	 * LOGIN_INFO 
	 */
	private static final String KEY_LOGIN_ACCOUNT					= "login_account";
	private static final String KEY_LOGIN_PWD						= "login_pwd";
	public static final String KEY_LOGIN_USER_NAME					= "user_name";
	private static final String VAL_DEFAULT_ACCOUNT					= "";
	private static final String VAL_DEFAULT_PWD						= "";
	private static final String VAL_DEFAULT_USER_NAME				= "";
	
	private static final String VAL_DEFAULT_NULL					= "NULL";
	
	
	
	/**
	 * 
	 */
	public static final String KEY_MY_LATE6 						= "my_late6";
	public static final String KEY_MY_LONGE6 						= "my_longe6";
	
	public static final int VALUE_DEFAULT_MY_LATE6 					= 31754648;//QUNKAI_LATE6;
	public static final int VALUE_DEFAULT_MY_LONGE6 				= 120936981;//QUNKAI_LONGE6;
	
	
	public static final String KEY_MY_INSTANCE 						= "my_instance";
	public static final String VALUE_DEFAULT_MY_INSTANCE 			= "";
	
	public static final String KEY_MY_ORDER 						= "my_order";
	public static final String VALUE_DEFAULT_MY_ORDER 				= "";
	
	/////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////

	/////////////////////////////////////////////////
    // METHODS, STRING
    /////////////////////////////////////////////////


	/**
	 * putString		write string to preferences
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context,String key, String value)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * getString 		read string from preferences
	 * @param context
	 * @param key
	 * @param def_value
	 * @return String
	 */
	public static String getString(Context context, String key, String def_value)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(key, def_value);
	}


	/////////////////////////////////////////////////
    // METHODS, INT
    /////////////////////////////////////////////////


	/**
	 * putInt		write int to preferences
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context,String key, int value)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * getint 		read int from preferences
	 * @param context
	 * @param key
	 * @param def_value
	 * @return String
	 */
	public static int getInt(Context context, String key, int def_value)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getInt(key, def_value);
	}


	
	/////////////////////////////////////////////////
    // METHODS, KEY/VALUE PAIR
    /////////////////////////////////////////////////
	class KeyTextPair{
		String key;
		String text;
		KeyTextPair(String key, String text){
			this.key = key;
			this.text = text;
		}
	}

	/**
	 * setKeyText		write key/text pair to preferences
	 * @param context
	 * @param pair
	 */
	public static void setKeyText(Context context,KeyTextPair pair)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(pair.key, pair.text);
		editor.commit();
	}
	
	/**
	 * getKeyText 		read key/text pair from preferences
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getKeyText(Context context, String key)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(key, VAL_DEFAULT_NULL);
	}


	/////////////////////////////////////////////////
    // METHODS, HUB, AREA, LINE, DATE
    /////////////////////////////////////////////////

	
	
    /////////////////////////////////////////////////
    // METHODS, SETTING PARAMETERS
    /////////////////////////////////////////////////

	/**
	 * setHost		write host to preferences
	 * @param context
	 * @param host
	 */
	public static void setHost(Context context,String host)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_SETTING_HOST, host);
		editor.commit();
	}
	
	/**
	 * getHost 		read host from preferences
	 * @param context
	 * @return
	 */
	public static String getHost(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(KEY_SETTING_HOST, VAL_DEFAULT_HOST);
	}
	
	/**
	 * setHost		write port to preferences
	 * @param context
	 * @param host
	 */
	public static void setPort(Context context,String port)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_SETTING_PORT, port);
		editor.commit();
	}
	
	/**
	 * getHost 		read port from preferences
	 * @param context
	 * @return
	 */
	public static String getPort(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(KEY_SETTING_PORT, VAL_DEFAULT_PORT);
	}
	
	
	/**
	 * setHost		write port to preferences
	 * @param context
	 * @param host
	 */
	public static void setApiRoot(Context context,String port)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_SETTING_API_ROOT, port);
		editor.commit();
	}
	
	/**
	 * getHost 		read port from preferences
	 * @param context
	 * @return
	 */
	public static String getApiRoot(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(KEY_SETTING_API_ROOT, VAL_DEFAULT_API_ROOT);
	}

	/**
	 * setSocketPort	write socket port to preferences
	 * @param context
	 * @param host
	 */
	public static void setSocketPort(Context context,int port)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_SETTING_SOCKETPORT, port);
		editor.commit();
	}
	
	/**
	 * getSocketHost 	read port from preferences
	 * @param context
	 * @return
	 */
	public static int getSocketPort(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getInt(KEY_SETTING_SOCKETPORT, VAL_DEFAULT_SOCKETPORT);
	}

    /////////////////////////////////////////////////
    // METHODS, LOGIN_INFOS
    /////////////////////////////////////////////////
	
	/**
	 * setAccount		write account to preferences
	 * @param context
	 * @param account
	 */
	public static void setAccount(Context context,String account)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_LOGIN_ACCOUNT, account);
		editor.commit();
	}
	
	/**
	 * getAccount 		read account from preferences
	 * @param context
	 * @return
	 */
	public static String getAccount(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(KEY_LOGIN_ACCOUNT, VAL_DEFAULT_ACCOUNT);
	}
	
	public static void setUserName(Context context,String name)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_LOGIN_USER_NAME, name);
		editor.commit();
	}
	
	public static String getUserName(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return pref.getString(KEY_LOGIN_USER_NAME, VAL_DEFAULT_USER_NAME);
	}

	
	/**
	 * setPwd		write pwd to preferences
	 * @param context
	 * @param pwd
	 */
	public static void setPwd(Context context,String pwd)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_LOGIN_PWD, signPassword(pwd));
		editor.commit();
	}
	
	/**
	 * getPwd 		read pwd from preferences
	 * @param context
	 * @return
	 */
	public static String getPwd(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		return signPassword(pref.getString(KEY_LOGIN_PWD, VAL_DEFAULT_PWD));
		
	}
	
	private static String signPassword(String pwd){
		//return pwd;
		//Log.i("test", "pwd="+pwd);
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<pwd.length(); i++){
			sb.append(pwd.charAt(pwd.length()-1-i));
		}
		//Log.i("test", "password="+sb.toString());
		return sb.toString();
		
	}
	
	
	
	
	// write myLate6
	public static void setMyLate6(Context context,int late6){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_MY_LATE6, late6);
		
		editor.commit();
		
	}
	
	// read myLate6
	public static int getMyLate6(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_MY_LATE6, VALUE_DEFAULT_MY_LATE6);
	
	}
	

	// write myLonge6
	public static void setMyLonge6(Context context,int longe6){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_MY_LONGE6, longe6);
		
		editor.commit();
		
	}
	
	// read myLonge6
	public static int getMyLonge6(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_MY_LONGE6, VALUE_DEFAULT_MY_LONGE6);
	
	}
	
	
	// write 
	public static void setMyInstance(Context context,String strInstance){
		
		Log.i("setMyInstance", strInstance);
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_MY_INSTANCE, strInstance);
		
		editor.commit();
		
	}
	
	// read 
	public static String getMyInstance(Context context){
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		String strInstance =  myPreferences.getString(KEY_MY_INSTANCE, VALUE_DEFAULT_MY_INSTANCE);
		//Log.i("getMyInstance", strInstance);
		return strInstance;
		
	
	}
	
	public static int getMyInstanceHubSeriale(Context context){
		int serial = 0;
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			serial = obj.getInt(WebApi.API_PARAM_HUB_SERIAL);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return serial;
	}
	
	public static String getMyInstanceHubName(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_RESP_HUB_NAME);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	
	public static String getMyInstanceLineType(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_PARAM_LINE_TYPE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	
	public static String getMyInstanceDate(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_PARAM_INSTANCE_DATE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	public static String getMyInstanceFrom(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_RESP_LINE_FROM);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	public static String getMyInstanceTo(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_RESP_LINE_TO);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	public static String getMyInstanceDepartureTime(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_RESP_TIME_DEPARTURE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	public static String getMyInstanceArriveTime(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			string = obj.getString(WebApi.API_RESP_TIME_ARRIVE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}


	public static int getMyInstanceSeriale(Context context){
		int serial = 0;
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			serial = obj.getInt(WebApi.API_RESP_INSTANCE_SERIAL);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return serial;
	}
	

	public static int getMyInstancePrice(Context context){
		int price = 0;
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			price = obj.getInt(WebApi.API_RESP_PRICE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return price;
	}


	public static int getMyInstanceSeats(Context context){
		int seats = 0;
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			seats = obj.getInt(WebApi.API_RESP_SEATS);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return seats;
	}
	
	public static int getMyInstanceTickets(Context context){
		int tickets = 0;
		try{
			JSONObject obj = new JSONObject(getMyInstance(context));
			tickets = obj.getInt(WebApi.API_RESP_TICKETS);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return tickets;
	}
	
	
	// write 
	public static void setMyOrder(Context context,String strOrder){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_MY_ORDER, strOrder);
		
		editor.commit();
		
	}
	
	// read 
	public static String getMyOrder(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_MY_ORDER, VALUE_DEFAULT_MY_ORDER);
	
	}

	public static int getMyOrderPriceTotal(Context context){
		int price = 0;
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			price = obj.getInt(WebApi.API_PARAM_ORDER_PRICE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return price;
	}
	
	public static String getMyOrderHubName(Context context){
		String string  = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			string = obj.getString(WebApi.API_RESP_HUB_NAME);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}

	public static String  getMyOrderAreaName(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			string = obj.getString(WebApi.API_RESP_AREA_NAME);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	
	public static String getMyOrderLineType(Context context){
		String  string = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			string = obj.getString(WebApi.API_RESP_LINE_OPERATION_TYPE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string ;
	}
	
	public static String getMyOrderInstanceDate(Context context){
		String date = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			date = obj.getString(WebApi.API_PARAM_INSTANCE_DATE);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getMyOrderStartTime(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			string = obj.getString(WebApi.API_RESP_START_TIME);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
	
	public static String getMyOrderStopTime(Context context){
		String string = "";
		try{
			JSONObject obj = new JSONObject(getMyOrder(context));
			string = obj.getString(WebApi.API_RESP_STOP_TIME);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return string;
	}
}
