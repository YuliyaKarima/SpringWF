package pizza;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.hibernate.search.annotations.Indexed;

@Indexed
public class Bookings extends Order implements Serializable {
	private static final Logger LOGGER = getLogger(Order.class);
	private LocalDateTime dateFor;
	private boolean isConfirmed;
	private Place table;

	public Bookings() {
		super();
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

	public boolean getIsConfirmed() {
		LOGGER.warn("getIsConfirmed");
		return isConfirmed;
	}

	public void setIsConfirmed(boolean isConfirmed) {
		LOGGER.warn("setIsConfirmed " + isConfirmed);
		this.isConfirmed = isConfirmed;
	}

	/**
	 * @return the table
	 */
	public Place getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(Place table) {
		this.table = table;
		table.setIsFree(false);
		table.setIsBooked(true);
	}
}
