package com.asemonash.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.css.ElementCSSInlineStyle;

public class CypherQueryBuilder<E> {
	private List<Relationships> relationshipsList;
	private List<DiagramNode<E>> diagramNodesList;
	private Set<String> startNodeSet;
	private RelationshipLinkedSet relationshipLinkedSet; 
	private String cypherString;
	private int queryCounter = 0;
	private int edgeCounter = 0;
	private ArrayList<String> startNodeList;
	//private ArrayList<String> endNodeList;
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
		startNodeList = new ArrayList<String>();
	
		while(relItr.hasNext()) {
			Relationships r = (Relationships) relItr.next();
			startNode = getDiagramNode(r.getStartNode());
			endNode = getDiagramNode(r.getEndNode());
			if(startNode==null || endNode == null) {
				continue;
			}
			//System.out.println("err1"+ endNode);
			cypherString += createCypherQuery(startNode, endNode);	
		}
		System.out.println("Cypher String is \n" + cypherString);
	}
	
	private String createCypherQuery(DiagramNode startNode, DiagramNode endNode) {
		String cString = "", startStr, endStr = "";
	
		if(!(startNodeList.contains(startNode.getAlias()))) {
			queryCounter++;
			if(startNode.getLabel() == Label.TASK) {
				endNode.setLabel(Label.SUB_TASK);
			}
			startStr = "(" + startNode.getAlias()+":"+ startNode.getLabel() + "{name:" + "\""+ startNode.getName() + "\"" + 
			",id:" + "\""+ startNode.getId() + "\"" +
			",sub_label:" + "\""+ startNode.getSubLabel() + "\"" +"}"+")";
		}
		else {
			if(startNode.getLabel() == Label.TASK) {
				//System.out.println("ERROR "+ endNode);
				endNode.setLabel(Label.SUB_TASK);
			}
			startStr = "(" +startNode.getAlias()+ ")";
		}
		
		if(!(startNodeList.contains(endNode.getAlias()))) {
			if(startNode.getLabel() == Label.TASK) {
				endNode.setLabel(Label.SUB_TASK);
			}
			endStr	= "(" + endNode.getAlias() +":"+ endNode.getLabel() + "{name:" + "\""+ endNode.getName() + "\"" + 
			",id:" + "\""+ endNode.getId() + "\"" +
			",sub_label:" + "\""+ endNode.getSubLabel() + "\"" +"}"+")";
			edgeCounter++;
		}
		else {
			if(startNode.getLabel() == Label.TASK) {
				endNode.setLabel(Label.SUB_TASK);
			}
			endStr = "(" + endNode.getAlias() + ")";
		}
		
		if(startNode.getLabel() == Label.DAP) {
			cString = startStr + "-[:RT]->" + endStr + ",";
		}
		else {
			cString = startStr + "-[:TS]->" + endStr + ",";
		}
		
		System.out.println(cString);
		//System.out.println(startNode +"__>"+ endNode);
		
		startNodeList.add(startNode.getAlias());
		startNodeList.add(endNode.getAlias());
		return cString;
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
					else if (relationships.getInRoleHeader1().toLowerCase().contains("FROM".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("TO".toLowerCase())) {
						String startNode = getDiagramNode(startN).getId();
						//System.out.println("At cypher query builder-->" + relationships.getEndNode());
						String endNode = relationships.getEndNode();
						relationshipLinkedSet.addtoSet(startNode, endNode);
					}
					else if (relationships.getInRoleHeader1().toLowerCase().contains("TO".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("FROM".toLowerCase())) {
						String endNode = getDiagramNode(startN).getId();
						String startNode = relationships.getEndNode();
						relationshipLinkedSet.addtoSet(startNode, endNode);
					}
				}
			}
		}
		return relationshipLinkedSet;
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
