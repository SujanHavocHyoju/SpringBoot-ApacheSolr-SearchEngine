package com.quertle.demo.dto;

public class Address {
	public String location;
	
	public Address() {
		
	}
	
	public Address(String location) {
		super();
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "Address [location=" + location + "]";
	}
	
}
