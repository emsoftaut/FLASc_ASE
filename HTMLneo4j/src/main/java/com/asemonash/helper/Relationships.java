package com.asemonash.helper;

public class Relationships {
	private String startNode;
	private String startNodeData;
	private String inRoleHeader1;
	private String endNodeData;
	private String inRoleHeader2;
	private String endNode;
	private String inRelationship;
	
	
	
	public Relationships() {
		super();
	}
	public Relationships(String startNode, String endNode, String inRelationship) {
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.inRelationship = inRelationship;
	}
	
	public String getInRelationship() {
		return inRelationship;
	}
	public void setInRelationship(String inRelationship) {
		this.inRelationship = inRelationship;
	}
	public String getStartNode() {
		return startNode;
	}
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	public String getStartNodeData() {
		return startNodeData;
	}
	public void setStartNodeData(String startNodeData) {
		this.startNodeData = startNodeData;
	}
	public String getInRoleHeader1() {
		return inRoleHeader1;
	}
	public void setInRoleHeader1(String inRoleHeader1) {
		this.inRoleHeader1 = inRoleHeader1;
	}
	public String getEndNodeData() {
		return endNodeData;
	}
	public void setEndNodeData(String endNodeData) {
		this.endNodeData = endNodeData;
	}
	public String getInRoleHeader2() {
		return inRoleHeader2;
	}
	public void setInRoleHeader2(String inRoleHeader2) {
		this.inRoleHeader2 = inRoleHeader2;
	}
	public String getEndNode() {
		return endNode;
	}
	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}
	@Override
	public String toString() {
		return "Relationships [startNode=" + startNode + ", startNodeData=" + startNodeData + ", inRoleHeader1="
				+ inRoleHeader1 + ", endNodeData=" + endNodeData + ", inRoleHeader2=" + inRoleHeader2 + ", endNode="
				+ endNode + ", inRelationship=" + inRelationship + "]";
	}
	
	
	
	
	
	
}
