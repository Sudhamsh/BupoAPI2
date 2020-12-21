package com.reit.util;

import org.apache.commons.math3.stat.StatUtils;

public class DataUtils {

	public static double[] normalizeData(double[] data) {
		double normArr[] = StatUtils.normalize(data);
		return normArr;
	}

}
