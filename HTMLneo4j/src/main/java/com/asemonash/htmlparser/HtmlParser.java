package com.asemonash.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlParser<E> {
	
	private File htmlFile;
	private Document document;
	private DiagramNode<E> diagramNode;
	private DiagramEdge<E> diagramEdge;
	private boolean isNode;
	private List<DiagramNode<E>> diagramNodesList;
	private List<DiagramEdge<E>> diagramEdgesList;
	/**
	 * afterCounter and beforeCounter are debug
	 * statements. Remove in the final version
	 */
	private int afterCounter, beforeCounter = 0;
	
	public HtmlParser(File file) {
		this.htmlFile = file;
		diagramNodesList = new LinkedList<DiagramNode<E>>();
		diagramEdgesList = new LinkedList<DiagramEdge<E>>();
	}
	
	public void initHtmlParser() {
		try {
			document = Jsoup.parse(htmlFile, "UTF-8", "");
			Elements map = document.getElementsByTag("map");
			String mapHtml = map.toString();
			Document mapHtmlDoc = Jsoup.parse(mapHtml);
			Elements areaTagElements = mapHtmlDoc.getElementsByTag("area");

			for(Element areaTagElement : areaTagElements) {
				diagramNode = new DiagramNode<E>();
				diagramEdge = new DiagramEdge<E>();
				//isNode = true;
				extractDataFrmAttr(areaTagElement.attributes());
				if(isNode) {
					diagramNodesList.add(diagramNode);
				}
				else {
					diagramEdgesList.add(diagramEdge);
				}
				//System.out.println("************");
//				System.out.println("NODE "+diagramNode);
//				System.out.println("EDGE "+diagramEdge);
				beforeCounter++;
			}
			
			//rowCounterDebugFunc();
			displayNodeList();
			System.out.println("******************");
			displayEdgeList();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractDataFrmAttr(Attributes attributes) {
	
		
		
		for(Attribute attribute: attributes.asList()) {

			if(attribute.getKey().equalsIgnoreCase("alt") && 
				(attribute.getValue().equalsIgnoreCase("from")|| 
						attribute.getValue().equalsIgnoreCase("to"))){
					if(attribute.getValue().equalsIgnoreCase("from")) {
						diagramEdge.setLabel(Label.FROM);
					}
					else {
						diagramEdge.setLabel(Label.TO);
					}
					isNode = false;
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") && 
					(attribute.getValue().toLowerCase().contains("CONDITION".toLowerCase())|| 
							attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase()))) {
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase())) {
					
					diagramNode.setLabel(Label.OPERATION);
					diagramNode.setName((E) attribute.getValue());
				}
				else {
					diagramNode.setLabel(Label.CONDITION);
					diagramNode.setName((E) attribute.getValue());
				}
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("INITIAL STEP".toLowerCase()) ||
					attribute.getValue().toLowerCase().contains("STEPS".toLowerCase())) {
				//System.out.println("INITIAL STEP-->" + attribute.getValue());
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("INITIAL STEP".toLowerCase())) {
					diagramNode.setLabel(Label.INITIAL_STEP);
					diagramNode.setName((E) attribute.getValue());
				}
				else {
					diagramNode.setLabel(Label.STEPS);
					diagramNode.setName((E) attribute.getValue());
				}
			}
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("TASK".toLowerCase())) {
				//System.out.println("TASKS-->" + attribute.getValue());
				isNode = true;
				diagramNode.setLabel(Label.TASK);
				diagramNode.setName((E)attribute.getValue());
			}
			else if(attribute.getKey().equalsIgnoreCase("alt")) {
				//System.out.println("SUB TASKS-->" + attribute.getValue());
				isNode = true;
				diagramNode.setLabel(Label.SUB_TASK);
				diagramNode.setName((E)attribute.getValue());
			}
			
			if(attribute.getKey().equalsIgnoreCase("href")) {
				if(isNode == true) {
					String nodeID = attribute.getValue().substring(1);
					diagramNode.setId((E) nodeID);
					//System.out.println("Node-->" + diagramNode);
				}
				else {
					String edgeID = attribute.getValue().substring(1);
					diagramEdge.setId((E) edgeID);
					//System.out.println("Edge-->" + diagramEdge);
				}
			}
		}
		//isNode = true;
		afterCounter++;
		
		//System.out.println(it.getAttributes());
		
//				if(areaTagElement.hasAttr("href") && areaTagElement.hasAttr("alt")) {
//					System.out.println(areaTagElement.attr("href") +"--"+ areaTagElement.attr("alt"));
//					afterCounter++;
//				}
		//extractData(areaTagElement);
	}
	
	
	private void rowCounterDebugFunc() {
		System.out.println("Rows before filtering-->" + beforeCounter);
		System.out.println("******************");
		System.out.println("Rows after filtering-->"+ afterCounter);
	}
	
	public void displayNodeList() {
		for(DiagramNode<E> nodes: diagramNodesList) {
			System.out.println(nodes);
		}
	}
 	
	public void displayEdgeList() {
		for(DiagramEdge<E> edges: diagramEdgesList) {
			System.out.println(edges);
		}
	}
}
