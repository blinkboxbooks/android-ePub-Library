/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.factory;

import android.content.Context;
import android.graphics.Bitmap;

import com.blinkbox.java.book.exceptions.BBBEPubException;
import com.blinkbox.java.book.exceptions.BBBEPubParseException;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipEntry;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipParser;
import com.blinkbox.java.book.model.BBBEPubBook;
import com.blinkbox.java.book.utils.BBBEPubParser;
import com.blinkbox.java.book.utils.BitmapDecoder;
import com.blinkbox.java.book.utils.StreamUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is a factory class for creating {@link BBBEPubBook} objects from files or Android assets
 */
public class BBBEPubFactory {

	private static BBBEPubFactory sInstance;

	/**
	 * Singleton getter
	 * 
	 * @return the BBBBBEPubFactory singleton object
	 */
	public static BBBEPubFactory getInstance() {

		if (sInstance == null) {
			sInstance = new BBBEPubFactory();
		}

		return sInstance;
	}

	/**
	 * Load a BBBEPubBook from file url (file or android_asset)
	 * 
	 * @param context
	 * @param url
	 * @param zipPassword
	 * @return
	 * @throws BBBEPubException
	 */
	public BBBEPubBook createFromURL(final Context context, final String url, final char[] zipPassword) throws BBBEPubException {
		BBBEZipParser parser = BBBEPubZipFactory.getBestZipParser(context, url, zipPassword);
		try {
			return BBBEPubParser.loadFromFile(parser);
		} catch (XmlPullParserException e) {
			throw new BBBEPubParseException(e);
		}
	}

	/**
	 * Load content from within an embedded epub book
	 * 
	 * @param context
	 * @param url
	 * @param zipPassword
	 * @return inputStream An inputstream to the data or null
	 * @throws BBBEPubException
	 */
	public InputStream loadFromURL(final Context context, final String url, final char[] zipPassword) throws BBBEPubException {
		int endIndex = url.indexOf(".epub/");
		if (endIndex == -1) {
			return null;
		}
		String epubUrl = url.substring(0, endIndex + 5);
		String fileUrl = url.substring(endIndex + 6);
		BBBEZipParser parser = BBBEPubZipFactory.getBestZipParser(context, epubUrl, zipPassword);
		BBBEZipEntry zipEntry = parser.getZipFileInputStream(fileUrl);
		if (zipEntry == null) {
			return null;
		}
		return zipEntry.inputStream;
	}

    /**
     * Load a bitmap from an ebook.
     *
     * @param context a Context object
     * @param url the url of the image to load
     * @return a Bitmap object containing the image (or null if a Bitmap could not be loaded)
     */
    public Bitmap loadBitmapFromBook(Context context, String url, int maxWidth, int maxHeight, char[] encryptionKey) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = BBBEPubFactory.getInstance().loadFromURL(context, url, encryptionKey);
            if (inputStream == null) {
                return null;
            }
            byte[] bitmapBytes = StreamUtils.toByteArray(inputStream);
            inputStream.close();

            bitmap = BitmapDecoder.decodeBitmap(bitmapBytes, null, maxWidth, maxHeight);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BBBEPubException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
