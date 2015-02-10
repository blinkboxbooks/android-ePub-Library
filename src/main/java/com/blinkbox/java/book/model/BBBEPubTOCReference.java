/*******************************************************************************
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *******************************************************************************/
package com.blinkbox.java.book.model;

/**
 * This class represents a table of contents entry
 * 
 */
public class BBBEPubTOCReference {

	private String mlabel;
	private String href;
	
	private int mdepth;

	/**
	 * Create a table of contents item
	 * 
	 * @param label
	 *            The human readable label
	 * @param hrefLink
	 *            A link a content within the epub book
	 * @param depth
	 *            The depth of this item within the navigation tree
	 * 
	 */
	public BBBEPubTOCReference(String label, String hrefLink, int depth) {
		this.mlabel = label;
		this.href = hrefLink;
		this.mdepth = depth;
	}

	/**
	 * Returns the table of contents label as a String
	 * 
	 * @return
	 */
	public String getLabel() {
		return mlabel;
	}

	/**
	 * Returns the href link of the contents as a String
	 * 
	 * @return
	 */
	public String getHREFLink() {
		return href;
	}

	/**
	 * Returns the depth of this node within the navigation tree
	 * 
	 * @return
	 */
	public int getDepth() {
		return mdepth;
	}

}
