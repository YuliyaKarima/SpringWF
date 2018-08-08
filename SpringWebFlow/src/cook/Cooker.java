package cook;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import employee.Employee;
import user.User;

public class Cooker implements Serializable, Employee{
	private int id;
	private String name;
	private int category;
	private Date dob;
	private String address;
	private double experience;
	private Set<User> users;
		
	public Cooker() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public double getExperience() {
		return experience;
	}
	public void setExperience(double experience) {
		this.experience = experience;
	}
	@Override
	public Date getDob() {		
		return dob;
	}
	@Override
	public String getAddress() {		
		return address;
	}
	@Override
	public Set<User> getUser() {		
		return users;
	}	
}
