package com.example.StockExchangeService;

public interface StockExchangeService {
	
	/**
	 * method to update purchase record of user
	 */
	public String updateBuy(String username, String exchange, String stock, Integer quantity);

	/**
	 * method to fetch top Nth Transaction made overall
	 */
	public String topTransaction(Integer position);

	/**
	 * method to validate login by swagger only
	 */
	public String loginValidation(String username, String pass);

	/**
	 * method to update purchase / sell history of user
	 */
	public String buySellHistory( String sdate, String edate, String buysell);

	/**
	 * method to fetch average purchase/sell history of user
	 */
	public String averageHistory(Integer average);

	/**
	 * method to update sell record of user
	 */
	public String updateSell(String username, String exchange, String stock, Integer quantity);
}
