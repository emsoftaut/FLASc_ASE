package com.asemonash.controller;

public class DiagramNode<E> {
	private String id;
	private E name;
	private Label label;
	private E subLabel;
	private String alias;
	
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
	@Override
	public String toString() {
		return "DiagramNode [id=" + id + ", name=" + name + ", label=" + label + ", subLabel=" + subLabel + ", alias="
				+ alias + "]";
	}
	
	
	
	
	
	
	
}
