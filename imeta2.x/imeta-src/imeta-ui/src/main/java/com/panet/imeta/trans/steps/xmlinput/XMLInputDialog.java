package com.panet.imeta.trans.steps.xmlinput;

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
import com.panet.imeta.core.Const;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.steps.sortedmerge.Messages;
import com.panet.imeta.ui.exception.ImetaException;

public class XMLInputDialog extends BaseStepDialog implements
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

	/**********************************************************
	 * 文件
	 **********************************************************/

	/**
	 * 文件或目录
	 */
	// private LabelInputMeta fileOrdirectory;
	// private ButtonMeta addBtn;
	// private ButtonMeta browse;
	/**
	 * 正则表达式
	 */
	// private LabelInputMeta regularExpression;
	/**
	 * 已选择的文件
	 */
	private LabelGridMeta selectedFiles;
	//	
	// private ButtonMeta delete;
	//	
	// private ButtonMeta edit;
	//
	// private ButtonMeta showFilename;

	/********************************************************
	 * 内容
	 ********************************************************/

	/**
	 * 在输出中包含文件名称？
	 */
	private LabelInputMeta includeFilename;
	private LabelInputMeta filenameField;

	/**
	 * 输出中包含记录号？
	 */
	private LabelInputMeta includeRowNumber;
	private LabelInputMeta rowNumberField;

	/**
	 * 限制
	 */
	private LabelInputMeta rowLimit;

	/**
	 * 需要跳过的标题记录数
	 */
	private LabelInputMeta nrRowsToSkip;

	/**
	 * Base URI for relative URIs
	 */
	private LabelInputMeta fileBaseURI;

	/**
	 * Ignore external entities
	 */
	private LabelInputMeta ignoreEntities;

	/**
	 * Support XML namespaces
	 */
	private LabelInputMeta namespaceAware;

	/**
	 * 位置
	 */
	private LabelGridMeta location;

	/***************************************************
	 * 字段
	 ***************************************************/

	/**
	 * Files
	 */
	private LabelGridMeta fields;

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
	public XMLInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			XMLInputMeta step = (XMLInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------File
			 ******************************************************************/

			// 文件或内容
			// this.fileOrdirectory = new LabelInputMeta(id +
			// ".fileOrdirectory",
			// "文件或内容",null, null, null, String.valueOf(step.getFileName()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.addBtn = new ButtonMeta(id
			// + ".btn.fileOrdirectoryAdd", id + ".btn.addBtn",
			// "增加(A)...", "增加");
			// this.browse = new ButtonMeta(id
			// + ".btn.browse", id + ".btn.fileOrdirectorySelecter",
			// "浏览(B)...", "浏览");
			// this.fileOrdirectory.addButton(new ButtonMeta[]{ this.addBtn,
			// this.browse});
			// this.fileOrdirectory.setSingle(true);
			//
			// // 正则表达式
			// this.regularExpression = new LabelInputMeta(id +
			// ".regularExpression",
			// "正则表达式",null, null, null, String.valueOf(step.getFileMask()),
			// null,
			// ValidateForm.getInstance().setRequired(true));
			// this.regularExpression.setSingle(true);
			// 已选择的文件
			this.selectedFiles = new LabelGridMeta(id + "_selectedFiles",
					"已选择的文件：", 200);
			this.selectedFiles.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_selectedFiles.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_selectedFiles.fileName",
							"文件/目录", null, false, 150),
					new GridHeaderDataMeta(id + "_selectedFiles.fileMask",
							"通配符", null, false, 80) });
			this.selectedFiles.setSingle(true);
			this.selectedFiles.setHasBottomBar(true);
			this.selectedFiles.setHasAdd(true, true,
					"jQuery.imeta.steps.xmlinput.selectedfiles.btn.fieldAdd");
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
									GridCellDataMeta.CELL_TYPE_INPUT), });
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
			// "显示文件名(s)...", "显示文件名(s)...");
			// this.showFilename.appendTo(showFilenamebtn);

			// this.meta.putTabContent(0, new BaseFormMeta[] {
			// this.fileOrdirectory,this.regularExpression, this.selectedFiles
			// });
			this.meta.putTabContent(0,
					new BaseFormMeta[] { this.selectedFiles });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/

			// 在输出中包含文件名称
			this.includeFilename = new LabelInputMeta(id + ".includeFilename",
					"在输出中包含文件名称？", null, null, null, String.valueOf(step
							.includeFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeFilename.setSingle(true);
			this.includeFilename
					.addClick("jQuery.imeta.steps.xmlinput.listeners.includeFilenameListeners");

			this.filenameField = new LabelInputMeta(id + ".filenameField",
					"文件名称字段名称", null, null, "文件名称 字段名称 必须填写", step
							.getFilenameField(), null, ValidateForm
							.getInstance().setRequired(false));
			this.filenameField.setSingle(true);
			this.filenameField.setDisabled(!step.includeFilename());

			// 在输出中包含记录号
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "在输出中包含记录号？", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.includeRowNumber.setSingle(true);
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.xmlinput.listeners.includeRowNumberListeners");

			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"记录数字字段名称", null, null, "名称必须填写", step.getRowNumberField(),
					null, ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setSingle(true);
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			// 限制
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, "限制必须填写", String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setRequired(false));
			this.rowLimit.setSingle(true);

			// 需要跳过的记录数
			this.nrRowsToSkip = new LabelInputMeta(id + ".nrRowsToSkip",
					"需要跳过的记录数", null, null, "记录数必须填写", String.valueOf(step
							.getNrRowsToSkip()), null, ValidateForm
							.getInstance().setRequired(false));
			this.nrRowsToSkip.setSingle(true);

			// Base URI for relative URIs
			this.fileBaseURI = new LabelInputMeta(id + ".fileBaseURI",
					"相应的网址相对网址", null, null, "相应的网址相对网址必须填写", step
							.getFileBaseURI(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.fileBaseURI.setSingle(true);

			// Ignore external entities
			this.ignoreEntities = new LabelInputMeta(id + ".ignoreEntities",
					"忽略外部实体", null, null, null, String.valueOf(step
							.isIgnoreEntities()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.ignoreEntities.setSingle(true);

			// Support XML namespaces
			this.namespaceAware = new LabelInputMeta(id + ".namespaceAware",
					"支持XML命名空间", null, null, null, String.valueOf(step
							.isNamespaceAware()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.namespaceAware.setSingle(true);

			// 位置
			this.location = new LabelGridMeta(id + "_location", "位置：", 200);
			this.location.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_location.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_location.element", "元素",
							null, false, 50), });
			this.location.setSingle(true);
			this.location.setHasBottomBar(true);
			this.location.setHasAdd(true, true,
					"jQuery.imeta.steps.xmlinput.location.btn.fieldAdd");
			this.location.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] inputPosition = step.getInputPosition();
			if (inputPosition != null && inputPosition.length > 0) {
				for (int i = 0; i < inputPosition.length; i++) {
					this.location.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null,
									step.getInputPosition()[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.includeFilename, this.filenameField,
					this.includeRowNumber, this.rowNumberField, this.rowLimit,
					this.nrRowsToSkip, this.fileBaseURI, this.ignoreEntities,
					this.namespaceAware, this.location });

			/*******************************************************************
			 * 字段
			 ******************************************************************/
			this.fields = new LabelGridMeta(id + "_fields", null, 200);
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.typeCodes, false));
			GridHeaderDataMeta fieldFormatDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldFormat", "格式", null, false, 100);
			String dats[] = Const.getDateFormats();
			String nums[] = Const.getNumberFormats();
			int totsize = dats.length + nums.length;
			String formats[] = new String[totsize];
			for (int x = 0; x < dats.length; x++)
				formats[x] = dats[x];
			for (int x = 0; x < nums.length; x++)
				formats[dats.length + x] = nums[x];
			fieldFormatDataMeta.setOptions(super.getOptionsByStringArray(
					formats, false));

			GridHeaderDataMeta fieldTrimTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.fieldTrimType", "修整类型", null, false, 100);
			fieldTrimTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							XMLInputField.trimTypeDesc, false));
			GridHeaderDataMeta fieldRepeatDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldRepeat", "重复", null, false, 100);
			fieldRepeatDataMeta.setOptions(super
					.getOptionsByStringArrayWithBooleanValue(new String[] {
							Messages.getString("System.Combo.Yes"),
							Messages.getString("System.Combo.No") }, false));
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 100),
					fieldTypeDataMeta,
					fieldFormatDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制数",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",
							null, false, 50),
					fieldTrimTypeDataMeta,
					fieldRepeatDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldPosition", "位置",
							null, false, 50) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.xmlinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			XMLInputField[] xmlinputfield = step.getInputFields();
			if (xmlinputfield != null && xmlinputfield.length > 0) {
				for (int i = 0; i < xmlinputfield.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, xmlinputfield[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, xmlinputfield[i]
									.getFormat(),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, xmlinputfield[i]
									.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, xmlinputfield[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, xmlinputfield[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i].getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(xmlinputfield[i]
											.getFieldPosition()),
									GridCellDataMeta.CELL_TYPE_INPUT), });
				}
			}

			DivMeta getFieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.xmlinput.btn.getfields")
			.appendTo(getFieldsbtn);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields,
					getFieldsbtn });

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
