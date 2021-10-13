package com.rtomyj.skc.constant;


public class ErrConstants
{
	private ErrConstants() {
		throw new UnsupportedOperationException("Cannot create instance for class: " + this.getClass().toString());
	}


	public static final String BAN_LIST_NOT_FOUND_FOR_START_DATE = "There is no ban list in DB with date: %s";
	public static final String NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE = "There was no new content for ban list starting on date: %s";
	public static final String NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE = "There was no removed content for ban list starting on date: %s";

	public static final String CARD_ID_REQUESTED_NOT_FOUND_IN_DB = "Unable to find card in DB with ID: %s";

	public static final String DB_MISSING_TABLE = "DB not setup properly, missing table";
}