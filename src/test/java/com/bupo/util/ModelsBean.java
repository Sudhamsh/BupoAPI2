package com.bupo.util;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModelsBean {

	private int year;
	private Map<String, Integer> allMakeNameAndId;

}
