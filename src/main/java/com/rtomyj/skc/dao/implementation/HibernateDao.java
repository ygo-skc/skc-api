package com.rtomyj.skc.dao.implementation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.enums.ProductType;
import com.rtomyj.skc.model.banlist.BanListDate;
import com.rtomyj.skc.model.card.CardBrowseResults;
import com.rtomyj.skc.model.banlist.CardBanListStatus;
import com.rtomyj.skc.model.banlist.CardsPreviousBanListStatus;
import com.rtomyj.skc.model.banlist.BanListDates;
import com.rtomyj.skc.model.card.Card;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;
import com.rtomyj.skc.model.card.MonsterAssociation;
import com.rtomyj.skc.model.hibernate.BanListTable;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.model.product.ProductContent;
import com.rtomyj.skc.model.product.Products;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;


/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("hibernate")
@Slf4j
public class HibernateDao implements Dao
{

	@Autowired
	private EntityManagerFactory entityManagerFactory;


	@Override
	public BanListDates getBanListDates()
	{

		final StopWatch stopwatch = new StopWatch();
		stopwatch.start();


		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		CriteriaQuery<BanListDate> criteriaQuery = criteriaBuilder.createQuery(BanListDate.class);
		Root<BanListTable> root = criteriaQuery.from(BanListTable.class);
		criteriaQuery.select(criteriaBuilder.construct(BanListDate.class, root.get("banListDate"))).distinct(true);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("banListDate")));

		final BanListDates banListDates = BanListDates
			.builder()
			.dates(session.createQuery(criteriaQuery).getResultList())
			.build();

		session.close();


		stopwatch.stop();
		log.debug("Time taken to fetch ban list effective start dates from DB: {}", stopwatch.getTotalTimeMillis());

		return banListDates;

	}


	@Override
	public Card getCardInfo(String cardID) throws YgoException
	{
		return null;
	}

	@Override
	public List<Card> getBanListByBanStatus(String date, Status status)
	{
		return null;
	}

	public int getNumberOfBanLists()
	{
		return 0;
	}

	public List<String> getBanListDatesInOrder()
	{
		return null;
	}

	public String getPreviousBanListDate(String currentBanList)
	{
		return "";
	}

	public List<CardsPreviousBanListStatus> getNewContentOfBanList(String banListDate, Status status){
		return null;
	}

	public List<CardsPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
	{
		return null;
	}

	@Override
	public String getCardBanListStatusByDate(String cardId, String banListDate) {
		return null;
	}

	@Override
	public List<Card> searchForCardWithCriteria(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType, final int limit, final boolean getBanInfo)
	{
		return null;
	}
	public boolean isValidBanList(final String banListDate)
	{
		return false;
	}

	public List<Product> getAllPackDetails()
	{
		return null;
	}

	public Products getAllProductsByType(final ProductType productType, final String locale)
	{
		return null;
	}

	public Map<String, Integer> getProductRarityCount(final String packId)
	{
		return null;
	}

	public Set<ProductContent> getProductContents(final String packId, final String locale)
	{
		return null;
	}

	public MonsterTypeStats getMonsterTypeStats(final String cardColor){ return null; }

	public DatabaseStats getDatabaseStats()	{ return null; }

	public Set<Product> getProductDetailsForCard(final String cardId) { return null; }

	public List<CardBanListStatus> getBanListDetailsForCard(final String cardId) { return null; }

	public CardBrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> attributeSet, final Set<String> monsterTypeSet, final Set<String> monsterSubTypeSet
			, final Set<String> monsterLevels, Set<String> monsterRankSet, Set<String> monsterLinkRatingsSet){ return null; }

	public Set<String> getCardColors(){ return null; }

	public Set<String> getMonsterAttributes(){ return null; }

	public Set<String> getMonsterTypes(){ return null; }

	public Set<String> getMonsterSubTypes(){ return null; }

	public Set<MonsterAssociation> getMonsterAssociationField(final String monsterAssociationField){ return null; }

	public Product getProductInfo(final String productId, final String locale) { return null; }

	public List<Product> getProductsByLocale(final String locale) { return null; }

}