package com.plywet.imeta.laf;

import java.util.HashMap;

import com.plywet.imeta.core.property.PropertyHandler;
import com.plywet.imeta.i18n.MessageHandler;

/**
 * LAFFactory提供了一种机制，能够为继承Handler接口的平台接口提供相应的实现类
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 */
public class LAFFactory {

	static Class<? extends Handler> _defMessageHandler = com.plywet.imeta.i18n.LAFMessageHandler.class;
	static Class<? extends Handler> _defPropertyHandler = com.plywet.imeta.core.property.OverlayPropertyHandler.class;

	// 注册继承Handler接口的平台接口和代理实现类的关联关系
	private static HashMap<Class<? extends Handler>, LAFDelegate<? extends Handler>> delegateRegistry = new HashMap<Class<? extends Handler>, LAFDelegate<? extends Handler>>();

	// 放置所有平台接口和默认的实现类的映射
	private static HashMap<String, Class<? extends Handler>> handlerRef = new HashMap<String, Class<? extends Handler>>();
	// 静态初始化，加载所有平台接口的代理实现类对应关系
	static {
		// message国际化消息默认实现类
		handlerRef.put(MessageHandler.class.getName(), _defMessageHandler);
		// 平台属性文件默认实现类
		handlerRef.put(PropertyHandler.class.getName(), _defPropertyHandler);
	}

	/**
	 * 通过接口类型获得实现类的代理类
	 * 
	 * @param <V>
	 *            继承了Handler的平台接口类
	 * @param handler
	 *            平台接口类对象(class)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static synchronized <V extends Handler> LAFDelegate<V> getDelegate(
			Class<V> handler) {
		LAFDelegate<V> l = (LAFDelegate<V>) delegateRegistry.get(handler);
		// 如果没有注册实现类，通过实现类的类对象，创建一个实现类的代理类
		if (l == null) {
			// 默认实现类对象
			Class<V> defaultHandler = (Class<V>) handlerRef.get(handler
					.getName());
			l = new LAFDelegate<V>(handler, defaultHandler);
			delegateRegistry.put(handler, l);
		}
		return l;
	}

	/**
	 * 通过继承Handler接口的平台接口，返回一个实现类
	 * 
	 * @param <V>
	 * @param handler
	 *            平台接口类对象(class)
	 * @return
	 */
	public static <V extends Handler> V getHandler(Class<V> handler) {
		// 首先获得代理类
		LAFDelegate<V> l = getDelegate(handler);
		// 然后获得实现类
		return (V) l.getHandler();
	}

}
