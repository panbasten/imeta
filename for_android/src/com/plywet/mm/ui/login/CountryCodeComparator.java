package com.plywet.mm.ui.login;

import java.util.Comparator;

/**
 * 国家代码的比较器
 * 
 * @author Peter Pan（潘巍）
 * @email panbasten@gmail.com
 * 
 * @since 2011-12-17 下午01:07:31
 */
public class CountryCodeComparator implements Comparator {

	@Override
	public int compare(Object object1, Object object2) {
		CountryCodeObject code1 = (CountryCodeObject) object1;
		CountryCodeObject code2 = (CountryCodeObject) object2;
		return code1.getCapital() - code2.getCapital();
	}

}
