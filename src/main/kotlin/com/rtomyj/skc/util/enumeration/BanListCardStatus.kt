package com.rtomyj.skc.util.enumeration

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
	SEMI_LIMITED("Semi-Limited"),

	/**
	 * Only one card can be used from this pool MAX - Duel Links limited list.
	 */
	LIMITED_ONE("Limited 1"),

	/**
	 * Only two card can be used from this pool MAX - Duel Links semi-limited list.
	 */
	LIMITED_TWO("Limited 2"),


	/**
	 * Only three card can be used from this pool MAX - Duel Links specific.
	 */
	LIMITED_THREE("Limited 3");

	/**
	 * String representation of enum.
	 */
	override fun toString(): String {
		return status
	}
}