package com.reit.beans;

import com.reit.enums.PropStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusBean {
	private String userEmail;
	private long epochTime = System.currentTimeMillis() / 1000L;
	private PropStatus status;

	public StatusBean(String userEmail, PropStatus status) {
		this.userEmail = userEmail;
		this.status = status;
	}
}
