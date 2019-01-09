package com.example.demo;


/**
 * This is the POGO for JSON mapping of all incoming data from client.
 * @author nikhil.singhal
 *
 */
public class StockMessage {

    private String name;
    
    
    private String value;

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StockMessage() {
    }

    public StockMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}