package com.plywet.mm.ui;

import android.widget.LinearLayout;
import android.widget.TextView;

public class MMDialogItemObject {

	LinearLayout ll;
	
	TextView text;
	
	int index;
	
	MMDialogAdapter adapter;
	
	MMDialogItemObject(MMDialogAdapter adapter){
		this.adapter = adapter;
	}
}
