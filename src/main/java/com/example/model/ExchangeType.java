package com.example.model;

public enum ExchangeType {
	
        NSE(1), BSE(2); 
	
	
	 private int ExchangeType;

	 ExchangeType(int id){
	    this.ExchangeType = id;
	  }

	  public int getExchangeType(){
	    return ExchangeType;
	  }
}



