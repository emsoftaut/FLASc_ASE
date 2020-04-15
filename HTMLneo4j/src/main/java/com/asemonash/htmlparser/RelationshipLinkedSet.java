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

	public List<Relationships> getRelationshipsList() {
		Node<E> currentNode = firstNode;
		while(currentNode != null) {
			relList.add((Relationships) new Relationships(currentNode.startNode.toString(), currentNode.endNode.toString()));
			currentNode = currentNode.next;
		}
		return relList;
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
}
