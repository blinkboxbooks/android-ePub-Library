/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.xml;

import com.blinkbox.java.book.factory.BBBEPubXMLFactory;
import com.blinkbox.java.book.model.BBBEPubMetaData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to parse a OPS/package.xml file and extract the spine, manifest and
 * toc.ncx path
 */
public class BBBEPubPackage {

	private static final String METADATA = "metadata";
	private static final String MANIFEST = "manifest";
	private static final String SPINE = "spine";
	private static final String IDREF = "idref";
	private static final String ID = "id";
	private static final String HREF = "href";
	private static final String MEDIATYPE = "media-type";
	private static final String TOC = "toc";
	private static final String ITEM = "item";
	private static final String ITEMREF = "itemref";
	private static final String META = "meta";
	private static final String NAME = "name";
	private static final String COVER = "cover";
	private static final String CONTENT = "content";
	private static final String COVER_URL_END = "_cover_epub.jpg";

	private String mTocId = "ncx";
	private String mNcxPath = "toc.ncx";
	private String coverId = null;
	private String coverUrl = null;
	private BBBEPubMetaData bbBBBEPubMetaData;
	private List<String> spineIdList;
	private List<ManifestItem> manifestIdList;
	private HashMap<String, String> mMimeTypeTable;

	public HashMap<String, String> getmMimeTypeTable() {
		return mMimeTypeTable;
	}

	private void parseSpine(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
		spineIdList = new LinkedList<String>();
		int eventType = XmlPullParser.START_TAG;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && SPINE.equals(xmlPullParser.getName())) {
				String tocId = xmlPullParser.getAttributeValue(null, TOC);
				if (tocId != null) {
					mTocId = tocId;
				}
			} else if (eventType == XmlPullParser.START_TAG && ITEMREF.equals(xmlPullParser.getName())) {
				spineIdList.add(xmlPullParser.getAttributeValue(null, IDREF));
			} else if (eventType == XmlPullParser.END_TAG) {
				break;
			}
			eventType = xmlPullParser.nextTag();
		}
	}

	private void parseManifest(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
		manifestIdList = new LinkedList<BBBEPubPackage.ManifestItem>();
		mMimeTypeTable = new HashMap<String, String>();
		int eventType = xmlPullParser.nextTag();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && MANIFEST.equals(xmlPullParser.getName())) {
				String id = xmlPullParser.getAttributeValue(null, ID);
				String href = xmlPullParser.getAttributeValue(null, HREF);
				String mediatype = xmlPullParser.getAttributeValue(null, MEDIATYPE);
				mMimeTypeTable.put(href, mediatype);
				manifestIdList.add(new ManifestItem(id, href, mediatype));
			} else if (eventType == XmlPullParser.START_TAG && ITEM.equals(xmlPullParser.getName())) {
				String id = xmlPullParser.getAttributeValue(null, ID);
				String href = xmlPullParser.getAttributeValue(null, HREF);
				String mediatype = xmlPullParser.getAttributeValue(null, MEDIATYPE);
				mMimeTypeTable.put(href, mediatype);
				manifestIdList.add(new ManifestItem(id, href, mediatype));
				if (mTocId.equals(id)) {
					mNcxPath = href;
				}
			} else if (eventType == XmlPullParser.END_TAG && MANIFEST.equals(xmlPullParser.getName())) {
				break;
			}
			eventType = xmlPullParser.nextTag();
		}
	}

	private void parseMetaDataList(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
		bbBBBEPubMetaData = new BBBEPubMetaData();
		int eventType = xmlPullParser.nextTag();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String name = xmlPullParser.getName();
				if (META.equals(name)) {
					String attr = xmlPullParser.getAttributeValue(null, NAME);
					if (COVER.equals(attr)) {
						coverId = xmlPullParser.getAttributeValue(null, CONTENT);
					}
				} else {
					String label = xmlPullParser.nextText();
					if (!"".equals(label)) {
						bbBBBEPubMetaData.addAttribute(name, label);
					}
				}
			} else if (eventType == XmlPullParser.END_TAG && METADATA.equals(xmlPullParser.getName())) {
				break;
			}
			eventType = xmlPullParser.next();
		}
	}

	public BBBEPubPackage(InputStream in) throws XmlPullParserException, IOException {
		XmlPullParser xmlPullParser = BBBEPubXMLFactory.parseInputStream(in);
		xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
		int eventType = xmlPullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG && METADATA.equals(xmlPullParser.getName())) {
				parseMetaDataList(xmlPullParser);
			}
			if (eventType == XmlPullParser.START_TAG && MANIFEST.equals(xmlPullParser.getName())) {
				parseManifest(xmlPullParser);
			}
			if (eventType == XmlPullParser.START_TAG && SPINE.equals(xmlPullParser.getName())) {
				parseSpine(xmlPullParser);
			}
			eventType = xmlPullParser.next();
		}
		
		if (manifestIdList != null) {
			for (ManifestItem item : manifestIdList) {
				if (mTocId != null && mTocId.equals(item.id)) {
					mNcxPath = item.href;
				}
				if (coverId != null && coverId.equals(item.id)) {
					coverUrl = item.href;
				} else if (item.href != null && item.href.endsWith(COVER_URL_END)) {
					coverUrl = item.href;
				}
			}
		}
	}

	public String getNcxPath() {
		return mNcxPath;
	}

	public BBBEPubMetaData getMetaData() {
		return bbBBBEPubMetaData;
	}

	public List<String> getSpineIdList() {
		return spineIdList;
	}

	public List<ManifestItem> getManifestIdList() {
		return manifestIdList;
	}
	
	public String getCoverUrl() {
		return coverUrl;
	}

	public class ManifestItem {
		String id;
		String href;
		String mediatype;

		public ManifestItem(String id, String href, String mediatype) {
			this.id = id;
			this.href = href;
			this.mediatype = mediatype;
		}

		public String getId() {
			return id;
		}

		public String getHref() {
			return href;
		}

		public String getMediatype() {
			return mediatype;
		}
	}
}
