package pizza;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class PricingEngineImpl implements PricingEngine, Serializable {
	private static final Logger LOGGER = Logger.getLogger(PricingEngineImpl.class);
	
	private Map<String, Float> sizePrices;
	private float pricePerTopping;
	
	public PricingEngineImpl() {
	}

	
	public Map<String, Float> getSizePrices() {
		return sizePrices;
	}


	public void setSizePrices(Map<String, Float> sizePrices) {
		this.sizePrices = sizePrices;
	}


	public float getPricePerTopping() {
		return pricePerTopping;
	}


	public void setPricePerTopping(float pricePerTopping) {
		this.pricePerTopping = pricePerTopping;
	}


	public float calculateOrderTotal(Order order) {
		LOGGER.warn("Calculating order price total");

		float total = 0.0f;
	
		for (Pizza pizza : order.getPizzas()) {
			LOGGER.warn(pizza.getSize());
			
			float pizzaPrice = sizePrices.get(pizza.getSize().toString());
			if (pizza.getToppings().size() > 2) {
				pizzaPrice += (pizza.getToppings().size() * pricePerTopping);
			}
			total += pizzaPrice;
		}

		return total;
	}
}
