/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

import com.blinkbox.java.book.factory.BBBEPubZipFactory;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipEntry;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipParser;

public class BBBEPubZipInputStream implements BBBEZipParser {

	private final Context mContext;
	private final String mFileUrl;
	private final String mAssetFile;

	public BBBEPubZipInputStream(Context context, String fileUrl) {
		mContext = context;
		mFileUrl = fileUrl;
		if (mFileUrl.startsWith(BBBEPubZipFactory.ANDROID_ASSET)) {
			mAssetFile = mFileUrl.substring(BBBEPubZipFactory.ANDROID_ASSET.length());
		} else {
			mAssetFile = null;
		}
	}

	private InputStream getInputStream() throws IOException {
		if (mAssetFile != null) {
			return mContext.getAssets().open(mAssetFile);
		}
		return new FileInputStream(mFileUrl);
	}

	@SuppressWarnings("resource")
	public BBBEZipEntry getZipFileInputStream(String zip2Entry) {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream(getInputStream());
			ZipEntry zipEntry = null;

			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					continue;
				}

				// String name = zipEntry.getName();
				// long size = zipEntry.getSize();
				// int compression = zipEntry.getMethod();
				// System.out.println("out " + stripAnchor + " " + name + " " + size + " comp " + compression);

				if (zipEntry.getName().equals(zip2Entry)) {
					return new BBBEZipEntry(zipInputStream, zipEntry.getSize());
				}
			}
			
			zipInputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFileUrl() {
		return mFileUrl;
	}
}
