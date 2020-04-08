package com.asemonash.htmlparser;

public class DiagramNode<E> {
	private E id;
	private E name;
	private Label label;
	private E subLabel;
	
	
	public E getId() {
		return id;
	}
	public void setId(E id) {
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
		return "DiagramNode [id=" + id + ", name=" + name + ", label=" + label + ", subLabel=" + subLabel + "]";
	}
	
	
	
	
	
}
