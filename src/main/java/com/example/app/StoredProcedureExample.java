package com.example.app;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StoredProcedureExample {

	public static void main(String[] args) {
		CallableStatement cstmt = null;
		try {
			
			String URL = "jdbc:oracle:thin:@localhost:1521/xe";
			String USER = "SYSTEM";
			String PASS = "password@1";
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			
		

			String SQL = "{call hr.getEmpName (?, ?)}";
			cstmt = conn.prepareCall (SQL);
			cstmt.setInt(1, 198);
			// register out paramater to read from result after executing query its position and datatype
			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
			cstmt.executeUpdate();
			// fetching the previously registered parameter at position 2
			String name = cstmt.getString(2);
			System.out.println("Name at position 198 is ::" + name);
		}
		catch (SQLException e) {
			System.out.println("exception::::" +e.getMessage());
		}
		finally {
			 try {
				cstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
