/**
 * 
 */
package com.qshuttle.passenger;

/**
 * @author wangpeifeng
 *
 */
public class PassengerInfos {
	
    /////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////

	private int serial;
	private String phone;
	private String name;
	private AddressInfos addressInfos;
	private boolean selected;
	private String recordDesc;
	private int bigpackages;
	
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////
	
	private static final String VAL_DEFAULT_STRING = "";

	/////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////

	/////////////////////////////////////////////////
    // METHODS, STRING
    /////////////////////////////////////////////////
	
	public PassengerInfos(){
		this.serial = 0;
		this.phone = VAL_DEFAULT_STRING;
		this.name = VAL_DEFAULT_STRING;
		this.recordDesc = this.name;
		this.addressInfos = new AddressInfos();
		this.selected = false;
		this.bigpackages = 0;
	}
	
	public PassengerInfos(int serial, String name, String phone, String address, int late6, int longe6, boolean selected){
		this.serial = serial;
		this.phone = phone;
		this.name = name;
		this.recordDesc = this.name;
		this.addressInfos = new AddressInfos(address, late6, longe6);
		this.selected = selected;
		
	}
	
	public int getSerial(){	return this.serial;	}
	public void setSerial(int serial){	this.serial = serial;	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
	
	public String getPhone(){
		return this.phone;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setRecordDesc(String desc){
		this.recordDesc = desc;
	}
	
	public String getRecordDesc(){
		return this.recordDesc;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public void setAddreInfos(AddressInfos addressInfos){
		this.addressInfos = addressInfos;
	}
	
	public AddressInfos getAddressInfos(){
		return this.addressInfos;
	}
	
	
	public int getPackages(){
		return this.bigpackages;
	}
	
	public void setPackages(int packages){
		this.bigpackages = packages;
	}
	
	public boolean isAvailable(){
		boolean bool = false;
		
		if(!VAL_DEFAULT_STRING.equals(name)
				&& this.addressInfos.isAvailable() ){
			bool = true;
		}
		
		return bool;
	}
}
