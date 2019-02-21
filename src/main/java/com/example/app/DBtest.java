package com.example.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class DBtest {
	
    public static void main(String[] args) {
    	System.out.println("going to get lock");
    	
        String url      = "jdbc:oracle:thin:@localhost:1521/xe";   //database specific url.
        String user     = "HR";
        String password = "hr";

        try(Connection connection = DriverManager.getConnection(url, user, password)) {
            try(Statement statement = connection.createStatement()){
                String sql = "select * from hr.employees where FIRST_NAME = 'Julia' for update ";
                try(ResultSet result = statement.executeQuery(sql)){
                    while(result.next()) {
                        String firstname = result.getString("First_name");
                        String   lastname  = result.getString  ("Last_name");
                        System.out.println("name is ::" + firstname +  " " + lastname);
                      
                    }
                    System.out.println("going to sleep ...");
                    Thread.sleep(100000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
}
