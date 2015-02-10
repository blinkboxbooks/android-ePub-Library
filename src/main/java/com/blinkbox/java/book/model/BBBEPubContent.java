/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.model;

import java.io.InputStream;

/**
 * This is a container class for content within a BBBEPubBook.
 */
public class BBBEPubContent {

	private String mMimeType;
	private String mEncoding;
	private InputStream mData;
	private long mDataLength;

	public BBBEPubContent(String mMimeType, String mEncoding, InputStream mData, long mDataLength) {
		this.mMimeType = mMimeType;
		this.mEncoding = mEncoding;
		this.mData = mData;
		this.mDataLength = mDataLength;
	}

	/**
	 * Returns the length of the content
	 * 
	 * @return
	 */
	public long getDataLength() {
		return mDataLength;
	}

	/**
	 * Returns the mimetype of the content
	 * 
	 * @return
	 */
	public String getMimeType() {
		return mMimeType;
	}

	/**
	 * Returns the encoding of the content (e.g UTF-8)
	 * 
	 * @return
	 */
	public String getEncoding() {
		return mEncoding;
	}

	/**
	 * Returns an InputStream of content
	 * 
	 * @return
	 */
	public InputStream getData() {
		return mData;
	}
}
