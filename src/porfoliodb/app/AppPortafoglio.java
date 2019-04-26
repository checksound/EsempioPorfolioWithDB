package porfoliodb.app;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import porfoliodb.core.Operazione;
import porfoliodb.core.Portafoglio;
import porfoliodb.exception.ParsingFormatException;
import porfoliodb.exception.PorfolioException;
import porfoliodb.util.PorfolioOnFile;

public class AppPortafoglio {
	
	private static boolean isPorfolioModified = false;
	
	private static String DATA_FILE_NAME = ".porfolio";
	
	private static void statusPortafoglio(Portafoglio portafoglio) {
		
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
		
		/* Create a dataFile variable of type File to represent the
         * data file that is stored in the user's home directory.
         */

        File userHomeDirectory = new File( System.getProperty("user.home") );
        File dataFile = new File( userHomeDirectory, DATA_FILE_NAME );
      
        List<Operazione> listOperations = null;
        
        PorfolioOnFile dataPorfolioFile = 
        		new PorfolioOnFile(dataFile);
        
        if (!dataFile.exists()) {
            out.println("No porfolio data file found.  A new one");
            out.println("will be created, if you add any entries.");
            out.println("File name:  " + dataFile.getAbsolutePath());
        }
        else {
            out.println("Reading porfolio data...");
            
            try {
    			listOperations = dataPorfolioFile.readFromFile();
    		} catch (IOException e1) {
    			System.err.println("IOException: " + e1.getMessage());
    		} catch (ParsingFormatException e1) {
    			System.err.println("ParsingFormatException: " + e1.getMessage());
    		}
        }
        
		Portafoglio portafoglio = null;
		
		if(listOperations == null)
			portafoglio = new Portafoglio();
		else
			portafoglio = new Portafoglio(listOperations);
		
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
				statusPortafoglio(portafoglio);
				menu();
				continue;
			}
			
			if(opzione == 4) {
				// exit
				
				if(isPorfolioModified) {
					out.println("SALVATAGGIO DATI");
					try {
						dataPorfolioFile.writeToFile(portafoglio.getListOperazioni());
					} catch (IOException e) {
						System.err.println("IOException saving - " 
								+ e.getMessage());						
					}
				}
				out.println("Applicazione terminata");
				break;
			}
			
			int quantita;
			
			switch(opzione) {
				case 1:
					quantita = menuVersamento(scanner);
					portafoglio.versa(quantita);
					
					isPorfolioModified = true;
					
					out.println("OK versamento: " + quantita);
					break;
				case 2:
					quantita = menuPrelievo(scanner);
					
					try {
						portafoglio.preleva(quantita);
						
						isPorfolioModified = true;
						
						out.println("OK prelievo: " + quantita);
					} catch (PorfolioException e) {
						System.err.println("ERRORE su prelievo: " + e.getMessage());
					} 
					
					break;
			}
			
			menu();
			
		}
			

	}

}
