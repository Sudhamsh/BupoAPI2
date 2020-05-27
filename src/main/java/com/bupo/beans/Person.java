package com.bupo.beans;

import java.util.Date;

import com.bupo.enums.MaritalStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Person {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Date dateOfBirth;
	private int age;
	private MaritalStatus maritalStatus;

}
