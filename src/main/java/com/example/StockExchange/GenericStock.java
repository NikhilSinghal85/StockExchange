package com.example.StockExchange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * This is implementation of StockExchange will further extend it
 * @author nikhil.singhal
 *
 */

@RestController
@RequestMapping("/Generic")
public class GenericStock implements StockExchange {
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Logger logger = LoggerFactory.getLogger(GenericStock.class);
	
	private Set<Company> myData = new HashSet<>();

	@GetMapping("/fetch")
	@ResponseBody
	public String getResult(@RequestParam("name") String name, @RequestParam("value") String val) {
		logger.info("Fetching Stock results");
		Iterator<Company> itr = myData.iterator();
		
		while(itr.hasNext()) {
			Company com = itr.next();
			if (com.getStockType().equalsIgnoreCase(name) 
					&& (com.getName().equalsIgnoreCase(val))) {
				logger.info("type:: " + com.getStockType() + "val:: " + com.getName());
				return com.getValue();
			}
		}
		return null;
	}
	
	
	
	@PostConstruct
	private void dataLoad() {
//		Map m = new HashMap<>();
//		String ss = "CREATE TABLE company\r\n" + 
//				"( company_id number(10) PRIMARY KEY,  \r\n" + 
//				"  company_name varchar2(50) NOT NULL,  \r\n" + 
//				"  company_value varchar2(50),  \r\n" + 
//				"  company_stock varchar2(50)" + 
//				")";
//		jdbcTemplate.execute(ss);
//		
//		String ss2 = "INSERT INTO company\r\n" + 
//				"(company_id, company_name, company_value, company_stock)  \r\n" + 
//				"VALUES  \r\n" + 
//				"(1, 'Comp1', 'NSE Comp1: Price = ','NSE' )";
//		String ss3 = "INSERT INTO company\r\n" + 
//				"(company_id, company_name, company_value, company_stock)  \r\n" + 
//				"VALUES  \r\n" + 
//				"(2, 'Comp2', 'NSE Comp2: Price = ','NSE' )";
//		String ss4 = "INSERT INTO company\r\n" + 
//				"(company_id, company_name, company_value, company_stock)  \r\n" + 
//				"VALUES  \r\n" + 
//				"(3, 'Comp3', 'NSE Comp2: Price = ','NSE' )";
//		
//		jdbcTemplate.update(ss2);
//		jdbcTemplate.update(ss3);
//		jdbcTemplate.update(ss4);
		
		List<Company> lst = namedParameterJdbcTemplate.query("Select * from company ", new CompanyRowMapper());
		Iterator<Company> itr = lst.iterator();
		while(itr.hasNext()) {
			Company c = itr.next(); 
			myData.add(c);
			
		}
		
	}
}


