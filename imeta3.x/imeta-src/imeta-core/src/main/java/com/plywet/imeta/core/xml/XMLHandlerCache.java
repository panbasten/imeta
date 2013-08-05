package com.plywet.imeta.core.xml;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 单例，用来帮助从XML DOM树中快速查找，<br>
 * 原理是你常常循环在一个节点中出现的指定标签。<br>
 * 如果出现20个，你可以通过0..19索引获得。<br>
 * 每次我们如下操作<br>
 * - found node 0<br>
 * - found node 0, 1<br>
 * - found node 0, 1, 2<br>
 * - ...<br>
 * 所以查找索引19节点的时间比索引0的时间平均大20倍<br>
 * 我们可以通过缓存索引18的位置来解决，从该位置开始返回。<br>
 * 该类是单例的，用其余的基础代码来保证每一个100%兼容
 * 
 * @since 1.0 2010-1-27
 * @author 潘巍（Peter Pan）
 * 
 */
public class XMLHandlerCache {
	public static final int MAX_NUMBER_OF_ENTRIES = 500;

	private static XMLHandlerCache cache;

	private Hashtable<XMLHandlerCacheEntry, Integer> hashtable;
	private ArrayList<XMLHandlerCacheEntry> list;

	private int cacheHits;

	private XMLHandlerCache() {
		hashtable = new Hashtable<XMLHandlerCacheEntry, Integer>(
				MAX_NUMBER_OF_ENTRIES);
		list = new ArrayList<XMLHandlerCacheEntry>(MAX_NUMBER_OF_ENTRIES);

		cacheHits = 0;
	}

	public synchronized static final XMLHandlerCache getInstance() {
		if (cache != null)
			return cache;

		cache = new XMLHandlerCache();
		return cache;
	}

	/**
	 * 存储一个缓冲实体
	 * 
	 * @param entry
	 *            缓冲实体
	 * @param lastChildNr
	 *            上一个子的位置
	 */
	public synchronized void storeCache(XMLHandlerCacheEntry entry,
			int lastChildNr) {
		hashtable.put(entry, Integer.valueOf(lastChildNr));
		list.add(entry);

		if (list.size() > MAX_NUMBER_OF_ENTRIES) {
			// 简单方式：列表头部的是最旧的
			XMLHandlerCacheEntry cacheEntry = list.get(0);

			// 从缓存中移除该缓冲实体
			hashtable.remove(cacheEntry);

			// 从列表中移除
			list.remove(0);
		}
	}

	/**
	 * 检索我们留下的上一个子节点
	 * 
	 * @param entry
	 *            要查询的缓冲实体
	 * @return 上一个子节点的位置或者-1如果没有找到。
	 */
	public int getLastChildNr(XMLHandlerCacheEntry entry) {
		Integer lastChildNr = hashtable.get(entry);
		if (lastChildNr != null) {
			cacheHits++;
			return lastChildNr.intValue();
		}
		return -1;
	}

	/**
	 * @return 缓存命中的统计数据
	 */
	public int getCacheHits() {
		return cacheHits;
	}

	/**
	 * 允许你（重新）设置缓存命中次数。
	 * 
	 * @param cacheHits
	 *            缓存命中次数
	 */
	public void setCacheHits(int cacheHits) {
		this.cacheHits = cacheHits;
	}

	/**
	 * 清空缓存
	 * 
	 */
	public synchronized void clear() {
		this.hashtable.clear();
		this.list.clear();
	}
}
