package pizza;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.IndexedEmbedded;

import flow.PizzaFlowActions;

public class Order implements Serializable {
	private static final Logger LOGGER = getLogger(Order.class);
	@DocumentId
	@Id
	private int id;
	private Customer customer;
	private Set<Pizza> pizzas;
	@IndexedEmbedded
	private Payment payment;
	private Date dateOfOrder;
	private float costs;

	public Order() {	
		pizzas = new HashSet<Pizza>();
		customer = new Customer();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(Set<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	public void addPizza(Pizza pizza) {
		pizzas.add(pizza);
	}

	public float getTotal() {
		return 0.0f;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dateOfOrder
	 */
	public Date getDateOfOrder() {
		return dateOfOrder;
	}

	/**
	 * @param dateOfOrder
	 *            the dateOfOrder to set
	 */
	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public float getCosts() {
		return costs;
	}

	public void setCosts(float costs) {
		this.costs = costs;
	}
}
