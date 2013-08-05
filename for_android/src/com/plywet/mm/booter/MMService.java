package com.plywet.mm.booter;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Log;
import com.plywet.mm.platformtools.Utils;
import com.plywet.mm.platformtools.utils.MMConst;
import com.plywet.mm.platformtools.utils.MMLocaleUtils;
import com.plywet.mm.protocal.MMVersion;

/**
 * 服务：
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 上午08:24:41
 */
public class MMService extends Service {

	private static final String PKG = "MicroMsg.MMBoot";

	private MMNotification notification;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.debug(PKG, "[MMBoot onCreate]");
		setForeground(true);

		MMDebugProvider dp = new MMDebugProvider(this);

		String http = dp.getString("com.tencent.mm.debug.server.host.http");
		String socket = dp.getString("com.tencent.mm.debug.server.host.socket");

		// 设置日志级别
		Log.setLogLevel(Utils.defaultInteger(
				dp.getInteger("com.tencent.mm.debug.log.level"), 0));

		// 设置Test对象
		com.plywet.mm.test.Test.DISPLAY_ERRCODE = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.display_errcode"),
				false);
		com.plywet.mm.test.Test.DISPLAY_MSGSTATE = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.display_msgstate"),
				false);
		com.plywet.mm.test.Test.NETWORK_SIMULATE_FAULT = Utils
				.defaultBoolean(
						dp.getBoolean("com.tencent.mm.debug.test.network.simulate_fault"),
						false);
		com.plywet.mm.test.Test.NETWORK_FORCE_TOUCH = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.network.force_touch"),
				false);
		com.plywet.mm.test.Test.OUTPUT_TO_SD_CARDLOG = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.outputToSdCardlog"),
				false);
		com.plywet.mm.test.Test.CRASH_IS_EXIT = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.crashIsExit"), false);
		com.plywet.mm.test.Test.UPLOAD_LOG = Utils.defaultBoolean(
				dp.getBoolean("com.tencent.mm.debug.test.uploadLog"), false);

		// Test协议
		Log.debug(PKG, "try set test protocal version");
		if (!Utils
				.isEmpty(dp.getString(".com.tencent.mm.debug.log.setversion"))) {
			int ver = Integer.decode(dp
					.getString(".com.tencent.mm.debug.log.setversion"));
			MMVersion.setTestProtocalVersion(ver);
			Log.debug(
					PKG,
					"set up test protocal version = "
							+ Integer.toHexString(ver));
		}

		// 上传日志文件
		if (com.plywet.mm.test.Test.UPLOAD_LOG) {
			File pwDir = new File("/sdcard/plywet/");
			if (!pwDir.exists()) {
				pwDir.mkdirs();
			}

			File mmLog = new File("/sdcard/plywet/", "mm.log");
			if (!mmLog.exists()) {
				mmLog.mkdirs();
			} else {
				// TODO 通过获得的http和socket上传日志文档
			}
		}

		Log.setLog("/sdcard/plywet/mm.log", "plywet",
				MMVersion.TEST_PROTOCAL_VERSION);

		// 群聊
		MMLocaleUtils.getLanguage(getSharedPreferences(
				"com.tencent.mm_preferences", 0));
		MMConst.put("server.chatroom.hardcode_nicks",
				getString(R.string.hardcode_group));

	}

	/**
	 * 服务的操作
	 * 
	 * @param context
	 * @param param
	 */
	public static void operate(Context context, String param) {
		try {
			if (!"noop".equals(param)) {
				// TODO
				return;
			}

			// noop
			// 确保MMService服务开启
			Log.info(PKG, "ensure service running, type=" + param);
			Intent intent = new Intent();
			intent.setClassName(context, MMService.class.getName());
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(intent);
		} catch (Exception e) {

		}
	}

	public void cancelNotification() {
		if (this.notification != null)
			this.notification.cancelNotification();
	}
	
}
