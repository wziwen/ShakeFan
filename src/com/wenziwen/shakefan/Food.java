package com.wenziwen.shakefan;

public class Food {
	
	public Food(int type, String name, String phone, String restaurant) {
		super();
		this.type = type;
		this.name = name;
		this.phone = phone;
		this.restaurant = restaurant;
	}
	
	public Food() {
		id = -1;
		type = LUNCH;
		name = "";
		phone = "";
		restaurant = "";
	}
	
	public long id;
	public int type;
	public String name;
	public String phone;
	public String restaurant;
	
	public static final int BREAKFAST = 0;
	public static final int LUNCH	  = 1;
	public static final int DINER     = 2;
}
