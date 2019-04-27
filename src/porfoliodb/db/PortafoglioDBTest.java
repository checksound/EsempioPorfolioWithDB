package porfoliodb.db;

import java.sql.SQLException;
import java.util.List;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;

public class PortafoglioDBTest {

	public static void main(String[] args) throws SQLException {
		
		PorfolioDB porfolioDB = new PorfolioDB("jdbc:h2:~/porfolio_db", 
				"sa", "");
		
//		porfolioDB.insertOperazione(
//				new Operazione(OperationType.VERSAMENTO, 170, 
//						System.currentTimeMillis()));
		
		List<Operazione> listOp = porfolioDB.getOperazioni();
		
		System.out.println(listOp);

	}

}
