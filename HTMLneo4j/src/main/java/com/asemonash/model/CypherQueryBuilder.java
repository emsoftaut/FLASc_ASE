package com.asemonash.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.driver.internal.shaded.io.netty.util.internal.SocketUtils;

import com.asemonash.controller.QueryStringBuilder;
import com.asemonash.helper.DiagramNode;
import com.asemonash.helper.Label;
import com.asemonash.helper.RelationshipLinkedSet;
import com.asemonash.helper.Relationships;



public class CypherQueryBuilder<E> {
	
	private String cypherString;
	private DatabaseConnector databaseConnector;
	private List<FetchedRecords> fetchedRecordsList;
	private List<DiagramNode> diagramNodesList;
	private Set<String> matchedNodes;
	private ArrayList<String> startNodeList;
	
	public CypherQueryBuilder(String cypherString) {
		this.cypherString = cypherString;
		this.databaseConnector = new DatabaseConnector("bolt://localhost:7687", "neo4j", "chandan");
		fetchedRecordsList = new ArrayList<FetchedRecords>();
		matchedNodes = new TreeSet();
		startNodeList = new ArrayList<String>();
	}
	
	public String getCypherString() {
		return cypherString;
	}

	public void setCypherString(String cypherString) {
		this.cypherString = cypherString;
	}

	public boolean createdNewCypherQuery() {
		cypherString = "CREATE " + cypherString.substring(0, cypherString.length() - 1);
		System.out.println(cypherString);
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
	
	
	public void createUpdatedCypherQuery(List<Relationships> relList, List<DiagramNode> diagramNodesList) {
		String matchString = "MATCH";
		
		this.diagramNodesList = diagramNodesList;
		String updatedCypherString = "";
		System.out.println("More records in relList");
		for(Relationships relationships : relList) {
			for(FetchedRecords fetchedRecords: fetchedRecordsList) {
				if(relationships.getStartNode().equalsIgnoreCase(fetchedRecords.getId()) || 
					relationships.getEndNode().equalsIgnoreCase(fetchedRecords.getId())) {
					matchedNodes.add(fetchedRecords.getId());
				}
			}
		}
			
		for(String nodeID: matchedNodes) {
			matchString += " (" + getDiagramNode(nodeID).getAlias() + "{id :" +"\""+ getDiagramNode(nodeID).getId() +"\""+ "}),";
		}
			
		matchString = matchString.substring(0, matchString.length()-1);
			
		for(Relationships relationships: relList) {
			updatedCypherString += createUpdatedCypherQuery(relationships, relationships.getStartNode(), relationships.getEndNode());
		}
	
		databaseConnector.runInitialCypherScript(matchString +" CREATE "+ updatedCypherString.substring(0, updatedCypherString.length() - 1));
	}
	
	
	private String createUpdatedCypherQuery(Relationships r, String start, String end) {
		String cString = "", startStr, endStr = "";
		DiagramNode startNode = getDiagramNode(start);
		DiagramNode endNode = getDiagramNode(end);
		
		
//		if(startNode.getLabel()==Label.TASK) {
//			endNode.setLabel(Label.SUB_TASK);
//		}
	
		if(!(startNodeList.contains(startNode.getAlias()))) {
			
			startStr = "(" + startNode.getAlias()+":"+ startNode.getLabel() + "{name:" + "\""+ startNode.getName() + "\"" + 
			",id:" + "\""+ startNode.getId() + "\"" +
			",sub_label:" + "\""+ startNode.getSubLabel() + "\"" +"}"+")";
		}
		else {
			startStr = "(" +startNode.getAlias()+ ")";
		}
		
		if(matchedNodes.contains(startNode.getId())) {
			startStr = "(" +startNode.getAlias()+ ")";
		}
		
		if(!(startNodeList.contains(endNode.getAlias()))) {
			
			endStr	= "(" + endNode.getAlias() +":"+ endNode.getLabel() + "{name:" + "\""+ endNode.getName() + "\"" + 
			",id:" + "\""+ endNode.getId() + "\"" +
			",sub_label:" + "\""+ endNode.getSubLabel() + "\"" +"}"+")";
		}
		else {
			
			endStr = "(" + endNode.getAlias() + ")";
		}
		
		if(matchedNodes.contains(endNode.getId())) {
			endStr = "(" + endNode.getAlias() + ")";
		}
		
		
		if(r.getInRelationship().equalsIgnoreCase(": Condition Correct") || 
				r.getInRelationship().equalsIgnoreCase(": Condition inCorrect") ) {
			if(r.getInRelationship().equalsIgnoreCase(": Condition Correct")) {
				cString = startStr + "-[:YES]->"+ endStr + ",";
			}
			else if (r.getInRelationship().equalsIgnoreCase(": Condition inCorrect")) {
				cString = startStr + "-[:NO]->"+ endStr + ",";
			}
			
			//System.out.println("The relationship is-->" + cString);
		}
		else if(startNode.getLabel() == Label.DAP) {
			cString = startStr + "-[:RT]->" + endStr + ",";
		}
		else {
			cString = startStr + "-[:TS]->" + endStr + ",";
		}
		
		startNodeList.add(startNode.getAlias());
		startNodeList.add(endNode.getAlias());
		return cString;
	}

	@Override
	public String toString() {
		return "Model [cypherString=" + cypherString + "]";
	}
	
	
	private DiagramNode<E> getDiagramNode(String nodeID){
		
		DiagramNode<E> diagramNode = null;
		for(DiagramNode<E> d : diagramNodesList) {
			if(d.getId().equals(nodeID)) {
				diagramNode = d;
			}
		}
		return diagramNode;	
	}
}


