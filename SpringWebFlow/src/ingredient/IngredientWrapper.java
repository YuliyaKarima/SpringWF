package ingredient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IngredientWrapper implements Serializable{
	
	private Map<Ingredient, Double> ingredients = new HashMap<>();
	
	private Ingredient ingredient;
	private float value;
	
	public IngredientWrapper() {
		
	}
	public Ingredient getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}	
}
