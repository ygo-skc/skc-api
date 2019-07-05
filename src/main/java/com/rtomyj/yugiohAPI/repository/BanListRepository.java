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

    public List<Card> getBanList()
    {
        return jdbcTemplate.query("select card_name, monster_type, card_color, card_effect from card_colors, cards where card_colors.color_id = cards.color_id", new ResultSetExtractor<List<Card>>()
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
}
