package pizza;

import static org.apache.log4j.Logger.*;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.CustomerDao;
import flow.PizzaFlowActions;

public class CustomerServiceImpl implements CustomerService {

	private static final Logger LOGGER = getLogger(CustomerServiceImpl.class);

	public CustomerServiceImpl() {
	}

	public Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException {
		ApplicationContext context = new ClassPathXmlApplicationContext("hibernate.xml");
		LOGGER.warn("Find customer with phone: " + phoneNumber);
		CustomerDao dao = (CustomerDao) context.getBean("customerDao");
		Customer customer = dao.findByTel(phoneNumber);
		if (customer != null) {
			LOGGER.warn("Customer found.");
			return customer;
		} else {
			LOGGER.warn("Customer not found.");
			throw new CustomerNotFoundException();
		}
	}
}
