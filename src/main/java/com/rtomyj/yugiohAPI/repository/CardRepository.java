package com.rtomyj.yugiohAPI.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class CardRepository
{
	@Autowired
	JdbcTemplate jdbcConn;

	public Card getCardInfo(String cardID)
	{
		return jdbcConn.query(String.format(
			"SELECT card_name, monster_type, card_color, card_effect, card_attribute, monster_attack, monster_defense FROM cards, card_colors WHERE card_number = '%s' AND card_colors.color_id = cards.color_id",
				cardID), new ResultSetExtractor<Card>()
			{
				@Override
				public Card extractData(ResultSet row) throws SQLException, DataAccessException
				{
					row.next();
					Card card = new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4), cardID,
							row.getString(5), row.getInt(6), row.getInt(7));
					return card;
				}
			});
	}
}