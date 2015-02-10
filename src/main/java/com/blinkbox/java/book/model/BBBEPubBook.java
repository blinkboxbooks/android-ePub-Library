/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.model;

import com.blinkbox.java.book.json.BBBEPubBookInfo;

import java.util.List;

/**
 * This class contains a Blinkbox Book ePub book object created from a zip file.
 */
public abstract class BBBEPubBook {

	/**
	 * Returns the books mimetype as a string
	 * 
	 * @return
	 */
	public abstract String getMimeType();

	/**
	 * Returns the books ePub version
	 * 
	 * @return
	 */
	public abstract String getVersion();

	/**
	 * Returns a list of the books table of contents
	 * 
	 * @return
	 */
	public abstract List<BBBEPubTOCReference> getTOCList();

	/**
	 * Returns all the {@link BBBEPubTOCReference} items below a given tree depth
	 * 
	 * @param treeDepth
	 * @return
	 */
	public abstract List<BBBEPubTOCReference> getTOCList(int treeDepth);

	/**
	 * Currently not implemented - Returns the root element of the books navigation tree
	 * 
	 * @return
	 */
	public abstract BBBEPubNavigationNode getNavigationTree();

	/**
	 * Returns the books title from the {@link BBBEPubMetaData} object
	 * 
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * Returns the books author from the {@link BBBEPubMetaData} object
	 * 
	 * @return
	 */
	public abstract String getAuthor();

	/**
	 * Returns the books {@link BBBEPubMetaData} object
	 * 
	 * @return
	 */
	public abstract BBBEPubMetaData getMetaData();

	/**
	 * Returns the resource at a given url
	 * 
	 * @return the {@link BBBEPubContent} at the given url
	 */
	public abstract BBBEPubContent getContentAtUrl(String url);

	/**
	 * Returns the books base url
	 * 
	 * @return base url
	 */
	public abstract String getBaseUrl();

	/**
	 * Returns the books cover url
	 * 
	 * @return the cover url
	 */
	public abstract String getCoverUrl();

	/**
	 * Returns the books info object
	 * 
	 * @return the {@link BBBEPubBookInfo} object from the book, or null
	 */
	public abstract BBBEPubBookInfo getBookInfo();
}
