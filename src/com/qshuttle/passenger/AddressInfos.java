/**
 * 
 */
package com.qshuttle.passenger;

/**
 * @author wangpeifeng
 *
 */
public class AddressInfos {
    /////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
    
	
	private String address;
	private int latE6;
	private int longE6;
	private String addressGeo;
	
	
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////
	public static final int VAL_DEFAULT_LATE6 = 0;
	public static final int VAL_DEFAULT_LONGE6 = 0;
	public static final String VAL_DEFAULT_STRING = "";
	

	/////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////
	
	public AddressInfos(){
		
		this.address = VAL_DEFAULT_STRING;
		this.latE6 = VAL_DEFAULT_LATE6;
		this.longE6 = VAL_DEFAULT_LONGE6;
		this.addressGeo = VAL_DEFAULT_STRING;
			
	}
	
	public AddressInfos(String address, int late6, int longe6){
		this.address = address;
		this.latE6 = late6;
		this.longE6 = longe6;
	}

	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	
	public void setPosition(int late6, int longe6){
		this.latE6 = late6;
		this.longE6 = longe6;
	}
	
	public int getLatE6(){
		return this.latE6;
	}
	
	public int getLongE6(){
		return this.longE6;
	}
	
	public void setaddressGeo(String addressGeo){
		this.addressGeo = addressGeo;
	}
	
	public String getAddressGeo(){
		return this.addressGeo;
	}
	
	public boolean isAvailable(){
		boolean bool = false;
		if( !VAL_DEFAULT_STRING.equals(this.address) 
			&& (this.latE6 != 0 || this.longE6 != 0 ) ){
			bool = true;
		}
		
		return bool;
	}

}
