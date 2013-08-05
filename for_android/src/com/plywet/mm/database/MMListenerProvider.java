package com.plywet.mm.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class MMListenerProvider {

	// 注册的监听器
	private final List listeners = new ArrayList();

	// 被监听对象
	private final Set obj = new HashSet();

	/**
	 * 调用监听器
	 */
	public final void deal() {
		Iterator iter_lis = this.listeners.iterator();

		while (iter_lis.hasNext()) {
			Object lis = iter_lis.next();

			Iterator iter_obj = this.obj.iterator();

			while (iter_obj.hasNext()) {
				dealListener(lis, iter_obj.next());
			}
		}

		this.obj.clear();
	}

	protected abstract void dealListener(Object lis, Object obj);

	public final void addListener(Object lis) {
		if (!this.listeners.contains(lis))
			this.listeners.add(lis);
	}

	public final void removeListener(Object lis) {
		this.listeners.remove(lis);
	}

	public final boolean addObject(Object obj) {
		return this.obj.add(obj);
	}
}
