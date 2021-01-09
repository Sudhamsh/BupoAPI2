package com.reit.services;

import static java.lang.System.currentTimeMillis;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

import com.reit.beans.saas.tenant.SaasTenantBean;
import com.reit.beans.saas.tenant.Team;
import com.reit.beans.saas.tenant.UserContact;

public class SaasTenantServiceTest {

	@Test
	public void createTenant_hp() {
		try {
			SaasTenantBean saasTenant = populateSaasTenantBean();
			SaasTenantService tenantService = new SaasTenantService(saasTenant);
			tenantService.createTenant();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public SaasTenantBean populateSaasTenantBean() {
		SaasTenantBean saasTenant = new SaasTenantBean();
		saasTenant.setTenantName("testTenant_" + System.currentTimeMillis());

		return saasTenant;

	}

	@Test
	public void createDepartment() {

		try {
			// create new tenant
			SaasTenantBean saasTenant = populateSaasTenantBean();
			String tenantName = saasTenant.getTenantName();
			SaasTenantService tenantService = new SaasTenantService(saasTenant);
			tenantService.createTenant();

			// find and validate newly created object
			SaasTenantBean saasTenantDB = tenantService.getSaasTenant(tenantName);
			Assert.assertNotNull("Tenant search failed", saasTenantDB);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void createTeam() {

		try {
			// create new tenant
			SaasTenantBean saasTenant = populateSaasTenantBean();
			SaasTenantService tenantService = new SaasTenantService(saasTenant);
			ObjectId tenantId = tenantService.createTenant();

			Team team = new Team("dev_team_" + currentTimeMillis(), "a@a.com", new ArrayList<>(), tenantId);
			UserContact user = new UserContact("dev_username_" + currentTimeMillis(), "fName", "lName", "a@a.com", null,
					null);
			team.getMembers().add(user);
			ObjectId id = tenantService.createTeam(team);
			Assert.assertNotNull("Object Id came as null", id);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Test
	public void createTenant_cc() {
		try {
			SaasTenantService tenantService = new SaasTenantService(null);
			tenantService.createTenant();
			Assert.fail("Create tenant accepted null object");
		} catch (Exception e) {

		}
	}

}
