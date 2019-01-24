package com.example.StockServer;


/**
 * This is the POJO for JSON mapping of all incoming data from client.
 * @author nikhil.singhal
 *
 */
public class StockMessage {

	private String user;


	private String exchange;

	private String stock;

	private Integer number;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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


	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}