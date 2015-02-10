/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.model;

import java.util.List;

/**
 * This class represents a node in the books navigation tree
 */
public class BBBEPubNavigationNode {

	private String id;
	private String href;
	private String label;
	private boolean active;
	private List<BBBEPubNavigationNode> children;

	/**
	 * Returns the navigation id as a String
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the navigations href url link into the book as a String
	 * 
	 * @return
	 */
	public String getHREF() {
		return href;
	}

	/**
	 * Returns a list of this navigation nodes child elements
	 * 
	 * @return
	 */
	public List<BBBEPubNavigationNode> getChildren() {
		return children;
	}

	/**
	 * Returns the navigation nodes human readable label
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Whether this node is active or not. All entries will be true for a normal book but for samples only the first section may be active
	 * 
	 * @return true if this entry is active, or false, if the entry is inactive
	 */
	public boolean isActive() {
		return active;
	}

}
