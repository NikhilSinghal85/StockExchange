package com.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.Company;


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
    	company.setName(rs.getString("company_name"));
    	company.setValue(rs.getString("company_value"));
    	company.setStockType(rs.getString("company_stock"));
        return company;
    }
}
