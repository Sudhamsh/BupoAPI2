package com.reit.beans;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoiRequestBean {
	private String offerPrice;
	private ObjectId propId;
	private String buyerEmail;
}
