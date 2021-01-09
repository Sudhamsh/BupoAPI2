package com.reit.services;

import java.util.Date;
import java.util.Map;

import com.bupo.beans.User;
import com.bupo.services.UserService;
import com.bupo.util.LogManager;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reit.beans.Settings;

public class SettingService {
	private LogManager logger = LogManager.getLogger(this.getClass());
	private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

	public void addFavorite(String userEmail, String hexObjId) throws Exception {
		Preconditions.checkNotNull(userEmail, "User Email is null");
		Preconditions.checkNotNull(hexObjId, "HexObjId is null");

		// get user object
		UserService userService = new UserService();
		User user = userService.getUserByEmail(userEmail);

		Preconditions.checkNotNull(user, "Coundn't find user");

		// add setting
		Settings settings = user.getSettings();
		if (settings == null || settings.getFavProps() == null) {
			logger.error("Settings object came as null. Not expected. Receovering by receating the object");
			user.setSettings(new Settings());
		}
		user.getSettings().getFavProps().add(hexObjId);

		// save setting
		userService.updateUser(user);

	}

	public void addNotes(String userEmail, String hexObjId, String notes) throws Exception {
		Preconditions.checkNotNull(userEmail, "User Email is null");
		Preconditions.checkNotNull(hexObjId, "HexObjId is null");
		Preconditions.checkNotNull(notes, "Notes is null");

		// get user object
		UserService userService = new UserService();
		User user = userService.getUserByEmail(userEmail);

		Preconditions.checkNotNull(user, "Coundn't find user");

		// add setting
		Settings settings = user.getSettings();
		if (settings == null || settings.getFavProps() == null) {
			logger.error("Settings object came as null. Not expected. Receovering by receating the object");
			user.setSettings(new Settings());
		}
		Map<String, String> propNotes = user.getSettings().getPropNotes();

		propNotes.put(hexObjId, appendNotes(propNotes.get(hexObjId), notes));

		// save setting
		userService.updateUser(user);

	}

	private String appendNotes(String oldNotes, String newNotes) {
		String notes;
		notes = new Date() + "\n" + oldNotes + "\n" + newNotes;
		return notes;
	}

	public void saveSetting(String userEmail, String settingType, String settingName, String value) {
		Preconditions.checkNotNull(userEmail, "User Email is null");
		Preconditions.checkNotNull(settingType, "Setting Type Email is null");
		Preconditions.checkNotNull(settingName, "Setting Name Email is null");
		Preconditions.checkNotNull(value, "Value is null");
		// get user object
		// save setting

	}
}
