package com.example.Constants;

public class QueryConstants {
	
	
	public final static String  INSERTBUY = "insert into hr.records (records_id, username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) "
			+ "values(hr.records_id_sequence.nextval, :username, :exchange, :stock, :amount, :datetime, :price, :buysell )";
	
	public final static String  INSERTSELL = "insert into hr.records (records_id, username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) values(hr.records_id_sequence.nextval,"
			+ "	:username, :exchange, :stock, :amount, :datetime, :price, :buysell )";
	
	public final static String  FETCHBUY = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :buy";
			
	public final static String  FETCHSELL = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :sell";
	
	public final static String  LOGINVALIDATION  = "Select * from hr.Login where username = :userName  AND password = :password";

}
