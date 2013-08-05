package com.plywet.imeta.platform.view;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌面对象bean
 * 
 * @author PeterPan
 * 
 */
public class DesktopBean {

	private List<DesktopAppBean> apps = new ArrayList<DesktopAppBean>();

	public List<DesktopAppBean> getApps() {
		return apps;
	}

	public void addApp(DesktopAppBean app) {
		this.apps.add(app);
	}

}
