package com.asemonash.controller;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

public class CypherQueryBuilder<E> {
	private List<Relationships> relationshipsList;
	private List<DiagramNode<E>> diagramNodesList;
	private Set<String> startNodeSet;
	private RelationshipLinkedSet relationshipLinkedSet; 
	private String cypherString;
	private int queryCounter = 0;
	public CypherQueryBuilder(){
		startNodeSet = new TreeSet<String>();
		cypherString = "";
	}
	
	public List<Relationships> getRelationshipsList() {
		return relationshipsList;
	}

	public void setRelationshipsList(List<Relationships> relationshipsList) {
		this.relationshipsList = relationshipsList;
	}
	
	public List<DiagramNode<E>> getDiagramNodesList() {
		return diagramNodesList;
	}

	public void setDiagramNodesList(List<DiagramNode<E>> diagramNodesList) {
		this.diagramNodesList = diagramNodesList;
	}

	public void initQueryBuilder() {
		
		DiagramNode<E> startNode, endNode = null;
		for(Relationships rel: relationshipsList) {
			startNodeSet.add(rel.getStartNode());
		}
		
		Iterator<E> relItr = populateRelationshipsLinkedList().iterator();
		
		while(relItr.hasNext()) {
			Relationships r = (Relationships) relItr.next();
			startNode = getDiagramNode(r.getStartNode());
			endNode = getDiagramNode(r.getEndNode());
			cypherString += createCypherQuery(startNode, endNode);
			queryCounter++;
		}
		System.out.println("Cypher String is \n" + cypherString);
	}

	private RelationshipLinkedSet populateRelationshipsLinkedList() {
		relationshipLinkedSet = new RelationshipLinkedSet<Comparable>();
		
		for(String startN : startNodeSet ) {
			for(Relationships relationships : relationshipsList) {
				if(startN.equals(relationships.getStartNode())) {
					if(relationships.getInRoleHeader1().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("STEPS".toLowerCase())) {
						
					String startNode = getDiagramNode(startN).getId();
					String endNode = getDiagramNode(relationships.getEndNode()).getId();
					relationshipLinkedSet.addtoSet(startNode, endNode);
					}
					else if(relationships.getInRoleHeader2().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader1().toLowerCase().contains("STEPS".toLowerCase())) {
						
						String endNode = getDiagramNode(startN).getId();
						String startNode = getDiagramNode(relationships.getEndNode()).getId();
						relationshipLinkedSet.addtoSet(startNode, endNode);
					}
				}
			}
		}
		return relationshipLinkedSet;
	}

	private String createCypherQuery(DiagramNode startNode, DiagramNode endNode) {
		
		System.out.println(startNode +"-->"+ endNode);
		String cString = "", startStr, endStr = "";
		//System.out.println(queryCounter);
		if(startNode.getLabel().toString().equalsIgnoreCase(Label.DAP.toString())) {
			startStr = "(" +"root" +queryCounter+":"+ startNode.getLabel() + "{name:" + "\""+ startNode.getName() + "\"" + 
																			 ",id:" + "\""+ startNode.getId() + "\"" +
																			 ",sub_label:" + "\""+ startNode.getLabel() + "\"" +"}"+")";
			
			
			endStr	= "(" + "task"+ queryCounter +":"+ endNode.getLabel() + "{name:" + "\""+ endNode.getName() + "\"" + 
					 														",id:" + "\""+ endNode.getId() + "\"" +
					 														",sub_label:" + "\""+ endNode.getLabel() + "\"" +"}"+")";
			
			cString = startStr + "-[:RT]->" + endStr + ",";
		}
		
		//System.out.println(cypherString);
		return cString;
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
