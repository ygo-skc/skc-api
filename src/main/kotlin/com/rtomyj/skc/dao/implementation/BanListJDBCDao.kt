package com.rtomyj.skc.dao.implementation

import com.rtomyj.skc.constant.DBQueryConstants
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.enums.BanListCardStatus
import com.rtomyj.skc.model.banlist.BanListDates
import com.rtomyj.skc.model.banlist.CardBanListStatus
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus
import com.rtomyj.skc.model.card.Card
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch
import java.sql.ResultSet
import java.text.ParseException
import java.text.SimpleDateFormat

@Repository
@Qualifier("ban-list-jdbc")
@Slf4j
class BanListJDBCDao @Autowired constructor(
    private val jdbcNamedTemplate: NamedParameterJdbcTemplate,
    @Qualifier("dbSimpleDateFormat") private val dateFormat: SimpleDateFormat,
) : BanListDao {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        private const val UNSUPPORTED_OPERATION_MESSAGE = "HibernateDao not able to execute method."
    }


    override fun getBanListByBanStatus(date: String, status: BanListCardStatus): List<Card> {
        val query = DBQueryConstants.GET_BAN_LIST_BY_STATUS

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("date", date)
        sqlParams.addValue("status", status.toString())

        return jdbcNamedTemplate.query<List<Card>>(query, sqlParams) { row: ResultSet ->
            val cardList = ArrayList<Card>()
            while (row.next()) {
                cardList.add(
                    Card(
                        row.getString(5),
                        row.getString(1),
                        row.getString(3),
                        row.getString(6),
                        row.getString(4)

                    )
                        .apply {
                            monsterType = row.getString(2)
                        }
                )
            }
            cardList
        }!!
    }


    override fun numberOfBanLists(): Int {
        val query = "SELECT COUNT(DISTINCT ban_list_date) AS 'Total Ban Lists' FROM ban_lists"

        return jdbcNamedTemplate.query<Int>(query) { row: ResultSet ->
            if (row.next()) {
                row.getInt(1)
            }
            null
        } ?: return 0
    }


    override fun banListDatesInOrder(): List<String> {
        val query = "select distinct ban_list_date from ban_lists order by ban_list_date"

        return jdbcNamedTemplate.queryForList(query, MapSqlParameterSource(), String::class.java)
    }


    override fun getPreviousBanListDate(currentBanList: String): String {
        val sortedBanListDates = banListDatesInOrder()
        val currentBanListPosition = sortedBanListDates.indexOf(currentBanList)

        if (currentBanListPosition == 0) {
            return ""
        }

        val previousBanListPosition = currentBanListPosition - 1
        return sortedBanListDates[previousBanListPosition]
    }


    // TODO: make sure you write a test for the instance where the last ban list is selected
    override fun getRemovedContentOfBanList(banListDate: String): List<CardsPreviousBanListStatus> {
        val oldBanList = getPreviousBanListDate(banListDate)
        if (oldBanList == "") {
            return emptyList()
        }

        val query = "select removed_cards.card_number, removed_cards.ban_status, cards.card_name" +
                " from (select old_list.card_number, old_list.ban_status from (select card_number from ban_lists" +
                " where ban_list_date = :newBanList) as new_list right join (select card_number, ban_status" +
                " from ban_lists where ban_list_date = :oldBanList) as old_list on new_list.card_number = old_list.card_number" +
                " where new_list.card_number is NULL) as removed_cards, cards where cards.card_number = removed_cards.card_number" +
                " ORDER BY cards.card_name"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("newBanList", banListDate)
        sqlParams.addValue("oldBanList", oldBanList)

        return jdbcNamedTemplate.query<List<CardsPreviousBanListStatus>>(query, sqlParams) { row: ResultSet ->
            val removedCards = ArrayList<CardsPreviousBanListStatus>()

            while (row.next()) {
                val removedCard = CardsPreviousBanListStatus(
                    row.getString(1),
                    row.getString(3),
                    row.getString(2)


                )
                removedCards.add(removedCard)
            }
            return@query removedCards
        }!!
    }


    override fun getCardBanListStatusByDate(cardId: String, banListDate: String): String {
        val query = "select ban_status from ban_lists where card_number = :cardId and ban_list_date = :banListDate"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardId", cardId)
        sqlParams.addValue("banListDate", banListDate)

        return jdbcNamedTemplate.query<String>(query, sqlParams) { row: ResultSet ->
            if (row.next()) return@query row.getString(1)
            return@query null
        } ?: "Unlimited"
    }


    override fun isValidBanList(banListDate: String): Boolean {
        val query = "select distinct ban_list_date from ban_lists where ban_list_date = :banListDate"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("banListDate", banListDate)

        val results = listOf<Any>(jdbcNamedTemplate.queryForList(query, sqlParams, Any::class.java))
        return results.isNotEmpty()
    }

    // TODO: make sure you write a test for the instance where the last ban list is selected
    override fun getNewContentOfBanList(
        banListDate: String,
        status: BanListCardStatus
    ): List<CardsPreviousBanListStatus> {
        val stopwatch = StopWatch()
        stopwatch.start()

        val oldBanList = getPreviousBanListDate(banListDate)
        if (oldBanList == "") {
            return emptyList()
        }

        val query = "select new_cards.card_number, cards.card_name from (select new_list.card_number" +
                " from (select card_number from ban_lists where ban_list_date = :newBanList and ban_status = :status)" +
                " as new_list left join (select card_number from ban_lists where ban_list_date = :oldBanList" +
                " and ban_status = :status) as old_list on new_list.card_number = old_list.card_number" +
                " where old_list.card_number is NULL) as new_cards, cards where cards.card_number = new_cards.card_number" +
                " ORDER BY cards.card_name"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("status", status.toString())
        sqlParams.addValue("newBanList", banListDate)
        sqlParams.addValue("oldBanList", oldBanList)

        log.debug(
            "Fetching new {} cards in ban list from DB using query ({}) with sql params ({}).",
            status,
            query,
            sqlParams
        )
        val newCardList =
            jdbcNamedTemplate.query<List<CardsPreviousBanListStatus>>(query, sqlParams) { row: ResultSet ->
                val newCards: MutableList<CardsPreviousBanListStatus> = ArrayList()
                while (row.next()) {
                    val cardID = row.getString(1)
                    val previousStatus = getCardBanListStatusByDate(cardID, oldBanList)
                    val cardsPreviousBanListStatus = CardsPreviousBanListStatus(
                        cardID,
                        row.getString(2),
                        previousStatus
                    )

                    newCards.add(cardsPreviousBanListStatus)
                }
                newCards
            }!!

        stopwatch.stop()
        log.debug("Time taken to fetch new {} cards ({}ms)", status, stopwatch.totalTimeMillis)

        return newCardList
    }


    override fun getBanListDates(): BanListDates {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }


    override fun getBanListDetailsForCard(cardId: String): List<CardBanListStatus> {
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardId", cardId)

        return jdbcNamedTemplate.query(
            DBQueryConstants.GET_BAN_LIST_INFO_FOR_CARD,
            sqlParams
        ) { row: ResultSet, _: Int ->
            try {
                return@query CardBanListStatus(dateFormat.parse(row.getString(1)), cardId, row.getString(2))
            } catch (e: ParseException) {
                log.error(
                    "Cannot parse date from DB when retrieving ban list info for card {} with exception: {}",
                    cardId,
                    e.toString()
                )
                return@query null
            }
        }
    }
}