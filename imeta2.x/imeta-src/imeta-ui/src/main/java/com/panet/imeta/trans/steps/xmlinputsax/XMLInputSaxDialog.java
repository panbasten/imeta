package com.panet.imeta.trans.steps.xmlinputsax;

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
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class XMLInputSaxDialog extends BaseStepDialog implements
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

	// File
	/**
	 * File or directory
	 */
	// private LabelInputMeta fileOrdirectory;
	// private ButtonMeta fileOrdirectoryAdd;
	// private ButtonMeta fileOrdirectorySelecter;
	/**
	 * Regular Expression
	 */
	// private LabelInputMeta regularExpression;
	/**
	 * Accept filenames from previous steps
	 */
	private LabelGridMeta selectedFiles;

	// private ButtonMeta delete;
	//	
	// private ButtonMeta edit;
	//
	// private ButtonMeta showFilename;

	// Content
	/**
	 * Include filename in output
	 */
	private LabelInputMeta includeFilename;

	/**
	 * Filename field name
	 */
	private LabelInputMeta filenameField;

	/**
	 * Row number in output
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * Row number field name
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Location(see also: tooltip help)
	 */
	private LabelGridMeta location;

	// Fields

	private LabelGridMeta elementFields;

	private LabelGridMeta fields;

	/**
	 * Get fields
	 */
//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public XMLInputSaxDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			XMLInputSaxMeta step = (XMLInputSaxMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------File
			 ******************************************************************/
			// File or directory
			// this.fileOrdirectory = new LabelInputMeta(id +
			// ".fileOrdirectory",
			// "文件或目录",null, null, null, String.valueOf(step.getFileName()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.fileOrdirectoryAdd = new ButtonMeta(id
			// + ".btn.fileOrdirectoryAdd", id + ".btn.fileOrdirectoryAdd",
			// "增加(A)...", "增加");
			// this.fileOrdirectorySelecter = new ButtonMeta(id
			// + ".btn.fileOrdirectorySelecter", id +
			// ".btn.fileOrdirectorySelecter",
			// "浏览(B)...", "浏览");
			// this.fileOrdirectory.addButton(new
			// ButtonMeta[]{fileOrdirectoryAdd,
			// fileOrdirectorySelecter});
			// this.fileOrdirectory.setSingle(true);
			// Regular Expression
			// this.regularExpression = new LabelInputMeta(id +
			// ".regularExpression",
			// "正规表达式",null, null, null, String.valueOf(step.getFileMask()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// Selected Expression
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件名称", 200);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fileId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件/目录", null, false, 200),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 200) });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles
					.setHasAdd(true, true,
							"jQuery.imeta.steps.xmlinputsax.selectedfiles.btn.fieldAdd");
			this.selectedFiles
					.setHasDelete(true, true,
							"jQuery.imeta.parameter.fieldsDelete");

			String[] fileName = step.getFileName();
			if (fileName != null && fileName.length > 0) {
				for (int i = 0; i < fileName.length; i++) {
					this.selectedFiles.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFileName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFileMask()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			// DivMeta showFilenamebtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.delete = new ButtonMeta(
			// id + ".btn.delete", id + ".btn.delete",
			// "删除", "删除");
			// this.delete.appendTo(showFilenamebtn);
			//			
			// this.edit = new ButtonMeta(
			// id + ".btn.edit", id + ".btn.edit",
			// "编辑", "编辑");
			// this.edit.appendTo(showFilenamebtn);
			//			
			// this.showFilename = new ButtonMeta(
			// id + ".btn.showFilename", id + ".btn.showFilename",
			// "查看文件", "查看文件");
			// this.showFilename.appendTo(showFilenamebtn);

			// this.meta.putTabContent(0, new BaseFormMeta[]
			// {this.fileOrdirectory,
			// this.regularExpression, this.selectedFiles});
			this.meta.putTabContent(0,
					new BaseFormMeta[] { this.selectedFiles });

			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Include filename in output
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"包含输出的文件", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeFilename
					.addClick("jQuery.imeta.steps.xmlinputsax.listeners.includeFilenameListeners");

			// Filename field name
			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"文件字段名称", null, null, null, step.getFilenameField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.filenameField.setDisabled(!step.includeFilename());

			// Row number in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "输出行号", null, null, null, String
							.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.xmlinputsax.listeners.includeRowNumberListeners");

			// Row number field name
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段名称", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			// Location
			this.location = new LabelGridMeta(id + "_location", "位置", 200);
			this.location.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_location.elementId", "#",
							null, false, 50),
					new GridHeaderDataMeta(id + "_location.element", "元素",
							null, false, 200), });
			this.location.setSingle(true);
			this.location.setHasBottomBar(true);
			this.location.setHasAdd(true, true,
					"jQuery.imeta.steps.xmlinputsax.location.btn.fieldAdd");
			this.location.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			
			XMLInputSaxFieldPosition[] xmlinputsaxPosition = step.getInputPosition();
			if(xmlinputsaxPosition !=null && xmlinputsaxPosition.length > 0){
				for (int i = 0; i < xmlinputsaxPosition.length; i++) {
					this.location.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, String.valueOf(xmlinputsaxPosition[i]
							      .getElementNr()),
							      GridCellDataMeta.CELL_TYPE_INPUT)
					});
				}
			}
			
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeRowNumber, this.rowNumberField, this.rowLimit,
					this.location });

			/*******************************************************************
			 * 标签2---------Filters
			 ******************************************************************/
			this.elementFields = new LabelGridMeta(id + "_elementFields", null,
					200);
			this.elementFields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_elementFields.elementId",
							"#", null, false, 50),
					new GridHeaderDataMeta(id + "_elementFields.element", "元素",
							null, false, 50),
					new GridHeaderDataMeta(id + "_elementFields.attribute",
							"定义属性", null, false, 150), });
			this.elementFields.setSingle(true);
			this.elementFields.setHasBottomBar(true);
			this.elementFields
					.setHasAdd(true, true,
							"jQuery.imeta.steps.xmlinputsax.elementfields.btn.fieldAdd");
			this.elementFields
					.setHasDelete(true, true,
							"jQuery.imeta.parameter.fieldsDelete");

			this.fields = new LabelGridMeta(id + "_fields", null, 200);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 100),
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
							.getOptionsByTrueAndFalse(false)),
					new GridHeaderDataMeta(id + "_fields.fieldPostion", "位置",
							null, false, 50), });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.xmlinputsax.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			XMLInputSaxField[] XMLInputSaxField = step.getInputFields();
			if (XMLInputSaxField != null && XMLInputSaxField.length > 0)
				for (int i = 0; i < XMLInputSaxField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, XMLInputSaxField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(XMLInputSaxField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(XMLInputSaxField[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(XMLInputSaxField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(XMLInputSaxField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(XMLInputSaxField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, XMLInputSaxField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, XMLInputSaxField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(XMLInputSaxField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(XMLInputSaxField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, XMLInputSaxField[i]
									.getFieldPositionsCode(),
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}

			DivMeta getFieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.xmlinputsax.btn.getfields")
			.appendTo(getFieldsbtn);
			
			this.meta.putTabContent(2, new BaseFormMeta[] { this.elementFields,
					this.fields, getFieldsbtn });

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
