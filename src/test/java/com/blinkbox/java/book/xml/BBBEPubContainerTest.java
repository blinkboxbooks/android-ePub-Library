/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Test;

import com.blinkbox.java.book.xml.BBBEPubContainer;

/**
 * Test the parsing of a container.xml file within an epub
 */
public class BBBEPubContainerTest {

	/**
	 * Test that the path to an OPF package file can be extracted from
	 * container.xml
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParseSampleContainer() throws Exception {
		BBBEPubContainer bbBBBEPubContainer = containerFromString("xml/container.xml");
		assertEquals("OPS/package.opf", bbBBBEPubContainer.getOebpsPackagePath());
	}

	private BBBEPubContainer containerFromString(String resource) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		return new BBBEPubContainer(inputStream);
	}

}
