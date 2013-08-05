package com.plywet.imeta.laf;

import java.util.HashSet;
import java.util.Iterator;

import com.plywet.imeta.core.log.Log;

/**
 * 根据LAFHandler中指定接口类型的实现类的代理类<br>
 * 主要用于添加监听器功能
 * 
 * @since 1.0 2010-1-20
 * @author 潘巍（Peter Pan）
 * 
 * @param <E>
 */
public class LAFDelegate<E extends Handler> {

	private static final Log log = Log.getLog(LAFDelegate.class.getName());

	// 实现类实例对象
	E handler;

	// 接口类对象,默认实现类对象
	Class<E> handlerClass, defaultHandlerClass = null;

	// 用于在实例的值改变需要通知的监听器对象存放的对象
	private HashSet<LAFChangeListener<E>> registry = new HashSet<LAFChangeListener<E>>();

	public LAFDelegate(Class<E> handler, Class<E> defaultHandler) {
		// 接口类对象
		this.handlerClass = handler;
		// 默认实现类对象
		this.defaultHandlerClass = defaultHandler;
		init();
	}

	private void init() {
		handler = loadHandler(defaultHandlerClass);
	}

	/**
	 * 外部通过实现类的类名更新代理的
	 * 
	 * @param classname
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E newHandlerInstance(String classname) throws ClassNotFoundException {
		E h = null;
		Class<E> c = null;
		c = (Class<E>) Class.forName(classname);
		h = loadHandler(c);
		return h;
	}

	private E loadHandler(Class<E> c) {
		E h = null;
		try {
			h = (E) c.newInstance();
		} catch (Exception e) {
			log.error("创建接口实例出现错误，实现类：" + c.getName());
		}
		if (h != null) {
			changeHandler(h);
		}
		return h;
	}

	/**
	 * 注册一个值改变监听者
	 * 
	 * @param listener
	 * @return
	 */
	public E registerChangeListener(LAFChangeListener<E> listener) {
		registry.add(listener);
		return handler;
	}

	/**
	 * 撤销注册一个值改变监听者
	 * 
	 * @param listener
	 */
	public void unregisterChangeListener(LAFChangeListener<E> listener) {
		registry.remove(listener);
	}

	public HashSet<LAFChangeListener<E>> getListeners() {
		return registry;
	}

	public void loadListeners(HashSet<LAFChangeListener<E>> listeners) {
		registry = listeners;
	}

	/**
	 * 值改变调用方法
	 * 
	 * @param handler
	 */
	public void changeHandler(E handler) {
		this.handler = handler;
		// 调用所有注册的监听器
		notifyListeners();
	}

	protected void notifyListeners() {
		Iterator<LAFChangeListener<E>> iterator = registry.iterator();
		while (iterator.hasNext()) {
			iterator.next().notify(handler);
		}
	}

	/**
	 * 获得该代理类的具体实例对象
	 * 
	 * @return
	 */
	public E getHandler() {
		return handler;
	}

}