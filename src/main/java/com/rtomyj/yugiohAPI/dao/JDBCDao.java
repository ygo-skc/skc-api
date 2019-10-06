package com.rtomyj.yugiohAPI.dao;

import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository()
@Qualifier("mysql_jdbc")
public class JDBCDao implements Dao {

	@Autowired
	JdbcTemplate jdbcConn;

	@Override
	public List<BanLists> getBanListStartDates() {
		return null;
	}

	@Override
	public Card getCardInfo(String cardID) {
		return jdbcConn.query(String.format(
				"SELECT card_name, cards.monster_type, card_colors.card_color, cards.card_effect, cards.card_attribute, cards.monster_attack, cards.monster_defense FROM cards, card_colors WHERE cards.card_number = '%s' AND card_colors.color_id = cards.color_id",
				cardID), new ResultSetExtractor<Card>() {
					@Override
					public Card extractData(ResultSet row) throws SQLException, DataAccessException {
						if (row.next())
						{
							Card card = new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4), cardID,
									row.getString(5), row.getInt(6), row.getInt(7));
							return card;
						}

						return null;
					}
				});
	}

	@Override
	public List<Card> getBanListByBanStatus(String date, String status) {
		return jdbcConn.query(
			"SELECT card_name, monster_type, card_colors.card_color, card_effect, cards.card_number FROM card_colors, cards, ban_lists WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number AND ban_lists.ban_status = '"
					+ status + "' AND ban_list_date = '" + date + "' ORDER BY card_colors.card_color, card_name;",
			new ResultSetExtractor<List<Card>>() {
				@Override
				public List<Card> extractData(ResultSet row) throws SQLException, DataAccessException {
					List<Card> cardList = new ArrayList<>();
					while (row.next()) {
						cardList.add(
								new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4), row.getString(5)));
					}
					return cardList;
				}
			});
	}

}