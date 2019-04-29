package porfoliodb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import porfoliodb.core.OperationType;
import porfoliodb.core.Operazione;

public class PorfolioDB {
	
	private Connection conn = null;
	
	private final String urlDB;
	private final String usernameDB;
	private final String passwordDB;

	public PorfolioDB(String urlDB, String usernameDB, String passwordDB) {
		this.urlDB = urlDB;
		this.usernameDB = usernameDB;
		this.passwordDB = passwordDB;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(urlDB, usernameDB, passwordDB);

		return conn;

	}

	public void closeConnection() throws SQLException {
		if(conn !=null)
			conn.close();
	}

	public boolean insertOperazione(Operazione op) throws SQLException {
        
		if (conn == null)
			conn = getConnection();
		
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("INSERT INTO OPERAZIONI (OPERAZIONE, QUANT, DATE_OP) VALUES (?, ?, ?)");
			
			if(op.operationType == OperationType.PRELIEVO)
				pstmt.setString(1, "P");
			else if(op.operationType == OperationType.VERSAMENTO)
				pstmt.setString(1, "V"); 
			
			pstmt.setInt(2, op.quantita);
			pstmt.setTimestamp(3, new Timestamp(op.timestamp));
			int val = pstmt.executeUpdate();
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return true;
	}

	public List<Operazione> getOperazioni() throws SQLException {
		List<Operazione> listOperazione = new ArrayList<>();
        
		if (conn == null)
			conn = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = conn.prepareStatement("SELECT * FROM OPERAZIONI ORDER BY DATE_OP");
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("id");
		         String operazione = rs.getString("OPERAZIONE");
		         int quant = rs.getInt("QUANT");
		         Date dateOp = rs.getTimestamp("DATE_OP");

		         OperationType opType = null;
		         
		         if("V".equals(operazione))
		        	 opType = OperationType.VERSAMENTO;
		         else if("P".equals(operazione))
		        	 opType = OperationType.PRELIEVO;
		         
		         listOperazione.add(new Operazione(opType, 
		        		 quant, dateOp.getTime()));
		      }
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		}
        
		return listOperazione;
	}

	public List<Operazione> getOperazioni(OperationType type) throws SQLException {
		List<Operazione> listOperazione = new ArrayList<>();
        
		if (conn == null)
			conn = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			pstmt = conn.prepareStatement("SELECT * FROM OPERAZIONI WHERE OPERAZIONE = ? ORDER BY DATE_OP");
			
			String operationStr = null;
			
			if(type == OperationType.PRELIEVO)
				operationStr = "P";
			else if(type == OperationType.VERSAMENTO)
				operationStr = "V";
			
			pstmt.setString(1, operationStr);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("id");
		         String operazione = rs.getString("OPERAZIONE");
		         int quant = rs.getInt("QUANT");
		         Date dateOp = rs.getTimestamp("DATE_OP");

		         OperationType opType = null;
		         
		         if("V".equals(operazione))
		        	 opType = OperationType.VERSAMENTO;
		         else if("P".equals(operazione))
		        	 opType = OperationType.PRELIEVO;
		         
		         listOperazione.add(new Operazione(opType, 
		        		 quant, dateOp.getTime()));
		      }
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

		}
        
		return listOperazione;
	}

	public List<Operazione> getOperazioni(OperationType type, Date fromDate, Date toDate) throws SQLException {

		List<Operazione> listOperazione = new ArrayList<>();
        
		if (conn == null)
			conn = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			String query = null;
			
			query = "SELECT * FROM OPERAZIONI ";
			
			boolean isQueryByOperationType = false;
			boolean isQueryByDateFrom = false;
			boolean isQueryByDateTo = false;
			
			if(type != null)
				isQueryByOperationType = true;
			
			if(fromDate != null)
				isQueryByDateFrom = true;
			
			if(toDate != null)
				isQueryByDateTo = true;
			
			if(isQueryByOperationType) 
				query += "WHERE OPERAZIONE = ? ";
			
			if(isQueryByDateFrom) {
				if(isQueryByOperationType) 
					query += "AND DATE_OP >= ? ";
				else
					query += "WHERE DATE_OP >= ? ";
			}
			
			if(isQueryByDateTo) {
				if(isQueryByOperationType || isQueryByDateFrom) 
					query += "AND DATE_OP <= ? ";
				else
					query += "WHERE DATE_OP <= ? ";
			}
						
			query += " ORDER BY DATE_OP";
			
			pstmt = conn.prepareStatement(query);
			
			int paramCount = 1;
			
			if(isQueryByOperationType) {
				String operationStr = null;
				
				if(type == OperationType.PRELIEVO)
					operationStr = "P";
				else if(type == OperationType.VERSAMENTO)
					operationStr = "V";
				
				pstmt.setString(paramCount, operationStr);
				
				paramCount++;
			}
			
			if(isQueryByDateFrom) {
				
				pstmt.setTimestamp(paramCount, 
						new Timestamp(fromDate.getTime()));
				
				paramCount++;
			}
			
			if(isQueryByDateTo) {
				
				pstmt.setTimestamp(paramCount, 
						new Timestamp(toDate.getTime()));
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
		         //Retrieve by column name
		         int id  = rs.getInt("id");
		         String operazione = rs.getString("OPERAZIONE");
		         int quant = rs.getInt("QUANT");
		         Date dateOp = rs.getTimestamp("DATE_OP");

		         OperationType opType = null;
		         
		         if("V".equals(operazione))
		        	 opType = OperationType.VERSAMENTO;
		         else if("P".equals(operazione))
		        	 opType = OperationType.PRELIEVO;
		         
		         listOperazione.add(new Operazione(opType, 
		        		 quant, dateOp.getTime()));
		      }
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();

		}
        
		return listOperazione;
	}

}
