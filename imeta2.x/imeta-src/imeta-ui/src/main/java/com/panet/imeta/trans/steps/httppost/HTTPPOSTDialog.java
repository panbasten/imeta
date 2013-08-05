package com.panet.imeta.trans.steps.httppost;

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
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class HTTPPOSTDialog extends BaseStepDialog implements
		StepDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * Step name
	 */
	private LabelInputMeta name;

	/**
	 * URL
	 */
	private LabelInputMeta url;

	/**
	 * Accept URL from field?
	 */
	private LabelInputMeta urlInField;

	/**
	 * URL field name
	 */
	private LabelSelectMeta urlField;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Request entity field
	 */
	private LabelSelectMeta requestEntity;

	/**
	 * Post a file
	 */
	private LabelInputMeta postafile;

	/**
	 * Result fieldname
	 */
	private LabelInputMeta fieldName;

	/**
	 * Body Parameters
	 */
	private LabelGridMeta bodyParameters;
	private ButtonMeta getBodyFieldsBtn;

	/**
	 * Query parameters
	 */
	private LabelGridMeta queryParameters;
	private ButtonMeta getQueryFieldsBtn;

	/**
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public HTTPPOSTDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			HTTPPOSTMeta step = (HTTPPOSTMeta) super.getStep();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// Step name 步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// URL 网址
			this.url = new LabelInputMeta(id + ".url", "网址", null, null,
					"网址必须填写", step.getUrl(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.url.setDisabled(step.isUrlInField());
			this.url.setSingle(true);

			// Accept URL from field? 从字段中获取网址？
			this.urlInField = new LabelInputMeta(id + ".urlInField",
					"从字段中获取网址？", null, null, null, String.valueOf(step
							.isUrlInField()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.urlInField.setSingle(true);
			this.urlInField
					.addClick("jQuery.imeta.steps.httppost.listeners.urlInFieldListeners");

			// URL field name 网址字段名称
			this.urlField = new LabelSelectMeta(id + ".urlField", "URL字段名称",
					null, null, null, step.getUrlField(), null, super
							.getPrevStepResultFields());
			this.urlField.setSingle(true);
			this.urlField.setDisabled(!step.isUrlInField());

			// Encoding 编码方式
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码方式", null,
					null, null, step.getEncoding(), null, super.getEncoding());
			this.encoding.setSingle(true);

			// Request entity field 请求实体字段
			this.requestEntity = new LabelSelectMeta(id + ".requestEntity",
					"请求实体字段", null, null, null, step.getRequestEntity(), null,
					super.getPrevStepResultFields());
			this.requestEntity.setSingle(true);

			// Post a file 发布文件
			this.postafile = new LabelInputMeta(id + ".postafile", "发布文件",
					null, null, null, String.valueOf(step.isPostAFile()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(true));
			this.postafile.setSingle(true);

			// Result fieldname 返回字段名称
			this.fieldName = new LabelInputMeta(id + ".fieldName", "返回字段名称",
					null, null, "字段名称必须填写", step.getFieldName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.fieldName.setSingle(true);

			// Body Parameters 主体数据
			this.bodyParameters = new LabelGridMeta(id + "_bodyParameters",
					"主体数据", 200);
			this.bodyParameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_bodyParameters.fieldId", "#",
							null, false, 50),
					new GridHeaderDataMeta(
							id + "_bodyParameters.argumentField", "名称", null,
							false, 200),
					new GridHeaderDataMeta(id
							+ "_bodyParameters.argumentParameter", "数据", null,
							false, 200) });
			String[] argumentField = step.getArgumentField();
			GridCellDataMeta bfield, bparameter;
			for (int i = 0; i < argumentField.length; i++) {
				bfield = new GridCellDataMeta(null, step.getArgumentField()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				bparameter = new GridCellDataMeta(null, step
						.getArgumentParameter()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				this.bodyParameters.addRow(new Object[] {
						String.valueOf(i + 1), bfield, bparameter });
			}
			this.bodyParameters.setHasBottomBar(true);
			this.bodyParameters.setHasAdd(true, true,
					"jQuery.imeta.steps.httppost.bodyfield.btn.fieldAdd");
			this.bodyParameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.bodyParameters.setSingle(true);

			DivMeta bodyfieldBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getBodyFieldsBtn = new ButtonMeta(
					id + ".btn.getBodyFieldsBtn", id + ".btn.getBodyFieldsBtn",
					"获取字段", "获取字段");
			this.getBodyFieldsBtn.appendTo(bodyfieldBtn);
			this.getBodyFieldsBtn
					.addClick("jQuery.imeta.steps.httppost.btn.getbodyfields");
			this.getBodyFieldsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getBodyFieldsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getBodyFieldsBtn.putProperty("elementName", super
					.getStepName());
			this.getBodyFieldsBtn.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());
			// Query Parameters 查询参数
			this.queryParameters = new LabelGridMeta(id + "_queryParameters",
					"查询参数：", 200);
			this.queryParameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_queryParameters.fieldId",
							"#", null, false, 50),
					new GridHeaderDataMeta(id + "_queryParameters.queryField",
							"名称", null, false, 200),
					new GridHeaderDataMeta(id
							+ "_queryParameters.queryParameter", "值", null,
							false, 200) });
			String[] queryField = step.getQueryField();
			GridCellDataMeta qfield, qparameter;
			for (int i = 0; i < queryField.length; i++) {
				qfield = new GridCellDataMeta(null, step.getQueryField()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				qparameter = new GridCellDataMeta(null, step
						.getQueryParameter()[i],
						GridCellDataMeta.CELL_TYPE_INPUT);
				this.queryParameters.addRow(new Object[] {
						String.valueOf(i + 1), qfield, qparameter });
			}
			this.queryParameters.setHasBottomBar(true);
			this.queryParameters.setHasAdd(true, true,
					"jQuery.imeta.steps.httppost.queryfield.btn.fieldAdd");
			this.queryParameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.queryParameters.setSingle(true);

			DivMeta queryfieldBtn = new DivMeta(new NullSimpleFormDataMeta(),
					true);
			this.getQueryFieldsBtn = new ButtonMeta(id
					+ ".btn.getQueryFieldsBtn", id + ".btn.getQueryFieldsBtn",
					"获取字段", "获取字段");
			this.getQueryFieldsBtn.appendTo(queryfieldBtn);
			this.getQueryFieldsBtn
					.addClick("jQuery.imeta.steps.httppost.btn.getqueryfields");
			this.getQueryFieldsBtn.putProperty("roType",
					RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
			this.getQueryFieldsBtn.putProperty("roName", super.getTransMeta()
					.getName());
			this.getQueryFieldsBtn.putProperty("elementName", super
					.getStepName());
			this.getQueryFieldsBtn.putProperty("directoryId", super
					.getTransMeta().getDirectory().getID());

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.url, this.urlInField, this.urlField, this.encoding,
					this.requestEntity, this.postafile, this.fieldName,
					this.bodyParameters, bodyfieldBtn, this.queryParameters,
					queryfieldBtn });

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
