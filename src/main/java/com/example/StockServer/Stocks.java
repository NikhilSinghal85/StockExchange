package com.example.StockServer;


/**
 * This POJO for sending JSON response to client at periodic interval based on input provided
 * @author nikhil.singhal
 *
 */

public class Stocks {

    private String content;
    
    private String message;

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Stocks() {
    }

    public Stocks(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
    	this.content = content;
    }


}
