package com.plywet.imeta.platform.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.plywet.imeta.platform.exception.ImetaViewBeanException;

/**
 * 桌面列表的 View Bean
 * 
 * @author PeterPan
 * 
 */
@ManagedBean(name="desktopList")
@RequestScoped
public class DesktopListViewBean {

	/**
	 * 多桌面列表
	 */
	private List<DesktopBean> desktops = new ArrayList<DesktopBean>();

	/**
	 * 默认桌面索引
	 */
	private int defaultDesktopIndex = 2;

	/**
	 * 桌面个数
	 */
	private int desktopNum = 5;

	/**
	 * 桌面图标样式(1--小图标，0--大图标)
	 */
	private String desktopIconStyle;

	private boolean load;

	public DesktopListViewBean() {

	}

	public void init() throws ImetaViewBeanException {
		// TODO
		try {
			desktops.clear();
			for (int i = 0; i < desktopNum; i++) {
				DesktopBean db = new DesktopBean();
				for (int j = 0; j < 10; j++) {
					DesktopAppBean app = new DesktopAppBean(j, "应用" + i + "-"
							+ j, "这是应用" + j, "coda.png");
					db.addApp(app.addDefaultAttrs());
				}
				desktops.add(db);
			}
		} catch (Exception e) {
			throw new ImetaViewBeanException("初始化桌面出现错误", e);
		}
	}

	public List<DesktopBean> getDesktops() throws ImetaViewBeanException {
		if (!load) {
			init();
			load = true;
		}
		return desktops;
	}

	public void addDesktop(DesktopBean desktop) {
		this.desktops.add(desktop);
	}

	public int getDefaultDesktopIndex() {
		return defaultDesktopIndex;
	}

	public void setDefaultDesktopIndex(int defaultDesktopIndex) {
		this.defaultDesktopIndex = defaultDesktopIndex;
	}

	public String getDesktopIconStyle() {
		return desktopIconStyle;
	}

	public void setDesktopIconStyle(String desktopIconStyle) {
		this.desktopIconStyle = desktopIconStyle;
	}

	public int getDesktopNum() {
		return desktopNum;
	}

	public void setDesktopNum(int desktopNum) {
		this.desktopNum = desktopNum;
	}

}
