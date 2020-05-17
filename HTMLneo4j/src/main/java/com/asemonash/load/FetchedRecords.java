package com.asemonash.load;

public class FetchedRecords {
	private String name;
	private String id;
	
	
	public FetchedRecords(String name, String id) {
		this.name = name;
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		return "Records [name=" + name + ", id=" + id + "]";
	}
	
	
	
}
