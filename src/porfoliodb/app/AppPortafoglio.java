package porfoliodb.app;

import static java.lang.System.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.err;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
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
		
		String pathConfigFile = "./conf/config.properties";
		
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream(pathConfigFile));
		} catch (FileNotFoundException e1) {
			err.println("Error loading config file: " + 
					e1.getMessage());
			return;
		} catch (IOException e1) {
			err.println("Error loading config file: " + 
					e1.getMessage());
			return;
		}
		
		out.println("Caricate le configurazioni da file: " + 
				(new File(pathConfigFile)).getAbsolutePath());

		String dbURL = prop.getProperty("dbURL");
		String dbUSERNAME = prop.getProperty("dbUSERNAME");
		String dbPASSWORD = prop.getProperty("dbPASSWORD");
        
		if(dbUSERNAME == null)
        	dbUSERNAME = "";
        
        if(dbPASSWORD == null)
        	dbPASSWORD = "";
        
		out.println("* dbURL:" + dbURL);
		out.println("* dbUSERNAME: " + dbUSERNAME);
		out.println("* dbPASSWORD: " + dbPASSWORD);
		out.println("*");
		out.println("*****************************");
		
        List<Operazione> listOperations = null;
        
        PorfolioDB porfolioDB = new PorfolioDB(dbURL, 
        		dbUSERNAME, dbPASSWORD);
        
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
					err.println("Error checking status portafolio: " + e.getMessage() + 
							", SQLState: " + e.getSQLState());
				}
				menu();
				continue;
			}
			
			if(opzione == 4) {
				// exit
				try {
					porfolioDB.closeConnection();
				} catch (SQLException e) {
					err.println("Error closing connectio: " + 
							e.getMessage() + 
							", SQLState: " + e.getSQLState());
				}
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
