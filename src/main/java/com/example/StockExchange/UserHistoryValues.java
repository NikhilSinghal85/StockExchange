package com.example.StockExchange;

/**
 * POJO class for database table History
 * @author nikhil.singhal
 *
 */
public class UserHistoryValues {
	
	private String timestamp;

	private String price;
	
	private String exchange;

	private String stock;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	


	}




