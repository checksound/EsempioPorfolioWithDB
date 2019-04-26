package porfoliodb.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import porfoliodb.exception.AmountWithdrawnException;
import porfoliodb.exception.DailyWithdrawnLimitException;
import porfoliodb.exception.PorfolioException;
import porfoliodb.exception.SingleWithdrawnLimitException;

public class Portafoglio {
	
	public static final long RANGE = 2 * 60 * 1000;
	
	private final List<Operazione> listOperazioni = new ArrayList<Operazione>();
	
	private int disponibilita;
	
	public final int SINGLE_WITHDRAWAL_LIMIT;
	public final int DAYLY_WITHDRAWAL_LIMIT;
	
	public Portafoglio() {
		this(new ArrayList<Operazione>(), 500, 1500);
	}
	
	public Portafoglio(List<Operazione> listOperazioni) {
		this(listOperazioni, 500, 1500);
	}
	
	public Portafoglio(List<Operazione> listOperazioni, 
			int singleWithdrawalLimit, int daylyWithdrawalLimit) {
		
		for (Operazione operazione: listOperazioni) {
			if(operazione.operationType == OperationType.VERSAMENTO) {
				disponibilita += operazione.quantita;
			} else if(operazione.operationType == OperationType.PRELIEVO) {
				disponibilita -= operazione.quantita;
			}	
			
			this.listOperazioni.add(operazione);
		}
		
		this.SINGLE_WITHDRAWAL_LIMIT = singleWithdrawalLimit;
		this.DAYLY_WITHDRAWAL_LIMIT = daylyWithdrawalLimit;
		
		
	}
	
	
	/**
	 * 
	 * @param now - timestamp del momento attuale 
	 * @return - la somma dei prelievi nel RANGE rispetto al momento attuale
	 */
	private int getSumPrelievoInRange(long now) {
		int prelievoInRange = 0;
		
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
	 */
	public int versa(int amount) {
		this.listOperazioni.add(
				new Operazione(OperationType.VERSAMENTO, amount, System.currentTimeMillis()));
		
		this.disponibilita += amount;
		return this.disponibilita;
	}
	
	/**
	 * 
	 * @param amount
	 * @return la disponibilità ad operazione avvenuta
	 * @throws PorfolioException se la disponibilità è < della richiesta di 
	 * prelievo.
	 * 
	 */
	public int preleva(int amount) throws PorfolioException {
		
		if(this.SINGLE_WITHDRAWAL_LIMIT< amount)
			throw new SingleWithdrawnLimitException(amount, this.SINGLE_WITHDRAWAL_LIMIT);
		
		if(this.DAYLY_WITHDRAWAL_LIMIT < 
				getSumPrelievoInRange(System.currentTimeMillis()) + amount)
			throw new DailyWithdrawnLimitException(amount, getPrelievoGiornaliero());
		
		if(this.disponibilita < amount) 
			throw new AmountWithdrawnException(amount, this.disponibilita);
		
		this.disponibilita -= amount;
		this.listOperazioni.add(
				new Operazione(OperationType.PRELIEVO, amount, System.currentTimeMillis()));
		return this.disponibilita;
	}
	
	public int getDisponibilita() {
		return this.disponibilita;
	}
	
	public int getPrelievoGiornaliero() {
		return getSumPrelievoInRange(System.currentTimeMillis());
	}

	public List<Operazione> getListOperazioni() {
		return Collections.unmodifiableList(listOperazioni);
	}

}