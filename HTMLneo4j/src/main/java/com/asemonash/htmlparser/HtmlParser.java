package com.asemonash.htmlparser;

import java.io.File;
import java.io.IOException;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	
	private File htmlFile;
	private Document document;
	int afterCounter, beforeCounter = 0;
	
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
				extractData(areaTagElement);
				beforeCounter++;
			}
			
			rowCounterDebugFunc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void extractData(Element areaTagElement) {
		//System.out.println(areaTagElement.attributes().iterator());
		Attributes attributes = areaTagElement.attributes();
		
		//System.out.println(e.asList());
		
		for(Attribute attribute: attributes.asList()) {
			System.out.println(attribute.getKey() +"::"+ attribute.getValue());
		}
		
		//System.out.println(it.getAttributes());
		
//		if(areaTagElement.hasAttr("href") && areaTagElement.hasAttr("alt")) {
//			System.out.println(areaTagElement.attr("href") +"--"+ areaTagElement.attr("alt"));
//			afterCounter++;
//		}
	}
	
	private void rowCounterDebugFunc() {
		System.out.println("Rows before filtering-->" + beforeCounter);
		System.out.println("******************");
		System.out.println("Rows after filtering-->"+ afterCounter);
	}
}
