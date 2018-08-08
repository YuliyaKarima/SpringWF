package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cook.Cooker;
import pizza.Express;;

public class CookDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Cooker cooker) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(cooker);
		tx.commit();
		session.close();
	}

	public List<Cooker> list() {
		Session session = sessionFactory.openSession();
		List<Cooker> cookerList = session.createQuery("from Cooker").list();
		session.close();
		return cookerList;
	}
}
