package com.quertle.demo.model;

public class General {

	public String firstName;
	public String lastName;

	public General() {
		
	}
	
	public General(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "General [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	

}
