package com.panet.imeta.trans.steps.delete;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.LabelDataMeta;
import com.panet.iform.core.base.LabelMeta;
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
import com.panet.imeta.ui.exception.ImetaException;

public class DeleteDialog extends BaseStepDialog implements StepDialogInterface {
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
	 * 数据库连接
	 */
	private LabelSelectMeta connection;

	/**
	 * 目的模式
	 */
	private LabelInputMeta schemaName;

	/**
	 * 目标表
	 */
	private LabelInputMeta tableName;

	// private ButtonMeta skim;

	/**
	 * commit size
	 */
	private LabelInputMeta commitSize;

	/**
	 * 用来查询值的关键字：(框代替)
	 */
	private LabelInputMeta keywords;

	/**
	 * 用来查询值的关键字
	 */
	private LabelGridMeta selectedFiles;

	public DeleteDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			DeleteMeta step = (DeleteMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到连接数据库
			this.connection = new LabelSelectMeta(id + ".connection", "连接数据库",
					null, null, "连接数据库", String
							.valueOf((step.getDatabaseMeta() != null) ? step
									.getDatabaseMeta().getID() : ""), null,
					super.getConnectionLine());

			this.connection.setSingle(true);

			// 得到目的模式

			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, null, step.getSchemaName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.schemaName.setSingle(true);

			// 得到目标表

			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "目标表必须填写", step.getTableName(), null, ValidateForm
							.getInstance().setRequired(false));

			this.tableName.setSingle(true);

			// 得到Commit size

			this.commitSize = new LabelInputMeta(id + ".commitSize", "提交的大小",
					null, null, null, String.valueOf(step.getCommitSize()),
					null, ValidateForm.getInstance().setRequired(false));

			this.commitSize.setSingle(true);

			// 选中的文件列表
			this.selectedFiles = new LabelGridMeta(id + "_fields",
					"查询值所需要的关键字", 150);

			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.keyLookup", "表字段",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.keyCondition", "比较符",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.keyStream", "流里的字段1",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.keyStream2", "流里的字段2",
							null, false, 100) });

			String[] fileName = step.getKeyLookup();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getKeyLookup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									step.getKeyCondition()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getKeyStream()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getKeyStream2()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, true,
					"jQuery.imeta.steps.deleteDialog.btn.deleteAdd");
			this.selectedFiles.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.deleteDialog.btn.getfields")
					.appendTo(div);

			LabelMeta notes = new LabelMeta(new LabelDataMeta(id + ".notes", id + ".notes",
					"如果要在“流里的字段1”和“流里的字段2”中输入常量，请以“#”开头。另外，请注意SQL中的常量的引号问题。",
					null), true);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.connection, this.schemaName, this.tableName,
					this.commitSize, this.keywords, this.selectedFiles, div,
					notes });

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
