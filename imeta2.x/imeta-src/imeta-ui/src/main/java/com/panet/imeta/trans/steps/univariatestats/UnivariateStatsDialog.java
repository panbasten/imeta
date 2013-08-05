package com.panet.imeta.trans.steps.univariatestats;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.sortedmerge.Messages;
import com.panet.imeta.ui.exception.ImetaException;

public class UnivariateStatsDialog extends BaseStepDialog implements
		StepDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * Step name
	 */
	private LabelInputMeta name;

	/**
	 * Input fields and derived stats:
	 */
	private LabelGridMeta inputFields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public UnivariateStatsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			UnivariateStatsMeta step = (UnivariateStatsMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// Input fields and derived stats
			this.inputFields = new LabelGridMeta(id + "_inputFields",
					"输入字段和衍生统计", 200, 0);
			GridHeaderDataMeta nDataMeta = new GridHeaderDataMeta(id
					+ "_inputFields.n", "N", null, false, 100);
			nDataMeta.setOptions(super.getOptionsByStringArrayWithBooleanValue(
					new String[] { "True", "False" }, false));
			GridHeaderDataMeta meanDataMeta = new GridHeaderDataMeta(id
					+ "_inputFields.mean", "意义", null, false, 100);
			meanDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			GridHeaderDataMeta stdDevMeta = new GridHeaderDataMeta(id
					+ "_inputFields.stdDev", "Std Dev", null, false, 100);
			stdDevMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			GridHeaderDataMeta minDataMeta = new GridHeaderDataMeta(id
					+ "_inputFields.min", "最小", null, false, 100);
			minDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			GridHeaderDataMeta maxDataMeta = new GridHeaderDataMeta(id
					+ "_inputFields.max", "最大", null, false, 100);
			maxDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			GridHeaderDataMeta medianDataMeta = new GridHeaderDataMeta(id
					+ "_inputFields.median", "中间值", null, false, 100);
			medianDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			GridHeaderDataMeta interpolatePercentileDataMeta = new GridHeaderDataMeta(
					id + "_inputFields.interpolatePercentile", "插入百分位", null,
					false, 100);
			interpolatePercentileDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							"True", "False" }, false));
			this.inputFields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_inputFields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_inputFields.fieldName",
							"输入字段", null, false, 100),
					nDataMeta,
					meanDataMeta,
					stdDevMeta,
					minDataMeta,
					maxDataMeta,
					medianDataMeta,
					new GridHeaderDataMeta(id
							+ "_inputFields.arbitraryPercentile", "百分位", null,
							false, 100), interpolatePercentileDataMeta,

			});
			this.inputFields.setHasBottomBar(true);
			this.inputFields.setHasAdd(true, true,
					"jQuery.imeta.steps.group.btn.statsAdd");
			this.inputFields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.inputFields.setSingle(true);
			UnivariateStatsMetaFunction[] statsMetas = step
					.getInputFieldMetaFunctions();

			if (statsMetas != null && statsMetas.length > 0)
				for (int i = 0; i < statsMetas.length; i++) {
					this.inputFields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, statsMetas[i]
									.getSourceFieldName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcN()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcMean()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcStdDev()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcMin()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcMax()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i].getCalcMedian()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,
									String.valueOf(statsMetas[i]
											.getCalcPercentile()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(statsMetas[i]
											.getInterpolatePercentile()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.inputFields });
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
