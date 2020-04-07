package com.asemonash.htmlparser;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.css.ElementCSSInlineStyle;


public class HtmlParser<E> {
	
	private File htmlFile;
	private Document document;
	private DiagramNode<E> diagramNode;
	/**
	 * afterCounter and beforeCounter are debug
	 * statements. Remove in the final version
	 */
	private int afterCounter, beforeCounter = 0;
	
	public HtmlParser(File file) {
		this.htmlFile = file;
	}
	
	public void initHtmlParser() {
		try {
			document = Jsoup.parse(htmlFile, "UTF-8", "");
			Elements map = document.getElementsByTag("map");
			String mapHtml = map.toString();
			Document mapHtmlDoc = Jsoup.parse(mapHtml);
			Elements areaTagElements = mapHtmlDoc.getElementsByTag("area");

			for(Element areaTagElement : areaTagElements) {
				extractDataFrmAttr(areaTagElement.attributes());
				System.out.println("************");
				beforeCounter++;
			}
			
			rowCounterDebugFunc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractDataFrmAttr(Attributes attributes) {
		diagramNode = new DiagramNode<E>();
		
		for(Attribute attribute: attributes.asList()) {
			//System.out.println(attribute.getKey() +"::"+ attribute.getValue());
			if(attribute.getKey().equalsIgnoreCase("alt") && 
				(attribute.getValue().equalsIgnoreCase("from")|| 
						attribute.getValue().equalsIgnoreCase("to"))){
					System.out.println("Edge");
			}
			else if(attribute.getKey().equalsIgnoreCase("alt") && 
					(attribute.getValue().toLowerCase().contains("CONDITION".toLowerCase())|| 
							attribute.getValue().toLowerCase().contains("OPERATION".toLowerCase()))) {
				System.out.println("CONDITION/OPERATION-->"+ attribute.getValue());
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
				System.out.println("INITIAL STEP-->" + attribute.getValue());
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
				System.out.println("TASKS-->" + attribute.getValue());
				diagramNode.setLabel(Label.TASK);
				diagramNode.setName((E)attribute.getValue());
			}
			else if(attribute.getKey().equalsIgnoreCase("alt")) {
				System.out.println("SUB TASKS-->" + attribute.getValue());
				diagramNode.setLabel(Label.SUB_TASK);
				diagramNode.setName((E)attribute.getValue());
			}
			
			if(attribute.getKey().equalsIgnoreCase("href")) {
				String nodeID = attribute.getValue().substring(1);
				System.out.println("ID-->" + nodeID);
			}
		}
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
 	
}
