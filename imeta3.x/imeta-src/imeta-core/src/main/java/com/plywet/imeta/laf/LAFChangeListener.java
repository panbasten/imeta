package com.plywet.imeta.laf;

/**
 * 平台后台提供者对象改变监听者接口
 * 
 * @author 潘巍（Peter Pan）
 * @since 2011-4-20 上午11:39:17
 */
public interface LAFChangeListener<E> {
	/**
	 * 对象改变执行的方法
	 * 
	 * @param changedObject
	 *            改变后的对象
	 */
	public void notify(E changedObject);
}
