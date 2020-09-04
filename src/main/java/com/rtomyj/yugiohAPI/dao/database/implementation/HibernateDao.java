package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.banlist.BanList;
import com.rtomyj.yugiohAPI.model.banlist.CardPreviousBanListStatus;
import com.rtomyj.yugiohAPI.model.banlist.BanListStartDates;
import com.rtomyj.yugiohAPI.model.card.Card;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterTypeStats;
import com.rtomyj.yugiohAPI.model.card.MonsterAssociation;
import com.rtomyj.yugiohAPI.model.hibernate.BanListTable;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.ProductContent;
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
		Root<BanListTable> root = criteriaQuery.from(BanListTable.class);
		criteriaQuery.select(criteriaBuilder.construct(BanList.class, root.get("banListDate"))).distinct(true);
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

	public List<CardPreviousBanListStatus> getNewContentOfBanList(String banListDate, Status status){
		return null;
	}

	public List<CardPreviousBanListStatus> getRemovedContentOfBanList(String newBanList)
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

	public Set<ProductContent> getProductContents(final String packId, final String locale)
	{
		return null;
	}

	public MonsterTypeStats getMonsterTypeStats(final String cardColor){ return null; }

	public DatabaseStats getDatabaseStats()	{ return null; }

	public Set<Product> getProductDetailsForCard(final String cardId) { return null; }

	public List<BanList> getBanListDetailsForCard(final String cardId) { return null; }

	public CardBrowseResults getBrowseResults(final Set<String> cardColors, final Set<String> attributeSet, final Set<String> monsterLevels){ return null; }

	public Set<String> getCardColors(){ return null; }

	public Set<String> getMonsterAttributes(){ return null; }

	public Set<MonsterAssociation> getMonsterAssociationField(final String monsterAssociationField){ return null; }

	public Product getProductInfo(final String productId, final String locale) { return null; }

	public List<Product> getProductsByLocale(final String locale) { return null; }

}