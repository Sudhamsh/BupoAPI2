package com.reit.beans;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PropDealAbs {
	private ObjectId propObjId;
	private ObjectId tenantObjId;
	private List<DealUserRole> userRoleList = new ArrayList<>();
}
