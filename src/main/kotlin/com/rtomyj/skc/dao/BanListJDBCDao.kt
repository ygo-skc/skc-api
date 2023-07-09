package com.rtomyj.skc.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.model.*
import com.rtomyj.skc.util.constant.DBQueryConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import com.rtomyj.skc.util.enumeration.BanListFormat
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
class BanListJDBCDao @Autowired constructor(
    private val jdbcNamedTemplate: NamedParameterJdbcTemplate,
    @Qualifier("dbSimpleDateFormat") private val dateFormat: SimpleDateFormat,
    val objectMapper: ObjectMapper
) : BanListDao {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        private const val UNSUPPORTED_OPERATION_MESSAGE = "BanListJDBCDao not able to execute method."
    }


    override fun getBanListByBanStatus(date: String, status: BanListCardStatus, format: String): List<Card> {
        val query = DBQueryConstants.GET_BAN_LIST_BY_STATUS

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("date", date)
        sqlParams.addValue("status", status.toString())
        sqlParams.addValue("format", format)

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
                            monsterAssociation = MonsterAssociation.parseDBString(row.getString(7), objectMapper)
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


    override fun banListDatesInOrder(format: String): List<String> {
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("format", format)

        val query = "select distinct ban_list_date from ban_lists WHERE duel_format = :format order by ban_list_date"

        return jdbcNamedTemplate.queryForList(query, sqlParams, String::class.java)
    }


    override fun getPreviousBanListDate(currentBanList: String, format: String): String {
        val sortedBanListDates = banListDatesInOrder(format)
        val currentBanListPosition = sortedBanListDates.indexOf(currentBanList)

        if (currentBanListPosition == 0) {
            return ""
        }

        val previousBanListPosition = currentBanListPosition - 1
        return sortedBanListDates[previousBanListPosition]
    }

    override fun getRemovedContentOfBanList(
        banListDate: String,
        previousBanListDate: String,
        format: String
    ): List<CardsPreviousBanListStatus> {
        if (previousBanListDate == "") {
            return emptyList()
        }

        val query = "select" +
                " old_list.card_name, old_list.monster_type, old_list.card_color, old_list.card_effect, old_list.card_number, old_list.card_attribute, old_list.monster_association, old_list.ban_status" +
                " from (select card_number from ban_list_info where ban_list_date = :newBanList) as new_list" +
                " right join" +
                " (select * from ban_list_info where ban_list_date = :oldBanList) as old_list" +
                " on new_list.card_number = old_list.card_number where new_list.card_number is NULL " +
                "AND duel_format = :format " +
                "ORDER BY color_id, card_name"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("format", format)
        sqlParams.addValue("newBanList", banListDate)
        sqlParams.addValue("oldBanList", previousBanListDate)

        return jdbcNamedTemplate.query<List<CardsPreviousBanListStatus>>(query, sqlParams) { row: ResultSet ->
            val removedCards = ArrayList<CardsPreviousBanListStatus>()

            while (row.next()) {
                val removedCard = CardsPreviousBanListStatus(
                    Card(
                        row.getString(5),
                        row.getString(1),
                        row.getString(3),
                        row.getString(6),
                        row.getString(4)
                    )
                        .apply {
                            monsterType = row.getString(2)
                            monsterAssociation = MonsterAssociation.parseDBString(row.getString(7), objectMapper)
                        },
                    row.getString(8)
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

    override fun getNewContentOfBanList(
        banListDate: String,
        previousBanListDate: String,
        status: BanListCardStatus,
        format: String
    ): List<CardsPreviousBanListStatus> {
        val stopwatch = StopWatch()
        stopwatch.start()

        if (previousBanListDate == "") {
            return emptyList()
        }

        val query =
            "select card_name, monster_type, card_color, card_effect, new_list.card_number, card_attribute, monster_association " +
                    "from (select * from ban_list_info where ban_list_date = :newBanList and ban_status = :status) as new_list " +
                    "left join " +
                    "(select card_number, ban_status from ban_list_info where ban_list_date = :oldBanList and ban_status = :status) as old_list " +
                    "on new_list.card_number = old_list.card_number " +
                    "where old_list.card_number is NULL " +
                    "AND duel_format = :format " +
                    "ORDER BY color_id, card_name"

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("status", status.toString())
        sqlParams.addValue("format", format)
        sqlParams.addValue("newBanList", banListDate)
        sqlParams.addValue("oldBanList", previousBanListDate)

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
                    val cardID = row.getString(5)
                    val previousStatus = getCardBanListStatusByDate(cardID, previousBanListDate)
                    val cardsPreviousBanListStatus = CardsPreviousBanListStatus(
                        Card(
                            row.getString(5),
                            row.getString(1),
                            row.getString(3),
                            row.getString(6),
                            row.getString(4)
                        )
                            .apply {
                                monsterType = row.getString(2)
                                monsterAssociation = MonsterAssociation.parseDBString(row.getString(7), objectMapper)
                            },
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


    override fun getBanListDates(format: String): BanListDates {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }


    override fun getBanListDetailsForCard(cardId: String, format: BanListFormat): List<CardBanListStatus> {
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardId", cardId)
        sqlParams.addValue("format", format.toString())

        return jdbcNamedTemplate.query(
            DBQueryConstants.GET_BAN_LIST_INFO_FOR_CARD,
            sqlParams
        ) { row: ResultSet, _: Int ->
            try {
                return@query CardBanListStatus(dateFormat.parse(row.getString(1)), cardId, row.getString(2), format)
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