package com.uwlighthouse.photocontest.server;

public class ImageDto {
	private String url;
	private String caption;

	public ImageDto(String url, String caption) {
		super();
		this.url = url;
		this.caption = caption;
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
}
