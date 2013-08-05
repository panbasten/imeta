package com.plywet.mm.platformtools.android;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BitmapFactoryFor4 implements BitmapFactoryInterface {
	private static final float LDPI_LEVEL_FLOAT = 1.0F;
	private static final float HDPI_LEVEL_FLOAT = 1.5F;

	@Override
	public int transDpi(Context context, int dpi) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay().getMetrics(displayMetrics);
		return dpi * displayMetrics.densityDpi / 160;
	}

	@Override
	public Bitmap getBitmap(InputStream is) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDensity = 160;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeStream(is, null, opts);
	}

	@Override
	public Bitmap getBitmap(String fileName, float dpi) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		float f = 160.0F * dpi;
		opts.inDensity = (int) f;
		Bitmap localBitmap = BitmapFactory.decodeFile(fileName, opts);
		if (localBitmap != null)
			localBitmap.setDensity((int) f);
		return localBitmap;
	}

	@Override
	public String getDisplayType(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		Configuration conf = context.getResources().getConfiguration();
		String rtn = null;
		if (displayMetrics.density < LDPI_LEVEL_FLOAT) {
			rtn = "LDPI";
		} else if (displayMetrics.density >= HDPI_LEVEL_FLOAT) {
			rtn = "HDPI";
		} else {
			rtn = "MDPI";
		}

		if (conf.orientation != 2) {
			rtn += "_L";
		} else {
			rtn += "_P";
		}
		return rtn;
	}

}
