package com.example.StockServer;

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

import com.example.Constants.QueryConstants;
import com.example.StockExchange.StockAvailable;
import com.example.StockExchange.StockMapper;
import com.example.StockExchange.User;
import com.example.StockExchange.UserRowMapper;

public class DaoImpl {
	
	private static DaoImpl singelton = new DaoImpl();
	
	private Logger logger = LoggerFactory.getLogger(DaoImpl.class);
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	public static DaoImpl getInstance() {
		return singelton;
	}
	
	private DaoImpl() {
		
	}
	
	public String updateBuy(String username, String exchange,String stock, Integer quantity) {
		
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
			return "Purchase Successful";
		}
		catch (DataAccessException e) {
			return "Purchase Failed";
		}
		
	}
	
	
	public String updateSell(String username, String exchange,String stock, Integer quantity) {
		
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
				ss2.put("price", DaoImpl.randomNumberGenerator());
				ss2.put("buysell", "sell");
				
				namedParameterJdbcTemplate.update(QueryConstants.INSERTSELL, ss2);
				
				return "Selling successful sold stock :::" + number;
				
			}
		}
		else {
			
			return "Not enough stock on you";
		}
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
	
	
	public static double randomNumberGenerator() {
		return 2+Math.random();
	}
	

}
