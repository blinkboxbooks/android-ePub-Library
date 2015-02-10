/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.factory;

import static org.junit.Assert.*;

import com.blinkbox.java.book.factory.BBBEPubFactory;
import com.blinkbox.java.book.model.BBBEPubBook;
import com.blinkbox.java.book.model.BBBEPubContent;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test the end to end functionality of parsing an epub file and producing a
 * {@link BBBEPubBook} object
 */
public class BBBEPubCoverTest {

	private BBBEPubFactory bbBBBEPubFactory = BBBEPubFactory.getInstance();

	/**
	 * Parse an ebook that contains a cover
	 */
	@Test
	public void testParseEBookCoverUrl() throws Exception {
		BBBEPubBook book = bookFromResource("books/9780553106633.epub");
		assertEquals("A Storm of Swords", book.getTitle());
		assertEquals("George R. R. Martin", book.getAuthor());

		BBBEPubContent content = book.getContentAtUrl("cover.jpeg");
		assertEquals("image/jpeg", content.getMimeType());
		
		String coverUrl = book.getCoverUrl();
		
		assertTrue(coverUrl.endsWith("/cover.jpeg"));

		InputStream inputStream = bbBBBEPubFactory.loadFromURL(null, coverUrl, null);
		assertNotNull(inputStream);
	}

	/**
	 * Parse an that does not contain a cover (A history of my times)
	 */
	@Test
	public void testParseEBookWithoutCover() throws Exception {
		BBBEPubBook book = bookFromResource("books/history.epub");
		assertNull(book.getCoverUrl());
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
