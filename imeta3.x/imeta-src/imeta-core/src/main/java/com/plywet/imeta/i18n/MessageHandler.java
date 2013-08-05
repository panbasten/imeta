package com.plywet.imeta.i18n;

import com.plywet.imeta.laf.Handler;

/**
 * 该接口用于提供获得message的国际化(i18n)属性文件中属性值的相关方法
 * 
 * @since 1.0 2010-1-19
 * @author 潘巍（Peter Pan）
 * 
 */
public interface MessageHandler extends Handler {

	/**
	 * 从默认（系统全局）的message属性文件中，根据key获得相应值
	 * 
	 * @param key
	 *            KEY
	 * @return
	 */
	public String getString(String key);

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key获得相应值
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @return
	 */
	public String getString(String packageName, String key);

	/**
	 * 从指定包名的目录下面的message属性文件中，根据key获得相应值<br>
	 * 可以指定相应的替换参数
	 * 
	 * @param packageName
	 *            指定的包路径
	 * @param key
	 *            KEY
	 * @param parameters
	 *            用于属性值替换参数，用于替换配置中的{0},{1},{2}....
	 * @return
	 */
	public String getString(String packageName, String key,
			String... parameters);

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
	 *            用于属性值替换参数，用于替换配置中的{0},{1},{2}....
	 * @return
	 */
	public String getString(String packageName, String key,
			Class<?> resourceClass, String... parameters);
}
