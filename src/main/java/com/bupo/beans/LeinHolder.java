package com.bupo.beans;

import com.reit.beans.Address;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeinHolder {
	private String name;
	private String phone;
	private String email;
	private Address address;

}
