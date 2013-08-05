package com.plywet.mm.ui;

import java.util.ArrayList;
import java.util.List;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MMDialogAdapter extends BaseAdapter {

	private List menuList;
	private int[] menuType;
	private boolean head = false;
	private Context context;

	public MMDialogAdapter(Context context, String title, String[] menus,
			String special, String cancel) {
		this.context = context;
		if (menus == null || menus.length == 0) {
			this.menuList = new ArrayList();
		} else {
			this.menuList = Utils.stringArray2List(menus);
		}

		this.menuType = new int[3 + this.menuList.size()];

		if (title != null && !"".equals(title)) {
			this.menuType[0] = 1;
			this.head = true;
			this.menuList.add(0, menus);
		}

		if (special != null && !"".equals(special)) {
			this.menuType[this.menuList.size()] = 2;
			this.menuList.add(special);
		}

		if (cancel != null && !"".equals(cancel)) {
			this.menuType[this.menuList.size()] = 3;
			this.menuList.add(cancel);
		}
	}

	@Override
	public int getCount() {
		return this.menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0L;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String menu = (String) getItem(position);
		int index = this.menuType[position];
		MMDialogItemObject item = null;

		if (convertView == null) {
			// 取消按钮
			if (index == 3) {
				convertView = View.inflate(this.context,
						R.layout.alert_dialog_menu_list_layout_cancel, null);
			} else if (index == 0) {
				convertView = View.inflate(this.context,
						R.layout.alert_dialog_menu_list_layout, null);
			} else if (index == 1) {
				convertView = View.inflate(this.context,
						R.layout.alert_dialog_menu_list_layout_title, null);
			} else if (index == 2) {
				convertView = View.inflate(this.context,
						R.layout.alert_dialog_menu_list_layout_special, null);
			}
		}

		if (convertView.getTag() == null) {
			item = new MMDialogItemObject(this);

			item.ll = (LinearLayout) convertView
					.findViewById(R.id.popup_layout);
			item.text = (TextView) convertView.findViewById(R.id.popup_text);
			item.text.setText(menu);
			item.index = index;

			convertView.setTag(item);

		} else {
			item = (MMDialogItemObject) convertView.getTag();
		}

		return convertView;
	}

	@Override
	public final boolean isEnabled(int index) {
		if (index == 0 && this.head) {
			return false;
		}

		return super.isEnabled(index);
	}

}
