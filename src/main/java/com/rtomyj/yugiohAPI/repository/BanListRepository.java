package com.rtomyj.yugiohAPI.repository;

		import com.rtomyj.yugiohAPI.Card;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.dao.DataAccessException;
		import org.springframework.jdbc.core.JdbcTemplate;
		import org.springframework.jdbc.core.ResultSetExtractor;
		import org.springframework.stereotype.Repository;

		import java.sql.ResultSet;
		import java.sql.SQLException;
		import java.util.ArrayList;
		import java.util.List;

@Repository
public class BanListRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Card> getForbiddenCards(String date)
	{
		return jdbcTemplate.query("SELECT card_name, monster_type, card_color, card_effect FROM card_colors, cards, ban_lists WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number AND ban_lists.ban_status = 'Forbidden' AND ban_list_date = '" + date + "' ORDER BY card_color, card_name;", new ResultSetExtractor<List<Card>>()
		{
			@Override
			public List<Card> extractData(ResultSet row) throws SQLException, DataAccessException {
				List<Card> cardList = new ArrayList<>();
				while(row.next())
				{
					cardList.add(new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4)));
				}
				return cardList;
			}
		});
	}

	public List<Card> getLimitedCards(String date)
	{
		return jdbcTemplate.query("SELECT card_name, monster_type, card_color, card_effect FROM card_colors, cards, ban_lists WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number AND ban_lists.ban_status = 'Limited' AND ban_list_date = '" + date + "' ORDER BY card_color, card_name;", new ResultSetExtractor<List<Card>>()
		{
			@Override
			public List<Card> extractData(ResultSet row) throws SQLException, DataAccessException {
				List<Card> cardList = new ArrayList<>();
				while(row.next())
				{
					cardList.add(new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4)));
				}
				return cardList;
			}
		});
	}

	public List<Card> getSemiLimitedCards(String date)
	{
		return jdbcTemplate.query("SELECT card_name, monster_type, card_color, card_effect FROM card_colors, cards, ban_lists WHERE card_colors.color_id = cards.color_id AND cards.card_number = ban_lists.card_number AND ban_lists.ban_status = 'Semi-Limited' AND ban_list_date = '" + date + "' ORDER BY card_color, card_name;", new ResultSetExtractor<List<Card>>()
		{
			@Override
			public List<Card> extractData(ResultSet row) throws SQLException, DataAccessException {
				List<Card> cardList = new ArrayList<>();
				while(row.next())
				{
					cardList.add(new Card(row.getString(1), row.getString(2), row.getString(3), row.getString(4)));
				}
				return cardList;
			}
		});
	}

	public List<String> getBanListDates() {
		//SELECT DISTINCT ban_list_date from ban_lists;
		return jdbcTemplate.query("SELECT DISTINCT ban_list_date from ban_lists;",
				new ResultSetExtractor<List<String>>() {
					@Override
					public List<String> extractData(ResultSet row) throws SQLException, DataAccessException {
						List<String> banListDates = new ArrayList<>();
						while (row.next()) {
							banListDates.add(row.getDate(1).toString());
						}
						return banListDates;
					}
				});
	}
}
