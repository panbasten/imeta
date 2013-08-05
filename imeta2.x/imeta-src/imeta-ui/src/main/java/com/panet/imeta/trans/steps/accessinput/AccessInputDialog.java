package com.panet.imeta.trans.steps.accessinput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class AccessInputDialog extends BaseStepDialog implements
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
	 * 页签
	 */
	private MenuTabMeta meta;

	// 文件 ------------------------------------------------------------------
	/**
	 * 从字段获得文件名
	 */
	private ColumnFieldsetMeta toFilename;
	/**
	 * 文件定义在一个字段里
	 */
	private LabelInputMeta filefield;
	/**
	 * 从字段获取文件名
	 */
	private LabelSelectMeta dynamicFilenameField;

	/**
	 * 文件或路径
	 */
	// private LabelInputMeta fileOrpath;
	/**
	 * 正则表达式
	 */
	// private LabelInputMeta regularExpression;
	/**
	 * 选中的文件列表
	 */
	private LabelGridMeta selectedFiles;

	// 内容 ------------------------------------------------------------------
	/**
	 * 表
	 */
	private LabelInputMeta TableName;
	// private ButtonMeta tableSelecter;

	/**
	 * 附加字段
	 */
	private ColumnFieldsetMeta additionalField;
	/**
	 * 在输入中包含文件名
	 */
	private LabelInputMeta includeFilename;
	/**
	 * 包含文件名的字段名
	 */
	private LabelInputMeta filenameField;
	/**
	 * 在输入中包含表名
	 */
	private LabelInputMeta includeTablename;
	/**
	 * 包含表名的字段名
	 */
	private LabelInputMeta tablenameField;
	/**
	 * 在输入中包含行数
	 */
	private LabelInputMeta includeRowNumber;
	/**
	 * 包含行数的字段名
	 */
	private LabelInputMeta rowNumberField;
	/**
	 * 每个文件的结果集行数
	 */
	private LabelInputMeta resetRowNumber;

	/**
	 * 限制
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 增加到结果文件名
	 */
	private ColumnFieldsetMeta addResultsFilename;
	/**
	 * 添加文件名
	 */
	private LabelInputMeta isaddresult;

	// 字段 ------------------------------------------------------------------
	/**
	 * 字段列表
	 */
	private GridMeta fields;

	/**
	 * 获取字段
	 */
//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public AccessInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			AccessInputMeta step = (AccessInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------文件
			 ******************************************************************/
			// 从字段获得文件名
			this.toFilename = new ColumnFieldsetMeta(id, "从字段获得文件名");
			this.toFilename.setSingle(true);
			// 文件定义在一个字段里
			boolean isFileField = step.isFileField();
			this.filefield = new LabelInputMeta(id + ".filefield",
					"文件名定义在一个字段里", null, null, null, String.valueOf(step
							.isFileField()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(true));
			this.filefield
					.addClick("jQuery.imeta.steps.accessinput.listeners.isFileFieldListeners");
			// 从字段获取文件名
			this.dynamicFilenameField = new LabelSelectMeta(id
					+ ".dynamicFilenameField", "从字段获取文件名", null, null, null,
					step.getDynamicFilenameField(), null, super
							.getPrevStepResultFields());
			this.dynamicFilenameField.setSingle(true);
			this.dynamicFilenameField.setHasEmpty(true);
			this.dynamicFilenameField.setDisabled(!step.isFileField());

			// 将标签加入到toFilename
			this.toFilename.putFieldsetsContent(new BaseFormMeta[] {
					this.filefield, this.dynamicFilenameField });

			// 文件或路径
			// this.fileOrpath = new LabelInputMeta(id + ".fileOrpath", "文件或路径",
			// null, null, null, null, null, ValidateForm.getInstance()
			// .setRequired(true));
			// this.fileOrpath.setSingle(true);
			// this.fileOrpath.setDisabled(step.isFileField());
			// 正则表达式
			// this.regularExpression = new LabelInputMeta(id
			// + ".regularExpression", "正则表达式", null, null, null, null,
			// null, ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// this.regularExpression.setDisabled(step.isFileField());
			// 选中的文件列表
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"选中的文件", 200);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件＼目录", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 200) });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles
					.setHasAdd(true, !isFileField,
							"jQuery.imeta.steps.accessinput.selectedfiles.btn.fieldAdd");
			this.selectedFiles
					.setHasDelete(true, !isFileField,
							"jQuery.imeta.parameter.fieldsDelete");
			String[] fileName = step.getFileName();
			GridCellDataMeta name, mask;
			for (int i = 0; i < fileName.length; i++) {

				name = new GridCellDataMeta(null, step.getFileName()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				name.setDisabled(isFileField);
				mask = new GridCellDataMeta(null, step.getFileMask()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				mask.setDisabled(isFileField);
				this.selectedFiles.addRow(new Object[] { String.valueOf(i + 1),
						name, mask });
			}

			// 装载到页签
			// this.meta.putTabContent(0,
			// new BaseFormMeta[] { this.toFilename, this.fileOrpath,
			// this.regularExpression, this.selectedFiles });
			this.meta.putTabContent(0, new BaseFormMeta[] { this.toFilename,
					this.selectedFiles });

			/*******************************************************************
			 * 标签1---------内容
			 ******************************************************************/
			// 表
			this.TableName = new LabelInputMeta(id + ".TableName", "表", null,
					null, null, step.getTableName(), null, ValidateForm
							.getInstance().setRequired(true));
			// this.tableSelecter = new ButtonMeta(id + ".btn.tableSelecter", id
			// + ".btn.tableSelecter", "浏览(B)...", "浏览");
			// this.table.addButton(new ButtonMeta[] { tableSelecter });
			this.TableName.setSingle(true);

			// 附加字段
			this.additionalField = new ColumnFieldsetMeta(id, "附加字段");
			this.additionalField.setSingle(true);
			// 在输入中包含文件名
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"在输入中包含文件名", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilename
					.addClick("jQuery.imeta.steps.accessinput.listeners.includefilenameListeners");
			this.includeFilename.setDisabled(step.isFileField());
			// 包含文件名的字段名
			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"包含文件名的字段名", null, null, null, step.getFilenameField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());
			this.filenameField.setDisabled(!step.isFileField());
			// 在输入中包含表名
			this.includeTablename = new LabelInputMeta(
					id + ".includeTablename", "在输入中包含表名", null, null, null,
					String.valueOf(step.includeTablename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeTablename
					.addClick("jQuery.imeta.steps.accessinput.listeners.includeTablenameListeners");
			// 包含表名的字段名
			this.tablenameField = new LabelInputMeta(id + ".tablenameField",
					"包含表名的字段名 ", null, null, null, step.gettablenameField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.tablenameField.setDisabled(!step.includeTablename());
			// 在输入中包含行数
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "在输入中包含行数", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.accessinput.listeners.includeRowNumberListeners");
			// 包含行数的字段名
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"包含行数的字段名", null, null, null, step.getRowNumberField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());
			// 每个文件的结果集行数
			this.resetRowNumber = new LabelInputMeta(id + ".resetRowNumber",
					"每个文件的结果集行数 ", null, null, null, String.valueOf(step
							.resetRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.resetRowNumber.setDisabled(!step.includeRowNumber());
			// 将标签加入到附加字段
			this.additionalField.putFieldsetsContent(new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeTablename, this.tablenameField,
					this.includeRowNumber, this.rowNumberField,
					this.resetRowNumber });

			// 限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			this.rowLimit.setDisabled(step.isFileField());

			// 增加到结果文件名
			this.addResultsFilename = new ColumnFieldsetMeta(id, "增加到结果文件名");
			this.addResultsFilename.setSingle(true);
			// 添加文件名
			this.isaddresult = new LabelInputMeta(id + ".addFilename", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.addResultsFilename
					.putFieldsetsContent(new BaseFormMeta[] { this.isaddresult });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.TableName,
					this.additionalField, this.rowLimit,
					this.addResultsFilename });

			/*******************************************************************
			 * 标签2---------字段
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 220, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldColumn", "列",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 50)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "组",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldTrimType",
							"去空格类型", null, false, 120)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.trimTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldRepeat", "重复",
							null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.accessinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			AccessInputField[] inputFields = step.getInputFields();
			for (int i = 0; i < inputFields.length; i++) {
				AccessInputField field = inputFields[i];
				this.fields
						.addRow(new Object[] {
								String.valueOf(i + 1),
								new GridCellDataMeta(null, field.getName(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getColumn()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getType()),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, String.valueOf(field
										.getFormat()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getLength()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getPrecision()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getCurrencySymbol()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getDecimalSymbol()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getGroupSymbol()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String.valueOf(field
										.getTrimType()),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, String.valueOf(field
										.isRepeated()),
										GridCellDataMeta.CELL_TYPE_SELECT)

						});
			}

			// 获取字段
			DivMeta fieldBtn = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.accessinput.btn.getfields")
			.appendTo(fieldBtn);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields,
					fieldBtn });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

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
