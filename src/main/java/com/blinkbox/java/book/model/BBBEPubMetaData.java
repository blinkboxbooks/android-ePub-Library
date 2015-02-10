/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.model;

import java.util.HashMap;

/**
 * Contains the books metadata (e.g title, author)
 */
public class BBBEPubMetaData implements BBBEPubConstants {

	private HashMap<String, String> metaData;

	/**
	 * Create a new BBBEPubMetaData object
	 */
	public BBBEPubMetaData() {
		metaData = new HashMap<String, String>();
	}

	/**
	 * Returns a hashmap of the key value pairs of ePub metadata
	 * 
	 * @return
	 */
	public HashMap<String, String> getMap() {
		return metaData;
	}

	/**
	 * Get an attribute from the metadata
	 * 
	 * @param attribute
	 * @return value The attributes value or null
	 */
	public String getAttribute(String attribute) {
		return metaData.get(attribute);
	}

	/**
	 * Add an attribute to the metadata
	 * 
	 * @param name The attribute key
	 * @param value The attribute value
	 */
	public void addAttribute(String name, String value) {
		metaData.put(name, value);
	}
}
