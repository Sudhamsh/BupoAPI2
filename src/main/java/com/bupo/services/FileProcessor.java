package com.bupo.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.bupo.dao.BaseDao;
import com.bupo.dao.model.UserAuto;
import com.bupo.util.SessionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.reit.util.JsonUtil;

public class FileProcessor {

	/**
	 * Orchestrates file processing steps
	 * 
	 * @param fileInputStream
	 * @param fileMetaData
	 */
	public void processUpload(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
		// save file in FS
		String filePath = saveFile(fileInputStream, fileMetaData);

		// Save details to DB
		BaseDao baseDao = new BaseDao();

		String loggedInUser = new SessionUtil().getLoggedInUser();
		UserAuto userAuto = baseDao.findById(UserAuto.class, loggedInUser);

		// Adds or appends URLs in DB
		if (userAuto == null) {
			// TODO convert text to json before db insert
			userAuto = new UserAuto();
			userAuto.setEmail(loggedInUser);
			JsonArray array = new JsonArray();
			array.add(filePath);
			userAuto.setPolicyUrls(array.toString());
			baseDao.update(UserAuto.class, userAuto);

		} else {
			userAuto = new UserAuto();
			String existingUrls = userAuto.getPolicyUrls();
			JsonArray array = JsonUtil.isJson(existingUrls) ? new Gson().fromJson(existingUrls, JsonArray.class)
					: new JsonArray();
			array.add(filePath);
			userAuto.setPolicyUrls(array.toString());
			baseDao.create(userAuto);
		}

	}

	public String saveFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {

		// TODO: Save to cloud
		String UPLOAD_PATH = "/tmp/";
		String filePath = null;
		try {
			int read = 0;
			byte[] bytes = new byte[1024];
			filePath = UPLOAD_PATH + fileMetaData.getFileName();
			OutputStream out = new FileOutputStream(new File(filePath));
			while ((read = fileInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException("Error while uploading file. Please try again !!");
		}

		return filePath;
	}

}
