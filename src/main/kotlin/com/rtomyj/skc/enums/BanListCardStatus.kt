package com.rtomyj.skc.enums

enum class BanListCardStatus(
	val status: String
) {
	/**
	 * Defines statuses of a card a ban list that is used by the database.
	 * In other words, these strings are used in the database to differentiate between different statuses.
	 */
	/**
	 * Card cannot be used in advanced format
	 */
	FORBIDDEN("Forbidden"),

	/**
	 * Only one instance of the card can be used.
	 */
	LIMITED("Limited"),

	/**
	 * Only two instance of the card can be used.
	 */
	SEMI_LIMITED("Semi-Limited");

	/**
	 * String representation of enum.
	 */
	override fun toString(): String {
		return status
	}
}