package com.reit.beans;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credentials implements Serializable {

	private String username;
	private String password;

}
