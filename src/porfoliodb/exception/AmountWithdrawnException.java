package porfoliodb.exception;

/**
 * 
 *
 */
public class AmountWithdrawnException extends PorfolioException {
	
	private final int balance;
	
	public AmountWithdrawnException(int amount, int balance) {
		super(amount);
		this.balance = balance;
	}
	
	public int getBalance() {
		return balance;
	}


	@Override
	public String toString() {
		return "AmountWithdrawnException [amount=" + amount + ", balance=" + balance + "]";
	}
	
}
