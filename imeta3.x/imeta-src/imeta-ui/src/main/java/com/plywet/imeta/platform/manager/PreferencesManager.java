package com.plywet.imeta.platform.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * 用户样式管理
 * 
 * @author 潘巍（PeterPan）
 * @since 2011-6-2 下午05:30:34
 * 
 */
public class PreferencesManager implements Serializable {

	private static final long serialVersionUID = -6396049164078801234L;

	private List<Theme> allThemes;

	private String theme = "aristo"; // default

	private int pageWidth;

	private int pageHeight;

	public List<Theme> getAllThemes() {
		if (allThemes == null) {
			allThemes = new ArrayList<Theme>();

		}
		return allThemes;
	}

	public String getTheme() {
		Map<String, String> params = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		if (params.containsKey("theme")) {
			theme = params.get("theme");
		}
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public int getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}

	public int getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(int pageHeight) {
		this.pageHeight = pageHeight;
	}
}

class Theme implements Serializable {

	private static final long serialVersionUID = 167431496487952038L;

	private String name;

	private String image;

	public Theme() {
	}

	public Theme(String name, String image) {
		this.name = name;
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
