package porfoliodb.exception;

/**
 * Eccezione generica del portafoglio
 * 
 * @see AmountWithdrawnException
 * @see SingleWithdrawnLimitException
 * @see DailyWithdrawnLimitException
 */

public abstract class PorfolioException extends Exception {
	
	protected final int amount;
	
	/**
	 * Costruttore eccezione generica per portafoglio 
	 * 
	 * @param amount - valore del prelievo che ha generato l'eccezione
	 * 
	 */
	public PorfolioException(int amount) {
		this.amount = amount;
	}
	
	/**
	 * Ritorna il valore dell'attributo amount
	 * 
	 * @return la quantità che si cercava di prelevare
	 */
	public int getAmount() {
		return amount;
	}

	@Override
	public String getMessage() {
		return toString();
	}
}

