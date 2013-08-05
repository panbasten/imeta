package com.plywet.mm.ui;

import android.content.DialogInterface;

/**
 * 通用取消按钮Listener
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-15 上午08:06:20
 */
public class MMCancelListener implements DialogInterface.OnClickListener {

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	}

}
