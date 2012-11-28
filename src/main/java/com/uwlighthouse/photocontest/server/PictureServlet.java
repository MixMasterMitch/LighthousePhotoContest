package com.uwlighthouse.photocontest.server;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;
import static com.google.common.collect.Lists.newArrayList;
import static com.uwlighthouse.photocontest.aws.AwsUtil.getS3Client;
import static com.uwlighthouse.photocontest.server.ServerUtil.S3_BUCKET;
import static com.uwlighthouse.photocontest.server.ServerUtil.getCurrentWeekNumber;
import static com.uwlighthouse.photocontest.server.ServerUtil.getNextWeekNumber;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.Gson;
import com.uwlighthouse.photocontest.daos.PictureDao;
import com.uwlighthouse.photocontest.daos.UserDao;
import com.uwlighthouse.photocontest.databaseobjects.Picture;
import com.uwlighthouse.photocontest.databaseobjects.User;

public class PictureServlet extends HttpServlet {

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

		Object previousWinnersParameter = request.getParameter("previousWinners");
		if (previousWinnersParameter != null && previousWinnersParameter.equals("true")) {
			// For each week get the winning picture
			for (int i = 1; i < week; i++) {
				Picture winner = null;
				for (Picture pic : new PictureDao().findByWeek(i)) {
					if (winner == null || winner.getVotes().size() < pic.getVotes().size()) {
						winner = pic;
					}
				}
				images.add(new ImageDto(winner));
			}
		} else {
			// Get this week's image urls and captions.
			for (Picture pic : new PictureDao().findByWeek(week)) {
				images.add(new ImageDto(pic));
			}
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
			uploadFailed(response);
			return;
		}

		// Instantiate user, picture, and uploadPicture
		User user = new User();
		Picture picture = new Picture();
		File uploadPicture = File.createTempFile("picture-upload-", ".jpg");

		// Sets form fields to user and picture
		// Writes picture to uploadPicture
		for (FileItem item : items) {
			if (item.getFieldName().equals("name")) {
				user.setName(item.getString());
			} else if (item.getFieldName().equals("id")) {
				user.setFacebookId(item.getString());
			} else if (item.getFieldName().equals("caption")) {
				picture.setCaption(item.getString());
			} else if (item.getFieldName().equals("picture")) {
				try {
					if (item.getContentType().equals("image/jpeg") || item.getContentType().equals("image/png")) {
						item.write(uploadPicture);
					} else {
						uploadFailed(response);
						return;
					}
				} catch (Exception e) {
					uploadFailed(response);
				}
			}
		}

		// Resize image
		File formatedPicture = File.createTempFile("picture-formatted-", ".jpg");
		Thumbnails.of(uploadPicture)
		.size(800, 1600)
		.outputFormat("jpg")
		.outputQuality(1)
		.toFile(formatedPicture);

		// Write uploadPicture to S3
		AmazonS3 s3 = getS3Client();
		String key = "week-" + getNextWeekNumber() + "/" + user.getName().toLowerCase().replace(' ', '_') + ".jpg"; // week-#/first_last.jpg
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpeg");
		s3.putObject(new PutObjectRequest(S3_BUCKET, key, formatedPicture).withCannedAcl(PublicRead).withMetadata(metadata));

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

		response.getWriter().append("upload_success");
	}

	private void uploadFailed(HttpServletResponse response) throws IOException {
		response.getWriter().append("upload_failed");
	}
}
