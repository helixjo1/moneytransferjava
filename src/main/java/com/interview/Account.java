package com.interview;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Account {
	@JsonProperty("id")
    private String id;
	@JsonProperty("balance")
    private BigDecimal balance;
    

    public Account(String id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account() {
    	
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
