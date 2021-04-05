package com.bupo.services;

import org.junit.Assert;
import org.junit.Test;

import com.dropbox.core.DbxException;
import com.reit.services.DropBoxService;

public class DropBoxServiceTest {
	DropBoxService dropServ = new DropBoxService();

	@Test
	public void createFolder_hp() {
		try {
			dropServ.createFolder("/testProp");
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

}
