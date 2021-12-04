package com.assignment.financial.dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.assignment.financial.config.DBAccessManager;
import com.assignment.financial.exception.AccessSQLException;
import com.assignment.financial.exception.DBConnectionException;
import com.assignment.financial.exception.HashedAlgorithmException;
import com.assignment.financial.model.AccountStatement;
import com.assignment.financial.utils.PBKDF2HashingUtils;

public class StatementDao {
	
	static Logger logger = Logger.getLogger(StatementDao.class);

	public static List<AccountStatement> getAccountStatementResult(Integer accountID) throws AccessSQLException, HashedAlgorithmException, DBConnectionException, IOException {

		DBAccessManager manager = DBAccessManager.getInstance();
		Connection connection = manager.getConnection();
		
		List<AccountStatement> statementBeans = new ArrayList<AccountStatement>();
		
		String sql = "SELECT * FROM account a INNER JOIN statement b on "
				+ "a.id = b.account_id where a.ID ='" + accountID + "'";
		logger.info("Account statement search query : " + sql);
		
		logger.info("Fetching data from DB");
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				AccountStatement statementResult = new AccountStatement();
				statementResult.setId(rs.getInt("ID"));
				statementResult.setAccountNumber(PBKDF2HashingUtils.createHash(rs.getString("account_number").toCharArray()));

				statementResult.setAmount(rs.getString("amount"));
				statementResult.setDateField(rs.getString("datefield"));
				statementBeans.add(statementResult);

			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.error("HashAlgorithm Exception :Error while hashing the account number! ");
			throw new HashedAlgorithmException("HashAlgorithm Exception :Error while hashing the account number! ");
		}catch (SQLException e) {
			logger.error("SQL Exception :Error while executing get account statement query");
			throw new AccessSQLException("SQL Exception :Error while executing SQL query ");
		}
		return statementBeans;

	}
	
}
