package pizza;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class CostsEngine implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(PricingEngineImpl.class);
	private float pizzaCost = 0.0f;

	public CostsEngine() {

	}

	public float calculateCostsTotal(Order order) {
		LOGGER.warn("Calculating order costs total");

		float total = 0.0f;

		for (Pizza pizza : order.getPizzas()) {
			pizza.getPizzaType().getIngredientsValue()
					.forEach((ingredient, value) -> pizzaCost = (float) (ingredient.getCost() * value));
			total += pizzaCost;
		}

		return total;
	}
}
