package pizza;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;

public class Delivery extends Order implements Serializable {
	private static final Logger LOGGER = getLogger(Delivery.class);
	private Express express;
	private String status;
	private String expressName;
	private LocalDateTime dateFor;
	
	public Delivery() {
		super();
		status = "NEW";
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the express
	 */
	public Express getExpress() {
		LOGGER.warn("getExpress");
		return express;
	}

	/**
	 * @param express
	 *            the express to set
	 */
	public void setExpress(Express express) {
		LOGGER.warn("setExpress");
		this.express = express;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		LOGGER.warn("setExpressName");
		this.expressName = expressName;
	}

	public LocalDateTime getDateFor() {
		return dateFor;
	}

	public void setDateFor(LocalDateTime dateFor) {
		this.dateFor = dateFor;
	}
	
	public void setDateFor(String dateFor) {
		this.dateFor = LocalDateTime.parse(dateFor);
		LOGGER.warn("Date for: " + this.dateFor);
	}
}
