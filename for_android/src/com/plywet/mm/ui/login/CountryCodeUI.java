package com.plywet.mm.ui.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Utils;
import com.plywet.mm.ui.AlphabetScrollBar;
import com.plywet.mm.ui.MMActivity;
import com.plywet.mm.ui.MMAlphabetActionInterface;

public class CountryCodeUI extends MMActivity {

	private String countryName;

	private String countryCode;

	private int userMobileType = 0;

	private List countriesList;

	private ListView addressContactList;

	private CountryContactListAdapter adapter;

	private AlphabetScrollBar alphabet;

	private String textChangedFlag = "";

	private MMAlphabetActionInterface action = new CountryCodeAlphabetAction(
			this);

	public String getTextChangedFlag() {
		return textChangedFlag;
	}

	public void setTextChangedFlag(String textChangedFlag) {
		this.textChangedFlag = textChangedFlag;
	}

	public ListView getAddressContactList() {
		return addressContactList;
	}

	public CountryContactListAdapter getAdapter() {
		return adapter;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public int getUserMobileType() {
		return userMobileType;
	}

	@Override
	protected int getCententLayoutId() {
		return R.layout.country_code_select;
	}

	public void initCountryCode() {
		String[] countries = getString(R.string.country_code).trim().split(",");
		for (int i = 0; i < countries.length; i++) {
			String[] code = countries[i].trim().split(":");
			countriesList.add(new CountryCodeObject(code[1], code[0], code[2]
					.charAt(0), code[2]));
		}
		Collections.sort(countriesList, new CountryCodeComparator());
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		this.countriesList = new ArrayList();

		this.countryName = Utils.defaultString(
				getIntent().getStringExtra("country_name"), "");
		this.countryCode = Utils.defaultString(
				getIntent().getStringExtra("couttry_code"), "");

		this.userMobileType = getIntent().getIntExtra("user_mobile_type", 0);

		setTitle(R.string.address_title_select_country_code);

		initCountryCode();

		this.addressContactList = (ListView) findViewById(R.id.address_contactlist);

		this.adapter = new CountryContactListAdapter(this, this.countriesList);

		View searchBar = View.inflate(this, R.layout.search_bar, null);
		EditText searchBarEditText = (EditText) searchBar
				.findViewById(R.id.search_bar_et);

		this.addressContactList.addHeaderView(searchBar);
		this.addressContactList.setAdapter(this.adapter);
		this.addressContactList.setVisibility(View.VISIBLE);

		this.alphabet = (AlphabetScrollBar) findViewById(R.id.address_scrollbar);
		this.alphabet.setAction(this.action);

		setLeftBtn(R.string.app_cancel, new CountryCodeCancelListener(this));

		searchBarEditText
				.addTextChangedListener(new CountryCodeSearchChangedListener(
						this));
		this.addressContactList
				.setOnItemClickListener(new CountryCodeContactListener(this));

	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {
		if (key == KeyEvent.META_SYM_ON) {
			Intent intent = new Intent();
			intent.putExtra("country_name", this.countryName);
			intent.putExtra("couttry_code", this.countryCode);

			switch (this.userMobileType) {
			default:
			case 0:
				intent.setClass(this, ForgetPwdMobileUI.class);
				break;
			case 1:
				intent.setClass(this, RegByMobileRegUI.class);
				break;
			case 2:
				//intent.setClass(this, BindMContactUI.class);
				break;
			}
			
			setResult(0, intent);
			finish();
			return true;
		}

		return super.onKeyDown(key, event);
	}

}

class CountryCodeCancelListener implements View.OnClickListener {

	private CountryCodeUI ui;

	CountryCodeCancelListener(CountryCodeUI ui) {
		this.ui = ui;
	}

	@Override
	public void onClick(View v) {
		this.ui.onBodyTouch();

		Intent intent = new Intent();
		intent.putExtra("country_name", this.ui.getCountryName());
		intent.putExtra("couttry_code", this.ui.getCountryCode());

		this.ui.setResult(0, intent);
		this.ui.finish();
	}
}

class CountryCodeContactListener implements AdapterView.OnItemClickListener {

	private CountryCodeUI ui;

	CountryCodeContactListener(CountryCodeUI ui) {
		this.ui = ui;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();

		if (position > 0) {
			CountryCodeObject obj = (CountryCodeObject) this.ui.getAdapter()
					.getItem(position - 1);
			intent.putExtra("country_name", obj.getCountryName());
			intent.putExtra("couttry_code", obj.getCountryCode());
			this.ui.setResult(0, intent);
		}
		this.ui.finish();
	}

}

class CountryCodeSearchChangedListener implements TextWatcher {

	private CountryCodeUI ui;

	CountryCodeSearchChangedListener(CountryCodeUI ui) {
		this.ui = ui;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		this.ui.setTextChangedFlag(Utils.filterString(s.toString()));
		this.ui.initCountryCode();
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
