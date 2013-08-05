package com.panet.imeta.ui.dialog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

/**
 * 参数的对话框片段
 * 
 * @author Peter Pan
 * 
 */
public class StepFieldDialog implements StepDialogInterface {
	/**
	 * 输入数据
	 */
	private RowMetaInterface input;
	private Repository rep;

	public StepFieldDialog(RowMetaInterface input) {
		this.input = input;
	}

	@Override
	public JSONObject open() throws ImetaException {
		JSONObject rtn = new JSONObject();
		JSONArray fieldsJa = new JSONArray();
		if (input != null && input.size() > 0) {
			for (int i = 0; i < input.size(); i++) {
				ValueMetaInterface v = input.getValueMeta(i);
				JSONObject fieldJo = new JSONObject();
				fieldJo.put("fieldName", v.getName());
				fieldJo.put("type", v.getType());
				fieldJo.put("typeDesc", v.getTypeDesc());
				fieldJo.put("length", v.getLength());
				fieldJo.put("precision", v.getPrecision());
				fieldJo.put("origin", Const.NVL(v.getOrigin(), ""));
				fieldJo.put("storageType", v.getStorageType());
				fieldJo.put("storageTypeCode", ValueMeta.getStorageTypeCode(v
						.getStorageType()));
				fieldJo.put("conversionMask", Const.NVL(v.getConversionMask(),
						""));
				fieldJo.put("decimal", Const.NVL(v.getDecimalSymbol(), ""));
				fieldJo.put("group", Const.NVL(v.getGroupingSymbol(), ""));
				fieldJo.put("trimType", v.getTrimType());
				fieldJo.put("trimTypeDesc", ValueMeta.getTrimTypeDesc(v
						.getTrimType()));
				fieldJo.put("comments", Const.NVL(v.getComments(), ""));
				fieldsJa.add(fieldJo);

			}
		}
		rtn.put("fields", fieldsJa);
		return rtn;
	}

	@Override
	public void setRepository(Repository rep) {
		this.rep = rep;
	}

	public Repository getRep() {
		return rep;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {

	}

}
