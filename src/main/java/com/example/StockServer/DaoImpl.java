package com.example.StockServer;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.Constants.QueryConstants;
import com.example.DBMappers.HistoryRowMapper;
import com.example.DBMappers.StockMapper;
import com.example.DBMappers.UserHistoryRowMapper;
import com.example.DBMappers.UserRowMapper;
import com.example.StockExchange.History;
import com.example.StockExchange.StockAvailable;
import com.example.StockExchange.User;
import com.example.StockExchange.UserHistoryValues;


@Component
public class DaoImpl {
	
	private Logger logger = LoggerFactory.getLogger(DaoImpl.class);
	
	
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	public String updateBuy(String username, Integer exchange,String stock, Integer quantity) {
		
		LocalDateTime ldt =  LocalDateTime.now();
		
		Map<String, Object> ss4 = new HashMap<>();
		ss4.put("username", username);
		ss4.put("exchange", exchange);
		ss4.put("stock", stock);
		ss4.put("amount", quantity);
		ss4.put("datetime", ldt.toString());
		ss4.put("price", randomNumberGenerator());
		ss4.put("buysell", "buy");
		// first entry for given stock
		try {
			namedParameterJdbcTemplate.update(QueryConstants.INSERTBUY, ss4);
			logger.info("Purchase Successful");
			return "PurchaseSuccessful";
		}
		catch (DataAccessException e) {
			logger.info("Purchase Failed");
			return "PurchaseFailed";
		}
		
	}
	
	
	public String updateSell(String username, Integer exchange,String stock, Integer quantity) {
		
		Map<String, Object> ss = new HashMap<>();
		ss.put("stock", stock);
		ss.put("exchange", exchange);
		ss.put("username", username);
		ss.put("buy", "buy");
		ss.put("sell", "sell");
		
		List<StockAvailable> resultSetBuy = namedParameterJdbcTemplate.query(QueryConstants.FETCHBUY, ss, new StockMapper());
		List<StockAvailable> resultSetSell = namedParameterJdbcTemplate.query(QueryConstants.FETCHSELL, ss, new StockMapper());
		
		if (resultSetBuy.size() > 0) {
			Integer buyNumber = 0;
			Integer sellNumber = 0;
			
			
			Iterator<StockAvailable> itr = resultSetBuy.iterator();
			Iterator<StockAvailable> itr2 = resultSetSell.iterator();
			while(itr.hasNext()) {
				buyNumber = itr.next().getNumber() + buyNumber;
			}
			while(itr2.hasNext()) {
				sellNumber = itr2.next().getNumber() + sellNumber;
			}
			Integer number = buyNumber -sellNumber;
			logger.info("Available quantity with user : " + number );

			if (quantity > number) {
				return "Not enough stock please select less stock";
			}
			else {
				
				LocalDateTime ldt =  LocalDateTime.now();
				Map<String, Object> ss2 = new HashMap<>();
				ss2.put("stock", stock);
				ss2.put("exchange", exchange);
				ss2.put("username", username);
				ss2.put("amount", quantity);
				ss2.put("datetime", ldt.toString());
				ss2.put("price", randomNumberGenerator());
				ss2.put("buysell", "sell");
				
				namedParameterJdbcTemplate.update(QueryConstants.INSERTSELL, ss2);
				
				return "SellingSuccessful";
				
			}
		}
		else {
			
			return "Not enough stock on you";
		}
	}
	
	
	
	public String averageHistory(Integer average) {
		Map<String, Object> ss = new HashMap<>();
		ss.put("average", average);
		List<History> resultSet = namedParameterJdbcTemplate.query(QueryConstants.HISTORY, ss, new HistoryRowMapper());
		Iterator<History> itr = resultSet.iterator();
		String result = " ";
		while (itr.hasNext()) {
			History temp = itr.next();
			result  = result + " --> " +  temp.getusername() + " Average Buy Price is :: Rs " + temp.getaverage() + "\n";
		}
		return result;
		
	}
	
	public String buySellHistory(String username, String sdate, String edate, String buysell ) {
		Map<String, Object> ss = new HashMap<>();
		ss.put("username", username);
		ss.put("sdate", sdate);
		ss.put("edate", edate);
		ss.put("buysell", buysell);
		
		
		String  buySellHistory = "Select timestamp, price, exchange_name, stock_name from hr.records where buysell = :buysell and username = :username and timestamp > to_date(:sdate, 'yyyy MM dd') and timestamp < to_date(:edate, 'yyyy MM dd')";

		List<UserHistoryValues> resultSet = namedParameterJdbcTemplate.query(buySellHistory, ss, new UserHistoryRowMapper());
		Iterator<UserHistoryValues> itr = resultSet.iterator();
		String result = "";
		if (buysell.equalsIgnoreCase("buy")) {
			result = "Purchase History::  ";
		}
		else {
			 result = "Sale History::  ";
		}
		while (itr.hasNext()) {
			UserHistoryValues temp = itr.next();
			result  = result + " --> Exchange is :: " +  temp.getExchange() + " Stock  is ::  " + temp.getStock()+ " Price  is :: Rs  " + temp.getPrice() + " Time  is ::  " + temp.getTimestamp() + "\n";
		}
		return result;
		
	}
	
	
	
	
	public String loginValidation(String username, String pass) {
		Map<String, Object> ss = new HashMap<>();
		ss.put("userName", username.toLowerCase());
		ss.put("password", pass);
		
		List<User> resultSet = namedParameterJdbcTemplate.query(QueryConstants.LOGINVALIDATION, ss, new UserRowMapper());
		
		if (resultSet.size() == 0  ) {
			return "Invalid Username or Password";
		}
		else if (resultSet.get(0).getUser().equalsIgnoreCase(username) && resultSet.get(0).getPassword().equals(pass)) {
			return "Login success";
		}
		else if (!resultSet.get(0).getPassword().equals(pass)) {
			return "Incorrect Password";
		}
		else {
			return "Invalid Username or Password";
		}
	}
	
	
	public String topTransaction(Integer position) {
		Map<String, Object> ss = new HashMap<>();
		ss.put("position", position);
		List<History> resultSet = namedParameterJdbcTemplate.query(QueryConstants.POSITON, ss, new HistoryRowMapper());
		Iterator<History> itr = resultSet.iterator();
		String result = " ";
		while (itr.hasNext()) {
			History temp = itr.next();
			result  = result + " The value at this position is --> " +  temp.getPositionValue() + "\n";
		}
		return result;
		
	}
	
	public  double randomNumberGenerator() {
		DecimalFormat df = new DecimalFormat("#.####");
		double d = Math.random();
		
		return Double.parseDouble(df.format(d)) *100 ;
	}
	

}
