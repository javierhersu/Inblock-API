package quorum;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;    
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PGobject;


public class Test {

	public static void main(String[] args) {
	    String SQL_SELECT = "SELECT * FROM aeat WHERE hash='0x33019bfc7b57347d5e534765a32c8db172b54432'";
	    String SQL_UPDATE = "UPDATE aeat SET json=? WHERE hash='0x33019bfc7b57347d5e534765a32c8db172b54432'";
		try { 
			Class.forName(Constants.SQL_DRIVER);
		    Connection connection = null;
		    connection = DriverManager.getConnection(Constants.SQL_URL, Constants.SQL_USER, Constants.SQL_PASSWORD);
		    //boolean valid = connection.isValid(50000);
            //System.out.println(valid ? "TEST OK" : "TEST FAIL");
            
			Statement st;

			st = connection.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			JSONObject json = null;
			while (rs.next()) {
				JSONObject res = new JSONObject(rs.getObject("json"));
				try {
					json = new JSONObject(res.getString("value"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			st.close();
			
			try {
				json.getJSONObject("blockchain").put("discountStatus", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println(json);

			PreparedStatement pstmt = connection.prepareStatement(SQL_UPDATE, Statement.RETURN_GENERATED_KEYS);
			
			PGobject jsonObject = new PGobject();
			 jsonObject.setType("json");
			 jsonObject.setValue(json.toString());
			pstmt.setObject(1, jsonObject);
			
			pstmt.execute();
	    
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException ex) {
		    System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
		}
	}
	
	public static JSONObject autocomplete(Connection conn, String cuenta) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String SQL_SELECT = "SELECT json FROM "+cuenta;
		Statement st;
		double santanderAmount = 0.00;
		double bbvaAmount = 0.00;
		double sabadellAmount = 0.00;
		double caixaAmount = 0.00;
		double bankiaAmount = 0.00;

		int santanderInvoice = 0;
		int bbvaInvoice = 0;
		int sabadellInvoice = 0;
		int caixaInvoice = 0;
		int bankiaInvoice = 0;

		double santanderFact = 0.00;
		double bbvaFact = 0.00;
		double sabadellFact = 0.00;
		double caixaFact = 0.00;
		double bankiaFact = 0.00;
		
		JSONObject res = new JSONObject();
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				JSONObject json = new JSONObject(rs.getObject("json"));

				try {
					JSONObject result = new JSONObject(json.getString("value"));
					String account = result.getJSONObject("blockchain").getString("account");
					String amountStr = result.getJSONObject("invoice").getString("totalAmountInvoices");
					double amount = Double.valueOf(amountStr);
					double fact = 0.00;
					if(amount < 5000) {
						fact = 0.03;
					}else if(amount >= 5000 && amount < 10000) {
						fact = 0.20;
					}else if(amount >= 10000 && amount < 50000) {
						fact = 0.80;
					}else if(amount >= 50000 && amount < 100000) {
						fact = 2.00;
					}else if(amount >= 100000 && amount < 500000) {
						fact = 8.00;
					}else if(amount >= 500000 && amount < 1000000) {
						fact = 20.00;
					}else {
						fact = 50.00;
					}
					
					if(account.contentEquals("Santander")) {
						santanderAmount += amount;
						santanderFact += fact;
						santanderInvoice++;
					}else if(account.contentEquals("BBVA")) {
						bbvaAmount += amount;
						bbvaFact += fact;
						bbvaInvoice++;
					}else if(account.contentEquals("Bankia")) {
						bankiaAmount += amount;
						bankiaFact += fact;
						bankiaInvoice++;
					}else if(account.contentEquals("Caixabank")) {
						caixaAmount += amount;
						caixaFact += fact;
						caixaInvoice++;
					}else if(account.contentEquals("Sabadell")) {
						sabadellAmount += amount;
						sabadellFact += fact;
						sabadellInvoice++;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			NumberFormat formatter = new DecimalFormat("0.00");

			res.put("santanderAmount", formatter.format(santanderAmount).replace(",", "."));
			res.put("bbvaAmount", formatter.format(bbvaAmount).replace(",", "."));
			res.put("sabadellAmount", formatter.format(sabadellAmount).replace(",", "."));
			res.put("caixaAmount", formatter.format(caixaAmount).replace(",", "."));
			res.put("bankiaAmount", formatter.format(bankiaAmount).replace(",", "."));
			
			res.put("santanderInvoice", String.valueOf(santanderInvoice).replace(",", "."));
			res.put("bbvaInvoice", String.valueOf(bbvaInvoice).replace(",", "."));
			res.put("sabadellInvoice", String.valueOf(sabadellInvoice).replace(",", "."));
			res.put("caixaInvoice", String.valueOf(caixaInvoice).replace(",", "."));
			res.put("bankiaInvoice", String.valueOf(bankiaInvoice).replace(",", "."));
			
			res.put("santanderFact", formatter.format(santanderFact).replace(",", "."));
			res.put("bbvaFact", formatter.format(bbvaFact).replace(",", "."));
			res.put("sabadellFact", formatter.format(sabadellFact).replace(",", "."));
			res.put("caixaFact", formatter.format(caixaFact).replace(",", "."));
			res.put("bankiaFact", formatter.format(bankiaFact).replace(",", "."));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	

	
	public static boolean userExist(Connection conn, String user) {
		String SQL_SELECT = "SELECT username FROM users WHERE username='"+user+"'";
		Statement st;
		boolean res = false;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				System.out.println(rs.getString("username"));
				if(rs.getString("username") != null)
					res = true;
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public static String getUser(Connection conn, String user) {
		String SQL_SELECT = "SELECT username FROM users WHERE username='"+user+"'";
		Statement st;
		String res = "";
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				if(rs.getString("username") != null)
					res = rs.getString("username");
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public static String getPassword(Connection conn, String user) {
		String SQL_SELECT = "SELECT password FROM users WHERE username='"+user+"'";
		Statement st;
		String res = "";
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				if(rs.getString("password") != null)
					res = rs.getString("password");
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void insert(Connection conn) {
		String cuenta = "aeat";
		String SQL_INSERT = "INSERT INTO "+ cuenta +" (totalAmountInvoices, issueDate, "
				+ "transactionDate, uploadingTimestamp, inBlockID, hash, account, "
				+ "validInvoice, doubledInvoice, discountStatus, entityDiscountStatus, "
				+ "discountCheckExecuted, existenceCheckExecuted, existenceStatus, json)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String amount = "1400.25";
		String issueDate = "23/04/1994 83:39:39";
		String transactionDate = "23/04/1994 83:39:39";
		String uploadingTimestamp = "23/04/1994 83:39:39";
		String inBlockID = "0984035";
		String hash = "lkdgioshep";
		String account = "AEAT";
		boolean validInvoice = false;
		boolean doubledInvoice = false;
		boolean discountStatus = false;
		boolean entityDiscountStatus = false;
		boolean discountCheckExecuted = false;
		boolean existenceCheckExecuted = false;
		boolean existenceStatus = false;
		JSONObject json = new JSONObject();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, amount);
			pstmt.setString(2, issueDate);
			pstmt.setString(3, transactionDate);
			pstmt.setString(4, uploadingTimestamp);
			pstmt.setString(5, inBlockID);
			pstmt.setString(6, hash);
			pstmt.setString(7, account);
			pstmt.setBoolean(8, validInvoice);
			pstmt.setBoolean(9, doubledInvoice);
			pstmt.setBoolean(10, discountStatus);
			pstmt.setBoolean(11, entityDiscountStatus);
			pstmt.setBoolean(12, discountCheckExecuted);
			pstmt.setBoolean(13, existenceCheckExecuted);
			pstmt.setBoolean(14, existenceStatus);
			PGobject jsonObject = new PGobject();
			 jsonObject.setType("json");
			 jsonObject.setValue(json.toString());
			pstmt.setObject(15, jsonObject);
			
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
