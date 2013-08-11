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
import com.panet.imeta.job.BaseJobDialog;
import com.panet.imeta.job.JobDialogInterface;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class JobSettingDialog extends BaseJobDialog implements
		JobDialogInterface {

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

	private LabelSelectMeta jobstatus;

	private LabelInputMeta jobversion;

	private ButtonMeta dicSelecter;

	private LabelInputMeta directory;

	private LabelInputMeta createdUser;

	private LabelInputMeta createdDate;

	private LabelInputMeta modifiedUser;

	private LabelInputMeta modifiedDate;

	private LabelSelectMeta logConnection;

	private LabelInputMeta logTable;

	private LabelGridMeta parameters;

	private LabelInputMeta stepPerformanceLogTable;

	private LabelInputMeta batchIdUsed;
	
	private LabelInputMeta batchIdPassed;

	private LabelInputMeta logfieldUsed;

	private LabelInputMeta sharedObjectsFile;

	private ButtonMeta ok;

	private ButtonMeta cancel;

	/**
	 * 初始化
	 * 
	 * @param JobMeta
	 * @param winId
	 */
	public JobSettingDialog(JobMeta JobMeta, String winId) {
		super(JobMeta);
		this.winId = winId;
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobMeta jobMeta = super.getJobMeta();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到tab
			this.meta = new MenuTabMeta(id, new String[] { "作业", "参数", "日志" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 0 作业
			 ******************************************************************/
			this.name = new LabelInputMeta(id + ".name", "作业名称", null, null,
					null, jobMeta.getName(), null, ValidateForm.getInstance()
							.setRequired(true));
			this.name.setSingle(true);

			this.description = new LabelInputMeta(id + ".description", "描述",
					null, null, null, jobMeta.getDescription(), null, null);
			this.description.setSingle(true);

			this.extendedDescription = new LabelTextareaMeta(id
					+ ".extendedDescription", "扩展描述", null, null, null, jobMeta
					.getExtendedDescription(), 5, null);
			this.extendedDescription.setSingle(true);

			// 得到状态列表
			List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
			for (int i = 0; i < JobMeta.jobstatusList.length; i++) {
				options.add(new OptionDataMeta(String.valueOf(i),
						JobMeta.jobstatusList[i]));
			}
			this.jobstatus = new LabelSelectMeta(id + ".jobstatus", "状态", null,
					null, null, String.valueOf(jobMeta.getJobstatus()), null,
					options);

			this.jobversion = new LabelInputMeta(id + ".jobversion", "版本",
					null, null, null, jobMeta.getJobversion(), null, null);

			this.dicSelecter = new ButtonMeta(id + ".btn.dicSelecter", id
					+ ".btn.dicSelecter", "选择", "选择");
			this.dicSelecter.addClick("jQuery.imeta.job.setting.btn.pathBtn");
			this.directory = new LabelInputMeta(id + ".directory", "目录", null,
					null, null, jobMeta.getDirectory().toString(), null, null);
			this.directory.addButton(dicSelecter);
			this.directory.setSingle(true);
			this.directory.setReadonly(true);

			this.createdUser = new LabelInputMeta(id + ".createdUser", "创建者",
					null, null, null, jobMeta.getCreatedUser(), null, null);
			this.createdUser.setDisabled(true);

			this.createdDate = new LabelInputMeta(id + ".createdDate", "创建日期",
					null, null, null, DateFormatUtils.format(jobMeta
							.getCreatedDate(),
							Const.GENERALIZED_DATE_TIME_SHOW_FORMAT), null,
					null);
			this.createdDate.setDisabled(true);

			this.modifiedUser = new LabelInputMeta(id + ".modifiedUser",
					"最近修改的用户", null, null, null, jobMeta.getModifiedUser(),
					null, null);
			this.modifiedUser.setDisabled(true);

			this.modifiedDate = new LabelInputMeta(id + ".modifiedDate",
					"最近修改的日期", null, null, null, DateFormatUtils.format(jobMeta
							.getModifiedDate(),
							Const.GENERALIZED_DATE_TIME_SHOW_FORMAT), null,
					null);
			this.modifiedDate.setDisabled(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { name, description,
					extendedDescription, jobstatus, jobversion, directory,
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

			NamedParamsDefault namedParams = (NamedParamsDefault) jobMeta
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
			this.logConnection = new LabelSelectMeta(id + ".logConnection",
					"日志数据库连接", null, null, null, String.valueOf((jobMeta
							.getLogConnection() != null) ? jobMeta
							.getLogConnection().getID() : -1), null, super
							.getConnectionLine());
			this.logConnection.setSingle(true);
			this.logConnection.setHasEmpty(true);

			this.logTable = new LabelInputMeta(id + ".logTable", "日志表", null,
					null, null, jobMeta.getLogTable(), null, null);
			this.logTable.setSingle(true);

			this.batchIdUsed = new LabelInputMeta(id + ".batchIdUsed",
					"继续作业批次ID", null, null, null, String.valueOf(jobMeta
							.isBatchIdUsed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.batchIdPassed = new LabelInputMeta(id + ".batchIdPassed",
					"向下传递批次ID", null, null, null, String.valueOf(jobMeta
							.isBatchIdPassed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.logfieldUsed = new LabelInputMeta(id + ".logfieldUsed",
					"记录详细日志(调试阶段)", null, null, "注意：对于长时间执行的作业有内存溢出风险，所以仅限调试使用", String.valueOf(jobMeta
							.isLogfieldUsed()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.sharedObjectsFile = new LabelInputMeta(id
					+ ".sharedObjectsFile", "共享对象文件", null, null, null, jobMeta
					.getSharedObjectsFile(), null, null);
			this.sharedObjectsFile.setSingle(true);

			this.meta.putTabContent(2, new BaseFormMeta[] { logConnection,
					logTable, stepPerformanceLogTable, batchIdUsed,
					batchIdPassed, logfieldUsed, sharedObjectsFile });

			// 装载到form
			this.columnFormMeta
					.putFieldsetsContent(new BaseFormMeta[] { meta });

			this.ok = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
			this.ok.putProperty("winId", winId);
			this.ok.putProperty("roName", jobMeta.getName());
			this.ok.putProperty("directoryId", jobMeta.getDirectory().getID());
			this.ok.addClick("jQuery.imeta.job.setting.btn.ok");
			this.cancel = new ButtonMeta(id + ".btn.cancel",
					id + ".btn.cancel", "取消", "取消");
			this.cancel.putProperty("winId", winId);
			this.cancel.addClick("jQuery.imeta.job.setting.btn.cancel");
			this.columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] { ok,
					cancel });

			cArr.add(this.columnFormMeta.getFormJo());

			rtn.put("items", cArr);

			rtn.put("title", jobMeta.getName() + "设置");

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
