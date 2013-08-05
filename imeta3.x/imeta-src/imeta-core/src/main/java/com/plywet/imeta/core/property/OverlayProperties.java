package com.plywet.imeta.core.property;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.plywet.imeta.utils.FileUtils;

/**
 * 全局属性配置对象
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 上午10:36:31
 */
public class OverlayProperties extends Properties implements PropertyHandler {

	private static final long serialVersionUID = 1L;
	private String name = null;

	public OverlayProperties(String file) throws IOException {
		load(file);
	}

	/**
	 * 外部加载属性配置文件<br>
	 * 该方法是在外部重新加载该属性对象
	 * 
	 * @param filename
	 * @return
	 */
	@Override
	public boolean loadProps(String filename) {
		try {
			return load(filename);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 清空和重载属性文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private boolean load(String filename) throws IOException {
		URL url = FileUtils.getURL(filename);
		if (url == null)
			return false;
		clear();
		load(url.openStream());
		this.name = filename;
		return true;
	}

	public String getName() {
		return name;
	}
}
