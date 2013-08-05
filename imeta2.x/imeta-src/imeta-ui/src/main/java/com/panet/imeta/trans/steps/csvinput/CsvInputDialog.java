package com.panet.imeta.trans.steps.csvinput;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.textfileinput.TextFileInputField;
import com.panet.imeta.ui.exception.ImetaException;

public class CsvInputDialog extends BaseStepDialog implements
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
	 * 文件名
	 */
	private LabelInputMeta filename;

	/**
	 * Delimiter分隔符
	 */
	private LabelInputMeta delimiter;
	// private ButtonMeta delimiterInsertTAB;

	/**
	 * Enclosure
	 */
	private LabelInputMeta enclosure;

	/**
	 * NIO 缓冲量
	 */
	private LabelInputMeta bufferSize;

	/**
	 * Lazy conversion懒惰转换
	 */
	private LabelInputMeta lazyConversionActive;

	/**
	 * Header row present标题行本
	 */
	private LabelInputMeta headerPresent;

	/**
	 * Add filename to result 同时运行多个并行
	 */
	private LabelInputMeta isaddresult;

	/**
	 * The row number field name(optional)该行一些领域名称（可选）
	 */
	private LabelInputMeta rowNumField;

	/**
	 * Running in parallel同时运行多个并行
	 */
	private LabelInputMeta runningInParallel;

	/**
	 * File encoding文件编码
	 */
	private LabelSelectMeta encoding;

	/**
	 * 列表
	 */
	private GridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public CsvInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			CsvInputMeta step = (CsvInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// TODO
			// Step name
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null,
					null, "CsvInput", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			// Filename
			this.filename = new LabelInputMeta(id + ".filename", "文件名", null,
					null, "文件名未填", step.getFilename(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filename.setSingle(true);
			// Delimiter
			this.delimiter = new LabelInputMeta(id + ".delimiter", "分隔符", null,
					null, null, String.valueOf(step.getDelimiter()), null,
					ValidateForm.getInstance().setRequired(true));
			// this.delimiterInsertTAB = new ButtonMeta(id
			// + ".btn.delimiterInsertTAB",
			// id + ".btn.delimiterInsertTAB", "Insert TAB", "Insert TAB");
			// this.delimiter
			// .addButton(new ButtonMeta[] { this.delimiterInsertTAB });
			this.delimiter.setSingle(true);
			// Enclosure
			this.enclosure = new LabelInputMeta(id + ".enclosure", "附件", null,
					null, null, step.getEnclosure(), null, ValidateForm
							.getInstance().setRequired(true));
			this.enclosure.setSingle(true);
			// NIO buffer size
			this.bufferSize = new LabelInputMeta(id + ".bufferSize", "NIO 缓冲量",
					null, null, null, String.valueOf(step.getBufferSize()),
					null, ValidateForm.getInstance().setNumber(true));
			this.bufferSize.setSingle(true);
			// Lazy conversion
			this.lazyConversionActive = new LabelInputMeta(id
					+ ".lazyConversionActive", "懒惰转换", null, null, null, String
					.valueOf(step.isLazyConversionActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Header row present
			this.headerPresent = new LabelInputMeta(id + ".headerPresent",
					"标题", null, null, null, String.valueOf(step
							.isHeaderPresent()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// Add filename to result
			this.isaddresult = new LabelInputMeta(id + ".isaddresult", "添加文件名",
					null, null, null, String.valueOf(step.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// The row number field name(optional)
			this.rowNumField = new LabelInputMeta(id + ".rowNumField",
					"该行号字段名称（可选）", null, null, null, step.getRowNumField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.rowNumField.setSingle(true);
			// Running in parallel
			this.runningInParallel = new LabelInputMeta(id
					+ ".runningInParallel", "同时运行多个并行", null, null, null,
					String.valueOf(step.isRunningInParallel()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// File encoding
			// TODO 得到列表
			this.encoding = new LabelSelectMeta(id + ".encoding", "文件编码", null,
					null, null, String.valueOf(step.getEncoding()), null, super
							.getEncoding());
			this.encoding.setSingle(true);
			this.encoding.isHasEmpty();
			// 列表
			this.fields = new GridMeta(id + "_fields", 120, 0, true);
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.typeCodes, false));

			GridHeaderDataMeta fieldTrimTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.fieldTrimType", "去空格类型", null, false, 100);
			fieldTrimTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.trimTypeDesc, false));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency",
							"货币类型", null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "小数",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "组",
							null, false, 50), fieldTrimTypeDataMeta });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.csvinput.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			TextFileInputField[] textFileInputField = step.getInputFields();
			if (textFileInputField != null && textFileInputField.length > 0) {
				for (int i = 0; i < textFileInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, textFileInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getFormat(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(textFileInputField[i]
											.getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, textFileInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(textFileInputField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT), });
				}
			}
			
			// 获取字段
			DivMeta getFieldsBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.csvinput.btn.getfields")
					.appendTo(getFieldsBtn);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.name, this.filename, this.delimiter,
					this.enclosure, this.bufferSize, this.lazyConversionActive,
					this.headerPresent, this.isaddresult, this.rowNumField,
					this.runningInParallel, this.encoding, this.fields, getFieldsBtn});

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
