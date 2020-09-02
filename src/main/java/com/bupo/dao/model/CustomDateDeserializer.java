package com.bupo.dao.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected CustomDateDeserializer(Class<String> vc) {
		super(vc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return p.getText();
	}

}
