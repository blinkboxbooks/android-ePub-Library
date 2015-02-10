/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.blinkbox.java.book.exceptions.BBBEPubFileNotFoundException;
import com.blinkbox.java.book.model.BBBEPubTOCReference;

/**
 * Helper class for the EPub Library
 */
public class BBBEPubBookUtils {

	/**
	 * Extract an asset
	 * 
	 * @param context
	 * @param filename
	 * @throws BBBEPubFileNotFoundException
	 */
	public static void extractAsset(Context context, String filename) throws BBBEPubFileNotFoundException {
		try {
			InputStream myInput = context.getAssets().open(filename);
			OutputStream myOutput = context.openFileOutput(filename,
					Context.MODE_PRIVATE);

			// transfer bytes from the input file to the output file
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			throw new BBBEPubFileNotFoundException("Could not extract " + filename);
		}
	}

	/**
	 * Extract a file from the assets to internal memory and return the location
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 * @throws BBBEPubFileNotFoundException
	 */
	public static String extractAssetToFile(Context context, String fileName) throws BBBEPubFileNotFoundException {
		File file = new File(context.getFilesDir(), fileName);
		String fullPath = file.getAbsolutePath();
		if (!file.exists()) {
			extractAsset(context, fileName);
		}
		return fullPath;
	}
	
	
	/**
	 * Strip the anchor link from a url
	 * 
	 * @param url
	 * @return
	 */
	public static String stripAnchor(String url) {
		int anchorIndex = url.indexOf('#');
		if (anchorIndex > 0) {
			return url.substring(0, anchorIndex);
		} else {
			return url;
		}
	}

	/**
	 * Returns all the TOC items below a given tree depth
	 * 
	 * @param treeDepth
	 * @return
	 */
	public static List<BBBEPubTOCReference> getListForDepth(List<BBBEPubTOCReference> tocList, int treeDepth) {
		List<BBBEPubTOCReference> newTOCList = new LinkedList<BBBEPubTOCReference>();
		for (BBBEPubTOCReference tocReference : tocList) {
			if (tocReference.getDepth() <= treeDepth) {
				newTOCList.add(tocReference);
			}
		}
		return newTOCList;
	}
}
