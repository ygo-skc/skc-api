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

	public Card getCardInfo(String cardName)
	{
		return jdbcConn.query(String.format(
			"SELECT monster_type, color_id, card_effect, card_number, card_attribute, monster_attack, monster_defense FROM cards WHERE card_name = '%s'",
			cardName), new ResultSetExtractor<Card>()
			{
				@Override
				public Card extractData(ResultSet row) throws SQLException, DataAccessException
				{
					row.next();
					Card card = new Card(cardName, row.getString(1), "card_color", row.getString(3), row.getString(4),
							row.getString(5), row.getInt(6), row.getInt(7));
					return card;
				}
			});
	}
}