package porfoliodb.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestUtils {
	
    public static Long[] createListTimestamp(String... timestampStrings) {
		
		List<Long> listTimestamps = new ArrayList<>();
		
		for(String timespampString: timestampStrings) {
			listTimestamps.add(stringToLong(timespampString));
		}
		
		return listTimestamps.toArray(new Long[0]);
	}
	
	public static long stringToLong(String stringDate) {
		// use default format
		return stringToLong("dd.MM.yy-hh:mm:ss", stringDate);
	}
	
	
	public static long stringToLong(String format, String stringDate) {
		
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
	
	public static Date stringToDate(String stringDate) {
		
		// use default format
		return stringToDate("dd.MM.yy-hh:mm:ss", stringDate);
	}
	
	public static Date stringToDate(String format, String stringDate) {
		
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
