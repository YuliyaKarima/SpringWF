package dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import ingredient.Ingredient;
import ingredient.ProductType;
import pizza.Express;
import pizza.Pizza;;

public class IngredientDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Ingredient ingredient) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(ingredient);
		tx.commit();
		session.close();
	}
	
	public void update(Ingredient ingredient) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.merge(ingredient);
		tx.commit();
		session.close();
	}

	public List<Ingredient> list() {
		Session session = sessionFactory.openSession();
		List<Ingredient> ingredientList = session.createQuery("from Ingredient").list();
		session.close();
		return ingredientList;
	}

	public List<Ingredient> byType(ProductType type) {
		List<Ingredient> ingredients = null;
		Session session = sessionFactory.openSession();
		String queryString = "select ingredient from Ingredient ingredient where ingredient.type = :type";
		TypedQuery<Ingredient> query = session.createQuery(queryString, Ingredient.class);
		query.setParameter("type", type);
		if (!query.getResultList().isEmpty()) {
			ingredients = query.getResultList();
		}
		session.close();
		return ingredients;
	}

	public void add(Ingredient ingredient, float value) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update Ingredient ingredient set ingredient.stockValue = ingredient.stockValue + :value where ingredient.id = :id";
		TypedQuery<Ingredient> query = session.createQuery(queryString, Ingredient.class);
		query.setParameter("value", value);
		query.setParameter("id", ingredient.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void get(Ingredient ingredient, float value) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String queryString = "update Ingredient ingredient set ingredient.stockValue = ingredient.stockValue - :value where ingredient.id = :id";
		TypedQuery<Ingredient> query = session.createQuery(queryString, Ingredient.class);
		query.setParameter("value", value);
		query.setParameter("id", ingredient.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void get(Pizza pizza) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		pizza.getPizzaType().getIngredientsValue().forEach((ingredient, value) -> {
			if(ingredient.takeIngredients(value)) {
				String queryString = "update Ingredient ingredient set ingredient.stockValue = ingredient.stockValue - :value where ingredient.id = :id";
				TypedQuery<Ingredient> query = session.createQuery(queryString, Ingredient.class);
				query.setParameter("value", value);
				query.setParameter("id", ingredient.getId());
				query.executeUpdate();
			}			
		});

		tx.commit();
		session.close();
	}

}
