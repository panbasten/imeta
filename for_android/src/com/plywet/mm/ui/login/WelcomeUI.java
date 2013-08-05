package com.plywet.mm.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.MMEntryLock;

/**
 * 欢迎页面
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-9 上午11:43:05
 */
public class WelcomeUI extends Activity {
	@Override
	protected void onCreate(Bundle paramBundle) {
		requestWindowFeature(1);
		super.onCreate(paramBundle);
		setContentView(R.layout.welcome);
		// 锁定欢迎页面
		MMEntryLock.addLock("welcome_page_show");
		
		// 延时1700ms
		new Handler().postDelayed(new WelcomeDelayed(this), 1700L);
	}

}

/**
 * 欢迎页面的等候跳转操作
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-9 下午12:06:47
 */
class WelcomeDelayed implements Runnable {

	private WelcomeUI ui;

	WelcomeDelayed(WelcomeUI ui) {
		this.ui = ui;
	}

	@Override
	public void run() {
		this.ui.setResult(-1, new Intent());
		this.ui.finish();
	}
}
