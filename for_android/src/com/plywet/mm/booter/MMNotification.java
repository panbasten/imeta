package com.plywet.mm.booter;

import android.app.NotificationManager;
import android.content.Context;

import com.plywet.mm.platformtools.Log;

/**
 * 通知管理
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 上午06:25:03
 */
public class MMNotification {

	private static final String PKG = "MicroMsg.MMNotification";

	private boolean hasUpdate;

	private long lastUpdateTime;

	private Context context;

	public MMNotification(Context context) {
		this.context = context;
		this.lastUpdateTime = 0L;
		this.hasUpdate = false;
	}

	/**
	 * 更新通知
	 * 
	 * @param silent
	 *            是否安静更新
	 */
	private void updateNotitfyInfo(boolean silent) {
		Log.debug(PKG, "updateNotitfyInfo : silent = " + silent);
		this.hasUpdate = true;

		if (!silent) {
			Log.debug(PKG, "updateNotifyInfo : modify lastNotSilentTime = ");
			this.lastUpdateTime = System.currentTimeMillis();
		}
	}

	public void cancelNotification() {
		Log.debug(PKG, "cancelNotification");
		NotificationManager manager = (NotificationManager) this.context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		if(manager!=null){
			this.hasUpdate = false;
			manager.cancel(0);
		}
	}
}
