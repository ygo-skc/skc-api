package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.BanListStartDates;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository()
@Qualifier("jdbc")
public class JDBCDao implements Dao
{
	@Autowired
	NamedParameterJdbcTemplate jdbcNamedTemplate;



	@Override
	public BanListStartDates getBanListStartDates()
	{
		return null;
	}



	@Override
	public Card getCardInfo(final String cardID) throws YgoException
	{
		String query = new StringBuilder().append("SELECT card_name, cards.monster_type, card_colors.card_color, cards.card_effect, cards.card_attribute")
			.append(", cards.monster_attack, cards.monster_defense FROM cards, card_colors WHERE cards.card_number = :cardId AND card_colors.color_id = cards.color_id")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardID);


		final Card card = jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) ->
		{
			if (row.next())
			{
				return Card.builder().cardName(row.getString(1))
					.monsterType(row.getString(2))
					.cardColor(row.getString(3))
					.cardEffect(row.getString(4))
					.cardID(cardID)
					.cardAttribute(row.getString(5))
					.monsterAttack(row.getInt(6))
					.monsterDefense(row.getInt(7)).build();
			}

			return null;
		});

		if (card == null)	throw new YgoException(ErrConstants.NOT_FOUND_DAO_ERR, String.format("%s was not found in DB.", cardID));

		return card;
	}



	@Override
	public List<Card> getBanListByBanStatus(String date, Status status)
	{
		String query = new StringBuilder()
			.append("SELECT card_name, monster_type, card_colors.card_color, card_effect, cards.card_number FROM card_colors, cards, ban_lists")
			.append(" WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number")
			.append(" AND ban_lists.ban_status = :status AND ban_list_date = :date ORDER BY card_colors.card_color, card_name")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("date", date);
		sqlParams.addValue("status", status.toString());


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			List<Card> cardList = new ArrayList<>();
			while (row.next()) {
				cardList.add(
					Card.builder().cardName(row.getString(1))
						.monsterType(row.getString(2))
						.cardColor(row.getString(3))
						.cardEffect(row.getString(4))
						.cardID(row.getString(5)).build()
					);
			}
			return cardList;
		});
	}



	public int getNumberOfBanLists() {

		String query = "SELECT COUNT(DISTINCT ban_list_date) AS 'Total Ban Lists' FROM ban_lists";
		return jdbcNamedTemplate.query(query, (ResultSet row) ->  {
			if (row.next())	return Integer.parseInt(row.getString(1));

			return null;
		});
	}



	public int getBanListPosition(String banListDate)
	{
		String query = new StringBuilder().append("SELECT row_num FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date")
			.append(" FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC) AS dates, (SELECT @row_num:=0) counter)")
			.append(" AS sorted WHERE ban_list_date = :banListDate")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("banListDate", banListDate);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return (int) Float.parseFloat(row.getString(1));	// somehow row_num is treated as a float

			return -1;
		});
	}



	public String getPreviousBanListDate(String currentBanList)
	{
		int currentBanListPosition = this.getBanListPosition(currentBanList);
		if (currentBanListPosition <= 1)	return "";
		int previousBanListPosition = currentBanListPosition - 1;

		String query = new StringBuilder()
			.append("SELECT ban_list_date FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date")
			.append(" FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC)")
			.append(" AS dates, (SELECT @row_num:=0) counter) AS sorted where row_num = :previousBanListPosition")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("previousBanListPosition", previousBanListPosition);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<BanListComparisonResults> getNewContentOfBanList(final String newBanList, final Status status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select new_cards.card_number, cards.card_name from (select new_list.card_number")
			.append(" from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)")
			.append(" as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList")
			.append(" and ban_status = :status) as old_list on new_list.card_number = old_list.card_number")
			.append(" where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number;")
			.toString();

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("status", status.toString());
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<BanListComparisonResults> newCards = new ArrayList<>();

			while (row.next())
			{
				BanListComparisonResults banListComparisonResults = new BanListComparisonResults();
				final String cardID = row.getString(1);
				String previousStatus = this.getCardBanListStatusByDate(cardID, oldBanList);
				previousStatus = ( previousStatus == null ) ? "Unlimited" : previousStatus;


				banListComparisonResults.setId(cardID);
				banListComparisonResults.setPreviousState(previousStatus);
				banListComparisonResults.setName(row.getString(2));

				newCards.add(banListComparisonResults);
			}

			return newCards;
		});
	}



	// TODO: make sure you write a test for the instance where the last ban list is selected
	public List<BanListComparisonResults> getRemovedContentOfBanList(String newBanList)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select removed_cards.card_number, removed_cards.ban_status, cards.card_name")
			.append(" from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists")
			.append(" where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status")
			.append(" from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number")
			.append(" where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number;")
			.toString();

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("newBanList", newBanList);
		sqlParams.addValue("oldBanList", oldBanList);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			final List<BanListComparisonResults> REMOVED_CARDS = new ArrayList<>();

			while(row.next())
			{
				final BanListComparisonResults REMOVED_CARD = new BanListComparisonResults();

				REMOVED_CARD.setId(row.getString(1));
				REMOVED_CARD.setPreviousState(row.getString(2));
				REMOVED_CARD.setName(row.getString(3));

				REMOVED_CARDS.add(REMOVED_CARD);
			}

			return REMOVED_CARDS;
		});
	}



	public String getCardBanListStatusByDate(String cardId, String banListDate)
	{
		String query = "select ban_status from ban_lists where card_number = :cardId and ban_list_date = :banListDate";

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("banListDate", banListDate);


		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	public String getCardInfoByCardNameSearch(String cardName)
	{
		String query = "SELECT DISTINCT * FROM cards WHERE card_name LIKE '%:cardName%'";

		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		return null;
	}



	public boolean isValidBanList(final String banListDate)
	{
		final String query = "select distinct ban_list_date from ban_lists where ban_list_date = :banListDate";

		final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("banListDate", banListDate);

		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			if (row.next())	return true;
			else	return false;
		});
	}
}