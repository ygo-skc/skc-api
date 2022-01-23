package com.rtomyj.skc.dao.implementation

import com.rtomyj.skc.constant.ErrConstants
import com.rtomyj.skc.dao.BanListDao
import com.rtomyj.skc.enums.BanListCardStatus
import com.rtomyj.skc.exception.ErrorType
import com.rtomyj.skc.exception.YgoException
import com.rtomyj.skc.model.banlist.BanListDate
import com.rtomyj.skc.model.banlist.BanListDates
import com.rtomyj.skc.model.banlist.CardBanListStatus
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus
import com.rtomyj.skc.model.card.Card
import com.rtomyj.skc.model.hibernate.BanListTable
import org.hibernate.SessionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.util.StopWatch
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceException

/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("ban-list-hibernate")
class BanListHibernateDao @Autowired constructor(private var entityManagerFactory: EntityManagerFactory) : BanListDao {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        private const val UNSUPPORTED_OPERATION_MESSAGE = "HibernateDao not able to execute method."
    }


    @Throws(YgoException::class)
    override fun getBanListDates(): BanListDates {
        var dates: List<BanListDate> = emptyList()

        val stopwatch = StopWatch()
        stopwatch.start()

        try {
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
                                .construct(BanListDate::class.java, root.get<Any>("banListDate"))
                        )
                        .distinct(true)
                    criteriaQuery
                        .orderBy(
                            criteriaBuilder
                                .desc(root.get<Any>("banListDate"))
                        )

                    dates = session
                        .createQuery(criteriaQuery)
                        .resultList

                    stopwatch.stop()
                    log.debug(
                        "Time taken to fetch ban list effective start dates from DB: {}",
                        stopwatch.totalTimeMillis
                    )
                }
        } catch (exception: PersistenceException) {
            val causeMessage = exception.cause?.cause?.message

            if ((causeMessage != null)
                && causeMessage.contains("Table") && causeMessage.contains("doesn't exist")
            ) {
                throw YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002)
            }
        }

        return BanListDates(dates)
    }


    override fun getBanListByBanStatus(date: String, status: BanListCardStatus): List<Card> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun numberOfBanLists(): Int {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun banListDatesInOrder(): List<String> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun getPreviousBanListDate(currentBanList: String): String {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun getNewContentOfBanList(
        banListDate: String,
        status: BanListCardStatus
    ): List<CardsPreviousBanListStatus> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun getRemovedContentOfBanList(banListDate: String): List<CardsPreviousBanListStatus> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun getBanListDetailsForCard(cardId: String): List<CardBanListStatus> {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun isValidBanList(banListDate: String): Boolean {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }

    override fun getCardBanListStatusByDate(cardId: String, banListDate: String): String {
        throw UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE)
    }
}