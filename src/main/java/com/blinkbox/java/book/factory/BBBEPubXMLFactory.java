/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.factory;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * This factory class contains common xml helper functions
 */
public class BBBEPubXMLFactory {

	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_MIMETYPE = "application/xhtml+xml";

	/**
	 * Create a pull parser from an inputstream
	 * 
	 * @param in
	 * @return
	 * @throws XmlPullParserException
	 */
	public static XmlPullParser parseInputStream(InputStream in) throws XmlPullParserException {
		XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
		xmlPullParser.setInput(in, DEFAULT_ENCODING);
		return xmlPullParser;
	}

	/**
	 * Debug function to print the next element in an {@link XmlPullParser}
	 * 
	 * @param xmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static void printElement(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_TAG) {
			// if (eventType == XmlPullParser.START_DOCUMENT) {
			// DebugUtils.logOutput("Start document");
			// } else if (eventType == XmlPullParser.START_TAG) {
			// DebugUtils.logOutput("Start tag " + xmlPullParser.getName());
			// } else if (eventType == XmlPullParser.END_TAG) {
			// DebugUtils.logOutput("End tag " + xmlPullParser.getName());
			// } else if (eventType == XmlPullParser.TEXT) {
			// DebugUtils.logOutput("Text " + xmlPullParser.getText());
			// }
			eventType = xmlPullParser.next();
		}
	}

}
