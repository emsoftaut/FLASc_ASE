package com.asemonash.htmlparser;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RelationshipLinkedSet<E extends Comparable>{

	private Node<E> firstNode;
	private int numOfElements;
	private List<Relationships> relList;
	
	public RelationshipLinkedSet() {
		super();
		firstNode = null;
		numOfElements = 0;
		relList = (List<Relationships>) new LinkedList<E>();
	}
	
	
	private void add(E startNode, E endNode) {
		Node newNode = new Node<E>(startNode, endNode);
		newNode.next = firstNode;
		firstNode = newNode;
		numOfElements++;
	}

	public int size() {
		return this.numOfElements;
	}
	
	public void addtoSet(E start, E end) {
		Node<E> currentNode = firstNode;
		int counter = 0;
		while(currentNode != null) {
			if(containsRelationshipElements(currentNode, start, end)) {
				counter++;
			}	
			currentNode = currentNode.next;
		}
		if(counter == 0){
			add(start, end);
		}
	}
	
	private boolean containsRelationshipElements(Node<E> currentNode, E start, E end) {
		if(currentNode.startNode.compareTo(start) == 0 
				&& currentNode.endNode.compareTo(end)== 0)
			return true;
		return false;
	}

	
	protected class Node<E>{
		public E startNode;
		public E endNode;
		public Node<E> next;
		
		public Node(E startNode, E endNode){
			this.startNode = startNode;
			this.endNode = endNode;
			this.next = null;
		}
	}
	
	public List<Relationships> getRelationshipsList() {
		Node<E> currentNode = firstNode;
		while(currentNode != null) {
			//System.out.println(currentNode.startNode +"::"+ currentNode.endNode);
			relList.add((Relationships) new Relationships(currentNode.startNode.toString(), currentNode.endNode.toString()));
			currentNode = currentNode.next;
		}
		return relList;
		//System.out.println("***************");
	}
	
	public static void main(String[] arg) {
		RelationshipLinkedSet relationshipLinkedSet = new RelationshipLinkedSet<Comparable>();
		
		
		long startTime = System.currentTimeMillis();
		for(int i =0; i < 1000000000; i++) {
		relationshipLinkedSet.addtoSet("1_2", "1_3");
		relationshipLinkedSet.addtoSet("3_2", "3_3");
		relationshipLinkedSet.addtoSet("1_2", "1_3");
		
		relationshipLinkedSet.addtoSet("2_2", "2_3");
		relationshipLinkedSet.addtoSet("2_2", "2_3");
		relationshipLinkedSet.addtoSet("3_2", "3_3");
		relationshipLinkedSet.addtoSet("3_2", "3_3");
		relationshipLinkedSet.addtoSet("3_2", "3_3");
		relationshipLinkedSet.addtoSet("3_2", "3_3");
		relationshipLinkedSet.addtoSet("2_2", "2_3");
		relationshipLinkedSet.addtoSet("1_2", "1_3");
		relationshipLinkedSet.addtoSet("1_2", "1_3");
		relationshipLinkedSet.addtoSet("3_2", "1_4");
		relationshipLinkedSet.addtoSet("3_2", "1_4");
		relationshipLinkedSet.addtoSet("3_2", "1_4");
		}
		relationshipLinkedSet.addtoSet("chandan", "sudha");
		relationshipLinkedSet.addtoSet("chandan", "sudha");
		//System.out.println(relationshipLinkedSet.size());
		
		//relationshipLinkedSet.display();
		System.out.println("Start time -->"+startTime);
		long startMinutes = TimeUnit.MILLISECONDS.toSeconds(startTime);
		long endTime = System.currentTimeMillis();
		long endMinutes = TimeUnit.MILLISECONDS.toSeconds(endTime);
		System.out.println("Start Time-->" + startMinutes +" End time -->" + endMinutes);
	}
}
