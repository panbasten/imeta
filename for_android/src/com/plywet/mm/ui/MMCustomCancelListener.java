package com.plywet.mm.ui;

import android.content.DialogInterface;

/**
 * 自定义的取消事件
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-15 上午08:34:46
 */
public class MMCustomCancelListener implements DialogInterface.OnCancelListener {

	private DialogInterface.OnClickListener listener;

	MMCustomCancelListener(DialogInterface.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (this.listener != null)
			this.listener.onClick(dialog, 0);
	}

}
