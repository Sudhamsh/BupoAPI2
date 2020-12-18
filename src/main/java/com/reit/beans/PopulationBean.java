package com.reit.beans;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PopulationBean {

	private int year;

	@CsvBindByName(column = "NAME")
	private int zip;

	@CsvBindByName(column = "GEO_ID")
	private String geoId;

	// Estimate!!Total
	@CsvBindByName(column = "B01001_001E")
	private String estimateTotal;

	// Margin of Error!!Total
	@CsvBindByName(column = "B01001_001M")
	private String marginOfError;

//	@CsvBindByName(column = "B01001_002E")
//	private String zip;
//
//	@CsvBindByName(column = "B01001_003E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_003M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_004E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_004M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_005E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_005M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_006E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_006M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_007E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_007M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_008E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_008M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_009E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_009M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_010E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_010M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_011E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_011M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_012E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_012M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_013E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_013M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_014E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_014M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_015E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_015M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_016E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_016M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_017E")
//	private String geoId;
//
//	// B01001_020M,B01001_021E,B01001_021M,B01001_022E,B01001_022M,B01001_023E,B01001_023M,B01001_024E,B01001_024M,B01001_025E,B01001_025M,B01001_026E,B01001_026M,B01001_027E,B01001_027M,B01001_028E,B01001_028M,B01001_029E,B01001_029M,B01001_030E,B01001_030M,B01001_031E,B01001_031M,B01001_032E,B01001_032M,B01001_033E,B01001_033M,B01001_034E,B01001_034M,B01001_035E,B01001_035M,B01001_036E,B01001_036M,B01001_037E,B01001_037M,B01001_038E,B01001_038M,B01001_039E,B01001_039M,B01001_040E,B01001_040M,B01001_041E,B01001_041M,B01001_042E,B01001_042M,B01001_043E,B01001_043M,B01001_044E,B01001_044M,B01001_045E,B01001_045M,B01001_046E,B01001_046M,B01001_047E,B01001_047M,B01001_048E,B01001_048M,B01001_049E,B01001_049M,NAME,B01001_001EA,B01001_001MA,B01001_002EA,B01001_002MA,B01001_003MA,B01001_003EA,B01001_004EA,B01001_004MA,B01001_005EA,B01001_005MA,B01001_006EA,B01001_006MA,B01001_007EA,B01001_007MA,B01001_008EA,B01001_008MA,B01001_009MA,B01001_009EA,B01001_010MA,B01001_010EA,B01001_011EA,B01001_011MA,B01001_012EA,B01001_012MA,B01001_013EA,B01001_013MA,B01001_014MA,B01001_014EA,B01001_015EA,B01001_015MA,B01001_016EA,B01001_016MA,B01001_017EA,B01001_017MA,B01001_018EA,B01001_018MA,B01001_019MA,B01001_019EA,B01001_020MA,B01001_020EA,B01001_021EA,B01001_021MA,B01001_022EA,B01001_022MA,B01001_023EA,B01001_023MA,B01001_024EA,B01001_024MA,B01001_025MA,B01001_025EA,B01001_026EA,B01001_026MA,B01001_027EA,B01001_027MA,B01001_028EA,B01001_028MA,B01001_029MA,B01001_029EA,B01001_030MA,B01001_030EA,B01001_031MA,B01001_031EA,B01001_032MA,B01001_032EA,B01001_033MA,B01001_033EA,B01001_034EA,B01001_034MA,B01001_035EA,B01001_035MA,B01001_036MA,B01001_036EA,B01001_037EA,B01001_037MA,B01001_038EA,B01001_038MA,B01001_039EA,B01001_039MA,B01001_040EA,B01001_040MA,B01001_041MA,B01001_041EA,B01001_042MA,B01001_042EA,B01001_043MA,B01001_043EA,B01001_044EA,B01001_044MA,B01001_045EA,B01001_045MA,B01001_046EA,B01001_046MA,B01001_047MA,B01001_047EA,B01001_048EA,B01001_048MA,B01001_049EA,B01001_049MA,zip
//
//	@CsvBindByName(column = "B01001_017M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_018E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_018M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_019E")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_019M")
//	private String geoId;
//
//	@CsvBindByName(column = "B01001_020E")
//	private String geoId;
//
//	@CsvBindByName(column = "test")
//	private String geoId;
//
//	@CsvBindByName(column = "test")
//	private String geoId;
//
//	@CsvBindByName(column = "test")
//	private String geoId;
//
//	@CsvBindByName(column = "test")
//	private String geoId;

}
