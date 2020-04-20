package com.asemonash.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asemonash.controller.QueryStringBuilder;
import com.asemonash.helper.RelationshipLinkedSet;
import com.asemonash.helper.Relationships;



public class CypherQueryBuilder<E> {
	
	private String cypherString;
	private DatabaseConnector databaseConnector;
	private List<FetchedRecords> fetchedRecordsList;
	//private List<Relationships> relationshipsList;
	//private QueryStringBuilder queryStringBuilder;
	//private List<>
	
	public CypherQueryBuilder(String cypherString) {
		this.cypherString = cypherString;
		this.databaseConnector = new DatabaseConnector("bolt://localhost:7687", "neo4j", "chandan");
		fetchedRecordsList = new ArrayList<FetchedRecords>();
	}
	
	public String getCypherString() {
		return cypherString;
	}

	public void setCypherString(String cypherString) {
		this.cypherString = cypherString;
		//createCypherSyntax();
	}

	
	public boolean createdNewCypherQuery() {
		cypherString = "CREATE " + cypherString.substring(0, cypherString.length() - 1);
		System.out.println("In model " + cypherString );
		
		if(!(databaseConnector.recordsExist())) {
			System.out.println("NO RECORDS EXIST.... RUNNING INITIAL SCRIPT");
			databaseConnector.runInitialCypherScript(cypherString);
			return true;
		}
		else {
			System.out.println("RECORDS EXIST");
			fetchedRecordsList = databaseConnector.getRecordsFrmDatabase();
			return false;
		}
	}
	
	public void createGraphAnyway() {
		databaseConnector.runInitialCypherScript(cypherString);
	}
	
	public void createUpdatedCypherQuery(List<Relationships> relList) {
		//System.out.println("Hello");
		//System.out.println(relList.size() +"-->"+ fetchedRecordsList.size());
		
		if(relList.size() >= fetchedRecordsList.size()) {
			System.out.println("More records in relList");
			for(Relationships relationships : relList) {
				for(FetchedRecords fetchedRecords: fetchedRecordsList) {
					
					//System.out.println(relationships.getStartNode() +"-->"+ fetchedRecords.getId());
					if(relationships.getStartNode().equalsIgnoreCase(fetchedRecords.getId())) {
						System.out.println("Node is present1");
					}
				}
			}
		}
		else {
			System.out.println("More records in database");
			for(FetchedRecords fetchedRecords: fetchedRecordsList) {
				for(Relationships relationships: relList) {
					//System.out.println(relationships.getStartNode() +"-->"+ fetchedRecords.getId());
					if(relationships.getStartNode().equalsIgnoreCase(fetchedRecords.getId())) {
						System.out.println("Node is present2");
					}
				}
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "Model [cypherString=" + cypherString + "]";
	}
	
}


