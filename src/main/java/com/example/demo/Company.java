package com.example.demo;

/**
 * POJO class for database table Company
 * @author nikhil.singhal
 *
 */
public class Company {

	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	private String value;
	
	private String stockType;
	
	
}
