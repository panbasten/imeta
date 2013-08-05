package com.plywet.mm.ui;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Log;
import com.plywet.mm.platformtools.android.BitmapFactory;

public class UIConst {

	private static int titleTextSizePixel;

	private static int smallTextSizePixel;

	private static UIConst uiConst = null;

	private Bitmap[] emoji = null;

	private UIConst(Context context) {
		titleTextSizePixel = context.getResources().getDimensionPixelSize(
				R.dimen.TitleTextSize);
		smallTextSizePixel = context.getResources().getDimensionPixelSize(
				R.dimen.SmallTextSize);
	}

	public static int char2Bitmap(char c) {
		int i = -1;

		if (c >= 57345 && c <= 57434)
			i = c - 57345;
		else if (c >= 57601 && c <= 57690)
			i = c + 'Z' - 57601;
		else if (c >= 57857 && c <= 57939)
			i = c + '´' - 57857;
		else if (c >= 58113 && c <= 58189)
			i = c + 'ć' - 58113;
		else if (c >= 58369 && c <= 58444)
			i = c + 'Ŕ' - 58369;
		else if (c >= 58625 && c <= 58679)
			i = c + 'Ơ' - 58625;

		return i;
	}

	public static Drawable getBitmapDrawable(Context context, int imgNum, int t) {
		try {
			if (getInstance(context).emoji == null) {
				int n = context.getAssets().list("emoji/").length;
				getInstance(context).emoji = new Bitmap[n];
			}

			if (imgNum >= getInstance(context).emoji.length) {
				return null;
			}

			if (getInstance(context).emoji[imgNum] == null) {
				getInstance(context).emoji[imgNum] = BitmapFactory
						.getBitmap(context.getAssets().open(
								"emoji/" + imgNum + ".png"));
			}

			Bitmap bm = getInstance(context).emoji[imgNum];
			Bitmap transBm;
			if (t == -1) {
				transBm = Bitmap.createScaledBitmap(bm, titleTextSizePixel,
						titleTextSizePixel, true);
			} else if (t == -2) {
				transBm = Bitmap.createScaledBitmap(bm, smallTextSizePixel,
						smallTextSizePixel, true);
			}else{
				transBm = Bitmap.createScaledBitmap(bm,t,t,true);
			}
			
			return new BitmapDrawable(transBm);

		} catch (IOException localIOException) {
			Log.error("", "");
			return null;
		}
	}

	private static UIConst getInstance(Context context) {
		if (uiConst == null)
			uiConst = new UIConst(context);
		return uiConst;
	}

	public static void clear() {
		if (uiConst != null && uiConst.emoji != null) {
			uiConst.emoji = null;
		}
		uiConst = null;
	}
}
