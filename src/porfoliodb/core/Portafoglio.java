package porfoliodb.core;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import porfoliodb.db.PorfolioDB;
import porfoliodb.exception.AmountWithdrawnException;
import porfoliodb.exception.DailyWithdrawnLimitException;
import porfoliodb.exception.PorfolioException;
import porfoliodb.exception.SingleWithdrawnLimitException;

public class Portafoglio {
	
	public static final long RANGE = 2 * 60 * 1000;
	
	private PorfolioDB db;
	private int disponibilita;
	
	public final int SINGLE_WITHDRAWAL_LIMIT;
	public final int DAYLY_WITHDRAWAL_LIMIT;
	
	public Portafoglio(PorfolioDB db) throws SQLException {
		this(db, 500, 1500);
	}
	
	public Portafoglio(PorfolioDB db, 
			int singleWithdrawalLimit, int daylyWithdrawalLimit) throws SQLException {
		
		this.db = db;
		this.SINGLE_WITHDRAWAL_LIMIT = singleWithdrawalLimit;
		this.DAYLY_WITHDRAWAL_LIMIT = daylyWithdrawalLimit;
				
		List<Operazione> listOperazioni = db.getOperazioni();
		
		for (Operazione operazione: listOperazioni) {
			if(operazione.operationType == OperationType.VERSAMENTO) {
				disponibilita += operazione.quantita;
			} else if(operazione.operationType == OperationType.PRELIEVO) {
				disponibilita -= operazione.quantita;
			}	
			
		}
			
		
	}
	
	
	/**
	 * 
	 * @param now - timestamp del momento attuale 
	 * @return - la somma dei prelievi nel RANGE rispetto al momento attuale
	 * @throws SQLException 
	 */
	private int getSumPrelievoInRange(long now) throws SQLException {
		int prelievoInRange = 0;
		
		List<Operazione> listOperazioni = db.getOperazioni();
		
		ListIterator<Operazione> lIterator = 
				listOperazioni.listIterator(listOperazioni.size());
		while(lIterator.hasPrevious()) {
			Operazione elem = lIterator.previous();
			if(elem.timestamp < now - RANGE) {
				break;
			}
			
			if(elem.operationType == OperationType.PRELIEVO)
				prelievoInRange += elem.quantita;
		}
		return prelievoInRange;
	}
	/**
	 * 
	 * @param amount
	 * @return la disponibilità ad operazione avvenuta
	 * @throws SQLException 
	 */
	public int versa(int amount) throws SQLException {
		
		db.insertOperazione(new Operazione(OperationType.VERSAMENTO, 
				amount, System.currentTimeMillis()));
		
		this.disponibilita += amount;
		return this.disponibilita;
	}
	
	/**
	 * 
	 * @param amount
	 * @return la disponibilità ad operazione avvenuta
	 * @throws PorfolioException se la disponibilità è < della richiesta di 
	 * prelievo.
	 * @throws SQLException 
	 * 
	 */
	public int preleva(int amount) throws PorfolioException, SQLException {
		
		if(this.SINGLE_WITHDRAWAL_LIMIT< amount)
			throw new SingleWithdrawnLimitException(amount, this.SINGLE_WITHDRAWAL_LIMIT);
		
		int currentPrelievoGiornaliero = getPrelievoGiornaliero();
	
		if(this.DAYLY_WITHDRAWAL_LIMIT < currentPrelievoGiornaliero + amount)
			throw new DailyWithdrawnLimitException(amount, currentPrelievoGiornaliero);
		
		if(this.disponibilita < amount) 
			throw new AmountWithdrawnException(amount, this.disponibilita);
		
		db.insertOperazione(new Operazione(OperationType.PRELIEVO, 
				amount, System.currentTimeMillis()));
		
		this.disponibilita -= amount;
				
		return this.disponibilita;
	}
	
	public int getDisponibilita() {
		return this.disponibilita;
	}
	
	public int getPrelievoGiornaliero() throws SQLException {
		return getSumPrelievoInRange(System.currentTimeMillis());
	}

	public List<Operazione> getListOperazioni() throws SQLException {
		List<Operazione> listOperazioni = db.getOperazioni();
		
		return Collections.unmodifiableList(listOperazioni);
	}

}