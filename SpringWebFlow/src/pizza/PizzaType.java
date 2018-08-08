package pizza;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Embeddable;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import ingredient.Ingredient;

@Embeddable
public class PizzaType implements Serializable {

	@DocumentId
	private int id;

	@Field
	private String name;

	private Map<Ingredient, Double> ingredientsValue;

	public Map<Ingredient, Double> getIngredientsValue() {
		return ingredientsValue;
	}

	public String nutritionInfo;

	public void setIngredientsValue(Map<Ingredient, Double> ingredientsValue) {
		this.ingredientsValue = ingredientsValue;
	}

	public PizzaType() {
		ingredientsValue = new HashMap<>();
	}

	public PizzaType(String type) {
		name = type;
	}

	public PizzaType(int id, String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PizzaType other = (PizzaType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Set<Ingredient> ingredients() {
		return ingredientsValue.keySet();
	}

	public void setNutritionInfo() {
		double fat = 0.0f;
		double protein = 0.0f;
		double carbohydrate = 0.0f;
		for (Map.Entry<Ingredient, Double> set : ingredientsValue.entrySet()) {
			fat += set.getKey().getFat() * (set.getValue() / 10) * 10000;
			protein += set.getKey().getProtein() * (set.getValue() / 10) * 10000;
			carbohydrate += set.getKey().getCarbohydrate() * (set.getValue() / 10) * 10000;
		}

		int nutritionValue = (int) (fat * 9 + protein * 4 + carbohydrate * 4);
		String info = "fats: " + fat + "\n proteins: " + protein + "\n carbohydrates: " + carbohydrate
				+ "\n nutrition value: " + nutritionValue;
		nutritionInfo = info;
	}

	public String getNutritionInfo() {
		return nutritionInfo;
	}
}
