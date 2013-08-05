package com.plywet.mm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.plywet.mm.booter.MMService;
import com.plywet.mm.platformtools.Log;
import com.plywet.mm.platformtools.MMEntryLock;
import com.plywet.mm.ui.login.SelectLoginRegUI;
import com.plywet.mm.ui.login.WelcomeUI;

/**
 * 打开程序的第一个欢迎页面的Activity
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-11 上午07:58:06
 */
public class LauncherUI extends Activity {

	private static final String PKG = "MicroMsg.LauncherUI";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MMService.operate(this, "noop");
		Log.verbose(PKG, "on create + is root " + isTaskRoot());
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.verbose(PKG, "onpause");
	}

	@Override
	protected void onDestroy() {
		Log.verbose(PKG, "on destroy");
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -1) {
			showContent();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!MMEntryLock.isLocked("welcome_page_show")) {
			// 显示欢迎页面
			startActivityForResult(new Intent(this, WelcomeUI.class), 1);
		} else {
			showContent();
		}

	}

	private void showContent() {
		// 不是切换用户，并且用户已经登陆，直接跳转到首页
		if (getIntent().getBooleanExtra("Intro_Switch", false)) {
			Log.warn("PKG", "[Launching Application]");
			Intent intent = new Intent(this, MainTabUI.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		// 跳转到登陆页面
		else {
			startActivity(new Intent().setClass(this, SelectLoginRegUI.class));
		}

		finish();
	}

}
