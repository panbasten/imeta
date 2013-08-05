package com.plywet.mm.ui.login;

public class CountryCodeObject {

	private String countryName;
	private String countryCode;
	private int capital;
	private String pinyin;

	public CountryCodeObject(String countryName, String countryCode,
			int capital, String pinyin) {
		this.countryName = countryName;
		this.countryCode = countryCode;
		this.capital = capital;
		this.pinyin = pinyin;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public int getCapital() {
		return capital;
	}

	public String getPinyin() {
		return pinyin;
	}

}
