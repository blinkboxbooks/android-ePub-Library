/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.factory;

import com.blinkbox.java.book.exceptions.BBBEPubFileNotFoundException;
import com.blinkbox.java.book.exceptions.BBBEPubParseException;
import com.blinkbox.java.book.model.BBBEPubBook;
import com.blinkbox.java.book.model.BBBEPubContent;
import com.blinkbox.java.book.utils.StreamUtils;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test the end to end functionality of parsing an epub file and producing a
 * {@link BBBEPubBook} object
 */
public class BBBEPubFactoryTest {

	private BBBEPubFactory bbBBBEPubFactory = BBBEPubFactory.getInstance();

	/**
	 * Parse the sample book from the test resources
	 */
	@Test
	public void testParseASampleBook() throws Exception {
		BBBEPubBook book = bookFromResource("books/sample.epub");

		assertEquals("application/epub+zip", book.getMimeType());
		assertEquals("Nemesis", book.getTitle());
		assertEquals(63, book.getTOCList().size());
		assertEquals("Cover", book.getTOCList().get(0).getLabel());
		assertEquals("OPS/cover.xml", book.getTOCList().get(0).getHREFLink());
		assertEquals(0, book.getTOCList().get(0).getDepth());
		assertEquals("51 Sans Souci", book.getTOCList().get(62).getLabel());
		assertEquals("OPS/062 - Chapter_51.xhtml", book.getTOCList().get(62).getHREFLink());
		assertEquals(1, book.getTOCList().get(62).getDepth());

		String titleurl = book.getTOCList().get(1).getHREFLink();
		BBBEPubContent content = book.getContentAtUrl(titleurl);
		assertNotNull(content);
		assertNotNull(content.getData());
		String data = new String(StreamUtils.toByteArray(content.getData()));
		assertTrue(data.startsWith("<?xml"));
		assertTrue(data.endsWith("</html>"));
		assertEquals(769, data.length());

		content = book.getContentAtUrl("OPS/images/fsc.jpg");
		assertEquals("image/jpeg", content.getMimeType());
		content = book.getContentAtUrl("/OPS/images/fsc.jpg");
		assertEquals("image/jpeg", content.getMimeType());

		assertEquals(book.getTOCList().get(0).getHREFLink(), book.getBookInfo().getToc().get(0).href);
	}

	/**
	 * Parse an actual epub book (A history of my times)
	 */
	@Test
	public void testParseTheHistoryOfMyTimes() throws Exception {
		BBBEPubBook book = bookFromResource("books/history.epub");

		assertEquals("application/epub+zip", book.getMimeType());
		assertEquals("A History of My Times", book.getTitle());
		assertEquals(248, book.getTOCList().size());
		assertEquals(248, book.getTOCList(5).size());
		assertEquals(12, book.getTOCList(0).size());
		assertEquals(34, book.getTOCList(1).size());
		assertEquals("Cover", book.getTOCList().get(0).getLabel());
		assertEquals("OEBPS/html/cover.html", book.getTOCList().get(0).getHREFLink());
		assertEquals("page 362", book.getTOCList().get(229).getLabel());
		assertEquals("OEBPS/html/bk07notes02.html#bk07notes02", book.getTOCList().get(229).getHREFLink());
		assertEquals("page 403", book.getTOCList().get(247).getLabel());
		assertEquals("OEBPS/html/bk07notes20.html#bk07notes20", book.getTOCList().get(247).getHREFLink());

		String titleurl = book.getTOCList().get(1).getHREFLink();
		BBBEPubContent content = book.getContentAtUrl(titleurl);
		assertNotNull(content);
		assertNotNull(content.getData());
		String data = new String(StreamUtils.toByteArray(content.getData()));
		assertTrue(data.startsWith("<?xml"));
		assertEquals(8548, data.length());
	}
	
	/**
	 * Parse an encrypted epub book
	 */
	@Test
	public void testParseEncryptedAES128SmallEpub() throws Exception {
		BBBEPubBook book = bookFromResource("books/encrypted/small-aes128-comp.epub");

//		assertEquals("application/epub+zip", book.getMimeType());
		assertEquals("Designated Targets: World War 2.2", book.getTitle());
		assertEquals(46, book.getTOCList().size());
		assertEquals("Cover", book.getTOCList().get(0).getLabel());
		assertEquals("DesignatedTargets/xhtml/cover.html", book.getTOCList().get(0).getHREFLink());

		String titleurl = book.getTOCList().get(1).getHREFLink();
		BBBEPubContent content = book.getContentAtUrl(titleurl);
		assertNotNull(content);
		assertNotNull(content.getData());
		assertEquals("application/xhtml+xml", content.getMimeType());
		String data = new String(StreamUtils.toByteArray(content.getData()));
		assertTrue(data.startsWith("<?xml"));
		assertEquals(1384, data.length());
	}


	/**
	 * Test that books with invalid XML can still be read, as all the information
	 * is available in book-info.json
	 */
	@Test
	public void testParseBookWithInvalidXML() throws Exception {
		BBBEPubBook book = bookFromResource("books/9780330462716.epub");

		assertEquals("application/epub+zip", book.getMimeType());
		BBBEPubContent content = book.getContentAtUrl("/META-INF/book-info.json");
		assertNotNull(content);
		assertNotNull(content.getData());
		String data = new String(StreamUtils.toByteArray(content.getData()));
		assertTrue(data.startsWith("{\"version\":"));
		assertEquals(1423, data.length());
	}
	
	/**
	 * Parse an invalid epub book
	 */
	@Test
	public void testParseInvalidEPub() throws Exception {
		try {
			BBBEPubBook book = bookFromResource("books/encrypted/invalidxml.epub");
			assertEquals(book.getBaseUrl(), "DesignatedTargets/");
		} catch (BBBEPubParseException e) {
			fail();
		}
	}

	/**
	 * Test that BBBEPubFileNotFoundException is thrown if the book is missing
	 */
	@Test
	public void testParseMissingBook() throws Exception {
		try {
			bbBBBEPubFactory.createFromURL(null, "books/missing.epub", null);
			fail();
		} catch (BBBEPubFileNotFoundException e) {
			assertTrue(true);
		}
	}
	
	private String extractFile(String resource) throws Exception {
		File fileOutput = new File("test.epub");
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resource);
		OutputStream outputStream = new FileOutputStream(fileOutput);
		
		// transfer bytes from the input file to the output file
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		// Close the streams
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		return fileOutput.getAbsolutePath();
	}

	private BBBEPubBook bookFromResource(final String resource) throws Exception {
		String fullPath = extractFile(resource);
		System.out.println("url " + fullPath);

		final long startTime = System.currentTimeMillis();
		BBBEPubBook book = bbBBBEPubFactory.createFromURL(null, fullPath, "M0bc45T".toCharArray());
		final long elapsedTimeMillis = System.currentTimeMillis() - startTime;

		System.out.println("Parsed " + resource + " in " + elapsedTimeMillis + " ms");

		return book;
	}

}
