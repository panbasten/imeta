package com.plywet.mm.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.plywet.mm.R;
import com.plywet.mm.booter.MMService;
import com.plywet.mm.platformtools.Log;
import com.plywet.mm.platformtools.MMAlive;
import com.plywet.mm.platformtools.MMAliveHandleInterface;
import com.plywet.mm.platformtools.MMEntryLock;
import com.plywet.mm.ui.tools.WhatsNewUI;

/**
 * 系统应用管理
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-16 上午06:28:17
 */
public class MMAppMgr {

	public static final String PKG = "MicroMsg.MMAppMgr";

	private static MMAppMgr app = null;

	private MMService mmService = null;

	// 保持活跃
	private boolean aliveState = false;

	// 上次的活动状态
	private boolean lastAliveState = false;

	// 激活控制器
	private MMAlive aliveHandle = new MMAlive(new MMAppMgrAliveHandle(this),
			false);

	private static MMAppMgr getInstance() {
		if (app == null)
			app = new MMAppMgr();
		return app;
	}

	public static void setMmService(MMService mmService) {
		getInstance().mmService = mmService;
	}

	public static void a(boolean aliveState) {
		getInstance().aliveState = aliveState;

	}

	/**
	 * 显示whatsnew页面
	 * 
	 * @param act
	 */
	public static void showWhatsnewActivity(Activity act) {
		if (!MMEntryLock.isLocked("show_whatsnew")) {
			Intent intent = new Intent();
			intent.setClass(act, WhatsNewUI.class);
			act.startActivity(intent);
		}
	}

	/**
	 * 显示网络医生页面
	 * 
	 * @param context
	 */
	public static void showNetworkDoctor(Context context) {
		if (!MMEntryLock.isLocked("network_doctor_shown")) {
			MMDialogUtils.showAlertDialog(context, R.string.network_doctor,
					R.string.app_tip, new MMWirelessSettingsListener(context),
					new MMNullListener());
		}
	}

	public static void cancelNotification() {
		if (getInstance().mmService != null) {
			getInstance().mmService.cancelNotification();
		}
	}

	/**
	 * 设置激活状态
	 * 
	 * @param alive
	 */
	public static void setAliveState(boolean alive) {
		getInstance().aliveState = alive;
		getInstance().aliveHandle.start(1000L);
	}

	public boolean isAliveState() {
		return aliveState;
	}

	public boolean isLastAliveState() {
		return lastAliveState;
	}

	public void setLastAliveState(boolean lastAliveState) {
		this.lastAliveState = lastAliveState;
	}

}

class MMAppMgrAliveHandle implements MMAliveHandleInterface {

	private MMAppMgr mgr;

	MMAppMgrAliveHandle(MMAppMgr mgr) {
		this.mgr = mgr;
	}

	@Override
	public boolean isAlive() {
		if (this.mgr.isAliveState() == this.mgr.isLastAliveState()) {
			Log.debug(MMAppMgr.PKG,
					"status not changed, cur=" + this.mgr.isAliveState());
			return true;
		}

		this.mgr.setLastAliveState(this.mgr.isAliveState());

		if (this.mgr.isAliveState()) {
			Log.debug(MMAppMgr.PKG, "[ACTIVATED MODE]");

			// TODO 激活其他后台程序
		} else {
			Log.debug(MMAppMgr.PKG, "[DEACTIVATED MODE]");
		}

		return true;
	}

}
