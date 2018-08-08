package pizza;
import org.apache.log4j.Logger;

public class OrderServiceImpl implements  OrderService{
    private static final Logger LOGGER =
            Logger.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl() {
    }

    public void saveOrder(Order order) {
        LOGGER.debug("SAVING ORDER:  ");
        LOGGER.debug("   id:  " + order.getId());
        LOGGER.debug("   Customer:  " + order.getCustomer().getName());
        LOGGER.debug("   # of Pizzas:  " + order.getPizzas().size());
        LOGGER.debug("   Payment:  " + order.getPayment());
    }
}
