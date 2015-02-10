/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.blinkbox.java.book.factory.BBBEPubXMLFactory;

/**
 * Simple class to parse the path to an oebps file from the container.xml file
 * in an ePub file
 */
public class BBBEPubContainer {

	private static final String ROOTFILE = "rootfile";
	private static final String FULLPATH = "full-path";
	private static final String MEDIATYPE = "media-type";
	
	private static final String OEBPSPACKAGE = "application/oebps-package+xml";
	
	private String oebpsPackagePath;
	
	private void parsePackagePage(XmlPullParser xmlPullParser) {
		if (OEBPSPACKAGE.equals(xmlPullParser.getAttributeValue(null, MEDIATYPE))) {
			oebpsPackagePath = xmlPullParser.getAttributeValue(null, FULLPATH);
		}
	}

	public BBBEPubContainer(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser xmlPullParser = BBBEPubXMLFactory.parseInputStream(in);
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && ROOTFILE.equals(xmlPullParser.getName())) {
				parsePackagePage(xmlPullParser);
			}
			eventType = xmlPullParser.next();
		}
	}

	public String getOebpsPackagePath() {
		return oebpsPackagePath;
	}

	// Extend to include pdf files
//	public String getPDFDocument() {
//		return pdfDocument;
//	}
}
