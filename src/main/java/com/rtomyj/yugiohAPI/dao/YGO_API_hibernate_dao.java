package com.rtomyj.yugiohAPI.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.rtomyj.yugiohAPI.model.BanLists;
import com.rtomyj.yugiohAPI.model.Card;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("hibernate")
public class YGO_API_hibernate_dao implements YGO_API_dao {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public List<BanLists> getBanListStartDates() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		Criteria criteria = session.createCriteria(BanLists.class);
		criteria.addOrder(Order.desc("banListDate"));
		criteria.setProjection(Projections.distinct(Projections.property("banListDate")));

		List<BanLists> banLists = criteria.list();

		return banLists;
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