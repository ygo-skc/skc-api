package com.rtomyj.skc.browse.card.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.browse.card.model.Card
import com.rtomyj.skc.browse.card.model.CardBrowseCriteria
import com.rtomyj.skc.browse.card.model.CardBrowseResults
import com.rtomyj.skc.browse.card.model.MonsterAssociation
import com.rtomyj.skc.util.constant.DBQueryConstants
import com.rtomyj.skc.util.enumeration.MonsterAssociationType
import com.rtomyj.skc.util.enumeration.table.definitions.BrowseQueryDefinition
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch
import java.sql.ResultSet

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class CardBrowseJDBCDao @Autowired constructor(
    val jdbcNamedTemplate: NamedParameterJdbcTemplate,
    val objectMapper: ObjectMapper
) : CardBrowseDao {
    companion object {
        private const val UNIQUE_CARD_ATTRIBUTES = "SELECT DISTINCT card_attribute FROM cards WHERE card_attribute NOT IN ('Spell', 'Trap', '?', '') ORDER BY card_attribute"

        private const val UNIQUE_MONSTER_TYPES = "SELECT DISTINCT TRIM(SUBSTRING_INDEX(monster_type, '/', 1)) AS monster_types FROM cards WHERE monster_type IS NOT NULL AND monster_type != '?' ORDER BY monster_types"
        private const val UNIQUE_MONSTER_SUB_TYPES = "SELECT DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(monster_type, '/', 2), '/', -1) AS monster_sub_types FROM cards WHERE monster_type IS NOT NULL AND monster_type != '?' ORDER BY monster_sub_types"

        private const val UNIQUE_LEVEL_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.level') AS DOUBLE) AS level FROM cards WHERE monster_association LIKE '%level%' ORDER BY level"
        private const val UNIQUE_RANK_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.rank') AS DOUBLE) AS `rank` FROM cards WHERE monster_association LIKE '%rank%' ORDER BY `rank`;"
        private const val UNIQUE_LINK_VALUES_QUERY = "SELECT DISTINCT CAST(JSON_EXTRACT(monster_association, '$.linkRating') AS DOUBLE) AS linkRating FROM cards WHERE monster_association LIKE '%linkRating%' ORDER BY linkRating"

        private val log = LoggerFactory.getLogger(this::class.java.name)
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