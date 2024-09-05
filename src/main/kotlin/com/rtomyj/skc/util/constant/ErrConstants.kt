package com.rtomyj.skc.util.constant

object ErrConstants {
  const val BAN_LIST_NOT_FOUND_FOR_START_DATE = "There is no ban list in DB with date: %s"
  const val NO_NEW_BAN_LIST_CONTENT_FOR_START_DATE = "There was no new content for ban list starting on date: %s"
  const val NO_REMOVED_BAN_LIST_CONTENT_FOR_START_DATE =
    "There was no removed content for ban list starting on date: %s"
  const val CARD_ID_REQUESTED_NOT_FOUND_IN_DB = "Unable to find card in DB with ID: %s"
  const val DB_MISSING_TABLE = "DB not setup properly, missing table"
}