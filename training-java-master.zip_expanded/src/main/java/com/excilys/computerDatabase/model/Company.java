package main.java.com.excilys.computerDatabase.model;

public class Company {
	private long id;
	private String name;
	
	public Company(String name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name != null ? this.name : "unknown";
	}
}
