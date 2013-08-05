package com.plywet.mm.platformtools;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;

/**
 * 唤醒CPU帮助类
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 下午12:22:19
 */
public class MMWakerLock {
	private static final String PKG = "MicroMsg.MMWakerLock";

	private PowerManager.WakeLock wl = null;

	public MMWakerLock(Context context) {
		this.wl = ((PowerManager) context.getSystemService("power"))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PKG);
	}

	public void acquire() {
		this.wl.acquire();
		new Handler().postDelayed(new MMWakerLockDelayed(this), 14000L);
	}

	public boolean isHeld() {
		return this.wl.isHeld();
	}

	@Override
	protected void finalize() {
		if (this.wl != null) {
			if (this.wl.isHeld())
				this.wl.release();
			this.wl = null;
		}
	}
}

class MMWakerLockDelayed implements Runnable {

	private MMWakerLock wl;

	MMWakerLockDelayed(MMWakerLock wl) {
		this.wl = wl;
	}

	@Override
	public void run() {
		this.wl.acquire();
		return;
	}

}
