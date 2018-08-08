package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pizza.Customer;
import pizza.PizzaSize;

public class PizzaSizeDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(PizzaSize pizzaSize) {
		if(!findSize(pizzaSize.getSize().toString())) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.persist(pizzaSize);
			tx.commit();
			session.close();
		}		
	}

	public List<PizzaSize> list() {
		Session session = sessionFactory.openSession();
		List<PizzaSize> sizes = session.createQuery("from PizzaSize").list();
		session.close();
		return sizes;
	}

	public boolean findSize(String sizename) {
		Session session = sessionFactory.openSession();
		String queryString = "select size from PizzaSize size where size.size = :size";
		TypedQuery<PizzaSize> query = session.createQuery(queryString, PizzaSize.class);
		query.setParameter("size", sizename);
		boolean exist = !query.getResultList().isEmpty();
		session.close();
		return exist;
	}
	
	public PizzaSize size(String sizename) {
		Session session = sessionFactory.openSession();
		String queryString = "select size from PizzaSize size where size.size = :size";
		TypedQuery<PizzaSize> query = session.createQuery(queryString, PizzaSize.class);
		query.setParameter("size", sizename);
		PizzaSize size = query.getSingleResult();
		session.close();
		return size;
	}
}
