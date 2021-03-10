package main.java.com.excilys.computerDatabase.model;

import main.java.com.excilys.computerDatabase.validator.InputValidator;

public class Company {
	private long id;
	private String name;
	
	public Company(long id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}
	
	public void setId(long id) {
		if (!InputValidator.isValidId(id)) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		if (!InputValidator.isValidName(name)) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company: " + this.name != null ? this.name : "unknown";
	}
}
