package porfoliodb.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestUtils {
	
	// "dd.MM.yy-hh:mm:ss"
	long stringToLong(String format, String stringDate) {
		
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

}
