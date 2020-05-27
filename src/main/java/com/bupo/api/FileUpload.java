package com.bupo.api;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.bupo.services.FileProcessor;

@Path("/upload")
public class FileUpload {
	@POST
	@Path("/pdf")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadPdfFile(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
		System.out.println("here...");
		FileProcessor autoFileProcessor = new FileProcessor();
		autoFileProcessor.processUpload(fileInputStream, fileMetaData);

		return Response.ok("Data uploaded successfully !!").build();
	}
}
