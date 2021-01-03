package com.reit.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

public class DropBoxService {

	private static final String ACCESS_TOKEN = "sl.AomlT4NO1qg8yQ81UrTFGgvRv4SPepezq8PoYi0erdxnBAvRfFVgzAkZNd4zY8_vlHdCnTGZ09K-iIlYnN4qp6MZf0Iivwm8kbUBGfy_qfKdpGpX3Q4FmpxJi6JNY-DDj6Dt1iEb";

	public static void main(String args[]) throws DbxException, FileNotFoundException, IOException {
		// Create Dropbox client
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		// Get current account info
		FullAccount account = client.users().getCurrentAccount();
		System.out.println(account.getName().getDisplayName());

		// Get files and folder metadata from Dropbox root directory
		ListFolderResult result = client.files().listFolder("");
		while (true) {
			for (Metadata metadata : result.getEntries()) {
				System.out.println(metadata.getPathLower());
			}

			if (!result.getHasMore()) {
				break;
			}

			result = client.files().listFolderContinue(result.getCursor());
		}

		// Upload "test.txt" to Dropbox
		try (InputStream in = new FileInputStream("/tmp/test.txt")) {
			FileMetadata metadata = client.files().uploadBuilder("/test.txt").uploadAndFinish(in);
		}
	}
}
