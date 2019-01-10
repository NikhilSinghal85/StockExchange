package com.example.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
@RequestMapping("/NSE")
public class NSE implements StockExchange {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Logger logger = LoggerFactory.getLogger(NSE.class);
	
	private Map<String, String> myData = new HashMap<>();

	@GetMapping("/fetch")
	@ResponseBody
	public String getResult(@RequestParam("value") String val) {
		logger.info("Fetching NSE results");
		return myData.get(val.toLowerCase().trim());
	}
	
	
	
	@PostConstruct
	private void dataLoad() {
		List<Company> lst = jdbcTemplate.query("Select * from company where stocktype = 'NSE'", new CompanyRowMapper());
		Iterator<Company> itr = lst.iterator();
		while(itr.hasNext()) {
			Company c = itr.next(); 
			myData.put(c.getName().toLowerCase(), c.getValue());
			
		}
		
	}
}


