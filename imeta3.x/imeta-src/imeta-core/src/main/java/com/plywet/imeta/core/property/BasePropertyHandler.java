package com.plywet.imeta.core.property;

import com.plywet.imeta.i18n.MessageHandler;
import com.plywet.imeta.laf.LAFChangeListener;
import com.plywet.imeta.laf.LAFFactory;

/**
 * 该类是基础的访问属性文件的入口<br>
 * 该类是为动态属性加载器的静态访问器，可以被所有的类用来请求访问属性文件。<br>
 * 该静态的访问器提供了一个来自LAFFactory的引用。
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 */
public class BasePropertyHandler implements LAFChangeListener<PropertyHandler> {

	static BasePropertyHandler instance = null;
	protected PropertyHandler handler = null;
	Class<PropertyHandler> clazz = PropertyHandler.class;

	static {
		getInstance();
	}

	private BasePropertyHandler() {
		init();
	}

	/**
	 * 依赖LAFFactory返回一个类
	 * 
	 * @see MessageHandler
	 */
	private void init() {
		// 通过接口获得默认的实现类实例
		handler = (PropertyHandler) LAFFactory.getHandler(clazz);
	}

	public static BasePropertyHandler getInstance() {
		if (instance == null) {
			instance = new BasePropertyHandler();
		}
		return instance;
	}

	protected PropertyHandler getHandler() {
		return handler;
	}

	protected static PropertyHandler getInstanceHandler() {
		return getInstance().getHandler();
	}

	/**
	 * 从属性列表中返回一个给定key的值
	 * 
	 * @param key
	 * @return 如果key没有找到返回null
	 */
	public static String getProperty(String key) {
		return getInstanceHandler().getProperty(key);
	}

	/**
	 * 从属性列表中返回一个给定key的值，当key没有找到时返回defValue
	 * 
	 * @param key
	 * @param defValue
	 * @return 该字符串表示绑定值或者默认值
	 */
	public static String getProperty(String key, String defValue) {
		return getInstanceHandler().getProperty(key, defValue);
	}

	public void notify(PropertyHandler changedObject) {
		handler = changedObject;
	}

}
