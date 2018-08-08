package pizza;

import javax.persistence.Embeddable;

@Embeddable
public class CashOrCheckPayment extends Payment {

	public CashOrCheckPayment() {
	}

	public String toString() {
		return "CASH or CHECK:  $" + getAmount();
	}
}
