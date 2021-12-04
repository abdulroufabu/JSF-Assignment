package com.assignment.financial.model;

import java.time.LocalDate;

public class SearchParameters {

	private int accountId;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Double fromAmount;
	private Double toAmount;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
	public Double getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount;
	}
	
	public Double getToAmount() {
		return toAmount;
	}
	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount;
	}
	
	@Override
	public String toString() {
		return "SearchParameters [accountId=" + accountId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", fromAmount=" + fromAmount + ", toAmount=" + toAmount + "]";
	}
	
	
}
