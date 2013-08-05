package com.panet.iform.forms.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GridCellComplexDataMeta implements Cloneable,
		GridCellDataMetaInterface {

	public static final String CELL_TYPE_BUTTON_SET = "buttonset";

	/**
	 * 列表类型-默认为头部类型
	 */
	private String type = CELL_TYPE_BUTTON_SET;

	private List<GridCellDataMeta> sub = new ArrayList<GridCellDataMeta>();

	/**
	 * extendsMap
	 */
	private Map<String, String> extendsMap;

	/**
	 * 是否缺省值
	 */
	private boolean disabled = false;

	@Override
	public JSONObject getFormJo() {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();

		if (sub.size() > 0) {
			for (GridCellDataMeta d : sub) {
				ja.add(d.getFormJo());
			}
		}

		jo.put("sub", ja);

		jo.put("type", this.type);
		jo.put("disabled", this.disabled);

		if (this.extendsMap != null && this.extendsMap.size() > 0) {
			JSONObject ex = new JSONObject();
			String key;
			for (Iterator<String> it = this.extendsMap.keySet().iterator(); it
					.hasNext();) {
				key = it.next();
				ex.put(key, this.extendsMap.get(key));
			}
			jo.put("extendsMap", ex);
		}

		return jo;
	}
}
