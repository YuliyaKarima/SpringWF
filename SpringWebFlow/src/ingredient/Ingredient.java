package ingredient;

import static org.apache.log4j.Logger.getLogger;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;

import flow.PizzaFlowActions;

public class Ingredient implements Serializable {

	private static final Logger LOGGER = getLogger(PizzaFlowActions.class);

	private int id;
	private String country;
	private LocalDateTime dateOfProduction;
	private double cost;
	private int shelfLife;
	private double fat;
	private double protein;
	private double carbohydrate;
	private ProductType type;
	private float stockValue;

	public Ingredient() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public LocalDateTime getDateOfProduction() {			
		return dateOfProduction;
	}

	public double getCost() {
		return cost;
	}

	public int getShelfLife() {
		return shelfLife;
	}

	public double getFat() {
		return fat;
	}

	public double getProtein() {
		return protein;
	}

	public double getCarbohydrate() {
		return carbohydrate;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDateOfProduction(LocalDateTime dateOfProduction) {
		this.dateOfProduction = dateOfProduction;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void setShelfLife(int shelfLife) {
		this.shelfLife = shelfLife;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setCarbohydrate(double carbohydrate) {
		this.carbohydrate = carbohydrate;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public float getStockValue() {
		return stockValue;
	}

	public void setStockValue(float stockValue) {
		this.stockValue = stockValue;
	}

	public void addIngredients(float count) {
		stockValue += count;
	}

	public boolean takeIngredients(double count) {
		if (stockValue >= count) {
			stockValue -= count;
			return true;
		} else {
			LOGGER.warn("There is not enough count of " + type.getName() + " in the stock.");
			return false;
		}
	}	
	
	public boolean isBestDate(){
		return dateOfProduction.plusDays((long)(0.90*shelfLife)).isBefore(LocalDateTime.now());
	}	
}
