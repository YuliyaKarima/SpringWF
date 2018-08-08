package dao;

import static org.apache.log4j.Logger.getLogger;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import ingredient.Ingredient;
import pizza.Bookings;
import pizza.Customer;
import pizza.Payment;
import pizza.Pizza;
import pizza.PizzaSize;
import pizza.PizzaToppings;
import pizza.PizzaType;
import pizza.Place;

public class BookingDao {

	private static final Logger LOGGER = getLogger(BookingDao.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Bookings booking) {
		Session session = sessionFactory.openSession();
		session.save(booking);
		session.close();
	}

	public List<Bookings> list() {
		Session session = sessionFactory.openSession();

		/*CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Bookings> criteriaQuery = builder.createQuery(Bookings.class);

		Root<Bookings> bookings = criteriaQuery.from(Bookings.class);
		bookings.join("customer", JoinType.LEFT);		
		SetJoin<Pizza, Bookings> joinPizza = bookings.joinSet("pizzas", JoinType.LEFT);		
		Join<PizzaType, Pizza> joinPizzaType = joinPizza.join("pizzaType", JoinType.LEFT);
		joinPizza.join("size", JoinType.LEFT);
		joinPizza.joinSet("toppings", JoinType.LEFT);
		joinPizzaType.joinMap("ingredientsValue", JoinType.LEFT);
		bookings.join("table", JoinType.LEFT);
		bookings.join("payment", JoinType.LEFT);

		criteriaQuery.select(bookings).where(builder.equal(bookings.get("isConfirmed"), false));
		criteriaQuery.distinct(true);
		TypedQuery<Bookings> tq = session.createQuery(criteriaQuery);
		List<Bookings> allBookings = tq.getResultList();
		LOGGER.warn("allBookings: " + allBookings.size());
		for (Bookings booking : allBookings) {
			LOGGER.warn(booking.getId());
		}*/
		
		Criteria criteria = session.createCriteria(Bookings.class);
		criteria.add(Restrictions.eq("isConfirmed", false));
		LOGGER.warn("Get new bookings ");
		criteria.setFetchMode("customer", FetchMode.JOIN);
		criteria.setFetchMode("pizzas", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.pizzaType", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.size", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.toppings", FetchMode.JOIN);
		criteria.setFetchMode("pizzas.pizzaType.ingredientsValue", FetchMode.SELECT);
		criteria.setFetchMode("table", FetchMode.JOIN);
		criteria.setFetchMode("payment", FetchMode.JOIN);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Bookings> bookingList = criteria.list();

		session.close();
		return bookingList;
	}

	public void confirm(int bookingID) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.get(Bookings.class, bookingID).setIsConfirmed(true);
		tx.commit();
		session.close();
	}
}
