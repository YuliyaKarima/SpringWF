package pizza;

import javax.persistence.Embeddable;

@Embeddable
public class CreditCardPayment extends Payment {
	private String authorization;

	public CreditCardPayment() {
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String toString() {
		return "CREDIT:  $" + getAmount() + " ; AUTH: " + authorization;
	}

	public String getAuthorization() {
		return authorization;
	}

}
