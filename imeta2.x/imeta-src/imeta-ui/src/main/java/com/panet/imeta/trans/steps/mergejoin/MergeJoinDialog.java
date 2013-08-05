package com.panet.imeta.trans.steps.mergejoin;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class MergeJoinDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 第一个步骤
	 */
	private LabelSelectMeta stepName1;

	/**
	 * 第二个步骤
	 */
	private LabelSelectMeta stepName2;

	/**
	 * 连接类型
	 */
	private LabelSelectMeta joinType;

	/**
	 * 第一个步骤的连接类型
	 */
	private LabelGridMeta stepAGrid;
	private ButtonMeta getConnectWordsA;

	/**
	 * 第二个步骤的连接类型
	 */
	private LabelGridMeta stepBGrid;
	private ButtonMeta getConnectWordsB;

	public MergeJoinDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			MergeJoinMeta step = (MergeJoinMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 第一个步骤
			this.stepName1 = new LabelSelectMeta(id + ".stepName1", "第一个步骤",
					null, null, null, step.getStepName1(), null, super
							.getPrevStepNames());

			this.stepName1.setSingle(true);

			// 第二个步骤
			this.stepName2 = new LabelSelectMeta(id + ".stepName2", "第二个步骤",
					null, null, null, String.valueOf(step.getStepName2()),
					null, super.getPrevStepNames());

			this.stepName2.setSingle(true);

			// 连接类型
			List<OptionDataMeta> optionsConnect = new ArrayList<OptionDataMeta>();
			for (String s : step.join_types) {
				optionsConnect.add(new OptionDataMeta(s, s));
			}
			this.joinType = new LabelSelectMeta(id + ".joinType", "连接类型", null,
					null, null, step.getJoinType(), null, optionsConnect);
			this.joinType.setSingle(true);

			// 第一个步骤的连接字段
			this.stepAGrid = new LabelGridMeta(id + "_stepAGrid", "第一个步骤的连接字段",
					200, 0);
			this.stepAGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_stepAGrid.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_stepAGrid.value", "连接字段",
							null, false, 100)

			});
			this.stepAGrid.setHasBottomBar(true);
			this.stepAGrid.setHasAdd(true, true,
					"jQuery.imeta.steps.mergejoin.btn.stepAGridAdd");
			this.stepAGrid.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.stepAGrid.setSingle(true);
			String[] values1 = step.getKeyFields1();
			if (values1 != null && values1.length > 0)
				for (int i = 0; i < values1.length; i++) {
					this.stepAGrid.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values1[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}

			// 第二个步骤的连接字段
			this.stepBGrid = new LabelGridMeta(id + "_stepBGrid", "第二个步骤的连接字段",
					200, 0);
			this.stepBGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_stepBGrid.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_stepBGrid.value", "连接字段",
							null, false, 100)

			});
			this.stepBGrid.setHasBottomBar(true);
			this.stepBGrid.setHasAdd(true, true,
					"jQuery.imeta.steps.mergejoin.btn.stepBGridAdd");
			this.stepBGrid.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.stepBGrid.setSingle(true);
			String[] values2 = step.getKeyFields2();
			if (values2 != null && values2.length > 0)
				for (int i = 0; i < values2.length; i++) {
					this.stepBGrid.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, values2[i],
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}

			DivMeta diva = new DivMeta(new NullSimpleFormDataMeta(), true);
			super
					.getGetfields(
							"jQuery.imeta.steps.mergejoin.btn.getStep1Field")
					.appendTo(diva);
			// this.getConnectWordsA = new ButtonMeta(
			// id + ".btn.getConnectWordsA", id + ".btn.getConnectWordsA",
			// "获取连接字段", "获取连接字段");
			// this.getConnectWordsA
			// .addClick("jQuery.imeta.steps.mergejoin.btn.getStep1Field");
			// this.getConnectWordsA.putProperty("roType",
			// RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			// this.getConnectWordsA.putProperty("roName", super.getTransMeta()
			// .getName());
			// this.getConnectWordsA.putProperty("elementName", super
			// .getStepName());
			// this.getConnectWordsA.appendTo(diva);
			DivMeta divb = new DivMeta(new NullSimpleFormDataMeta(), true);
			this.getConnectWordsB = new ButtonMeta(id + ".btn.getfields1", id
					+ ".btn.getfields1", "获取字段", "获取字段");
			this.getConnectWordsB
					.addClick("jQuery.imeta.steps.mergejoin.btn.getStep2Field");
			this.getConnectWordsB.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getConnectWordsB.putProperty("roName", super.getTransMeta()
					.getName());
			this.getConnectWordsB.putProperty("elementName", super
					.getStepName());
			this.getConnectWordsB.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			// this.getConnectWordsB = new ButtonMeta(
			// id + ".btn.getConnectWordsB", id + ".btn.getConnectWordsB",
			// "获取连接字段", "获取连接字段");
			// this.getConnectWordsB
			// .addClick("jQuery.imeta.steps.mergejoin.btn.getStep2Field");
			// this.getConnectWordsB.putProperty("roType",
			// RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			// this.getConnectWordsB.putProperty("roName", super.getTransMeta()
			// .getName());
			// this.getConnectWordsB.putProperty("elementName", super
			// .getStepName());
			this.getConnectWordsB.appendTo(divb);
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.stepName1, this.stepName2, this.joinType,
					this.stepAGrid, diva, this.stepBGrid, divb });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
