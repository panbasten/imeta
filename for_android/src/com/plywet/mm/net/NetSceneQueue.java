package com.plywet.mm.net;

/**
 * 网络场景队列<br>
 * 
 * 用于请求网络资源的队列类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-18 下午12:04:58
 */
public class NetSceneQueue {

	private static NetSceneQueue sq = null;

	private NetSceneQueue() {

	}

	public static NetSceneQueue getInstance() {
		if (sq == null) {
			sq = new NetSceneQueue();
		}
		return sq;
	}
}
