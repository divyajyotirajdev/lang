/*
 * @author DivyaJyoti Rajdev
 * NOTE: values in this class have been intentionally obfuscated
 * please use given code as a skeleton or provide your own input streat 
 */

package com.carma.lang;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.*;


public class MongoClass {
	private static ObjectId lastDocId=new ObjectId("FFFFFFFFFFFFFFFFFFFFFFFF");
	public static DB getDataBase() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient();
		mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
  		DB db = mongoClient.getDB( "obsfucated name" );
  		return db;
	}
	
	public static DBCollection getCollection(DB db) throws UnknownHostException {
  		DBCollection coll = db.getCollection("obsfucated name");
  		return coll;
	}
	
	public static void updateLastDocId(ObjectId id){
		lastDocId=id;
	}
	
	/* creates DocProperty type object of each doc 
	 * ----------------------------------------------------------------------------------
	 * @input - int count that specifies limit
	 * @output - arrayList of docProperty objects containing headline, content information
	 */
	public static ArrayList<DocProperty> getDocsOfEng(int count) throws UnknownHostException{
		DB db = getDataBase();
		DBCollection coll=getCollection(db);
		ArrayList<DocProperty> data = new ArrayList<DocProperty>();
		
  		/* creates conditions for querying database and returns relevant docs of English
  		 * ----------------------------------------------------------------------------------
  		 * @attributes:  no twitter, "_id" greater than lastDocId
  		 */
		DBObject andQuery=buildQueryEng();
		DBCursor cursor = coll.find(andQuery).sort(new BasicDBObject("_id",-1)).limit(count);
		try {
	  		   while(cursor.hasNext()) {
	  		       DBObject doc = cursor.next();
	  		       DBObject interaction = (DBObject) doc.get("obsfucated category");
	  		       String headline = (String) interaction.get("obsfucated fieldname");
	  		       String content = (String) interaction.get("obsfucated fieldname");
	  		       	/* updates lastDocId, adds headline,content to data collection
	  		  		 */
	  		       updateLastDocId((ObjectId)doc.get( "_id" ));
	  		       data.add(new DocProperty(headline,content));
	  		   }
	  		} finally {
	  		   cursor.close();
	  		}
		
		//System.out.println(data.size());
		return data;
	}
	
	public static DBObject buildQueryEng() {
		QueryBuilder query = new QueryBuilder();
		query.put("_id").lessThanEquals(lastDocId);
		query.and("obsfucated filedname").is("en");
		query.and("obsfucated fieldname").notEquals("twitter");
		DBObject queryDB= new BasicDBObject();
		queryDB.putAll(query.get());
		return queryDB;
	}
	
	public static List<String> getAllLang() throws UnknownHostException{
		DB db = getDataBase();
		DBCollection coll=getCollection(db);
		@SuppressWarnings("unchecked")
		List<String> allLang= coll.distinct("obsfucated fieldname");
		return allLang;	
	}

}


