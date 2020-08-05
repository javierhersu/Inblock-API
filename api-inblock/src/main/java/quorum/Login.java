package quorum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Login {
    Connection connection;
    HttpHeaders header = new HttpHeaders();
	
    public Login() {
    	header.set("Content-Type", "application/json");

        try {	 
    		Class.forName(Constants.SQL_DRIVER);
    	    connection = DriverManager.getConnection(Constants.SQL_URL, Constants.SQL_USER, Constants.SQL_PASSWORD);	    
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}catch (ClassNotFoundException ex) {
    	    System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
    	}
        
    }
    
    public ResponseEntity<String> login(String user, String password) {
    	String username = null;
    	String pass = null;
    	
    	if(!userExist(connection, user)) {
    		JSONObject json = new JSONObject();
    		JSONObject error = new JSONObject();
    		try {
				error.put("auth", false);
				error.put("message", "User Not Found");
	    		error.put("type", "NullPointerException");
	    		error.put("code", HttpStatus.NOT_FOUND);
	    		json.put("error", error);
			} catch (JSONException e) {
				return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
			}
    		
    		return new ResponseEntity<String>(json.toString(), header, HttpStatus.NOT_FOUND);
    	}
    	
		username = getUser(connection, user);
	    pass = getPassword(connection, user);
		
    	
    	JSONObject json = new JSONObject();
    	System.out.println(username + " -- "+ pass);
    	if(pass.contentEquals(password)) {
    		String token = generateJWTToken(username, pass);
	    	try {
				json.put("auth", true);
				json.put("token", token);
		    	json.put("user", user);
			} catch (JSONException e) {
				return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", header, HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	}else {
    		JSONObject json2 = new JSONObject();
    		JSONObject error = new JSONObject();
    		try {
				error.put("auth", false);
				error.put("message", "Incorrect Password");
	    		error.put("type", "OAuthException");
	    		error.put("code", HttpStatus.UNAUTHORIZED);
	    		json2.put("error", error);
			} catch (JSONException e) {
				return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", header, HttpStatus.INTERNAL_SERVER_ERROR);
			}
    		
    		return new ResponseEntity<String>(json2.toString(), header, HttpStatus.UNAUTHORIZED);
    	}
    	
    	
    	return new ResponseEntity<String>(json.toString(), header, HttpStatus.OK);
    }
    
    public boolean validateLogin(String token) {
    	if(token == null) {
    		return false;
		}
    	String _cuenta = verifyJWTToken(token);
		if(_cuenta.contentEquals("")) {
			return false;
		}
    	return true;
    }
    
    public String getCuenta(String token) {
    	String _cuenta = verifyJWTToken(token);
    	return _cuenta;
    }
    
    public ResponseEntity<String> throwError1() {
    	JSONObject json2 = new JSONObject();
		JSONObject error = new JSONObject();
		try {
			error.put("auth", false);
			error.put("message", "No Token Provided");
    		error.put("type", "OAuthException");
    		error.put("code", HttpStatus.UNAUTHORIZED);
    		json2.put("error", error);
		} catch (JSONException e) {
			return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", header, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(json2.toString(), header, HttpStatus.UNAUTHORIZED);
    }
    
    public ResponseEntity<String> throwError2() {
    	JSONObject json2 = new JSONObject();
		JSONObject error = new JSONObject();
		try {
			error.put("auth", false);
			error.put("message", "Failed to Authenticate Token");
    		error.put("type", "OAuthException");
    		error.put("code", HttpStatus.UNAUTHORIZED);
    		json2.put("error", error);
		} catch (JSONException e) {
			return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", header, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(json2.toString(), header, HttpStatus.UNAUTHORIZED);
    }
    
    /******************************** TOKEN ************************************************/
    public String generateJWTToken(String user, String password) {
        String signatureSecret = "SECRET_VALUE_FOR_SIGNATURE";
        Algorithm algorithm = Algorithm.HMAC256(signatureSecret);

        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();

        c.add(Calendar.HOUR, 24);
        Date expireDate = c.getTime();

        String jwtToken = JWT.create()
                .withIssuer("telefonicaJWTAuthentication")
                .withSubject("demo")
                .withAudience("ey")
                .withIssuedAt(currentDate)
                .withExpiresAt(expireDate)
                .withClaim("user", user)
                .withClaim("password", password)
                .sign(algorithm);

        return jwtToken;
    }
    
    public String verifyJWTToken(String jwtToken) {
        String signatureSecret = "SECRET_VALUE_FOR_SIGNATURE";
        Algorithm algorithm = Algorithm.HMAC256(signatureSecret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("telefonicaJWTAuthentication")
                .withSubject("demo")
                .build();

        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        String username = null;
        String pass = null;
    	username = getUser(connection, decodedJWT.getClaim("user").asString());
    	pass = getPassword(connection, decodedJWT.getClaim("user").asString());
       
        if(decodedJWT.getClaim("password").asString().contentEquals(pass))
        	return decodedJWT.getClaim("user").asString();
        else
        	return "";
    }
    
    /***************** DB METHODS ******************************/
    
    public boolean userExist(Connection conn, String user) {
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
			e.printStackTrace();
		}
		return res;
	}
    
    public String getUser(Connection conn, String user) {
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
			e.printStackTrace();
		}
		return res;
	}
	
	public String getPassword(Connection conn, String user) {
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
}
