package com.plywet.mm.system;

/**
 * 计时器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 下午07:38:24
 */
public class Timer {
	private long startTime = System.currentTimeMillis();

	public void setStartTime() {
		this.startTime = System.currentTimeMillis();
	}

	public long interval() {
		return System.currentTimeMillis() - this.startTime;
	}
}
