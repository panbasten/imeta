package com.plywet.mm.ui.login;

import com.plywet.mm.R;
import com.plywet.mm.platformtools.Log;
import com.plywet.mm.ui.MMAlphabetActionInterface;

public class CountryCodeAlphabetAction implements MMAlphabetActionInterface {

	private CountryCodeUI ui;

	CountryCodeAlphabetAction(CountryCodeUI ui) {
		this.ui = ui;
	}

	@Override
	public void action(String key) {
		// 如果是“搜”
		if (this.ui.getString(R.string.scroll_bar_search).equals(key)) {
			this.ui.getAddressContactList().setSelection(0);
		}
		// 如果是字母
		else {
			int alphabet = key.charAt(0);

			int[] codeIndexes = this.ui.getAdapter().getCodeIndexes();
			if (codeIndexes != null) {
				for (int i = 0; i < codeIndexes.length; i++) {
					Log.debug("checked", "showhead:" + codeIndexes[i]);
					if (codeIndexes[i] == alphabet) {
						Log.debug("checked", "showhead index:" + i + "  pos:"
								+ i);
						this.ui.getAddressContactList().setSelection(i);
						break;
					}
				}
			}
		}
	}

}
