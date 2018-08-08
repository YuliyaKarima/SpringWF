package pizza;

import java.io.Serializable;

public class CustomerNotFoundException extends Exception implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

}
