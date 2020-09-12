package com.asemonash.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.AuthToken;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import com.asemonash.helper.DiagramType;

public class DatabaseConnector implements AutoCloseable {
	
	
	private Driver driver;
	private Session session;
	private ArrayList<FetchedRecords> recordList;
	
	public DatabaseConnector(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
		session = driver.session();
		recordList = new ArrayList();
	}
	
	

	public void runInitialCypherScript(String cypherQuery) {
		session.run(cypherQuery);
		DiagramType.setDiagramType(false);
	}
	
	public boolean recordsExist() {
		Result result = session.run("MATCH (a) RETURN a.name, a.id");
		List<Record> rList = result.list();
		if(rList.isEmpty()) {
			return false;
		}

		else {
			for(Record record : rList) {
				String name = record.get("a.name").toString();
				//name = name.substring(1, name.indexOf("\""));
				String id = record.get("a.id").toString();
				id = id.substring(1, id.length() - 1);
				
				//System.out.println(name +"-->"+ id);
				recordList.add(new FetchedRecords(name, id));
			}
			return true;
		}
	}
	
	public ArrayList<FetchedRecords> getRecordsFrmDatabase(){
		return this.recordList;
	}
	
	public void close() throws Exception {
		driver.close();
	}

}
