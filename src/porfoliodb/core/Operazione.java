package porfoliodb.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Operazione {
	
	public final OperationType operationType;
	public final int quantita;
	public final long timestamp;
	
	public Operazione(OperationType operationType, int quantita, long timestamp) {
		this.operationType = operationType;
		this.quantita = quantita;
		this.timestamp = timestamp;
	}
	
	

	@Override
	public String toString() {
		Date date = new Date();
		date.setTime(timestamp);
		
		SimpleDateFormat formatter = new SimpleDateFormat(
                "dd.MM.yy-HH:mm:ss", 
                Locale.ITALIAN);
		
		return "OPERAZIONE [ tipo=" + operationType + ", qt=" + quantita + ", dt='" + formatter.format(date) + "']";
	}
	
	

}
