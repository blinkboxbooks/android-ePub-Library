/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.exceptions;

import com.blinkbox.java.book.model.BBBEPubBook;

/**
 * This is exception is thrown if a {@link BBBEPubBook} could not loaded because the ePub is invalid
 */
public class BBBEPubParseException extends BBBEPubException {

	private static final long serialVersionUID = 7916919551784530711L;

	public BBBEPubParseException(String message) {
		super(message);
	}

	public BBBEPubParseException(Throwable cause) {
		super(cause);
	}

}
