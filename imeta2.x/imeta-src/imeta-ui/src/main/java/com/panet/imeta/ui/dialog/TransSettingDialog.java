package com.panet.imeta.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.parameters.NamedParamsDefault;
import com.panet.imeta.trans.BaseTransDialog;
import com.panet.imeta.trans.TransDependency;
import com.panet.imeta.trans.TransDialogInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class TransSettingDialog extends BaseTransDialog implements
		TransDialogInterface {

	private String winId;

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	private MenuTabMeta meta;

	private LabelInputMeta name;

	private LabelInputMeta description;

	private LabelTextareaMeta extendedDescription;

	private LabelSelectMeta transstatus;

	private LabelInputMeta transversion;

	private ButtonMeta dicSelecter;

	private LabelInputMeta directory;

	private LabelInputMeta createdUser;

	private LabelInputMeta createdDate;

	private LabelInputMeta modifiedUser;

	private LabelInputMeta modifiedDate;

	private LabelSelectMeta readLogStep;

	private LabelSelectMeta inputLogStep;

	private LabelSelectMeta writeLogStep;

	private LabelSelectMeta outputLogStep;

	private LabelSelectMeta updateLogStep;

	private LabelSelectMeta rejectedLogStep;

	private LabelSelectMeta logConnection;

	private LabelInputMeta logTable;

	private LabelInputMeta stepPerformanceLogTable;

	private LabelInputMeta batchIdUsed;

	private LabelInputMeta logfieldUsed;

	private LabelGridMeta parameters;

	private LabelSelectMeta maxDateConnection;

	private LabelInputMeta maxDateTable;

	private LabelInputMeta maxDateField;

	private LabelInputMeta maxDateOffset;
	private LabelInputMeta maxDateDifference;

	private LabelGridMeta dependencies;

	private LabelInputMeta sizeRowset, feedbackShown, feedbackSize,
			usingUniqueConnections, sharedObjectsFile,
			usingThreadPriorityManagment;

	private LabelInputMeta capturingStepPerformanceSnapShots,
			stepPerformanceCapturingDelay;

	private ButtonMeta ok;

	private ButtonMeta cancel;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public TransSettingDialog(TransMeta transMeta, String winId) {
		super(transMeta);
		this.winId = winId;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			TransMeta transMeta = super.getTransMeta();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到tab
			this.meta = new MenuTabMeta(id, new String[] { "转换", "参数", "日志",
					"日期", "依赖", "杂项", "分区", "监控" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 0 转换
			 ******************************************************************/
			this.name = new LabelInputMeta(id + ".name", "转换名称", null, null,
					null, transMeta.getName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.name.setSingle(true);

			this.description = new LabelInputMeta(id + ".description", "描述",
					null, null, null, transMeta.getDescription(), null, null);
			this.description.setSingle(true);

			this.extendedDescription = new LabelTextareaMeta(id
					+ ".extendedDescription", "扩展描述", null, null, null,
					transMeta.getExtendedDescription(), 5, null);
			this.extendedDescription.setSingle(true);

			// 得到状态列表
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < TransMeta.transstatusList.length; i++) {
				options.add(new OptionDataMeta(String.valueOf(i),
						TransMeta.transstatusList[i]));
			}
			this.transstatus = new LabelSelectMeta(id + ".transstatus", "状态",
					null, null, null, String
							.valueOf(transMeta.getTransstatus()), null, options);

			this.transversion = new LabelInputMeta(id + ".transversion", "版本",
					null, null, null, transMeta.getTransversion(), null, null);

			this.dicSelecter = new ButtonMeta(id + ".btn.dicSelecter", id
					+ ".btn.dicSelecter", "选择", "选择");
			this.dicSelecter.addClick("jQuery.imeta.trans.setting.btn.pathBtn");
			this.directory = new LabelInputMeta(id + ".directory", "目录", null,
					null, null, transMeta.getDirectory().toString(), null, null);
			this.directory.addButton(dicSelecter);
			this.directory.setSingle(true);
			this.directory.setReadonly(true);

			this.createdUser = new LabelInputMeta(id + ".createdUser", "创建者",
					null, null, null, transMeta.getCreatedUser(), null, null);
			this.createdUser.setDisabled(true);

			this.createdDate = new LabelInputMeta(id + ".createdDate", "创建日期",
					null, null, null, DateFormatUtils.format(transMeta
							.getCreatedDate(),
							Const.GENERALIZED_DATE_TIME_SHOW_FORMAT), null,
					null);
			this.createdDate.setDisabled(true);

			this.modifiedUser = new LabelInputMeta(id + ".modifiedUser",
					"最近修改的用户", null, null, null, transMeta.getModifiedUser(),
					null, null);
			this.modifiedUser.setDisabled(true);

			this.modifiedDate = new LabelInputMeta(id + ".modifiedDate",
					"最近修改的日期", null, null, null, DateFormatUtils.format(
							transMeta.getModifiedDate(),
							Const.GENERALIZED_DATE_TIME_SHOW_FORMAT), null,
					null);
			this.modifiedDate.setDisabled(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { name, description,
					extendedDescription, transstatus, transversion, directory,
					createdUser, createdDate, modifiedUser, modifiedDate });

			/*******************************************************************
			 * 1 参数
			 ******************************************************************/
			this.parameters = new LabelGridMeta(id + "_parameters", "参数", 200);
			this.parameters.setHasBottomBar(true);
			this.parameters.setHasAdd(true, true,
					"jQuery.imeta.trans.setting.btn.parametersAdd");
			this.parameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.parameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_parameters.parameterId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_parameters.parameter", "参数名",
							null, false, 220),
					new GridHeaderDataMeta(id + "_parameters.description",
							"描述", null, false, 220) });

			NamedParamsDefault namedParams = (NamedParamsDefault) transMeta
					.getNamedParams();
			String parameter, description;
			String[] keys = namedParams.listParameters();
			int index = 1;
			for (int i = 0; i < keys.length; i++) {
				parameter = (String) keys[i];
				if (StringUtils.isEmpty(parameter)) {
					continue;
				}
				description = namedParams.getParameterDescription(parameter);
				if (!Const.isEmpty(description)
						&& description
								.equals(DatabaseMeta.EMPTY_OPTIONS_STRING))
					description = "";

				this.parameters.addRow(new Object[] {
						String.valueOf(index++),
						new GridCellDataMeta(null, parameter,
								GridCellDataMeta.CELL_TYPE_INPUT),
						new GridCellDataMeta(null, description,
								GridCellDataMeta.CELL_TYPE_INPUT) });
			}

			this.parameters.setSingle(true);
			this.meta.putTabContent(1, new BaseFormMeta[] { this.parameters });

			/*******************************************************************
			 * 2 日志
			 ******************************************************************/

			this.readLogStep = new LabelSelectMeta(id + ".readLogStep",
					"读取日志步骤", null, null, null, getStepName(transMeta
							.getReadStep()), null, super.getAllStepNames());
			this.readLogStep.setSingle(true);
			this.readLogStep.setHasEmpty(true);

			this.inputLogStep = new LabelSelectMeta(id + ".inputLogStep",
					"输入日志步骤", null, null, null, getStepName(transMeta
							.getInputStep()), null, super.getAllStepNames());
			this.inputLogStep.setSingle(true);
			this.inputLogStep.setHasEmpty(true);

			this.writeLogStep = new LabelSelectMeta(id + ".writeLogStep",
					"写入日志步骤", null, null, null, getStepName(transMeta
							.getWriteStep()), null, super.getAllStepNames());
			this.writeLogStep.setSingle(true);
			this.writeLogStep.setHasEmpty(true);

			this.outputLogStep = new LabelSelectMeta(id + ".outputLogStep",
					"输出日志步骤", null, null, null, getStepName(transMeta
							.getOutputStep()), null, super.getAllStepNames());
			this.outputLogStep.setSingle(true);
			this.outputLogStep.setHasEmpty(true);

			this.updateLogStep = new LabelSelectMeta(id + ".updateLogStep",
					"更新日志步骤", null, null, null, getStepName(transMeta
							.getUpdateStep()), null, super.getAllStepNames());
			this.updateLogStep.setSingle(true);
			this.updateLogStep.setHasEmpty(true);

			this.rejectedLogStep = new LabelSelectMeta(id + ".rejectedLogStep",
					"日志错误行数对应的步骤", null, null, null, getStepName(transMeta
							.getRejectedStep()), null, super.getAllStepNames());
			this.rejectedLogStep.setSingle(true);
			this.rejectedLogStep.setHasEmpty(true);

			this.logConnection = new LabelSelectMeta(id + ".logConnection",
					"日志数据库连接", null, null, null, String.valueOf((transMeta
							.getLogConnection() != null) ? transMeta
							.getLogConnection().getID() : -1), null, super
							.getConnectionLine());
			this.logConnection.setSingle(true);
			this.logConnection.setHasEmpty(true);

			this.logTable = new LabelInputMeta(id + ".logTable", "日志表", null,
					null, null, transMeta.getLogTable(), null, null);
			this.logTable.setSingle(true);

			this.stepPerformanceLogTable = new LabelInputMeta(id
					+ ".stepPerformanceLogTable", "步骤性能日志表", null, null, null,
					transMeta.getStepPerformanceLogTable(), null, null);
			this.stepPerformanceLogTable.setSingle(true);

			this.batchIdUsed = new LabelInputMeta(id + ".batchIdUsed",
					"使用批量ID", null, null, null, String.valueOf(transMeta
							.isBatchIdUsed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.logfieldUsed = new LabelInputMeta(id + ".logfieldUsed",
					"使用日志表记录登录", null, null, null, String.valueOf(transMeta
							.isLogfieldUsed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.meta.putTabContent(2, new BaseFormMeta[] { readLogStep,
					inputLogStep, writeLogStep, outputLogStep, updateLogStep,
					rejectedLogStep, logConnection, logTable,
					stepPerformanceLogTable, batchIdUsed, logfieldUsed });

			/*******************************************************************
			 * 3 日期
			 ******************************************************************/
			this.maxDateConnection = new LabelSelectMeta(
					id + ".maxDateConnection",
					"最大日期数据库连接",
					null,
					null,
					null,
					String
							.valueOf((transMeta.getMaxDateConnection() != null) ? transMeta
									.getMaxDateConnection().getID()
									: -1), null, super.getConnectionLine());
			this.maxDateConnection.setSingle(true);
			this.maxDateConnection.setHasEmpty(true);

			this.maxDateTable = new LabelInputMeta(id + ".maxDateTable",
					"最大日期表", null, null, null, transMeta.getMaxDateTable(),
					null, null);
			this.maxDateTable.setSingle(true);

			this.maxDateField = new LabelInputMeta(id + ".maxDateField",
					"最大日期字段", null, null, null, transMeta.getMaxDateField(),
					null, null);
			this.maxDateField.setSingle(true);

			this.maxDateOffset = new LabelInputMeta(id + ".maxDateOffset",
					"最大日期偏移（秒）", null, null, null, Double.toString(transMeta
							.getMaxDateOffset()), null, null);
			this.maxDateOffset.setSingle(true);

			this.maxDateDifference = new LabelInputMeta(id
					+ ".maxDateDifference", "最大日期差别（秒）", null, null, null,
					Double.toString(transMeta.getMaxDateDifference()), null,
					null);
			this.maxDateDifference.setSingle(true);

			this.meta.putTabContent(3, new BaseFormMeta[] { maxDateConnection,
					maxDateTable, maxDateField, maxDateOffset,
					maxDateDifference });

			/*******************************************************************
			 * 4 依赖
			 ******************************************************************/
			this.dependencies = new LabelGridMeta(id + "_dependencies", "依赖",
					200);
			this.dependencies.setHasBottomBar(true);
			this.dependencies.setHasAdd(true, true,
					"jQuery.imeta.trans.setting.btn.dependenciesAdd");
			this.dependencies.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");
			this.dependencies.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_dependencies.dependencyId",
							"#", GridHeaderDataMeta.HEADER_TYPE_NUMBER, false,
							50),
					new GridHeaderDataMeta(id + "_dependencies.dbconnection",
							"数据库连接", null, false, 150),
					new GridHeaderDataMeta(id + "_dependencies.dbtable", "表",
							null, false, 150),
					new GridHeaderDataMeta(id + "_dependencies.dbfield", "字段",
							null, false, 150) });

			List<TransDependency> depList = transMeta.getDependencies();
			TransDependency dep;

			for (int i = 0; i < depList.size(); i++) {
				dep = depList.get(i);

				this.dependencies
						.addRow(new Object[] {
								String.valueOf(i + 1),
								new GridCellDataMeta(
										id + "_dependencies.dbconnection",
										String
												.valueOf((dep.getDatabase() != null) ? dep
														.getDatabase().getID()
														: -1),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(id
										+ "_dependencies.dbtable", dep
										.getTablename(),
										GridCellDataMeta.CELL_TYPE_INPUT),
								new GridCellDataMeta(id
										+ "_dependencies.dbfield", dep
										.getFieldname(),
										GridCellDataMeta.CELL_TYPE_INPUT) });
			}

			this.dependencies.setSingle(true);
			this.meta.putTabContent(4, new BaseFormMeta[] { dependencies });

			/*******************************************************************
			 * 5 杂项
			 ******************************************************************/
			this.sizeRowset = new LabelInputMeta(id + ".sizeRowset",
					"结果集中的记录数", null, null, null, String.valueOf(transMeta
							.getSizeRowset()), null, null);
			this.sizeRowset.setSingle(true);

			this.feedbackShown = new LabelInputMeta(id + ".feedbackShown",
					"转换时是否在日志中记录反馈", null, null, null, String.valueOf(transMeta
							.isFeedbackShown()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.feedbackSize = new LabelInputMeta(id + ".feedbackSize",
					"结果集中的记录数", null, null, null, String.valueOf(transMeta
							.getFeedbackSize()), null, null);
			this.feedbackSize.setSingle(true);

			this.usingUniqueConnections = new LabelInputMeta(id
					+ ".usingUniqueConnections", "使用唯一连接", null, null, null,
					String.valueOf(transMeta.isUsingUniqueConnections()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.sharedObjectsFile = new LabelInputMeta(id
					+ ".sharedObjectsFile", "共享对象文件", null, null, null,
					transMeta.getSharedObjectsFile(), null, null);
			this.sharedObjectsFile.setSingle(true);

			this.usingThreadPriorityManagment = new LabelInputMeta(id
					+ ".usingThreadPriorityManagment", "管理线程优先级", null, null,
					null, String.valueOf(transMeta
							.isUsingThreadPriorityManagment()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.meta.putTabContent(5, new BaseFormMeta[] { sizeRowset,
					feedbackShown, feedbackSize, usingUniqueConnections,
					sharedObjectsFile, usingThreadPriorityManagment });

			/*******************************************************************
			 * 7 监控
			 ******************************************************************/
			this.capturingStepPerformanceSnapShots = new LabelInputMeta(id
					+ ".capturingStepPerformanceSnapShots", "开启步骤性能监控", null,
					null, null, String.valueOf(transMeta
							.isCapturingStepPerformanceSnapShots()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.stepPerformanceCapturingDelay = new LabelInputMeta(id
					+ ".stepPerformanceCapturingDelay", "步骤性能测量间隔（毫秒）", null,
					null, null, String.valueOf(transMeta
							.getStepPerformanceCapturingDelay()), null, null);
			this.stepPerformanceCapturingDelay.setSingle(true);

			this.meta.putTabContent(7, new BaseFormMeta[] {
					capturingStepPerformanceSnapShots,
					stepPerformanceCapturingDelay });

			// 装载到form
			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { meta });

			this.ok = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
			this.ok.putProperty("winId", winId);
			this.ok.putProperty("roName", transMeta.getName());
			this.ok
					.putProperty("directoryId", transMeta.getDirectory()
							.getID());
			this.ok.addClick("jQuery.imeta.trans.setting.btn.ok");
			this.cancel = new ButtonMeta(id + ".btn.cancel",
					id + ".btn.cancel", "取消", "取消");
			this.cancel.putProperty("winId", winId);

			this.cancel.addClick("jQuery.imeta.trans.setting.btn.cancel");
			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] { ok,
					cancel });

			cArr.add(this.columnFormMeta.getFormJo());

			rtn.put("items", cArr);

			rtn.put("title", transMeta.getName() + "设置");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
