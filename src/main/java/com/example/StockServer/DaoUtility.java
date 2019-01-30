package com.example.StockServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DaoUtility {
	
	@Autowired
	private NamedParameterJdbcTemplate  namedParameterJdbcTemplate;
	

}
