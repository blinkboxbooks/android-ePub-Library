/*******************************************************************************
 *  Copyright (c) 2014 blinkbox Entertainment Limited. All rights reserved.
 *******************************************************************************/
package com.blinkbox.java.book.json;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to parse the META-INF/book-info.json in epub books
 */
public class BBBEPubBookInfo implements Serializable {

	private String opfPath = null;
	private boolean sample;
	private ArrayList<BBBSpineItem> spine = new ArrayList<BBBSpineItem>();
	private ArrayList<BBBTocItem> toc = new ArrayList<BBBTocItem>();
	private String version;
	
	public static BBBEPubBookInfo createFromStream(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuilder builder = new StringBuilder();
		String aux = "";

		BBBEPubBookInfo book = null;
		
		try {
			while ((aux = reader.readLine()) != null) {
			    builder.append(aux);
			}
			
			String text = builder.toString();
			
			try {
				book = new Gson().fromJson(text, BBBEPubBookInfo.class);
			} catch(Exception e) {
				text.replace("null", "");
				book = new Gson().fromJson(text, BBBEPubBookInfo.class);
			}
		} catch(Exception e){};
		
		return book;
	}

	public ArrayList<BBBSpineItem> getSpine() {
		return spine;
	}
	
	public ArrayList<BBBTocItem> getToc() {
		return toc;
	}
	
	public String getOpfPath() {
		return opfPath;
	}
	
	public boolean isSample() {
		return sample;
	}

	public String getVersion() {
		return version;
	}
}
