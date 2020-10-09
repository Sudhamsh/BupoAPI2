package com.bupo.dao;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDao {
	private static MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
	private static MongoClient mongoClient = new MongoClient(connectionString);
	private static MongoDatabase database = mongoClient.getDatabase("mydb");

	private static Map<String, MongoCollection<Document>> collectionMap = new HashMap<String, MongoCollection<Document>>();

	static {

		collectionMap.put("test", database.getCollection("test"));
		collectionMap.put("autoHome", database.getCollection("autoHome"));
		collectionMap.put("user", database.getCollection("user"));

	}

	public void insert(String collectionName, Document doc) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		if (collection == null) {
			throw new RuntimeException("Collection Not Found");
		}
		collection.insertOne(doc);
	}

	public void insert(String collectionName, String docStr) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		if (collection == null) {
			throw new RuntimeException("Collection Not Found");
		}
		Document doc = Document.parse(docStr);
		collection.insertOne(doc);
	}

	public void findAndReplace(String collectionName, Bson filter, String docStr) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		Document newDoc = Document.parse(docStr);
		collection.replaceOne(filter, newDoc);

	}

	public FindIterable<Document> find(String collectionName, Bson filter, int limit) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		if (filter == null) {
			return collection.find().limit(limit);
		}

		return collection.find(filter).limit(limit);

	}

	public Document findOne(String collectionName, Bson filter) {

		MongoCollection<Document> collection = collectionMap.get(collectionName);
		return collection.find(filter).first();

	}

}
