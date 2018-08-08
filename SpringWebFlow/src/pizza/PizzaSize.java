package pizza;

import java.io.Serializable;
import javax.persistence.Embeddable;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

@Embeddable
public class PizzaSize implements Serializable {
	
	@DocumentId
	private int id;
	
	@Field
	private String size;

	public PizzaSize() {

	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public PizzaSize(String size) {
		super();
		this.size = size;
	}

	@Override
	public String toString() {
		return size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PizzaSize other = (PizzaSize) obj;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

}
