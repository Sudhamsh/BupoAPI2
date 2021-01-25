package com.reit.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesBean {
	private String userEmail;
	private long epochTime = System.currentTimeMillis() / 1000L;
	private String note;

	public NotesBean(String userEmail, String note) {
		this.userEmail = userEmail;
		this.note = note;
	}
}
