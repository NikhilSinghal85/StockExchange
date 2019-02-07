package com.example.DBMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.StockExchange.History;


/**
 * Database table mapper class it maps to "History" table in database
 * @author nikhil.singhal
 *
 */
public class HistoryRowMapper implements RowMapper<History>
{
    @Override
    public History mapRow(ResultSet rs, int rowNum) throws SQLException {
    	History history = new History();
    	try {
    	history.setusername(rs.getString("username"));
    	history.setaverage(rs.getString("average"));
    	}
    	catch (Exception e) {
    		// will have to make new mapper class later 
    		history.setPositionValue(rs.getString("ranking"));
    		history.setPrice(rs.getString("price"));
    	}
    	
        return history;
    }
}
