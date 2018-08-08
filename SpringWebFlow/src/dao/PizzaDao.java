package dao;

import static org.apache.log4j.Logger.getLogger;

import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import flow.PizzaFlowActions;
import pizza.Bookings;
import pizza.Pizza;
import pizza.PizzaToppings;

public class PizzaDao {
	private SessionFactory sessionFactory;
	private static final Logger LOGGER = getLogger(PizzaDao.class);

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Save pizza object. Check if pizza's name, size and toppings already exist in
	 * DB and if not save them to DB.
	 * 
	 * @param pizza
	 */
	public void save(Pizza pizza) {
		ApplicationContext context = new ClassPathXmlApplicationContext("hibernate.xml");
		PizzaSizeDao pizzaSizeDao = (PizzaSizeDao) context.getBean("pizzaSizeDao");
		PizzaTypeDao pizzaTypeDao = (PizzaTypeDao) context.getBean("pizzaTypeDao");
		PizzaToppingDao pizzaToppingDao = (PizzaToppingDao) context.getBean("pizzaToppingDao");
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");

		LOGGER.warn("Pizza: " +  pizza.getPizzaType() + " (" + pizza.getSize() + ") ");
		// save pizza size if not exists in DB yet
		if (pizzaSizeDao.findSize(pizza.getSize().toString())) {
			pizza.setSize(pizzaSizeDao.size(pizza.getSize().toString()));
		} else {
			pizzaSizeDao.save(pizza.getSize());
		}
		// save pizza type if not exists in DB yet
		if (pizzaTypeDao.findType(pizza.getPizzaType().toString())) {
			pizza.setPizzaType(pizzaTypeDao.type(pizza.getPizzaType().toString()));
		} else {
			pizzaTypeDao.save(pizza.getPizzaType());
		}
		// save pizza toppings if not exist in DB yet
		Set<PizzaToppings> toppings = new HashSet<PizzaToppings>();
		for (PizzaToppings topping : pizza.getToppings()) {
			if (pizzaToppingDao.findTopping(topping.getName())) {
				toppings.add(pizzaToppingDao.topping(topping.getName()));
			} else {
				pizzaToppingDao.save(topping);
				toppings.add(topping);
			}
		}
		pizza.setToppings(toppings);

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(pizza);
		tx.commit();
		session.close();
	}

	public List<Pizza> list() {
		Session session = this.sessionFactory.openSession();
		List<Pizza> pizzaList = session.createQuery("from Pizza").list();
		session.close();
		return pizzaList;
	}
	
	public void update(Pizza pizza) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.merge(pizza);
		tx.commit();
		session.close();
	}
	
	public List<Pizza> toCook() {
		Session session = this.sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Pizza.class);
		criteria.add(Restrictions.eq("status", "WAIT_FOR_COOK"));			
		criteria.setFetchMode("pizzaType", FetchMode.JOIN);
		criteria.setFetchMode("size", FetchMode.JOIN);
		criteria.setFetchMode("toppings", FetchMode.JOIN);
		criteria.setFetchMode("pizzaType.ingredientsValue", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Pizza> pizzaList = criteria.list();
		session.close();
		return pizzaList;
	}
	
	public List<Pizza> cooking() {		
		Session session = this.sessionFactory.openSession();	
		Criteria criteria = session.createCriteria(Pizza.class);
		criteria.add(Restrictions.eq("status", "COOKING"));		
		criteria.setFetchMode("pizzaType", FetchMode.JOIN);
		criteria.setFetchMode("size", FetchMode.JOIN);
		criteria.setFetchMode("toppings", FetchMode.JOIN);
		criteria.setFetchMode("pizzaType.ingredientsValue", FetchMode.SELECT);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Pizza> pizzaList = criteria.list();
		session.close();
		return pizzaList;
	}
}
