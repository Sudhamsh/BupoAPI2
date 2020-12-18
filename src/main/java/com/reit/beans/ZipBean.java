package com.reit.beans;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZipBean {
	private int zip;
	private Map<Integer, IncomeBean> incomes;
	private Map<Integer, PopulationBean> populations;

}
