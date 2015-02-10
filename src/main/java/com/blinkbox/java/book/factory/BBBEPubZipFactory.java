/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.factory;

import com.blinkbox.java.book.exceptions.BBBEPubException;
import com.blinkbox.java.book.exceptions.BBBEPubFileNotFoundException;
import com.blinkbox.java.book.utils.BBBEPubBookUtils;
import com.blinkbox.java.book.utils.BBBEPubZip4J;
import com.blinkbox.java.book.utils.BBBEPubZipInputStream;

import net.lingala.zip4j.exception.ZipException;

import android.content.Context;

import java.io.File;
import java.io.InputStream;

/**
 * This is a factory class for creating {@link BBBEZipParser} objects from InputStreams, files or Android assets
 */
public class BBBEPubZipFactory {

	public static final String ANDROID_ASSET = "file:///android_asset/";
	public static final String FILE_URL = "file://";

	/**
	 * Interface of a zip parser. Zip parsers must implement this interface
	 */
	public interface BBBEZipParser {
		/**
		 * Returns the url to the original epub file
		 * 
		 * @return fileUrl
		 */
		public String getFileUrl();

		/**
		 * Get an InputStream for the given zip file entry
		 * 
		 * @param zipEntry
		 * @return an InputStream to the file, or null
		 */
		public BBBEZipEntry getZipFileInputStream(String zipEntry);
	}

	/**
	 * Holder class to represent a zip entry
	 */
	public static class BBBEZipEntry {
		public BBBEZipEntry(InputStream inputStream, long contentLength) {
			this.inputStream = inputStream;
			this.contentLength = contentLength;
		}
		public InputStream inputStream;
		public long contentLength;
	}

	/**
	 * Get the best zip parser for the given url and zip password
	 * 
	 * @param context
	 * @param fileUrl
	 * @param zipPass
	 * @return
	 * @throws BBBEPubException
	 */
	public static BBBEZipParser getBestZipParser(Context context, String fileUrl, char[] zipPass) throws BBBEPubException {
		if (fileUrl.startsWith(ANDROID_ASSET)) {
			if (zipPass != null) {
				// The Zip4J currently does not take an inputstream so the asset must be extracted first
				fileUrl = BBBEPubBookUtils.extractAssetToFile(context, fileUrl);
			} else {
				return new BBBEPubZipInputStream(context, fileUrl);
			}
		} else if (fileUrl.startsWith(FILE_URL)) {
			fileUrl = fileUrl.substring(FILE_URL.length());
		}

		try {
			File file = new File(fileUrl);
			if (!file.canRead()) {
				throw new BBBEPubFileNotFoundException("Could not read " + file.getAbsolutePath());
			}

			return new BBBEPubZip4J(fileUrl, zipPass);
		} catch (ZipException e) {
			throw new BBBEPubException(e);
		}
	}

}
