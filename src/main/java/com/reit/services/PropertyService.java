package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.ws.rs.NotFoundException;

import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reit.beans.Address;
import com.reit.beans.IncomeBean;
import com.reit.beans.NotesBean;
import com.reit.beans.PopulationBean;
import com.reit.beans.PropertyBean;
import com.reit.beans.PropertyResultsBean;
import com.reit.beans.SearchFilter;
import com.reit.beans.StatusBean;
import com.reit.beans.ZipBean;
import com.reit.util.DataUtils;
import com.reit.util.GsonUtils;
import com.reit.util.MultipleLinearRegression;

public class PropertyService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = GsonUtils.getGson();

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
		List<PropertyResultsBean> propRespList = new ArrayList<PropertyResultsBean>();
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

		// Convert results to results bean to hide unrequired properties
		propRespList = generateResultsBean(results);

		// Enrich data
		enrichData(propRespList);

		// normalize data
		normalizeData(propRespList);

		// predict cap
		computeMLCapPredictions(propRespList);

		return propRespList;

	}

	public void computeMLCapPredictions(List<PropertyResultsBean> propRespList) {
		// Create a map to calculate CAP prediction
		Map<ObjectId, PropertyResultsBean> propResMap = new HashMap<>();
		for (PropertyResultsBean propResBean : propRespList) {
			propResMap.put(propResBean.getId(), propResBean);
		}

		// predict cap
		for (PropertyResultsBean propResultBean : propRespList) {
			Map<ObjectId, PropertyResultsBean> propMapCopy = new HashMap<>(propResMap);
			PropertyResultsBean predictFor = propMapCopy.remove(propResultBean.getId());
			propResultBean.setPredictedCap(predictCap(propMapCopy.values(), predictFor));
		}

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

	public void enrichData(List<PropertyResultsBean> resultsResBeans) {

		for (PropertyResultsBean propResBean : resultsResBeans) {
			populateZipData(propResBean, propResBean.getAddress().getZip());
		}
	}

	public List<PropertyResultsBean> generateResultsBean(List<PropertyBean> results) {
		List<PropertyResultsBean> resultsResBeans = new ArrayList<PropertyResultsBean>();

		for (PropertyBean propertyBean : results) {

			try {
				PropertyResultsBean propResBean = new PropertyResultsBean();

				BeanUtils.copyProperties(propResBean, propertyBean);

				// populate zip data
				populateZipData(propResBean, propertyBean.getAddress().getZip());

				resultsResBeans.add(propResBean);

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

		return resultsResBeans;
	}

	public double predictCap(Collection<PropertyResultsBean> featureColl, PropertyResultsBean propertyBean) {
		double predictedCap = 0;

		double[][] featureData = new double[featureColl.size()][5];
		double[] featureCaps = new double[featureColl.size()];

		// Feature order
		// norPricePerSqft
		// norRentPerSqft
		// norRemainingTerm
		// norPopulation
		// norIncome
		int i = 0;

		for (PropertyResultsBean tempProp : featureColl) {
			System.out.println(gson.toJson(tempProp));
			featureData[i][0] = tempProp.getPricePerSqft();
			featureData[i][1] = tempProp.getRentPerSqft();
			featureData[i][2] = tempProp.getRemainingTerm();
			featureData[i][3] = tempProp.getPopulation();
			featureData[i][4] = tempProp.getIncome();

			featureCaps[i] = tempProp.getCap();

			i++;

		}

		System.out.println(gson.toJson(featureData));
		System.out.println(gson.toJson(featureCaps));

		MultipleLinearRegression regression = new MultipleLinearRegression(featureData, featureCaps);

		predictedCap = regression.beta(0) * propertyBean.getPricePerSqft()
				+ regression.beta(1) * propertyBean.getRentPerSqft()
				+ regression.beta(2) * propertyBean.getRemainingTerm()
				+ regression.beta(3) * propertyBean.getPopulation() + regression.beta(4) * propertyBean.getIncome();

		System.out.println("predictedCap:" + predictedCap);

		return predictedCap;
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

		// normalization inputs as a double array :)
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

			// save normalized data for future use
			resultsBeans.get(i).setNorRemainingTerm(norRemainingTerm[i]);
			resultsBeans.get(i).setNorCap(norCap[i]);
			resultsBeans.get(i).setNorPricePerSqft(norPricePerSqft[i]);
			resultsBeans.get(i).setNorRentPerSqft(norRentPerSqft[i]);
			resultsBeans.get(i).setNorIncome(norIncome[i]);
			resultsBeans.get(i).setNorPopulation(norPop[i]);

			// calculate score
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

	public void addNotes(ObjectId propObjectId, NotesBean note) throws Exception {
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(note, "Notes is null");

		// find property
		PropertyBean propertyBean = getPopertyById(propObjectId);
		Preconditions.checkNotNull(propertyBean, "Couldn't find property by Id");

		List<NotesBean> propNotes = propertyBean.getPropNotes();

		// init
		if (propNotes == null) {
			logger.error("Notes object came as null. Not expected. Receovering by receating the object");
			propNotes = new ArrayList<>();
		}

		// Add notes
		String tenantName = new TokenService().getTenantName();

		propNotes.add(note);
		propertyBean.setPropNotes(propNotes);

		// save setting
		updateProp(propertyBean);
	}

	public void updateStatus(ObjectId propObjectId, StatusBean status) throws Exception {
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(status, "Status is null");

		// find property
		PropertyBean propertyBean = getPopertyById(propObjectId);
		Preconditions.checkNotNull(propertyBean, "Couldn't find property by Id");

		propertyBean.setStatusBean(status);

		// save setting
		updateProp(propertyBean);
	}

	public void updateCaps(ObjectId propObjectId, List<Long> capsList) throws Exception {
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(capsList, "Cap List is null");

		// find property
		PropertyBean propertyBean = getPopertyById(propObjectId);
		Preconditions.checkNotNull(propertyBean, "Couldn't find property by Id");

		propertyBean.setCapsList(capsList);

		// save setting
		updateProp(propertyBean);
	}

	public List<NotesBean> getNotes(ObjectId propObjectId, String tenantName) {
		Preconditions.checkNotNull(propObjectId, "ObjId is null");
		Preconditions.checkNotNull(tenantName, "Tenant Name is null");

		List<NotesBean> notes = new ArrayList<>();
		PropertyBean bean = getPopertyById(propObjectId);

		if (bean == null) {
			throw new EntityExistsException("Couldn't find the property");
		}
		if (bean.getPropNotes() != null) {
			notes = bean.getPropNotes();
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

	public void populatePropertyData(ObjectId propId, Map<String, String> variablesMap) {
		Preconditions.checkNotNull(propId, "Property ID is null");
		Preconditions.checkNotNull(variablesMap, "Variable Map is null");

		PropertyBean propertyBean = getPopertyById(propId);

		if (propertyBean == null) {
			throw new NotFoundException("Property Not found");
		}

		variablesMap.put("loi_prop_name", propertyBean.getPropertyName());
		variablesMap.put("loi_prop_address", propertyBean.getAddress().getFullAddress());
		variablesMap.put("loi_noi", Float.toString(propertyBean.getNoi()));
		variablesMap.put("loi_broker_name", "Reit LLC");
		variablesMap.put("buyer_name", "Sudhamsh Bachu");

	}

}
