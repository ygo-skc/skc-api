package com.rtomyj.skc.dao.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.constant.DBQueryConstants
import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.dao.Dao
import com.rtomyj.skc.enums.MonsterAssociationType
import com.rtomyj.skc.enums.table.definitions.BrowseQueryDefinition
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.DownstreamStatus
import com.rtomyj.skc.model.banlist.CardBanListStatus
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.model.card.CardBrowseCriteria
import com.rtomyj.skc.model.card.CardBrowseResults
import com.rtomyj.skc.model.card.MonsterAssociation
import com.rtomyj.skc.model.stats.DatabaseStats
import com.rtomyj.skc.model.stats.MonsterTypeStats
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch
import java.sql.ResultSet
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class JDBCDao @Autowired constructor(
    val jdbcNamedTemplate: NamedParameterJdbcTemplate,
    @Qualifier("dbSimpleDateFormat") val dateFormat: SimpleDateFormat,
    val objectMapper: ObjectMapper
) : Dao {
    companion object {
        private const val VERSION_QUERY = "select version() as version"

        private const val UNIQUE_CARD_ATTRIBUTES = "SELECT DISTINCT card_attribute FROM cards WHERE card_attribute NOT IN ('Spell', 'Trap', '?', '') ORDER BY card_attribute"

        private const val UNIQUE_MONSTER_TYPES = "SELECT DISTINCT TRIM(SUBSTRING_INDEX(monster_type, '/', 1)) AS monster_types FROM cards WHERE monster_type IS NOT NULL AND monster_type != '?' ORDER BY monster_types"
        private const val UNIQUE_MONSTER_SUB_TYPES = "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(monster_type, '/', 2), '/', -1) AS monster_sub_types FROM cards WHERE monster_type IS NOT NULL AND monster_type != '?' ORDER BY monster_sub_types"

        private const val UNIQUE_LEVEL_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.level') AS DOUBLE) AS level FROM cards WHERE monster_association LIKE '%level%' ORDER BY level"
        private const val UNIQUE_RANK_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.rank') AS DOUBLE) AS `rank` FROM cards WHERE monster_association LIKE '%rank%' ORDER BY `rank`;"
        private const val UNIQUE_LINK_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.linkRating') AS DOUBLE) AS linkRating FROM cards WHERE monster_association LIKE '%linkRating%' ORDER BY linkRating"

        private val log = LoggerFactory.getLogger(this::class.java.name)
    }


    @Throws(YgoException::class)
    override fun dbConnection(): DownstreamStatus {
        val dbName = "SKC DB"
        var versionMajor = "---"
        var status = "down"
        var downstreamStatus: DownstreamStatus

        try {
            val version =
                jdbcNamedTemplate.queryForObject(VERSION_QUERY, MapSqlParameterSource(), String::class.java)
            status = "up"
            assert(version != null)
            val versionStringTokens = version!!.split("-").toTypedArray()
            versionMajor = if (versionStringTokens.isNotEmpty()) versionStringTokens[0] else "---"
            downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
        } catch (e: DataAccessException) {
            log.error("Could not get version of the DB. Exception occurred: {}", e.toString())
            downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
        } catch (e: AssertionError) {
            log.error("Could not get version of the DB. Exception occurred: {}", e.toString())
            downstreamStatus = DownstreamStatus(dbName, versionMajor, status)
        }
        return downstreamStatus
    }


    @Throws(YgoException::class)
    override fun getCardInfo(cardID: String): Card {
        val query = DBQueryConstants.GET_CARD_BY_ID
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardId", cardID)
        log.debug("Fetching card info from DB using query: ( {} ) with sql params ( {} ).", query, sqlParams)
        return jdbcNamedTemplate.query<Card?>(query, sqlParams) { row: ResultSet ->
            if (row.next()) {
                return@query Card(
                    cardID,
                    row.getString(2),
                    row.getString(1),
                    row.getString(3),
                    row.getString(4)
                )
                    .apply {
                        monsterType = row.getString(5)
                        monsterAttack = row.getObject(6, Int::class.javaObjectType)
                        monsterDefense = row.getObject(7, Int::class.javaObjectType)
                        monsterAssociation = MonsterAssociation.parseDBString(row.getString(8), objectMapper)
                    }
            }
            null
        }
            ?: throw YgoException(
                String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, cardID),
                ErrorType.D001
            )
    }

    override fun searchForCardWithCriteria(
        cardId: String?,
        cardName: String?,
        cardAttribute: String?,
        cardColor: String?,
        monsterType: String?,
        limit: Int,
        getBanInfo: Boolean
    ): List<Card> {
        return if (getBanInfo) searchForCardsIncludeBanInfo(
            cardId,
            cardName,
            cardAttribute,
            cardColor,
            monsterType,
            limit
        ) else searchForCards(cardId, cardName, cardAttribute, cardColor, monsterType, limit)
    }

    private fun prepSearchParams(
        cardId: String?,
        cardName: String?,
        cardAttribute: String?,
        cardColor: String?,
        monsterType: String?,
        limit: Int,
        sqlParams: MapSqlParameterSource
    ) {
        sqlParams.addValue("cardId", "%$cardId%")
        sqlParams.addValue("cardName", "%$cardName%")
        sqlParams.addValue("cardAttribute", if (cardAttribute!!.isEmpty()) ".*" else cardAttribute)
        sqlParams.addValue("cardColor", if (cardColor!!.isEmpty()) ".*" else cardColor)
        sqlParams.addValue("monsterType", if (monsterType!!.isEmpty()) ".*" else monsterType)
        sqlParams.addValue("limit", limit)
    }

    private fun fullTextQueryTransformer(oldQuery: String): String {
        val newQuery = oldQuery
            .replace("-", " ")
            .trim()
            .replace(" ", " +")
        return "+$newQuery*"
    }

    private fun searchForCards(
        cardId: String?,
        cardName: String?,
        cardAttribute: String?,
        cardColor: String?,
        monsterType: String?,
        limit: Int
    ): List<Card> {
        val stopwatch = StopWatch()
        stopwatch.start()
        val sqlParams = MapSqlParameterSource()
        val query = DBQueryConstants.SEARCH_QUERY
        prepSearchParams(cardId, fullTextQueryTransformer(cardName!!), cardAttribute, cardColor, monsterType, limit, sqlParams)
        log.debug(
            "Fetching card search results from DB using query: ( {} ) with sql params ( {} ).",
            query,
            sqlParams
        )
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
                                    this.monsterAttack = row.getObject(7, Int::class.java)
                                    this.monsterDefense = row.getObject(8, Int::class.java)
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

    private fun searchForCardsIncludeBanInfo(
        cardId: String?,
        cardName: String?,
        cardAttribute: String?,
        cardColor: String?,
        monsterType: String?,
        limit: Int
    ): List<Card> {
        val stopwatch = StopWatch()
        stopwatch.start()
        val sqlParams = MapSqlParameterSource()
        val query = DBQueryConstants.SEARCH_QUERY_WITH_BAN_INFO
        prepSearchParams(cardId, fullTextQueryTransformer(cardName!!), cardAttribute, cardColor, monsterType, limit, sqlParams)
        log.debug(
            "Fetching card search results from DB using query: ( {} ) with sql params ( {} ).",
            query,
            sqlParams
        )
        val searchResults = ArrayList<Card>(
            Objects.requireNonNull(
                jdbcNamedTemplate.query<Collection<Card?>>(query, sqlParams) { row: ResultSet ->
                    /*
                    Since a join between ban lists and card info is done, there will be multiple rows having the same card info (id, name, atk, etc) but with different ban info.
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
                            if (numUniqueCardsParsed == limit) break
                            card = Card(
                                row.getString(1),
                                row.getString(3),
                                row.getString(2),
                                row.getString(4),
                                row.getString(5)
                            )
                                .apply {
                                    this.monsterType = row.getString(6)
                                    this.monsterAttack = row.getObject(7, Int::class.java)
                                    this.monsterDefense = row.getObject(8, Int::class.java)
                                }
                            cardInfoTracker[card.cardID] = card
                        }
                        if (row.getString(9) != null) {
                            try {
                                val cardBanListStatus = CardBanListStatus(
                                    dateFormat.parse(row.getString(9)),
                                    cardId!!,
                                    row.getString(10)
                                )
                                card.restrictedIn?.add(cardBanListStatus)
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

    override fun getMonsterTypeStats(cardColor: String): MonsterTypeStats {
        val query =
            "SELECT monster_type, count(*) AS 'Total' FROM card_info WHERE monster_type IS NOT NULL AND card_color = :cardColor GROUP BY monster_type ORDER BY monster_type"
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardColor", cardColor)
        val monsterType = MonsterTypeStats(cardColor, HashMap())
        jdbcNamedTemplate.query<Any?>(query, sqlParams) { row: ResultSet, _: Int ->
            monsterType.monsterTypes[row.getString(1)] = row.getInt(2)
            null
        }
        return monsterType
    }

    override fun getDatabaseStats(): DatabaseStats {
        return jdbcNamedTemplate
            .queryForObject(
                DBQueryConstants.GET_DATABASE_TOTALS, MapSqlParameterSource()
            ) { row: ResultSet, _: Int ->
                DatabaseStats(
                    row.getInt(1),
                    row.getInt(2),
                    row.getInt(3),
                    row.getInt(4)
                )
            }!!
    }

    private fun transformCollectionToSQLOr(monsterAssociationValueSet: Collection<String>): String {
        var monsterAssociationStr = ""
        if (!monsterAssociationValueSet.isEmpty()) {
            monsterAssociationStr = java.lang.String.join("|", monsterAssociationValueSet)
        }
        return monsterAssociationStr
    }

    override fun getBrowseResults(
        criteria: CardBrowseCriteria,
        monsterLevelSet: Set<String>, monsterRankSet: Set<String>,
        monsterLinkRatingsSet: Set<String>
    ): CardBrowseResults {
        val stopWatch = StopWatch()
        stopWatch.start()
        val cardColorCriteria =
            if (criteria.cardColors.isEmpty()) ".*" else java.lang.String.join("|", criteria.cardColors)
        val attributeCriteria =
            if (criteria.attributes.isEmpty()) ".*" else java.lang.String.join("|", criteria.attributes)
        val monsterTypeCriteria =
            if (criteria.monsterTypes.isEmpty()) ".*" else "^" + java.lang.String.join("|", criteria.monsterTypes)
                .replace("?", "\\?")
        val monsterSubTypeCriteria = if (criteria.monsterSubTypes.isEmpty()) ".*" else ".+/" + java.lang.String.join(
            "|",
            criteria.monsterSubTypes
        ).replace("?", "\\?")
        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("cardColors", cardColorCriteria)
        sqlParams.addValue("attributes", attributeCriteria)
        sqlParams.addValue("monsterTypes", monsterTypeCriteria)
        sqlParams.addValue("monsterSubTypes", monsterSubTypeCriteria)

        /*
			Only use where clause for card level if there is a criteria specified by user.
			Unlike other criteria, using the REGEX .* will not work as it will clash with other monster association JSON fields in DB.
		 */
        val monsterAssociationWhereClause: String
        if (monsterLevelSet.isEmpty() && monsterRankSet.isEmpty() && monsterLinkRatingsSet.isEmpty()) {
            monsterAssociationWhereClause = ""
        } else {
            monsterAssociationWhereClause = " AND monster_association REGEXP :monsterAssociation "
            val levelCriteria = transformCollectionToSQLOr(monsterLevelSet)
            val rankCriteria = transformCollectionToSQLOr(monsterRankSet)
            val linkRatingCriteria = transformCollectionToSQLOr(monsterLinkRatingsSet)

            val monsterAssociationCriteriaList: MutableList<String?> =
                mutableListOf(levelCriteria, rankCriteria, linkRatingCriteria)
            monsterAssociationCriteriaList.removeAll(listOf(null, ""))

            val monsterAssociationCriteria = java.lang.String.join("+", monsterAssociationCriteriaList)
            sqlParams.addValue("monsterAssociation", monsterAssociationCriteria)
        }
        val sql = String.format(DBQueryConstants.GET_CARD_BROWSE_RESULTS, monsterAssociationWhereClause)
        log.debug(
            "Fetching card browse results from DB using query: ( {} ) with sql params ( {} ).",
            sql,
            sqlParams
        )
        val results = jdbcNamedTemplate.query(sql, sqlParams) { row: ResultSet, _: Int ->
            Card(
                row.getString(BrowseQueryDefinition.CARD_ID.toString()),
                row.getString(BrowseQueryDefinition.CARD_NAME.toString()),
                row.getString(BrowseQueryDefinition.CARD_COLOR.toString()),
                row.getString(BrowseQueryDefinition.CARD_ATTRIBUTE.toString()),
                row.getString(BrowseQueryDefinition.CARD_EFFECT.toString())
            )
                .apply {
                    this.monsterType = row.getString(BrowseQueryDefinition.MONSTER_TYPE.toString())
                    this.monsterAssociation = MonsterAssociation.parseDBString(
                        row.getString(BrowseQueryDefinition.MONSTER_ASSOCIATION.toString()), objectMapper
                    )
                }
        }
        val cardBrowseResults = CardBrowseResults(results, results.size)
        stopWatch.stop()
        log.debug("Time taken to retrieve card browse results: {}ms", stopWatch.totalTimeMillis)
        return cardBrowseResults
    }

    override fun getCardColors(): Set<String> {
        val stopWatch = StopWatch()
        stopWatch.start()
        val sql = "SELECT card_color FROM card_colors WHERE card_color != 'Token'"
        log.debug("Retrieving unique card color values from DB using query {}", sql)
        val cardColors: Set<String> = LinkedHashSet(
            jdbcNamedTemplate.query(sql) { row: ResultSet, _: Int -> row.getString(1) })
        stopWatch.stop()
        log.debug("Time taken to retrieve unique card color values: {}ms", stopWatch.totalTimeMillis)
        return cardColors
    }

    override fun getMonsterAttributes(): Set<String> = LinkedHashSet(jdbcNamedTemplate.query(UNIQUE_CARD_ATTRIBUTES) { row: ResultSet, _: Int -> row.getString(1) })


    override fun getMonsterTypes(): Set<String> = LinkedHashSet(jdbcNamedTemplate.query(UNIQUE_MONSTER_TYPES) { row: ResultSet, _: Int -> row.getString(1) })


    override fun getMonsterSubTypes(): Set<String> {
        val monsterSubTypes: MutableSet<String> = LinkedHashSet(
            jdbcNamedTemplate.query(UNIQUE_MONSTER_SUB_TYPES) { row: ResultSet, _: Int ->
                row.getString(1).split("/").toTypedArray()[0]
            })

        val cardColors = getCardColors()
        monsterSubTypes.removeAll(cardColors)
        monsterSubTypes.remove("Pendulum") // removing pendulum individually as pendulum monster color/name is categorized by cards other color: e.g.  Pendulum-Normal, Pendulum-Fusion, etc
        return monsterSubTypes
    }


    override fun getMonsterAssociationField(monsterAssociationType: MonsterAssociationType): Set<Int> {
        val stopWatch = StopWatch()
        stopWatch.start()

        val sql = when(monsterAssociationType) {
            MonsterAssociationType.LEVEL -> UNIQUE_LEVEL_VALUES_QUERY
            MonsterAssociationType.RANK -> UNIQUE_RANK_VALUES_QUERY
            MonsterAssociationType.LINK -> UNIQUE_LINK_VALUES_QUERY
        }

        val result: Set<Int> = HashSet(
            jdbcNamedTemplate.query(sql) { row: ResultSet, _: Int ->
                row.getInt(1)
            })

        stopWatch.stop()
        log.debug(
            "Time taken to retrieve unique {} from DB was: {}ms",
            monsterAssociationType,
            stopWatch.totalTimeMillis
        )

        return result
    }
}