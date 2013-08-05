package com.panet.imeta.trans.steps.textfileoutput;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.grid.GridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TextFileOutputDialog extends BaseStepDialog implements
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

	/**
	 * 文件 0
	 */

	/**
	 * 文件名称
	 */
	private LabelInputMeta fileName;

	/**
	 * Run this as command instead?
	 */
	private LabelInputMeta fileAsCommand;

	/**
	 * Do not create file at start?
	 */
	private LabelInputMeta doNotOpenNewFileInit;

	/**
	 * Accept file name from field?
	 */
	private LabelInputMeta fileNameInField;

	/**
	 * File name field
	 */
	private LabelInputMeta fileNameField;

	/**
	 * 扩展名
	 */
	private LabelInputMeta extension;

	/**
	 * 在文件里面包含步骤数
	 */
	private LabelInputMeta stepNrInFilename;

	/**
	 * Include partition nr in filename?
	 */
	private LabelInputMeta partNrInFilename;

	/**
	 * 在文件里包含日期
	 */
	private LabelInputMeta dateInFilename;

	/**
	 * 在文件里包含时间
	 */
	private LabelInputMeta timeInFilename;

	/**
	 * Specify Date time format
	 */
	private LabelInputMeta specifyingFormat;

	/**
	 * Date time format
	 */
	private LabelSelectMeta dateTimeFormat;

	/**
	 * Add filenames to result
	 */
	private LabelInputMeta addToResultFilenames;

	/**
	 * 内容 1
	 */
	/**
	 * 追加方式
	 */
	private LabelInputMeta fileAppended;

	/**
	 * 分隔符
	 */
	private LabelInputMeta separator;

	/**
	 * 封闭符
	 */
	private LabelInputMeta enclosure;

	/**
	 * 强制在字段周围添加封闭符？
	 */
	private LabelInputMeta enclosureForced;

	/**
	 * 头部
	 */
	private LabelInputMeta headerEnabled;

	/**
	 * 尾部
	 */
	private LabelInputMeta footerEnabled;

	/**
	 * 格式
	 */
	private LabelSelectMeta fileFormat;

	/**
	 * Compression
	 */
	private LabelSelectMeta fileCompression;

	/**
	 * 编码
	 */
	private LabelSelectMeta encoding;

	/**
	 * Right pad fields
	 */
	private LabelInputMeta padded;

	/**
	 * Fast data dump
	 */
	private LabelInputMeta fastDump;

	/**
	 * Split every ... rows
	 */
	private LabelInputMeta splitEvery;

	/**
	 * Add Ending line of file
	 */
	private LabelInputMeta endedLine;

	/**
	 * 字段 2
	 */
	private GridMeta fields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public TextFileOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			TextFileOutputMeta step = (TextFileOutputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------文件
			 ******************************************************************/
			// 文件名称
			this.fileName = new LabelInputMeta(id + ".fileName", "文件名称", null,
					null, "文件名称必须填写", step.getFileName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.fileName.setSingle(true);
			this.fileName.setDisabled(step.isFileNameInField());

			// Run this as command instead?
			this.fileAsCommand = new LabelInputMeta(id + ".fileAsCommand",
					"以命令方式运行", null, null, null, String.valueOf(step
							.isFileAsCommand()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.fileAsCommand.setDisabled(step.isFileNameInField());

			// Do not create file at start
			this.doNotOpenNewFileInit = new LabelInputMeta(id
					+ ".doNotOpenNewFileInit", "不要建立档案", null, null, null,
					String.valueOf(step.isDoNotOpenNewFileInit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// Accept file name from field
			this.fileNameInField = new LabelInputMeta(id + ".fileNameInField",
					"接受文件名从字段获得", null, null, null, String.valueOf(step
							.isFileNameInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.fileNameInField
					.addClick("jQuery.imeta.steps.textfileoutput.listeners.fileNameInFieldListener");

			// File name field
			this.fileNameField = new LabelInputMeta(id + ".fileNameField",
					"文件名字段", null, null, null, step.getFileNameField(), null,
					ValidateForm.getInstance().setRequired(false));
			this.fileNameField.setSingle(true);
			this.fileNameField.setDisabled(!step.isFileNameInField());

			// 扩展名
			this.extension = new LabelInputMeta(id + ".extension", "扩展名", null,
					null, "扩展名必须填写", step.getExtension(), null, ValidateForm
							.getInstance().setRequired(false));
			this.extension.setSingle(true);

			// 在文件名里面包含步骤数
			this.stepNrInFilename = new LabelInputMeta(
					id + ".stepNrInFilename", "在文件名里面包含步骤数", null, null, null,
					String.valueOf(step.isStepNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// Include partition nr in filename?
			this.partNrInFilename = new LabelInputMeta(
					id + ".partNrInFilename", "在文件名里面包含分区数", null, null, null,
					String.valueOf(step.isPartNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));

			// 在文件名里包含日期
			this.dateInFilename = new LabelInputMeta(id + ".dateInFilename",
					"在文件名里包含日期", null, null, null, String.valueOf(step
							.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.dateInFilename.setDisabled(step.isSpecifyingFormat());

			// 在文件名里包含时间
			this.timeInFilename = new LabelInputMeta(id + ".timeInFilename",
					"在文件名里包含时间", null, null, null, String.valueOf(step
							.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.timeInFilename.setDisabled(step.isSpecifyingFormat());

			// Specify Date time format
			this.specifyingFormat = new LabelInputMeta(
					id + ".specifyingFormat", "指定日期时间格式", null, null, null,
					String.valueOf(step.isSpecifyingFormat()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.specifyingFormat
					.addClick("jQuery.imeta.steps.textfileoutput.listeners.specifyingFormatListener");

			// Date time format
			this.dateTimeFormat = new LabelSelectMeta(id + ".dateTimeFormat",
					"日期时间格式", null, null, null, step.getDateTimeFormat(), null,
					super.getDateFormats());
			this.dateTimeFormat.setSingle(true);
			this.dateTimeFormat.setDisabled(step.isSpecifyingFormat());

			// Add filenames to result
			this.addToResultFilenames = new LabelInputMeta(id
					+ ".addToResultFilenames", "添加文件名到结果", null, null, null,
					String.valueOf(step.isAddToResultFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.fileName,
					this.fileAsCommand, this.doNotOpenNewFileInit,
					this.fileNameInField, this.fileNameField, this.extension,
					this.stepNrInFilename, this.partNrInFilename,
					this.dateInFilename, this.timeInFilename,
					this.specifyingFormat, this.dateTimeFormat,
					this.addToResultFilenames });

			/*******************************************************************
			 * 标签1---------内容
			 ******************************************************************/

			// 追加方式
			this.fileAppended = new LabelInputMeta(id + ".fileAppended",
					"追加方式", null, null, null, String.valueOf(step
							.isFileAppended()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.fileAppended.setSingle(true);

			// 分隔符
			this.separator = new LabelInputMeta(id + ".separator", "分隔符", null,
					null, null, step.getSeparator(), null, ValidateForm
							.getInstance().setRequired(false));
			this.separator.setSingle(true);

			// 封闭符
			this.enclosure = new LabelInputMeta(id + ".enclosure", "封闭符", null,
					null, null, step.getEnclosure(), null, ValidateForm
							.getInstance().setRequired(false));
			this.enclosure.setSingle(true);

			// 强制在字段周围加封闭符
			this.enclosureForced = new LabelInputMeta(id + ".enclosureForced",
					"强制在字段周围加封闭符", null, null, null, String.valueOf(step
							.isEnclosureForced()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.enclosureForced.setSingle(true);

			// 头部
			this.headerEnabled = new LabelInputMeta(id + ".headerEnabled", "头部",
					null, null, null, String.valueOf(step.isHeaderEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.headerEnabled.setSingle(true);

			// 尾部
			this.footerEnabled = new LabelInputMeta(id + ".footerEnabled",
					"尾部", null, null, null, String.valueOf(step
							.isFooterEnabled()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.footerEnabled.setSingle(true);

			// 格式
			List<OptionDataMeta> fileFormatOptions = new ArrayList<OptionDataMeta>();
			fileFormatOptions.add(new OptionDataMeta("DOS", "DOS"));
			fileFormatOptions.add(new OptionDataMeta("Unix", "Unix"));

			this.fileFormat = new LabelSelectMeta(id + ".fileFormat", "格式",
					null, null, null, step.getFileFormat(), null,
					fileFormatOptions);
			this.fileFormat.setSingle(true);

			// Compression
			List<OptionDataMeta> compressionOptions = new ArrayList<OptionDataMeta>();
			for (String c : TextFileOutputMeta.fileCompressionTypeCodes) {
				compressionOptions.add(new OptionDataMeta(c, c));
			}
			this.fileCompression = new LabelSelectMeta(id + ".fileCompression",
					"压缩", null, null, null, step.getFileCompression(), null,
					compressionOptions);
			this.fileCompression.setSingle(true);

			// 编码
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, super.getEncoding());
			this.encoding.setSingle(true);

			// Right pad fields
			this.padded = new LabelInputMeta(id + ".padded", "右侧补充字段", null,
					null, null, String.valueOf(step.isPadded()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.padded.setSingle(true);

			// Fast data dump
			this.fastDump = new LabelInputMeta(id + ".fastDump", "快速数据导出（没格式）",
					null, null, null, String.valueOf(step.isFastDump()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.fastDump.setSingle(true);

			// Split every ... rows
			this.splitEvery = new LabelInputMeta(id + ".splitEvery",
					"拆分 ... 每一行", null, null, null, String.valueOf(step
							.getSplitEvery()), null, null);
			this.splitEvery.setSingle(true);

			// Add Ending line of file
			this.endedLine = new LabelInputMeta(id + ".endedLine", "添加文件的结束行",
					null, null, null, step.getEndedLine(), null, ValidateForm
							.getInstance().setRequired(false));

			this.endedLine.setSingle(true);
			this.meta.putTabContent(1, new BaseFormMeta[] { this.fileAppended,
					this.separator, this.enclosure, this.enclosureForced,
					this.headerEnabled, this.footerEnabled, this.fileFormat,
					this.fileCompression, this.encoding, this.padded,
					this.fastDump, this.splitEvery });

			/*******************************************************************
			 * 标签2---------字段
			 ******************************************************************/
			// 字段
			this.fields = new GridMeta(id + "_fields", 200, 0, true);

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
					new GridHeaderDataMeta(id + "_fields.fieldName", "字段名",
							null, false, 100),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 100),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency",
							"货币类型", null, false, 100),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "小数",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",
							null, false, 50),
					fieldTrimTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldNullif", "空字符串",
							null, false, 100) });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.textfileoutput.btn.fieldsAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			TextFileField[] outputFields = step.getOutputFields();
			if (outputFields != null && outputFields.length > 0) {
				for (int i = 0; i < outputFields.length; i++) {
					if (outputFields[i] != null)
						this.fields.addRow(new Object[] {
								String.valueOf(i + 1),
								new GridCellDataMeta(null, outputFields[i]
										.getName(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String
										.valueOf(outputFields[i].getType()),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, outputFields[i]
										.getFormat(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, String
										.valueOf(outputFields[i].getLength()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null,
										String.valueOf(outputFields[i]
												.getPrecision()),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, outputFields[i]
										.getCurrencySymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, outputFields[i]
										.getDecimalSymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null, outputFields[i]
										.getGroupingSymbol(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(null,
										String.valueOf(outputFields[i]
												.getTrimType()),
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, outputFields[i]
										.getNullString(),
										GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			}

			// Get Fields
			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields(
					"jQuery.imeta.steps.textfileoutput.btn.getfields")
					.appendTo(div);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields, div });

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
