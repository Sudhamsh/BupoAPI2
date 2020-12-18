package com.reit.services;

import static com.mongodb.client.model.Filters.eq;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.conversions.Bson;

import com.bupo.dao.MongoDao;
import com.bupo.enums.MongoCollEnum;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.reit.beans.IncomeBean;
import com.reit.beans.PopulationBean;
import com.reit.beans.ZipBean;

public class ZipService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	private MongoDao mongoDao = new MongoDao();

	public void createZipRecord(ZipBean zipBean) {
		Preconditions.checkNotNull(zipBean, "ZipBean is null");
		Preconditions.checkNotNull(zipBean.getZip(), "ZipBean zip value is null");

		// check if the record exists
		// Preconditions.checkArgument(getZipRecordy(zipBean.getZip()) == null, "Zip
		// record exists in data store");

		try {
			// TODO - Check if property exists before create.
			mongoDao.insert(MongoCollEnum.ZipMetrics.toString(), gson.toJson(zipBean));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	// Replaces the whole bean, retrieve and update the bean if you want incremental
	// update
	public void updsertIncomeBean(int zip, IncomeBean incomeBean) {
		Preconditions.checkNotNull(zip, "zip is null");
		Preconditions.checkNotNull(incomeBean, "IncomeBean zip value is null");
		Preconditions.checkNotNull(incomeBean.getYear(), "IncomeBean year value is null");

		ZipBean zipBean = getZipRecordy(zip);
		Preconditions.checkNotNull(zipBean, "Hmm.., couldn't find the record");

		try {
			Map<Integer, IncomeBean> incomes = zipBean.getIncomes();
			incomes.put(incomeBean.getZip(), incomeBean);
			zipBean.setIncomes(incomes);

			mongoDao.findAndReplace(MongoCollEnum.ZipMetrics.toString(), eq("zip", zip), gson.toJson(zipBean));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public void updsertPopulationBean(int zip, PopulationBean populationBean) {
		Preconditions.checkNotNull(zip, "zip is null");
		Preconditions.checkNotNull(populationBean, "PopulationBean zip value is null");

		ZipBean zipBean = getZipRecordy(zip);
		Preconditions.checkNotNull(zipBean, "Hmm.., couldn't find the record");

		try {
			Map<Integer, PopulationBean> populations = zipBean.getPopulations();
			populations.put(populationBean.getYear(), populationBean);
			zipBean.setPopulations(populations);

			mongoDao.findAndReplace(MongoCollEnum.ZipMetrics.toString(), eq("zip", zip), gson.toJson(populationBean));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public ZipBean getZipRecordy(int zip) {

		MongoDao mongoDao = new MongoDao();
		Bson filter = eq("zip", zip);
		ZipBean zipBean = mongoDao.findOne(MongoCollEnum.ZipMetrics.toString(), ZipBean.class, filter);

		return zipBean;
	}

	public static void main(String[] args) {

		try {
			List<IncomeBean> incomeBeans = createBeansFromFile("census/2017allMedianIncome.csv", IncomeBean.class);

			// lets create map for easy search
			Map<Integer, IncomeBean> incomeBeansMap = new HashMap();
			for (IncomeBean incomeBean : incomeBeans) {
				incomeBean.setYear(2017);
				incomeBeansMap.put(incomeBean.getZip(), incomeBean);
			}

			List<PopulationBean> populationBeans = createBeansFromFile("census/2017allPopulationData.csv",
					PopulationBean.class);
			ZipService zipService = new ZipService();

			for (PopulationBean populationBean : populationBeans) {
				int year = 2017;
				populationBean.setYear(year);

				// Populate zip bean object
				ZipBean zipBean = new ZipBean();
				int zip = populationBean.getZip();
				zipBean.setZip(zip);
				zipBean.setPopulations(Collections.singletonMap(year, populationBean));
				zipBean.setIncomes(Collections.singletonMap(year, incomeBeansMap.get(zip)));

				zipService.createZipRecord(zipBean);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static <T> List<T> createBeansFromFile(String filePath, Class<T> clazz) {
		List<T> list = new ArrayList<T>();

		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream(filePath);
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			final CsvToBean<T> beans = new CsvToBeanBuilder<T>(streamReader).withType(clazz).withThrowExceptions(false)
					.build();
			LogManager logger = LogManager.getLogger(clazz);
			list = beans.parse();

			beans.getCapturedExceptions().stream().forEach((exception) -> { // 3
				logger.error("Inconsistent data:" + String.join("", exception.getLine()), exception);// 4
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

}
