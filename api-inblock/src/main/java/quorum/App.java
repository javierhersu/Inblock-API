package quorum;

import java.math.BigInteger;
//Inner impots
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.web3j.tuples.generated.Tuple2;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.Date;
import java.util.Iterator;    


public class App 
{
	//Connection connection = null;
	MongoDatabase database;
	Notification not;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public App(MongoDatabase db) {        
		not = new Notification(db);

		database = db;
	}


	/**
	 * Method to include a new invoice from a bank
	 * @param _cuenta
	 * @param _codigoIdentificacionProveedor
	 * @param _codigoIdentificacionCliente
	 * @param _numFactura
	 * @return
	 */
	 public String addInvoice(Blockchain _blockchain, String _cuenta, String schemaVersion, String modality,
    	    String invoiceIssuerType, String batchIdentifier, String invoicesCount, String totalAmountInvoices,
    	    String totalAmountOutstanding, String totalAmountExecutable, String headerInvoiceCurrencyCode,
    	    String sellerPersonTypeCode, String sellerResidenceTypeCode, String sellerTaxIdentificationNumber,
    	    String sellerCorporateName, String sellerAddress,
    	    String sellerPostCode, String sellerTown, String sellerProvince, String sellerCountryCode,
    	    String buyerPersonTypeCode, String buyerResidenceTypeCode, String buyerTaxIdentificationNumber, 
    	    String buyerCorporateName, String buyerAddress, String buyerPostCode,
    	    String buyerTown, String buyerProvince, String buyerCountryCode, String invoiceNumber, 
    	    String invoiceDocumentType, String invoiceClass, String issueDate, 
    	    String invoiceCurrencyCode, String taxCurrencyCode, String languageName, String taxTypeCode,
    	    String taxRate, String taxableBaseTotalAmount, String taxTotalAmount, String totalGrossAmount,
    	    String totalGrossAmountBeforeTaxes, String totalTaxOutputs, String totalTaxesWithheld, 
    	    String invoiceTotal, String totalOutstandingAmount, String totalExecutableAmount, 
    	    String itemDescription, String quantity, String unitOfMeasure, String transactionDate, boolean status) {
    	JSONObject json = new JSONObject();
    	Blockchain blockApp = _blockchain;
    	String inBlockID = "";
		Date today = new Date();
		String hashStr = "";
		boolean duplicate = false;
   		try {
   			JSONObject buyer = new JSONObject();
			buyer.put("buyerPersonTypeCode", buyerPersonTypeCode);
			buyer.put("buyerResidenceTypeCode", buyerResidenceTypeCode);
			buyer.put("buyerTaxIdentificationNumber", buyerTaxIdentificationNumber);
			buyer.put("buyerCorporateName", buyerCorporateName);
			buyer.put("buyerAddress", buyerAddress);
			buyer.put("buyerPostCode", buyerPostCode);
			buyer.put("buyerTown", buyerTown);
			buyer.put("buyerProvince", buyerProvince);
			buyer.put("buyerCountryCode", buyerCountryCode);
			json.put("buyer", buyer);
			JSONObject seller = new JSONObject();
			seller.put("sellerPersonTypeCode", sellerPersonTypeCode);
			seller.put("sellerResidenceTypeCode", sellerResidenceTypeCode);
			seller.put("sellerTaxIdentificationNumber", sellerTaxIdentificationNumber);
			seller.put("sellerCorporateName", sellerCorporateName);
			seller.put("sellerAddress", sellerAddress);
			seller.put("sellerPostCode", sellerPostCode);
			seller.put("sellerTown", sellerTown);
			seller.put("sellerProvince", sellerProvince);
			seller.put("sellerCountryCode", sellerCountryCode);
			json.put("seller", seller);
			JSONObject invoice = new JSONObject();
			invoice.put("schemaVersion", schemaVersion);
			invoice.put("modality", modality);
			invoice.put("invoiceIssuerType", invoiceIssuerType);
			invoice.put("batchIdentifier", batchIdentifier);
			invoice.put("invoicesCount", invoicesCount);
			invoice.put("totalAmountInvoices", totalAmountInvoices);
			invoice.put("totalAmountOutstanding", totalAmountOutstanding);
			invoice.put("totalAmountExecutable", totalAmountExecutable);
			invoice.put("headerInvoiceCurrencyCode", headerInvoiceCurrencyCode);
			invoice.put("invoiceNumber", invoiceNumber);
			invoice.put("invoiceDocumentType", invoiceDocumentType);
			invoice.put("invoiceClass", invoiceClass);
			invoice.put("issueDate", issueDate);
			invoice.put("invoiceCurrencyCode", invoiceCurrencyCode);
			invoice.put("taxCurrencyCode", taxCurrencyCode);
			invoice.put("languageName", languageName);
			invoice.put("taxTypeCode", taxTypeCode);
   			invoice.put("taxRate", taxRate);
   			invoice.put("taxableBaseTotalAmount", taxableBaseTotalAmount);
   			invoice.put("taxTotalAmount", taxTotalAmount);
   			invoice.put("totalGrossAmount", totalGrossAmount);
   			invoice.put("totalGrossAmountBeforeTaxes", totalGrossAmountBeforeTaxes);
   			invoice.put("totalTaxOutputs", totalTaxOutputs);
   			invoice.put("totalTaxesWithheld", totalTaxesWithheld);
   			invoice.put("invoiceTotal", invoiceTotal);
   			invoice.put("totalOutstandingAmount", totalOutstandingAmount);
   			invoice.put("totalExecutableAmount", totalExecutableAmount);
   			invoice.put("itemDescription", itemDescription);
   			invoice.put("quantity", quantity);
   			invoice.put("unitOfMeasure", unitOfMeasure);
   			invoice.put("transactionDate", transactionDate);
			json.put("invoice", invoice);

   			JSONObject blockchain = new JSONObject();
   			blockchain.put("uploadingTimestamp", sdf.format(today));
			//Crear Blockchain ID
	        Random rand = new Random(); 
			int num = rand.nextInt(1000);
			String digits = batchIdentifier.replaceAll("[^0-9.]", "");
			String digits2 = digits.replaceAll("-", "");
			inBlockID = digits2+today.getTime()+num;
			blockchain.put("inBlockID", inBlockID);
			blockchain.put("validInvoice", true);
			blockchain.put("existAEAT", false);
			blockchain.put("discountAccount", "");
			blockchain.put("account", _cuenta);
			//Crear HASH
			JSONObject hash = new JSONObject();
			hash.put("amount", totalAmountInvoices);
			hash.put("issueDate", issueDate);
			hash.put("seller", sellerTaxIdentificationNumber);
			hash.put("buyer", buyerTaxIdentificationNumber);
			hashStr = makeHash(hash.toString());

			duplicate = getHash("inblock", hashStr);
   			if(duplicate == false)
   				blockchain.put("discountStatus", status);
   			else
   				blockchain.put("discountStatus", false);
			blockchain.put("doubledInvoice", duplicate);
			blockchain.put("entityDiscountStatus", false);
			blockchain.put("discountCheckExecuted", false);
			blockchain.put("existenceCheckExecuted", false);
			blockchain.put("existenceStatus", false);

			blockchain.put("hash", hashStr);

			blockchain.put("duplicate", duplicate);

			JSONArray array = new JSONArray();
			JSONObject steps = new JSONObject();
			steps.put("id", 1);
			steps.put("step", "Upload Invoice");
			steps.put("timestamp", sdf.format(today));
			array.put(steps);
			json.put("steps", array);
			json.put("blockchain", blockchain);

			if(duplicate) {
				System.out.println("duplicated");
				//blockchain.put("account", getCuenta(connection, "inblock", hashStr));

				//not.addNotification1(_cuenta, inBlockID, getCuenta(connection, "inblock", hashStr));
				//not.addNotification1(getCuenta(connection, "inblock", hashStr), getID(connection, "inblock", hashStr), getCuenta(connection, "inblock", hashStr));
				//not.addNotification1("inblock", inBlockID, getCuenta(connection, "inblock", hashStr));
			}else {
				try {
					//blockchain.put("account", _cuenta);
					blockApp.init(hashStr);
					System.out.println("Blockchain add: "+hashStr);
					if(status == true) {
						blockApp.setDiscount(hashStr);
						System.out.println("Blockchain discount: "+hashStr);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
   		}catch(JSONException e) {
   			e.printStackTrace();
   		}

   		insert(_cuenta.toLowerCase(), json.toString());
   		insert("inblock", json.toString());

		boolean existAEAT = getExistAEAT(hashStr);
   		if(!duplicate & !existAEAT)
			insert("aeat", json.toString());


    	return inBlockID;
    }



    public String addInvoiceAEAT(Blockchain _blockchain, String _cuenta, String schemaVersion, String modality,
    	    String invoiceIssuerType, String batchIdentifier, String invoicesCount, String totalAmountInvoices,
    	    String totalAmountOutstanding, String totalAmountExecutable, String headerInvoiceCurrencyCode,
    	    String sellerPersonTypeCode, String sellerResidenceTypeCode, String sellerTaxIdentificationNumber,
    	    String sellerCorporateName, String sellerAddress,
    	    String sellerPostCode, String sellerTown, String sellerProvince, String sellerCountryCode,
    	    String buyerPersonTypeCode, String buyerResidenceTypeCode, String buyerTaxIdentificationNumber, 
    	    String buyerCorporateName, String buyerAddress, String buyerPostCode,
    	    String buyerTown, String buyerProvince, String buyerCountryCode, String invoiceNumber, 
    	    String invoiceDocumentType, String invoiceClass, String issueDate, 
    	    String invoiceCurrencyCode, String taxCurrencyCode, String languageName, String taxTypeCode,
    	    String taxRate, String taxableBaseTotalAmount, String taxTotalAmount, String totalGrossAmount,
    	    String totalGrossAmountBeforeTaxes, String totalTaxOutputs, String totalTaxesWithheld, 
    	    String invoiceTotal, String totalOutstandingAmount, String totalExecutableAmount, 
    	    String itemDescription, String quantity, String unitOfMeasure, String transactionDate, boolean status) {
    	JSONObject json = new JSONObject();

    	Blockchain blockApp = _blockchain;
    	String inBlockID = "";
		Date today = new Date();
		String hashStr = "";
		boolean duplicate = false;
   		try {
   			JSONObject buyer = new JSONObject();
			buyer.put("buyerPersonTypeCode", buyerPersonTypeCode);
			buyer.put("buyerResidenceTypeCode", buyerResidenceTypeCode);
			buyer.put("buyerTaxIdentificationNumber", buyerTaxIdentificationNumber);
			buyer.put("buyerCorporateName", buyerCorporateName);
			buyer.put("buyerAddress", buyerAddress);
			buyer.put("buyerPostCode", buyerPostCode);
			buyer.put("buyerTown", buyerTown);
			buyer.put("buyerProvince", buyerProvince);
			buyer.put("buyerCountryCode", buyerCountryCode);
			json.put("buyer", buyer);
			JSONObject seller = new JSONObject();
			seller.put("sellerPersonTypeCode", sellerPersonTypeCode);
			seller.put("sellerResidenceTypeCode", sellerResidenceTypeCode);
			seller.put("sellerTaxIdentificationNumber", sellerTaxIdentificationNumber);
			seller.put("sellerCorporateName", sellerCorporateName);
			seller.put("sellerAddress", sellerAddress);
			seller.put("sellerPostCode", sellerPostCode);
			seller.put("sellerTown", sellerTown);
			seller.put("sellerProvince", sellerProvince);
			seller.put("sellerCountryCode", sellerCountryCode);
			json.put("seller", seller);
			JSONObject invoice = new JSONObject();
			invoice.put("schemaVersion", schemaVersion);
			invoice.put("modality", modality);
			invoice.put("invoiceIssuerType", invoiceIssuerType);
			invoice.put("batchIdentifier", batchIdentifier);
			invoice.put("invoicesCount", invoicesCount);
			invoice.put("totalAmountInvoices", totalAmountInvoices);
			invoice.put("totalAmountOutstanding", totalAmountOutstanding);
			invoice.put("totalAmountExecutable", totalAmountExecutable);
			invoice.put("headerInvoiceCurrencyCode", headerInvoiceCurrencyCode);
			invoice.put("invoiceNumber", invoiceNumber);
			invoice.put("invoiceDocumentType", invoiceDocumentType);
			invoice.put("invoiceClass", invoiceClass);
			invoice.put("issueDate", issueDate);
			invoice.put("invoiceCurrencyCode", invoiceCurrencyCode);
			invoice.put("taxCurrencyCode", taxCurrencyCode);
			invoice.put("languageName", languageName);
			invoice.put("taxTypeCode", taxTypeCode);
   			invoice.put("taxRate", taxRate);
   			invoice.put("taxableBaseTotalAmount", taxableBaseTotalAmount);
   			invoice.put("taxTotalAmount", taxTotalAmount);
   			invoice.put("totalGrossAmount", totalGrossAmount);
   			invoice.put("totalGrossAmountBeforeTaxes", totalGrossAmountBeforeTaxes);
   			invoice.put("totalTaxOutputs", totalTaxOutputs);
   			invoice.put("totalTaxesWithheld", totalTaxesWithheld);
   			invoice.put("invoiceTotal", invoiceTotal);
   			invoice.put("totalOutstandingAmount", totalOutstandingAmount);
   			invoice.put("totalExecutableAmount", totalExecutableAmount);
   			invoice.put("itemDescription", itemDescription);
   			invoice.put("quantity", quantity);
   			invoice.put("unitOfMeasure", unitOfMeasure);
   			invoice.put("transactionDate", transactionDate);
			json.put("invoice", invoice);

   			JSONObject blockchain = new JSONObject();
   			blockchain.put("uploadingTimestamp", sdf.format(today));
			blockchain.put("discountStatus", status);
			blockchain.put("discountAccount", "");
			//Crear Blockchain ID
	        Random rand = new Random(); 
			int num = rand.nextInt(1000);
			String digits = batchIdentifier.replaceAll("[^0-9.]", "");
			String digits2 = digits.replaceAll("-", "");
			inBlockID = digits2+today.getTime()+num;
			blockchain.put("inBlockID", inBlockID);
			blockchain.put("validInvoice", true);
			blockchain.put("existAEAT", true);

			//Crear HASH
			JSONObject hash = new JSONObject();
			hash.put("amount", totalAmountInvoices);
			hash.put("issueDate", issueDate);
			hash.put("seller", sellerTaxIdentificationNumber);
			hash.put("buyer", buyerTaxIdentificationNumber);
			hashStr = makeHash(hash.toString());

			duplicate = getHash("inblock", hashStr);
			blockchain.put("doubledInvoice", duplicate);
			blockchain.put("entityDiscountStatus", false);
			blockchain.put("discountCheckExecuted", false);
			blockchain.put("existenceCheckExecuted", false);
			blockchain.put("existenceStatus", false);

			blockchain.put("hash", hashStr);

			blockchain.put("account", _cuenta);
			blockchain.put("duplicate", duplicate);

			JSONArray array = new JSONArray();
			JSONObject steps = new JSONObject();
			steps.put("id", 1);
			steps.put("step", "Upload Invoice");
			steps.put("timestamp", sdf.format(today));
			array.put(steps);
			json.put("steps", array);
			json.put("blockchain", blockchain);

   		}catch(JSONException e) {
   			e.printStackTrace();
   		}

   		try {
   			blockApp.init(hashStr);
			blockApp.setExistence(hashStr);
			System.out.println("Blockchain check existence: "+hashStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

   		if(!getHash("aeat", hashStr)) {
   			insert(_cuenta.toLowerCase(), json.toString());
   		}else {
   			String blockID = getID("aeat", hashStr);
   			deleteAEAT("aeat", blockID);
   			insert(_cuenta.toLowerCase(), json.toString());
   		}

    	return json.toString();
    }



	/**
	 * Method to obtain all the invoices
	 * @param _cuenta
	 * @return
	 */
	public ResponseEntity<String> getAllInvoices(String _cuenta) {

		if(_cuenta.contentEquals("AEF"))
			_cuenta="inBlock";

		JSONArray array = getAll(_cuenta);

		JSONArray newJsonArray = new JSONArray();
		for (int i = array.length()-1; i>=0; i--) {
			try {
				newJsonArray.put(array.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		return new ResponseEntity<String>(newJsonArray.toString(), header, HttpStatus.OK);
	}

	/**
	 * Method to obtain all the invoices
	 * @param _cuenta
	 * @return
	 */
	public ResponseEntity<String> getInvoice(String _cuenta, String _id) {

		if(_cuenta.contentEquals("AEF"))
			_cuenta="inBlock";

		JSONObject json = getInvoiceByID(_cuenta.toLowerCase(), _id);

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		return new ResponseEntity<String>(json.toString(), header, HttpStatus.OK);
	}

	/**
	 * Method to obtain all the invoices
	 * @param _cuenta
	 * @return
	 */
	public ResponseEntity<String> getInvoiceUploaded(String _cuenta, JSONArray array) {

		if(_cuenta.contentEquals("AEF"))
			_cuenta="inBlock";

		JSONArray jsonArr = new JSONArray();
		for(int i=0; i<array.length(); i++) {
			JSONObject json;
			try {
				json = getInvoiceByID(_cuenta.toLowerCase(), array.getString(i));
				jsonArr.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		return new ResponseEntity<String>(jsonArr.toString(), header, HttpStatus.OK);
	}

	/**
	 * Method to obtain all the invoices
	 * @param _cuenta
	 * @return
	 */
	public String discount(Blockchain _blockchain, String _cuenta, String _id) {

		Blockchain blockApp = _blockchain;
		JSONObject json = addNewStep(_cuenta.toLowerCase(), _id, "Discount");
		json.remove("_id");
		String hash = null;
		try {
			hash = json.getJSONObject("blockchain").getString("hash");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		boolean checked = false;
		try {
			checked = json.getJSONObject("blockchain").getBoolean("discountCheckExecuted");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		if(!checked) {
			try {
				json.getJSONObject("blockchain").put("discountStatus", true);
				json.getJSONObject("blockchain").put("discountCheckExecuted", true);
				json.getJSONObject("blockchain").put("discountAccount", _cuenta);
				System.out.println("Blockchain discount 2: "+hash);
				blockApp.setDiscount(hash);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			updateDiscount(_cuenta.toLowerCase(), json.toString(), _id);	   		
			updateDiscount("inblock", json.toString(), _id);
			//updateAEAT(connection, hash);

			return "";
		}

		if(getHash("inblock", hash)) {
			//Añadir notificacion de que ya existe esta factura.
			BigInteger big = null;
			try {
				big = blockApp.getDiscount(hash);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			int discStatus = Integer.valueOf(String.valueOf(big));

			if(discStatus == 1) {   				
				try {
					json.getJSONObject("blockchain").put("discountStatus", true);
					json.getJSONObject("blockchain").put("discountCheckExecuted", true);
					json.getJSONObject("blockchain").put("discountAccount", _cuenta);

					System.out.println("Blockchain discount: "+hash);
					blockApp.setDiscount(hash);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				updateDiscount(_cuenta.toLowerCase(), json.toString(), _id);	   		
				updateDiscount("inblock", json.toString(), _id);
				updateAEAT(hash);

				return "";
			}else {
				System.out.println("No se puede descontar, ya descontada");
				String _cuentaDescuento = getDiscountAccount(_cuenta, hash);
				try {
					json.getJSONObject("blockchain").put("discountAccount", _cuentaDescuento);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				updateDiscount(_cuenta.toLowerCase(), json.toString(), _id);
				updateDiscount("inblock", json.toString(), _id);
				updateAEAT(hash);

				return _id;
			}
			//Si no esta descontada descontar
			//   ¿¿¿Si esta descontada actualiza aunque no lo modifique??
			//Añadir notificacion de que existe esta factura y ya esta descontada
		}else {
			try {
				json.getJSONObject("blockchain").put("discountStatus", true);
				json.getJSONObject("blockchain").put("discountCheckExecuted", true);
				json.getJSONObject("blockchain").put("discountAccount", _cuenta);

				System.out.println("Blockchain discount 2: "+hash);
				blockApp.setDiscount(hash);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			updateDiscount(_cuenta.toLowerCase(), json.toString(), _id);	   		
			updateDiscount("inblock", json.toString(), _id);
			updateAEAT(hash);

			return "";
		}
	}

	/**
	 * Method to obtain all the invoices
	 * @param _cuenta
	 * @return
	 */
	public String check(Blockchain _blockchain, String _cuenta, String _id) {

		Blockchain blockApp = _blockchain;
		JSONObject res = addNewStep(_cuenta.toLowerCase(), _id, "Check");
		System.out.println(_id);

		String hash = null;
		try {
			hash = res.getJSONObject("blockchain").getString("hash");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}	
		System.out.println(hash);


		boolean duplicate = false;
		String cuenta = "";
		try {
			duplicate = res.getJSONObject("blockchain").getBoolean("doubledInvoice");
			cuenta = res.getJSONObject("blockchain").getString("account");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}

		if(duplicate) {
			//not.addNotification1(_cuenta, _id, getCuenta("inblock", hash));
			//not.addNotification1(cuenta, getID(cuenta, hash), getCuenta(cuenta, hash));
			//not.addNotification1("inblock", _id, getCuenta("inblock", hash));
		}

		boolean discountStatus = false;
		boolean existenceStatus = false;
		try {
			Tuple2<BigInteger, Boolean> tuple = blockApp.getAll(hash);
			System.out.println("Blockchain add: "+tuple);
			int i = Integer.valueOf(String.valueOf(tuple.getValue1()));
			if(i == 1)
				discountStatus = false;
			else if(i > 1)
				discountStatus = true;
			existenceStatus = tuple.getValue2();
		} catch (Exception e1) {
			e1.printStackTrace();
		}


		try {
			res.getJSONObject("blockchain").put("discountStatus", discountStatus);
			res.getJSONObject("blockchain").put("discountCheckExecuted", true);
			res.getJSONObject("blockchain").put("existenceStatus", existenceStatus);
			res.getJSONObject("blockchain").put("existenceCheckExecuted", true);
			String _cuentaDescuento = getDiscountAccount(_cuenta, hash);
			res.getJSONObject("blockchain").put("discountAccount", _cuentaDescuento);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(res.toString());
		
		res.remove("_id");
		updateCheck(_cuenta.toLowerCase(), existenceStatus, _id);
		updateCheck("inblock", existenceStatus, _id);
		updateDiscount(_cuenta.toLowerCase(), res.toString(), _id);
		updateDiscount("inblock", res.toString(), _id);

		return "";
	}

	public ResponseEntity<String> dashboard(String _cuenta) {
		JSONObject res = null;

		if(_cuenta.contentEquals("AEF"))
			_cuenta="inBlock";

		res = getDashboard(_cuenta.toLowerCase());
		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}


	public ResponseEntity<String> getAutocomplete(String _cuenta) {
		JSONArray res = null;

		if(_cuenta.contentEquals("AEF"))
			_cuenta="inBlock";

		res = autocomplete(_cuenta.toLowerCase());

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
		return new ResponseEntity<String>(res.toString(), header, HttpStatus.OK);
	}

	/************************** CREATE HASH *******************************/

	public String makeHash(String input)
	{
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("SHA1");
			//Add password bytes to digest
			md.update(input.getBytes());
			//Get the hash's bytes 
			byte[] bytes = md.digest();
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		//System.out.println(generatedPassword);

		return "0x"+generatedPassword;
	}


	/******************** SQL METHODS ***********************************************/

	 public void insert(String cuenta, String jsonString) {
		 
		 MongoCollection<Document> collection = database.getCollection(cuenta);
		 
		 collection.insertOne(Document.parse(jsonString));
	}

	public void updateDiscount(String cuenta, String jsonString, String inblockID) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Iterator<Document> it = collection.find().iterator();

		Object query = BasicDBObject.parse(jsonString);
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", query);
		collection.findOneAndUpdate(Filters.eq("blockchain.inBlockID", inblockID), setQuery);

	}


	public void updateAEAT(String hash) {

		MongoCollection<Document> collection = database.getCollection("aeat");
		Iterator<Document> it = collection.find().iterator();

		BasicDBObject query = new BasicDBObject();
		query.append("blockchain.discountStatus", true);
		BasicDBObject setQuery = new BasicDBObject();
		setQuery.append("$set", query);
		collection.findOneAndUpdate(Filters.eq("blockchain.hash", hash), setQuery);

	}


	public boolean getExistAEAT(String hash) {
		MongoCollection<Document> collection = database.getCollection("aeat");
		int count = (int) collection.count(Filters.eq("blockchain.hash", hash));

		boolean res = false;
		if(count > 0)
			res = true;

		return res;
	}


	public void updateCheck(String cuenta, boolean existenceStatus, String inblockID) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.inBlockID", inblockID)).first();
		JSONObject o = null;
		try {
			o = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		BasicDBObject query = new BasicDBObject();
		query.append("blockchain.existenceStatus", existenceStatus);
		query.append("blockchain.existenceCheckExecuted", true);
		BasicDBObject update = new BasicDBObject();
		update.append("$set", query);
		collection.updateOne(Filters.eq("blockchain.inBlockID", inblockID), update);

	}


	public void deleteAEAT(String cuenta, String id) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		collection.findOneAndDelete(Filters.eq("blockchain.inBlockID", id));
	}

	public JSONArray getAll(String cuenta) {

		MongoCollection<Document> collection = database.getCollection(cuenta);
		Iterator<Document> it = collection.find().iterator();
		JSONArray array = new JSONArray();
		while(it.hasNext()) {
			Document d = it.next();
			JSONObject obj = null;
			try {
				obj = new JSONObject(d.toJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);

		}

		return array;
	}


	public boolean getCheckStatus(String hash) {
		MongoCollection<Document> collection = database.getCollection("aeat");
		int cont = (int) collection.count(Filters.eq("blockchain.hash", hash));

		boolean exists = false;
		if(cont > 0)
			exists = true;

		return exists;
	}


	public boolean getHash(String cuenta, String hash) {

		MongoCollection<Document> collection = database.getCollection(cuenta);
		int count = (int) collection.count(Filters.eq("blockchain.hash", hash));


		boolean exists = false;
		if(count > 0)
			exists = true;

		return exists;
	}


	public String getHashStr(String cuenta, String inblockID) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.inBlockID", inblockID)).first();
		JSONObject o = null;
		try {
			o = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String hash = null;
		try {
			hash = o.getJSONObject("blockchain").getString("hash");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return hash;
	}


	public String getCuenta(String cuenta, String hash) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.hash", hash)).first();
		JSONObject o = null;
		try {
			o = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String account = null;
		try {
			account = o.getJSONObject("blockchain").getString("account");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return account;
	}

	public String getDiscountAccount(String _cuenta, String hash) {

		String disAccount = "";
		MongoCollection<Document> collection = database.getCollection(_cuenta);
		Document object = collection.find(Filters.eq("blockchain.hash", hash)).first();
		JSONObject o = null;
		try {
			o = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String account = null;
		try {
			account = o.getJSONObject("blockchain").getString("discountAccount");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(!account.contentEquals("")) {
			disAccount = account; 
		}

		return disAccount;
	}


	public String getID(String cuenta, String hash) {
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.hash", hash)).first();
		JSONObject res = null;
		try {
			res = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String account = "";

		try {
			account = res.getJSONObject("blockchain").getString("inBlockID");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return account;
	}


	public JSONObject getInvoiceByID(String cuenta, String inBlockID) {

		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.inBlockID", inBlockID)).first();
		JSONObject res = null;
		try {
			res = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return res;
	}


	public boolean getDuplicated(String cuenta, String inBlockID) {

		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document object = collection.find(Filters.eq("blockchain.inBlockID", inBlockID)).first();
		JSONObject res = null;
		try {
			res = new JSONObject(object.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}


		boolean duplicate = false;
		try {
			duplicate = res.getJSONObject("blockchain").getBoolean("duplicate");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return duplicate;
	}


	public JSONObject addNewStep(String cuenta, String inBlockID, String step) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MongoCollection<Document> collection = database.getCollection(cuenta);
		Document doc = collection.find(Filters.eq("blockchain.inBlockID", inBlockID)).first();
		JSONObject res = null;
		try {
			res = new JSONObject(doc.toJson());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			JSONArray steps = res.getJSONArray("steps");
			String timestamp = sdf.format(new Date());
			int id = steps.getJSONObject(steps.length()-1).getInt("id");
			JSONObject jStep = new JSONObject();
			jStep.put("id", id+1);
			jStep.put("timestamp", timestamp);
			jStep.put("step", step);

			steps.put(jStep);

		} catch (JSONException e) {
			e.printStackTrace();
		}		

		return res;
	}

	public JSONArray autocomplete(String cuenta) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JSONArray res = new JSONArray();

		MongoCollection<Document> collection = database.getCollection(cuenta);
		Iterator<Document> cursor = collection.find().iterator();
		while(cursor.hasNext()) {
			Document object = cursor.next();
			try {
				JSONObject result = new JSONObject(object.toJson());
				JSONObject json2 = new JSONObject();

				String inblockID;

				inblockID = result.getJSONObject("blockchain").getString("inBlockID");

				String amount = result.getJSONObject("invoice").getString("totalAmountInvoices");
				String invoiceNumber = result.getJSONObject("invoice").getString("invoiceNumber");
				json2.put("id", inblockID);
				String name = inblockID + "("+invoiceNumber+" | "+amount+" €)";
				json2.put("name", name);

				res.put(json2);

			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}

		return res;
	}


	public JSONObject getDashboard(String cuenta) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		MongoCollection<Document> collection = database.getCollection(cuenta);

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
		Iterator<Document> it= collection.find().iterator();

		while (it.hasNext()) {
			Document d = it.next();
			JSONObject json = null;
			try {
				json = new JSONObject(d.toJson());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			try {
				String account = json.getJSONObject("blockchain").getString("account");
				String amountStr = json.getJSONObject("invoice").getString("totalAmountInvoices");
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

}
