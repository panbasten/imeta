package com.plywet.mm.platformtools.android;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;

public interface BitmapFactoryInterface {

	/**
	 * 转换显示像素
	 * 
	 * @param context
	 * @param dpi
	 * @return
	 */
	public int transDpi(Context context, int dpi);

	/**
	 * 获得Bitmap
	 * 
	 * @param is
	 * @return
	 */
	public Bitmap getBitmap(InputStream is);

	/**
	 * 获得Bitmap
	 * 
	 * @param fileName
	 * @param dpi
	 * @return
	 */
	public Bitmap getBitmap(String fileName, float dpi);

	/**
	 * 获得显示类型
	 * 
	 * @param context
	 * @return
	 */
	public String getDisplayType(Context context);

}