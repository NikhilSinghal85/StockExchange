package com.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.ExcelEmployee;


/**
 * Database table mapper class it maps to "user" table in database
 * @author nikhil.singhal
 *
 */
public class ExcelEmployeeRowMapper implements RowMapper<ExcelEmployee>
{
    @Override
    public ExcelEmployee mapRow(ResultSet rs, int rowNum) throws SQLException {
    	ExcelEmployee emp = new ExcelEmployee();
    	emp.setUserId(rs.getInt("userid"));
    	emp.setFirstName(rs.getString("firstname"));
    	emp.setLastName(rs.getString("lastname"));
    	emp.setType(rs.getString("typeemp"));
        return emp;
    }
}
