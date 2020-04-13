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
		startNodeSet = new TreeSet<String>();
		
		ListIterator<Relationships> reListIterator = relationshipsList.listIterator();
		System.out.println("hello" + reListIterator.hasNext());
		
		int listIteCounter = 0;
		while(reListIterator.hasNext()) {
			
			//System.out.println(reListIterator.getClass());
			Relationships rel = reListIterator.next();
			startNodeSet.add(rel.getStartNode());
			//System.out.println(rel);
			listIteCounter++;
			//reListIterator.next();
		}
		
		//System.out.println("Records returned by List Iterator " + listIteCounter);
		
		int counter = 0; 
		
		for(String startN : startNodeSet ) {
			//System.out.println("The start node is"+startN);
			for(Relationships relationships : relationshipsList) {
				if(startN.equals(relationships.getStartNode())) {
					if(relationships.getInRoleHeader1().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader2().toLowerCase().contains("STEPS".toLowerCase())) {
					System.out.println(getDiagramNode(startN).getName() +"-->"+ getDiagramNode(relationships.getEndNode()).getName());
					}
					else if(relationships.getInRoleHeader2().toLowerCase().contains("INITIAL STEP".toLowerCase()) &&
							relationships.getInRoleHeader1().toLowerCase().contains("STEPS".toLowerCase())) {
						System.out.println(getDiagramNode(startN).getName() +"<--"+ getDiagramNode(relationships.getEndNode()).getName());
						
					}
				}
			}
			//counter++;
		}
		//System.out.println("Records returned by  for each " + counter);
		
//		while(reListIterator.has) {
//			System.out.println("PREVIOUS "+reListIterator.previous());
//			System.out.println("CURRENT " + reListIterator);
//			System.out.println("NEXT " +reListIterator.next());
//		}
		
//		for(Relationships r : this.relationshipsList) {
//			
//			//System.out.println(r);
//			if(r.getInRoleHeader2().toLowerCase().contains("steps".toLowerCase())) {
//				//System.out.println("ROOT-->" + getDiagramNode(r.getStartNode()));
//				System.out.println(r);
//			}
////			else if(r.getInRoleHeader2().toLowerCase().contains("initial".toLowerCase())) {
////				System.out.println("LEAF-->" + getDiagramNode(r.getStartNode()));
////			}
//			
////			System.out.println("Start Node "+getDiagramNode(r.getStartNode()));
////			System.out.println("End Node "+getDiagramNode(r.getEndNode()));
////			System.out.println(r);
////			System.out.println("*****************************");
//		}
	}
	
	
	private Relationships getRelationshiById(String id) {
		Relationships rel = null;
		for(Relationships relationships: relationshipsList) {
			
		}
		return rel;
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
