package com.example.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.constant.QueryConstants;
import com.example.mapper.ExcelEmployeeRowMapper;
import com.example.model.ExcelEmployee;

public class DaoExcelDataFetcher<T> {
	
	
	public static List fetchResult(String cls, NamedParameterJdbcTemplate namedParameterJdbcTemplate ) {
		
		
		
		if (cls.equalsIgnoreCase("ExcelEmployee")){
			Map<String, Object> ss = new HashMap<>();
			List<ExcelEmployee> resultSetEmployee = namedParameterJdbcTemplate.query(QueryConstants.FETCHEMPLOYEES, ss, new ExcelEmployeeRowMapper());
			return resultSetEmployee;
		}
		else {
			//later
			return null;
		}
	}

}
