package com.uwlighthouse.photocontest.server;

public class ItemDto {
	private String thisWeek;
	private String nextWeek;

	public ItemDto(String thisWeek, String nextWeek) {
		super();
		setThisWeek(thisWeek);
		setNextWeek(nextWeek);
	}

	public ItemDto() {
		super();
	}

	public String getThisWeek() {
		return thisWeek;
	}

	public void setThisWeek(String thisWeek) {
		this.thisWeek = thisWeek;
	}

	public String getNextWeek() {
		return nextWeek;
	}

	public void setNextWeek(String nextWeek) {
		this.nextWeek = nextWeek;
	}
}
