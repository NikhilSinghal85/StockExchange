package com.example.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.model.User;


/**
 * Database table mapper class it maps to "user" table in database
 * @author nikhil.singhal
 *
 */
public class UserRowMapper implements RowMapper<User>
{
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    	User user = new User();
    	user.setUser(rs.getString("username"));
    	user.setPassword(rs.getString("password"));
        return user;
    }
}
