package com.assignment.financial.utils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.assignment.financial.exception.InvalidParameterException;
import com.assignment.financial.exception.UnauthorizedException;
import com.assignment.financial.model.SearchParameters;
import com.assignment.financial.utils.LoginUtils.Feature;
import com.assignment.financial.utils.LoginUtils.Role;

/**
 * Utilities related to Account functionality.
 * @author abdul.abu
 *
 */
public class AccountUtil {
	
	static Logger smLogger = Logger.getLogger(AccountUtil.class);
	
	public static void validateSearchParameters(SearchParameters parameters, List<Role>userRoles ) throws InvalidParameterException, UnauthorizedException {
		
		StringBuilder errorMessage =new StringBuilder("");
		
		HashSet<Feature> features = LoginUtils.getFeatures(userRoles);
		
		validateAccountIdFilter(parameters,features, errorMessage);
		
		validateAmountFilter(parameters,features, errorMessage);
	
		validateDateFilter(parameters,features, errorMessage);
		
		if(!errorMessage.toString().isEmpty()) {
			smLogger.error("Invalid input search parameters!");
			throw new InvalidParameterException (errorMessage.toString());
		}
		
	}
	
	private static StringBuilder  validateAccountIdFilter(SearchParameters parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		int accountId = parameters.getAccountId();
		
		if(!features.contains(Feature.SEARCH_BY_ACCOUNT_ID) && accountId != 0) {
			String msg= "User does not have the access to filter by account ID!";
			smLogger.error(msg);
			throw new UnauthorizedException(msg);
		}
		
		if(accountId == 0) {
			errorMessage.append("Please search with a valid account ID \n");
			
		}
		return errorMessage;
	}


	private static StringBuilder validateDateFilter(SearchParameters parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		LocalDate fromDate = parameters.getFromDate();
		LocalDate toDate = parameters.getToDate();
		
		if(!features.contains(Feature.SEARCH_BY_DATE) && (fromDate != null || toDate != null) ) {
			String msg = "User does not have the access to filter by dates";
			smLogger.error(msg);
			throw new UnauthorizedException(msg);
		}
		if(fromDate!=null && toDate== null || (fromDate==null && toDate!= null)) {
			errorMessage.append("The Date filter is not valid, please fill the date range \n");
		}
		return errorMessage;
	}


	private static StringBuilder validateAmountFilter(SearchParameters parameters,HashSet<Feature> features, StringBuilder errorMessage) throws UnauthorizedException {
		
		Double fromAmount  = parameters.getFromAmount();
		Double toAmount  = parameters.getToAmount();
		
		if(!features.contains(Feature.SEARCH_BY_AMOUNT) && (fromAmount != null || toAmount != null) ) {
			String msg = "User does not have the access to filter by amounts.";
			smLogger.error(msg);
			throw new UnauthorizedException(msg);
		}
		
		if(fromAmount!=null && toAmount !=null && (fromAmount > toAmount)) {
			errorMessage.append("The amount filter is not valid, Kindly check the amount range\n");
			
		}
		
		if((fromAmount!=null && toAmount== null) || (fromAmount== null  && toAmount!=null)) {
			errorMessage.append("The amount filter is not valid, please fill the amount range \n");
		}
		return errorMessage;
	}
	
	

}
