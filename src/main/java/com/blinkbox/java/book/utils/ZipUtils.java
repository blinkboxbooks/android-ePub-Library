/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.utils;

public class ZipUtils {

	/**
	 * Get the directory of a given filepath
	 * 
	 * @param input
	 *            A path to to a file
	 * @return The directory of a the input file
	 */
	public static String getDirectoryName(String input) {
		int lastSlash = input.lastIndexOf('/');
		return input.substring(0, lastSlash + 1);
	}

	/**
	 * Strip the baseUrl off a given filepath
	 * 
	 * @param baseUrl
	 * @param input
	 * @return
	 */
	public static String stripBaseUrl(String baseUrl, String input) {
		if (input.startsWith(baseUrl)) {
			return input.substring(baseUrl.length());
		}
		return input;
	}
}
