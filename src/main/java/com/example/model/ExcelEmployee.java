package com.example.model;

import org.springframework.stereotype.Component;

/**
 * POJO class for database table ExcelEmployee
 * @author nikhil.singhal
 *
 */
@Component
public class ExcelEmployee {


	private Integer userId;
	
	private String firstName;

	private String lastName;
	
	private String type;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	

	




}
