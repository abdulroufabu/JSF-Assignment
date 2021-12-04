package com.assignment.financial.model;

import java.time.LocalDate;

import com.assignment.financial.utils.Utils;

public class AccountStatement {

	private int id;
	private String accountNumber;
	private LocalDate dateField;
	private Double amount;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public LocalDate getDateField() {
		return dateField;
	}

	public void setDateField(String date) {
		LocalDate dateField = Utils.convertDBStringToDate(date);
		this.dateField = dateField;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		double d = Double.parseDouble(amount);
	    double roundDbl = Math.round(d*100.0)/100.0;
		this.amount = roundDbl;
	}
	
}
