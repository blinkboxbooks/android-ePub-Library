/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/

package com.blinkbox.java.book.model;

import com.blinkbox.java.book.factory.BBBEPubXMLFactory;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipEntry;
import com.blinkbox.java.book.factory.BBBEPubZipFactory.BBBEZipParser;
import com.blinkbox.java.book.json.BBBEPubBookInfo;
import com.blinkbox.java.book.utils.BBBEPubBookUtils;
import com.blinkbox.java.book.utils.ZipUtils;

import java.util.HashMap;
import java.util.List;

/**
 * This class contains a Blinkbox Book ePub 2.0 book object created from a zip file.
 */
public class BBBEPub2Book extends BBBEPubBook {

	private BBBEZipParser mZipFile;
	private String mMimeType;
	private String mBaseUrl;
	private String mCoverUrl;
	private List<BBBEPubTOCReference> mTOCList;
	private BBBEPubNavigationNode mNavigationTree;
	private BBBEPubMetaData mMetaData;
	private BBBEPubBookInfo mBookInfo;
	private HashMap<String, String> mMimeTypeTable;

	/**
	 * Create a new {@link BBBEPubBook} from an ePub 2.0 zip file
	 * 
	 * @param zipFile
	 * @param mMimeType
	 * @param mTOCList
	 * @param mNavigationTree
	 * @param mMetaData
	 * @param mMimeTypeTable
	 */
	public BBBEPub2Book(BBBEZipParser zipFile, String mMimeType, String baseUrl, String coverUrl, List<BBBEPubTOCReference> mTOCList,
			BBBEPubNavigationNode mNavigationTree, BBBEPubMetaData mMetaData, BBBEPubBookInfo mBookInfo, HashMap<String, String> mMimeTypeTable) {
		this.mZipFile = zipFile;
		this.mMimeType = mMimeType;
		this.mBaseUrl = baseUrl;
		this.mCoverUrl = coverUrl;
		this.mTOCList = mTOCList;
		this.mNavigationTree = mNavigationTree;
		this.mMetaData = mMetaData;
		this.mBookInfo = mBookInfo;
		this.mMimeTypeTable = mMimeTypeTable;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return mMimeType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getVersion()
	 */
	@Override
	public String getVersion() {
		return "epub2";
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getTOCList()
	 */
	@Override
	public List<BBBEPubTOCReference> getTOCList() {
		return mTOCList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getTOCList(int)
	 */
	@Override
	public List<BBBEPubTOCReference> getTOCList(int treeDepth) {
		return BBBEPubBookUtils.getListForDepth(mTOCList, treeDepth);
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getNavigationTree()
	 */
	@Override
	public BBBEPubNavigationNode getNavigationTree() {
		return mNavigationTree;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getTitle()
	 */
	@Override
	public String getTitle() {
		return mMetaData == null ? "" : mMetaData.getAttribute(BBBEPubConstants.METADATA_TITLE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return mMetaData == null ? "" : mMetaData.getAttribute(BBBEPubConstants.METADATA_AUTHOR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getMetaData()
	 */
	@Override
	public BBBEPubMetaData getMetaData() {
		return mMetaData;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getContentAtUrl(java.lang.String)
	 */
	@Override
	public BBBEPubContent getContentAtUrl(String url) {
		if (url.startsWith("/")) {
			url = url.substring(1);
		}
		String stripAnchor = BBBEPubBookUtils.stripAnchor(url);
		BBBEZipEntry zipEntry = mZipFile.getZipFileInputStream(stripAnchor);
		if (zipEntry == null) {
			return null;
		}

		String mimeUrl = ZipUtils.stripBaseUrl(mBaseUrl, stripAnchor);
		String mimeType = mMimeTypeTable.get(mimeUrl);
		if (mimeType == null) {
			mimeType = BBBEPubXMLFactory.DEFAULT_MIMETYPE;
		}
		return new BBBEPubContent(mimeType, BBBEPubXMLFactory.DEFAULT_ENCODING, zipEntry.inputStream, zipEntry.contentLength);
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getBaseUrl()
	 */
	@Override
	public String getBaseUrl() {
		return mBaseUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getBaseUrl()
	 */
	@Override
	public String getCoverUrl() {
		if (mCoverUrl == null) {
			return null;
		}
		return mZipFile.getFileUrl() + "/" + mCoverUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see com.blinkbox.java.book.model.BBBEPubBook#getBaseUrl()
	 */
	@Override
	public BBBEPubBookInfo getBookInfo() {
		return mBookInfo;
	}
}
