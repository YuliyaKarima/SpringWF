package pizza;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flow.PizzaFlowActions;

public class DeliveriesWrapper implements Serializable{
	private static final Logger LOGGER = getLogger(DeliveriesWrapper.class);

	private List<Delivery> deliveryList;

	public DeliveriesWrapper() {		
		this.deliveryList = new ArrayList<>();
	}
	
	public List<Delivery> getDeliveryList() {
		LOGGER.warn("getDeliveryList");
		return deliveryList;
	}

	public void setDeliveryList(List<Delivery> deliveryList) {
		LOGGER.warn("setDeliveryList");
		this.deliveryList = deliveryList;
	}
}
