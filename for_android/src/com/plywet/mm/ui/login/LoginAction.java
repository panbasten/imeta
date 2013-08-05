package com.plywet.mm.ui.login;

import android.content.Intent;
import android.net.Uri;

/**
 * 登陆页面的Action
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 上午11:14:05
 */
public class LoginAction implements MMActionInterface {

	private LoginUI ui;

	LoginAction(LoginUI ui) {
		this.ui = ui;
	}

	@Override
	public void action(int key) {
		switch (key) {
		default:
		case 0:
			this.ui.startActivity(new Intent("android.intent.action.VIEW", Uri
					.parse("http://aq.qq.com/cn/findpsw/findpsw_index")));
			break;
		case 1:
			this.ui.doStartActivity(ForgetPwdMobileUI.class);
			break;
		case 2:
			this.ui.startActivity(new Intent("android.intent.action.VIEW", Uri
					.parse("http://weixin.qq.com/getpassword?lang=zh_CN")));
			break;
		}
	}

}
