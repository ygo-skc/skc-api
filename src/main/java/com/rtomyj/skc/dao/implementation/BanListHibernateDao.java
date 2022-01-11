package com.rtomyj.skc.dao.implementation;

import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.dao.BanListDao;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListDate;
import com.rtomyj.skc.model.banlist.BanListDates;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.hibernate.BanListTable;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("ban-list-hibernate")
@Slf4j
public class BanListHibernateDao implements BanListDao
{
	private static final String UNSUPPORTED_OPERATION_MESSAGE = "HibernateDao not able to execute method.";
	@Autowired
	private EntityManagerFactory entityManagerFactory;


	@Override
	public BanListDates getBanListDates() throws YgoException
	{
		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();

		try (final Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession())
		{
			CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

			CriteriaQuery<BanListDate> criteriaQuery = criteriaBuilder.createQuery(BanListDate.class);
			Root<BanListTable> root = criteriaQuery.from(BanListTable.class);
			criteriaQuery.select(criteriaBuilder.construct(BanListDate.class, root.get("banListDate"))).distinct(true);
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("banListDate")));

			final BanListDates banListDates = BanListDates
					.builder()
					.dates(
							session.createQuery(criteriaQuery)
									.getResultList()
					)
					.build();


			stopwatch.stop();
			log.debug("Time taken to fetch ban list effective start dates from DB: {}", stopwatch.getTotalTimeMillis());

			return banListDates;
		} catch (PersistenceException exception)
		{
			String causeMessage = exception.getCause().getCause().getMessage();
			if (causeMessage.contains("Table") && causeMessage.contains("doesn't exist"))
			{
				throw new YgoException(ErrConstants.DB_MISSING_TABLE, ErrorType.D002);
			}
		}

		return null;
	}

	@Override
	public List<Card> getBanListByBanStatus(String date, Dao.Status status)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public int getNumberOfBanLists()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<String> getBanListDatesInOrder()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public String getPreviousBanListDate(String currentBanList)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<CardsPreviousBanListStatus> getNewContentOfBanList(String banListDate, Dao.Status status)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<CardsPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<CardBanListStatus> getBanListDetailsForCard(final String cardId)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	@Override
	public boolean isValidBanList(@NotNull String banListDate) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	@Nullable
	@Override
	public String getCardBanListStatusByDate(@NotNull String cardId, @NotNull String banListDate) {
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}
}