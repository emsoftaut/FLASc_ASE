package com.asemonash.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.driver.internal.shaded.io.netty.util.internal.SocketUtils;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.asemonash.extract.HtmlParser;
import com.asemonash.helper.DiagramNode;
import com.asemonash.helper.DiagramType;
import com.asemonash.helper.Label;
import com.asemonash.helper.RelationshipLinkedSet;
import com.asemonash.helper.Relationships;
import com.asemonash.load.DatabaseConnector;
import com.asemonash.load.FetchedRecords;



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
		
		if(!(databaseConnector.recordsExist())) {
			System.out.println("NO RECORDS EXIST.... RUNNING INITIAL SCRIPT");
			//System.out.println("hello");
			System.out.println("Initial query-->"+ cypherString);
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
	
		String updateQuery = matchString +" CREATE "+ updatedCypherString.substring(0, updatedCypherString.length() - 1);
		System.out.println("Update query --> "+updateQuery);
		databaseConnector.runInitialCypherScript(updateQuery);
	}
	
	
	private String createUpdatedCypherQuery(Relationships r, String start, String end) {
		String cString = "", startStr, endStr = "";
		//String startNodeLabel, endNodeLabel;
		DiagramNode startNode = getDiagramNode(start);
		DiagramNode endNode = getDiagramNode(end);
		
		
//		startNodeLabel = startNode.getLabel().toString();
//		endNodeLabel = endNode.getLabel().toString();
//		
		
//		if(r.getInRelationship().equalsIgnoreCase(": Condition Correct") || 
//				r.getInRelationship().equalsIgnoreCase(": Condition inCorrect") ) {
//			isProcessDiagram = true;
//			
//		}
		
//		if (startNode.getLabel() == Label.TASK && endNode.getLabel()== Label.TASK && !DiagramType.diagramTypeProcess) {
//			endNode.setLabel(Label.SUB_TASK);
//			//cString = startStr + "-[:TS]->" + endStr + ",";
//		}

	
		if(!(startNodeList.contains(startNode.getAlias()))) {
			
			startStr = "(" + startNode.getAlias()+":"+ startNode.getLabel() + "{name:" + "\""+ startNode.getName() + "\"" + 
			",id:" + "\""+ startNode.getId() + "\"" +
			",sub_label:" + "\""+ startNode.getSubLabel() + "\"" +
			",organization:" + "\""+ startNode.getOrganization() + "\"" +
			",participant:" + "\""+ startNode.getParticipant() + "\"" +"}"+
			")";
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
			",sub_label:" + "\""+ endNode.getSubLabel() + "\"" +
			",organization:" + "\""+ endNode.getOrganization() + "\"" +
			",participant:" + "\""+ endNode.getParticipant() + "\"" +"}"+
			")";
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
		
		else if (DiagramType.diagramTypeProcess) {
			cString = startStr + "-[:PR]->" + endStr + ",";
		}
//		else if (startNode.getLabel() == Label.TASK && endNode.getLabel()== Label.TASK) {
//			endNode.setLabel(Label.SUB_TASK);
//			cString = startStr + "-[:TS]->" + endStr + ",";
//		}
		
		else if (startNode.getLabel() == Label.OTHER && endNode.getLabel() == Label.OTHER) {
			cString = startStr + "-[:OO]->" + endStr + ",";
		}
		
		else if (startNode.getLabel() == Label.TASK && endNode.getLabel() == Label.TASK) {
			cString = startStr + "-[:TT]->" + endStr + ",";
		}
		
		else if (startNode.getLabel() == Label.OTHER && endNode.getLabel() == Label.TASK) {
			cString = startStr + "-[:OT]->" + endStr + ",";
		}
		
		else if (startNode.getLabel() == Label.TASK && endNode.getLabel() == Label.OTHER) {
			cString = startStr + "-[:TO]->" + endStr + ",";
		}
		
//		else  {
//			cString = startStr + "-[:TS]->" + endStr + ",";
//		}
		
		//System.out.println("is a process diagram "+ isProcessDiagram);
		
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


