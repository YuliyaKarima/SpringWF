package pizza;

import java.io.Serializable;

public class OrderWrapper implements Serializable {
private Order order;

/**
 * @return the order
 */
public Order getOrder() {
	return order;
}

/**
 * @param order the order to set
 */
public void setOrder(Order order) {
	this.order = order;
}

}