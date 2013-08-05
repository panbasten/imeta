package com.plywet.mm.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.plywet.mm.R;
import com.plywet.mm.ui.MMActivity;
import com.plywet.mm.ui.MMAppMgr;
import com.plywet.mm.ui.MMDialogUtils;

/**
 * 登陆页面
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 上午12:20:01
 */
public class LoginUI extends MMActivity {

	private EditText account;

	private EditText password;

	private String s_account;

	private String s_password;

	private MMActionInterface action = new LoginAction(this);

	private ProgressDialog progressDialog = null;

	@Override
	protected int getCententLayoutId() {
		return R.layout.login;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public MMActionInterface getAction() {
		return action;
	}

	public String getS_account() {
		return s_account;
	}

	public void setS_account(String s_account) {
		this.s_account = s_account;
	}

	public String getS_password() {
		return s_password;
	}

	public void setS_password(String s_password) {
		this.s_password = s_password;
	}

	public EditText getAccount() {
		return account;
	}

	public EditText getPassword() {
		return password;
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		setTitle(R.string.app_name);

		MMAppMgr.cancelNotification();

		this.account = (EditText) findViewById(R.id.login_account_auto);
		String str = getSharedPreferences("com.plywet.mm_preferences", 0)
				.getString("login_user_name", "");
		this.account.setText(str);

		this.password = (EditText) findViewById(R.id.login_password_et);

		findViewById(R.id.login_foget_btn).setOnClickListener(
				new LoginFogetListener(this));

		enableLogo();

		setTitle(R.string.login_title);

		setRightBtn(R.string.login_login, new LoginLoginListener(this));

		setBtn4(new LoginBackListener(this));

	}

	@Override
	public void onDestroy() {
		// 如果程序加载对话框存在，首先停止该对话框
		if(this.progressDialog != null){
			this.progressDialog.dismiss();
		}
		
		// TODO 从后台队列中删除提交
		
		super.onDestroy();
	}
}

/**
 * 忘记密码单击监听器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-10 上午12:32:01
 */
final class LoginFogetListener implements View.OnClickListener {

	private LoginUI ui;

	LoginFogetListener(LoginUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {
		String[] menu = new String[] {
				this.ui.getResources().getString(R.string.reg_forgetpwd_byqq),
				this.ui.getResources().getString(
						R.string.reg_forgetpwd_bymobile),
				this.ui.getResources()
						.getString(R.string.reg_forgetpwd_byemail) };

		MMDialogUtils
				.showDialog(this.ui, null, menu, null, this.ui.getAction());
	}

}

/**
 * 登陆按钮监听器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 上午07:09:41
 */
final class LoginLoginListener implements View.OnClickListener {

	private LoginUI ui;

	LoginLoginListener(LoginUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {

		this.ui.setS_account(this.ui.getAccount().getText().toString().trim());
		this.ui.setS_password(this.ui.getPassword().getText().toString());

		// 验证账户名称不能为空
		if ("".equals(this.ui.getS_account())) {
			MMDialogUtils
					.showAlertDialog(this.ui,
							R.string.verify_username_null_tip,
							R.string.login_err_title);
			return;
		}

		// 验证密码不能为空
		if ("".equals(this.ui.getS_password())) {
			MMDialogUtils
					.showAlertDialog(this.ui,
							R.string.verify_password_null_tip,
							R.string.login_err_title);
			return;
		}
		this.ui.onBodyTouch();

		// TODO 后台验证，往NetSceneQueue队列中放置一个提交，

		// 显示程序加载对话框
		
		ProgressDialog progressDialog =MMDialogUtils.showProgressDialog(this.ui,
				this.ui.getString(R.string.app_tip),
				this.ui.getString(R.string.login_logining),
				new LoginCancelListener(this));
		
		this.ui.setProgressDialog(progressDialog);
	}
}

/**
 * 登陆取消事件
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-18 上午11:45:13
 */
final class LoginCancelListener implements DialogInterface.OnCancelListener {

	private LoginLoginListener listener;

	LoginCancelListener(LoginLoginListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO 从NetSceneQueue队列中删除该提交

	}

}

/**
 * 登陆返回按钮监听器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 上午07:30:34
 */
final class LoginBackListener implements View.OnClickListener {
	private LoginUI ui;

	LoginBackListener(LoginUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {
		this.ui.onBodyTouch();
		this.ui.doStartActivity(SelectLoginRegUI.class);
		this.ui.finish();
	}
}
