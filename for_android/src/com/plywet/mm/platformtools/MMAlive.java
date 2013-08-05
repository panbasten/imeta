package com.plywet.mm.platformtools;

import android.os.Handler;
import android.os.Message;

/**
 * 保持激活状态的帮助类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-18 上午10:14:39
 */
public class MMAlive extends Handler {

	/**
	 * 操作编号
	 */
	private static int index;

	/**
	 * 当前编号
	 */
	private int thisIndex;

	/**
	 * 持续激活
	 */
	private boolean auto;

	/**
	 * 延迟时间
	 */
	private long delay = 0L;

	private final MMAliveHandleInterface handle;

	public MMAlive(MMAliveHandleInterface handle, boolean auto) {
		this.handle = handle;
		this.auto = auto;

		if (index >= 8192) {
			index = 0;
		}
		index++;
		this.thisIndex = index;
	}

	public void stop() {
		removeMessages(this.thisIndex);
	}

	public void start(long delay) {
		this.delay = delay;
		stop();
		sendEmptyMessageDelayed(this.thisIndex, delay);
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg.what != this.thisIndex || this.handle == null) {
			return;
		}

		if (this.handle.isAlive() && this.auto) {
			sendEmptyMessageDelayed(this.thisIndex, this.delay);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

}
