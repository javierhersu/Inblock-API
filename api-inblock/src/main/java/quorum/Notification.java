package quorum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.tomcat.util.bcel.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PGobject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Notification {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Connection connection = null;
	Random rand = new Random(); 
	
    public Notification() {
        try {	 
    		Class.forName(Constants.SQL_DRIVER);
    	    connection = DriverManager.getConnection(Constants.SQL_URL, Constants.SQL_USER, Constants.SQL_PASSWORD);	    
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}catch (ClassNotFoundException ex) {
    	    System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
    	}
    }
    
    //Notificacion enviada al tener invoices duplicadas
    public void addNotification1(String _cuenta, String _inblockID, String _account) {
 		JSONObject json = new JSONObject();
 		
 		int num = rand.nextInt(100000);
 		String _id = _cuenta + num;
 		try {
			json.put("type", 1);
			json.put("inblockID", _inblockID);
			json.put("accountDiscounted", _account);
			json.put("read", false);
			json.put("timestamp", sdf.format(new Date()));
			json.put("_id", _id);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
   		insert(connection, _cuenta, json, _id);
    }
    
    //Notificacion enviada al subir una factura nueva
    public void addNotification2(String _cuenta, int numInvoices, String _account, int errors) {
 		JSONObject json = new JSONObject();
 		
 		int num = rand.nextInt(100000);
 		String _id = _cuenta + num;
 		try {
			json.put("type", 2);
			json.put("numInvoices", numInvoices);
			json.put("accountDiscounted", _account);
			json.put("numErrors", errors);
			json.put("read", false);
			json.put("timestamp", sdf.format(new Date()));
			json.put("_id", _id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
   		insert(connection, _cuenta, json, _id);
    }
    
    //Notificacion descontar una factura ya descontada
    public void addNotification3(String _cuenta, String _inblockID) {
 		JSONObject json = new JSONObject();
 		
 		int num = rand.nextInt(100000);
 		String _id = _cuenta + num;
 		try {
			json.put("type", 4);
			json.put("inblockID", _inblockID);
			json.put("read", false);
			json.put("timestamp", sdf.format(new Date()));
			json.put("_id", _id);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
   		insert(connection, _cuenta, json, _id);
    }
    
    public void addNotification4(String _cuenta, String _inblockID) {
 		JSONObject json = new JSONObject();
 		
 		int num = rand.nextInt(100000);
 		String _id = _cuenta + num;
 		try {
			json.put("type", 4);
			json.put("inblockID", _inblockID);
			json.put("read", false);
			json.put("timestamp", sdf.format(new Date()));
			json.put("_id", _id);

		} catch (JSONException e) {
			e.printStackTrace();
		}
   		insert(connection, _cuenta, json, _id);
    }
    
    //Notificacion diaria de analysis
    public void addDailyNotification(String _cuenta, int uploaded, int discounted, int updated) {
 		JSONObject json = new JSONObject();
 		
 		int num = rand.nextInt(100000);
 		String _id = _cuenta + num;
 		try {
			json.put("type", 3);
			json.put("uploadedInvoices", uploaded);
			json.put("discountedInvoices", discounted);
			json.put("aeatInvoices", updated);
			json.put("read", false);
			json.put("timestamp", sdf.format(new Date()));
			json.put("_id", _id);

		} catch (JSONException e) {
			e.printStackTrace();
		}
   		insert(connection, _cuenta, json, _id);
    }
    
    public ResponseEntity<String> getNotifications(String _cuenta) {
    	JSONArray array = getDBNotifications(connection, _cuenta);
    	
    	JSONArray res = new JSONArray();
    	for(int i=array.length()-1; i>=0; i--) {
    		try {
				res.put(array.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
    	return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
    }
    
    public ResponseEntity<String> getNumberNotifications(String _cuenta) {
    	JSONObject json = new JSONObject();
    	try {
			json.put("notifications", getNumNotif(connection, _cuenta));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
    	return new ResponseEntity<String>(json.toString(), header, HttpStatus.OK);
    }
    
    public void markRead(String _cuenta, String _id) {
    	JSONObject json = getOneNotification(connection, _cuenta, _id);
    	System.out.println(json);
    	try {
			json.put("read", true);
			updateRead(connection, json.toString(), _id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void remove(String _cuenta, String _id) {
    	deleteNotification(connection, _cuenta, _id);
    }
    
    public String getCollectionName(String _cuenta) {
    	if(_cuenta.contentEquals("AEAT")) {
    		return "notAEAT";
    	}else if(_cuenta.contentEquals("inBlock")) {
    		return "notinBlock";
    	}else if(_cuenta.contentEquals("Santander")) {
    		return "notSantander";
    	}else if(_cuenta.contentEquals("BBVA")) {
    		return "notBBVA";
    	}else if(_cuenta.contentEquals("Bankia")) {
    		return "notBankia";
    	}else if(_cuenta.contentEquals("Caixabank")) {
    		return "notCaixabank";
    	}else {
    		return "notSabadell";
    	}
    }
    
    /**************************** DB METHODS ***********************************/
    public void insert(Connection conn, String account, JSONObject json, String _id) {
		String SQL_INSERT = "INSERT INTO notifications (id, account, jsonNotif)"
				+ "VALUES(?,?,?)";
				
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, _id);
			pstmt.setString(2, account);

			PGobject jsonObject = new PGobject();
			 jsonObject.setType("json");
			 jsonObject.setValue(json.toString());
			pstmt.setObject(3, jsonObject);
			
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public JSONArray getDBNotifications(Connection conn, String account) {
    	String SQL_SELECT = "SELECT jsonnotif FROM notifications WHERE account='"+account+"'";
    	Statement st;
		JSONArray res = new JSONArray();
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				JSONObject json = new JSONObject(rs.getObject("jsonnotif"));
				JSONObject json2 = null;
				try {
					json2 = new JSONObject(json.getString("value"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				res.put(json2);
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
    }
    
    public int getNumNotif(Connection conn, String account) {
    	String SQL_SELECT = "SELECT jsonnotif FROM notifications WHERE account='"+account+"'";
    	Statement st;
		int res = 0;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				JSONObject json = new JSONObject(rs.getObject("jsonnotif"));
				JSONObject json2 = null;
				try {
					json2 = new JSONObject(json.getString("value"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if(json2.getBoolean("read") == false)
					res++;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
    }
    
    public void updateRead(Connection conn, String jsonString, String id) {
		String SQL_INSERT = "UPDATE notifications SET jsonnotif=? WHERE id='"+id+"'";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			
			PGobject jsonObject = new PGobject();
			 jsonObject.setType("json");
			 jsonObject.setValue(jsonString);
			pstmt.setObject(1, jsonObject);
			
			pstmt.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public JSONObject getOneNotification(Connection conn, String account, String id) {
    	String SQL_SELECT = "SELECT jsonnotif FROM notifications WHERE id='"+id+"'";
    	Statement st;
    	JSONObject res = null;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(SQL_SELECT);	
			while (rs.next()) {
				JSONObject json = new JSONObject(rs.getObject("jsonnotif"));
				try {
					res = new JSONObject(json.getString("value"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
    }
    
    public void deleteNotification(Connection conn, String account, String id) {
    	String SQL_SELECT = "DELETE FROM notifications WHERE id='"+id+"'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT, Statement.RETURN_GENERATED_KEYS);
						
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    }
}
