package porfoliodb.app;

import static java.lang.System.out;
import static java.lang.System.err;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import porfoliodb.core.Operazione;
import porfoliodb.core.Portafoglio;
import porfoliodb.db.PorfolioDB;
import porfoliodb.exception.PorfolioException;

public class AppPortafoglio {
	
	private static void statusPortafoglio(Portafoglio portafoglio) throws SQLException {
		
		out.println("** STATUS PORTAFOGLIO **");
		
		out.println("* Disponibilità: " + portafoglio.getDisponibilita());
		out.println("* Prelievo giornaliero: " + portafoglio.getPrelievoGiornaliero());
		out.println("* Lista operazioni: " + portafoglio.getListOperazioni());
		
		out.println("********************************");
	}

	private static void menu() {
		out.println("Esegui operazioni portafoglio:");
		out.println("* Digita 1 per versare;");
		out.println("* Digita 2 per prelevare;");
		out.println("* Digita 3 per stato conto;");
		out.println("* Digita 4 per uscire;");
		out.println("Operazione?");
	}
	
	private static int menuVersamento(Scanner scanner) {
		out.println("Quanto vuoi versare?");
		
		int quantita = 0;
		
		boolean isError = false;
		
		try {
			quantita = scanner.nextInt();
		} catch (InputMismatchException e) {
			isError = true;			
		}
		
		if(isError) {
			out.println("Errore: Devi digitare un numero come quantità da versare");
			return menuVersamento(scanner);
		}
		
		if(quantita < 0) {
			out.println("La quantità deve essere positiva");
			return menuVersamento(scanner);
		}
		
		return quantita;
	}
	
	private static int menuPrelievo(Scanner scanner) {
		out.println("Quanto vuoi prelevare?");
		int quantita = 0;
		
		boolean isError = false;
		
		try {
			quantita = scanner.nextInt();
		} catch (InputMismatchException e) {
			isError = true;			
		}
		
		if(isError) {
			out.println("Errore: Devi digitare un numero come quantità da prelevare");
			return menuPrelievo(scanner);
		}
		
		if(quantita < 0) {
			out.println("La quantità deve essere positiva");
			return menuPrelievo(scanner);
		}
		
		return quantita;
	}
	
	public static void main(String[] args) {
		
		
        List<Operazione> listOperations = null;
        
        
        PorfolioDB porfolioDB = new PorfolioDB("jdbc:h2:~/porfolio_db", 
				"sa", "");
        
        Portafoglio portafoglio = null;
        
        try {
			portafoglio = new Portafoglio(porfolioDB);
		} catch (SQLException e) {
			err.println("Error FATAL: " + e.getMessage() + 
					", SQLState: " + e.getSQLState());
			return;
		}
		
		Scanner scanner = new Scanner(System.in);
		
		menu();
		
		while(true) {
			
			int opzione = 0;
			boolean isError = false;
			
			try {
				opzione = scanner.nextInt();
			} catch (InputMismatchException e) {
				isError = true;
			}
			
			if(isError) {
				out.println("Errore: Devi digitare un numero per scegliere l'opzione");
				menu();
				continue;
			}
			
			if (opzione != 1 && opzione != 2 && opzione != 3 && opzione != 4) {
				out.println("Opzione non valida");
				menu();
				continue;
			}
			
			if(opzione == 3) {
				// status conto
				
				try {
					statusPortafoglio(portafoglio);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				menu();
				continue;
			}
			
			if(opzione == 4) {
				// exit
				out.println("Applicazione terminata");
				break;
			}
			
			int quantita;
			
			switch(opzione) {
				case 1:
					quantita = menuVersamento(scanner);
				try {
					portafoglio.versa(quantita);
					
					out.println("OK versamento: " + quantita);
					
				} catch (SQLException e) {
					err.println("ERRORE VERSAMENTO: " + quantita + 
							" - ERROR: " + e.getMessage() + 
							", SQLState: " + e.getSQLState());
				}
					
					
					break;
				case 2:
					quantita = menuPrelievo(scanner);
					
					try {
						portafoglio.preleva(quantita);
						
						out.println("OK prelievo: " + quantita);
					} catch (PorfolioException e) {
						err.println("ERRORE su prelievo: " + e.getMessage());
					} catch (SQLException e) {
						err.println("ERRORE PRELIEVO: " + quantita + 
								" - ERROR: " + e.getMessage() + 
								", SQLState: " + e.getSQLState());
					}
					
					break;
			}
			
			menu();
			
		}
			

	}

}
