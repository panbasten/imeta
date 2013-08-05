package com.panet.imeta.core.json;

import net.sf.json.JSONObject;

import com.panet.imeta.core.exception.KettleException;

public interface JSONInterface {

	public JSONObject getJSON() throws KettleException;
}
