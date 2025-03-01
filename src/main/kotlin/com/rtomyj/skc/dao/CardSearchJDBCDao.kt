package com.rtomyj.skc.dao

import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardBanListStatus
import com.rtomyj.skc.model.CardSearchParameters
import com.rtomyj.skc.util.constant.DBQueryConstants
import com.rtomyj.skc.util.enumeration.BanListFormat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch
import java.sql.ResultSet
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class CardSearchJDBCDao @Autowired constructor(
  val jdbcNamedTemplate: NamedParameterJdbcTemplate,
  @Qualifier("dbDateTimeFormatter") val dbDateFormatter: DateTimeFormatter,
) : CardSearchDao {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)

    @JvmStatic
    private fun prepSearchParams(cardSearchParameters: CardSearchParameters): MapSqlParameterSource {
      val sqlParams = MapSqlParameterSource()
      sqlParams.addValue("cardId", if (cardSearchParameters.cId.isEmpty()) "" else "%${cardSearchParameters.cId}%")
      sqlParams.addValue("cardName", fullTextQueryTransformer(cardSearchParameters.cName))
      sqlParams.addValue("cardAttribute", cardSearchParameters.cAttribute)
      sqlParams.addValue("cardColor", cardSearchParameters.cColor)
      sqlParams.addValue("monsterType", cardSearchParameters.mType)
      sqlParams.addValue("limit", cardSearchParameters.limit)

      return sqlParams
    }

    @JvmStatic
    private fun fullTextQueryTransformer(query: String): String {
      return if (query.isNotBlank()) "+$query"
          .replace("-", " ")
          .replace(Regex("\\s+"), " ")
          .trim()
          .replace(" ", "* +") + "*" else query
    }
  }

  override fun searchForCardWithCriteria(cardSearchParameters: CardSearchParameters, getBanInfo: Boolean): List<Card> {
    return if (getBanInfo) searchForCardsIncludeBanInfo(cardSearchParameters) else searchForCards(cardSearchParameters)
  }

  private fun searchForCards(cardSearchParameters: CardSearchParameters): List<Card> {
    val sqlParams =
      prepSearchParams(cardSearchParameters)
    val query = DBQueryConstants.SEARCH_QUERY
    log.debug(
      "Fetching card search results from DB using query: ( {} ) with sql params ( {} ).",
      query,
      sqlParams
    )

    val stopwatch = StopWatch()
    stopwatch.start()
    val searchResults = ArrayList<Card>(
      Objects.requireNonNull(
        jdbcNamedTemplate.query<Collection<Card?>>(query, sqlParams) { row: ResultSet ->
          val cardInfoTracker: MutableMap<String, Card?> = LinkedHashMap()
          while (row.next()) {
            var card = cardInfoTracker[row.getString(1)]
            if (card == null) {
              card = Card(
                row.getString(1),
                row.getString(3),
                row.getString(2),
                row.getString(4),
                row.getString(5)
              )
                  .apply {
                    this.monsterType = row.getString(6)

                    val atk = row.getInt(7)
                    this.monsterAttack = if (row.wasNull()) null else atk
                    val def = row.getInt(8)
                    this.monsterDefense = if (row.wasNull()) null else def
                  }
              cardInfoTracker[card.cardID] = card
            }
          }
          cardInfoTracker.values
        })
    )
    stopwatch.stop()
    log.debug("Time taken to fetch search results from DB: {}ms", stopwatch.totalTimeMillis)
    return searchResults
  }

  private fun searchForCardsIncludeBanInfo(cardSearchParameters: CardSearchParameters): List<Card> {
    val sqlParams =
      prepSearchParams(cardSearchParameters)
    val query = DBQueryConstants.SEARCH_QUERY_WITH_BAN_INFO
    log.debug(
      "Fetching card search results from DB using query: ( {} ) with sql params ( {} ).",
      query,
      sqlParams
    )

    val stopwatch = StopWatch()
    stopwatch.start()
    val searchResults = ArrayList<Card>(
      Objects.requireNonNull(
        jdbcNamedTemplate.query<Collection<Card?>>(query, sqlParams) { row: ResultSet ->
          /*
          Since a join between ban lists and card info is done, there will be multiple rows having the same card info (id, name, atk, etc.) but with different ban info.
          ie:	ID		Name		BanList
              1234	Stratos	2019-07-15
              1234	Stratos	2019-04-29
          To prevent this, the map will use the cardId (unique) to map to a Card object containing info already gathered from previous rows.
          An array within the Card object will then be used to keep track of all the ban lists the card was a part of. The array will be updated
          every time a new row has new ban list info of a card already in the map.
          */
          val cardInfoTracker: MutableMap<String, Card?> = LinkedHashMap()
          var numUniqueCardsParsed = 0
          while (row.next()) {
            var card = cardInfoTracker[row.getString(1)]

            if (card == null) {
              if (numUniqueCardsParsed == cardSearchParameters.limit) break
              card = Card(
                row.getString(1),
                row.getString(3),
                row.getString(2),
                row.getString(4),
                row.getString(5)
              )
                  .apply {
                    this.monsterType = row.getString(6)

                    val atk = row.getInt(7)
                    this.monsterAttack = if (row.wasNull()) null else atk
                    val def = row.getInt(8)
                    this.monsterDefense = if (row.wasNull()) null else def
                  }
              cardInfoTracker[card.cardID] = card

              card.restrictedIn = mapOf() // init map for ban lists
            }
            if (row.getString(9) != null) {
              val format = BanListFormat.valueOf(row.getString(11))

              try {
                val cardBanListStatus = CardBanListStatus(
                  LocalDate.parse(row.getString(9), dbDateFormatter),
                  cardSearchParameters.cId,
                  row.getString(10),
                  format
                )

                card.restrictedIn
                    ?.getOrDefault(format, mutableListOf())
                    ?.add(cardBanListStatus)
              } catch (e: ParseException) {
                log.error(
                  "Error occurred while parsing date for ban list, date: {}.",
                  row.getString(9)
                )
              }
            }
            numUniqueCardsParsed++
          }
          cardInfoTracker.values
        })
    )
    stopwatch.stop()
    log.debug("Time taken to fetch search results from DB: {}ms", stopwatch.totalTimeMillis)
    return searchResults
  }
}