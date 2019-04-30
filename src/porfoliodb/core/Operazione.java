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
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if(!(obj instanceof Operazione))
			return false;
		
		Operazione op = (Operazione) obj;
		
		if(this.operationType != op.operationType)
			return false;
		
		if(this.quantita != op.quantita)
			return false;
		
		if(this.timestamp != op.timestamp)
			return false;
		return true;
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
