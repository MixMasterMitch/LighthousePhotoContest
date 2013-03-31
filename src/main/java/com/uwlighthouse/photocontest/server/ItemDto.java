package com.uwlighthouse.photocontest.server;

import com.uwlighthouse.photocontest.databaseobjects.Item;

public class ItemDto {
	private String thisWeek;
	private String nextWeek;

	public ItemDto(Item thisWeek, Item nextWeek) {
		super();
		if (thisWeek != null) {
			setThisWeek(thisWeek.getItem());
		}
		if (nextWeek != null) {
			setNextWeek(nextWeek.getItem());
		}
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
