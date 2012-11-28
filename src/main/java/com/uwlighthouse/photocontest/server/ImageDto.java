package com.uwlighthouse.photocontest.server;

import static com.uwlighthouse.photocontest.server.ServerUtil.S3_BUCKET_URL;

import com.uwlighthouse.photocontest.databaseobjects.Picture;

public class ImageDto {
	private String url;
	private String caption;
	private String photographer;

	public ImageDto(Picture picture) {
		this(S3_BUCKET_URL + picture.getImageKey(), picture.getCaption(), picture.getUser().getName());
	}

	public ImageDto(String url, String caption, String photographer) {
		this();
		this.url = url;
		this.caption = caption;
		this.photographer = photographer;
	}

	public ImageDto() {
		super();
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getPhotographer() {
		return photographer;
	}
	public void setPhotographer(String photographer) {
		this.photographer = photographer;
	}
}
