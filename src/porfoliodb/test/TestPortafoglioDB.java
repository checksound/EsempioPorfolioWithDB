package porfoliodb.test;

import static porfoliodb.test.TestUtils.stringToDate;
import static porfoliodb.test.TestUtils.stringToLong;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;
import porfoliodb.db.PorfolioDB;

class TestPortafoglioDB {

	@Test
	void testInsertOperazione() throws SQLException {
		String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		Operazione op = new Operazione(OperationType.VERSAMENTO, 170, 
				System.currentTimeMillis());
		
		porfolioDB.insertOperazione(op);
		
		List<Operazione> listOp = porfolioDB.getOperazioni();
		
		assertEquals(1, listOp.size());
		
		assertEquals(listOp.get(0), op);
		
		List<Operazione> listOpPrel = 
				porfolioDB.getOperazioni(OperationType.PRELIEVO);
		
		assertEquals(0, listOpPrel.size());
		
		List<Operazione> listOpVer = 
				porfolioDB.getOperazioni(OperationType.VERSAMENTO);
		
		assertEquals(1, listOpVer.size());
		
		assertEquals(listOp, listOpVer);
		
		porfolioDB.closeConnection();
	}

	@Test
	void testGetOperazioniOperationTypeDateDate() throws SQLException {
		
		// "dd.MM.yy-hh:mm:ss"
		
		long lg1 = stringToLong("17.04.19-08:14:56");
		
		long lg2 = stringToLong("17.04.19-09:14:27");
		
		long lg3 = stringToLong("17.04.20-08:14:56");
		
		long lg4 = stringToLong("17.04.20-13:14:56");
				
		String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		Operazione op1 = new Operazione(OperationType.VERSAMENTO, 170, 
				lg1);
		
		porfolioDB.insertOperazione(op1);
		
		Operazione op2 = new Operazione(OperationType.PRELIEVO, 100, 
				lg2);
		
		porfolioDB.insertOperazione(op2);
		
		Operazione op3 = new Operazione(OperationType.VERSAMENTO, 250, 
				lg3);
		
		porfolioDB.insertOperazione(op3);
		
		Operazione op4 = new Operazione(OperationType.PRELIEVO, 170, 
				lg4);
		
		porfolioDB.insertOperazione(op4);
		
		List<Operazione> listOpVer = 
				porfolioDB.getOperazioni(OperationType.VERSAMENTO);
		
		System.out.println(listOpVer);
		
		List<Operazione> listOpPrel = 
				porfolioDB.getOperazioni(OperationType.PRELIEVO);
		
		System.out.println(listOpPrel);
		
		// [ tipo=VERSAMENTO, qt=170, dt='17.04.19-08:14:56'], 
		// [ tipo=PRELIEVO, qt=100, dt='17.04.19-09:14:27'], 
		// [ tipo=VERSAMENTO, qt=250, dt='17.04.20-08:14:56'], 
		// [ tipo=PRELIEVO, qt=170, dt='17.04.20-01:14:56']
		
		List<Operazione> listOp = 
				porfolioDB.getOperazioni(OperationType.PRELIEVO, stringToDate("17.04.20-08:14:56"), null);
		
		System.out.println(listOp);
		
		
		porfolioDB.closeConnection();
		
	}

}
