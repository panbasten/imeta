package com.plywet.mm.ui.login;

import android.content.Intent;

public class SelectLoginRegAction implements MMActionInterface {

	private SelectLoginRegUI ui;

	SelectLoginRegAction(SelectLoginRegUI ui) {
		this.ui = ui;
	}

	@Override
	public void action(int key) {
		switch (key) {
		case 0:
			this.ui.startActivity(new Intent(this.ui, LoginUI.class));
			break;
		case 1:
			this.ui.startActivity(new Intent(this.ui, RegByMobileRegUI.class));
			break;
		case 2:
			this.ui.startActivity(new Intent(this.ui, RegByMailRegUI.class));
			break;
		}
		this.ui.finish();
	}
}
