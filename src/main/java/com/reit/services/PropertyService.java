package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
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
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reit.beans.Address;
import com.reit.beans.IncomeBean;
import com.reit.beans.PopulationBean;
import com.reit.beans.PropertyBean;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;
import com.reit.beans.ZipBean;
import com.reit.util.DataUtils;

public class PropertyService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public ObjectId createNewProperty(PropertyBean propertyBean) {
		Preconditions.checkNotNull(propertyBean, "PropertyBean is null");
		Preconditions.checkNotNull(propertyBean.getAddress(), "PropertyBean address is null");
		Preconditions.checkNotNull(propertyBean.getAddress().getFullAddress(), "PropertyBean full address is null");

		Preconditions.checkArgument(getPoperty(propertyBean.getAddress().getFullAddress()) == null,
				"Property exists in data store");

		MongoDao mongoDao = new MongoDao();
		ObjectId objectId = new ObjectId();
		try {
			// TODO - Check if property exists before create.
			objectId = mongoDao.insert(MongoCollEnum.Property.toString(), gson.toJson(propertyBean));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

		return objectId;

	}

	public Document getPoperty(String fullAddress) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("address.fullAddress", fullAddress);

		Document policyRequest = mongoDao.findOne(MongoCollEnum.Property.toString(), filter);

		return policyRequest;

	}

	public PropertyBean getPopertyById(ObjectId objectId) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("_id", objectId);

		List<PropertyBean> results = mongoDao.aggregate(MongoCollEnum.Property.toString(), PropertyBean.class,
				Aggregates.match(filter), 1);
		PropertyBean propertyBean = null;
		if (results != null && results.size() == 1) {
			propertyBean = results.get(0);
		}

		return propertyBean;

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
			case "askingPrice":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "score":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "remainingTerm":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			case "status":
				fieldList.add(getBsonFilter(searchFilter));
				break;
			default:
				break;
			}
		}

		Preconditions.checkNotNull(fieldList.size(), "Hmm. Filters is empty");

		List<PropertyBean> results = mongoDao.aggregate(MongoCollEnum.Property.toString(), PropertyBean.class,
				Aggregates.match(Filters.and(fieldList)), 5);

		System.out.println("results size: " + results.toString());

		// Convert docs to results bean to hide unrequired properties
		resultsBeans = generateResultsBean(results);

		normalizeData(resultsBeans);

		return resultsBeans;

	}

	public void populateZipData(PropertyResultsBean propertyResultsBean, String zip) {
		ZipService zipService = new ZipService();
		String year = "2017";

		try {
			ZipBean zipBean = zipService.findZipRecord(Integer.parseInt(zip));
			if (zipBean != null) {
				// Get income
				IncomeBean income = zipBean.getIncomes().get(year);
				if (income != null) {
					propertyResultsBean.setIncome(income.getMedianIncome());
				}

				// Get population
				PopulationBean population = zipBean.getPopulations().get(year);
				if (population != null) {
					propertyResultsBean.setPopulation(Float.parseFloat(population.getEstimateTotal()));
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public List<PropertyResultsBean> generateResultsBean(List<PropertyBean> results) {
		List<PropertyResultsBean> resultsBeans = new ArrayList<PropertyResultsBean>();

		for (PropertyBean propertyBean : results) {

			try {
				PropertyResultsBean propResBean = new PropertyResultsBean();
				System.out.println("id:" + propertyBean.getId());
				BeanUtils.copyProperties(propResBean, propertyBean);
				System.out.println(" propResBean id:" + propResBean.getId());

				// populate zip data
				populateZipData(propResBean, propertyBean.getAddress().getZip());

				resultsBeans.add(propResBean);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			}

		}

		return resultsBeans;
	}

	public void normalizeData(List<PropertyResultsBean> resultsBeans) {
		double[] remainingTerm = new double[resultsBeans.size()];
		double[] cap = new double[resultsBeans.size()];
		double[] pricePerSqft = new double[resultsBeans.size()];
		double[] rentPerSqft = new double[resultsBeans.size()];
		double[] income = new double[resultsBeans.size()];
		double[] population = new double[resultsBeans.size()];

		double[] norRemainingTerm = new double[resultsBeans.size()];
		double[] norCap = new double[resultsBeans.size()];
		double[] norPricePerSqft = new double[resultsBeans.size()];
		double[] norRentPerSqft = new double[resultsBeans.size()];
		double[] norIncome = new double[resultsBeans.size()];
		double[] norPop = new double[resultsBeans.size()];

		// normalization inputs is a double array :)
		for (int i = 0; i < resultsBeans.size(); i++) {
			remainingTerm[i] = resultsBeans.get(i).getRemainingTerm();
			cap[i] = resultsBeans.get(i).getCap();
			pricePerSqft[i] = resultsBeans.get(i).getPricePerSqft();
			rentPerSqft[i] = resultsBeans.get(i).getRentPerSqft();
			income[i] = resultsBeans.get(i).getIncome();
			population[i] = resultsBeans.get(i).getPopulation();
		}

		// get normalized data
		norRemainingTerm = DataUtils.normalizeData(remainingTerm);
		norCap = DataUtils.normalizeData(cap);
		norPricePerSqft = DataUtils.normalizeData(pricePerSqft);
		norRentPerSqft = DataUtils.normalizeData(rentPerSqft);

		norIncome = DataUtils.normalizeData(income);
		norPop = DataUtils.normalizeData(population);

		// compute weight
		for (int i = 0; i < resultsBeans.size(); i++) {
			double score = norRemainingTerm[i] + norCap[i] + norIncome[i] + norPop[i] - norPricePerSqft[i]
					- norRentPerSqft[i];
			resultsBeans.get(i).setWeightedScore(Double.isNaN(score) ? -9.99d : score);
		}

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

		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream("csv/FamilyDollar1-6.csv");
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
			propertyBean.setIsDataMissing(Boolean.TRUE);
			propertyBean.getMissingData().add(fieldName + " is missing");
			propertyBean.setStatus("Missing Data");
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

	public void addNotes(String userEmail, ObjectId propObjectId, String notes) throws Exception {
		Preconditions.checkNotNull(userEmail, "User Email is null");
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(notes, "Notes is null");

		// find property
		PropertyBean propertyBean = getPopertyById(propObjectId);
		Preconditions.checkNotNull(propertyBean, "Couldn't find property by Id");

		Map<String, String> propNotes = propertyBean.getPropNotes();
		// add setting
		if (propNotes == null) {
			logger.error("Notes object came as null. Not expected. Receovering by receating the object");
			propNotes = new HashMap<>();
		}
		String tenantName = new TokenService().getTenantName();
		propNotes.put(tenantName, appendNotes(userEmail, propNotes.get(tenantName), notes));
		propertyBean.setPropNotes(propNotes);

		// save setting
		updateProp(propertyBean);
	}

	public String getNotes(ObjectId propObjectId, String tenantName) {
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(tenantName, "Tenant Name is null");

		String notes = "";
		PropertyBean bean = getPopertyById(propObjectId);
		if (bean == null) {
			throw new EntityExistsException("Couldn't find the property");
		}
		if (bean.getPropNotes() != null) {
			notes = bean.getPropNotes().get(tenantName);
		}

		return notes;

	}

	public void updateProp(PropertyBean propertyBean) {
		Preconditions.checkNotNull(propertyBean, "PropertyBean is null");
		Preconditions.checkNotNull(propertyBean.getId(), "Prop Id is null");
		MongoDao mongoDao = new MongoDao();

		try {
			mongoDao.findAndReplace(MongoCollEnum.Property.toString(), eq("_id", propertyBean.getId()),
					gson.toJson(propertyBean));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	String appendNotes(String userEmail, String oldNotes, String newNotes) {
		StringBuilder notes = new StringBuilder();

		notes.append("Added by " + userEmail + " On " + new Date() + "\n" + newNotes);

		if (oldNotes != null) {
			notes.append("\n" + oldNotes + "\n");
		}

		return notes.toString();
	}

	public void addTag(ObjectId propId, String tag) throws Exception {
		modifyTags(propId, new HashSet<>(Arrays.asList(tag)), "add");
	}

	public void removeTag(ObjectId propId, String tag) throws Exception {
		modifyTags(propId, new HashSet<>(Arrays.asList(tag)), "remove");
	}

	public void modifyTags(ObjectId propId, Set<String> tags, String operation) throws Exception {
		Preconditions.checkNotNull(tags, "Tags list is null");
		Preconditions.checkNotNull(propId, "Property id  is null");

		PropertyBean property = getPopertyById(propId);
		Preconditions.checkNotNull(property, "Couldn't find property by id.");

		// TODO: Could use some simplification...
		if ("add".equals(operation)) {
			if (property.getTags() == null) {
				property.setTags(tags);
			} else {
				property.getTags().addAll(tags);
			}
		} else if ("remove".equals(operation)) {
			if (property.getTags() != null) {
				property.getTags().removeAll(tags);
			}
		} else {
			throw new Exception("Invalid operation. User add/remove.");
		}

		updateProp(property);
	}

	public Set<String> getTags(ObjectId propId) {
		Preconditions.checkNotNull(propId, "Property id  is null");
		Set<String> tags = null;

		PropertyBean property = getPopertyById(propId);
		Preconditions.checkNotNull(property, "Couldn't find property by id.");

		tags = property.getTags() == null ? new HashSet<>() : property.getTags();

		return tags;
	}

}
