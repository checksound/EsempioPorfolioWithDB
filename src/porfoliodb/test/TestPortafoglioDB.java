package porfoliodb.test;

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
	void testGetOperazioniOperationType() {
		// fail("Not yet implemented");
	}

	@Test
	void testGetOperazioniOperationTypeDateDate() {
		// fail("Not yet implemented");
	}

}
