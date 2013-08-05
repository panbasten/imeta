package com.panet.imeta.job.entries.trans;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryTransDialog extends JobEntryDialog implements
		JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	/**
	 * job名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 转换名称
	 */
	private LabelInputMeta transname;

	private ButtonMeta transnameBtn;

	/**
	 * 数据库路径
	 */
	private LabelInputMeta directory;

	/**
	 * 指定日志文件?
	 */
	private LabelInputMeta setLogfile;

	/**
	 * Append log file
	 */
	private LabelInputMeta setAppendLogfile;

	/**
	 * 日志文件名
	 */
	private LabelInputMeta logfile;

	/**
	 * 日志文件后缀名
	 */
	private LabelInputMeta logext;

	/**
	 * 日志文件包含日期
	 */
	private LabelInputMeta addDate;

	/**
	 * 日志文件包含时间
	 */
	private LabelInputMeta addTime;

	/**
	 * 日志级别
	 */
	private LabelSelectMeta loglevel;

	/**
	 * 复制以前结果到参数？
	 */
	private LabelInputMeta argFromPrevious;

	/**
	 * 复制以前结果到变量？
	 */
	private LabelInputMeta paramsFromPrevious;

	/**
	 * 传递父任务变量到转换中
	 */
	private LabelInputMeta passingAllParameters;

	/**
	 * 执行每一个输入行？
	 */
	private LabelInputMeta execPerRow;

	/**
	 * 在执行前清楚结果行列表？
	 */
	private LabelInputMeta clearResultRows;

	/**
	 * 在执行前清除记过文件列表？
	 */
	private LabelInputMeta clearResultFiles;

	/**
	 * 在集群模式下运行这个转换？
	 */
	private LabelInputMeta clustering;

	/**
	 * Remote slave server
	 */
	private LabelSelectMeta slaveServer;

	/**
	 * Wait for the remote
	 */
	private LabelInputMeta waitingToFinish;

	/**
	 * Follow local abort to remote
	 */
	private LabelInputMeta followingAbortRemotely;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;

	private LabelGridMeta parameters;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public JobEntryTransDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryTrans je = (JobEntryTrans) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到任务项名称
			this.name = new LabelInputMeta(id + ".name", "任务项名称", null, null,
					"任务项名称必须填写", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "基本", "高级", "日志",
					"参数", "变量" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0 基本
			 ******************************************************************/
			// 转换名称
			this.transname = new LabelInputMeta(id + ".transname", "转换名称",
					null, null, "转换名称必须填写", je.getTransname(), null,
					ValidateForm.getInstance().setRequired(true));
			this.transnameBtn = new ButtonMeta(id + ".btn.transnameBtn", id
					+ ".btn.transnameBtn", "转换", "转换");
			this.transnameBtn
					.addClick("jQuery.imeta.jobEntries.trans.btn.transnameBtn");
			this.transname.addButton(new ButtonMeta[] { this.transnameBtn });
			this.transname.setSingle(true);
			this.transname.setReadonly(true);

			// 资源库目录
			this.directory = new LabelInputMeta(id + ".directory", "资源库目录",
					null, null, "资源库目录必须填写", je.getDirectory(), null,
					ValidateForm.getInstance().setRequired(true));
			this.directory.setSingle(true);
			this.directory.setReadonly(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.transname,
					this.directory });

			/*******************************************************************
			 * 标签1 高级
			 ******************************************************************/
			// 复制以前结果到参数？
			this.argFromPrevious = new LabelInputMeta(id + ".argFromPrevious",
					"复制以前结果到参数", null, null, null, String
							.valueOf(je.argFromPrevious),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			this.paramsFromPrevious = new LabelInputMeta(id
					+ ".paramsFromPrevious", "复制以前结果到变量", null, null, null,
					String.valueOf(je.paramsFromPrevious),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 执行每一个输入行？
			this.execPerRow = new LabelInputMeta(id + ".execPerRow",
					"执行每一个输入行", null, null, null,
					String.valueOf(je.execPerRow),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 执行前清除结果行列表
			this.clearResultRows = new LabelInputMeta(id + ".clearResultRows",
					"在执行前清除结果行列表", null, null, null, String
							.valueOf(je.clearResultRows),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 执行前清除接过文件列表
			this.clearResultFiles = new LabelInputMeta(
					id + ".clearResultFiles", "执行前清除接过文件列表", null, null, null,
					String.valueOf(je.clearResultFiles),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);

			// 在集群模式下运行这个转换
			this.clustering = new LabelInputMeta(id + ".clustering",
					"在集群模式下运行这个转换", null, null, null, String.valueOf(je
							.isClustering()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.clustering
					.addClick("jQuery.imeta.jobEntries.trans.listeners.clusterMode");

			// Remote slave server
			this.slaveServer = new LabelSelectMeta(id + ".slaveServer",
					"远程从服务器", null, null, null, je.getRemoteSlaveServerName(),
					null, super.getSlaveServer());
			this.slaveServer.setSingle(true);
			this.slaveServer.setDisabled(je.isClustering());

			// Wait for the remote
			this.waitingToFinish = new LabelInputMeta(id + ".waitingToFinish",
					"等待远程转换完成", null, null, null, String
							.valueOf(je.waitingToFinish),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.waitingToFinish.setDisabled(je.isClustering());

			// Follow local abort to remote
			this.followingAbortRemotely = new LabelInputMeta(id
					+ ".followingAbortRemotely", "随着本地中止结束远程转换", null, null,
					null, String.valueOf(je.followingAbortRemotely),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.followingAbortRemotely.setDisabled(je.isClustering());

			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.argFromPrevious, this.paramsFromPrevious,
					this.execPerRow, this.clearResultRows,
					this.clearResultFiles, this.clustering, this.slaveServer,
					this.waitingToFinish, this.followingAbortRemotely });

			/*******************************************************************
			 * 标签2 日志
			 ******************************************************************/
			// 指定日志文件
			this.setLogfile = new LabelInputMeta(id + ".setLogfile", "指定日志文件",
					null, null, null, String.valueOf(je.setLogfile),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.setLogfile
					.addClick("jQuery.imeta.jobEntries.trans.listeners.setLogfile");

			// 追加日志文件
			this.setAppendLogfile = new LabelInputMeta(
					id + ".setAppendLogfile", "追加日志文件", null, null, null,
					String.valueOf(je.setAppendLogfile),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.setAppendLogfile.setDisabled(!je.setLogfile);

			// 日志文件名
			this.logfile = new LabelInputMeta(id + ".logfile", "日志文件名", null,
					null, "日志文件名必须填写", je.logfile, null, ValidateForm
							.getInstance().setRequired(true));
			this.logfile.setSingle(true);
			this.logfile.setDisabled(!je.setLogfile);

			// 日志文件名扩展名
			this.logext = new LabelInputMeta(id + ".logext", "日志文件名扩展名", null,
					null, "日志文件名扩展名必须填写", je.logext, null, ValidateForm
							.getInstance().setRequired(true));
			this.logext.setSingle(true);
			this.logext.setDisabled(!je.setLogfile);

			// 日志文件包含日期
			this.addDate = new LabelInputMeta(id + ".addDate", "日志文件包含日期",
					null, null, null, String.valueOf(je.addDate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.addDate.setDisabled(!je.setLogfile);

			// 日志文件包含时间
			this.addTime = new LabelInputMeta(id + ".addTime", "日志文件包含时间",
					null, null, null, String.valueOf(je.addTime),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.addTime.setDisabled(!je.setLogfile);

			// 日志级别
			this.loglevel = new LabelSelectMeta(id + ".loglevel", "日志级别", null,
					null, null, String.valueOf(je.loglevel), null, super
							.getLoglevel());
			this.loglevel.setSingle(true);
			this.loglevel.setDisabled(!je.setLogfile);

			this.meta.putTabContent(2, new BaseFormMeta[] { this.setLogfile,
					this.setAppendLogfile, this.logfile, this.logext,
					this.addDate, this.addTime, this.loglevel });

			/*******************************************************************
			 * 标签3 字段
			 ******************************************************************/
			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 100);
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.arguments", "参数",
							null, false, 300) });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.jobEntries.trans.btn.fieldAdd");
			this.fields.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] values = je.arguments;
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, je.arguments[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			this.meta.putTabContent(3, new BaseFormMeta[] { this.fields });

			/*******************************************************************
			 * 标签4 变量
			 ******************************************************************/
			// 传递父任务变量到转换中
			this.passingAllParameters = new LabelInputMeta(id
					+ ".passingAllParameters", "传递父任务变量到转换中", null, null, null,
					String.valueOf(je.passingAllParameters),
					InputDataMeta.INPUT_TYPE_CHECKBOX, null);
			this.passingAllParameters.setSingle(true);
			// 变量
			this.parameters = new LabelGridMeta(id + "_parameters", "变量", 100);
			this.parameters.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_parameters.parameterId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_parameters.parameterNames",
							"变量", null, false, 150),
					new GridHeaderDataMeta(id + "_parameters.valuesFromResult",
							"值(来自结果)", null, false, 150),
					new GridHeaderDataMeta(id + "_parameters.values", "值",
							null, false, 150) });
			this.parameters.setSingle(true);
			this.parameters.setHasBottomBar(true);
			this.parameters.setHasAdd(true, true,
					"jQuery.imeta.jobEntries.trans.btn.parameterAdd");
			this.parameters.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] parameterNames = je.parameters;
			String[] parameterValuesFromResult = je.parameterFieldNames;
			String[] parameterValues = je.parameterValues;
			if (parameterNames != null && parameterNames.length > 0)
				for (int i = 0; i < parameterNames.length; i++) {

					this.parameters.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, parameterNames[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,
									parameterValuesFromResult[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, parameterValues[i],
									GridCellDataMeta.CELL_TYPE_INPUT) });
				}
			this.meta.putTabContent(4, new BaseFormMeta[] {
					this.passingAllParameters, this.parameters });

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn() });

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
