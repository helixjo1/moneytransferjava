package com.interview;


import java.math.BigDecimal;

import java.util.Calendar;
import java.util.TimeZone;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"timeStamp"})
public class Transfer {
	
		public static final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	 	@JsonProperty("originatorId")
	    private String originatorId;
	 	@JsonProperty("destinationId")
	    private String destinationId;
	 	@JsonProperty("amount")
	    private BigDecimal amount;
	 	@JsonProperty("result")
	 	private String result;
	 	@JsonProperty("message")
	 	private String message;
	 	@JsonProperty("error")
	 	private String error;
	 	private String user_id;

	 	
	    public Transfer() {
	    	
	    }

	    //constructor for ResultsMapper
	    public Transfer(String orig, String dest, BigDecimal am, String r, String e, String m, String u) {
	   
	    	this.originatorId = orig;
	      	this.destinationId = dest;
	      	this.amount = am;
	      	this.result = r;
	      	this.message = m;
	      	this.error = e;
	      	this.user_id = u;
	    }
	    

	    
	    public String user() {
	    	return user_id;
	    }
	   
	    
	    public void setUser(String u) {
	    	user_id = u;
	    }
	    
	    public String originatorId() {
	        return originatorId;
	    }

	    public String destinationId() {
	        return destinationId;
	    }
	    
	    public void setOriginatorId(String id) {
	    	originatorId = id;
	    }
	    
	    public String result() {
	    	return result;
	    }
	    
	    public String message() {
	    	return message;
	    }
	    
	    public String error() {
	    	return error;
	    }
	    
	    public BigDecimal amount() {
	        return amount;
	    }

	    public void setResult(String r, String m) {
	    	result = r;
	    	message = m;
	    }
	    
	    public void setError(String e) {
	    	error = e;
	    	
	    }
	  
}
