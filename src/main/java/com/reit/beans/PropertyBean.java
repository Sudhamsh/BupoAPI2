package com.reit.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;
import com.opencsv.bean.processor.PreAssignmentProcessor;
import com.reit.util.NumberPreProcessor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyBean {

	private int version = 1;
	private ObjectId id;

	@CsvRecurse
	private Address address;

	private String status = "New";

	private Boolean isDataMissing = null;

	private List<String> missingData = new ArrayList<String>();

	private List<String> prosList = new ArrayList<String>();

	private List<String> consList = new ArrayList<String>();

	// private Date reitCreationDate = new Date();

	private TenantDetails tenantDetails;

	private List<String> labels = new ArrayList<>();

	@CsvBindByName(column = "Property Name")
	private String propertyName;

	@CsvBindByName(column = "External URL")
	private String externalLink;

	@CsvBindByName(column = "Property Status")
	private String propertyStatus;

	@CsvBindByName(column = "Type")
	private String type;

	@CsvBindByName(column = "Tenant(s)")
	private String numberOfTenants;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "Lease Term")
	private float leaseTerm;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "Remaining Term")
	private float remainingTerm;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "SqFt")
	private float sqFt;

	@CsvBindByName(column = "Units")
	private String units;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "NOI")
	private float noi;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "Cap Rate")
	private float cap;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "Asking Price")
	private float askingPrice;

	@PreAssignmentProcessor(processor = NumberPreProcessor.class)
	@CsvBindByName(column = "Price/SqFt")
	private float pricePerSqft;

	private float rentPerSqft;

	@CsvBindByName(column = "Opportunity Zone")
	private Boolean opportunityZone;

	// Key is tenantName and values are notes
	private Map<String, String> propNotes = new HashMap<>();

	// tags
	private Set<String> tags = new HashSet<>();

}
