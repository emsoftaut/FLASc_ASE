package com.asemonash.model;

public class Model {
	
	private String cypherString;
	
	public Model(String cypherString) {
		this.cypherString = cypherString;
	}
	
	public String getCypherString() {
		return cypherString;
	}

	public void setCypherString(String cypherString) {
		this.cypherString = cypherString;
		//createCypherSyntax();
	}

	
	public void createCypherSyntax() {
		System.out.println("In model " + cypherString );
	}
	
	
	@Override
	public String toString() {
		return "Model [cypherString=" + cypherString + "]";
	}
	
	

}
