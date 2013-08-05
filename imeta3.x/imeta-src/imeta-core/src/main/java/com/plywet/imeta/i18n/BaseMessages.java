package com.plywet.imeta.i18n;

import com.plywet.imeta.laf.LAFChangeListener;
import com.plywet.imeta.laf.LAFFactory;

/**
 * 该类是基础的访问message的入口<br>
 * 通过代理key调用注册在LAFFactory里的合适供应类实现国际化。
 * 
 * 
 * @since 1.0 2010-1-19
 * @author 潘巍（Peter Pan）
 * 
 */
public class BaseMessages implements LAFChangeListener<MessageHandler> {
	static BaseMessages instance = null;
	protected MessageHandler handler = null;
	Class<MessageHandler> clazz = MessageHandler.class;

	static {
		getInstance();
	}

	private BaseMessages() {
		// 初始化创建一个单例
		init();
	}

	private void init() {
		// 通过LAFFactory工厂获得或创建一个默认的消息提供者的实现类
		handler = (MessageHandler) LAFFactory.getHandler(clazz);
	}

	public static BaseMessages getInstance() {
		if (instance == null) {
			instance = new BaseMessages();
		}
		return instance;
	}

	protected MessageHandler getHandler() {
		return handler;
	}

	protected static MessageHandler getInstanceHandler() {
		return getInstance().getHandler();
	}

	/**
	 * 从默认（系统全局）的message属性文件中，根据key获得相应值
	 * 
	 * @param key
	 *            KEY
	 * @return
	 */
	public static String getString(String key) {
		return getInstanceHandler().getString(key);
	}

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key获得相应值
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @return
	 */
	public static String getString(String packageName, String key) {
		return getInstanceHandler().getString(packageName, key);
	}

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key获得相应值<br>
	 * 可以指定相应的替换参数
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @param parameters
	 *            用于属性值替换参数
	 * @return
	 */
	public static String getString(String packageName, String key,
			String... parameters) {
		return getInstanceHandler().getString(packageName, key, parameters);
	}

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key和资源类获得相应值
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @param resourceClass
	 *            资源类
	 * @param parameters
	 *            用于属性值替换参数
	 * @return
	 */
	public static String getString(String packageName, String key,
			Class<?> resourceClass, String... parameters) {
		return getInstanceHandler().getString(packageName, key, resourceClass,
				parameters);
	}

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key获得相应值
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @param parameters
	 *            用于属性值替换参数
	 * @return
	 */
	public static String getString(Class<?> packageClass, String key,
			String... parameters) {
		return getInstanceHandler().getString(
				packageClass.getPackage().getName(), key, packageClass,
				parameters);
	}

	/**
	 * 从指定类包的目录下面的message属性文件中，根据key和资源类获得相应值
	 * 
	 * @param packageClass
	 *            指定的类包对象
	 * @param key
	 *            KEY
	 * @param resourceClass
	 *            资源类
	 * @param parameters
	 *            用于属性值替换参数
	 * @return
	 */
	public static String getString(Class<?> packageClass, String key,
			Class<?> resourceClass, String... parameters) {
		return getInstanceHandler().getString(
				packageClass.getPackage().getName(), key, resourceClass,
				parameters);
	}

	/**
	 * 从指定类包的目录下面的message属性文件中，根据key获得相应值
	 * 
	 * @param packageClass
	 *            指定的类包对象
	 * @param key
	 *            KEY
	 * @param parameters
	 *            用于属性值替换参数
	 * @return
	 */
	public static String getString(Class<?> packageClass, String key,
			Object... parameters) {
		String[] strings = new String[parameters.length];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = parameters[i] != null ? parameters[i].toString() : "";
		}
		return getString(packageClass, key, strings);
	}

	public void notify(MessageHandler changedObject) {
		handler = changedObject;
	}
}
