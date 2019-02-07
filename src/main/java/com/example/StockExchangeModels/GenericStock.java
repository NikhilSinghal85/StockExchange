package com.example.StockExchangeModels;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.DBMappers.CompanyRowMapper;


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
		List<Company> lst = namedParameterJdbcTemplate.query("Select * from company ", new CompanyRowMapper());
		Iterator<Company> itr = lst.iterator();
		while(itr.hasNext()) {
			Company c = itr.next(); 
			myData.add(c);
			
		}
		
	}
}


