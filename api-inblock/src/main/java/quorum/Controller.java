package quorum;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class Controller {
    
	MongoClientURI connectionString = new MongoClientURI(Constants.MONGO_URL);
    MongoClient mongoClient  = new MongoClient(connectionString);
    private MongoDatabase db = mongoClient.getDatabase(Constants.MONGO_DB);
	private App app = new App(db);
	private Login login = new Login(db);
	private Notification not = new Notification(db);
	private Constants constants = new Constants();

	Logger LOGGER = Logger.getLogger(Controller.class.getName());
	
	@CrossOrigin(origins = "*")
    @RequestMapping("/init")
    public void greeting() {
        app = new App(db);
    }
	
	//MONGO
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/login", method=RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody String a){
		LOGGER.log(Level.INFO, "Llamada login");
    	ResponseEntity<String> res = null;
    	try {
    		JSONObject json = new JSONObject(a);
			res = login.login(json.getString("username"), json.getString("password"));
		} catch (JSONException e) {
			System.out.println(e);
			//return "{\"error\":\"005\",\"message\":\"ERROR AL GENERAR EL JSON\"}";
		}
    	return res;
    }
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/upload", method=RequestMethod.POST)
    public ResponseEntity<String> uploadInvoice(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Upload Invoices");
		String token = headers.get("authorization");
    	JSONObject res = new JSONObject();
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	String _cuenta = login.getCuenta(token);
    	if(_cuenta.contentEquals("AEF") || _cuenta.contentEquals("inBlock") || _cuenta.contentEquals("AEAT")) {
    		JSONObject json2 = new JSONObject();
    		JSONObject error = new JSONObject();
    		try {
    			error.put("auth", true);
    			error.put("message", "Only Factoring Entities can Upload Invoices");
        		error.put("type", "OAuthException");
        		error.put("code", HttpStatus.UNAUTHORIZED);
        		json2.put("error", error);
    		} catch (JSONException e) {
    			return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    		}
    		
    		return new ResponseEntity<String>(json2.toString(), HttpStatus.UNAUTHORIZED);
    	}
    	
    	String priv = constants.getPrivateKey(_cuenta);
    	String pub = constants.getPublicKey(_cuenta);
    	String host = constants.getHost(_cuenta);
    	Blockchain blockchain = new Blockchain(host, priv, pub);
    	
    	List<String> inblockIDs = new ArrayList<String>();
    	
    	try {
    		JSONObject o = new JSONObject(a);
    		JSONArray array = new JSONArray(o.getString("json"));
    		//System.out.println(array);
    		for(int i=0; i<array.length(); i++) {
    			JSONObject json = array.getJSONObject(i);
    			//System.out.println(json);
    			String schemaVersion = json.getString("schemaVersion");
    			String modality = json.getString("modality");
    		    String invoiceIssuerType = json.getString("invoiceIssuerType");
    		    String batchIdentifier = json.getString("batchIdentifier");
    		    String invoicesCount = json.getString("invoicesCount");
    		    String totalAmountInvoices = json.getString("totalAmountInvoices");
    		    String totalAmountOutstanding = json.getString("totalAmountOutstanding");
    		    String totalAmountExecutable = json.getString("totalAmountExecutable");
    		    String headerInvoiceCurrencyCode = json.getString("headerInvoiceCurrencyCode");
    		    String sellerPersonTypeCode = json.getString("sellerPersonTypeCode");
    		    String sellerResidenceTypeCode = json.getString("sellerResidenceTypeCode");
    		    String sellerTaxIdentificationNumber = json.getString("sellerTaxIdentificationNumber");
    		    String sellerCorporateName = json.getString("sellerCorporateName");
    		    String sellerAddress = json.getString("sellerAddress");
    		    String sellerPostCode = json.getString("sellerPostCode");
    		    String sellerTown = json.getString("sellerTown");
    		    String sellerProvince = json.getString("sellerProvince");
    		    String sellerCountryCode = json.getString("sellerCountryCode");
    		    String buyerPersonTypeCode = json.getString("buyerPersonTypeCode");
    		    String buyerResidenceTypeCode = json.getString("buyerResidenceTypeCode");
    		    String buyerTaxIdentificationNumber = json.getString("buyerTaxIdentificationNumber");
    		    String buyerCorporateName = json.getString("buyerCorporateName");
    		    String buyerAddress = json.getString("buyerAddress");
    		    String buyerPostCode = json.getString("buyerPostCode");
    		    String buyerTown = json.getString("buyerTown");
    		    String buyerProvince = json.getString("buyerProvince");
    		    String buyerCountryCode = json.getString("buyerCountryCode");
    		    String invoiceNumber = json.getString("invoiceNumber");
    		    String invoiceDocumentType = json.getString("invoiceDocumentType");
    		    String invoiceClass = json.getString("invoiceClass");
    		    String issueDate = json.getString("issueDate");
    		    String invoiceCurrencyCode = json.getString("invoiceCurrencyCode");
    		    String taxCurrencyCode = json.getString("taxCurrencyCode");
    		    String languageName = json.getString("languageName");
    		    String taxTypeCode = json.getString("taxTypeCode");
    		    String taxRate = json.getString("taxRate");
    		    String taxableBaseTotalAmount = json.getString("taxableBaseTotalAmount");
    		    String taxTotalAmount = json.getString("taxTotalAmount");
    		    String totalGrossAmount = json.getString("totalGrossAmount");
    		    String totalGrossAmountBeforeTaxes = json.getString("totalGrossAmountBeforeTaxes");
    		    String totalTaxOutputs = json.getString("totalTaxOutputs");
    		    String totalTaxesWithheld = json.getString("totalTaxesWithheld");
    		    String invoiceTotal = json.getString("invoiceTotal");
    		    String totalOutstandingAmount = json.getString("totalOutstandingAmount");
    		    String totalExecutableAmount = json.getString("totalExecutableAmount");
    		    String itemDescription = json.getString("itemDescription");
    		    String quantity = json.getString("quantity");
    		    String unitOfMeasure = json.getString("unitOfMeasure");
    		    String transactionDate = json.getString("transactionDate");
    		    boolean status = json.getBoolean("status");
    		    
    		    String inblockID = app.addInvoice(blockchain, _cuenta.toLowerCase(), schemaVersion, modality,
    		    	    invoiceIssuerType, batchIdentifier, invoicesCount, totalAmountInvoices,
    		    	    totalAmountOutstanding, totalAmountExecutable, headerInvoiceCurrencyCode,
    		    	    sellerPersonTypeCode, sellerResidenceTypeCode, sellerTaxIdentificationNumber,
    		    	    sellerCorporateName, sellerAddress,
    		    	    sellerPostCode, sellerTown, sellerProvince, sellerCountryCode,
    		    	    buyerPersonTypeCode, buyerResidenceTypeCode,  buyerTaxIdentificationNumber, 
    		    	    buyerCorporateName, buyerAddress, buyerPostCode,
    		    	    buyerTown, buyerProvince, buyerCountryCode, invoiceNumber, 
    		    	    invoiceDocumentType, invoiceClass, issueDate, 
    		    	    invoiceCurrencyCode, taxCurrencyCode, languageName, taxTypeCode,
    		    	    taxRate, taxableBaseTotalAmount, taxTotalAmount, totalGrossAmount,
    		    	    totalGrossAmountBeforeTaxes, totalTaxOutputs, totalTaxesWithheld, 
    		    	    invoiceTotal, totalOutstandingAmount, totalExecutableAmount, 
    		    	    itemDescription, quantity, unitOfMeasure, transactionDate, status);  
    		    
    		    inblockIDs.add(inblockID);
    		}
    		not.addNotification2(_cuenta.toLowerCase(), array.length(), _cuenta, 0);
			//res = login.login(json.getString("username"), json.getString("password"));
		} catch (JSONException e) {
			System.out.println(e);
			//return "{\"error\":\"005\",\"message\":\"ERROR AL GENERAR EL JSON\"}";
		}
    	
		try {
			res.put("message", "Invoices added correctly");
			res.put("IDs", inblockIDs);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
    }
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/uploadAEAT", method=RequestMethod.POST)
    public ResponseEntity<String> uploadInvoiceAEAT(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Upload Invoices AEAT");
		String token = headers.get("authorization");
    	JSONObject res = new JSONObject();
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	String _cuenta = login.getCuenta(token);
    	if(!_cuenta.contentEquals("AEAT")) {
    		JSONObject json2 = new JSONObject();
    		JSONObject error = new JSONObject();
    		try {
    			error.put("auth", true);
    			error.put("message", "Only AEAT can Upload Invoices");
        		error.put("type", "OAuthException");
        		error.put("code", HttpStatus.UNAUTHORIZED);
        		json2.put("error", error);
    		} catch (JSONException e) {
    			return new ResponseEntity<String>("{\"error\":\"JSONException\", \"message\":\""+e+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    		}
    		
    		return new ResponseEntity<String>(json2.toString(), HttpStatus.UNAUTHORIZED);
    	}
    	try {
    		JSONObject o = new JSONObject(a);
    		JSONArray array = new JSONArray(o.getString("json"));
    		//System.out.println(array);
    		String priv = constants.getPrivateKey(_cuenta);
        	String pub = constants.getPublicKey(_cuenta);
        	String host = constants.getHost(_cuenta);
        	Blockchain blockchain = new Blockchain(host, priv, pub);
        	
    		for(int i=0; i<array.length(); i++) {
    			JSONObject json = array.getJSONObject(i);
    			//System.out.println(json);
    			String schemaVersion = json.getString("schemaVersion");
    			String modality = json.getString("modality");
    		    String invoiceIssuerType = json.getString("invoiceIssuerType");
    		    String batchIdentifier = json.getString("batchIdentifier");
    		    String invoicesCount = json.getString("invoicesCount");
    		    String totalAmountInvoices = json.getString("totalAmountInvoices");
    		    String totalAmountOutstanding = json.getString("totalAmountOutstanding");
    		    String totalAmountExecutable = json.getString("totalAmountExecutable");
    		    String headerInvoiceCurrencyCode = json.getString("headerInvoiceCurrencyCode");
    		    String sellerPersonTypeCode = json.getString("sellerPersonTypeCode");
    		    String sellerResidenceTypeCode = json.getString("sellerResidenceTypeCode");
    		    String sellerTaxIdentificationNumber = json.getString("sellerTaxIdentificationNumber");
    		    String sellerCorporateName = json.getString("sellerCorporateName");
    		    String sellerAddress = json.getString("sellerAddress");
    		    String sellerPostCode = json.getString("sellerPostCode");
    		    String sellerTown = json.getString("sellerTown");
    		    String sellerProvince = json.getString("sellerProvince");
    		    String sellerCountryCode = json.getString("sellerCountryCode");
    		    String buyerPersonTypeCode = json.getString("buyerPersonTypeCode");
    		    String buyerResidenceTypeCode = json.getString("buyerResidenceTypeCode");
    		    String buyerTaxIdentificationNumber = json.getString("buyerTaxIdentificationNumber");
    		    String buyerCorporateName = json.getString("buyerCorporateName");
    		    String buyerAddress = json.getString("buyerAddress");
    		    String buyerPostCode = json.getString("buyerPostCode");
    		    String buyerTown = json.getString("buyerTown");
    		    String buyerProvince = json.getString("buyerProvince");
    		    String buyerCountryCode = json.getString("buyerCountryCode");
    		    String invoiceNumber = json.getString("invoiceNumber");
    		    String invoiceDocumentType = json.getString("invoiceDocumentType");
    		    String invoiceClass = json.getString("invoiceClass");
    		    String issueDate = json.getString("issueDate");
    		    String invoiceCurrencyCode = json.getString("invoiceCurrencyCode");
    		    String taxCurrencyCode = json.getString("taxCurrencyCode");
    		    String languageName = json.getString("languageName");
    		    String taxTypeCode = json.getString("taxTypeCode");
    		    String taxRate = json.getString("taxRate");
    		    String taxableBaseTotalAmount = json.getString("taxableBaseTotalAmount");
    		    String taxTotalAmount = json.getString("taxTotalAmount");
    		    String totalGrossAmount = json.getString("totalGrossAmount");
    		    String totalGrossAmountBeforeTaxes = json.getString("totalGrossAmountBeforeTaxes");
    		    String totalTaxOutputs = json.getString("totalTaxOutputs");
    		    String totalTaxesWithheld = json.getString("totalTaxesWithheld");
    		    String invoiceTotal = json.getString("invoiceTotal");
    		    String totalOutstandingAmount = json.getString("totalOutstandingAmount");
    		    String totalExecutableAmount = json.getString("totalExecutableAmount");
    		    String itemDescription = json.getString("itemDescription");
    		    String quantity = json.getString("quantity");
    		    String unitOfMeasure = json.getString("unitOfMeasure");
    		    String transactionDate = json.getString("transactionDate");
    		    boolean status = json.getBoolean("status");
    		    
    		    app.addInvoiceAEAT(blockchain, _cuenta.toLowerCase(), schemaVersion, modality,
    		    	    invoiceIssuerType, batchIdentifier, invoicesCount, totalAmountInvoices,
    		    	    totalAmountOutstanding, totalAmountExecutable, headerInvoiceCurrencyCode,
    		    	    sellerPersonTypeCode, sellerResidenceTypeCode, sellerTaxIdentificationNumber,
    		    	    sellerCorporateName, sellerAddress,
    		    	    sellerPostCode, sellerTown, sellerProvince, sellerCountryCode,
    		    	    buyerPersonTypeCode, buyerResidenceTypeCode,  buyerTaxIdentificationNumber, 
    		    	    buyerCorporateName, buyerAddress, buyerPostCode,
    		    	    buyerTown, buyerProvince, buyerCountryCode, invoiceNumber, 
    		    	    invoiceDocumentType, invoiceClass, issueDate, 
    		    	    invoiceCurrencyCode, taxCurrencyCode, languageName, taxTypeCode,
    		    	    taxRate, taxableBaseTotalAmount, taxTotalAmount, totalGrossAmount,
    		    	    totalGrossAmountBeforeTaxes, totalTaxOutputs, totalTaxesWithheld, 
    		    	    invoiceTotal, totalOutstandingAmount, totalExecutableAmount, 
    		    	    itemDescription, quantity, unitOfMeasure, transactionDate, status);    			
    		}
    		not.addNotification2(_cuenta.toLowerCase(), array.length(), _cuenta, 0);
			//res = login.login(json.getString("username"), json.getString("password"));
		} catch (JSONException e) {
			System.out.println(e);
			//return "{\"error\":\"005\",\"message\":\"ERROR AL GENERAR EL JSON\"}";
		}
    	
		try {
			res.put("message", "Invoices added correctly");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
    }
	
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/invoices", method=RequestMethod.GET)
    public ResponseEntity<String> getInvoices(@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get All Invoices");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	return app.getAllInvoices(login.getCuenta(token).toLowerCase());
	}
	
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/invoices-uploaded", method=RequestMethod.POST)
    public ResponseEntity<String> getInvoicesUploaded(@RequestHeader Map<String,String> headers,
    												@RequestBody String a){
		LOGGER.log(Level.INFO, "Llamada Get Invoices Uploaded");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	JSONArray array = null;
    	try {
			JSONObject json = new JSONObject(a);
			array = json.getJSONArray("array");
			//System.out.println(array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	return app.getInvoiceUploaded(login.getCuenta(token).toLowerCase(), array);
	}
	
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/invoices/{id}", method=RequestMethod.GET)
    public ResponseEntity<String> getInvoice(@RequestHeader Map<String,String> headers,
    		@PathVariable("id") String id){
		LOGGER.log(Level.INFO, "Llamada Get Invoice");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		login.throwError2();
    	}
    	
    	return app.getInvoice(login.getCuenta(token).toLowerCase(), id);
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/autocomplete", method=RequestMethod.GET)
    public ResponseEntity<String> getAutocomplete(@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Autocomplete");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	return app.getAutocomplete(login.getCuenta(token).toLowerCase());
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/discount", method=RequestMethod.POST)
    public ResponseEntity<String> discount(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Discount");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	try {
			JSONObject json = new JSONObject(a);
			String s = json.getString("json");
			JSONArray array = new JSONArray(s);
			
	    	String _cuenta = login.getCuenta(token);

			String priv = constants.getPrivateKey(_cuenta);
	    	String pub = constants.getPublicKey(_cuenta);
	    	String host = constants.getHost(_cuenta);
	    	Blockchain blockchain = new Blockchain(host, priv, pub);
	    	
			for(int i=0; i<array.length(); i++) {
				String o = array.getString(i);
				String disc = app.discount(blockchain, login.getCuenta(token).toLowerCase(), o);
				if(!disc.contentEquals("")) {
					not.addNotification3(login.getCuenta(token).toLowerCase(), disc);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	JSONObject res = new JSONObject();
    	try {
			res.put("message", "Discounted done correctly");
		} catch (JSONException e) {
			e.printStackTrace();
		}
   		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/check", method=RequestMethod.POST)
    public ResponseEntity<String> check(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Check");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	try {
			JSONObject json = new JSONObject(a);
			String s = json.getString("json");
			JSONArray array = new JSONArray(s);
			
	    	String _cuenta = login.getCuenta(token);

			String priv = constants.getPrivateKey(_cuenta);
	    	String pub = constants.getPublicKey(_cuenta);
	    	String host = constants.getHost(_cuenta);
	    	Blockchain blockchain = new Blockchain(host, priv, pub);
	    	
			for(int i=0; i<array.length(); i++) {
				String o = array.getString(i);
				String disc = app.check(blockchain, login.getCuenta(token).toLowerCase(), o);
				if(!disc.contentEquals("")) {
					not.addNotification3(login.getCuenta(token).toLowerCase(), disc);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	JSONObject res = new JSONObject();
    	try {
			res.put("message", "Discounted done correctly");
		} catch (JSONException e) {
			e.printStackTrace();
		}
   		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/dashboard", method=RequestMethod.GET)
    public ResponseEntity<String> getDashboard(@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get Dashboard");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	return app.dashboard(login.getCuenta(token).toLowerCase());
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/notifications", method=RequestMethod.GET)
    public ResponseEntity<String> getNotifications(@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get Notifications");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	return not.getNotifications(login.getCuenta(token).toLowerCase());
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/not-number", method=RequestMethod.GET)
    public ResponseEntity<String> getNumNotifications(@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get Num Notifications");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	return not.getNumberNotifications(login.getCuenta(token).toLowerCase());
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/readNotif", method=RequestMethod.POST)
    public ResponseEntity<String> markRead(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get Num Notifications");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	JSONObject j = null;
		try {
			j = new JSONObject(a);
			System.out.println(j);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    	String id = null;
		try {
			id = j.getString("_id");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    	
    	not.markRead(login.getCuenta(token).toLowerCase(), id);
    	
    	JSONObject res = new JSONObject();
    	try {
			res.put("message", "Mark as read");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}
	
	//POSTGRESQL
	@CrossOrigin(origins = "*")
    @RequestMapping(path="api-inblock/v1/del-notification", method=RequestMethod.POST)
	public ResponseEntity<String> delNoti(@RequestBody String a,
    		@RequestHeader Map<String,String> headers){
		LOGGER.log(Level.INFO, "Llamada Get Num Notifications");
		String token = headers.get("authorization");
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		
		if(token == null) {
			return login.throwError1();
		}
    	if(!login.validateLogin(token)) {
    		return login.throwError2();
    	}
    	
    	JSONObject j = null;
		try {
			j = new JSONObject(a);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    	String id = null;
		try {
			id = j.getString("_id");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    	
    	not.remove(login.getCuenta(token).toLowerCase(), id);
    	
    	JSONObject res = new JSONObject();
    	try {
			res.put("message", "Mark as read");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}
}