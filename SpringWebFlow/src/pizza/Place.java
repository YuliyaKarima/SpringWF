package pizza;

import java.io.Serializable;

public class Place implements Serializable{
	private int id;
	private int countSeats;
	private boolean isFree;
	private boolean isBooked;

	public Place() {

	}
	
	public Place(Place place, Bookings boooking) {
		
	}

	/**
	 * @return the countSeats
	 */
	public int getCountSeats() {
		return countSeats;
	}

	/**
	 * @param countSeats
	 *            the countSeats to set
	 */
	public void setCountSeats(int countSeats) {
		this.countSeats = countSeats;
	}

	/**
	 * @return the isFree
	 */
	public boolean getIsFree() {
		return isFree;
	}

	/**
	 * @param isFree
	 *            the isFree to set
	 */
	public void setIsFree(boolean isFree) {
		this.isFree = isFree;
	}

	/**
	 * @return the isBooked
	 */
	public boolean getIsBooked() {
		return isBooked;
	}

	/**
	 * @param isBooked the isBooked to set
	 */
	public void setIsBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}	
	
	public String toString() {
		return "Table N" + id + " with " + countSeats + " seats.";
	}
}
