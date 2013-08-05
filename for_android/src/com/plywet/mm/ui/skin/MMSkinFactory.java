package com.plywet.mm.ui.skin;

import java.util.Map;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class MMSkinFactory implements LayoutInflater.Factory {

	private Context context;

	private Context newContext;

	private String pkg;

	private Map localHashMap;

	private String defaultPackage = "com.plywet.mmskin";

	public MMSkinFactory(Context context) {
		this.context = context;
		init();
	}

	public void init() {
		String str = getSkinPackageName();
		if (str == null || "".equals(str)) {
			this.newContext = null;
			this.pkg = "";
		} else {
			this.pkg = str;
			buildContext();
		}
	}

	public void destroy() {
		this.context = null;
		this.newContext = null;
	}

	/**
	 * 通过ID获得皮肤中的绘图对象
	 * 
	 * @param id
	 * @return
	 */
	public final Drawable getDrawableObject(int id) {
		Object localObject;
		if (this.newContext == null) {
			localObject = this.context.getResources().getDrawable(id);
		} else {
			String str1 = this.context.getResources().getResourceName(id);
			String str2 = str1.substring(1 + str1.lastIndexOf("/"));
			int newId = this.newContext.getResources().getIdentifier(str2,
					"drawable", this.defaultPackage);
			if (newId != 0) {
				localObject = this.newContext.getResources().getDrawable(newId);
			} else {
				newId = this.newContext.getResources().getIdentifier(str2,
						"color", this.defaultPackage);
				if (newId != 0) {
					localObject = new ColorDrawable(this.newContext
							.getResources().getColor(newId));
				} else {
					newId = this.newContext.getResources().getIdentifier(str2,
							"anim", this.defaultPackage);
					if (newId != 0) {
						localObject = this.newContext.getResources()
								.getDrawable(newId);
					} else {
						localObject = this.context.getResources().getDrawable(
								id);
					}
				}
			}
		}
		return (Drawable) localObject;
	}

	/**
	 * 获得皮肤的包名
	 * 
	 * @return
	 */
	public String getSkinPackageName() {
		return this.context.getSharedPreferences("com.tencent.mm_preferences",
				0).getString("skin_pkg_name", "");
	}

	private void buildContext() {
		XmlResourceParser localXmlResourceParser;

		try {
			this.newContext = this.context.createPackageContext(this.pkg, 2);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return null;
	}

}
