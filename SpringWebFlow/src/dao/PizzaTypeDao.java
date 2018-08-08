package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pizza.Customer;
import pizza.Delivery;
import pizza.PizzaSize;
import pizza.PizzaType;

public class PizzaTypeDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(PizzaType pizzaType) {
		if (!findType(pizzaType.getName())) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.persist(pizzaType);
			tx.commit();
			session.close();
		}
	}

	public List<PizzaType> list() {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(PizzaType.class);
		criteria.setFetchMode("ingredientsValue", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<PizzaType> types = criteria.list();
		session.close();
		return types;
	}

	public boolean findType(String typename) {
		Session session = sessionFactory.openSession();
		String queryString = "select type from PizzaType type where type.name = :typename";
		TypedQuery<PizzaType> query = session.createQuery(queryString, PizzaType.class);
		query.setParameter("typename", typename);
		boolean exist = !query.getResultList().isEmpty();
		session.close();
		return exist;
	}
	
	public PizzaType type(String typename) {
		Session session = sessionFactory.openSession();
		String queryString = "select type from PizzaType type where type.name = :typename";
		TypedQuery<PizzaType> query = session.createQuery(queryString, PizzaType.class);
		query.setParameter("typename", typename);
		PizzaType type = query.getSingleResult();
		session.close();
		return type;
	}
}
