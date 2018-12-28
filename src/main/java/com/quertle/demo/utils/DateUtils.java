package com.quertle.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class contains all the date related methods
 * 
 * @author SUJAN
 *
 */
public class DateUtils {

	public static final String GENERAL_DATE_FORMAT = "YYYY-MM-dd";

	/**
	 * This method converts the year, month and day to java.util.Date.
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 * @throws ParseException 
	 */
	public static Date getDate(String year, String month, String day){
		DateFormat format = new SimpleDateFormat(GENERAL_DATE_FORMAT);
		Date givenDate = null;
		try {
			givenDate = format.parse(year+"-"+month+"-"+day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return givenDate;
	}

}
