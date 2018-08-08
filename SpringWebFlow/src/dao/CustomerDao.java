package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pizza.Customer;

public class CustomerDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Customer customer) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(customer);
		tx.commit();
		session.close();
	}

	public List<Customer> list() {
		Session session = sessionFactory.openSession();
		List<Customer> personList = session.createQuery("from Customer").list();
		session.close();
		return personList;
	}

	public Customer findByTel(String tel) {
		Customer customer = null;
		Session session = sessionFactory.openSession();
		String queryString = "select customer from Customer customer where customer.phoneNumber = :tel";
		TypedQuery<Customer> query = session.createQuery(queryString, Customer.class);
		query.setParameter("tel", tel);
		if (!query.getResultList().isEmpty()) {
			customer = query.getSingleResult();
		}
		session.close();
		return customer;
	}
}
