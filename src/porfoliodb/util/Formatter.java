package porfoliodb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;
import porfoliodb.exception.ParsingFormatException;

public class Formatter {
	
	public static String formatPorfolioEntry(Operazione operazione) {
	    OperationType operationType = operazione.operationType;
	    int quantita = operazione.quantita;
	    long timestamp = operazione.timestamp;
	    
	    Date date = new Date();
		date.setTime(timestamp);
		
		SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yy-hh:mm:ss", 
                Locale.ITALIAN);
		
		String stringDate = formatter.format(date);
		
		return operationType + "|" + quantita + "|" + stringDate;
	}
	
	public static Operazione parsePorfolioEntry(String porfolioEntry) 
			throws ParsingFormatException {
		
		StringTokenizer st = new StringTokenizer(porfolioEntry, "|");
		
		int counter = 0;
		String operationString = null;
		String valueString = null;
		String dateString = null;
		
	    while (st.hasMoreTokens()) {
	       String token = st.nextToken();
	       
	       if(counter == 0)
	    	   operationString = token;
	       else if (counter == 1)
	    	   valueString = token;
	       else if (counter == 2)
	    	   dateString = token;
	       
	       counter++;
	    }
	    
	    if(counter != 3) {
	    	throw new ParsingFormatException("ERROR line: " + porfolioEntry + 
	    			", invalid format - missing token");
	    }
	    
	    //parsing operation type 
	    OperationType operationType = null;
	    
	    if(operationString.equals("VERSAMENTO")) {
	    	operationType = OperationType.VERSAMENTO;
	    } else if(operationString.equals("PRELIEVO")) {
	    	operationType = OperationType.PRELIEVO;
	    } else {
	    	throw new ParsingFormatException("ERROR line: " + porfolioEntry + 
	    			", invalid operation name");
	    }
	    
	    // parsing quantita
	    
	    int quantita;
	    
	    try {
	      quantita = Integer.parseInt(valueString);
	    } catch (NumberFormatException e) {
	    	throw new ParsingFormatException("ERROR line: " + porfolioEntry + 
	    			", invalid format amount value: " + valueString + 
	    			", not integer.");
	    }
	    
	    // parse date
	    SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yy-hh:mm:ss", 
                Locale.ITALIAN);
		
	    long timestamp = -1; 
	    
		try {
			Date date = formatter.parse(dateString);
			timestamp = date.getTime();
		} catch (ParseException e) {
			throw new ParsingFormatException("ERROR line: " + porfolioEntry + 
	    			", invalid format date value: " + dateString);
		}
		
		return new Operazione(operationType, quantita, timestamp);
	}


}
