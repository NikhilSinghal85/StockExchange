package excelPojo;

import annotations.ExcelColumnName;

public class ExcelEmployee {
	
	
	@ExcelColumnName( name = "Last Name")
	private String lastName ;
	
	
	@ExcelColumnName( name = "First Name")
	private String firstName;
	
	@ExcelColumnName( name = "User Id")
	private Integer id;
	
	@ExcelColumnName( name = "Type")
	private String type;
	
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
