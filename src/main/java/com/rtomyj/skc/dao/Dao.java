package com.rtomyj.skc.dao;

import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.DownstreamStatus;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.card.CardBrowseResults;
import com.rtomyj.skc.model.card.MonsterAssociation;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;

import java.util.List;
import java.util.Set;

/**
 * Contract for database operations.
 */
public interface Dao
{
	/**
	 * Defines statuses of a card a ban list that is used by the database.
	 * In other words, these strings are used in the database to differentiate between different statuses.
	 */
    enum Status
	{
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

		private final String status;

		Status(final String status)
		{
			this.status = status;
		}

		/**
		 * String representation of enum.
		 */
		@Override
		public String toString()
		{
			return status;
		}
	}

	DownstreamStatus dbConection() throws YgoException;

	/**
	 * Retrieve the information about a Card given the ID.
	 * @param cardID The ID of a Yugioh card.
	 * @return The Card requested.
	 */
    Card getCardInfo(String cardID) throws YgoException;

	List<Card> searchForCardWithCriteria(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType, final int limit, final boolean getBanInfo);

	MonsterTypeStats getMonsterTypeStats(final String cardColor);

	DatabaseStats getDatabaseStats();

	CardBrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> attributeSet, final Set<String> monsterTypeSet, final Set<String> monsterSubTypeSet, final Set<String> monsterLevels, Set<String> monsterRankSet, Set<String> monsterLinkRatingsSet);

	Set<String> getCardColors();

	Set<String> getMonsterAttributes();

	Set<String> getMonsterTypes();

	Set<String> getMonsterSubTypes();

	Set<MonsterAssociation> getMonsterAssociationField(final String monsterAssociationField);

}