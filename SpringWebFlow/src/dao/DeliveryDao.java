package dao;

import static org.apache.log4j.Logger.getLogger;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import flow.PizzaFlowActions;
import pizza.Bookings;
import pizza.Customer;
import pizza.Delivery;
import pizza.Express;

public class DeliveryDao {
	private static final Logger LOGGER = getLogger(DeliveryDao.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Delivery delivery) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(delivery);
		tx.commit();
		session.close();
	}

	public List<Delivery> list() {
		Session session = sessionFactory.openSession();
		List<Delivery> deliveryList = session.createQuery("from Delivery").list();
		session.close();
		return deliveryList;
	}	
	
	public List<Delivery> newDeliveries() {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Delivery.class);
		criteria.add(Restrictions.eq("status", "NEW"));	
		LOGGER.warn("Get new deliveries ");
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("pizzas", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.pizzaType", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.size", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.toppings", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.pizzaType.ingredientsValue", FetchMode.SELECT);
		criteria.setFetchMode("payment", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Delivery> deliveryList = criteria.list();		
		
		session.close();
		return deliveryList;
	}	
	
	public List<Delivery> delivered() {
		Session session = sessionFactory.openSession();
		List<Delivery> deliveryList = session.createQuery("from Delivery where status = 'DELIVERED'").list();
		session.close();
		return deliveryList;
	}	
	
	public List<Delivery> inProgress() {
		Session session = sessionFactory.openSession();
		List<Delivery> deliveryList = session.createQuery("from Delivery where status = 'IN_PROGRESS'").list();
		session.close();
		return deliveryList;
	}	
	
	public void setExpress(int deliveryId, int expressId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Delivery delivery = session.get(Delivery.class, deliveryId);
		Express courier = session.get(Express.class, expressId);
		delivery.setExpress(courier);
		delivery.setStatus("WAIT_FOR_DELIVERY");
		tx.commit();
		session.close();
	}
	
	public void deliver(int deliveryId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Delivery delivery = session.get(Delivery.class, deliveryId);		
		delivery.setStatus("DELIVERING");
		tx.commit();
		session.close();
	}
}
