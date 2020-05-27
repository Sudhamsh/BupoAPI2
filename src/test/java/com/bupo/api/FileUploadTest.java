package com.bupo.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

public class FileUploadTest extends JerseyTest {

	@Override
	protected Application configure() {
		// register(MultiPartFeature.class)
		return new ResourceConfig(FileUpload.class, MultiPartFeature.class);
	}

	public static void main(String[] args) throws IOException {
		final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

		final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("/tmp/toUpload/PGRDeclarations.pdf"));
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar")
				.bodyPart(filePart);

		final WebTarget target = client.target("http://localhost:8080/rest/upload/pdf");
		final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

		// Use response object to verify upload success

		formDataMultiPart.close();
		multipart.close();
	}

	@Test
	public void fileUploadTest() {

		try {

			FormDataMultiPart form = new FormDataMultiPart();
			URI uri = new File("/tmp/meetingJMT.txt").toURI();
			InputStream data = this.getClass().getResourceAsStream("filePath");
			FormDataBodyPart fdp1 = new FormDataBodyPart("key1", uri.toString());
			FormDataBodyPart fdp2 = new FormDataBodyPart("key2", data, MediaType.APPLICATION_OCTET_STREAM_TYPE);

			form.bodyPart(fdp1).bodyPart(fdp2);
			Response response = target("/upload/pdf").request().post(Entity.entity(fdp2, fdp2.getMediaType()));

			Assert.assertEquals(response.getStatus(), Status.OK.getStatusCode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
