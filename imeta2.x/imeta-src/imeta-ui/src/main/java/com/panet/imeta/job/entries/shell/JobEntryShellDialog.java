package com.panet.imeta.job.entries.shell;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryShellDialog extends JobEntryDialog implements
		JobEntryDialogInterface {

	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 作业项名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * insert script
	 */

	private LabelInputMeta insertScript;

	/**
	 * 脚本文件名
	 */

	private LabelInputMeta filename;

	/**
	 * 工作路径
	 */

	private LabelInputMeta workDirectory;

	/**
	 * 日志设置
	 */
	private ColumnFieldsetMeta setLog;

	/**
	 *  指定日志文件
	 */

	private LabelInputMeta setLogfile;

	/**
	 *  追加日志文件
	 */

	private LabelInputMeta setAppendLogfile;

	/**
	 *  日志文件名称
	 */

	private LabelInputMeta logfile;

	/**
	 *  日志文件扩展名称
	 */

	private LabelInputMeta logext;

	/**
	 *  日志文件中包含日期
	 */

	private LabelInputMeta addDate;

	/**
	 *  日志文件中包含时间
	 */

	private LabelInputMeta addTime;

	/**
	 * 日志级别
	 */
	private LabelSelectMeta loglevel;

	/**
	 *  将上一结果作为参数
	 */

	private LabelInputMeta argFromPrevious;
	

	/**
	 *  对每个输入执行一次
	 */

	private LabelInputMeta execPerRow;

	/**
	 * 字段
	 */
	private LabelGridMeta words;
	
	/**
	 * 脚本
	 */
	private LabelTextareaMeta script;
	

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryShellDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			JobEntryShell jobEntry = (JobEntryShell) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "作业项名称", null, null,
					"作业项名称", super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id,
					new String[] { "通用", "脚本" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0--General
			 ******************************************************************/

			//insert script
			this.insertScript = new LabelInputMeta(id + ".insertScript",
					"输入脚本", null, null, null, String
							.valueOf(jobEntry.insertScript),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.insertScript.setSingle(true);

			// 脚本文件名
			this.filename = new LabelInputMeta(id + ".filename",
					"脚本文件名", null, null, "脚本文件名", jobEntry.getFilename(), null,
					ValidateForm.getInstance().setRequired(false));
			
			
			this.filename.setSingle(true);

			// 工作路径
			this.workDirectory = new LabelInputMeta(id + ".workDirectory", "工作路径", null,
					null, "工作路径", jobEntry.getWorkDirectory(), null,
					ValidateForm.getInstance().setRequired(false));
			this.workDirectory.setSingle(true);

			// 设置日志
			this.setLog = new ColumnFieldsetMeta(null, "设置日志");
			this.setLog.setSingle(true);

			//指定日志文件
			this.setLogfile = new LabelInputMeta(id + ".setLogfile",
					"指定日志文件", null, null, null, String.valueOf(jobEntry
							.setLogfile), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.setLogfile.addClick("jQuery.imeta.jobEntries.shell.listeners.setLogfile");
			this.setLogfile.setSingle(true);

			//追加日志文件
			this.setAppendLogfile = new LabelInputMeta(id + ".setAppendLogfile", "追加日志文件", null,
					null, null, 
					String.valueOf(jobEntry.setAppendLogfile), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.setAppendLogfile.setDisabled(jobEntry.isDummy());
			this.setAppendLogfile.setSingle(true);

			// 日志文件名称
			this.logfile = new LabelInputMeta(id + ".logfile", "日志文件名称",
					null, null, "日志文件名称",
					jobEntry.getLogfile(), null,
					ValidateForm.getInstance().setRequired(false));
			this.logfile.setDisabled(jobEntry.isDummy());
			this.logfile.setSingle(true);

			// 日志文件扩展名
			this.logext = new LabelInputMeta(id + ".logext", "日志文件扩展名",
					null, null, "日志文件扩展名",jobEntry.logext, null,
					ValidateForm.getInstance().setRequired(false));
			this.logext.setDisabled(jobEntry.isDummy());
			this.logext.setSingle(true);

			//日志文件中包含日期
			this.addDate = new LabelInputMeta(id + ".addDate",
					"日志文件中包含日期", null, null, null, 
					String.valueOf(jobEntry.addDate),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addDate.setDisabled(jobEntry.isDummy());
			this.addDate.setSingle(true);

			//日志文件中包含时间
			this.addTime = new LabelInputMeta(id + ".addTime",
					"日志文件中包含时间", null, null, null, 
					String.valueOf(jobEntry.addTime),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.addTime.setDisabled(jobEntry.isDummy());
			this.addTime.setSingle(true);

			//日志级别
			this.loglevel = new LabelSelectMeta(
					id + ".loglevel", 
					"日志级别：", null,
					null, null, String.valueOf(jobEntry.loglevel), null, super.getLoglevel());
			this.loglevel.setDisabled(jobEntry.isDummy());
			this.loglevel.setSingle(true);

			this.setLog.putFieldsetsContent(new BaseFormMeta[] {
					this.setLogfile,
					this.setAppendLogfile, //内容
					this.logfile, this.logext, this.addDate,
					this.addTime, this.loglevel });

			//将商议结果作为参数
			this.argFromPrevious = new LabelInputMeta(
					id+ ".argFromPrevious", 
					"将上一结果作为参数",
					null, null, null,String.valueOf(jobEntry.argFromPrevious), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.argFromPrevious.setSingle(true);

			//对每个输入执行一次  
			this.execPerRow = new LabelInputMeta(id + ".execPerRow", "对每个输入执行一次",
					null, null, null, 
					String.valueOf(jobEntry.execPerRow), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.execPerRow.setSingle(true);

			// 字段
			this.words = new LabelGridMeta(id+"_words", "字段", 200);
			this.words.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_words.fieldId", "#", null, false, 50),
					new GridHeaderDataMeta(id + "_words.arguments", "参数", null, false, 100)
					});
			
			String[] logfile = jobEntry.getArguments();
			if(logfile != null && logfile.length > 0){
				for(int i = 0; i < logfile.length; i++){
					this.words.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,jobEntry.getArguments()[i],
									GridCellDataMeta.CELL_TYPE_INPUT)
					});	
				}
			}
			this.words.setHasBottomBar(true);
			this.words.setHasAdd(true, true, 
					"jQuery.imeta.jobEntries.shell.btn.wordsAdd");
			this.words.setHasDelete(true, true, 
					"jQuery.imeta.parameter.fieldsDelete");
			this.words.setSingle(true);

			//页签里面添加内容

			this.meta.putTabContent(0, new BaseFormMeta[] { 
					this.insertScript, this.filename, this.workDirectory,
					this.setLog, this.argFromPrevious, this.execPerRow,
					this.words });
			
			/*******************************************************************
			 * 标签1--script
			 ******************************************************************/
			
			// 注释
			this.script = new LabelTextareaMeta(id + ".script", "脚本", null, null,
					null,  jobEntry.getScript(), 10
					,ValidateForm.getInstance().setRequired(false));
			
			this.script.setSingle(true);

			this.meta.putTabContent(1, new BaseFormMeta[] {this.script});

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });
			// 确定，取消
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
