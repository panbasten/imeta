package com.plywet.mm.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Utils;
import com.plywet.mm.ui.MMActivity;

/**
 * 忘记密码通过邮箱找回密码
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 上午11:42:55
 */
public class ForgetPwdMobileUI extends MMActivity {

	private String countryName = null;

	private String countryCode = null;

	private TextView countryNameTextView;

	private TextView countryCodeTextView;

	private EditText mobileEditText;

	private LinearLayout countryCodeLinearLayout;

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	@Override
	protected int getCententLayoutId() {
		return R.layout.forgetpwd_mobile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) {
			this.countryName = Utils.defaultString(
					data.getStringExtra("country_name"), "");
			this.countryCode = Utils.defaultString(
					data.getStringExtra("couttry_code"), "");

			if (!"".equals(this.countryName)) {
				this.countryNameTextView.setText(this.countryName);
			}

			if (!"".equals(this.countryCode)) {
				this.countryCodeTextView.setText(this.countryCode);
			}
		}
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.countryName = Utils.defaultString(
				getIntent().getStringExtra("country_name"), "");
		this.countryCode = Utils.defaultString(
				getIntent().getStringExtra("couttry_code"), "");

		setTitle(R.string.reg_forgetpwd_title);

		this.mobileEditText = (EditText) findViewById(R.id.forgetpwd_mobile_et);
		this.countryCodeLinearLayout = (LinearLayout) findViewById(R.id.country_code_ll);
		this.countryCodeLinearLayout
				.setOnClickListener(new CountryCodeLinearLayoutClick(this));
		this.countryNameTextView = (TextView) findViewById(R.id.country_name);
		this.countryCodeTextView = (TextView) findViewById(R.id.country_code);

		if (!Utils.isEmpty(this.countryName)) {
			this.countryNameTextView.setText(this.countryName);
		}

		if (!Utils.isEmpty(this.countryCode)) {
			this.countryCodeTextView.setText("+" + this.countryCode);
		}

		setRightBtn(R.string.app_nextstep, new ForgetPwdMobileNextStepClick(
				this));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(key, event);
	}

}

/**
 * 点击下一步的事件
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 下午12:20:47
 */
class ForgetPwdMobileNextStepClick implements View.OnClickListener {

	private ForgetPwdMobileUI ui;

	ForgetPwdMobileNextStepClick(ForgetPwdMobileUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}

/**
 * 点击国家代码的事件
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 下午12:19:06
 */
class CountryCodeLinearLayoutClick implements View.OnClickListener {

	private ForgetPwdMobileUI ui;

	CountryCodeLinearLayoutClick(ForgetPwdMobileUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this.ui, CountryCodeUI.class);
		intent.putExtra("country_name", this.ui.getCountryName());
		intent.putExtra("couttry_code", this.ui.getCountryCode());
		intent.putExtra("user_mobile_type", 0);
		this.ui.startActivityForResult(intent, 0);
	}

}
