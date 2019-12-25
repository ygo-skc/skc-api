package com.rtomyj.yugiohAPI.helper;

import java.util.regex.Pattern;

/**
 * Contains regex expressions for validating user input in queries.
 */
public class ResourceValidator
{
	/**
	 * Regex pattern for a cards ID - an 9 digit string
	 */
	private static final Pattern cardIdPattern = Pattern.compile("[0-9]{8}");

	/**
	 * Regex pattern for a date used for querying a specific ban list - {4 digits - 2 digits - 2 digits}
	 */
	private static final Pattern banListDatePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

	/**
	 * Check cardId for validity.
	 * @param cardId the string representation of an ID of a Yugioh card.
	 * @return Whether the specified ID is valid.
	 */
	public static boolean isValidCardId(String cardId)
	{
		return cardIdPattern.matcher(cardId).matches();
	}

	/**
	 * Check ban list date for validity.
	 * @param banListDate the string representation of a date used by the database to query a specific ban list.
	 * @return Whether the specified date is valid.
	 */
	public static boolean isValidBanListDate(String banListDate)
	{
		return banListDatePattern.matcher(banListDate).matches();
	}

}