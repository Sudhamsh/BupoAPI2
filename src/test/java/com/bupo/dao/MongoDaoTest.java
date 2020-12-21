package com.bupo.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.Arrays;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;

import com.bupo.enums.MongoCollEnum;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.reit.beans.PropertyBean;

public class MongoDaoTest {
	final Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1)
			.append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
			.append("info", new Document("x", 203).append("y", 102));

	@Test
	public void insertTest_hp() {
		try {
			MongoDao mongoDao = new MongoDao();

			mongoDao.insert("test", doc);
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	@Test
	public void insertTest_cc() {
		MongoDao mongoDao = new MongoDao();

		try {
			mongoDao.insert(String.valueOf(System.currentTimeMillis()), doc);
			Assert.fail("Invalid collection value was accepted");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test
	public void findOne() {
		MongoDao mongoDao = new MongoDao();
		Bson filter = and(eq("code", "TQ1W2-GOMA0-BXZQ0-CSCFV"), eq("lastName", "lName"));
		Document policyRequest = mongoDao.findOne(MongoCollEnum.AutoHome.toString(), filter);
		System.out.println(policyRequest.toString());
		Assert.assertNotNull("Search failed", policyRequest);

	}

	@Test
	public void findOneBean() {
		MongoDao mongoDao = new MongoDao();
		try {
			FindIterable<PropertyBean> policyRequests = mongoDao.find(MongoCollEnum.Property.toString(),
					PropertyBean.class, Filters.gt("cap", 3), 10);
			for (PropertyBean property : policyRequests) {
				System.out.println(property.getAskingPrice());
			}
			Assert.assertNotNull("Search failed", policyRequests);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
