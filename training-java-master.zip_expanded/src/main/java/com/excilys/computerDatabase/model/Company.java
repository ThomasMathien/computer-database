package main.java.com.excilys.computerDatabase.model;

public class Company {
	private long id;
	private String name;
	
	public Company(long id, String name) {
		super();
		this.setId(id);
		this.setName(name);
	}
	
	public void setId(long id) {
		if (id < 0) {
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
		if (name.length()>255) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name != null ? this.name : "unknown";
	}
}
