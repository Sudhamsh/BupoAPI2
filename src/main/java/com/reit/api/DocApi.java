package com.reit.api;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bupo.util.LogManager;
import com.google.gson.Gson;
import com.reit.beans.ErrorBean;
import com.reit.beans.KeyValueBean;
import com.reit.beans.LoiRequestBean;
import com.reit.services.DigiSignService;
import com.reit.services.GenerateDocService;
import com.reit.util.GsonUtils;
import com.reit.util.Secured;

@Path("/reit/doc")
public class DocApi {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private DigiSignService digiSignService = new DigiSignService();
	private GenerateDocService docService = new GenerateDocService();
	private Gson gson = GsonUtils.getGson();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Secured
	public Response createDoc(LoiRequestBean loiRequestBean) {
		Response response = null;
		KeyValueBean keyValue = new KeyValueBean();

		try {
			if ("dropbox".equals(loiRequestBean.getType())) {
				String storedPath = docService.generateDoc("dev", "LOI", loiRequestBean);
				keyValue = new KeyValueBean("dropboxPath", storedPath);

			} else if ("digiSign".equals(loiRequestBean.getType())) {
				digiSignService.sendDocForSign("LOI", loiRequestBean);
			} else {
				throw new Exception("Invalid Type");
			}

			response = Response.status(201).entity(gson.toJson(keyValue)).build();
		} catch (EntityNotFoundException e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		} catch (Exception e) {
			logger.error(e);
			response = Response.serverError().entity(new ErrorBean(500, "Unexpected Error")).build();
		}

		return response;

	}
}
