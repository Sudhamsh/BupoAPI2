package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reit.beans.Address;
import com.reit.beans.PropertyBean;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;

public class PropertyService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public void createNewProperty(PropertyBean propertyBean) {
		Preconditions.checkNotNull(propertyBean, "PropertyBean is null");
		Preconditions.checkNotNull(propertyBean.getAddress(), "PropertyBean address is null");
		Preconditions.checkNotNull(propertyBean.getAddress().getFullAddress(), "PropertyBean full address is null");

		Preconditions.checkArgument(getPoperty(propertyBean.getAddress().getFullAddress()) == null,
				"Property exists in data store");

		MongoDao mongoDao = new MongoDao();
		try {
			// TODO - Check if property exists before create.
			mongoDao.insert(MongoCollEnum.Property.toString(), gson.toJson(propertyBean));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public Document getPoperty(String fullAddress) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("address.fullAddress", fullAddress);

		Document policyRequest = mongoDao.findOne(MongoCollEnum.Property.toString(), filter);

		return policyRequest;

	}

	public List<PropertyResultsBean> findMatchingProperties(List<SearchFilter> filters) {
		List<PropertyResultsBean> resultsBeans = new ArrayList<PropertyResultsBean>();
		Preconditions.checkNotNull(filters, "Search Filters is null");
		Preconditions.checkArgument(filters.size() != 0, "Search Filters is empty");

		Bson filter = null;
		List<Bson> fieldList = new ArrayList();
		MongoDao mongoDao = new MongoDao();

		// Create mongo filter
		for (SearchFilter searchFilter : filters) {
			switch (searchFilter.getFieldName()) {
			case "cap":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "ASKING_PRICE":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "SCORE":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "REMAINING_TERM":
				fieldList.add(getBsonFilter(searchFilter));
				break;

			default:
				break;
			}
		}

		Preconditions.checkNotNull(fieldList.size(), "Hmm. Filters is empty");

		List<PropertyBean> results = mongoDao.aggregate(MongoCollEnum.Property.toString(), PropertyBean.class,
				Aggregates.match(Filters.and(fieldList)), 500);

		System.out.println("results size: " + results.toString());

		resultsBeans = convertDocs(results);

		return resultsBeans;

	}

	public List<PropertyResultsBean> convertDocs(List<PropertyBean> results) {
		List<PropertyResultsBean> resultsBeans = new ArrayList<PropertyResultsBean>();

		for (PropertyBean propertyBean : results) {
			PropertyResultsBean bean = new PropertyResultsBean();
			// bean.setAskingPrice(propertyBean.getAskingPrice());
			System.out.println("propertyBean : " + propertyBean.getAskingPrice());

			resultsBeans.add(bean);

		}

		return resultsBeans;
	}

	public Bson getBsonFilter(SearchFilter searchFilter) {
		Preconditions.checkNotNull(searchFilter, "Search Filter is null");
		Preconditions.checkNotNull(searchFilter.getFieldName(), "Search Filter filed name is null");

		Bson filter = null;
		switch (searchFilter.getOperator()) {
		case EQUALS:
			filter = Filters.eq(searchFilter.getFieldName(), searchFilter.getFieldValue());
			break;
		case GREATER_THAN:
			filter = Filters.gte(searchFilter.getFieldName(), searchFilter.getFieldValue());
			break;
		case LESS_THAN:
			filter = Filters.lte(searchFilter.getFieldName(), searchFilter.getFieldValue());
			break;

		default:
			break;
		}

		return filter;

	}

	public static void main(String[] args) {
		Path path;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream("csv/UrgentCareExportResults.csv");
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			final CsvToBean<PropertyBean> beans = new CsvToBeanBuilder<PropertyBean>(streamReader)
					.withType(PropertyBean.class).withThrowExceptions(false).build();

			final List<PropertyBean> properties = beans.parse();
			int i = 0;

			PropertyService propService = new PropertyService();
			LogManager logger = LogManager.getLogger(propService.getClass());

			// insert in DB
			for (PropertyBean propertyBean : properties) {
				try {
					propertyBean.setLabels(Arrays.asList("urgent care", "medical"));
					enhanceData(propertyBean);

					propService.createNewProperty(propertyBean);
				} catch (Exception e) {
					// TODO: Ignore exceptions for now. Need to return consolidated list later.
					e.printStackTrace();
				}

			}

			beans.getCapturedExceptions().stream().forEach((exception) -> { // 3
				logger.error("Inconsistent data:" + String.join("", exception.getLine()), exception);// 4
			});

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void enhanceData(PropertyBean propertyBean) {
		populateFullAddress(propertyBean);

		// Calculate derivable values
		if (propertyBean.getSqFt() != 0) {
			propertyBean.setPricePerSqft(propertyBean.getAskingPrice() / propertyBean.getSqFt());
			propertyBean.setRentPerSqft(propertyBean.getNoi() / propertyBean.getSqFt());
		}

		// missing data
		validateField(propertyBean, propertyBean.getSqFt(), "Square Feet");
		validateField(propertyBean, propertyBean.getCap(), "CAP");
		validateField(propertyBean, propertyBean.getNoi(), "NOI");
		validateField(propertyBean, propertyBean.getAskingPrice(), "Asking price");

	}

	static void validateField(PropertyBean propertyBean, float value, String fieldName) {
		Preconditions.checkNotNull(propertyBean, "PropertyBean is null");
		Preconditions.checkNotNull(propertyBean.getMissingData(), "PropertyBean missing data should be initialized");
		Preconditions.checkNotNull(value, "Value to be checked is null");
		Preconditions.checkNotNull(fieldName, "fieldName is null");

		if (value == 0) {
			propertyBean.setIsDataMissing(new Boolean(true));
			propertyBean.getMissingData().add(fieldName + " is missing");
		}
	}

	static void populateFullAddress(PropertyBean propertyBean) {
		if (propertyBean != null && propertyBean.getAddress() != null) {
			Address address = propertyBean.getAddress();
			String fullLine = address.getFirstLine() + "," + address.getCity() + "," + address.getState() + ","
					+ address.getZip();

			address.setFullAddress(fullLine);
			propertyBean.setAddress(address);
		}
	}

}
