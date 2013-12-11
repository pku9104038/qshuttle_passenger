/**
 * 
 */
package com.qshuttle.passenger;

/**
 * @author wangpeifeng
 *
 */
public class InstanceInfos {
	
	private int serial;
	private int hubSerial;
	private String lineType;
	private int serviceAreaSerial;
	private String hubName;
	private String serviceAreaName;
	private String instanceDate;//yyyy-mm-dd
	private String scheduleDepartureTime;//HH:MM:SS
	private String scheduleArriveTime;//HH:MM:SS
	private int seats;
	private int tickets;
	private int price;

	public InstanceInfos(){
		this.serial = 0;
		this.hubSerial = 0;
		this.lineType = "";
		this.serviceAreaSerial = 0;
		this.hubName = "";
		this.serviceAreaName = "";
		this.instanceDate = "";
		this.scheduleDepartureTime = "";
		this.scheduleArriveTime = "";
		this.seats = 0;
		this.tickets = 0;
		this.price = 0;
	}
	
	public int getSerial(){
		return this.serial;
	}
	
	public void setSerial(int serial){
		this.serial = serial;
	}
	
	public int getHubSerial(){
		return this.hubSerial;
	}
	
	public void setHubSerial(int hubSerial){
		this.hubSerial = hubSerial;
	}
	
	public String getLineType(){
		return this.lineType;
	}
	
	public void setLineType(String lineType){
		this.lineType = lineType;
	}
	
	public int getServiceAreaSerial(){
		return this.serviceAreaSerial;
	}
	
	public void setServiceAreaSerial(int serial){
		this.serviceAreaSerial = serial;
	}
	
	public String getHubName(){
		return this.hubName;
	}
	
	public void setHubName(String name){
		this.hubName = name;
	}
	
	public String getServiceAreaName(){
		return this.serviceAreaName;
	}
	
	public void setServiceAreaName(String name){
		this.serviceAreaName = name;
	}
	
	public String getInstanceDate(){
		return this.instanceDate;
	}
	
	public void setInstanceDate(String date){
		this.instanceDate = date;
	}
	
	public String getArraiveTime(){
		return this.scheduleArriveTime;
	}
	
	public void setArraiveTime(String time){
		this.scheduleArriveTime = time;
	}
	
	public String getDepartureTime(){
		return this.scheduleDepartureTime;
	}
	
	public void setDeparturetime(String time){
		this.scheduleDepartureTime = time;
	}
	
	public int getSeats(){
		return this.seats;
	}
	
	public void setSeats(int seats){
		this.seats = seats;
	}
	
	public int getTickets(){
		return this.tickets;
	}
	
	public void setTickets(int tickets){
		this.tickets = tickets;
	}
	
	public int getPrice(){
		return this.price;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
}
