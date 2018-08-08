package pizza;

import java.io.Serializable;

public class PlaceWrapper implements Serializable{
private Place place;

public PlaceWrapper() {
	
}
/**
 * @return the place
 */
public Place getPlace() {
	return place;
}

/**
 * @param place the place to set
 */
public void setPlace(Place place) {
	this.place = place;
}

}
