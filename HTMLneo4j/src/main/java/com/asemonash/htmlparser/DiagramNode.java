package com.asemonash.htmlparser;

public class DiagramNode<E> {
	private E id;
	private E name;
	private Label label;
	
	
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
	@Override
	public String toString() {
		return "DiagramNode [id=" + id + ", name=" + name + ", label=" + label + "]";
	}
	
	
	
	
}
