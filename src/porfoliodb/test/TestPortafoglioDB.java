package porfoliodb.test;

import static porfoliodb.test.TestUtils.stringToDate;
import static porfoliodb.test.TestUtils.stringToLong;
import static porfoliodb.test.TestUtils.createListTimestamp;

import static porfoliodb.core.OperationType.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;
import porfoliodb.db.PorfolioDB;

class TestPortafoglioDB {
	
	static List<Operazione> testSuite1() {
		
		List<Operazione> listOps = Arrays.asList(
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-08:14:56")),
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:15:34")), 
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:44:56")),
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56")),
				new Operazione(PRELIEVO, 300, 
						stringToLong("17.04.19-13:14:56")),
				new Operazione(VERSAMENTO, 100, 
						stringToLong("17.04.19-13:15:06"))
				);
	
		return listOps;
	}

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
	public void doTestListOperationAll() throws SQLException {
		
        String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		List<Operazione> listOps = testSuite1();
		for(Operazione op: listOps) {
			porfolioDB.insertOperazione(op);
		}
		
		List<Operazione> listOpAll = porfolioDB.getOperazioni();
		
		assertEquals(listOps, listOpAll);
		
		List<Operazione> listOpVersamento = porfolioDB.getOperazioni(VERSAMENTO);
		
		List<Operazione> listOpsVersEx = Arrays.asList(
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-08:14:56")),
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56")),
				new Operazione(VERSAMENTO, 100, 
						stringToLong("17.04.19-13:15:06"))
				);
		
		assertEquals(listOpsVersEx, listOpVersamento);
		
		porfolioDB.closeConnection();
		
	}
	
	@Test
	public void doTestListOperationFromDate() throws SQLException {
	
       String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		List<Operazione> listOps = testSuite1();
		for(Operazione op: listOps) {
			porfolioDB.insertOperazione(op);
		}
		
		List<Operazione> listOp = porfolioDB.getOperazioniFromDate( 
				stringToDate("17.04.19-09:45:00"));
		
		List<Operazione> listOpsExp = Arrays.asList(
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56")),
				new Operazione(PRELIEVO, 300, 
						stringToLong("17.04.19-13:14:56")),
				new Operazione(VERSAMENTO, 100, 
						stringToLong("17.04.19-13:15:06"))
				);
		
		assertEquals(listOpsExp, listOp);
		
		listOp = porfolioDB.getOperazioniFromDate(VERSAMENTO, 
				stringToDate("17.04.19-09:45:00"));
		
		List<Operazione> listOpsExp2 = Arrays.asList(
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56")),
				new Operazione(VERSAMENTO, 100, 
						stringToLong("17.04.19-13:15:06"))
				);
		
		assertEquals(listOpsExp2, listOp);
		
		porfolioDB.closeConnection();
		
	}
	
	@Test
	public void doTestListOperationToDate() throws SQLException {
		
		String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		List<Operazione> listOps = testSuite1();
		for(Operazione op: listOps) {
			porfolioDB.insertOperazione(op);
		}
		
		List<Operazione> listOp = porfolioDB.getOperazioniToDate( 
				stringToDate("17.04.19-09:45:00"));
		
		List<Operazione> listOpsExp1 = Arrays.asList(
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-08:14:56")),
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:15:34")), 
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:44:56"))
				);
		
		assertEquals(listOpsExp1, listOp);
		
		listOp = porfolioDB.getOperazioniToDate(VERSAMENTO, 
				stringToDate("17.04.19-09:45:00"));
		
			
		List<Operazione> listOpsExp2 = Arrays.asList(
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-08:14:56"))
				);
		
		assertEquals(listOpsExp2, listOp);
		
		porfolioDB.closeConnection();
		
	}
	

	
	@Test
	public void doTestListOperationsBetweenDate() throws SQLException {
		
		String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		List<Operazione> listOps = testSuite1();
		for(Operazione op: listOps) {
			porfolioDB.insertOperazione(op);
		}
		
		List<Operazione> listOp = porfolioDB.getOperazioniBetweenDate( 
				stringToDate("17.04.19-09:15:00"), stringToDate("17.04.19-13:15:00"));
		
		List<Operazione> listOpsExp1 = Arrays.asList(
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:15:34")), 
				new Operazione(PRELIEVO, 100, 
						stringToLong("17.04.19-09:44:56")),
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56")),
				new Operazione(PRELIEVO, 300, 
						stringToLong("17.04.19-13:14:56"))
			);
		
		assertEquals(listOpsExp1, listOp);
		
		listOp = porfolioDB.getOperazioniBetweenDate(VERSAMENTO, 
				stringToDate("17.04.19-09:15:00"), stringToDate("17.04.19-13:15:00"));
		
		List<Operazione> listOpsExp2 = Arrays.asList(
				new Operazione(VERSAMENTO, 300, 
						stringToLong("17.04.19-10:14:56")),
				new Operazione(VERSAMENTO, 200, 
						stringToLong("17.04.19-12:30:56"))
				);
		
		assertEquals(listOpsExp2, listOp);
		
		porfolioDB.closeConnection();
		
	}
	
	
}
