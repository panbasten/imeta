package com.plywet.mm.ui.chatting;

import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.plywet.mm.ui.UIConst;

public final class ChattingUtils {

	private static final String[] hrefPatternStrings;

	private static final Pattern urlPattern;

	static {
		hrefPatternStrings = new String[] { "<a.+?href\\s*=\\s*\"\\s*(.+?)\\s*\"\\s*>(.+?)</a>" };

		urlPattern = Pattern
				.compile("((?:(http|https|Http|Https):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eouw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)");
	}

	public static SpannableString createSpannableString(Context context,
			String str, int dpi) {
		SpannableString ss;
		if (str == null || "".equals(str)) {
			ss = new SpannableString("");
		} else {
			ss = new SpannableString(str + " ");

			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				int bitId = UIConst.char2Bitmap(chars[i]);
				if(bitId == -1)continue;
				Drawable d = UIConst.getBitmapDrawable(context, bitId, dpi);
				if(d == null)continue;
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				ss.setSpan(new ImageSpan(d,1), i, i+1, 33);
			}
		}
		return ss;
	}
}
