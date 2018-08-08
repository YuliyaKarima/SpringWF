package aspect;
import static org.apache.log4j.Logger.*;

import org.apache.log4j.Logger;

import pizza.CustomerServiceImpl;
public class Logging {
	private static final Logger LOGGER = getLogger(CustomerServiceImpl.class);
	
	public Logging() {
		
	}
	
	public void startFind(String phoneNumber) {
		LOGGER.warn("Find customer with phone: " + phoneNumber);
	}
	
	public void startCreate(Class<?> aClass) {
		LOGGER.warn("Creating new instance of " + aClass.getName());		
	}
}
