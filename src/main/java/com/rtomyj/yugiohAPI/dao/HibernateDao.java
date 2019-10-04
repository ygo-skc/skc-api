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
public class HibernateDao implements Dao {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public List<BanLists> getBanListStartDates() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();

		CriteriaQuery<BanLists> q = cb.createQuery(BanLists.class);
		Root<BanLists> c = q.from(BanLists.class);
		q.select(c.get("banListDate")).distinct(true);
		q.orderBy(cb.desc(c.get("banListDate")));
		List<BanLists> l = session.createQuery(q).getResultList();

		session.close();
		return l;
	}

	@Override
	public Card getCardInfo(String cardID) {
		return null;
	}

	@Override
	public List<Card> getBanListByBanStatus(String date, String status) {
		return null;
	}

}