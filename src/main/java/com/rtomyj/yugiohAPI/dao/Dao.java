package com.rtomyj.yugiohAPI.dao;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import java.util.List;

/**
 * Contract for database operations.
 */
public interface Dao
{
	public enum Status
	{
		FORBIDDEN("forbidden"),
		LIMITED("limited"),
		SEMI_LIMITED("semi-limited");

		private final String status;

		Status(final String status)
		{
			this.status = status;
		}

		@Override
		public String toString()
		{
			return status;
		}
	}



	/**
	 * Get the list of dates of all the ban lists stored in the database.
	 * @return A list of BanLists
	 */
	public List<BanLists> getBanListStartDates();

	/**
	 * Retrieve the information about a Card given the ID.
	 * @param cardID The ID of a Yugioh card.
	 * @return The Card requested.
	 */
	public Card getCardInfo(String cardID);

	/**
	 *
	 * @param date Valid start date of the ban list desired.
	 * @param status The status
	 * @return List of Cards that have the status wanted for the desired date.
	 */
	public List<Card> getBanListByBanStatus(String date, Status status);
}