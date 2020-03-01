package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.ErrConstants;
import com.rtomyj.yugiohAPI.model.BanList;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.BanListStartDates;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.CardSearchCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository()
@Qualifier("jdbc")
@Slf4j
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



	public List<BanListComparisonResults> getNewContentOfBanList(final String newBanList, final Status status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select new_cards.card_number, cards.card_name from (select new_list.card_number")
			.append(" from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)")
			.append(" as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList")
			.append(" and ban_status = :status) as old_list on new_list.card_number = old_list.card_number")
			.append(" where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number")
			.append(" ORDER BY cards.card_name")
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



	public List<BanListComparisonResults> getRemovedContentOfBanList(String newBanList)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select removed_cards.card_number, removed_cards.ban_status, cards.card_name")
			.append(" from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists")
			.append(" where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status")
			.append(" from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number")
			.append(" where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number")
			.append(" ORDER BY cards.card_name")
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



	public List<Card> getCardNameByCriteria(final CardSearchCriteria cardSearchCriteria)
	{
		final String cardId = new StringBuilder().append('%').append(cardSearchCriteria.getCardId()).append('%').toString();
		final String cardName = new StringBuilder().append('%').append(cardSearchCriteria.getCardName()).append('%').toString();

		final String cardAttribute = (cardSearchCriteria.getCardAttribute() == "")? ".*" : cardSearchCriteria.getCardAttribute();
		final String cardColor = (cardSearchCriteria.getCardColor() == "")? ".*" : cardSearchCriteria.getCardColor();
		final String monsterType = (cardSearchCriteria.getMonsterType() == "")? ".*" : cardSearchCriteria.getMonsterType();

		final String query = new StringBuilder()
			.append("SELECT DISTINCT cards.card_number, card_color, card_name, card_attribute, card_effect, monster_type, monster_attack, monster_defense, ban_list_date, ban_status, cards.color_id")
			.append(" FROM cards, card_colors, ban_lists")
			.append(" WHERE cards.color_id = card_colors.color_id AND cards.card_number = ban_lists.card_number AND cards.card_number LIKE :cardId AND card_name LIKE :cardName")
			.append(" AND card_attribute REGEXP :cardAttribute AND card_color REGEXP :cardColor AND monster_type REGEXP :monsterType ORDER BY color_id, card_name, ban_list_date DESC")
			.toString();


		MapSqlParameterSource sqlParams = new MapSqlParameterSource();
		sqlParams.addValue("cardId", cardId);
		sqlParams.addValue("cardName", cardName);
		sqlParams.addValue("cardAttribute", cardAttribute);
		sqlParams.addValue("cardColor", cardColor);
		sqlParams.addValue("monsterType", monsterType);

		return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
			/*
				Since a join between ban lists and card info is done, there will be multiple rows having the same card info (id, name, atk, etc) but with different ban info.
				ie:	ID		Name		BanList
						1234	Stratos	2019-07-15
						1234	Stratos	2019-04-29
				To prevent this, the map will use the cardId (unique) to map to a Card object containing info already gathered from previous rows.
				An array within the Card object will then be used to keep track of all the ban lists the card was a part of. The array will be updated
				 every time a new row has new ban list info of a card already in the map.
			*/
			final Map<String, Card> cardInfoTracker = new HashMap<>();
			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			while (row.next())
			{
				Card card = cardInfoTracker.get(row.getString(1));

				if (card == null)
				{
					card = Card.builder()
						.cardID(row.getString(1))
						.cardColor(row.getString(2))
						.cardName(row.getString(3))
						.cardAttribute(row.getString(4))
						.cardEffect(row.getString(5))
						.monsterType(row.getString(6))
						.monsterAttack(row.getInt(7))
						.monsterDefense(row.getInt(8))
						.restrictedIn(new ArrayList<>())
						.build();
						cardInfoTracker.put(card.getCardID(), card);
				}

				try
				{
					card.getRestrictedIn()
						.add(BanList
							.builder()
							.banListDate(dateFormat.parse(row.getString(9)))
							.banStatus(row.getString(10))
							.build());
				} catch (ParseException e)
				{
					log.error("Error occurred while parsing date for ban list, date: {}", row.getString(9));
				}
			}

			return cardInfoTracker.values();
		}).stream().collect(Collectors.toList());
	}
}