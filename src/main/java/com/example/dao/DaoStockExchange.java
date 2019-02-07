package com.example.dao;



public interface DaoStockExchange {
	
	/**
	 * method to update purchase record of user to database
	 */
	public String updateBuy(String username, Integer exchange, String stock, Integer quantity);

	/**
	 * method to fetch top Nth Transaction made overall to database
	 */
	public String topTransaction(Integer position);

	/**
	 * method to validate login by swagger only to database
	 */
	public String loginValidation(String username, String pass);

	/**
	 * method to update purchase / sell history of user to database
	 */
	public String buySellHistory(String username, String sdate, String edate, String buysell);

	/**
	 * method to fetch average purchase/sell history of user to database
	 */
	public String averageHistory(Integer average);

	/**
	 * method to update sell record of user to database
	 */
	public String updateSell(String username, Integer exchange, String stock, Integer quantity);

}