package com.rtomyj.skc.dao.implementation;

import com.rtomyj.skc.constant.ErrConstants;
import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.enums.ProductType;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.banlist.BanListDate;
import com.rtomyj.skc.model.banlist.BanListDates;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.card.CardBrowseResults;
import com.rtomyj.skc.model.card.MonsterAssociation;
import com.rtomyj.skc.model.hibernate.BanListTable;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.model.product.ProductContent;
import com.rtomyj.skc.model.product.Products;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("hibernate")
@Slf4j
public class HibernateDao implements Dao
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
	public Card getCardInfo(String cardID) throws YgoException
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	@Override
	public List<Card> getBanListByBanStatus(String date, Status status)
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

	public List<CardsPreviousBanListStatus> getNewContentOfBanList(String banListDate, Status status)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<CardsPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	@Override
	public String getCardBanListStatusByDate(String cardId, String banListDate)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	@Override
	public List<Card> searchForCardWithCriteria(final String cardId, final String cardName, final String cardAttribute
			, final String cardColor, final String monsterType, final int limit, final boolean getBanInfo)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}
	public boolean isValidBanList(final String banListDate)
	{
		return false;
	}

	public List<Product> getAllPackDetails()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Products getAllProductsByType(final ProductType productType, final String locale)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Map<String, Integer> getProductRarityCount(final String packId)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<ProductContent> getProductContents(final String packId, final String locale)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public MonsterTypeStats getMonsterTypeStats(final String cardColor)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public DatabaseStats getDatabaseStats()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<Product> getProductDetailsForCard(final String cardId)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<CardBanListStatus> getBanListDetailsForCard(final String cardId)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public CardBrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> attributeSet, final Set<String> monsterTypeSet, final Set<String> monsterSubTypeSet
			, final Set<String> monsterLevels, Set<String> monsterRankSet, Set<String> monsterLinkRatingsSet)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<String> getCardColors()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<String> getMonsterAttributes()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<String> getMonsterTypes()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<String> getMonsterSubTypes()
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Set<MonsterAssociation> getMonsterAssociationField(final String monsterAssociationField)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public Product getProductInfo(final String productId, final String locale)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

	public List<Product> getProductsByLocale(final String locale)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_MESSAGE);
	}

}