package com.asemonash.htmlparser;

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
	
	public CypherQueryBuilder(){
		relationshipLinkedSet = new RelationshipLinkedSet<Comparable>();
		startNodeSet = new TreeSet<String>();
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
		
		for(Relationships rel: relationshipsList) {
			startNodeSet.add(rel.getStartNode());
		}
		
		populateRelationshipsLinkedList();
		List<Relationships> rList = relationshipLinkedSet.getRelationshipsList();
			
		for(Relationships r: rList) {
			System.out.println(getDiagramNode(r.getStartNode()).getName() +"-->"+ getDiagramNode(r.getEndNode()).getName());
		}
	}

	private void populateRelationshipsLinkedList() {
		
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
