package com.panet.imeta.core;

import net.sf.json.JSONObject;

import com.panet.imeta.ui.exception.ImetaException;

public interface BaseDialogInterface {
	public JSONObject open() throws ImetaException;
}
