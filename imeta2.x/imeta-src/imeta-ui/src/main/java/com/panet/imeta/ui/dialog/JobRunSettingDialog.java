package com.panet.imeta.ui.dialog;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnDiv.ColumnDivMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.job.BaseJobDialog;
import com.panet.imeta.job.JobDialogInterface;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.trans.TransExecutionConfiguration;
import com.panet.imeta.trans.debug.TransDebugMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class JobRunSettingDialog extends BaseJobDialog implements
		JobDialogInterface {

	public boolean local, remote, cluster, preview, debug, safe;
	public Date replayDate;
	public TransDebugMeta transDebugMeta;
	public TransExecutionConfiguration executionConfiguration;

	private String rootId;

	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private ColumnFieldsetMeta runTypeFieldset;

	private String runClick = "jQuery.imeta.execute.run.btn.runJob";

	private String runBtnName = "运行";

	/**
	 * 自定义日志级别
	 */
	private int cLogLevel;

	private Map<String, String> params = new HashMap<String, String>();

	private Map<String, String> vars = new HashMap<String, String>();

	/**
	 * 单机域
	 */
	private ColumnDivMeta runTypeSingleField;

	/**
	 * 单机按钮
	 */
	private LabelInputMeta runTypeSingle;

	/**
	 * 选择机器
	 */
	private LabelSelectMeta runTypeSingleComputer;

	/**
	 * 集群域
	 */
	private ColumnDivMeta runTypeClusterField;

	/**
	 * 集群
	 */
	private LabelInputMeta runTypeCluster;

	/**
	 * 提交转换
	 */
	private LabelInputMeta submitTrans;

	/**
	 * 准备执行
	 */
	private LabelInputMeta prepareExecute;

	/**
	 * 开始执行
	 */
	private LabelInputMeta startExecute;

	/**
	 * 显示转换
	 */
	private LabelInputMeta showTrans;

	/**
	 * 细节域
	 */
	private ColumnFieldsetMeta detailFieldset;

	/**
	 * 日志级别
	 */
	private LabelSelectMeta logLevel;

	/**
	 * 参数
	 */
	private LabelGridMeta parameters;

	/**
	 * 变量
	 */
	private LabelGridMeta variables;

	/**
	 * 运行
	 */
	private ButtonMeta run;

	/**
	 * 取消
	 */
	private ButtonMeta cancel;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param jobMeta
	 */
	public JobRunSettingDialog(JobMeta jobMeta, boolean local, boolean remote,
			boolean cluster, boolean preview, boolean debug, Date replayDate,
			boolean safe) {
		super(jobMeta);
		this.local = local;
		this.remote = remote;
		this.cluster = cluster;
		this.preview = preview;
		this.debug = debug;
		this.replayDate = replayDate;
		this.safe = safe;

	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();

			String id = "run_" + super.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);
			this.columnFormMeta.setSingle(true);

			// 单机或集群方式执行域
			if (false) {
				this.runTypeFieldset = new ColumnFieldsetMeta(id
						+ "_runTypeFieldset", "单机或集群方式执行");
				this.runTypeFieldset.setSingle(true);

				ColumnDivMeta runTypeDiv = new ColumnDivMeta();
				runTypeDiv.setSingle(true);
				this.runTypeSingle = new LabelInputMeta(id + ".runType",
						"单机方式", null, null, null, "single",
						InputDataMeta.INPUT_TYPE_RADIO, null);
				this.runTypeSingle.setLabelAfter(true);
				this.runTypeCluster = new LabelInputMeta(id + ".runType",
						"集群方式", null, null, null, "cluster",
						InputDataMeta.INPUT_TYPE_RADIO, null);
				this.runTypeCluster.setLabelAfter(true);
				runTypeDiv.putDivContent(new BaseFormMeta[] {
						this.runTypeSingle, this.runTypeCluster });

				this.runTypeSingleField = new ColumnDivMeta();
				this.runTypeSingleComputer = new LabelSelectMeta(id
						+ ".runTypeSingleComputer", "选择机器", null, null, null,
						null, null, null);
				this.runTypeSingleComputer.setSingle(true);
				this.runTypeSingleField
						.putDivContent(new BaseFormMeta[] { this.runTypeSingleComputer });

				this.runTypeClusterField = new ColumnDivMeta();
				this.submitTrans = new LabelInputMeta(id + ".submitTrans",
						"提交转换", null, null, null, null,
						InputDataMeta.INPUT_TYPE_CHECKBOX, null);
				this.submitTrans.setSingle(true);
				this.submitTrans.setLabelAfter(true);
				this.prepareExecute = new LabelInputMeta(
						id + ".prepareExecute", "准备执行", null, null, null, null,
						InputDataMeta.INPUT_TYPE_CHECKBOX, null);
				this.prepareExecute.setSingle(true);
				this.prepareExecute.setLabelAfter(true);
				this.startExecute = new LabelInputMeta(id + ".startExecute",
						"开始执行", null, null, null, null,
						InputDataMeta.INPUT_TYPE_CHECKBOX, null);
				this.startExecute.setSingle(true);
				this.startExecute.setLabelAfter(true);
				this.showTrans = new LabelInputMeta(id + ".showTrans", "显示转换",
						null, null, null, null,
						InputDataMeta.INPUT_TYPE_CHECKBOX, null);
				this.showTrans.setSingle(true);
				this.showTrans.setLabelAfter(true);
				this.runTypeClusterField.putDivContent(new BaseFormMeta[] {
						this.submitTrans, this.prepareExecute,
						this.startExecute, this.showTrans, });

				this.runTypeFieldset.putFieldsetsContent(new BaseFormMeta[] {
						runTypeDiv, this.runTypeSingleField,
						this.runTypeClusterField });
			}

			// 细节
			this.detailFieldset = new ColumnFieldsetMeta(id + "_detail", "细节");
			this.detailFieldset.setSingle(true);
			this.logLevel = new LabelSelectMeta(id + ".logLevel", "日志级别", null,
					null, null, String.valueOf(cLogLevel), null, super.getLoglevel());
			this.logLevel.setSingle(true);
			this.detailFieldset
					.putFieldsetsContent(new BaseFormMeta[] { this.logLevel });

			ColumnDivMeta pvDiv = new ColumnDivMeta();
			pvDiv.setSingle(true);
			// 参数
			this.parameters = new LabelGridMeta(id + "_parameters", "参数", 200);
			this.parameters.setHasBottomBar(true);
			this.parameters.setHasAdd(true, true,
					"jQuery.imeta.execute.run.btn.parametersAdd");
			this.parameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.parameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_parameters.parameterId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, true, 50),
					new GridHeaderDataMeta(id + "_parameters.parameter", "参数",
							null, true, 100),
					new GridHeaderDataMeta(id + "_parameters.value", "值", null,
							true, 100) });

			if (this.params.size() > 0) {
				Iterator<String> iter = this.params.keySet().iterator();
				int i = 0;
				while (iter.hasNext()) {
					String key = iter.next();
					String value = Const.NVL(this.params.get(key), "");

					this.parameters.addRow(new Object[] {
							String.valueOf(++i),
							new GridCellDataMeta(null, key,
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, value,
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			// 变量
			this.variables = new LabelGridMeta(id + "_variables", "变量", 200);
			this.variables.setHasBottomBar(true);
			this.variables.setHasAdd(true, true,
					"jQuery.imeta.execute.run.btn.variablesAdd");
			this.variables.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.variables.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_variables.parameterId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, true, 50),
					new GridHeaderDataMeta(id + "_variables.parameter", "变量",
							null, true, 100),
					new GridHeaderDataMeta(id + "_variables.value", "值", null,
							true, 100) });

			if (this.vars.size() > 0) {
				Iterator<String> iter = this.vars.keySet().iterator();
				int i = 0;
				while (iter.hasNext()) {
					String key = iter.next();
					String value = Const.NVL(this.vars.get(key), "");

					this.variables.addRow(new Object[] {
							String.valueOf(++i),
							new GridCellDataMeta(null, key,
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, value,
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			}

			pvDiv.putDivContent(new BaseFormMeta[] { this.parameters,
					this.variables });

			// this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
			// this.runTypeFieldset, this.detailFieldset, pvDiv });
			this.columnFormMeta.putFieldsetsContent(new BaseFormMeta[] {
					this.detailFieldset, pvDiv });

			this.run = new ButtonMeta(id + ".btn.run", id + ".btn.run",
					runBtnName, runBtnName);
			this.run.putProperty("rootId", rootId);
			this.run.addClick(runClick);
			this.cancel = new ButtonMeta(id + ".btn.cancel",
					id + ".btn.cancel", "取消", "取消");
			this.cancel.putProperty("rootId", rootId);
			this.cancel.addClick("jQuery.imeta.execute.run.btn.cancel");

			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					this.run, this.cancel });

			cArr.add(this.columnFormMeta.getFormJo());
			rtn.put("items", cArr);

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public ButtonMeta getRun() {
		return run;
	}

	public String getRunClick() {
		return runClick;
	}

	public void setRunClick(String runClick) {
		this.runClick = runClick;
	}

	public String getRunBtnName() {
		return runBtnName;
	}

	public void setRunBtnName(String runBtnName) {
		this.runBtnName = runBtnName;
	}

	public void setcLogLevel(int cLogLevel) {
		this.cLogLevel = cLogLevel;
	}

	public void clearParams() {
		this.params.clear();
	}

	public void addParams(String key, String value) {
		this.params.put(key, value);
	}

	public void clearVars() {
		this.vars.clear();
	}

	public void addVars(String key, String value) {
		this.vars.put(key, value);
	}

}
