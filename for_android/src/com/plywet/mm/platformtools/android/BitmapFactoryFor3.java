package com.plywet.mm.platformtools.android;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapFactoryFor3 implements BitmapFactoryInterface {


	@Override
	public int transDpi(Context context, int dpi) {
		return dpi;
	}

	@Override
	public Bitmap getBitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}


	@Override
	public Bitmap getBitmap(String fileName, float dpi) {
		return BitmapFactory.decodeFile(fileName);
	}

	@Override
	public String getDisplayType(Context context) {
		Configuration conf = context.getResources().getConfiguration();
		String rtn = "MDPI";
		if (conf.orientation == 2) {
			return rtn + "_L";
		}

		return rtn + "_P";
	}
}
