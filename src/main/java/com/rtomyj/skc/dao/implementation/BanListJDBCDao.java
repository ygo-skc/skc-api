package com.rtomyj.skc.dao.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.constant.DBQueryConstants;
import com.rtomyj.skc.dao.BanListDao;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.model.banlist.BanListDates;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.skc.model.card.Card;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Qualifier("ban-list-jdbc")
@Slf4j
public class BanListJDBCDao implements BanListDao {
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    private final SimpleDateFormat dateFormat;

    @Autowired
    public BanListJDBCDao(final NamedParameterJdbcTemplate jdbcNamedTemplate
            , @Qualifier("dbSimpleDateFormat") final SimpleDateFormat dateFormat
            , final ObjectMapper objectMapper) {
        this.jdbcNamedTemplate = jdbcNamedTemplate;
        this.dateFormat = dateFormat;
    }


    @Override
    public List<Card> getBanListByBanStatus(@NonNull final String date, @NonNull final Dao.Status status) {
        final String query = DBQueryConstants.GET_BAN_LIST_BY_STATUS;

        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("date", date);
        sqlParams.addValue("status", status.toString());


        return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
            final List<Card> cardList = new ArrayList<>();
            while (row.next()) {
                cardList.add(
                        Card
                                .builder()
                                .cardName(row.getString(1))
                                .monsterType(row.getString(2))
                                .cardColor(row.getString(3))
                                .cardEffect(row.getString(4))
                                .cardID(row.getString(5))
                                .build()
                );
            }
            return cardList;
        });
    }


    public int numberOfBanLists() {
        String query = "SELECT COUNT(DISTINCT ban_list_date) AS 'Total Ban Lists' FROM ban_lists";
        final Integer numBanLists = jdbcNamedTemplate.query(query, (ResultSet row) ->  {
            if (row.next())	return row.getInt(1);

            return null;
        });

        if (numBanLists == null)	return 0;
        return numBanLists;
    }


    public List<String> banListDatesInOrder() {
        final String query = "select distinct ban_list_date from ban_lists order by ban_list_date";

        return jdbcNamedTemplate.queryForList(query, (SqlParameterSource) null, String.class);
    }


    public String getPreviousBanListDate(String currentBanList) {
        final List<String> sortedBanListDates = this.banListDatesInOrder();
        final int currentBanListPosition = sortedBanListDates.indexOf(currentBanList);

        if (currentBanListPosition == 0)	return "";
        final int previousBanListPosition = currentBanListPosition - 1;

        return sortedBanListDates.get(previousBanListPosition);
    }


    // TODO: make sure you write a test for the instance where the last ban list is selected
    public List<CardsPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
    {
        String oldBanList = this.getPreviousBanListDate(newBanList);
        if (oldBanList.equals(""))	return new ArrayList<>();

        String query = "select removed_cards.card_number, removed_cards.ban_status, cards.card_name" +
                " from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists" +
                " where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status" +
                " from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number" +
                " where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number" +
                " ORDER BY cards.card_name";

        MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("newBanList", newBanList);
        sqlParams.addValue("oldBanList", oldBanList);


        return jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
            final List<CardsPreviousBanListStatus> removedCards = new ArrayList<>();

            while(row.next())
            {
                final CardsPreviousBanListStatus removedCard = new CardsPreviousBanListStatus();

                removedCard.setCardId(row.getString(1));
                removedCard.setPreviousBanStatus(row.getString(2));
                removedCard.setCardName(row.getString(3));

                removedCards.add(removedCard);
            }

            return removedCards;
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


    public boolean isValidBanList(final String banListDate)
    {

        final String query = "select distinct ban_list_date from ban_lists where ban_list_date = :banListDate";

        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("banListDate", banListDate);


        final List<Object> results = Collections.singletonList(jdbcNamedTemplate.queryForList(query, sqlParams, Object.class));
        return !results.isEmpty();

    }


    // TODO: make sure you write a test for the instance where the last ban list is selected
    public List<CardsPreviousBanListStatus> getNewContentOfBanList(final String newBanList, final Dao.Status status)
    {
        final StopWatch stopwatch = new StopWatch();
        stopwatch.start();


        String oldBanList = this.getPreviousBanListDate(newBanList);
        if (oldBanList.equals(""))	return new ArrayList<>();

        String query = "select new_cards.card_number, cards.card_name from (select new_list.card_number" +
                " from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)" +
                " as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList" +
                " and ban_status = :status) as old_list on new_list.card_number = old_list.card_number" +
                " where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number" +
                " ORDER BY cards.card_name";

        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("status", status.toString());
        sqlParams.addValue("newBanList", newBanList);
        sqlParams.addValue("oldBanList", oldBanList);

        log.debug("Fetching new {} cards in ban list from DB using query ({}) with sql params ({}).", status, query, sqlParams);

        final List<CardsPreviousBanListStatus> newCardList =  jdbcNamedTemplate.query(query, sqlParams, (ResultSet row) -> {
            final List<CardsPreviousBanListStatus> newCards = new ArrayList<>();

            while (row.next())
            {
                CardsPreviousBanListStatus cardsPreviousBanListStatus = new CardsPreviousBanListStatus();
                final String cardID = row.getString(1);
                String previousStatus = this.getCardBanListStatusByDate(cardID, oldBanList);
                previousStatus = ( previousStatus == null ) ? "Unlimited" : previousStatus;


                cardsPreviousBanListStatus.setCardId(cardID);
                cardsPreviousBanListStatus.setPreviousBanStatus(previousStatus);
                cardsPreviousBanListStatus.setCardName(row.getString(2));

                newCards.add(cardsPreviousBanListStatus);
            }

            return newCards;
        });

        stopwatch.stop();
        log.debug("Time taken to fetch new {} cards ({}ms)", status, stopwatch.getTotalTimeMillis());

        return newCardList;
    }


    @Override
    public BanListDates getBanListDates()
    {
        return null;
    }


    public List<CardBanListStatus> getBanListDetailsForCard(final String cardId) {
        final MapSqlParameterSource sqlParams = new MapSqlParameterSource();
        sqlParams.addValue("cardId", cardId);

        return jdbcNamedTemplate.query(DBQueryConstants.GET_BAN_LIST_INFO_FOR_CARD, sqlParams, (ResultSet row, int rowNum) -> {
            try {
                return CardBanListStatus
                        .builder()
                        .banListDate(dateFormat.parse(row.getString(1)))
                        .banStatus(row.getString(2))
                        .build();
            } catch (ParseException e) {
                log.error("Cannot parse date from DB when retrieving ban list info for card {} with exception: {}", cardId, e.toString());
                return null;
            }
        });
    }
}
