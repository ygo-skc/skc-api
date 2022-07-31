package com.rtomyj.skc.banlist.dao

import com.rtomyj.skc.enums.BanListCardStatus
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.banlist.model.BanListDates
import com.rtomyj.skc.banlist.model.CardBanListStatus
import com.rtomyj.skc.banlist.model.CardsPreviousBanListStatus
import com.rtomyj.skc.browse.card.model.Card

interface BanListDao {
	/**
	 * Checks the databases and returns a list of cards in a specified ban list (date) that has the specified status (forbidden, limited, semi-limited)
	 * @param date Valid start date of the ban list desired.
	 * @param status The status
	 * @return List of Cards that have the status wanted for the desired date.
	 */
	fun getBanListByBanStatus(date: String, status: BanListCardStatus): List<Card>

	/**
	 * Checks the database for the number of ban lists stored.
	 * @return the number of ban lists in database.
	 */
	fun numberOfBanLists(): Int

	/**
	 * todo update me
	 * Returns an integer ([1, n]) that corresponds to the position of the banListDate when the database is sorted by banListDate ASC.
	 * @return The position of the ban list queried in the database: -1 if not in database, 1 if it is the first ban list while database is sorted ASC.
	 */
	fun banListDatesInOrder(): List<String>

	/**
	 * Checks the database and returns the date of the previous ban list as the one passed into the method.
	 *
	 * Returns an empty string if there are no previous ban lists or if the ban list date requested doesn't exist in database,
	 * ie: its the oldest ban list or the date isn't a date where a ban list started.
	 * @param currentBanList the date of a ban list to use to find the previous sequential ban list relative to it.
	 * @return String of the date of the previous ban list.
	 */
	fun getPreviousBanListDate(currentBanList: String): String

	/**
	 * Checks the database to find card(s) that where added to the ban list, ie: the card(s) was not in the previous ban list.
	 * Checks the database to find card(s) that have switched status compared to the previous ban list, ie: a card went from forbidden to limited.
	 * @param banListDate the date of the ban list to get the newly added cards for.
	 * @param status (forbidden, limited, semi-limited) used to get only new cards for that status.
	 * @return A list of maps that contains the following:
	 * id: Identifier of newly added card
	 * previousStatus: status the card had on the previous ban list, empty string if card wasn't in previous ban list.
	 */
	fun getNewContentOfBanList(banListDate: String, status: BanListCardStatus): List<CardsPreviousBanListStatus>

	/**
	 *
	 * @param banListDate effective date of a YGO ban list.
	 * @return the contents of the previous ban list in relation to `banListDate`.
	 */
	fun getRemovedContentOfBanList(banListDate: String): List<CardsPreviousBanListStatus>

	fun isValidBanList(banListDate: String): Boolean

	/**
	 * Get the list of dates of all the ban lists stored in the database.
	 * @return A list of BanList
	 */
	@Throws(YgoException::class)
	fun getBanListDates(): BanListDates

	fun getBanListDetailsForCard(cardId: String): List<CardBanListStatus>

	/**
	 *
	 * @param cardId unique identifier for a YGO card.
	 * @param banListDate effective date of a YGO ban list.
	 * @return ban status for card in a specific ban list (forbidden, limited, semi-limted).
	 */
	fun getCardBanListStatusByDate(cardId: String, banListDate: String): String
}