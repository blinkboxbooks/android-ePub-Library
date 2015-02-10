/*************************************************************************
 * Copyright (c) 2014 blinkbox Entertainment Limited. All rights reserved.
 *************************************************************************/
package com.blinkbox.java.book.json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents book Table of Content
 */
public class BBBTocItem implements Serializable {

	public String itemId;
	public String label;
	public String href;
	public boolean active;
	
	public ArrayList<BBBTocItem> children;
}
