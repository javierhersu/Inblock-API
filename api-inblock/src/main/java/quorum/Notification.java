package quorum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.apache.tomcat.util.bcel.Const;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.util.PGobject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Notification {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Random rand = new Random(); 
	MongoDatabase database;
	
    public Notification(MongoDatabase db) {
        database = db;
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
   		insert(_cuenta, json);
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
   		insert(_cuenta, json);
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
   		insert(_cuenta, json);
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
   		insert(_cuenta, json);
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
   		insert(_cuenta, json);
    }
    
    public ResponseEntity<String> getNotifications(String _cuenta) {
    	JSONArray array = getDBNotifications(_cuenta);
    	
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
			json.put("notifications", getNumNotif(_cuenta));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/json");
    	return new ResponseEntity<String>(json.toString(), header, HttpStatus.OK);
    }
    
    public void markRead(String _cuenta, String _id) {
    	JSONObject json = getOneNotification(_cuenta, _id);
    	System.out.println(json);
    	try {
			json.put("read", true);
			updateRead(json.toString(), _id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    public void remove(String _cuenta, String _id) {
    	deleteNotification(_cuenta, _id);
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
    public void insert(String account, JSONObject json) {
    	
    	MongoCollection<Document> collection = database.getCollection("not-"+account);
    	collection.insertOne(Document.parse(json.toString()));
	}
    
    public JSONArray getDBNotifications(String account) {
    	
    	MongoCollection<Document> collection = database.getCollection("not-"+account);
    	Iterator<Document> it = collection.find().iterator();
		JSONArray res = new JSONArray();

    	while(it.hasNext()) {
    		Document d = it.next();
    		res.put(d.toJson());
    	}
    	
		return res;
    }
    
    public int getNumNotif(String account) {
    	
    	MongoCollection<Document> collection = database.getCollection("not-"+account);
		int res = 0;
		res = (int) collection.count();

		return res;
    }
    
    public void updateRead(String jsonString, String id) {

    	MongoCollection<Document> collection = database.getCollection("notifications");
    	
    	
		Object query = BasicDBObject.parse(jsonString);
    	BasicDBObject update = new BasicDBObject();
    	update.append("$set", query);
    	
		collection.findOneAndUpdate(Filters.eq("id", id), update);
	}
    
    public JSONObject getOneNotification(String account, String id) {

    	JSONObject res = null;
    	
    	MongoCollection<Document> collection = database.getCollection("not-"+account);
    	Document d = collection.find(Filters.eq("id", id)).first();
		try {
			res = new JSONObject(d.toJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
    }
    
    public void deleteNotification(String account, String id) {
    	
    	MongoCollection<Document> collection = database.getCollection("not-"+account);
    	collection.findOneAndDelete(Filters.eq("id", id));
		
    }
}
