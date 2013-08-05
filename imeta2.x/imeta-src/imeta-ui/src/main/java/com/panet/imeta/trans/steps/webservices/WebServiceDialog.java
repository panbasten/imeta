package com.panet.imeta.trans.steps.webservices;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class WebServiceDialog extends BaseStepDialog implements
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

	/***************************************************************************
	 * ***************************8 Wab Services
	 **************************************************************************/

	/**
	 * URL
	 */
	private LabelInputMeta url;

	/**
	 * 操作
	 */
	private LabelInputMeta operationName;

	/**
	 * 将输入的数据传到输出
	 */
	private LabelInputMeta passingInputData;

	/**
	 * V2.x/3.0 兼容模式
	 */
	private LabelInputMeta compatible;

	/**
	 * 重复元素名称
	 */
	private LabelInputMeta repeatingElementName;

	/**
	 * return the complete reply from the service as a String
	 */
	private LabelInputMeta returningReplyAsString;

	/**
	 * HTTP 授权
	 */
	private ColumnFieldsetMeta httpAccredit;

	/**
	 * HTTP 登陆
	 */
	private LabelInputMeta httpLogin;

	/**
	 * HTTP 密码
	 */
	private LabelInputMeta httpPassword;

	/**
	 * 使用代理
	 */
	private ColumnFieldsetMeta useDeputize;

	/**
	 * 代理主机
	 */
	private LabelInputMeta proxyHost;

	/**
	 * 代理端口
	 */
	private LabelInputMeta proxyPort;

	/**
	 * 增加输入
	 */
	// private ButtonMeta addInput;
	/**
	 * 增加输出
	 */
	// private ButtonMeta addOutput;
	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public WebServiceDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			WebServiceMeta step = (WebServiceMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "WEB服务" });
			this.meta.setSingle(true);
			/*******************************************************************
			 * 标签0 Web services
			 ******************************************************************/
			// URL
			this.url = new LabelInputMeta(id + ".url", "URL", null, null,
					"URL必须填写", step.getUrl(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.url.setSingle(true);

			// 操作
			this.operationName = new LabelInputMeta(id + ".operationName",
					"操作", null, null , "操作必须填写", step.getOperationName(), null, 
					ValidateForm.getInstance().setRequired(true));
			this.operationName.setSingle(true);

			// 将输入的数据传到输出
			this.passingInputData = new LabelInputMeta(
					id + ".passingInputData", "将输入的数据传到输出", null, null, null,
					String.valueOf(step.isPassingInputData()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.passingInputData.setSingle(true);

			// v2.x/3.0兼容模式
			this.compatible = new LabelInputMeta(id + ".compatible",
					"v2.x/3.0兼容模式", null, null, null, String.valueOf(step
							.isCompatible()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.compatible.setSingle(true);

			// 重复元素名称
			this.repeatingElementName = new LabelInputMeta(id
					+ ".repeatingElementName", "重复元素名称", null, null,
					"重复元素名称必须填写", step.getRepeatingElementName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.repeatingElementName.setSingle(true);

			// Return the complete reply from the service as a String
			this.returningReplyAsString = new LabelInputMeta(id
					+ ".returningReplyAsString", "返回值作为一个字符串", null, null,
					null, String.valueOf(step.isReturningReplyAsString()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.returningReplyAsString.setSingle(true);

			// HTTP 授权
			this.httpAccredit = new ColumnFieldsetMeta(null, "HTTP 授权");
			this.httpAccredit.setSingle(true);

			// HTTP 登陆
			this.httpLogin = new LabelInputMeta(id + ".httpLogin", "HTTP 登陆",
					null, null, "HTTP登陆必须填写", step.getHttpLogin(), null,
					ValidateForm.getInstance().setRequired(true));
			this.httpLogin.setSingle(true);

			// HTTP密码
			this.httpPassword = new LabelInputMeta(id + ".httpPassword",
					"HTTP 密码", null, null, "HTTP密码必须填写",
					step.getHttpPassword(), InputDataMeta.INPUT_TYPE_PASSWORD, ValidateForm.getInstance()
							.setRequired(true));
			this.httpPassword.setSingle(true);

			this.httpAccredit.putFieldsetsContent(new BaseFormMeta[] {
					this.httpLogin, this.httpPassword });

			// 使用代理
			this.useDeputize = new ColumnFieldsetMeta(null, "使用代理");
			this.useDeputize.setSingle(true);

			// 代理主机
			this.proxyHost = new LabelInputMeta(id + ".proxyHost", "代理主机",
					null, null, "代理主机必须填写", step.getProxyHost(), null,
					ValidateForm.getInstance().setRequired(true));
			this.proxyHost.setSingle(true);

			// 代理端口
			this.proxyPort = new LabelInputMeta(id + ".proxyPort", "代理端口",
					null, null, "代理端口必须填写", step.getProxyPort(), null,
					ValidateForm.getInstance().setRequired(true));
			this.proxyPort.setSingle(true);

			this.useDeputize.putFieldsetsContent(new BaseFormMeta[] {
					this.proxyHost, this.proxyPort });

			this.meta.putTabContent(0, new BaseFormMeta[] { this.url,
					this.operationName, this.passingInputData, this.compatible,
					this.repeatingElementName, this.returningReplyAsString,
					this.httpAccredit, this.useDeputize });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定，取消
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			// this.addInput = new ButtonMeta(id + ".btn.addInput", id
			// + ".btn.addInput", "增加输入", "增加输入");
			// this.addOutput = new ButtonMeta(id + ".btn.addOutput", id
			// + ".btn.addOutput", "增加输出", "增加输出");
			// columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
			// this.addInput, this.addOutput });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
