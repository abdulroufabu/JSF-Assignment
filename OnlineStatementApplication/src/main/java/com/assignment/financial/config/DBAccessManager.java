package com.assignment.financial.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.assignment.financial.exception.DBConnectionException;

public class DBAccessManager {

	private static DBAccessManager instance= null;
	private static Connection connection = null;
	static Logger logger = Logger.getLogger(DBAccessManager.class);
	
	private DBAccessManager() throws DBConnectionException, IOException {
	
	}
	
	public static DBAccessManager getInstance() throws DBConnectionException, IOException {
		if (instance == null) {
			instance = new DBAccessManager();
			initalizeDBConnection();
		}
		return instance;
		
	}
	
	public Connection getConnection() throws DBConnectionException {
		if(connection == null) {
			logger.error("Database Connection : Connection is not yet established!");
			throw new DBConnectionException("Database Connection : Connection is not yet established!");
		}
	    return connection;
	
	}
	private static void initalizeDBConnection () throws DBConnectionException, IOException {

		try {
				Properties p = new Properties();
				InputStream in = instance.getClass().getClassLoader().getResourceAsStream("dbConnection.properties");
	            p.load(in);
	            in.close();

	            String driver = p.getProperty("driverClassName").trim();
	            String dbUrl = p.getProperty("db-url").trim();

	            Class.forName(driver);
	            String url= "jdbc:ucanaccess://" + dbUrl;
				
				connection = DriverManager.getConnection(url);
				
			} catch (ClassNotFoundException e) {
				logger.error("DB Connection :UcanaccessDriver is not defined, check MS driver");
				throw new DBConnectionException("DB Connection :UcanaccessDriver is not defined");
			} 
			catch (SQLException e) {
				logger.error("DB Connection : Error connecting to DB!");
				throw new DBConnectionException("DB Connection : Error connecting to DB!");
			}
			
		
	  
	}
	
}
