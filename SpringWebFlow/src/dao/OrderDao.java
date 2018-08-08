package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pizza.Order;
import pizza.Pizza;

public class OrderDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void save(Order order) {		
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.save(order);
			tx.commit();
			session.close();		
	}

	public List<Order> list() {
		Session session = this.sessionFactory.openSession();
		List<Order> pizzaList = session.createQuery("from Order").list();
		session.close();
		return pizzaList;
	}

}
