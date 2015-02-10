/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.utils;

import com.blinkbox.java.book.exceptions.BBBEPubParseException;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipEntry;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipParser;
import com.blinkbox.java.book.json.BBBEPubBookInfo;
import com.blinkbox.java.book.model.BBBEPub2Book;
import com.blinkbox.java.book.model.BBBEPubBook;
import com.blinkbox.java.book.model.BBBEPubMetaData;
import com.blinkbox.java.book.model.BBBEPubTOCReference;
import com.blinkbox.java.book.xml.BBBEPubContainer;
import com.blinkbox.java.book.xml.BBBEPubNavigation;
import com.blinkbox.java.book.xml.BBBEPubPackage;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * This class parsers a epub (zip) file and returns a {@link BBBEPubBook} object
 */
public class BBBEPubParser {

	private static final String DEFAULT_MIMETYPE = "application/epub+zip";
	private static final String DEFAULT_OPS_PACKAGE_PATH = "OPS/package.opf";
	private static final String PATH_MIMETYPE = "mimetype";
	private static final String CONTAINER = "META-INF/container.xml";
	private static final String BOOKINFO = "META-INF/book-info.json";

	/**
	 * Get an input stream to the given zip entry, or throw an exception if it is missing
	 * 
	 * @param parser
	 * @param fileName
	 * @return
	 * @throws BBBEPubParseException
	 */
	public static InputStream getInputStream(BBBEZipParser parser, String fileName) throws BBBEPubParseException {
		BBBEZipEntry zipEntry = parser.getZipFileInputStream(fileName);
		if (zipEntry == null) {
			throw new BBBEPubParseException("Could not find the zip entry: " + fileName);
		}
		return zipEntry.inputStream;
	}

	/**
	 * Create a {@link BBBEPubBook} from the input zip parser
	 * 
	 * @param parser
	 * @return a valid {@link BBBEPubBook} book or null
	 * @throws BBBEPubParseException
	 * @throws XmlPullParserException
	 */
	public static BBBEPubBook loadFromFile(BBBEZipParser parser) throws BBBEPubParseException, XmlPullParserException {
		InputStream inputStream = null;
		try {
			String mimeType = DEFAULT_MIMETYPE;
			String opsPackagePath = DEFAULT_OPS_PACKAGE_PATH;
			String baseUrl = null;
			String coverUrl = null;
			HashMap<String, String> mimeTypeMap = new HashMap<String, String>();
			BBBEPubMetaData bbbePubMetaData = null;
			List<BBBEPubTOCReference> tocList = null;

			// Parse the META-INF/book-info.json file
			BBBEPubBookInfo bbbePubBookInfo = null;
			BBBEZipEntry zipEntry = parser.getZipFileInputStream(BOOKINFO);
			if (zipEntry != null) {
				bbbePubBookInfo = BBBEPubBookInfo.createFromStream(zipEntry.inputStream);
				zipEntry.inputStream.close();
				opsPackagePath = bbbePubBookInfo.getOpfPath();
				if (opsPackagePath != null) {
					baseUrl = ZipUtils.getDirectoryName(opsPackagePath);
				}
			}
			
			try {
				// Get the mimetype
				inputStream = getInputStream(parser, PATH_MIMETYPE);
				mimeType = new String(com.blinkbox.java.book.utils.StreamUtils.toByteArray(inputStream));
				inputStream.close();
	
				// Get the path to the OEBPS package
				inputStream = getInputStream(parser, CONTAINER);
				opsPackagePath = new BBBEPubContainer(inputStream).getOebpsPackagePath();
				inputStream.close();
	
				// Parse the OEBPS package and extract the metadata and manifest
				inputStream = getInputStream(parser, opsPackagePath);
				BBBEPubPackage bbBBBEPubPackage = new BBBEPubPackage(inputStream);
				bbbePubMetaData = bbBBBEPubPackage.getMetaData();
				baseUrl = com.blinkbox.java.book.utils.ZipUtils.getDirectoryName(opsPackagePath);
				String tocNCXPath = baseUrl + bbBBBEPubPackage.getNcxPath();
				coverUrl = bbBBBEPubPackage.getCoverUrl();
				if (coverUrl != null) {
					coverUrl = baseUrl + coverUrl;
				}
				mimeTypeMap = bbBBBEPubPackage.getmMimeTypeTable();
				inputStream.close();
	
				// Get the table of contents from toc.ncx
				inputStream = getInputStream(parser, tocNCXPath);
				BBBEPubNavigation bbBBBEPubNavigation = new BBBEPubNavigation(inputStream, baseUrl);
				tocList = bbBBBEPubNavigation.getNavList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// System.out.println("mimeType " + mimeType + ", " + "opsPackagePath " + opsPackagePath + ", " + "bbbePubMetaData " + bbbePubMetaData + ", " + "baseUrl " + baseUrl +
			// ", " + "tocNCXPath " + tocNCXPath);
			return new BBBEPub2Book(parser, mimeType, baseUrl, coverUrl, tocList, null, bbbePubMetaData, bbbePubBookInfo, mimeTypeMap);
		} catch (IOException e) {
			throw new BBBEPubParseException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
