package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.mongodb.client.model.Aggregates;
import com.reit.beans.DealUserRole;
import com.reit.beans.PropDealBean;
import com.reit.util.GsonUtils;

public class PropDealService {

	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = GsonUtils.getGson();

	public ObjectId createDeal(PropDealBean propDeal) {
		Preconditions.checkNotNull(propDeal, "Prop Deal is null");
		Preconditions.checkNotNull(propDeal.getPropObjId(), "Prop ID address is null");
		Preconditions.checkNotNull(propDeal.getTenantObjId(), "Tenant ID is null");

		Preconditions.checkArgument(getPropDeal(propDeal.getPropObjId()) == null, "Property exists in data store");

		MongoDao mongoDao = new MongoDao();
		ObjectId objectId = new ObjectId();
		try {
			// TODO - Check if property exists before create.
			objectId = mongoDao.insert(MongoCollEnum.PropDeal.toString(), gson.toJson(propDeal));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		return objectId;
	}

	public List<DealUserRole> getDealUsers(ObjectId propId) {
		List<DealUserRole> userRoleList = new ArrayList<>();

		PropDealBean propDeal = getPropDeal(propId);
		if (propDeal != null && propDeal.getUserRoleList() != null) {
			userRoleList = propDeal.getUserRoleList();
		}

		return userRoleList;
	}

	public PropDealBean getPropDeal(ObjectId propId) {
		PropDealBean propDeal = null;
		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("propObjId", propId);

		List<PropDealBean> results = mongoDao.aggregate(MongoCollEnum.PropDeal.toString(), PropDealBean.class,
				Aggregates.match(filter), 1);

		if (results != null && results.size() > 0) {
			propDeal = results.get(0);
		}

		return propDeal;
	}
}
