package com.plywet.mm.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.plywet.mm.R;
import com.plywet.mm.ui.MMActivity;
import com.plywet.mm.ui.MMDialogUtils;

public class SelectLoginRegUI extends MMActivity implements
		View.OnClickListener {

	private MMActionInterface action = new SelectLoginRegAction(this);

	@Override
	protected int getCententLayoutId() {
		return R.layout.select_login_reg;
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		Button loginBtn = (Button) findViewById(R.id.select_login_btn);
		Button registerBtn = (Button) findViewById(R.id.select_register_btn);

		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.select_login_btn:
			startActivity(new Intent(this, LoginUI.class));
			finish();
			break;
		case R.id.select_register_btn:
			String[] menus = new String[] {
					getResources().getString(R.string.select_login_by_qq_num),
					getResources().getString(R.string.reg_type_mobile_text),
					getResources().getString(R.string.reg_type_email_text) };
			MMDialogUtils.showDialog(this, null, menus, null, action);
		}
	}
}
