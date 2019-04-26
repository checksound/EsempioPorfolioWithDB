package porfoliodb.exception;

/**
 * 
 *
 */
public class DailyWithdrawnLimitException extends PorfolioException {
	
	private final int total;
	
	public DailyWithdrawnLimitException(int amount, int total) {
		super(amount);
		this.total = total;
	}
	
	/**
	 * 
	 * @return il totale già prelevato in giornata
	 */
	public int getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return "DailyLimitException [amount=" + amount + ", total=" + total + "]";
	}
	
	
}
