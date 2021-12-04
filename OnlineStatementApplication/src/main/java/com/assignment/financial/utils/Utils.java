package com.assignment.financial.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author abdul.abu
 *
 */
public class Utils {

	public final static boolean isVoid(String s) {
		return ((s == null) || s.isEmpty());
	}

	public final static LocalDate convertDBStringToDate(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate convertedDate = LocalDate.parse(date.replace(".", "-"), formatter);
		return convertedDate;
	}

	public final static LocalDate convertStringToDate(String date) {
		return LocalDate.parse(date);
	}

}
