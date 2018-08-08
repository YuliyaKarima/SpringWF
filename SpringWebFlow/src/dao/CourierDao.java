package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pizza.Express;;

public class CourierDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Express express) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(express);
		tx.commit();
		session.close();
	}

	public List<Express> list() {
		Session session = sessionFactory.openSession();
		List<Express> expressList = session.createQuery("from Express").list();
		session.close();
		return expressList;
	}

	public List<Express> findFree() {
		List<Express> express = null;
		Session session = sessionFactory.openSession();
		String queryString = "select express from Express express where express.isFree = :isFree";
		TypedQuery<Express> query = session.createQuery(queryString, Express.class);
		query.setParameter("isFree", true);
		if (!query.getResultList().isEmpty()) {
			express = query.getResultList();
		}
		session.close();
		return express;
	}
}
