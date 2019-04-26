package porfoliodb.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import porfoliodb.core.Operazione;
import porfoliodb.exception.ParsingFormatException;

public class PorfolioOnFile {
	
	private File dataFile;
	
	public PorfolioOnFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
	public List<Operazione> readFromFile() 
			throws IOException, ParsingFormatException {
		
		List<Operazione> listOperations = new ArrayList<Operazione>();
		
		try( Scanner scanner = new Scanner(dataFile) ) {
            while (scanner.hasNextLine()) {
                String porfolioEntry = scanner.nextLine();
                Operazione op = Formatter.parsePorfolioEntry(porfolioEntry);
				// add operazione
				listOperations.add(op);
				
            }
		}
		return listOperations;
	}
	
	public boolean writeToFile(List<Operazione> listOperazioni) 
			throws IOException {
		
		System.out.println("Saving phone directory changes to file " + 
                dataFile.getAbsolutePath() + " ...");
        PrintWriter out;
        try {
            out = new PrintWriter( new FileWriter(dataFile) );
        }
        catch (IOException e) {
            System.out.println("ERROR: Can't open data file for output.");
            return false;
        }

        for(Operazione op: listOperazioni) {
			String lineEntry = Formatter.formatPorfolioEntry(op);
			out.println(lineEntry);
		}
        
        out.flush();
        out.close();
        
        if (out.checkError()) {
            System.out.println("ERROR: Some error occurred while writing data file.");
            return false;
        }
        
        return true;
		
		
	}
	
	
}
