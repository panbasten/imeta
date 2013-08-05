package com.panet.imeta.trans.steps.ldapinput;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class LDAPInputDialog extends BaseStepDialog implements
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
	 * Connection
	 */
	private ColumnFieldsetMeta connectionColum;
	/**
	 * Host
	 */
	private LabelInputMeta Host;
	/**
	 * Post
	 */
	private LabelInputMeta port;
	/**
	 * Use Authentication
	 */
	private LabelInputMeta useAuthentication;
	/**
	 * UseName
	 */
	private LabelInputMeta userName;
	/**
	 * Password
	 */
	private LabelInputMeta password;
	/**
	 * Test connection
	 */
	// private ButtonMeta testConnectionBtn;
	/**
	 * Search Settings
	 */
	private ColumnFieldsetMeta settings;
	/**
	 * Search base
	 */
	private LabelInputMeta searchBase;
	/**
	 * Filter String
	 */
	private LabelTextareaMeta filterString;

	// Content
	/**
	 * Additional Fields
	 */
	private ColumnFieldsetMeta additionalFieldsColum;
	/**
	 * Include rowNum in output
	 */
	private LabelInputMeta includeRowNumber;
	/**
	 * RowNum filedName
	 */
	private LabelInputMeta rowNumberField;

	/**
	 * Limit
	 */
	private LabelInputMeta rowLimit;

	/**
	 * Time limit
	 */
	private LabelInputMeta timeLimit;

	/**
	 * 多值字段分隔符
	 */
	private LabelInputMeta multiValuedSeparator;

	// Fields
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
	public LDAPInputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			LDAPInputMeta step = (LDAPInputMeta) super.getStep();

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
			// Connection
			this.connectionColum = new ColumnFieldsetMeta(id, "连接");
			this.connectionColum.setSingle(true);
			// Host
			this.Host = new LabelInputMeta(id + ".Host", "主机", null, null,
					null, step.getHost(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.Host.setSingle(true);
			// Port
			this.port = new LabelInputMeta(id + ".port", "端口", null, null,
					null, step.getPort(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.port.setSingle(true);
			// Use Authentication
			this.useAuthentication = new LabelInputMeta(id
					+ ".useAuthentication", "使用验证", null, null, null, String
					.valueOf(step.UseAuthentication()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.useAuthentication
					.addClick("jQuery.imeta.steps.ldapinput.listeners.useAuthenticationListeners");
			// UseName
			this.userName = new LabelInputMeta(id + ".userName", "用户名", null,
					null, null, step.getUserName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.userName.setSingle(true);
			this.userName.setDisabled(!step.UseAuthentication());
			// Password
			this.password = new LabelInputMeta(id + ".password", "密码", null,
					null, null, step.getPassword(),
					InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm
							.getInstance().setRequired(true));
			this.password.setSingle(true);
			this.password.setDisabled(!step.UseAuthentication());
			// // Test connection
			// DivMeta testConnectionbtn = new DivMeta(new
			// NullSimpleFormDataMeta(), true);
			// this.testConnectionBtn = new ButtonMeta(id +
			// ".btn.testConnectionBtn", id
			// + ".btn.testConnectionBtn", "测试连接", "测试连接");
			// this.testConnectionBtn.appendTo(testConnectionbtn);

			this.connectionColum.putFieldsetsContent(new BaseFormMeta[] {
					this.Host, this.port, this.useAuthentication,
					this.userName, this.password });

			// Settings
			this.settings = new ColumnFieldsetMeta(id, "设置");
			this.settings.setSingle(true);
			// Search base
			this.searchBase = new LabelInputMeta(id + ".searchBase", "搜索原则",
					null, null, null, step.getSearchBase(), null, ValidateForm
							.getInstance().setRequired(true));
			this.searchBase.setSingle(true);
			// Filter String
			this.filterString = new LabelTextareaMeta(id + ".filterString",
					"过滤字符串", null, null, null, step.getFilterString(), 1, null);
			this.filterString.setSingle(true);

			this.settings.putFieldsetsContent(new BaseFormMeta[] {
					this.searchBase, this.filterString });

			this.meta.putTabContent(0, new BaseFormMeta[] {
					this.connectionColum, this.settings });

			/*******************************************************************
			 * 标签1---------Content
			 ******************************************************************/
			// Additional Fields
			this.additionalFieldsColum = new ColumnFieldsetMeta(id, "其他领域");
			this.additionalFieldsColum.setSingle(true);
			// Include rowNum in output
			this.includeRowNumber = new LabelInputMeta(
					id + ".includeRowNumber", "包含输出行号", null, null, null,
					String.valueOf(step.includeRowNumber()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.includeRowNumber
					.addClick("jQuery.imeta.steps.ldapinput.listeners.includeRowNumberListeners");
			// RowNum filedName
			this.rowNumberField = new LabelInputMeta(id + ".rowNumberField",
					"行号字段名", null, null, null, step.getRowNumberField(), null,
					ValidateForm.getInstance().setRequired(true));
			this.rowNumberField.setDisabled(!step.includeRowNumber());
			this.additionalFieldsColum.putFieldsetsContent(new BaseFormMeta[] {
					this.includeRowNumber, this.rowNumberField });

			// Limit
			this.rowLimit = new LabelInputMeta(id + ".rowLimit", "限制", null,
					null, null, String.valueOf(step.getRowLimit()), null,
					ValidateForm.getInstance().setNumber(true));
			this.rowLimit.setSingle(true);
			// Time limit
			this.timeLimit = new LabelInputMeta(id + ".timeLimit", "时间限制",
					null, null, null, String.valueOf(step.getTimeLimit()),
					null, ValidateForm.getInstance().setNumber(true));
			this.timeLimit.setSingle(true);
			// 多值字段分隔符
			this.multiValuedSeparator = new LabelInputMeta(id
					+ ".multiValuedSeparator", "多值字段分隔符", null, null, null,
					step.getMultiValuedSeparator(), null, ValidateForm
							.getInstance().setRequired(true));
			this.multiValuedSeparator.setSingle(true);
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.additionalFieldsColum, this.rowLimit, this.timeLimit,
					this.multiValuedSeparator });

			/*******************************************************************
			 * 标签2---------Filters
			 ******************************************************************/
			// 字段列表
			this.fields = new GridMeta(id + "_fields", 220, 0, true);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "名称",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldAttribute", "属性",
							null, false, 50),
					(new GridHeaderDataMeta(id + "_fields.fieldType", "类型",
							null, false, 50)).setOptions(super
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
					"jQuery.imeta.steps.ldapinput.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			LDAPInputField[] LDAPInputField = step.getInputFields();
			if (LDAPInputField != null && LDAPInputField.length > 0)
				for (int i = 0; i < LDAPInputField.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, LDAPInputField[i]
									.getName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getAttribute()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getFormat()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i]
											.getCurrencySymbol()),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, LDAPInputField[i]
									.getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, LDAPInputField[i]
									.getGroupSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].getTrimType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String
									.valueOf(LDAPInputField[i].isRepeated()),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 获取字段
			DivMeta getFieldsBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			super.getGetfields("jQuery.imeta.steps.ldapinput.btn.getfields")
			.appendTo(getFieldsBtn);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.fields,
					getFieldsBtn });

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
