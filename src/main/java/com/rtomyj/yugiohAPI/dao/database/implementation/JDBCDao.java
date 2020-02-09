package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rtomyj.yugiohAPI.configuration.YgoConstants;
import com.rtomyj.yugiohAPI.configuration.exception.YgoException;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanList;
import com.rtomyj.yugiohAPI.model.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository()
@Qualifier("jdbc")
public class JDBCDao implements Dao
{
	@Autowired
	JdbcTemplate jdbcConn;



	@Override
	public List<BanList> getBanListStartDates()
	{
		return null;
	}



	@Override
	public Card getCardInfo(final String cardID) throws YgoException
	{
		String query = new StringBuilder().append("SELECT card_name, cards.monster_type, card_colors.card_color, cards.card_effect, cards.card_attribute")
			.append(", cards.monster_attack, cards.monster_defense FROM cards, card_colors WHERE cards.card_number = '%s' AND card_colors.color_id = cards.color_id")
			.toString();
		query = String.format(query, cardID);

		final Card card = jdbcConn.query(query, (ResultSet row) ->
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

		if (card == null)	throw new YgoException(YgoConstants.DAO_NOT_FOUND_ERR, String.format("%s was not found in DB.", cardID));

		return card;
	}



	@Override
	public List<Card> getBanListByBanStatus(String date, Status status)
	{
		String query = "SELECT card_name, monster_type, card_colors.card_color, card_effect, cards.card_number FROM card_colors, cards, ban_lists WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number AND ban_lists.ban_status = '"
		+ status.toString() + "' AND ban_list_date = '" + date + "' ORDER BY card_colors.card_color, card_name";

		return jdbcConn.query(query, (ResultSet row) -> {
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
		return jdbcConn.query(query, (ResultSet row) ->  {
			if (row.next())	return Integer.parseInt(row.getString(1));

			return null;
		});
	}



	public int getBanListPosition(String banListDate)
	{
		String query = new StringBuilder().append("SELECT row_num FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date")
			.append(" FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC) AS dates, (SELECT @row_num:=0) counter)")
			.append(" AS sorted WHERE ban_list_date = '%1$s'")
			.toString();
		query = String.format(query, banListDate);

		return jdbcConn.query(query, (ResultSet row) -> {
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
			.append(" AS dates, (SELECT @row_num:=0) counter) AS sorted where row_num = %1$d")
			.toString();
		query = String.format(query, previousBanListPosition);

		return jdbcConn.query(query, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	public List<BanListComparisonResults> getNewContentOfBanList(String newBanList, String status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == "")	return new ArrayList<BanListComparisonResults>();

		String query = new StringBuilder()
			.append("select new_cards.card_number, cards.card_name from (select new_list.card_number")
			.append(" from (select card_number from ban_lists where ban_list_date = '%2$s' and ban_status = '%1$s')")
			.append(" as new_list left join (select card_number from ban_lists where ban_list_date = '%3$s'")
			.append(" and ban_status = '%1$s') as old_list on new_list.card_number = old_list.card_number")
			.append(" where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number;")
			.toString();
		query = String.format(query, status, newBanList, oldBanList);

		return jdbcConn.query(query, (ResultSet row) -> {
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
			.append(" where ban_list_date = '%1$s') as new_list right join (select card_number, ban_status")
			.append(" from ban_lists where ban_list_date = '%2$s') as old_list on new_list.card_number = old_list.card_number")
			.append(" where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number;")
			.toString();

		query = String.format(query, newBanList, oldBanList);

		return jdbcConn.query(query, (ResultSet row) -> {
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
		String query = "select ban_status from ban_lists where card_number = '%1$s' and ban_list_date = '%2$s';";
		query = String.format(query, cardId, banListDate);

		return jdbcConn.query(query, (ResultSet row) -> {
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
}