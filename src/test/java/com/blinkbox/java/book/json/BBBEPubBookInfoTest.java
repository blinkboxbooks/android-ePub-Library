package com.blinkbox.java.book.json;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.InputStream;

public class BBBEPubBookInfoTest {

	/**
	 * Test that we can parse book-info.json
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParseSampleContainer() throws Exception {
		BBBEPubBookInfo bbBBBEPubBookInfo = fromString("json/book-info.json");
		assertEquals("OPS/package.opf", bbBBBEPubBookInfo.getOpfPath());
		assertEquals("1.2", bbBBBEPubBookInfo.getVersion());
		assertEquals("OPS/xhtml/brand.html", bbBBBEPubBookInfo.getSpine().get(0).href);
		assertEquals(9, bbBBBEPubBookInfo.getSpine().size());
		assertEquals(14, bbBBBEPubBookInfo.getToc().size());
		assertEquals("OPS/xhtml/cover.html", bbBBBEPubBookInfo.getToc().get(0).href);
	}

	private BBBEPubBookInfo fromString(String resource) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		return BBBEPubBookInfo.createFromStream(inputStream);
	}

}
