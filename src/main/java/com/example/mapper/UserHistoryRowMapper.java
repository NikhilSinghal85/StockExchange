package com.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.UserHistoryValues;


/**
 * Database table mapper class it maps to "UserHistoryValues" table in database
 * @author nikhil.singhal
 *
 */
public class UserHistoryRowMapper implements RowMapper<UserHistoryValues>
{
	
    @Override
    public UserHistoryValues mapRow(ResultSet rs, int rowNum) throws SQLException {
    	UserHistoryValues userhistory = new UserHistoryValues();
    	userhistory.setTimestamp(rs.getString("timestamp"));
    	userhistory.setPrice(rs.getString("price"));
    	userhistory.setExchange(rs.getString("exchange_name"));
    	userhistory.setStock(rs.getString("stock_name"));
    	
        return userhistory;
    }
}
