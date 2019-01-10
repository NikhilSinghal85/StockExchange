package com.example.demo;

import java.util.Collection;
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
		List<Map<String, Object>> lst = jdbcTemplate.queryForList("Select * from company where stocktype = 'NSE'");
		Iterator<Map<String, Object>> itr = lst.iterator();
		while(itr.hasNext()) {
			Map<String, Object> pp = itr.next();
			Collection<Object> vv = pp.values();
			Iterator<Object> itr2 = vv.iterator();
			String key = null;
			String val = null;
			int count = 0;
			while (itr2.hasNext()) {
				if (count == 1) {
					key = (String)itr2.next();
				}
				else if(count ==2) {
					val = (String)itr2.next();
				}
				else {
					itr2.next();
				}
				count++;
				
			}
			myData.put(key.toLowerCase(), val);
			
		}
		
	}
}
