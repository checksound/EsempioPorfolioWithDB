package porfoliodb.test;

import java.sql.SQLException;
import java.util.List;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;
import porfoliodb.db.PorfolioDB;

public class PortafoglioDBTest {

	public static void main(String[] args) throws SQLException {
		
		// String url = "jdbc:h2:mem:test;INIT=runscript from '~/create.sql'\\;runscript from '~/init.sql'";
		
		String url = "jdbc:h2:mem:test;INIT=runscript from './schema/create.sql'";
		
		PorfolioDB porfolioDB = new PorfolioDB(url, 
				"", "");
		
		porfolioDB.insertOperazione(
				new Operazione(OperationType.VERSAMENTO, 170, 
						System.currentTimeMillis()));
		
		List<Operazione> listOp = porfolioDB.getOperazioni();
		
		System.out.println(listOp);
		
		porfolioDB.closeConnection();

	}

}
