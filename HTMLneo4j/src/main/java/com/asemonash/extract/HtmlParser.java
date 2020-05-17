package com.asemonash.extract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.SocketUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.asemonash.helper.DiagramEdge;
import com.asemonash.helper.DiagramNode;
import com.asemonash.helper.DiagramType;
import com.asemonash.helper.Label;
import com.asemonash.helper.Relationships;
import com.asemonash.transform.QueryStringBuilder;


public class HtmlParser<E> {
	
	private File htmlFile;
	private Document document;
	private DiagramNode<E> diagramNode;
	private DiagramEdge<E> diagramEdge;
	private boolean isNode;
	private List<DiagramNode<E>> diagramNodesList;
	private List<DiagramEdge<E>> diagramEdgesList;
	private List<Relationships> relationshipsList;
	private QueryStringBuilder queryBuilder;
	public boolean isProcessDiagram = false;
	//private static <> List<DiagramNode<E>> listDiagramNodes;
	//private List<E> graphElementsList;
	/**
	 * afterCounter and beforeCounter are debug
	 * statements. Remove in the final version
	 */
	//private int afterCounter, beforeCounter = 0;
	
	public HtmlParser(File file) {
		this.htmlFile = file;
		diagramNodesList = new LinkedList<DiagramNode<E>>();
		diagramEdgesList = new LinkedList<DiagramEdge<E>>();
		//graphElementsList = new LinkedList<E>();
		relationshipsList = new LinkedList<Relationships>();
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
					if(diagramNode.getId() == null) {
						//A DEBUG CHECK TO FIND THE NODES THAT HAVE id ASSIGNED AS NULL
						//System.out.println(diagramNode);
						System.out.println("The diagram nodes with null id "+diagramNode);
						continue;
					}
					else {
						diagramNodesList.add(diagramNode);
						//graphElementsList.add((E) diagramNode);
					}
				}
				else {
					diagramEdgesList.add(diagramEdge);
					//graphElementsList.add((E)diagramEdge);
				}
				//beforeCounter++;
			}
			
			createRelationships(document, diagramNodesList);
			//displayRelatiosnhips();
//			for(Relationships r: relationshipsList) {
//				if(!(r.getInRelationship().equalsIgnoreCase(": Arrowed Connector"))) {
//					System.out.println(r.getStartNode() +"::"+ r.getEndNode());
//				}
//			}
			
			queryBuilder = new QueryStringBuilder();
			queryBuilder.setRelationshipsList(this.getRelationshipsList());
			queryBuilder.setDiagramNodesList(this.getDiagramNodesList());
			queryBuilder.initQueryBuilder();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractDataFrmAttr(Attributes attributes) {
		
		
		for(Attribute attribute: attributes.asList()) {

			if(attribute.getKey().equalsIgnoreCase("alt") && 
				(attribute.getValue().equalsIgnoreCase("from")|| 
						attribute.getValue().equalsIgnoreCase("to") || 
						attribute.getValue().equalsIgnoreCase("CONDITION"))){
					if(attribute.getValue().equalsIgnoreCase("from")) {
						//diagramEdge.setLabel(Label.FROM);
					}
					else {
						//diagramEdge.setLabel(Label.TO);
					}
					isNode = false;
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().equalsIgnoreCase(": START") ||
					attribute.getValue().equalsIgnoreCase(": END")) {
				
				isNode = true;
				if(attribute.getValue().equalsIgnoreCase(": START")) {
					String name = attribute.getValue();
					diagramNode.setLabel(Label.START);
					diagramNode.setName((E) name.replaceAll("[: ]", ""));
					diagramNode.setAlias("START");
					
					//System.out.println(" in START");
					//System.out.println(attribute.getKey());
				}
				else {
					String name = attribute.getValue();
					diagramNode.setLabel(Label.END);
					diagramNode.setName((E) name.replaceAll("[: ]", ""));
					diagramNode.setAlias("END");
					//System.out.println(" in END");
					//System.out.println(name);
				}
				isProcessDiagram = true;
				DiagramType.setDiagramType(isProcessDiagram);
				
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") && 
					(attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase()))) {
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase())) {
					String name = attribute.getValue();
					diagramNode.setName((E) name);
					diagramNode.setLabel(Label.OPERATION);
					//System.out.println(name);
					
				}
				
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") && 
					attribute.getValue().equalsIgnoreCase("Condition correct") ||
					attribute.getValue().equalsIgnoreCase("condition incorrect")) {
				isNode = true;
				if(attribute.getValue().equalsIgnoreCase("Condition correct")) {
					String name = attribute.getValue();
					diagramNode.setName((E) name);
					diagramNode.setLabel(Label.CONDITION_CORRECT);
					diagramNode.setAlias("YES");
					System.out.println("correct-->" + diagramNode);
				}
				else {
					String name = attribute.getValue();
					diagramNode.setName((E) name);
					diagramNode.setLabel(Label.CONDITION_INCORRECT);
					diagramNode.setAlias("NO");
					System.out.println("Incorrect-->" + diagramNode);
				}
				
				isProcessDiagram = true;
				DiagramType.setDiagramType(isProcessDiagram);
			}
			
			else if (isProcessDiagram == true && attribute.getKey().equalsIgnoreCase("alt") && 
					(attribute.getValue().contains("?"))) {
				//System.out.println("conditional check");
				isNode = true;
				String name = attribute.getValue();
				diagramNode.setName((E) name);
				diagramNode.setLabel(Label.CONDITIONAL_CHECK);
				diagramNode.setAlias(name.replaceAll("[-/()?: ]", ""));
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") && 
					(attribute.getValue().toLowerCase().contains("PROBLEM DEFINITION".toLowerCase()))) {
				isNode = true;
				diagramNode.setLabel(Label.DAP);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));

				diagramNode.setName((E)subValue);
				//System.out.println(subValue);
				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().equalsIgnoreCase("INITIAL STEP") ||
					attribute.getValue().equalsIgnoreCase("STEPS")) {
				
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("INITIAL STEP".toLowerCase())) {
					diagramNode.setLabel(Label.INITIAL_STEP);
					String name = attribute.getValue();
					diagramNode.setName((E) name);
					//System.out.println(name);
				}
				else {
					diagramNode.setLabel(Label.STEPS);
					String name = attribute.getValue();
					diagramNode.setName((E) name);
					//System.out.println(name);
				}
			}
			
		
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase()) ||
					attribute.getValue().toLowerCase().contains("LOW LEVEL".toLowerCase())) {
				
				isNode = true;
				if(attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase())) {
					String name = attribute.getValue();
					diagramNode.setLabel(Label.HIGH_LEVEL);
					diagramNode.setName((E) name);
				}
				else {
					String name = attribute.getValue();
					diagramNode.setLabel(Label.LOW_LEVEL);
					diagramNode.setName((E) name);
				}
			}
			
			else if(attribute.getKey().equalsIgnoreCase("alt") &&
					attribute.getValue().toLowerCase().contains("TASK".toLowerCase())) {
				
				isNode = true;
				diagramNode.setLabel(Label.TASK);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));
				diagramNode.setName((E)subValue);
				//System.out.println(subValue);
				System.out.println("The value is-->" + subValue +" ||is process "+ isProcessDiagram);
				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
				if(isProcessDiagram) {
					//System.out.println("process diagram nodes-->" + subValue);
					if(subValue.equalsIgnoreCase("Co-design of intervention") || 
							subValue.equalsIgnoreCase("Pilot intervention") ||
							subValue.equalsIgnoreCase("Finalising SMS intervention")) {
						diagramNode.setOrganization("Investigator");
						diagramNode.setParticipant("University of Melbourne/ Monash University/ PenCS/ Healthily/ Western Vic Primary Health Network/ Consumers \n");
					}
					else if(subValue.equalsIgnoreCase("Generate dataset of eligible patients") || 
							subValue.equalsIgnoreCase("Sending SMS intervention to patients")) {
						diagramNode.setOrganization("Hospital/Clinic");
						diagramNode.setParticipant("General Practitioner");
					}
					else if(subValue.equalsIgnoreCase("Receive report from EMR") || 
							subValue.equalsIgnoreCase("Receive SMS prior to receive NBCSP kit")) {
						diagramNode.setOrganization("Users");
						diagramNode.setParticipant("Patient");
						
					}
					else if(subValue.equalsIgnoreCase("Remove outliers") || 
							subValue.equalsIgnoreCase("Wrangle data") ||
							subValue.equalsIgnoreCase("Acquire data") || 
							subValue.equalsIgnoreCase("Impute missing value") ||
							subValue.equalsIgnoreCase("Develop AI model") ) {
						diagramNode.setOrganization("Technology Provider");
						diagramNode.setParticipant("Data Scientists");
					}
					else if (subValue.equalsIgnoreCase("Deploy AI model in clinical software")) {
						diagramNode.setOrganization("Technology Provider");
						diagramNode.setParticipant("Software engineer");
					}
				}
			}
			else if(attribute.getKey().equalsIgnoreCase("alt")) {
				
				
				diagramNode.setLabel(Label.OTHER);
				String value = attribute.getValue();
				String subValue = value.substring(0, value.indexOf(":"));
				String subLabel = value.substring(value.indexOf(":"), value.length());
				diagramNode.setName((E)subValue);
				diagramNode.setSubLabel((E)subLabel);
				
				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
				isNode = true;
				//System.out.println(attribute.getKey() +"-->"+ attribute.getValue() +"--"+ isNode);
				
			}
			
			if(attribute.getKey().equalsIgnoreCase("href")) {
				
				//System.out.println(attribute.getKey() +"-->"+ attribute.getValue() +"--"+ isNode);
				
				//if(isNode == true) {
					String nodeID = attribute.getValue().substring(1);
					//System.out.println("in htmlParser"+nodeID);
					//System.out.println(diagramNode);
					
					diagramNode.setId(nodeID);
					
				//}
//				else {
					String edgeID = attribute.getValue().substring(1);
					//diagramEdge.setId((E) edgeID);
//				}
			}
			if(attribute.getKey().equalsIgnoreCase("nohref")) {
				continue;
			}
		}
		
		//afterCounter++;
	}
	
//	private void extractDataFrmAttr(Attributes attributes) {
//	
//		for(Attribute attribute: attributes.asList()) {
//
//			if(attribute.getKey().equalsIgnoreCase("alt") && 
//				(attribute.getValue().equalsIgnoreCase("from")|| 
//						attribute.getValue().equalsIgnoreCase("to"))){
//					if(attribute.getValue().equalsIgnoreCase("from")) {
//						diagramEdge.setLabel(Label.FROM);
//					}
//					else {
//						diagramEdge.setLabel(Label.TO);
//					}
//					isNode = false;
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") && 
//					(attribute.getValue().toLowerCase().contains("CONDITION".toLowerCase())|| 
//							attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase()))) {
//				isNode = true;
//				if(attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase())) {
//					String name = attribute.getValue();
//					diagramNode.setName((E) name);
//					diagramNode.setLabel(Label.OPERATION);
//					//System.out.println(name);
//					
//				}
//				else {
//					String name = attribute.getValue();
//					diagramNode.setLabel(Label.CONDITION);
//					diagramNode.setName((E) name);
//					//System.out.println(name);
//				}
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") && 
//					(attribute.getValue().toLowerCase().contains("PROBLEM DEFINITION".toLowerCase()))) {
//				isNode = true;
//				diagramNode.setLabel(Label.DAP);
//				String value = attribute.getValue();
//				String subValue = value.substring(0, value.indexOf(":"));
//
//				diagramNode.setName((E)subValue);
//				//System.out.println(subValue);
//				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") &&
//					attribute.getValue().equalsIgnoreCase("INITIAL STEP") ||
//					attribute.getValue().equalsIgnoreCase("STEPS")) {
//				
//				isNode = true;
//				if(attribute.getValue().toLowerCase().contains("INITIAL STEP".toLowerCase())) {
//					diagramNode.setLabel(Label.INITIAL_STEP);
//					String name = attribute.getValue();
//					diagramNode.setName((E) name);
//					//System.out.println(name);
//				}
//				else {
//					diagramNode.setLabel(Label.STEPS);
//					String name = attribute.getValue();
//					diagramNode.setName((E) name);
//					//System.out.println(name);
//				}
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") &&
//					attribute.getValue().equalsIgnoreCase("START") ||
//					attribute.getValue().equalsIgnoreCase("END")) {
//				
//				isNode = true;
//				if(attribute.getValue().equalsIgnoreCase("START")) {
//					String name = attribute.getValue();
//					diagramNode.setLabel(Label.START);
//					diagramNode.setName((E) name);
//					//System.out.println(name);
//				}
//				else {
//					String name = attribute.getValue();
//					diagramNode.setLabel(Label.END);
//					diagramNode.setName((E) name);
//					//System.out.println(name);
//				}
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") &&
//					attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase()) ||
//					attribute.getValue().toLowerCase().contains("LOW LEVEL".toLowerCase())) {
//				
//				isNode = true;
//				if(attribute.getValue().toLowerCase().contains("HIGH LEVEL".toLowerCase())) {
//					String name = attribute.getValue();
//					diagramNode.setLabel(Label.HIGH_LEVEL);
//					diagramNode.setName((E) name);
//				}
//				else {
//					String name = attribute.getValue();
//					diagramNode.setLabel(Label.LOW_LEVEL);
//					diagramNode.setName((E) name);
//				}
//			}
//			
//			else if(attribute.getKey().equalsIgnoreCase("alt") &&
//					attribute.getValue().toLowerCase().contains("TASK".toLowerCase())) {
//				
//				isNode = true;
//				diagramNode.setLabel(Label.TASK);
//				String value = attribute.getValue();
//				String subValue = value.substring(0, value.indexOf(":"));
//				diagramNode.setName((E)subValue);
//				//System.out.println(subValue);
//				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
//			}
//			else if(attribute.getKey().equalsIgnoreCase("alt")) {
//				
//				
//				diagramNode.setLabel(Label.SUB_TASK);
//				String value = attribute.getValue();
//				String subValue = value.substring(0, value.indexOf(":"));
//				String subLabel = value.substring(value.indexOf(":"), value.length());
//				diagramNode.setName((E)subValue);
//				diagramNode.setSubLabel((E)subLabel);
//				
//				diagramNode.setAlias(subValue.replaceAll("[-/() ]", ""));
//				isNode = true;
//				//System.out.println(attribute.getKey() +"-->"+ attribute.getValue() +"--"+ isNode);
//				
//			}
//			
//			if(attribute.getKey().equalsIgnoreCase("href")) {
//				
//				//System.out.println(attribute.getKey() +"-->"+ attribute.getValue() +"--"+ isNode);
//				
//				//if(isNode == true) {
//					String nodeID = attribute.getValue().substring(1);
//					//System.out.println("in htmlParser"+nodeID);
//					//System.out.println(diagramNode);
//					
//					diagramNode.setId(nodeID);
//					
//				//}
////				else {
//					String edgeID = attribute.getValue().substring(1);
//					diagramEdge.setId((E) edgeID);
////				}
//			}
//			if(attribute.getKey().equalsIgnoreCase("nohref")) {
//				continue;
//			}
//		}
//		
//		afterCounter++;
//	}
	
	
	private void createRelationships(Document htmlDoc, List<DiagramNode<E>> graphElementsList) {
		Map<String, Elements> map = new HashMap();
		List<String> mapKeys = new LinkedList<String>();
	
		for (DiagramNode<E> element : diagramNodesList) {
			if(((DiagramNode<E>) element).getId() != null ) { 
				String id = (String) ((DiagramNode<E>) element).getId();
				String divID = "div#"+ id;
				String rTable = divID + "_RelationshipsTable";
				Elements innerrelTable = htmlDoc.select(rTable);
				String name = (String) ((DiagramNode<E>) element).getName();
				mapKeys.add(id);
				for(Element e : innerrelTable) {
					Elements innerEle = e.getElementsByTag("tr");
					map.put(id, innerEle);
				}
			}
		}
		for(String key: mapKeys) {
			Elements vElements = map.get(key);
			//THIS NULL CHECK BECAUSE THERE ARE FEW ELEMENTS THAT HAVE EMPTY div ELEMENTS IN THE HTML
			if(vElements != null) {
				for(Element ev : vElements) {
					calculateRelationships(key, ev.childNodes());
				}
			}
		}
	}
	
	private void calculateRelationships(String key, List<Node> nodeList) {

		Relationships relationships = new Relationships();
		relationships.setStartNode(key);
		String startNodeData = (String) getDiagramNode(key).getName();
		relationships.setStartNodeData(startNodeData);
		
		int i = 0;
		for(Node node : nodeList) {
			Element element = (Element)node;
			if(element.getElementsByTag("td").toString().contains("td")){
				if(element.select("td").toString().contains("href") && i == 2) {
					String endNode = element.select("td").select("a").attr("href").substring(1);
					relationships.setEndNode(endNode);
					relationships.setEndNodeData(element.getElementsByTag("td").text());
				}
				else {
					if(i == 0) {
						relationships.setInRoleHeader1(element.getElementsByTag("td").text());
					}
					else if(i == 1) {
						//System.out.println(element.getElementsByTag("td").text());
						relationships.setInRelationship(element.getElementsByTag("td").text());
					}
					
					else if (i == 3) {
						relationships.setInRoleHeader2(element.getElementsByTag("td").text());
					}
				}
			}
			i++;
		}
		if(relationships.getEndNode()!= null && relationships.getEndNodeData() != null) {
			relationshipsList.add(relationships);
		}
	}
	

//	private void rowCounterDebugFunc() {
//		System.out.println("Rows before filtering-->" + beforeCounter);
//		System.out.println("******************");
//		System.out.println("Rows after filtering-->"+ afterCounter);
//	}
	
	
	private void displayRelatiosnhips() {

		for(Relationships rel: relationshipsList) {
			System.out.println("Start Node " + getDiagramNode(rel.getStartNode()).getId() + rel.getInRoleHeader1() + "\n" 
								+ "End Node " + getDiagramNode(rel.getEndNode()).getId());
			System.out.println("*************");
		}
	}
	
	private DiagramNode<E> getDiagramNode(String nodeID){
		
		
		DiagramNode<E> diagramNode = null;
		for(DiagramNode<E> d : diagramNodesList) {
			if(d.getId().equals(nodeID)) {
				diagramNode = d;
			}
		}
		return diagramNode;	
	}

	public List<Relationships> getRelationshipsList() {
		return relationshipsList;
	}

	public List<DiagramNode<E>> getDiagramNodesList() {
		return diagramNodesList;
	}
	
}
