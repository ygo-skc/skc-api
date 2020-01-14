package com.rtomyj.yugiohAPI.dao.database.implementation;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.BanList;
import com.rtomyj.yugiohAPI.model.Card;

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
	public List<BanList> getBanListStartDates() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		CriteriaQuery<BanList> criteriaQuery = criteriaBuilder.createQuery(BanList.class);
		Root<BanList> root = criteriaQuery.from(BanList.class);
		criteriaQuery.select(root.get("banListDate")).distinct(true);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("banListDate")));
		List<BanList> results = session.createQuery(criteriaQuery).getResultList();

		session.close();
		return results;
	}

	@Override
	public Card getCardInfo(String cardID)
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

	public List<Map<String, String>> getNewContentOfBanList(String banListDate, String status){
		return null;
	}

	public List<Map<String, String>> getRemovedContentOfBanList(String newBanList)
	{
		return null;
	}

	@Override
	public String getCardBanListStatusByDate(String cardId, String banListDate) {
		return null;
	}
}