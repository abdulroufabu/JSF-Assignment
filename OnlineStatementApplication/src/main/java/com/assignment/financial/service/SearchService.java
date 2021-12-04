package com.assignment.financial.service;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.assignment.financial.dao.StatementDao;
import com.assignment.financial.exception.AccessSQLException;
import com.assignment.financial.exception.DBConnectionException;
import com.assignment.financial.exception.HashedAlgorithmException;
import com.assignment.financial.exception.InvalidParameterException;
import com.assignment.financial.exception.UnauthorizedException;
import com.assignment.financial.model.AccountStatement;
import com.assignment.financial.model.SearchParameters;
import com.assignment.financial.utils.AccountUtil;
import com.assignment.financial.utils.LoginUtils.Role;
import com.assignment.financial.utils.SessionUtils;
import com.assignment.financial.utils.Utils;

public class SearchService {
	
	public static Logger smLogger = Logger.getLogger(SearchService.class);

	private static SearchService instance;
	
	private SearchService() {
	}

	public static SearchService getInstance() {
		if (instance == null) {
			instance = new SearchService();
		}
		return instance;
	}
	
	
	public List<AccountStatement> searchAccountData(Integer accountID, List<LocalDate> ranges, Double fromAmount,
			Double toAmount) throws AccessSQLException, DBConnectionException, IOException, HashedAlgorithmException, InvalidParameterException, UnauthorizedException {
		
		//Set the search parameters
		SearchParameters searchParams = setSearchParamBean(accountID, ranges, fromAmount, toAmount);
		smLogger.info(String.format("User searched the account statement with params [%s]!", searchParams.toString()));
		
		List<Role> userRoles = SessionUtils.getUserRoles();
		AccountUtil.validateSearchParameters(searchParams, userRoles);
		
		List<AccountStatement> stmtsFromDB = StatementDao.getAccountStatementResult(accountID);
		
		LocalDate fromDate = null;
		LocalDate toDate = null;
		if (ranges == null || ranges.isEmpty()) {
			if (fromAmount == null && toAmount == null) {
				//If the request does not specify any parameter, then the search will return three months back statement.
				fromDate = Utils.convertStringToDate(LocalDate.now().minusMonths(3).toString());
				toDate = Utils.convertStringToDate(LocalDate.now().toString());
				smLogger.info("User searched the account statement without specify any parameter, the search will return three months back statement");
			}
		} else if (ranges.size() == 2) {
			//request specified from date and to date (the date range).
			fromDate = ranges.get(0);
			toDate = ranges.get(1);
		}
		
		List<AccountStatement> finalStmtList = new ArrayList<>();
		if(null != stmtsFromDB && !stmtsFromDB.isEmpty()) {
			
			for(AccountStatement acc : stmtsFromDB) {
				
				if (fromAmount != null && toAmount != null) {
					Double dbAmount = acc.getAmount();
					if((dbAmount < fromAmount) || (dbAmount > toAmount)){
						continue;
					}
				}
				LocalDate accountDate = acc.getDateField();
				if (fromDate != null && toDate != null) {
					if((accountDate.isBefore(fromDate)) || (accountDate).isAfter(toDate)) {
						continue;
					}
				}
				finalStmtList.add(acc);
			}
			
		} else {
			throw new InvalidParameterException("No data found for Account ID!");
		}
		
		return finalStmtList;
	}
	
	private SearchParameters setSearchParamBean(Integer accountID, List<LocalDate> ranges, Double fromAmount,
			Double toAmount) {
		
		SearchParameters params = new SearchParameters();
		params.setAccountId(accountID == null ? 0 : accountID);
		params.setFromAmount(fromAmount);
		params.setToAmount(toAmount);
		if (ranges != null && ranges.size() == 2) {
			params.setFromDate(ranges.get(0));
			params.setToDate(ranges.get(1));
		}
		return params;
	}
}