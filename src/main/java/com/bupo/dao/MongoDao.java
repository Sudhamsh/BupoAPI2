package com.bupo.dao;

import static com.mongodb.client.model.Aggregates.limit;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.common.base.Preconditions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDao {

	static ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
	static CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
	static CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
			pojoCodecRegistry);
	static MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
			.codecRegistry(codecRegistry).build();
	private static MongoClient mongoClient = MongoClients.create(clientSettings);
	private static MongoDatabase database = mongoClient.getDatabase("mydb");

	private static Map<String, MongoCollection<Document>> collectionMap = new HashMap<String, MongoCollection<Document>>();

	static {

		collectionMap.put("test", database.getCollection("test"));
		collectionMap.put("autoHome", database.getCollection("autoHome"));
		collectionMap.put("user", database.getCollection("user"));
		collectionMap.put("quote", database.getCollection("quote"));
		collectionMap.put("quoteSummary", database.getCollection("quoteSummary"));
		collectionMap.put("quoteDetail", database.getCollection("quoteDetail"));
		collectionMap.put("property", database.getCollection("property"));
		collectionMap.put("zipMetrics", database.getCollection("zipMetrics"));
		collectionMap.put("companyMetrics", database.getCollection("companyMetrics"));
		collectionMap.put("saasTenant", database.getCollection("saasTenant"));
		collectionMap.put("teams", database.getCollection("teams"));

	}

	public void insert(String collectionName, Document doc) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		if (collection == null) {
			throw new RuntimeException("Collection Not Found");
		}
		collection.insertOne(doc);
	}

	public ObjectId insert(String collectionName, String docStr) {
		MongoCollection<Document> collection = collectionMap.get(collectionName);
		if (collection == null) {
			throw new RuntimeException("Collection Not Found");
		}
		Document doc = Document.parse(docStr);
		collection.insertOne(doc);
		return (ObjectId) doc.get("_id");
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

	public <T> FindIterable<T> find(String collectionName, Class<T> clazz, Bson filter, int limit) {
		MongoCollection<T> collection = database.getCollection(collectionName, clazz);
		if (filter == null) {
			return collection.find(clazz).limit(limit);
		}

		return collection.find(filter, clazz).limit(limit);

	}

	public <T> ArrayList<T> aggregate(String collectionName, Class<T> clazz, Bson filter, int limit) {
		Preconditions.checkNotNull(filter, "Filter is null");

		MongoCollection<T> collection = database.getCollection(collectionName, clazz);
		Bson limitFilter = limit(3);
		return collection.aggregate(Arrays.asList(filter, limitFilter), clazz).into(new ArrayList<>());

	}

	public Document findOne(String collectionName, Bson filter) {

		MongoCollection<Document> collection = collectionMap.get(collectionName);
		return collection.find(filter).first();

	}

	public <T> T findOne(String collectionName, Class<T> clazz, Bson filter) {

		MongoCollection<Document> collection = collectionMap.get(collectionName);
		return collection.find(filter, clazz).first();

	}

}
