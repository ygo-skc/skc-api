package com.rtomyj.yugiohAPI.dao;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
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
	public List<BanLists> getBanListStartDates()
	{
		return null;
	}



	@Override
	public Card getCardInfo(String cardID)
	{
		String query = "SELECT card_name, cards.monster_type, card_colors.card_color, cards.card_effect, cards.card_attribute, cards.monster_attack, cards.monster_defense FROM cards, card_colors WHERE cards.card_number = '%s' AND card_colors.color_id = cards.color_id";
		return jdbcConn.query(String.format(query, cardID), (ResultSet row) ->
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



	/**
	*
	*/
	public int getNumberOfBanLists() {

		String query = "SELECT COUNT(DISTINCT ban_list_date) AS 'Total Ban Lists' FROM ban_lists";
		return jdbcConn.query(query, (ResultSet row) ->  {
			if (row.next())	return Integer.parseInt(row.getString(1));

			return null;
		});
	}



	/**
	 *
	 */
	public int getBanListPosition(String banListDate)
	{
		String query = String.format("SELECT row_num FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC) AS dates, (SELECT @row_num:=0) counter) AS sorted WHERE ban_list_date = '%1$s'", banListDate);

		return jdbcConn.query(query, (ResultSet row) -> {
			if (row.next())	return (int) Float.parseFloat(row.getString(1));	// somehow row_num is treated as a float

			return null;
		});
	}



	/**
	 *
	 */
	public String getPreviousBanListDate(String currentBanList)
	{
		int currentBanListPosition = this.getBanListPosition(currentBanList);
		if (currentBanListPosition == 1)	return null;
		int previousBanListPosition = currentBanListPosition - 1;

		String query = String.format("SELECT ban_list_date FROM (SELECT @row_num:=@row_num+1 row_num, ban_list_date FROM (SELECT DISTINCT ban_list_date FROM ban_lists ORDER BY ban_list_date ASC) AS dates, (SELECT @row_num:=0) counter) AS sorted where row_num = %1$d", previousBanListPosition);

		return jdbcConn.query(query, (ResultSet row) -> {
			if (row.next())	return row.getString(1);
			return null;
		});
	}



	/**
	 *
	 */
	public List<String> getNewContentFromBanList(String newBanList, String status)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == null)	return new ArrayList<String>();

		String query = String.format("select new_list.card_number from (select card_number from ban_lists where ban_list_date = '%2$s' and ban_status = '%1$s') as new_list left join (select card_number from ban_lists where ban_list_date = '%3$s' and ban_status = '%1$s') as old_list on new_list.card_number = old_list.card_number where old_list.card_number is NULL"
		, status, newBanList, oldBanList);

		return jdbcConn.query(query, (ResultSet row) -> {
			List<String> newContent = new ArrayList<String>();

			while (row.next())	newContent.add(row.getString(1));

			return newContent;
		});
	}



	/**
	 *
	 */
	public List<String> getRemovedContentOfBanList(String newBanList)
	{
		String oldBanList = this.getPreviousBanListDate(newBanList);
		if (oldBanList == null)	return new ArrayList<String>();

		String query = String.format("select old_list.card_number from (select card_number from ban_lists where ban_list_date = '%1$s') as new_list right join (select card_number from ban_lists where ban_list_date = '%2$s') as old_list on new_list.card_number = old_list.card_number where new_list.card_number is NULL;", newBanList, oldBanList);

		return jdbcConn.query(query, (ResultSet row) -> {
			List<String> removedContent = new ArrayList<>();

			while(row.next())	removedContent.add(row.getString(1));

			return removedContent;
		});
	}
}