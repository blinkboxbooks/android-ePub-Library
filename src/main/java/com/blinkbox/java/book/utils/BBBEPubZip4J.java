/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.utils;

import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipEntry;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipParser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.util.List;

public class BBBEPubZip4J implements BBBEZipParser {

	private final ZipFile mZipFile;
	private final String mFileUrl;

	public BBBEPubZip4J(String fileUrl, char[] zipPassword) throws ZipException {
		mFileUrl = fileUrl;
		mZipFile = new ZipFile(fileUrl);
		if (mZipFile.isEncrypted()) {
			mZipFile.setPassword(zipPassword);
		}
	}

	public BBBEZipEntry getZipFileInputStream(String zipEntry) {
		try {
			// Get the list of file headers from the zip file
			@SuppressWarnings("rawtypes")
			List fileHeaderList = mZipFile.getFileHeaders();

			// Loop through the file headers
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
				String name = fileHeader.getFileName();

				// System.out.println("name " + name);

				if (name.equals(zipEntry)) {
					return new BBBEZipEntry(mZipFile.getInputStream(fileHeader), fileHeader.getUncompressedSize());
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFileUrl() {
		return mFileUrl;
	}
}
