package com.rtomyj.skc.dao

import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.SKCException
import com.rtomyj.skc.model.BanListDate
import com.rtomyj.skc.model.BanListDates
import com.rtomyj.skc.model.BanListTable
import com.rtomyj.skc.model.Card
import com.rtomyj.skc.model.CardBanListStatus
import com.rtomyj.skc.model.CardsPreviousBanListStatus
import com.rtomyj.skc.util.constant.ErrConstants
import com.rtomyj.skc.util.enumeration.BanListCardStatus
import com.rtomyj.skc.util.enumeration.BanListFormat
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceException
import org.hibernate.SessionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch

/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("ban-list-hibernate")
class BanListHibernateDao @Autowired constructor(private var entityManagerFactory: EntityManagerFactory) : BanListDao {

  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private const val UNSUPPORTED_OPERATION_MESSAGE = "HibernateDao not able to execute method."
  }


  override fun isBanListValid(date: String, format: String): Boolean {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  @Throws(SKCException::class)
  override fun getBanListDates(format: String): BanListDates {
    val stopwatch = StopWatch()
    stopwatch.start()

    val dates: List<BanListDate> = try {
      entityManagerFactory
          .unwrap(SessionFactory::class.java)
          .openSession()
          .use { session ->
            val criteriaBuilder = entityManagerFactory.criteriaBuilder

            val criteriaQuery = criteriaBuilder.createQuery(BanListDate::class.java)
            val root = criteriaQuery.from(BanListTable::class.java)

            criteriaQuery
                .select(
                  criteriaBuilder
                      .construct(BanListDate::class.java, root.get<String>("banListDate"))
                )
                .where(criteriaBuilder.equal(root.get<String>("format"), format))
                .distinct(true)
                .orderBy(
                  criteriaBuilder
                      .desc(root.get<Any>("banListDate"))
                )

            session
                .createQuery(criteriaQuery)
                .resultList
          }
    } catch (exception: PersistenceException) {
      val causeMessage = exception.cause?.cause?.message

      if (causeMessage != null && causeMessage.contains("Table") && causeMessage.contains("doesn't exist")
      ) {
        throw SKCException(ErrConstants.DB_MISSING_TABLE, ErrorType.DB002)
      }

      log.error("Error fetching ban list effective start dates for format {}", format, exception)
      throw SKCException("Error fetching ban list dates from DB", ErrorType.DB002)
    }

    stopwatch.stop()
    log.debug(
      "Time taken to fetch ban list effective start dates from DB: {}",
      stopwatch.totalTimeMillis
    )

    return BanListDates(dates)
  }


  override fun getBanListByBanStatus(date: String, status: BanListCardStatus, format: String): List<Card> {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun numberOfBanLists(): Int {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun getPreviousBanListDate(currentBanList: String, format: String): String {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun getNewContentOfBanList(
    banListDate: String,
    previousBanListDate: String,
    status: BanListCardStatus,
    format: String
  ): List<CardsPreviousBanListStatus> {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun getRemovedContentOfBanList(
    banListDate: String,
    previousBanListDate: String,
    format: String
  ): List<CardsPreviousBanListStatus> {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun getBanListDetailsForCard(cardId: String, format: BanListFormat): List<CardBanListStatus> {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }

  override fun getCardBanListStatusByDate(cardId: String, banListDate: String): String {
    throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
  }
}