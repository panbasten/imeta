package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridRowDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.trans.BaseTransDialog;
import com.panet.imeta.trans.TransDialogInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class CheckTransProgressDialog extends BaseTransDialog implements
		TransDialogInterface {
	public CheckTransProgressDialog(TransMeta transMeta) {
		super(transMeta);
	}

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private String rootId;

	/**
	 * 警告和错误
	 */
	private LabelGridMeta we;

	private LabelInputMeta showSuccess;

	/**
	 * 关闭
	 */
	private ButtonMeta close;

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			String id = "check_" + super.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.columnFormMeta.setSingle(true);

			List<CheckResultInterface> remarks = new ArrayList<CheckResultInterface>();
			check(remarks);
			this.we = new LabelGridMeta(id + "_warnsAndErrors", "警告和错误", 300);
			this.we.setSingle(true);
			this.we.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(null, "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, true, 50),
					new GridHeaderDataMeta(id + "_warnsAndErrors.stepName",
							"步骤名", null, true, 100),
					new GridHeaderDataMeta(id + "_warnsAndErrors.result", "结果",
							null, true, 100),
					new GridHeaderDataMeta(id + "_warnsAndErrors.remark", "注释",
							null, true, 300) });
			int i = 1;
			StepMeta stepMeta;
			for (CheckResultInterface remark : remarks) {
				if (remark.getSourceInfo() != null) {
					stepMeta = (StepMeta) remark.getSourceInfo();
				} else {
					stepMeta = null;
				}
				if (remark.getType() == CheckResultInterface.TYPE_RESULT_OK) {
					GridRowDataMeta row = new GridRowDataMeta(4);
					row.setCell(0, "");
					row.setCell(1, new GridCellDataMeta(null, Const.NVL(
							(stepMeta != null) ? stepMeta.getName() : "<全局>",
							"")));
					row.setCell(2, new GridCellDataMeta(null, Const.NVL(remark
							.getType()
							+ " - " + remark.getTypeDesc(), "")));
					row.setCell(3, new GridCellDataMeta(null, Const.NVL(remark
							.getText(), "")));
					row.putStyle("background-color", "#7CFC00");
					row.setDisplay(false);
					this.we.addRow(row);
				} else if (remark.getType() == CheckResultInterface.TYPE_RESULT_ERROR) {
					GridRowDataMeta row = new GridRowDataMeta(4);
					row.setCell(0, String.valueOf(i++));
					row.setCell(1, new GridCellDataMeta(null, Const.NVL(
							(stepMeta != null) ? stepMeta.getName() : "<全局>",
							"")));
					row.setCell(2, new GridCellDataMeta(null, Const.NVL(remark
							.getType()
							+ " - " + remark.getTypeDesc(), "")));
					row.setCell(3, new GridCellDataMeta(null, Const.NVL(remark
							.getText(), "")));
					row.putStyle("background-color", "red");
					this.we.addRow(row);
				} else {
					this.we.addRow(new Object[] {
							String.valueOf(i++),
							new GridCellDataMeta(null, Const.NVL(
									(stepMeta != null) ? stepMeta.getName()
											: "<全局>", "")),
							new GridCellDataMeta(null, Const.NVL(remark
									.getType()
									+ " - " + remark.getTypeDesc(), "")),
							new GridCellDataMeta(null, Const.NVL(remark
									.getText(), "")), });
				}
			}

			this.showSuccess = new LabelInputMeta(id + ".showSuccess",
					"显示成功的结果", null, null, null, null,
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.showSuccess.setLabelAfter(true);
			this.showSuccess
					.addClick("jQuery.imeta.trans.check.listener.showSuccess");

			this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.we, this.showSuccess });

			this.close = new ButtonMeta(id + ".btn.closeWin", id
					+ ".btn.closeWin", "关闭", "关闭");
			this.close.putProperty("rootId", rootId);
			this.close.addClick("jQuery.imeta.trans.check.btn.closeWin");

			this.columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] { this.close });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	private void check(List<CheckResultInterface> remarks) {
		super.getTransMeta().checkSteps(remarks, false, null);
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

}
