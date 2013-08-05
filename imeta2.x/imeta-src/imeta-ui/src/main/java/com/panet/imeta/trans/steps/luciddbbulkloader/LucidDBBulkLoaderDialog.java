package com.panet.imeta.trans.steps.luciddbbulkloader;

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
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.sortedmerge.Messages;
import com.panet.imeta.ui.exception.ImetaException;

public class LucidDBBulkLoaderDialog extends BaseStepDialog implements
		StepDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * Step name 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 数据库连接
	 */
	private LabelSelectMeta dbConnect;

	/**
	 * Target schema
	 */
	private LabelInputMeta targetSchema;

	/**
	 * Target table
	 */
	private LabelInputMeta targetTable;

	/**
	 * Maximum errors
	 */
	private LabelInputMeta maximumErrors;

	/**
	 * FIFO file path(folder)
	 */
	private LabelInputMeta fifoFilePath;

	/**
	 * FIFO foreign server name
	 */
	private LabelInputMeta fifoForeign;

	/**
	 * Fields to load
	 */
	private LabelGridMeta fieldsToLoad;

	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public LucidDBBulkLoaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			LucidDBBulkLoaderMeta step = (LucidDBBulkLoaderMeta) super
					.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			this.dbConnect = new LabelSelectMeta(id + ".dbConnect", "数据库连接",
					null, null, null, String
							.valueOf(step.getDatabaseMeta() == null ? 0 : step
									.getDatabaseMeta().getID()), null, super
							.getConnectionLine());
			this.dbConnect.setSingle(true);

			// 目标模式
			this.targetSchema = new LabelInputMeta(id + ".targetSchame",
					"目标模式", null, null, "目标模式必须填写", step.getSchemaName(), null,
					ValidateForm.getInstance().setRequired(false));
			this.targetSchema.setSingle(true);

			// 目标表
			this.targetTable = new LabelInputMeta(id + ".targetTable", "目标表",
					null, null, "目标表必须填写", step.getTableName(), null,
					ValidateForm.getInstance().setRequired(false));
			this.targetTable.setSingle(true);

			// Maximum errors 最大误差
			this.maximumErrors = new LabelInputMeta(id + ".maximumErrors",
					"最大误差", null, null, "最大误差必须填写", String.valueOf(step
							.getMaxErrors()), null, ValidateForm.getInstance()
							.setRequired(false));
			this.maximumErrors.setSingle(true);

			// FIFO file path(folder)
			this.fifoFilePath = new LabelInputMeta(id + ".fifoFilePath",
					"FIFO的文件路径（文件夹）", null, null, "FIFO的文件路径必须填写", step
							.getFifoDirectory(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fifoFilePath.setSingle(true);

			// FIFO foreign server name FIFO外部服务器名称
			this.fifoForeign = new LabelInputMeta(id + ".fifoForeign",
					"FIFO外部服务器名称", null, null, "FIFO外部服务器名称必须填写", step
							.getFifoServerName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fifoForeign.setSingle(true);

			// Fields to load 加载字段
			this.fieldsToLoad = new LabelGridMeta(id + "_fieldsToLoad", "加载字段",
					200, 0);
			GridHeaderDataMeta fieldFormatOkDataMeta = new GridHeaderDataMeta(
					id + "_fieldsToLoad.fieldFormatOk", "字段是否已格式化", null,
					false, 100);
			fieldFormatOkDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							Messages.getString("System.Combo.Yes"),
							Messages.getString("System.Combo.No") }, false));
			this.fieldsToLoad.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldTable",
							"表字段", null, false, 100),
					new GridHeaderDataMeta(id + "_fieldsToLoad.fieldStream",
							"流字段", null, false, 100), fieldFormatOkDataMeta });
			this.fieldsToLoad.setSingle(true);
			this.fieldsToLoad.setHasBottomBar(true);
			this.fieldsToLoad.setHasAdd(true, true,
					"jQuery.imeta.steps.lucidDBBulkLoader.btn.fieldAdd");
			this.fieldsToLoad.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			String[] fieldNames = step.getFieldTable();
			if (fieldNames != null && fieldNames.length > 0)
				for (int i = 0; i < fieldNames.length; i++) {
					this.fieldsToLoad.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, fieldNames[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getFieldStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step
									.getFieldFormatOk()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.dbConnect, this.targetSchema, this.targetTable,
					this.maximumErrors, this.fifoFilePath, this.fifoForeign,
					this.fieldsToLoad });

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super
									.getGetfields("jQuery.imeta.steps.lucidDBBulkLoader.btn.getfields"),
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
