package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * Database table mapper class it maps to "Company" table in database
 * @author nikhil.singhal
 *
 */
public class CompanyRowMapper implements RowMapper<Company>
{
    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Company company = new Company();
    	company.setName(rs.getString("Name"));
    	company.setValue(rs.getString("value"));
    	company.setStockType(rs.getString("StockType"));
        return company;
    }
}
