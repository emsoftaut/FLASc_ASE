package com.asemonash.model;

import java.io.File;



public class CypherQueryBuilder {
	
	private String cypherString;
	
	public CypherQueryBuilder(String cypherString) {
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
		cypherString = "CREATE " + cypherString.substring(0, cypherString.length() - 1);
		System.out.println("In model " + cypherString );
		DatabaseConnector databaseConnector = new DatabaseConnector("bolt://localhost:7687", "neo4j", "chandan");
		//databaseConnector.runQuery(this.cypherString);
		if(!(databaseConnector.recordsExist())) {
			System.out.println("RECORDS DONT EXIST.... RUNNING INITIAL SCRIPT");
			databaseConnector.runInitialCypherScript(cypherString);
		}
		else {
			System.out.println("RECORDS EXIST");
		}

	}
	
	
	@Override
	public String toString() {
		return "Model [cypherString=" + cypherString + "]";
	}
	
}


