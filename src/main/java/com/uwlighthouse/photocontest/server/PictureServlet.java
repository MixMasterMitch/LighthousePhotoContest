package com.uwlighthouse.photocontest.server;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static com.google.common.collect.Lists.newArrayList;
import static com.uwlighthouse.photocontest.aws.AwsUtil.getS3Client;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.uwlighthouse.photocontest.daos.PictureDao;
import com.uwlighthouse.photocontest.daos.UserDao;
import com.uwlighthouse.photocontest.databaseobjects.Picture;
import com.uwlighthouse.photocontest.databaseobjects.User;
import com.uwlighthouse.photocontest.server.ImageDto;

public class PictureServlet extends HttpServlet {

	private static final String S3_BUCKET = "images.uwlighthouse.com";
	private static final String S3_BUCKET_URL = "http://" + S3_BUCKET + "/";
	private static final DateTime CONTEST_START = new DateTime(2012, 11, 5, 6, 30, DateTimeZone.forID("America/Los_Angeles")); // 6:30 PM 11/5/12 PST

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124673752944398246L;

	/**
	 * Responds with the current week's image urls and captions as JSON in the following form:
	 * [{url: "http://...jpeg", caption: "Caption text"}, ...]
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<ImageDto> images = newArrayList();
		int week = getCurrentWeekNumber();

		// Get this week's image urls and captions.
		for (Picture pic : new PictureDao().findByWeek(week)) {
			images.add(new ImageDto(S3_BUCKET_URL + pic.getImageKey(), pic.getCaption()));
		}

		// Convert to JSON
		response.setContentType("application/json");
		response.getWriter().print(new Gson().toJson(images));
	}

	/**
	 * Posts a picture to S3 and RDS. Overwrites an existing picture for the week.
	 * 
	 * The given request must contain name, id, and caption fields as well as a file.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Parse request
		// see org.apache.commons.fileupload
		List<FileItem> items = null;
		try {
			items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		} catch (FileUploadException e) {
			response.getWriter().append("upload_failed");
		}

		// Instantiate user, picture, and uploadPicture
		User user = new User();
		Picture picture = new Picture();
		File uploadPicture = File.createTempFile("picture-upload-", "jpeg");

		// Sets form fields to user and picture
		// Writes picture to uploadPicture
		for (FileItem item : items) {
			if (item.isFormField()) {
				if (item.getFieldName().equals("name")) {
					user.setName(item.getString());
				} else if (item.getFieldName().equals("id")) {
					user.setFacebookId(item.getString());
				} else if (item.getFieldName().equals("caption")) {
					picture.setCaption(item.getString());
				}
			} else {
				try {
					item.write(uploadPicture);
				} catch (Exception e) {
					response.getWriter().append("upload_failed");
				}
			}
		}

		// Write uploadPicture to S3
		AmazonS3 s3 = getS3Client();
		String key = "week-" + getNextWeekNumber() + "/" + user.getName().toLowerCase().replace(' ', '_') + ".jpeg"; // week-#/first_last.jpeg
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpeg");
		s3.putObject(new PutObjectRequest(S3_BUCKET, key, uploadPicture).withCannedAcl(PublicRead).withMetadata(metadata));

		// Persist user if they don't exist yet
		User persistedUser = new UserDao().findByFacebookId(user.getFacebookId());
		if (persistedUser == null) {
			persistedUser = new UserDao().makePersistent(user);
		}

		// Set remaining picture fields
		picture.setUploadTime(new Date());
		picture.setWeek(getNextWeekNumber());
		picture.setImageKey(key);
		picture.setUser(persistedUser);

		// Delete existing picture if exists
		Picture persistedPicture = new PictureDao().findByImageKey(key);
		if (persistedPicture != null) {
			new PictureDao().makeTransient(persistedPicture);
		}

		// Save picture
		new PictureDao().makePersistent(picture);
	}

	/**
	 * @return The current contest week where the first contest week is 1.
	 */
	private static int getCurrentWeekNumber() {
		return new Period(CONTEST_START, new DateTime()).getWeeks() + 1;
	}

	/**
	 * @return The next contest week number where 2 would be returned during the first contest week.
	 */
	private static int getNextWeekNumber() {
		return getCurrentWeekNumber() + 1;
	}
}
