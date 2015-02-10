/*******************************************************************************
 *  
 *  Copyright (c) 2013 blinkbox Entertainment Limited. All rights reserved.
 *
 *  
 *******************************************************************************/
package com.blinkbox.java.book.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	private static final int IO_COPY_BUFFER_SIZE = 1024 * 4;

	/**
	 * Returns the contents of the InputStream as a byte[]
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		copy(in, result);
		result.flush();
		return result.toByteArray();
	}

	/**
	 * Reads data from the InputStream, using the specified buffer size.
	 * 
	 * This is meant for situations where memory is tight, since it prevents
	 * buffer expansion.
	 * 
	 * @param in the stream to read data from
	 * @param size
	 *            the size of the array to create
	 * @return the array, or null
	 * @throws IOException
	 */
	public static byte[] toByteArray(InputStream in, int size) throws IOException {

		try {
			ByteArrayOutputStream result;

			if (size > 0) {
				result = new ByteArrayOutputStream(size);
			} else {
				result = new ByteArrayOutputStream();
			}

			copy(in, result);
			result.flush();
			return result.toByteArray();
		} catch (OutOfMemoryError error) {
			// Return null so it gets loaded lazily.
			return null;
		}

	}

	/**
	 * @param nrRead
	 * @param totalNrNread
	 * @return
	 */
	protected static int calcNewNrReadSize(int nrRead, int totalNrNread) {
		if (totalNrNread < 0) {
			return totalNrNread;
		}
		if (totalNrNread > (Integer.MAX_VALUE - nrRead)) {
			return -1;
		} else {
			return (totalNrNread + nrRead);
		}
	}

	/**
	 * Copies the contents of the InputStream to the OutputStream.
	 * 
	 * @param in
	 * @param out
	 * @return the nr of bytes read, or -1 if the amount greater than Integer.MAX_VALUE
	 * @throws IOException
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[IO_COPY_BUFFER_SIZE];
		int readSize = -1;
		int result = 0;
		while ((readSize = in.read(buffer)) >= 0) {
			out.write(buffer, 0, readSize);
			result = calcNewNrReadSize(readSize, result);
		}
		out.flush();
		return result;
	}
}
