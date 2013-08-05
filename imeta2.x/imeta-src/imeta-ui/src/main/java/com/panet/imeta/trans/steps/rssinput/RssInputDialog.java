package com.panet.imeta.trans.steps.rssinput;

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

public class RssInputDialog extends BaseStepDialog implements
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

	// General
	/**
	 * get URL from fields
	 */
	private ColumnFieldsetMeta getURLFromFields;

	/**
	 * URL is defined in a field
	 */
	private LabelInputMeta urlInField;

	/**
	 * URL Field
	 */
	private LabelSelectMeta UrlField;

	/**
	 * URL list
	 */
	private LabelGridMeta urlList;

	// Content
	/**
	 * Read articles from
	 */
	private LabelInputMeta readfrom;

	/**
	 * Max number of articles
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Additional fields
	 */
	private ColumnFieldsetMeta additionalFieldsColumn;

	/**
	 * Include url in output
	 */
	private LabelInputMeta includeUrl;

	/**
	 * Url fieldname
	 */
	private LabelInputMeta urlFieldname;

	/**
	 * Include rownum in output
	 */
	private LabelInputMeta includeRownumInOutput;

	/**
	 * Rownum fieldname
	 */
	private LabelInputMeta rownumFieldname;

	// Fields
	/**
	 * 字段列表
	 */
	private GridMeta fields;

//	private ButtonMeta getfields;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public RssInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			RssInputMeta step = (RssInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Get URL from fields
			this.getURLFromFields = new ColumnFieldsetMeta(id, "从字段获取网址");
			this.getURLFromFields.setSingle(true);

			// URL is defined in a field
			boolean urlinfield = step.urlInField();
			this.urlInField = new LabelInputMeta(id + ".urlInField",
					"通过字段定义URL", null, null, null, String.valueOf(step
							.urlInField()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(true));
			this.urlInField
					.addClick("jQuery.imeta.steps.rssinput.listeners.urlInField");

			// URL Field
			this.UrlField = new LabelSelectMeta(id + ".urlField", "URL字段",
					null, null, null, step.geturlField(), null, super
							.getPrevStepResultFields());
			this.UrlField.setDisabled(!step.urlInField());
			this.UrlField.setSingle(true);
			this.UrlField.setHasEmpty(true);

			this.getURLFromFields.putFieldsetsContent(new BaseFormMeta[] {
					this.urlInField, this.UrlField });

			// URL list
			this.urlList = new LabelGridMeta(id + "_urlList", "URL列表", 100);
			this.urlList.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_urlList.urlId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_urlList.url", "URL", null,
							false, 400) });
			this.urlList.setSingle(true);
			this.urlList.setHasBottomBar(true);
			this.urlList.setHasAdd(true, !urlinfield,
					"jQuery.imeta.steps.rssinput.urlList.btn.fieldAdd");
			this.urlList.setHasDelete(true, !urlinfield,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] URL = step.getUrl();
			if (URL != null && URL.length > 0) {
				GridCellDataMeta url;
				for (int i = 0; i < URL.length; i++) {
					url = new GridCellDataMeta(null, step.getUrl()[i],
							GridCellDataMeta.CELL_TYPE_INPUT);
					url.setDisabled(urlinfield);
					this.urlList.addRow(new Object[] { String.valueOf(i + 1),
							url });
				}
			}

			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.getURLFromFields, this.urlList });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Read articles from
			this.readfrom = new LabelInputMeta(id + ".readArticlesFrom",
					"阅读文章", null, null, null, step.getReadFrom(), null,
					ValidateForm.getInstance().setRequired(true));
			this.readfrom.setSingle(true);

			// Max number of articles
			this.rowLimit = new LabelInputMeta(id
					+ ".rowLimit", "最高若干条款", null, null, null,
					String.valueOf(step.getRowLimit()), null, ValidateForm
							.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			// Additional fields
			this.additionalFieldsColumn = new ColumnFieldsetMeta(id, "其他领域");
			this.additionalFieldsColumn.setSingle(true);

			// Include url in output
			this.includeUrl = new LabelInputMeta(id + ".includeUrl",
					"包含URL的输出", null, null, null, String.valueOf(step
							.includeUrl()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(true));
			this.includeUrl.setDisabled(step.urlInField());
			this.includeUrl
					.addClick("jQuery.imeta.steps.rssinput.listeners.includeUrlInOutput");

			// Url fieldname
			this.urlFieldname = new LabelInputMeta(id + ".urlFieldname",
					"Url字段", null, null, null, step.getUrlFieldname(), null,
					ValidateForm.getInstance().setRequired(true));
			this.urlFieldname.setDisabled(!step.urlInField());
			this.urlFieldname.setDisabled(!step.includeUrl());

			// Include rownum in output
			this.includeRownumInOutput = new LabelInputMeta(id
					+ ".includeRownumInOutput", "包括行号输出", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRownumInOutput
					.addClick("jQuery.imeta.steps.rssinput.listeners.includeRownumInOutput");

			// Rownum fieldname
			this.rownumFieldname = new LabelInputMeta(id + ".rownumFieldname",
					"行号字段", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rownumFieldname.setDisabled(!step.includeRowNumber());

			this.additionalFieldsColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.includeUrl, this.urlFieldname,
					this.includeRownumInOutput, this.rownumFieldname });

			this.meta.putTabContent(1, new BaseFormMeta[] { this.readfrom,
					this.rowLimit, this.additionalFieldsColumn });
			/*******************************************************************
			 * 标签2---------Fields
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 220, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldColumn", "列",
							null, false, 50)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									RssInputField.ColumnDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 80),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false, 60),
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
					"jQuery.imeta.steps.rssinput.fields.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			RssInputField[] rssInputField = step.getInputFields();
			if (rssInputField != null && rssInputField.length > 0)
				for (int i = 0; i < rssInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, rssInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getColumn()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i]
											.getDecimalSymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(rssInputField[i]
											.getGroupSymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(rssInputField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT)

					});
				}
			// TODO DEMO

			// 获取来自头部数据的字段
			DivMeta getFieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.rssinput.btn.getfields")
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
