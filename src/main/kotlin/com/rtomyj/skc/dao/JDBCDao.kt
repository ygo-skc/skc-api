package com.rtomyj.skc.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.util.constant.DBQueryConstants
import com.rtomyj.skc.util.constant.ErrConstants
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

/**
 * JDBC implementation of DB DAO interface.
 */
@Repository
@Qualifier("jdbc")
class JDBCDao @Autowired constructor(
  val jdbcNamedTemplate: NamedParameterJdbcTemplate,
  val objectMapper: ObjectMapper
) : Dao {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java.name)
  }


  @Throws(SKCException::class)
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
      ?: throw SKCException(
        String.format(ErrConstants.CARD_ID_REQUESTED_NOT_FOUND_IN_DB, cardID),
        ErrorType.DB001
      )
  }
}