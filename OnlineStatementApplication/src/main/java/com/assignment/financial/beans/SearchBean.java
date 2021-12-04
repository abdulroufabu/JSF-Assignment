/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.financial.beans;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.assignment.financial.exception.AccessSQLException;
import com.assignment.financial.exception.DBConnectionException;
import com.assignment.financial.exception.HashedAlgorithmException;
import com.assignment.financial.exception.InvalidParameterException;
import com.assignment.financial.exception.UnauthorizedException;
import com.assignment.financial.model.AccountStatement;
import com.assignment.financial.service.SearchService;

@ManagedBean(name = "searchBean")
@ViewScoped
public class SearchBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4532094093118257037L;
	
	static Logger smLogger = Logger.getLogger(SearchBean.class);

	private Integer accountId;
	private Double fromAmount;
	private Double toAmount;
	private List<LocalDate> range;
	
	private List<AccountStatement> statementList;
	
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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
	public List<LocalDate> getRange() {
		return range;
	}
	public void setRange(List<LocalDate> range) {
		this.range = range;
	}
	
	public List<AccountStatement> getStatementList() {
		return statementList;
	}

	public void setStatementList(List<AccountStatement> statementList) {
		this.statementList = statementList;
	}
	
	
	public void click() {
        PrimeFaces.current().ajax().update("form:display");
        PrimeFaces.current().executeScript("PF('dlg').show()");
    }
	
	private SearchService service;
	
	@PostConstruct
    public void init() {
		service = SearchService.getInstance();
    }

	
	public void search() {

		try {
			
			List<AccountStatement> result = service.searchAccountData(accountId,range, fromAmount, toAmount);
			setStatementList(result);

		} catch (ViewExpiredException e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Session Expired:",
					"Please do login again.")); 
			
		} catch (HashedAlgorithmException e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Error in Hashing account number:",
					"" + e.getMessage())); 
			
		} catch (UnauthorizedException e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Unauthorized Access:",
					"" +e.getMessage()));
			
		} catch (InvalidParameterException | NumberFormatException e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Invalid searching inputs:",
					"" + e.getMessage()));      
			
		} catch (AccessSQLException | DBConnectionException | IOException e) {
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Database Error:",
					"" + e.getMessage()));      
		}
	}

	public SearchBean() {
	}

}
