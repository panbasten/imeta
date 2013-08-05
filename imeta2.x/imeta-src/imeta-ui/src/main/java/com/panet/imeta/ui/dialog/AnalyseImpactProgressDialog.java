package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.trans.BaseTransDialog;
import com.panet.imeta.trans.DatabaseImpact;
import com.panet.imeta.trans.TransDialogInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AnalyseImpactProgressDialog extends BaseTransDialog implements
		TransDialogInterface {
	public AnalyseImpactProgressDialog(TransMeta transMeta) {
		super(transMeta);
	}

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private String rootId;

	/**
	 * 警告和错误
	 */
	private LabelGridMeta ai;

	/**
	 * 关闭
	 */
	private ButtonMeta close;

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			String id = "ai_" + super.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.columnFormMeta.setSingle(true);

			List<DatabaseImpact> analyse = new ArrayList<DatabaseImpact>();
			analyseImpact(analyse);
			this.ai = new LabelGridMeta(id + "_ai", "警告和错误", 300);
			this.ai.setSingle(true);
			this.ai.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(null, "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, true, 50),
					new GridHeaderDataMeta(id + "_ai.typeDesc",
							"类型", null, true, 50),
					new GridHeaderDataMeta(id
							+ "_ai.transformationName", "转换", null,
							true, 80),
					new GridHeaderDataMeta(id + "_ai.stepName",
							"步骤", null, true, 80),
					new GridHeaderDataMeta(id + "_ai.databaseName",
							"数据库", null, true, 80),
					new GridHeaderDataMeta(id + "_ai.table", "表",
							null, true, 80),
					new GridHeaderDataMeta(id + "_ai.field", "字段",
							null, true, 80),
					new GridHeaderDataMeta(id + "_ai.value", "值",
							null, true, 80),
					new GridHeaderDataMeta(id + "_ai.valueOrigin",
							"原始值", null, true, 80),
					new GridHeaderDataMeta(id + "_ai.sql", "SQL语句",
							null, true, 200),
					new GridHeaderDataMeta(id + "_ai.remark", "注释",
							null, true, 300) });
			int i = 1;
			for (DatabaseImpact a : analyse) {
				this.ai
						.addRow(new Object[] {
								String.valueOf(i++),
								new GridCellDataMeta(null, Const.NVL(a
										.getTypeDesc(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getTransformationName(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getStepName(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getDatabaseName(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getTable(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getField(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getValue(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getValueOrigin(), "")),
								new GridCellDataMeta(null, Const.NVL(
										a.getSQL(), "")),
								new GridCellDataMeta(null, Const.NVL(a
										.getRemark(), "")), });
			}

			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { this.ai });

			this.close = new ButtonMeta(id + ".btn.closeWin", id
					+ ".btn.closeWin", "关闭", "关闭");
			this.close.putProperty("rootId", rootId);
			this.close.addClick("jQuery.imeta.trans.analyseImpact.btn.closeWin");

			this.columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] { this.close });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	private void analyseImpact(List<DatabaseImpact> remarks)
			throws KettleStepException {
		super.getTransMeta().analyseImpact(remarks, null);
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

}
