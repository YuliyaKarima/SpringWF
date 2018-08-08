package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pizza.Customer;
import pizza.PizzaSize;
import pizza.PizzaToppings;
import pizza.PizzaType;

public class PizzaToppingDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(PizzaToppings pizzaTopping) {
		if (!findTopping(pizzaTopping.getName())) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.persist(pizzaTopping);
			tx.commit();
			session.close();
		}
	}

	public List<PizzaToppings> list() {
		Session session = sessionFactory.openSession();
		List<PizzaToppings> toppings = session.createQuery("from PizzaToppings").list();
		session.close();
		return toppings;
	}

	public boolean findTopping(String toppingname) {
		Session session = sessionFactory.openSession();
		String queryString = "select topping from PizzaToppings topping where topping.name = :toppingname";
		TypedQuery<PizzaToppings> query = session.createQuery(queryString, PizzaToppings.class);
		query.setParameter("toppingname", toppingname);
		boolean exist = !query.getResultList().isEmpty();
		session.close();
		return exist;
	}
	
	public PizzaToppings topping(String toppingname) {
		Session session = sessionFactory.openSession();
		String queryString = "select topping from PizzaToppings topping where topping.name = :toppingname";
		TypedQuery<PizzaToppings> query = session.createQuery(queryString, PizzaToppings.class);
		query.setParameter("toppingname", toppingname);
		PizzaToppings topping = query.getSingleResult();
		session.close();
		return topping;
	}
}
