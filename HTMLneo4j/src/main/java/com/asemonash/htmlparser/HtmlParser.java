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
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class HtmlParser<E> {
	
	private File htmlFile;
	private Document document;
	private DiagramNode<E> diagramNode;
	private DiagramEdge<E> diagramEdge;
	private boolean isNode;
//	private List<DiagramNode<E>> diagramNodesList;
//	private List<DiagramEdge<E>> diagramEdgesList;
	private List<E> graphElementsList;
	/**
	 * afterCounter and beforeCounter are debug
	 * statements. Remove in the final version
	 */
	private int afterCounter, beforeCounter = 0;
	
	public HtmlParser(File file) {
		this.htmlFile = file;
//		diagramNodesList = new LinkedList<DiagramNode<E>>();
//		diagramEdgesList = new LinkedList<DiagramEdge<E>>();
		graphElementsList = new LinkedList<E>();
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
				extractDataFrmAttr(areaTagElement.attributes());
				if(isNode) {
					//diagramNodesList.add(diagramNode);
					graphElementsList.add((E) diagramNode);
				}
				else {
					//diagramEdgesList.add(diagramEdge);
					graphElementsList.add((E)diagramEdge);
				}
				beforeCounter++;
			}
			
			createRelationships(document, graphElementsList);
			//rowCounterDebugFunc();
			//displayNodeList();
			//System.out.println("******************");
			//displayEdgeList();
			
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
					(attribute.getValue().toLowerCase().contains("PROBLEM DEFINITION".toLowerCase()))) {
				isNode = true;
				diagramNode.setLabel(Label.DAP);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));
				
				//String subLabel = value.substring(value.indexOf(":"), value.length());
				diagramNode.setName((E)subValue);
				//diagramNode.setSubLabel((E)subLabel);
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("INITIAL STEP".toLowerCase()) ||
					attribute.getValue().toLowerCase().contains("STEPS".toLowerCase())) {
				
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
					attribute.getValue().toLowerCase().contains("START".toLowerCase()) ||
					attribute.getValue().toLowerCase().contains("END".toLowerCase())) {
				
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("START".toLowerCase())) {
					diagramNode.setLabel(Label.START);
					diagramNode.setName((E) attribute.getValue());
				}
				else {
					diagramNode.setLabel(Label.END);
					diagramNode.setName((E) attribute.getValue());
				}
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase()) ||
					attribute.getValue().toLowerCase().contains("LOW LEVEL".toLowerCase())) {
				
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase())) {
					diagramNode.setLabel(Label.HIGH_LEVEL);
					diagramNode.setName((E) attribute.getValue());
				}
				else {
					diagramNode.setLabel(Label.LOW_LEVEL);
					diagramNode.setName((E) attribute.getValue());
				}
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("TASK".toLowerCase())) {
				
				isNode = true;
				diagramNode.setLabel(Label.TASK);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));
				diagramNode.setName((E)subValue);
			}
			else if(attribute.getKey().equalsIgnoreCase("alt")) {
				
				isNode = true;
				diagramNode.setLabel(Label.SUB_TASK);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));
				String subLabel = value.substring(value.indexOf(":"), value.length());
				diagramNode.setName((E)subValue);
				diagramNode.setSubLabel((E)subLabel);
			}
			
			if(attribute.getKey().equalsIgnoreCase("href")) {
				if(isNode == true) {
					String nodeID = attribute.getValue().substring(1);
					diagramNode.setId((E) nodeID);
					
				}
				else {
					String edgeID = attribute.getValue().substring(1);
					diagramEdge.setId((E) edgeID);
				}
			}
		}
		
		afterCounter++;
	}
	
	
	private void createRelationships(Document htmlDoc, List<E> graphElementsList) {
		
		Elements divElement = htmlDoc.getElementsByTag("div");
		//System.out.println(divElement);
		
		Elements dElements = htmlDoc.select("div#3_20101");
		
		List<Node> eList = null;
		for(Element element : dElements) {
			eList = element.childNodes();
			//System.out.println(eList);
		}
		
		
		
		
		for(E element: graphElementsList) {
			
			//System.out.println(element.getClass().toString().contains("DiagramNode"));
			//System.out.println("ELEMENT " + element);
			if(element.getClass().toString().contains("DiagramNode")) {
				
//				System.out.println("The sub Label is-->"+((DiagramNode<E>) element).getSubLabel() +
//						" The Label is-->"+ ((DiagramNode<E>) element).getLabel() + 
//						" THE NAME IS " + ((DiagramNode<E>) element).getName());
				
			}
			//System.out.println("The element is-->" + ((DiagramNode<E>) element).getSubLabel());
		}
		
		
		
		for (E element : graphElementsList) {
			if(element.getClass().toString().toLowerCase().contains("DIAGRAMNODE".toLowerCase()) 
					&& ((DiagramNode<E>) element).getId() != null ) { 
				//THE NOT NULL CHECK IS TEMPORARY REMOVE AFTER RESOLVING
				//System.out.println("div#"+ ((DiagramNode<E>) element).getId());
				String id = (String) ((DiagramNode<E>) element).getId();
				String divID = "div#"+ id;
				//Elements eTest = htmlDoc.select(divID);
				String rTable = divID + "_RelationshipsTable";
				Elements innerrelTable = htmlDoc.select(rTable);
				String name = (String) ((DiagramNode<E>) element).getName();
				for(Element e : innerrelTable) {
					System.out.println(name+" --> "+e.getElementsByTag("tr"));
				}
				//System.out.println(id+"\n"+ innerrelTable);
				
				
				
				//Document innerHtmlTable = Jsoup.parse(innerrelTable);
				
				//System.out.println(innerHtmlTable);
				
				System.out.println("**********************");
			}
		}
		
	}
	
	private void rowCounterDebugFunc() {
		System.out.println("Rows before filtering-->" + beforeCounter);
		System.out.println("******************");
		System.out.println("Rows after filtering-->"+ afterCounter);
	}
	
//	public void displayNodeList() {
//		for(DiagramNode<E> nodes: diagramNodesList) {
//			System.out.println(nodes);
//		}
//	}
// 	
//	public void displayEdgeList() {
//		for(DiagramEdge<E> edges: diagramEdgesList) {
//			System.out.println(edges);
//		}
//	}
}
