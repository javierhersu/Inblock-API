package quorum;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class Test {

	public static void main(String[] args) throws JSONException {
		MongoClient mongoClient = mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		MongoDatabase database = mongoClient.getDatabase("inblockDB");
		

		JSONObject res = null;
		MongoCollection<Document> collection = database.getCollection("santander");
		Document object = collection.find(Filters.eq("blockchain.inBlockID", "2500100100011588775534350310")).first();
		System.out.println(object.toJson());
	}
}
