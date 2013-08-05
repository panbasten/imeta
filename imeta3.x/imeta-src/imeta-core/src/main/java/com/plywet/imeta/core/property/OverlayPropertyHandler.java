package com.plywet.imeta.core.property;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;

import com.plywet.imeta.utils.Const;
import com.plywet.imeta.utils.FileUtils;

/**
 * 全局（默认）的属性配置文件访问接口的实现类<br>
 * 用于访问全局的属性配置文件Const.ENVIRONMENT_PROPERTIES，<br>
 * 以及其他的通过loadProps方法加载到该类中的属性配置文件
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 上午09:49:40
 */
public class OverlayPropertyHandler implements PropertyHandler {

	private static PropertyHandler instance = null;

	private LinkedList<OverlayProperties> propList = new LinkedList<OverlayProperties>();

	public OverlayPropertyHandler() {
		initProps();
	}

	public static PropertyHandler getInstance() {
		if (instance == null) {
			instance = new OverlayPropertyHandler();
		}
		return instance;
	}

	/**
	 * 初始化平台默认的属性配置文件<br>
	 * 首先加载平台全局的配置文件Const.ENVIRONMENT_PROPERTIES
	 * 
	 * @return
	 */
	protected boolean initProps() {
		String altFile = Const.ENVIRONMENT_PROPERTIES;
		if (altFile != null) {
			return loadProps(altFile);
		}
		return false;
	}

	/**
	 * 静态方法：通过key获得属性值
	 * 
	 * @param key
	 *            KEY
	 * @return
	 */
	public static String getLAFProp(String key) {
		return getInstance().getProperty(key);
	}

	/**
	 * 静态方法：通过key获得属性值，当属性值为空时使用默认值
	 * 
	 * @param key
	 *            KEY
	 * @param defValue
	 *            属性值
	 * @return
	 */
	public static String getLAFProp(String key, String defValue) {
		return getInstance().getProperty(key, defValue);
	}

	/**
	 * 外部加载属性配置文件<br>
	 * 由于该对象是属性对象的集合，所以会添加一个新的属性对象到列表中。
	 * 
	 * @param filename
	 *            文件路径
	 * @return
	 */
	@Override
	public boolean loadProps(String filename) {
		try {
			OverlayProperties ph = new OverlayProperties(filename);
			if (ph != null) {
				propList.addFirst(ph);
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 通过key获得属性值
	 * 
	 * @param key
	 *            KEY
	 * @return
	 */
	@Override
	public String getProperty(String key) {
		String s = null;
		Iterator<OverlayProperties> i = propList.iterator();
		while (i.hasNext()) {
			s = i.next().getProperty(key);
			if (s != null)
				return s;
		}
		return s;
	}

	/**
	 * 通过key获得属性值，当属性值为空时使用默认值
	 * 
	 * @param key
	 *            KEY
	 * @param defValue
	 *            属性值
	 * @return
	 */
	@Override
	public String getProperty(String key, String defValue) {
		String s = getProperty(key);
		if (s != null) {
			return s;
		}
		return defValue;
	}

	/**
	 * 通过文件名称判断其是否存在
	 * 
	 * @param filename
	 * @return
	 */
	public boolean exists(String filename) {
		try {
			return (FileUtils.getURL(filename) != null);
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
