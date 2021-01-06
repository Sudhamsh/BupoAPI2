package com.reit.services;

import static com.bupo.enums.MongoCollEnum.SaasTenant;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.reit.beans.saas.tenant.Department;
import com.reit.beans.saas.tenant.SaasTenantBean;
import com.reit.beans.saas.tenant.Team;
import com.reit.beans.saas.tenant.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SaasTenantService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private SaasTenantBean saasTenant;

	public SaasTenantService(SaasTenantBean saasTenant) {
		this.saasTenant = saasTenant;

	}

	public ObjectId createTenant() {

		Preconditions.checkNotNull(saasTenant, "Saas Tenant is null");
		Preconditions.checkNotNull(saasTenant.getTenantName(), "Tenant name  is null");
		Preconditions.checkArgument(getSaasTenant(saasTenant.getTenantName()) == null, "Tenant exists in data store");

		MongoDao mongoDao = new MongoDao();
		ObjectId id = null;
		try {

			AuthorizationService.hasAuthorized();
			id = mongoDao.insert(MongoCollEnum.SaasTenant.toString(), gson.toJson(saasTenant));

		} catch (NotAuthorizedException e) {
			logger.error(e);
			throw new NotAuthorizedException(e);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		return id;

	}

	public SaasTenantBean getSaasTenant(String tenantName) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("tenantName", tenantName);

		List<SaasTenantBean> results = mongoDao.aggregate(SaasTenant.toString(), SaasTenantBean.class,
				Aggregates.match(Filters.and(filter)), 1);

		if (results == null || results.size() != 1) {
			return null;
		}

		return results.get(0);

	}

	public SaasTenantBean getSaasTenant(ObjectId tenantId) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("_id", tenantId);

		List<SaasTenantBean> results = mongoDao.aggregate(SaasTenant.toString(), SaasTenantBean.class,
				Aggregates.match(Filters.and(filter)), 1);

		if (results == null || results.size() != 1) {
			return null;
		}

		return results.get(0);

	}

	public Department getDepartment(List<Department> departments, String deptName) {
		Preconditions.checkNotNull(departments, "Departments is null");
		Preconditions.checkNotNull(deptName, "Dept name is null");

		Department department = null;
		for (Department tempDept : departments) {
			if (deptName.equals(tempDept.getName())) {
				department = tempDept;
				break;
			}
		}

		return department;
	}

	public ObjectId createTeam(Team team) {
		Preconditions.checkNotNull(team, "Team object is null");
		Preconditions.checkNotNull(team.getTenantId(), "TenantId is null");
		Preconditions.checkArgument(getSaasTenant(team.getTenantId()) != null, "Tenant doesn't exist");
		Preconditions.checkArgument(getTeam(team.getTenantId(), team.getName()) == null, "Team in this tenant exist");

		// update db
		MongoDao mongoDao = new MongoDao();
		ObjectId teamId = null;

		try {
			teamId = mongoDao.insert(MongoCollEnum.Teams.toString(), gson.toJson(team));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return teamId;
	}

	public Team getTeam(ObjectId tenantId, String teamName) {
		Preconditions.checkNotNull(tenantId, "TenantId is null");
		Preconditions.checkNotNull(teamName, "Team name value is null");

		MongoDao mongoDao = new MongoDao();
		List<Bson> fieldList = new ArrayList();

		Bson filter = eq("name", teamName);
		fieldList.add(filter);

		filter = eq("_id", tenantId);
		fieldList.add(filter);

		List<Team> results = mongoDao.aggregate(MongoCollEnum.Teams.toString(), Team.class,
				Aggregates.match(Filters.and(fieldList)), 500);

		if (results == null || results.size() != 1) {
			return null;
		}

		return results.get(0);
	}

	public User getUser(List<User> users, String username) {
		Preconditions.checkNotNull(users, "Users is null");
		Preconditions.checkNotNull(username, "Username value is null");

		for (User user : users) {
			if (username.equals(user.getUsername())) {
				return user;
			}
		}

		return null;
	}

}
