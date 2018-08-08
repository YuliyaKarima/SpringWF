package ingredient;

import java.io.Serializable;

public class ProductType implements Serializable {
	private String name;
	private String measure;	

	public ProductType() {
	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}
}
