package com.asemonash.helper;

public class DiagramNode<E> {
	private String id;
	private E name;
	private Label label;
	private E subLabel;
	private String alias;
	private String participant;
	private String organization;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public E getName() {
		return name;
	}
	public void setName(E name) {
		this.name = name;
	}
	
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public E getSubLabel() {
		return subLabel;
	}
	public void setSubLabel(E subLabel) {
		this.subLabel = subLabel;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	@Override
	public String toString() {
		return "DiagramNode [id=" + id + ", name=" + name + ", label=" + label + ", subLabel=" + subLabel + ", alias="
				+ alias + ", participant=" + participant + ", organization=" + organization + "]";
	}
	
	
	
	
	
	
	
}
