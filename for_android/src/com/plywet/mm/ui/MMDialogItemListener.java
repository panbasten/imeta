package com.plywet.mm.ui;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.plywet.mm.ui.login.MMActionInterface;

public class MMDialogItemListener implements AdapterView.OnItemClickListener {

	private String title;

	private MMActionInterface action;

	private Dialog dialog;

	private ListView listView;

	MMDialogItemListener(String title, MMActionInterface action, Dialog dialog,
			ListView listView) {
		this.title = title;
		this.action = action;
		this.dialog = dialog;
		this.listView = listView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (this.title != null && !"".equals(this.title) && position - 1 >= 0) {
			this.action.action(position - 1);
			this.dialog.dismiss();
			this.listView.requestFocus();
		} else {
			this.action.action(position);
			this.dialog.dismiss();
			this.listView.requestFocus();
		}
	}

}
