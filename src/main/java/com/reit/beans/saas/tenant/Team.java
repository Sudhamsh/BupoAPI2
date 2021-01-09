package com.reit.beans.saas.tenant;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
	private String name;
	private String teamEmail;
	private List<UserContact> members = new ArrayList<>();
	private ObjectId tenantId;
}
