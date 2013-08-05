package com.panet.imeta.trans.steps.salesforceinput;

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
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SalesforceInputDialog extends BaseStepDialog implements
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
	 * Salesforce Webservice URL
	 */
	private LabelInputMeta targeturl;

	/**
	 * Username
	 */
	private LabelInputMeta username;

	/**
	 * Password
	 */
	private LabelInputMeta password;

	/**
	 * Test connection
	 */
	// private ButtonMeta testConnection;
	/**
	 * Module
	 */
	private LabelSelectMeta module;

	/**
	 * Query condition
	 */
	private LabelTextareaMeta condition;

	// Content
	/**
	 * Additional fields
	 */
	private ColumnFieldsetMeta additionalFieldsColumn;

	/**
	 * Include url in output
	 */
	private LabelInputMeta includeTargetURL;

	/**
	 * Url fieldname
	 */
	private LabelInputMeta targetURLField;

	/**
	 * Include Module in output
	 */
	private LabelInputMeta includeModule;

	/**
	 * Module fieldname
	 */
	private LabelInputMeta moduleField;

	/**
	 * Include SQL in output
	 */
	private LabelInputMeta includeSQL;

	/**
	 * SQL fieldname
	 */
	private LabelInputMeta sqlField;

	/**
	 * Include timestamp in output
	 */
	private LabelInputMeta includeTimestamp;

	/**
	 * Timestamp fieldname
	 */
	private LabelInputMeta timestampField;

	/**
	 * Include rownum in output
	 */
	private LabelInputMeta includeRowNumber;

	/**
	 * Rownum fieldname
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Time out
	 */
	private LabelInputMeta timeout;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

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
	public SalesforceInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SalesforceInputMeta step = (SalesforceInputMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Salesforce Webservice URL
			this.targeturl = new LabelInputMeta(id + ".targeturl",
					"Salesforce的Web服务的URL", null, null, null, step
							.getTargetURL(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.targeturl.setSingle(true);

			// Username
			this.username = new LabelInputMeta(id + ".username", "用户名", null,
					null, null, step.getUserName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.username.setSingle(true);

			// Password
			this.password = new LabelInputMeta(id + ".password", "密码", null,
					null, null, step.getPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(true));
			this.password.setSingle(true);

			// Test connection
			// DivMeta tetConnectionbtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.testConnection = new ButtonMeta(id + ".btn.tetConnection",
			// id + ".btn.tetConnection", "测试连接",
			// "测试连接");
			// this.testConnection.appendTo(tetConnectionbtn);

			// Module
			this.module = new LabelSelectMeta(id + "module", "模块", null, null,
					null, step.getModule(), null, null);
			this.module.setSingle(true);

			// Query condition
			this.condition = new LabelTextareaMeta(id + ".condition", "查询条件",
					null, null, null, step.getCondition(), 5, null);
			this.condition.setSingle(true);

			this.meta
					.putTabContent(0, new BaseFormMeta[] { this.targeturl,
							this.username, this.password, this.module,
							this.condition });
			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Additional fields
			this.additionalFieldsColumn = new ColumnFieldsetMeta(id, "其他领域");
			this.additionalFieldsColumn.setSingle(true);

			// Include url in output
			this.includeTargetURL = new LabelInputMeta(
					id + ".includeTargetURL", "包含URL的输出", null, null, null,
					String.valueOf(step.includeTargetURL()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeTargetURL
					.addClick("jQuery.imeta.steps.salesforceinput.listeners.includeTargetURLListeners");

			// Url fieldname
			this.targetURLField = new LabelInputMeta(id + ".targetURLField",
					"URL字段", null, null, null, step.getTargetURLField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.targetURLField.setDisabled(!step.includeTargetURL());

			// Include Module in output
			this.includeModule = new LabelInputMeta(id + ".includeModule",
					"包括模块的输出", null, null, null, String.valueOf(step
							.includeModule()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeModule
					.addClick("jQuery.imeta.steps.salesforceinput.listeners.includeModuleListeners");

			// Module fieldname
			this.moduleField = new LabelInputMeta(id + ".moduleField", "模块字段",
					null, null, null, step.getModuleField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.moduleField.setDisabled(!step.includeModule());

			// Include SQL in output
			this.includeSQL = new LabelInputMeta(id + ".includeSQL", "包括SQL输出",
					null, null, null, String.valueOf(step.includeSQL()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeSQL
					.addClick("jQuery.imeta.steps.salesforceinput.listeners.includeSQLListeners");

			// SQL fieldname
			this.sqlField = new LabelInputMeta(id + ".sqlField", "数据库字段名",
					null, null, null, step.getSQLField(), null, ValidateForm
							.getInstance().setRequired(true));
			this.sqlField.setDisabled(!step.includeSQL());

			// Include timestamp in output
			this.includeTimestamp = new LabelInputMeta(
					id + ".includeTimestamp", "包括时标输出", null, null, null,
					String.valueOf(step.includeTimestamp()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeTimestamp
					.addClick("jQuery.imeta.steps.salesforceinput.listeners.includeTimestampListeners");

			// Timestamp fieldname
			this.timestampField = new LabelInputMeta(id + ".timestampField",
					"时标字段", null, null, null, step.getTimestampField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.timestampField.setDisabled(!step.includeTimestamp());

			// Include rownum in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "包括行号在输出", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.salesforceinput.listeners.includeRowNumberListeners");

			// Rownum fieldname
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());

			this.additionalFieldsColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.includeTargetURL, this.targetURLField,
					this.includeModule, this.moduleField, this.includeSQL,
					this.sqlField, this.includeTimestamp, this.timestampField,
					this.includeRowNumber, this.rowNumberField });

			// Time out
			this.timeout = new LabelInputMeta(id + ".timeout", "超时", null,
					null, null, step.getTimeOut(), null, ValidateForm
							.getInstance().setNumber(true));
			this.timeout.setSingle(true);

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, step.getRowLimit(), null, ValidateForm
							.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.additionalFieldsColumn, this.timeout, this.rowLimit });
			/*******************************************************************
			 * 标签2---------Fields
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 300, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.name", "名称", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.field", "字段", null,
							false, 50),
					(new GridHeaderDataMeta(id + "_fields.type", "类型", null,
							false, 100)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.typeCodes, false)),
					new GridHeaderDataMeta(id + "_fields.format", "格式", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.length", "长度", null,
							false, 50),
					new GridHeaderDataMeta(id + "_fields.precision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.currencySymbol", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.decimalSymbol", "十进制",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.groupSymbol", "组",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.trimType", "去空格类型",
							null, false, 80)).setOptions(super
							.getOptionsByStringArrayWithNumberValue(
									ValueMeta.trimTypeDesc, false)),
					(new GridHeaderDataMeta(id + "_fields.repeated", "重复",
							null, false, 50)).setOptions(super
							.getOptionsByTrueAndFalse(false)) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.salesforceinput.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			SalesforceInputField[] salesforceInputField = step.getInputFields();
			if (salesforceInputField != null && salesforceInputField.length > 0)
				for (int i = 0; i < salesforceInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getField(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									String.valueOf(salesforceInputField[i]
											.getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getFormat(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(salesforceInputField[i]
											.getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(salesforceInputField[i]
											.getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, salesforceInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(salesforceInputField[i]
											.getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(salesforceInputField[i]
											.isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT)

					});
				}

			// 获取字段
			DivMeta getFieldsbtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.salesforceinput.btn.getfields")
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
