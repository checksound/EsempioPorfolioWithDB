package porfoliodb.exception;

/**
 * 
 *
 */
public class SingleWithdrawnLimitException extends PorfolioException {
	
	private final int limit;
	
	public SingleWithdrawnLimitException(int amount, int limit) {
		super(amount);
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "SingleWithdrawnLimitException [amount=" + amount + ", limit=" + limit + "]";
	}
	
	
}
