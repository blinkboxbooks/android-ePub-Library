/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.exceptions;

import com.blinkbox.java.book.model.BBBEPubBook;

/**
 * This is exception is thrown if a {@link BBBEPubBook} could not loaded because the file could not be found.
 */
public class BBBEPubFileNotFoundException extends BBBEPubException {

	private static final long serialVersionUID = 5328385050388801697L;

	public BBBEPubFileNotFoundException() {
		super();
	}
	
	public BBBEPubFileNotFoundException(String message) {
		super(message);
	}

	public BBBEPubFileNotFoundException(Throwable cause) {
		super(cause);
	}

}
