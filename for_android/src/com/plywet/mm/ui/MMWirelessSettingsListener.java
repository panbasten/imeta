package com.plywet.mm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MMWirelessSettingsListener implements
		DialogInterface.OnClickListener {
	private Context context;

	MMWirelessSettingsListener(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		this.context.startActivity(new Intent(
				"android.settings.WIRELESS_SETTINGS"));
	}
}
