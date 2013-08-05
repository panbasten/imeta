package com.panet.imeta.trans.steps.fixedinput;

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
import com.panet.imeta.ui.exception.ImetaException;

public class FixedInputDialog extends BaseStepDialog implements
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
	 * 以字节数表示的行宽度（不包括回车符 CR）
	 */
	private LabelInputMeta lineWidth;

	/**
	 * Line feeds present
	 */
	private LabelInputMeta lineFeedPresent;

	/**
	 * NIO 缓存大小
	 */
	private LabelInputMeta bufferSize;

	/**
	 * Lazy conversion
	 */
	private LabelInputMeta lazyConversionActive;

	/**
	 * Header row present
	 */
	private LabelInputMeta headerPresent;

	/**
	 * 以平行方式运行(Running in parallel)
	 */
	private LabelInputMeta runningInParallel;

	/**
	 * 换行符类型
	 */
	private LabelSelectMeta fileType;

	/**
	 * File encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * 添加到文件列表
	 */
	private LabelInputMeta isaddresult;

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
	public FixedInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			FixedInputMeta step = (FixedInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			// 文件名
			this.filename = new LabelInputMeta(id + ".filename", "文件名", null,
					null, "文件名未填", step.getFilename(), null, ValidateForm
							.getInstance().setRequired(true));
			this.filename.setSingle(true);
			// 以字节数表示的行宽度（不包括回车符 CR）
			this.lineWidth = new LabelInputMeta(id + ".lineWidth",
					"以字节数表示的行宽度-不包括回车符CR", null, null, null, String
							.valueOf(step.getLineWidth()), null, ValidateForm
							.getInstance().setNumber(true));
			this.lineWidth.setSingle(true);
			// Line feeds present
			this.lineFeedPresent = new LabelInputMeta(id + ".lineFeedPresent",
					"当前换行", null, null, null, String.valueOf(step
							.isLineFeedPresent()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			// NIO 缓存大小
			this.bufferSize = new LabelInputMeta(id + ".bufferSize",
					"NIO 缓存大小", null, null, null, String.valueOf(step
							.getBufferSize()), null, ValidateForm.getInstance()
							.setNumber(true));
			this.bufferSize.setSingle(true);
			// Lazy conversion
			this.lazyConversionActive = new LabelInputMeta(id
					+ ".lazyConversionActive", "懒惰转换", null, null, null, String
					.valueOf(step.isLazyConversionActive()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.lazyConversionActive.setSingle(true);
			
			// Header row present
			this.headerPresent = new LabelInputMeta(id + ".headerPresent",
					"头标题", null, null, null, String.valueOf(step
							.isHeaderPresent()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.headerPresent.setSingle(true);
			
			// 以平行方式运行
			this.runningInParallel = new LabelInputMeta(id
					+ ".runningInParallel", "以平行方式运行", null, null, null, String
					.valueOf(step.isRunningInParallel()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.runningInParallel.addClick("jQuery.imeta.steps.fixedinput.listeners.runningListeners");
			
			// 换行符类型
			this.fileType = new LabelSelectMeta(id + ".fileType", "换行符类型",
					null, null, null, String.valueOf(step.getFileType()), null,
					super.getOptionListByStringArrayWithNumber(
							FixedInputMeta.fileTypeDesc, false));
			this.fileType.setDisabled(!step.isRunningInParallel());
			
			// 编码
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, String.valueOf(step.getEncoding()), null, super
							.getEncoding());
			this.encoding.setSingle(true);
			
			// 添加到文件列表
			this.isaddresult = new LabelInputMeta(id + ".isaddresult",
					"添加到文件列表", null, null, null, String.valueOf(step
							.isAddResultFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.isaddresult.setSingle(true);
			
			// 列表
			this.fields = new GridMeta(id + "_fields", 150, 0, true);
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
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.fieldWidth", "宽度",
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
							null, false, 50), fieldTrimTypeDataMeta });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.fixedinput.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			FixedFileInputField[] fixedFileInputField = step
					.getFieldDefinition();
			if (fixedFileInputField != null && fixedFileInputField.length > 0) {
				for (int i = 0; i < fixedFileInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, fixedFileInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(fixedFileInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, fixedFileInputField[i]
									.getFormat(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(fixedFileInputField[i]
											.getWidth()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(fixedFileInputField[i]
											.getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(fixedFileInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, fixedFileInputField[i]
									.getCurrency(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, fixedFileInputField[i]
									.getDecimal(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, fixedFileInputField[i]
									.getGrouping(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(fixedFileInputField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			}

			DivMeta getFieldsBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.fixedinput.btn.getfields")
					.appendTo(getFieldsBtn);

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.filename, this.lineWidth, this.lineFeedPresent,
					this.bufferSize, this.lazyConversionActive,
					this.headerPresent, this.runningInParallel, this.fileType, this.encoding,
					this.isaddresult, this.fields, getFieldsBtn });

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
