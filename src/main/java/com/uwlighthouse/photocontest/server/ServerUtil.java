package com.uwlighthouse.photocontest.server;

import static org.joda.time.Weeks.weeksBetween;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ServerUtil {
	public static final String S3_BUCKET = "images.uwlighthouse.com";
	public static final String S3_BUCKET_URL = "http://" + S3_BUCKET + "/";
	public static final DateTime CONTEST_START = new DateTime(2012, 11, 5, 18, 30, DateTimeZone.forID("America/Los_Angeles")); // 6:30 PM 11/5/12 PST


	/**
	 * @return The current contest week where the first contest week is 1.
	 */
	public static int getCurrentWeekNumber() {
		return weeksBetween(CONTEST_START, new DateTime()).getWeeks() + 1;
	}

	/**
	 * @return The next contest week number where 2 would be returned during the first contest week.
	 */
	public static int getNextWeekNumber() {
		return getCurrentWeekNumber() + 1;
	}
}
