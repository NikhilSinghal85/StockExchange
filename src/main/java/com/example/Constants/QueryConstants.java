package com.example.Constants;

public class QueryConstants {
	
	
	public final static String  INSERTBUY = "insert into hr.records ( username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) "
			+ "values( :username, :exchange, :stock, :amount, :datetime, :price, :buysell )";
	
	public final static String  INSERTSELL = "insert into hr.records ( username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) values("
			+ "	:username, :exchange, :stock, :amount, :datetime, :price, :buysell )";
	
	public final static String  FETCHBUY = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :buy";
			
	public final static String  FETCHSELL = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :sell";
	
	public final static String  LOGINVALIDATION  = "Select * from hr.Login where username = :userName  AND password = :password";

	public final static String  HISTORY = "Select username, AVG (Price) AS average from hr.records where buysell = 'buy' GROUP BY username having AVG (Price) > :average order by average desc";
	
	// not used running them directly in DB 
	
	public final static String  USERSEQUENCE ="CREATE SEQUENCE hr.user_id_sequence START WITH 1 INCREMENT BY 1";
	
	public final static String  USERTRIGGER = " CREATE TRIGGER hr.user_id_trigger\r\n" + 
			"BEFORE INSERT ON hr.Login\r\n" + 
			"FOR EACH ROW\r\n" + 
			"BEGIN\r\n" + 
			"  IF :new.user_id IS NULL THEN \r\n" + 
			"  SELECT hr.user_id_sequence.NEXTVAL \r\n" + 
			"  INTO :new.user_id\r\n" + 
			"  FROM dual;\r\n" + 
			"  END IF;\r\n" + 
			"END";
	
}
