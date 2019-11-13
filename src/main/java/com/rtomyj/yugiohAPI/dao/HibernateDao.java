package com.rtomyj.yugiohAPI.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("hibernate")
public class HibernateDao implements Dao
{
	@Autowired
	private EntityManagerFactory entityManagerFactory;



	/**
	 * @return item
	 */
	@Override
	public List<BanLists> getBanListStartDates() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();

		CriteriaQuery<BanLists> criteriaQuery = criteriaBuilder.createQuery(BanLists.class);
		Root<BanLists> root = criteriaQuery.from(BanLists.class);
		criteriaQuery.select(root.get("banListDate")).distinct(true);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("banListDate")));
		List<BanLists> results = session.createQuery(criteriaQuery).getResultList();

		session.close();
		return results;
	}



	/**
	 * @param cardID
	 * @return item
	 */
	@Override
	public Card getCardInfo(String cardID)
	{
		return null;
	}



	/**
	 * @param date
	 * @param status
	 * @return item
	 */
	@Override
	public List<Card> getBanListByBanStatus(String date, Status status)
	{
		return null;
	}



	/**
	 *
	 */
	public int getNumberOfBanLists()
	{
		return 0;
	}



	/**
	 *
	 */
	public int getBanListPosition()
	{
		return 0;
	}
}