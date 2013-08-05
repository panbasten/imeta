package com.plywet.imeta.i18n;

import java.util.Locale;

/**
 * message国际化配置访问抽象类
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 */
public abstract class AbstractMessageHandler implements MessageHandler {

	/**
	 * 强制覆盖，通过动态类加载来允许单例实例化
	 * 
	 * @return MessageHandler
	 */
	public synchronized static MessageHandler getInstance() {
		return null;
	}

	/**
	 * 强制覆盖，具体实现类必须提供实例化
	 * 
	 * @return Locale
	 */
	public synchronized static Locale getLocale() {
		return null;
	}

	/**
	 * 强制覆盖，具体实现类必须提供实例化
	 * 
	 * @param newLocale
	 */
	public synchronized static void setLocale(Locale newLocale) {
	}

}