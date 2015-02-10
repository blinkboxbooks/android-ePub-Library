/*************************************************************************
 * Copyright (c) 2014 blinkbox Entertainment Limited. All rights reserved.
 *************************************************************************/
package com.blinkbox.java.book.json;

import java.io.Serializable;

public class BBBSpineItem implements Serializable {

    public String itemId;
    public String href;
    public String mediaType;
    public boolean linear;
    public int wordCount;
    public String label;
    public float progress;

}