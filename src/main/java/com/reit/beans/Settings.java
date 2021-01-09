package com.reit.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {
	private List<String> favProps = new ArrayList<>();
	private Map<String, String> propNotes = new HashMap<>();
}
