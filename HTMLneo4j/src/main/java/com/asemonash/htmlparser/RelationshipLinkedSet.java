package com.asemonash.htmlparser;

import java.util.AbstractSet;
import java.util.Iterator;

public class RelationshipLinkedSet<E extends Comparable> extends AbstractSet<E> {

	private Node<E> currentNode;
	private Node<E> firstNode;
	private int numOfElements;
	
	public RelationshipLinkedSet() {
		// TODO Auto-generated constructor stub
		firstNode = null;
		currentNode = null;
		numOfElements = 0;
	}
	
	public void add(E startNode, E endNode) {
		Node newNode = new Node<E>(startNode, endNode);
		if(firstNode == null) {
			
		}
	}
	
	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return this.numOfElements;
	}
	
	public boolean containsRelationshipElements(E start, E end) {
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
}
