package pizza;

import java.util.Set;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;

@SuppressWarnings("serial")

@Indexed
public class Pizza implements Serializable {
	private static final Logger LOGGER = getLogger(Pizza.class);

	@DocumentId
	@Id
	private int id;

	@IndexedEmbedded
	private PizzaType pizzaType;

	@IndexedEmbedded
	private PizzaSize size;

	@IndexedEmbedded
	private Set<PizzaToppings> toppings;
	
	private String status;
	
	private LocalDateTime dateToBeCooked;

	public LocalDateTime getDateToBeCooked() {		
		return dateToBeCooked;
	}

	public void setDateToBeCooked(LocalDateTime dateToBeCooked) {		
		this.dateToBeCooked = dateToBeCooked;
	}

	public Pizza() {		
		pizzaType = new PizzaType(Type.SPECIAL.toString());
		toppings = new HashSet<PizzaToppings>();
		size = new PizzaSize(Sizes.LARGE.toString());
		status = "NEW";
	}

	public Pizza(PizzaType pizzaType, PizzaSize size, Set<PizzaToppings> toppings) {
		LOGGER.warn("New pizza: ");
		this.pizzaType = pizzaType;
		this.size = size;
		this.toppings = toppings;
	}

	public PizzaSize getSize() {
		return size;
	}

	public void setSize(PizzaSize size) {
		this.size = size;
	}

	public void setSize(String sizeString) {
		this.size.setSize(sizeString);
	}

	public Set<PizzaToppings> getToppings() {
		return toppings;
	}

	public void setToppings(Set<PizzaToppings> toppings) {
		this.toppings = toppings;
	}

	public void setToppings(String[] toppingStrings) {
		for (int i = 0; i < toppingStrings.length; i++) {
			toppings.add(new PizzaToppings(toppingStrings[i]));
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PizzaType getPizzaType() {
		return pizzaType;
	}

	public void setPizzaType(PizzaType pizzaType) {
		this.pizzaType = pizzaType;
	}

	public void setPizzaType(String pizzaType) {
		this.pizzaType.setName(pizzaType);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int seconds() {
		return LocalDateTime.now().toLocalTime().toSecondOfDay()/18;
	}
}
