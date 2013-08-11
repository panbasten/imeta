package com.panet.imeta.job.entries.msaccessbulkload;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnFieldset.ColumnFieldsetMeta;
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

public class JobEntryMSAccessBulkLoadDialog extends JobEntryDialog implements
JobEntryDialogInterface{
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;

	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;

	//General 
	/**
	 * Data source
	 */
	private ColumnFieldsetMeta dataSourceColumn;

	/**
	 * Copy previous result to args
	 */
	private LabelInputMeta is_args_from_previous;

	/**
	 * Source file/folder name
	 */
//	private LabelInputMeta sourceFilefolderName;
//	private ButtonMeta fileBtn;
//	private ButtonMeta folderBtn;

	/**
	 * include subfolders
	 */
	private LabelInputMeta include_subfolders;

	/**
	 * Wildcard
//	 */
//	private LabelInputMeta wildcard;
//	/**
//	 * Delimiter
//	 */
//	private LabelInputMeta delimiter;

	/**
	 * Target MS Access Db
	 */
//	private ColumnFieldsetMeta targetMSAccessDbColumn;

//	/**
//	 * Target Db name
//	 */
//	private LabelInputMeta targetDbName;
//	private ButtonMeta browseDbBtn;

	/**
	 * Tablename
	 */
//	private LabelInputMeta tablename;

	/**
	 * Add to the grid
	 */
	private ButtonMeta addGridBtn;

	/**
	 * Fields
	 */
	private LabelGridMeta fieldsGrid;

	//Advanced 
	/**
	 * Success on
	 */
	private ColumnFieldsetMeta successOnColumn;
	/**
	 * Success condition
	 */
	private LabelSelectMeta success_condition;
	/**
	 * Limit
	 */
	private LabelInputMeta limit;

	/**
	 * Result filenames
	 */
	private ColumnFieldsetMeta  resultFilenamesColumn;
	/**
	 * Add filename to result
	 */
	private LabelInputMeta add_result_filenames;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryMSAccessBulkLoadDialog(JobMeta jobMeta,
			JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryMSAccessBulkLoad entry = (JobEntryMSAccessBulkLoad) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "同用", "高级" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Data source
			this.dataSourceColumn = new ColumnFieldsetMeta(null, "数据源");
			this.dataSourceColumn.setSingle(true);

			// Copy previous result to args
			this.is_args_from_previous = new LabelInputMeta(id + ".is_args_from_previous",
					"复制先前的结果参数",null, null, null, String.valueOf(entry.isArgsFromPrevious()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));

//			// Source file/folder name
//			this.sourceFilefolderName = new LabelInputMeta(id + ".sourceFilefolderName",
//					"源文件/文件夹名称",null, null, null, String.valueOf(entry.source_filefolder), 
//					null, ValidateForm.getInstance().setRequired(true));
//			this.fileBtn = new ButtonMeta(id
//					+ ".btn.fileBtn", id + ".btn.fileBtn",
//					"文件", "文件");
//			this.folderBtn = new ButtonMeta(id
//					+ ".btn.folderBtn", id + ".btn.folderBtn",
//					"文件夹", "文件夹");
//			this.sourceFilefolderName.addButton(new ButtonMeta[]{this.fileBtn,
//					this.folderBtn});
//			this.sourceFilefolderName.setSingle(true);

			// Include subfolders
			this.include_subfolders = new LabelInputMeta(id + ".include_subfolders",
					"包含子文件夹",null, null, null,  String.valueOf(entry.isIncludeSubFoders()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
//
//			// Wildcard
//			this.wildcard = new LabelInputMeta(id + ".Wildcard","通配符",null, null,
//					null,  String.valueOf(entry.source_wildcard), null, ValidateForm
//					.getInstance().setRequired(true));
//			this.wildcard.setSingle(true);
//
//			// Delimiter
//			this.delimiter = new LabelInputMeta(id + ".delimiter","分隔符",null,
//					null,null,  String.valueOf(entry.delimiter), null, ValidateForm.getInstance().setRequired(true));
//			this.delimiter.setSingle(true);
			//装到框体dataSourceColumn
//			this.dataSourceColumn.putFieldsetsContent(new BaseFormMeta[] {
//					this.copyPrevious,this.sourceFilefolderName,this.includeSubfolders,
//					this.wildcard,this.delimiter
//			});
			this.dataSourceColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.is_args_from_previous,this.include_subfolders
			});
			
			// Target MS Access Db
//			this.targetMSAccessDbColumn = new ColumnFieldsetMeta(null, "目标的MS Access数据库");
//			this.targetMSAccessDbColumn.setSingle(true);
//
//			// Target Db name
//			this.targetDbName = new LabelInputMeta(id + ".targetDbName","目标数据库名称",
//					null,null,null,  String.valueOf(entry.target_Db), null, ValidateForm.getInstance()
//					.setRequired(true));
//			this.targetDbName.setSingle(true);
//			this.browseDbBtn = new ButtonMeta(id
//					+ ".btn.browseDbBtn", id + ".btn.browseDbBtn",
//					"浏览", "浏览");
//			this.targetDbName.addButton(new ButtonMeta[]{this.browseDbBtn});
//
//			// Table name
//			this.tablename = new LabelInputMeta(id + ".tablename","表名称",
//					null,null,null,  String.valueOf(entry.target_table), null, ValidateForm.getInstance()
//					.setRequired(true));
//			this.tablename.setSingle(true);

			//装到框体Target MS Access Db
//			this.targetMSAccessDbColumn.putFieldsetsContent(new BaseFormMeta[] {
//					this.targetDbName ,this.tablename
//			});
//			this.targetMSAccessDbColumn.putFieldsetsContent(new BaseFormMeta[] {
//			});

			// Add to the grid
//			this.addGridBtn = new ButtonMeta(id
//					+ ".btn.addGridBtn", id + ".btn.addGridBtn",
//					" 添加到网格 ", " 添加到网格 ");
//			this.addGridBtn.setSingle(true);

			// Fields
			this.fieldsGrid = new LabelGridMeta(id + "_fieldsGrid","字段", 150);
			this.fieldsGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fieldsGrid.fieldId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							new GridHeaderDataMeta(id + "_fieldsGrid.source_filefolder", "源文件/文件夹", null, false, 150),
							new GridHeaderDataMeta(id + "_fieldsGrid.source_wildcard", "通配符", null, false, 80),
							new GridHeaderDataMeta(id + "_fieldsGrid.delimiter", "分隔符", null, false, 80),
							new GridHeaderDataMeta(id + "_fieldsGrid.target_Db", "对象访问数据库", null, false, 100),
							new GridHeaderDataMeta(id + "_fieldsGrid.target_table", "目标表", null, false, 80),
			}) ;
			this.fieldsGrid.setSingle(true);
			this.fieldsGrid.setHasBottomBar(true);
			this.fieldsGrid.setHasAdd(true, true, 
			"jQuery.imeta.jobEntries.jobentryMSaccessbulkload.btn.fieldAdd");
			this.fieldsGrid.setHasDelete(true, true, 
			"jQuery.imeta.parameter.fieldsDelete");
			
			String[] source_filefolder = entry.source_filefolder;
			if(source_filefolder != null && source_filefolder.length > 0){
				for(int i = 0; i < source_filefolder.length; i++){
					this.fieldsGrid.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,entry.source_filefolder[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.source_filefolder[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.delimiter[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.target_Db[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.target_table[i],
									GridCellDataMeta.CELL_TYPE_INPUT)
					});	
				}
			}

			// 装载到页签
//			this.meta.putTabContent(0, new BaseFormMeta[] {this.dataSourceColumn,
//					this.targetMSAccessDbColumn ,this.addGridBtn ,this.fieldsGrid
//			});
			this.meta.putTabContent(0, new BaseFormMeta[] {this.dataSourceColumn,
					this.addGridBtn ,this.fieldsGrid
			});
			/*******************************************************************
			 * 标签1---------Advanced
			 ******************************************************************/
			// Success on
			this.successOnColumn = new ColumnFieldsetMeta(null, "成功");
			this.successOnColumn.setSingle(true);
			// Success condition
			List<OptionDataMeta> success_conditionOptions = new ArrayList<OptionDataMeta>();
			success_conditionOptions.add(new OptionDataMeta(entry.SUCCESS_IF_NO_ERRORS, Messages.getString("JobEntryMSAccessBulkLoad.SuccessWhenAllWorksFine.Label")));
			success_conditionOptions.add(new OptionDataMeta(entry.SUCCESS_IF_AT_LEAST, Messages
					.getString("JobEntryMSAccessBulkLoad.SuccessWhenAtLeat.Label")));
			success_conditionOptions.add(new OptionDataMeta(entry.SUCCESS_IF_ERRORS_LESS, Messages
					.getString("JobEntryMSAccessBulkLoad.SuccessWhenErrorsLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id + ".success_condition",
					"成功的条件", null, null, "成功的条件", entry.getSuccessCondition(), null, success_conditionOptions);
			this.success_condition.setSingle(true);
			this.success_condition.addListener("change",
					"jQuery.imeta.jobEntries.jobentryMSaccessbulkload.listeners.successOnChange");
			// Limit
			this.limit = new LabelInputMeta(id + ".limit","限制",null, null, null, 
					String.valueOf(entry.getLimit()), null, ValidateForm
					.getInstance().setRequired(true));
			this.limit.setSingle(true);
			this.limit.setDisabled(entry.getSuccessCondition().equals(entry.SUCCESS_IF_NO_ERRORS));

			this.successOnColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.success_condition,this.limit});

			// Result filenames
			this.resultFilenamesColumn = new ColumnFieldsetMeta(null, "结果文件名");
			this.resultFilenamesColumn.setSingle(true);
			// Add filename to result
			this.add_result_filenames = new LabelInputMeta(id + ".add_result_filenames",
					"添加文件名",null, null, null, String
					.valueOf(entry.isAddResultFilename()), InputDataMeta.
					INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(true));
			this.resultFilenamesColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.add_result_filenames
			});

			// 装载到页签
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.successOnColumn ,this.resultFilenamesColumn});

			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta});

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});

			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		}catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}

}
