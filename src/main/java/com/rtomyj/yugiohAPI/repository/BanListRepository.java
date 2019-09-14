package com.rtomyj.yugiohAPI.repository;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.rtomyj.yugiohAPI.model.BanLists;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BanListRepository {


	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public List<BanLists> getBanListStartDates() {
		Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
		Criteria criteria = session.createCriteria(BanLists.class);
		criteria.addOrder(Order.desc("banListDate"));
		criteria.setProjection(Projections.distinct(Projections.property("banListDate")));

		List<BanLists> banLists = criteria.list();
		List<String> banListsDates;


		return banLists;
	}
}
