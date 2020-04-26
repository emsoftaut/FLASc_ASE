package com.asemonash.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import org.neo4j.driver.internal.shaded.io.netty.util.internal.SocketUtils;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.shaded.org.jctools.queues.ConcurrentCircularArrayQueue;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.asemonash.helper.DiagramNode;
import com.asemonash.helper.Label;
import com.asemonash.helper.RelationshipLinkedSet;
import com.asemonash.helper.Relationships;
import com.asemonash.model.CypherQueryBuilder;

public class QueryStringBuilder<E> {
	private List<Relationships> relationshipsList;
	private List<DiagramNode<E>> diagramNodesList;
	private Set<String> startNodeSet;
	private RelationshipLinkedSet relationshipLinkedSet; 
	private String cypherString;
	private ArrayList<String> startNodeList;
	private CypherQueryBuilder cypherQueryBuilder;
	
	
	public QueryStringBuilder(){
		startNodeSet = new TreeSet<String>();
		startNodeList = new ArrayList<String>();
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
		Iterator<E> relItr = null;
		
		relItr = populateRelationshipsLinkedList().iterator();
		
//		List<Relationships> lr = getRelationshipLinkedSet().getRelationshipsList();
//		
//		System.out.println("after removing duplicates");
//		
//		for(Relationships r: lr) {
//			if(!(r.getInRelationship().equalsIgnoreCase(": Arrowed Connector"))) {
//				
//				System.out.println(r.getStartNode() +"::"+ r.getEndNode());
//				
//				//System.out.println(r.getStartNode() +" data-"+ getDiagramNode(r.getStartNode()).getName() +"::"+ 
//			//" data-" + getDiagramNode(r.getEndNode()).getName() + r.getEndNode());
//			}
//		}
//		
		
		while(relItr.hasNext()) {
			Relationships r = (Relationships) relItr.next();
			startNode = getDiagramNode(r.getStartNode());
			endNode = getDiagramNode(r.getEndNode());
			cypherString += createCypherQuery(r,startNode, endNode);	
		}
		cypherQueryBuilder = new CypherQueryBuilder(cypherString);
		//cypherQueryBuilder.createCypherSyntax();
		
		if(!(cypherQueryBuilder.createdNewCypherQuery())) {
			//System.out.println("HERE");
			cypherQueryBuilder.createUpdatedCypherQuery(getRelationshipLinkedSet().getRelationshipsList(), diagramNodesList);
		}
		
	}
	
	private String createCypherQuery(Relationships r,DiagramNode startNode, DiagramNode endNode) {
		String cString = "", startStr, endStr = "";
		
//		if(startNode.getLabel()==Label.TASK) {
//			endNode.setLabel(Label.SUB_TASK);
//		}
//		
		
		if(!(startNodeList.contains(startNode.getAlias()))) {
			
			startStr = "(" + startNode.getAlias()+":"+ startNode.getLabel() + "{name:" + "\""+ startNode.getName() + "\"" + 
			",id:" + "\""+ startNode.getId() + "\"" +
			",sub_label:" + "\""+ startNode.getSubLabel() + "\"" +"}"+")";
		}
		else {
			startStr = "(" +startNode.getAlias()+ ")";
		}
		
		if(!(startNodeList.contains(endNode.getAlias()))) {
			
			endStr	= "(" + endNode.getAlias() +":"+ endNode.getLabel() + "{name:" + "\""+ endNode.getName() + "\"" + 
			",id:" + "\""+ endNode.getId() + "\"" +
			",sub_label:" + "\""+ endNode.getSubLabel() + "\"" +"}"+")";
			//edgeCounter++;
		}
		else {
			
			endStr = "(" + endNode.getAlias() + ")";
		}
		
		
		if(r.getInRelationship().equalsIgnoreCase(": Condition Correct") || 
				r.getInRelationship().equalsIgnoreCase(": Condition inCorrect") ) {
			//System.out.println("The relationship is-->" + startNode.getName() + "##"+ r.getInRelationship() + "##" + endNode.getName());
			cString = startStr + "-[:"+ r.getInRelationship().replaceAll("[: ]", "")  +"]->"+ endStr + ",";
			System.out.println("The relationship is-->" + cString);
			//System.out.println("Rel is-->" + getDiagramNode( r.getStartNode()) +"-"+r.getInRelationship()+"->"+ getDiagramNode( r.getEndNode()));
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
	
	
	private RelationshipLinkedSet populateRelationshipsLinkedList() {
		
		relationshipLinkedSet = new RelationshipLinkedSet<Comparable>();
		
		for(Relationships rel: relationshipsList) {
			startNodeSet.add(rel.getStartNode());
		}
		
		for(String startN : startNodeSet ) {
			for(Relationships relationships : relationshipsList) {
				if(startN.equals(relationships.getStartNode())) {
					//BRAINSTOMRING DIAGRAM
					if(relationships.getInRoleHeader1().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("STEPS".toLowerCase())) {
						
					String startNode = getDiagramNode(startN).getId();
					String endNode = getDiagramNode(relationships.getEndNode()).getId();
					relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
					}
					else if(relationships.getInRoleHeader2().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader1().toLowerCase().contains("STEPS".toLowerCase())) {
						
						String endNode = getDiagramNode(startN).getId();
						String startNode = getDiagramNode(relationships.getEndNode()).getId();
						relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
					}
					//TECHNIQUE AND DATA DIAGRAM
					if(relationships.getInRoleHeader1().toLowerCase().contains("FROM".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("TO".toLowerCase())) {
						
					String startNode = getDiagramNode(startN).getId();
					String endNode = getDiagramNode(relationships.getEndNode()).getId();
					relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
					}
					else if(relationships.getInRoleHeader2().toLowerCase().contains("FROM".toLowerCase()) &&
							relationships.getInRoleHeader1().toLowerCase().contains("TO".toLowerCase())) {
						
						String endNode = getDiagramNode(startN).getId();
						String startNode = getDiagramNode(relationships.getEndNode()).getId();
						relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
					}
					
					//PROCESS DIAGRAM
					if(relationships.getInRoleHeader1().toLowerCase().contains("CONDITION".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("OPERATION".toLowerCase())) {
						
					String startNode = getDiagramNode(startN).getId();
					String endNode = getDiagramNode(relationships.getEndNode()).getId();
					relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
					}
					else if(relationships.getInRoleHeader2().toLowerCase().contains("CONDITION".toLowerCase()) &&
							relationships.getInRoleHeader1().toLowerCase().contains("OPERATION".toLowerCase())) {
						
						String endNode = getDiagramNode(startN).getId();
						String startNode = getDiagramNode(relationships.getEndNode()).getId();
						relationshipLinkedSet.addtoSet(startNode, endNode, relationships.getInRelationship());
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

	public RelationshipLinkedSet getRelationshipLinkedSet() {
		return this.relationshipLinkedSet;
	}
	
}