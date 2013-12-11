/**
 * 
 */
package com.qshuttle.passenger;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * @author wangpeifeng
 *
 */
public class WebApi 
{
	
    /////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
    
	private OnHttpResponse onHttpResponse;
	
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////
	
	public static final String PROTOCOL_HTTP 						= "http://";
    
	/**
	 * PrefProxy.getApiRoot(context)
	 */
	//public static final String API_ROOT 							= "/qk_shuttle2/api/";
	//public static final String API_ROOT 							= "/qk_shuttle/api/";
    
	
	//api script
	public static final String API_USER_LOGIN 						= "userLogin";
	public static final String API_USER_REGISTER 					= "userRegister";
	public static final String API_GETBACK_PWD 						= "findPassword";
	
	public static final String API_QUERY_HUB	 					= "findHub";
	public static final String API_QUERY_AREA	 					= "findServiceArea";
	public static final String API_QUERY_TICKET 					= "findSchedule";
	public static final String API_QUERY_PASSENGER					= "findCommonPassenger";
	public static final String API_ADD_PASSENGER					= "addCommonPassenger";
	public static final String API_EDIT_PASSENGER					= "modCommonPassenger";
	public static final String API_DELETE_PASSENGER					= "deleteCommonPassenger";
	public static final String API_COMMIT_ORDER						= "commitOrder";
	public static final String API_QUERY_ORDERS						= "myOrder";
	public static final String API_QUERY_ORDER_DETAIL				= "orderTicket";
	public static final String API_QUERY_MYCAR						= "findDriverInfo";
	
	
	//api params
	public static final String API_PARAM_ACCOUNT 					= "userCellphone";
	public static final String API_PARAM_PASSWORD					= "userPasswordMd5";
	
	public static final String API_PARAM_HUB_SERIAL 				= "hubSerial";
	public static final String API_PARAM_LINE_TYPE					= "lineType";
	
	public static final String API_RESP_LINE_OPERATION_TYPE			= "lineOperationType";
	public static final String API_RESP_START_TIME					= "startTime";
	public static final String API_RESP_STOP_TIME					= "stopTime";
	
	public static final String API_PARAM_AREA_SERIAL 				= "serviceAreaSerial";
	public static final String API_PARAM_INSTANCE_DATE				= "instanceDate";
	
	public static final String API_PARAM_ORDER_PRICE				= "orderPrice";
	
	public static final String API_PARAM_DUE_DATE					= "dueDate";
	
	//api response fields
	public static final String API_RESP_USER_NAME					= "user_name";
	
	public static final String API_RESP_HUB_SERIAL					= "hubSerial";
	public static final String API_RESP_HUB_NAME					= "hubName";
	
	public static final String API_RESP_AREA_SERIAL					= "serviceAreaSerial";
	public static final String API_RESP_AREA_NAME					= "serviceAreaName";
	
	public static final String API_RESP_INSTANCE_SERIAL				= "instanceSerial";
	public static final String API_RESP_TICKETS						= "ticketAccount";
	public static final String API_RESP_SEATS						= "ticketSeats";
	public static final String API_RESP_PRICE						= "linePrice";
	public static final String API_RESP_TIME_DEPARTURE				= "scheduleDepartureTime";
	public static final String API_RESP_TIME_ARRIVE					= "scheduleArriveTime";
	public static final String API_RESP_LINE_FROM					= "line_from";
	public static final String API_RESP_LINE_TO						= "line_to";
	
	
	public static final String API_RESP_ADDRESS_SERIAL				= "addressSerial";
	public static final String API_RESP_PASSENGER_NAME				= "passengerName";
	public static final String API_RESP_PASSENGER_PHONE				= "passengerPhone";
	public static final String API_RESP_ADDRESS_INFO				= "addressInfo";
	public static final String API_RESP_ADDRESS_LATE6				= "addressLate6";
	public static final String API_RESP_ADDRESS_LONGE6				= "addressLong6";
	public static final String API_RESP_RECORD_DESC					= "recordDesc";				
	
	public static final String API_RESP_TICKET_SERIAL				= "ticketSerial";
	public static final String API_RESP_TICKET_PASSENGER_NAME		= "ticketPassengerName";
	public static final String API_RESP_TICKET_PASSENGER_PHONE		= "ticketPassengerPhone";
	public static final String API_RESP_TICKET_PASSENGER_ADDRESS	= "ticketPassengerAddress";
	public static final String API_RESP_TICKET_PASSENGER_LATE6		= "ticketPassengerLate6";
	public static final String API_RESP_TICKET_PASSENGER_LONGE6		= "ticketPassengerLonge6";
	public static final String API_RESP_TICKET_PASSENGER_PACKAGES	= "ticketPassengerPackage";				
	
	public static final String API_RESP_ORDER_SERIAL				= "orderSerial";				
	public static final String API_RESP_ORDER_STATE					= "orderState";				
	public static final String API_RESP_ORDER_PRICE					= "orderPrice";				
	public static final String API_RESP_ORDER_UPDATETIME			= "orderUpdateStamp";				
	
	
	public static final String API_RESP_DRIVER_NAME					= "driverName";				
	public static final String API_RESP_DRIVER_PHONE				= "driverCellPhone";				
	public static final String API_RESP_CAR_NUMBER					= "carNumber";				
	public static final String API_RESP_CAR_POS_LATE6				= "carPosLate6";				
	public static final String API_RESP_CAR_POS_LONGE6				= "carPosLong6";				
	
	//api response 
	public static final String API_RESP 							= "api_resp";
	public static final String API_RESP_ERR 						= "api_resp_err";
	public static final String API_RESP_MSG							= "api_resp_msg";
	public static final String API_RESP_ARRAY						= "apiArray";
	
	//public static final String API_RESP_MSG_NULL					= "请求网络服务失败！";
	
	//api values
	public static final String API_VAL_LINE_TYPE_TO					= "送站";
	public static final String API_VAL_LINE_TYPE_FROM				= "接站";
	
	
	private Context context;
	

    /////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////
	
    /////////////////////////////////////////////////
    // METHODS, COMMON
    /////////////////////////////////////////////////
	
	
	/**Constructor 
	 * @param CONTEXT context
	 */
	public WebApi(Context context) {
		super();
		this.context = context;
	}
	
	private String getApiRespNullMsg(){
		return this.context.getResources().getString(R.string.api_resp_null_msg);
	}
	
	
	/**
	 * setOnHttpResponce
	 * @param onHttpResponce
	 * @return void
	 */
	public void setOnHttpResponse(OnHttpResponse onHttpResponse)
	{
		this.onHttpResponse = onHttpResponse;
	}
	
	/**
	 * isRespSuccess
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isRespSuccess(String response)
	{
		try {
			return new JSONObject(response).getBoolean(API_RESP);
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public String getRespMsg(String response)
	{
		try {
			return new JSONObject(response).getString(API_RESP_MSG);
		}
		catch (Exception e){
			e.printStackTrace();
			return this.getApiRespNullMsg();
		}
	}
	
	public static int getRespErr(String response)
	{
		try {
			return new JSONObject(response).getInt(API_RESP_ERR);
		}
		catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public static JSONArray getRespArray(String response)
	{
		try {
			return new JSONObject(response).getJSONArray(API_RESP_ARRAY);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * postApi  create a new thread to post the params to script located a strUrl
	 * 			then callback the onHttpResponse while get the response from server
	 * @param strUrl
	 * @param params
	 */
	private void postApi(String strUrl, ArrayList<NameValuePair> params)
	{
		ThreadHttpPost thread = new ThreadHttpPost(strUrl, params, onHttpResponse);
		thread.start();
	}
	


    /////////////////////////////////////////////////
    // METHODS, APIs
    /////////////////////////////////////////////////

	/**
	 * registerUser
	 * 
	 * @param account
	 * @param pwd
	 */
	public void registerUser(String account, String pwd)
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_USER_REGISTER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, account));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_PASSWORD, pwd));
		
		postApi(strUrl, params);
	}
		
	/**
	 * loginDevice
	 * 
	 * @param account
	 * @param pwd
	 */
	public void loginDevice(String account, String pwd)
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_USER_LOGIN;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, account));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_PASSWORD, pwd));
		
		postApi(strUrl, params);
	}

	/**
	 * registerUser
	 * 
	 * @param account
	 * @param pwd
	 */
	public void getbackPwd(String account)
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_GETBACK_PWD;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, account));
		
		postApi(strUrl, params);
	}

	/**
	 * queryHub
	 * 
	 * @param 
	 * @param 
	 */
	public void queryHub()
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_HUB;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		postApi(strUrl, params);
	}

	/**
	 * queryArea
	 * 
	 * @param hub_serial
	 * @param line_type
	 */
	public void queryArea(int hub_serial, String line_type)
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_AREA;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_HUB_SERIAL, hub_serial+""));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_LINE_TYPE, line_type));
		
		
		postApi(strUrl, params);
	}

	/**
	 * queryTicket
	 * 
	 * @param hub_serial
	 * @param line_type
	 * @param area_serial
	 * @param instance_date
	 */
	public void queryTicket(int hub_serial, String line_type, int area_serial, String instance_date)
	{
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_TICKET;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_HUB_SERIAL, hub_serial+""));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_LINE_TYPE, line_type));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_AREA_SERIAL, area_serial+""));
		params.add(new BasicNameValuePair(WebApi.API_PARAM_INSTANCE_DATE, instance_date));
		
		postApi(strUrl, params);
	}

	
	public void queryPassengers(String myphone){

		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_PASSENGER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, myphone+""));
		
		postApi(strUrl, params);
		
	}
	
	public void addPassenger(String userPhone,PassengerInfos passenger){
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_ADD_PASSENGER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, userPhone+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_NAME, 
				passenger.getName()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_PHONE, 
				passenger.getPhone()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_INFO, 
				passenger.getAddressInfos().getAddress()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_RECORD_DESC, 
				passenger.getRecordDesc()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LATE6, 
				passenger.getAddressInfos().getLatE6()+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LONGE6, 
				passenger.getAddressInfos().getLongE6()+""));
		
		postApi(strUrl, params);
		
	}

	
	public void editPassenger(String userPhone,PassengerInfos passenger){
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_EDIT_PASSENGER;
		Log.i(this.getClass().getName(), "address serial:"+passenger.getSerial());
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, userPhone+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_SERIAL, 
				""+passenger.getSerial()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_NAME, 
				passenger.getName()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_PHONE, 
				passenger.getPhone()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_INFO, 
				passenger.getAddressInfos().getAddress()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_RECORD_DESC, 
				passenger.getRecordDesc()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LATE6, 
				passenger.getAddressInfos().getLatE6()+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LONGE6, 
				passenger.getAddressInfos().getLongE6()+""));
		
		postApi(strUrl, params);
		
	}
	
	public void deletePassenger(String userPhone,PassengerInfos passenger){
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_DELETE_PASSENGER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, userPhone+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_SERIAL, 
				""+passenger.getSerial()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_NAME, 
				passenger.getName()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_PASSENGER_PHONE, 
				passenger.getPhone()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_INFO, 
				passenger.getAddressInfos().getAddress()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_RECORD_DESC, 
				passenger.getRecordDesc()));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LATE6, 
				passenger.getAddressInfos().getLatE6()+""));
		params.add(new BasicNameValuePair(WebApi.API_RESP_ADDRESS_LONGE6, 
				passenger.getAddressInfos().getLongE6()+""));
		
		postApi(strUrl, params);
		
	}

	public void commitOrder(String order){

		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_COMMIT_ORDER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("jsonArray", order));
		Log.i(this.getClass().getName(), order);
		postApi(strUrl, params);
		
	}
	
	public void queryMyOrders(String account, String dueDate){

		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_ORDERS;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, account));
		
		params.add(new BasicNameValuePair(WebApi.API_PARAM_DUE_DATE, dueDate));
		postApi(strUrl, params);
		
	}
	
	public void queryOrderDetail(int serial){

		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_ORDER_DETAIL;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_RESP_ORDER_SERIAL, ""+serial));
		
		postApi(strUrl, params);
		
	}
	
	
	public void queryMyCar(String cellphone){
		String strUrl = PROTOCOL_HTTP
				+ PrefProxy.getHost(context) 
				+ ":" 
				+ PrefProxy.getPort(context)
				+ PrefProxy.getApiRoot(context) 
				+ API_QUERY_MYCAR;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebApi.API_PARAM_ACCOUNT, cellphone));
		
		postApi(strUrl, params);
			
	}
	

}
