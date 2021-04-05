package com.reit.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.LookupError;
import com.dropbox.core.v2.sharing.AddMember;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.MemberSelector;
import com.dropbox.core.v2.sharing.ShareFolderErrorException;
import com.google.common.base.Preconditions;
import com.reit.beans.LoiRequestBean;

public class DropBoxService {

	private static final String ACCESS_TOKEN = "b0OFKP8OTJ8AAAAAAAAAAVqA0nbXC0jV5pWwsROYD_ndZGBw7EsjNg06acCGg19t";
	private static final DbxRequestConfig config = DbxRequestConfig.newBuilder("dev_reit").build();
	private static final DbxClientV2 dbxClient = new DbxClientV2(config, ACCESS_TOKEN);
	private static String outputDir = "/tmp";

	public String createDocument(String propertyName, String category, XWPFDocument doc, LoiRequestBean loiRequestBean)
			throws Exception {
		// check if folder exists for the address, add auth to the folder
		// create root folder
		String rootPath = scrubFolderName(propertyName);
		String storedPath = null;
		try {
			rootPath = createFolder(rootPath);
			shareFolder(rootPath, loiRequestBean.getBuyerEmail());
			// check if category folder exists for the document category, add auth to the
			// folder
			String categoryPath = scrubFolderName(category);
			createFolder(rootPath + categoryPath);

			// save file with date
			// write file to temp directory with a unique filename- There may be a way to
			// skip it
			String tmpFilePath = String.format("%s/%d_%s", outputDir, System.currentTimeMillis(), category);
			File newFile = new File(tmpFilePath);
			// newFile.createNewFile();
			FileOutputStream out = new FileOutputStream(newFile);
			doc.write(out);

			// write to dropbox
			String dropBoxFileName = String.format("%s%s/%s.docx", rootPath, categoryPath, category);
			try (InputStream in = new FileInputStream(tmpFilePath)) {
				FileMetadata metadata = dbxClient.files().uploadBuilder(dropBoxFileName).uploadAndFinish(in);
				storedPath = metadata.getPathLower();

				System.out.println(metadata.getPathLower());

			}

			// return link
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Error creating folder/file");
		}

		return storedPath;

	}

	public String scrubFolderName(String name) {
		String path = name.trim().replace(" ", "_").replace(",", "_").replace("-", "_");
		path = path.startsWith("/") ? path : ("/" + path);
		return path;
	}

	public String createFolder(String path) throws CreateFolderErrorException, DbxException {
		Preconditions.checkNotNull(path, "Path is null");
		Preconditions.checkArgument(path.startsWith("/"), "Path param should start with /");

		try {
			dbxClient.files().getMetadata(path);
		} catch (GetMetadataErrorException e) {
			// TODO Auto-generated catch block
			if (e.errorValue.isPath()) {
				LookupError le = e.errorValue.getPathValue();
				if (le.isNotFound()) {
					FolderMetadata folderMetadata = dbxClient.files().createFolderV2(path, true).getMetadata();

					path = folderMetadata.getPathLower();
				}

			}
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// path could be same as input param
		return path;

	}

	public void shareFolder(String path, String shareEmail) throws ShareFolderErrorException, DbxException {

		String sharedFolderId = null;

		ListSharedLinksResult listSharedLinksResult = dbxClient.sharing().listSharedLinksBuilder().withPath(path)
				.withDirectOnly(true).start();

		if (listSharedLinksResult.getLinks().isEmpty()) {
			sharedFolderId = dbxClient.sharing().shareFolder(path).getCompleteValue().getSharedFolderId();
		} else {
			sharedFolderId = dbxClient.files().getMetadata(path).getParentSharedFolderId();
		}

		List<AddMember> list = Arrays.asList(new AddMember(MemberSelector.email(shareEmail)));

		dbxClient.sharing().addFolderMember(sharedFolderId, list);

	}

}
