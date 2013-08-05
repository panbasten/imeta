package com.plywet.mm.ui.login;

import java.util.ArrayList;
import java.util.List;

import com.plywet.mm.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 国家内容列表的适配器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 下午01:17:01
 */
public class CountryContactListAdapter extends BaseAdapter {

	private List backup = new ArrayList();

	private int[] codeIndexes;

	private List countryCodeList;

	private Context context;

	public CountryContactListAdapter(Context context, List countryCodeList) {
		this.context = context;
		this.countryCodeList = countryCodeList;

		copyList();

		buildIndex();
	}

	private void copyList() {
		for (Object obj : this.countryCodeList) {
			this.backup.add(obj);
		}
	}

	private void buildIndex() {
		this.codeIndexes = new int[this.countryCodeList.size()];
		for (int i = 0; i < this.countryCodeList.size(); i++) {
			this.codeIndexes[i] = ((CountryCodeObject) this.countryCodeList
					.get(i)).getCapital();
		}
	}

	public int[] getCodeIndexes() {
		return codeIndexes;
	}

	@Override
	public int getCount() {
		return this.countryCodeList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.countryCodeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < 0) {
			return null;
		}

		CountryCodeObject codeObj = (CountryCodeObject) getItem(position);

		CountryCodeItemObject item;

		if (convertView == null) {
			convertView = View.inflate(this.context,
					R.layout.country_code_item, null);
			item = new CountryCodeItemObject(this);
			item.contactItemCatalog = (TextView) convertView
					.findViewById(R.id.contactitem_catalog);
			item.contactItemNick = (TextView) convertView
					.findViewById(R.id.contactitem_nick);
			item.contactItemSignature = (TextView) convertView
					.findViewById(R.id.contactitem_signature);

			convertView.setTag(item);

		} else {
			item = (CountryCodeItemObject) convertView.getTag();
		}

		if (position == 0) {
			item.contactItemCatalog.setVisibility(View.VISIBLE);
			item.contactItemCatalog.setText(String
					.valueOf((char) this.codeIndexes[position]));
		} else {
			if (this.codeIndexes[position - 1] != this.codeIndexes[position]) {
				item.contactItemCatalog.setVisibility(View.VISIBLE);
				item.contactItemCatalog.setText(String
						.valueOf((char) this.codeIndexes[position]));
			} else {
				item.contactItemCatalog.setVisibility(View.GONE);
			}
		}

		item.contactItemNick.setText(codeObj.getCountryName());
		item.contactItemSignature.setText(codeObj.getCountryCode());

		return convertView;
	}
}
