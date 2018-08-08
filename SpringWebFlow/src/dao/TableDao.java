package dao;

import static org.apache.log4j.Logger.getLogger;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.query.Query;

import flow.PizzaFlowActions;
import pizza.Bookings;
import pizza.Customer;
import pizza.Place;

public class TableDao {

	private static final Logger LOGGER = getLogger(TableDao.class);
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Place table) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(table);
		tx.commit();
		session.close();
	}

	public List<Place> allTables() {
		Session session = sessionFactory.openSession();
		List<Place> tableList = session.createQuery("from Place").list();
		session.close();
		return tableList;
	}

	public Place tableByID(int id) {
		Session session = sessionFactory.openSession();
		Place place = session.get(Place.class, id);
		session.close();
		return place;
	}

	public List<Place> freeTables() {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Place.class);
		criteria.add(Restrictions.eq("isFree", true));
		criteria.add(Restrictions.eq("isBooked", false));
		// List<Place> tableList = session.createQuery("from Place").list();
		List<Place> tableList = criteria.list();
		session.close();
		return tableList;
	}

	public List<Place> freeTables(LocalDateTime forDate) {
		Session session = sessionFactory.openSession();

	/*	CriteriaBuilder builder = session.getCriteriaBuilder();

		CriteriaQuery<Place> criteriaQuery = builder.createQuery(Place.class);

		Root<Bookings> bookings = criteriaQuery.from(Bookings.class);
		Root<Place> tables = criteriaQuery.from(Place.class);

		bookings.join("table", JoinType.LEFT);

		criteriaQuery.select(tables)
				.where(builder.or(builder.equal(tables.get("isFree"), true),
						builder.and(builder.equal(tables.get("isFree"), false),								
								builder.or(builder.greaterThanOrEqualTo(bookings.get("dateFor"), forDate.plusHours(2)),
										builder.lessThan(bookings.get("dateFor"), forDate.minusHours(2))))));

		criteriaQuery.distinct(true);
		TypedQuery<Place> places = session.createQuery(criteriaQuery);

		List<Place> freePlaces = places.getResultList();
		LOGGER.warn("freePlaces: " + freePlaces.size());
		for (Place place : freePlaces) {
			LOGGER.warn(place.getId());
		}*/

		String queryString = "select place from Place place "
				+ "where place.isFree = true or (place.isFree = false "
				+ "and not exists (select booking from Bookings booking where booking.table.id = place.id "
				+ "and (booking.dateFor <= :dateBefore and booking.dateFor > :dateAfter)))";
		LOGGER.warn(queryString);
		TypedQuery<Place> query = session.createQuery(queryString, Place.class);
		LOGGER.warn(forDate.plusHours(2) + " " + forDate.minusHours(2));
		query.setParameter("dateBefore", forDate.plusHours(2));
		query.setParameter("dateAfter", forDate.minusHours(2));

		List<Place> tableList = query.getResultList();
		session.close();
		return tableList;
	}
}
