package com.example.constant;

public class QueryConstants {
	
	
	public final static String  INSERTBUY = "insert into hr.records ( username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) "
			+ "values( :username, :exchange, :stock, :amount, CURRENT_TIMESTAMP, :price, :buysell )";
	
	public final static String  INSERTSELL = "insert into hr.records ( username, exchange_name,stock_name,Stock_number, timestamp, price, buysell ) values("
			+ "	:username, :exchange, :stock, :amount, CURRENT_TIMESTAMP, :price, :buysell )";
	
	public final static String  FETCHBUY = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :buy";
			
	public final static String  FETCHSELL = "Select Stock_number from hr.records where stock_name = :stock AND exchange_name = :exchange AND username = :username AND buysell = :sell";
	
	public final static String  LOGINVALIDATION  = "Select * from hr.Login where username = :userName  AND password = :password";

//	public final static String  HISTORY = "Select username, AVG (Price) AS average from hr.records where buysell = 'buy' GROUP BY username having average > :average order by average desc";
	
	
	public final static String  HISTORY = "Select * from hr.AvgHistory where average > :average order by average desc";
	
	
	public final static String  POSITON = "select *\r\n" + 
			"  from\r\n" + 
			"  (\r\n" + 
			"    select\r\n" + 
			"        Price\r\n" + 
			"          ,dense_rank() over (order by Price desc) ranking\r\n" + 
			"    from   hr.records\r\n" + 
			"  )\r\n" + 
			"  where ranking = :position ";
	
	// this will same as above but in case 2 transactions are equal then it will still rank them 1 and 2 and fetch only 1 in even if 2 are valid
	public final static String  POSITON2 =  " SELECT *  FROM (SELECT Price, row_number() OVER (order by Price desc) AS rn FROM hr.records) WHERE rn = :position";
	
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
	
	public final static String  RECORDVIEW = "CREATE OR REPLACE VIEW HR.AvgHistory AS Select username, AVG (Price) AS average from hr.records where buysell = 'buy' GROUP BY username";
	
	
	
	//This procedure will fetch first_name from table hr.employee with employed_id given while executing procedure and
	// store same in string  variable emp_first which can be fetched in code at runtime
	public final static String STOREPROCEDURE = "CREATE OR REPLACE PROCEDURE hr.getEmpName \r\n" + 
			"   (EMP_ID IN NUMBER, EMP_FIRST OUT VARCHAR2) IS\r\n" + 
			"BEGIN\r\n" + 
			"   SELECT FIRST_NAME INTO EMP_FIRST\r\n" + 
			"   FROM hr.Employees\r\n" + 
			"   WHERE EMPLOYEE_ID = EMP_ID;\r\n" + 
			"END";
}
