/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.blinkbox.java.book.factory.BBBEPubXMLFactory;
import com.blinkbox.java.book.model.BBBEPubTOCReference;

/**
 * Class to parse the navigation tree in toc.ncx within an ePub file
 * 
 */
public class BBBEPubNavigation {

	private static final String NAVMAP = "navMap";
	private static final String NAVPOINT = "navPoint";
	private static final String NAVLABEL = "navLabel";
	private static final String TEXT = "text";
	private static final String CONTENT = "content";
	private static final String SRC = "src";

	private final String baseURL;
	private List<BBBEPubTOCReference> navList = new LinkedList<BBBEPubTOCReference>();

	private List<BBBEPubTOCReference> parseNavPoint(XmlPullParser xmlPullParser, int depth) throws XmlPullParserException, IOException {
		List<BBBEPubTOCReference> childList = new LinkedList<BBBEPubTOCReference>();
		String label = null;
		String href = null;
		int eventType = xmlPullParser.nextTag();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && NAVPOINT.equals(xmlPullParser.getName())) {
				List<BBBEPubTOCReference> innerList = parseNavPoint(xmlPullParser, depth + 1);
				childList.addAll(innerList);
			} else if (eventType == XmlPullParser.START_TAG && NAVLABEL.equals(xmlPullParser.getName())) {
				eventType = xmlPullParser.nextTag();
				if (eventType == XmlPullParser.START_TAG && TEXT.equals(xmlPullParser.getName())) {
					label = xmlPullParser.nextText();
				}
			} else if (eventType == XmlPullParser.START_TAG && CONTENT.equals(xmlPullParser.getName())) {
				href = xmlPullParser.getAttributeValue(null, SRC);
			} else if (eventType == XmlPullParser.END_TAG && NAVPOINT.equals(xmlPullParser.getName())) {
				childList.add(0, new BBBEPubTOCReference(label, baseURL + href, depth));
				return childList;
			}
			eventType = xmlPullParser.nextTag();
		}
		return childList;
	}

	private void parseNavMap(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
		int eventType = xmlPullParser.nextTag();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && NAVPOINT.equals(xmlPullParser.getName())) {
				navList.addAll(parseNavPoint(xmlPullParser, 0));
			} else if (eventType == XmlPullParser.END_TAG && NAVMAP.equals(xmlPullParser.getName())) {
				break;
			}
			eventType = xmlPullParser.nextTag();
		}
	}

	public BBBEPubNavigation(InputStream in, String baseUrl) throws XmlPullParserException, IOException {
		baseURL = baseUrl;
		XmlPullParser xmlPullParser = BBBEPubXMLFactory.parseInputStream(in);
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && NAVMAP.equals(xmlPullParser.getName())) {
				parseNavMap(xmlPullParser);
			}
			eventType = xmlPullParser.next();
		}
	}

	public List<BBBEPubTOCReference> getNavList() {
		return navList;
	}

	public class NavItem {
		String id;
		String playOrder;
		String href;
		String label;

		public NavItem(String id, String playOrder, String href, String label) {
			this.id = id;
			this.playOrder = playOrder;
			this.href = href;
			this.label = label;
		}

		public String getId() {
			return id;
		}

		public String getPlayOrder() {
			return playOrder;
		}

		public String getHref() {
			return href;
		}

		public String getLabel() {
			return label;
		}
	}
}
