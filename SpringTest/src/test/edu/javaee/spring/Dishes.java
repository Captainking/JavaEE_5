package test.edu.javaee.spring;

import javax.management.loading.PrivateClassLoader;

public class Dishes {
	private String dishName;
	private Integer dishPrice;
	private Atest atest;
	
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	
	public Integer getDishPrice() {
		return dishPrice;
	}
	public void setDishPrice(Integer dishPrice) {
		this.dishPrice = dishPrice;
	}
	public Atest getAtest() {
		return atest;
	}
	public void setAtest(Atest atest) {
		this.atest = atest;
	}
	public void show()
	{
		System.out.println("Dish÷–show∑Ω∑®: "+dishName);
	}
	
	
}
