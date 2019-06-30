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
        return jdbcTemplate.query("select card_name, rarity, quantity from Collection where rarity = 'Super Rare' order by card_name", new ResultSetExtractor<List<Card>>()
        {
            @Override
            public List<Card> extractData(ResultSet row) throws SQLException, DataAccessException {
                List<Card> cardList = new ArrayList<>();
                while(row.next())
                {
                    cardList.add(new Card(row.getString(1), row.getString(2), row.getInt(3)));
                }
                return cardList;
            }
        });
    }
}
