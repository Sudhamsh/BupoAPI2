package com.bupo.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderQuotes {

	private List<List<CoverageDetailBean>> quotes = new ArrayList<List<CoverageDetailBean>>();
}
