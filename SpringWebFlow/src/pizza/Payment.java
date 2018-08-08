package pizza;

import java.io.Serializable;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

public abstract class Payment implements Serializable {
	@DocumentId
	private int id;
	@Field
	private float amount;

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
