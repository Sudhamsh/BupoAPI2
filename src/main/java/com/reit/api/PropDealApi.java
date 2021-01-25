package com.reit.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.types.ObjectId;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.reit.beans.DealUserRole;
import com.reit.beans.ErrorBean;
import com.reit.beans.PropDealBean;
import com.reit.beans.PropDealReq;
import com.reit.services.PropDealService;
import com.reit.services.TokenService;
import com.reit.util.GsonUtils;

@Path("/deal")
public class PropDealApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = GsonUtils.getGson();
	PropDealService propDealService = new PropDealService();

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response createPropDeal(PropDealReq propDealReq) {
		Response response = null;

		try {
			PropDealBean propDeal = new PropDealBean();
			ObjectId tId = new TokenService().getTenantObjId();
			System.out.println(tId);
			propDealReq.setTenantObjId(tId);
			BeanUtils.copyProperties(propDeal, propDealReq);
			propDealService.createDeal(propDeal);
			response = Response.status(201).build();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}

	@GET
	@Path("/property/{propertyId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTenantUsers(@PathParam("propertyId") ObjectId propertyId) {
		Response response = null;
		try {
			List<DealUserRole> userRoleList = propDealService.getDealUsers(propertyId);

			response = Response.status(200).entity(gson.toJson(userRoleList)).build();

		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;
	}

}
