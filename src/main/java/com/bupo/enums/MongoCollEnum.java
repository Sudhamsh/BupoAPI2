package com.bupo.enums;

public enum MongoCollEnum {
	AutoHome("autoHome"), User("user"), Quote("quote"), QuoteDetail("quoteDetail"), Property("property"),
	ZipMetrics("zipMetrics"), CompanyMetrics("companyMetrics"), SaasTenant("saasTenant"), Teams("teams"),
	TOKEN("token"), PropDeal("propDeal"), CountyData("countyData");

	private final String name;

	private MongoCollEnum(String s) {
		name = s;
	}

	public boolean equalsName(String otherName) {
		// (otherName == null) check is not needed because name.equals(null) returns
		// false
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
}
