// Copyright (c) 2014 blinkbox Entertainment Limited. All rights reserved.
package com.blinkbox.java.book.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapDecoder {

    /**
     * @param data
     * @param decodeConfig
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap decodeBitmap(byte[] data, Config decodeConfig, int maxWidth, int maxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap = null;

        if (maxWidth == 0 && maxHeight == 0) {
            decodeOptions.inPreferredConfig = decodeConfig;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
        } else {
            // If we have to resize this image, first get the natural bounds.
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            // Then compute the dimensions we would ideally like to decode to.
            int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

            // Decode to the nearest power of two scaling factor.
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inPreferQualityOverSpeed = true;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);

            // If necessary, scale down to the maximal acceptable size.
            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }
        return bitmap;
    }

    /**
     * Scales one side of a rectangle to fit aspect ratio.
     *
     * @param maxPrimary      Maximum size of the primary dimension (i.e. width for max width), or zero to maintain aspect ratio with secondary dimension
     * @param maxSecondary    Maximum size of the secondary dimension, or zero to maintain aspect ratio with primary dimension
     * @param actualPrimary   Actual size of the primary dimension
     * @param actualSecondary Actual size of the secondary dimension
     */
    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;

        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
}
