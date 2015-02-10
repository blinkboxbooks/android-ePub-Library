/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.xml;

import com.blinkbox.java.book.model.BBBEPubMetaData;

import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class BBBEPubPackageTest {

	@Test
	public void testParseSampleContainer() throws Exception {
		BBBEPubPackage bbbEPubPackage = containerFromString("xml/package.opf");
		assertEquals("toc.ncx", bbbEPubPackage.getNcxPath());
	}

	@Test
	public void testParseHistoryContainer() throws Exception {
		BBBEPubPackage bbbEPubPackage = containerFromString("xml/9780140441758.opf");
		assertEquals("toc.ncx", bbbEPubPackage.getNcxPath());
	}

	@Test
	public void testParseMissingTOCContainer() throws Exception {
		BBBEPubPackage bbbEPubPackage = containerFromString("xml/content.opf");
		assertEquals("toc.ncx", bbbEPubPackage.getNcxPath());
	}

	@Test
	public void testParseTextContainer() throws Exception {
		BBBEPubPackage bbbEPubPackage = containerFromString("xml/textpackage.opf");
		assertEquals(bbbEPubPackage.getMetaData().getAttribute(BBBEPubMetaData.METADATA_AUTHOR), "Rick Riordan");
	}

	private BBBEPubPackage containerFromString(String resource) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		return new BBBEPubPackage(inputStream);
	}

}
