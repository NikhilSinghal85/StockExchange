package com.example.StockExchangeModels;

/**
 * POJO class for database table History
 * @author nikhil.singhal
 *
 */
public class History {


	private String username;

	private String average;
	
	private String positionValue;
	
	/**
	 * 
	 */
	private String price;


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPositionValue() {
		return positionValue;
	}

	public void setPositionValue(String positionValue) {
		this.positionValue = positionValue;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}


	public String getaverage() {
		return average;
	}

	public void setaverage(String average) {
		this.average = average;
	}




}
