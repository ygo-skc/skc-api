package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.BrowseResults;
import com.rtomyj.yugiohAPI.model.banlist.BanList;
import com.rtomyj.yugiohAPI.model.banlist.BanListComparisonResults;
import com.rtomyj.yugiohAPI.model.banlist.BanListStartDates;
import com.rtomyj.yugiohAPI.model.Card;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterType;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.Products;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Hibernate implementation of DB DAO interface.
 */
@Repository("hibernate")
public class HibernateDao implements Dao
{

	@Autowired
	private EntityManagerFactory entityManagerFactory;


	@Override
	public BanListStartDates getBanListStartDates()
	{

		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		CriteriaQuery<BanList> criteriaQuery = criteriaBuilder.createQuery(BanList.class);
		Root<BanList> root = criteriaQuery.from(BanList.class);
		criteriaQuery.select(root.get("banListDate")).distinct(true);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("banListDate")));

		final BanListStartDates banListStartDates = BanListStartDates
			.builder()
			.banListStartDates(session.createQuery(criteriaQuery).getResultList())
			.build();

		session.close();
		return banListStartDates;

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

	public int getBanListPosition(String banListDate)
	{
		return 0;
	}

	public String getPreviousBanListDate(String currentBanList)
	{
		return "";
	}

	public List<BanListComparisonResults> getNewContentOfBanList(String banListDate, Status status){
		return null;
	}

	public List<BanListComparisonResults> getRemovedContentOfBanList(String newBanList)
	{
		return null;
	}

	@Override
	public String getCardBanListStatusByDate(String cardId, String banListDate) {
		return null;
	}

	@Override
	public List<Card> getCardNameByCriteria(final String cardId, final String cardName, final String cardAttribute, final String cardColor, final String monsterType, final int limit)
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

	public Product getPackContents(final String packId, final String locale)
	{
		return null;
	}

	public MonsterType getMonsterTypeStats(final String cardColor){ return null; }

	public DatabaseStats getDatabaseStats()	{ return null; }

	public List<Product> getProductDetailsForCard(final String cardId) { return null; }

	public List<BanList> getBanListDetailsForCard(final String cardId) { return null; }

	public BrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> attributeSet, final Set<String> monsterLevels){ return null; }

	public CompletableFuture<Set<String>> getCardColors(){ return null; }

	public CompletableFuture<Set<String>> getMonsterAttributes(){ return null; }

	public CompletableFuture<Set<Integer>> getLevels(){ return null; }

	public CompletableFuture<Set<Integer>> getRanks() { return null; }

	public CompletableFuture<Set<Integer>> getLinkRatings() { return null; }

}