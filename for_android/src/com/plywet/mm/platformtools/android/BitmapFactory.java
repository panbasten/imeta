package com.plywet.mm.platformtools.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;

import com.plywet.mm.platformtools.Log;

/**
 * Bitmap的工厂类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 上午10:29:11
 */
public class BitmapFactory {

	private static final String LOG_TYPE = "MicroMsg.BitmapFactory";

	private static BitmapFactoryInterface getBitmapFactory() {
		BitmapFactoryInterface bf;
		if (Integer.valueOf(Build.VERSION.SDK).intValue() <= 3)
			bf = new BitmapFactoryFor3();
		else
			bf = new BitmapFactoryFor4();
		return bf;
	}

	/**
	 * 获得转换后的dpi
	 * 
	 * @param context
	 * @param dpi
	 * @return
	 */
	public static int transDpi(Context context, int dpi) {
		BitmapFactoryInterface bf = getBitmapFactory();
		return bf.transDpi(context, dpi);
	}

	/**
	 * 获得Bitmap
	 * 
	 * @param is
	 * @return
	 */
	public static Bitmap getBitmap(InputStream is) {
		BitmapFactoryInterface bf = getBitmapFactory();
		return bf.getBitmap(is);
	}

	/**
	 * 获得Bitmap
	 * 
	 * @param fileName
	 * @param dpi
	 * @return
	 */
	public static Bitmap getBitmap(String fileName, float dpi) {
		BitmapFactoryInterface bf = getBitmapFactory();
		return bf.getBitmap(fileName, dpi);
	}

	/**
	 * 通过URL获得Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String url) {
		try {
			Log.debug(LOG_TYPE, "get bitmap from url:" + url);
			HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
					url).openConnection();
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.connect();
			Bitmap b = getBitmap(localHttpURLConnection.getInputStream());
			return b;
		} catch (IOException e) {
			Log.error(LOG_TYPE, "get bitmap from url failed");
			return null;
		}
	}

	/**
	 * 获得DisplayMetrics的基本信息<br>
	 * 
	 * @param context
	 * @return
	 */
	public static String getInfo(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels + "x" + dm.widthPixels;
	}

	/**
	 * 获得显示类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getDisplayType(Context context) {
		BitmapFactoryInterface bf = getBitmapFactory();
		return bf.getDisplayType(context);
	}

}
