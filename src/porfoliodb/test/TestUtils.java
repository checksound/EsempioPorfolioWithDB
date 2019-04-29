package porfoliodb.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestUtils {
	
	static long stringToLong(String stringDate) {
		// use default format
		return stringToLong("dd.MM.yy-hh:mm:ss", stringDate);
	}
	
	
	static long stringToLong(String format, String stringDate) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(
                format, 
                Locale.ITALIAN);
		
		Date date = null;
		
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {
			System.err.println("Error parsing " + stringDate + 
					", MSG: " + e.getMessage());
		}
		return (date!= null)? date.getTime(): -1;
	}
	
	static Date stringToDate(String stringDate) {
		
		// use default format
		return stringToDate("dd.MM.yy-hh:mm:ss", stringDate);
	}
	
	static Date stringToDate(String format, String stringDate) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(
                format, 
                Locale.ITALIAN);
		
		Date date = null;
		
		try {
			date = formatter.parse(stringDate);
		} catch (ParseException e) {
			System.err.println("Error parsing " + stringDate + 
					", MSG: " + e.getMessage());
		}
		return date;
	}

}
