package com.reit.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bupo.exceptions.ObjectExists;
import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.services.SettingService;

@Path("/reit/settings")
public class SettingsApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private SettingService settingService = new SettingService();
	private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setDateFormat("yyyy-MM-dd").create();

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/propertyId/{propertyId}")
	public Response createUser(@PathParam("propertyId") String propertyId) {
		Response response = null;

		try {
			settingService.addFavorite("sudhamsh.b@gmail.com", propertyId);

			response = Response.status(200).build();
		} catch (ObjectExists e) {
			response = Response.status(Status.CONFLICT).entity(Status.NO_CONTENT).build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}

		return response;

	}

}
