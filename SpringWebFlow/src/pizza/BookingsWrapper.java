package pizza;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import flow.PizzaFlowActions;

public class BookingsWrapper implements Serializable{
	private static final Logger LOGGER = getLogger(BookingsWrapper.class);

	private List<Bookings> bookingList;
	
	public BookingsWrapper() {
		this.bookingList = new ArrayList<>();		
	}

	public List<Bookings> getBookingList() {
		LOGGER.warn("getBookingList");
		return bookingList;
	}

	public void setBookingList(List<Bookings> bookingList) {
		LOGGER.warn("setBookingList");
		this.bookingList = bookingList;
	}	
}
