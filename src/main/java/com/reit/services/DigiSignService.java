package com.reit.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.bson.types.ObjectId;

import com.bupo.beans.User;
import com.bupo.services.UserService;
import com.google.common.base.Preconditions;
import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.SignatureRequest;
import com.hellosign.sdk.resource.Template;
import com.hellosign.sdk.resource.TemplateSignatureRequest;
import com.hellosign.sdk.resource.support.CustomField;
import com.hellosign.sdk.resource.support.TemplateList;
import com.reit.beans.LoiRequestBean;
import com.reit.beans.PropertyBean;

public class DigiSignService {

	public void sendDocForSign(String templateName, LoiRequestBean loiRequestBean) throws Exception {
		Preconditions.checkNotNull(templateName, "Template Name is null");
		Preconditions.checkNotNull(loiRequestBean, "LOI bean is null");
		Preconditions.checkNotNull(loiRequestBean.getPropId(), "Property Id is null");
		Preconditions.checkNotNull(loiRequestBean.getBuyerEmail(), "Buyer Email Id is null");

		HelloSignClient client = new HelloSignClient(
				"42991be9557f5f8264f195d07edb9e762de44bccb203caff0a66ba2f9e4c8bde");

		TemplateList templateList;
		// Get buyer details
		User buyer = new UserService().getUserByEmail(loiRequestBean.getBuyerEmail());

		// populate property specific data
		Map<String, String> variablesMap = new HashMap<>();
		variablesMap.put("loi_purchase_price", loiRequestBean.getOfferPrice());
		variablesMap.put("loi_buyer_name", buyer.getGivenName());
		populatePropertyData(loiRequestBean.getPropId(), variablesMap);

		try {
			templateList = client.getTemplates();
			List<Template> filteredList = templateList.filterCurrentPageBy(Template.TEMPLATE_TITLE, templateName);

			if (buyer == null) {
				throw new Exception("Buyer email not found in the system!");
			}

			for (Template template : filteredList) {
				TemplateSignatureRequest request = new TemplateSignatureRequest();
				request.setTemplateId(template.getId());
				request.setSigner("Buyer", buyer.getEmail(), buyer.getGivenName());

				CustomField customField = null;
				for (String fieldName : variablesMap.keySet()) {
					customField = new CustomField();
					customField.setName(fieldName);
					customField.setValue(variablesMap.get(fieldName));
					request.addCustomField(customField);
				}

				request.setTestMode(true);

				SignatureRequest response = client.sendTemplateSignatureRequest(request);
				System.out.println(response.toString());
				if (response.hasError()) {
					throw new Exception(response.getMessage());
				}
			}

		} catch (HelloSignException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	public void populatePropertyData(ObjectId propId, Map<String, String> variablesMap) {
		Preconditions.checkNotNull(propId, "Property ID is null");
		Preconditions.checkNotNull(variablesMap, "Fields map is null");

		PropertyService propertyService = new PropertyService();
		PropertyBean propertyBean = propertyService.getPopertyById(propId);

		if (propertyBean == null) {
			throw new NotFoundException("Property Not found");
		}

		variablesMap.put("loi_prop_name", propertyBean.getPropertyName());
		variablesMap.put("loi_prop_address", propertyBean.getAddress().getFullAddress());
		variablesMap.put("loi_noi", Float.toString(propertyBean.getNoi()));

	}

}
