/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.exceptions;

import com.blinkbox.java.book.model.BBBEPubBook;

/**
 * This is a generic exception thrown if a {@link BBBEPubBook} could not loaded
 */
public class BBBEPubException extends Exception {

	private static final long serialVersionUID = -4777030181330003322L;

	public BBBEPubException() {
		super();
	}

	public BBBEPubException(String message) {
		super(message);
	}

	public BBBEPubException(Throwable cause) {
		super(cause);
	}

	public BBBEPubException(String message, Throwable cause) {
		super(message, cause);
	}

}
