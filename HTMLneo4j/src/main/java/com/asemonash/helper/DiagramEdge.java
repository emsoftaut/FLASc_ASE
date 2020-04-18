package com.asemonash.helper;

public class DiagramEdge<E> {
	private E id;
	private Label label;
		
	public E getId() {
		return id;
	}

	public void setId(E id) {
		this.id = id;
	}

	public Label getLabel() {
		return label;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "DiagramEdge [id=" + id + ", label=" + label + "]";
	}
	
	

}
