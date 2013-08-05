package com.panet.imeta.job.entries.xmlwellformed;

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

public class JobEntryXMLWellFormedDialog extends JobEntryDialog implements
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
	 * 任务实体名
	 */
	private LabelInputMeta name;
	
	//General ------------------------------------------------------------------
	/**
	 * Settings
	 */
	private ColumnFieldsetMeta settingColumn;
	/**
	 * Include subfolders
	 */
	private LabelInputMeta include_subfolders;
	/**
	 * Copy previous result to args
	 */
	private LabelInputMeta arg_from_previous;
	
	/**
	 * File/Folder source
	 */
//	private LabelInputMeta fileFolderSource;
//	private ButtonMeta addBtn;
//	private ButtonMeta fileBtn;
//	private ButtonMeta folderBtn;
	
	/**
	 * Wild card
	 */
//	private LabelInputMeta wildcard;
	
	/**
	 * Files/Folders
	 */
	private LabelGridMeta filesFoldersGrid;
	
	//Advanced -----------------------------------------------------------------
	/**
	 * Success On
	 */
	private ColumnFieldsetMeta successOnColumn;
	/**
	 * Success condition
	 */
	private LabelSelectMeta success_condition;
	/**
	 * Nr files
	 */
	private LabelInputMeta nr_errors_less_than;
	
	/**
	 * Result files name
	 */
	private ColumnFieldsetMeta resultNameColumn;
	/**
	 * Add Filenames
	 */
	private LabelSelectMeta resultfilenames;
	
	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryXMLWellFormedDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryXMLWellFormed entry = (JobEntryXMLWellFormed) super.getJobEntry();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 任务实体名
			this.name = new LabelInputMeta(id + ".name", "任务实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);
			
			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "通用", "高级" });
			this.meta.setSingle(true);
			
			/*******************************************************************
			 * 标签0---------General
			 ******************************************************************/
			// Settings
			this.settingColumn = new ColumnFieldsetMeta(null, "设置");
			this.settingColumn.setSingle(true);
			// Include subfolders
			this.include_subfolders = new LabelInputMeta(id + ".include_subfolders",
					"包含子文件夹",null, null, null, String.valueOf(entry.include_subfolders), 
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(true));
			// Copy previous result to args
			this.arg_from_previous = new LabelInputMeta(id + ".arg_from_previous",
					"复制先前的结果参数",null, null, null, String.valueOf(entry.arg_from_previous), InputDataMeta.
					INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(true));
			this.settingColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.include_subfolders , this.arg_from_previous});
			
			// File/Folder source
//			this.fileFolderSource = new LabelInputMeta(id + ".fileFolderSource",
//					"文件/文件夹源",null, null, null, String.valueOf(entry.source_filefolder),
//					null, ValidateForm.getInstance().setRequired(true));
//			this.addBtn = new ButtonMeta(id
//					+ ".btn.addBtn", id + ".btn.addBtn",
//					"增加", "增加");
//			this.fileBtn = new ButtonMeta(id
//					+ ".btn.fileBtn", id + ".btn.fileBtn",
//					"文件", "文件");
//			this.folderBtn = new ButtonMeta(id
//					+ ".btn.folderBtn", id + ".btn.folderBtn",
//					"文件夹", "文件夹");
//			this.fileFolderSource.addButton(new ButtonMeta[]{this.addBtn,
//					this.fileBtn,this.folderBtn});
//			this.fileFolderSource.setSingle(true);
			// Wild card
//			this.wildcard = new LabelInputMeta(id + ".wildcard",
//					"通配符",null, null, null, String.valueOf(entry.wildcard), 
//					null, ValidateForm.getInstance().setRequired(true));
//			this.wildcard.setSingle(true);
			// Files/Folders
			this.filesFoldersGrid = new LabelGridMeta(id + "_filesFoldersGrid",
					"文件/文件夹", 150);
			this.filesFoldersGrid.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_filesFoldersGrid.fieldId", "序号",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_filesFoldersGrid.source_filefolder", "文件/文件夹", null, false, 200),
					new GridHeaderDataMeta(id + "_filesFoldersGrid.wildcard", "通配符", null, false, 200) }) ;
			this.filesFoldersGrid.setSingle(true);
			this.filesFoldersGrid.setHasBottomBar(true);
			this.filesFoldersGrid.setHasAdd(true, true, 
					"jQuery.imeta.jobEntries.jobentryXMLwellformed.btn.fieldAdd");
			this.filesFoldersGrid.setHasDelete(true, true, 
					"jQuery.imeta.parameter.fieldsDelete");
			
			String[] source_filefolder = entry.source_filefolder;
			if(source_filefolder != null && source_filefolder.length > 0){
				for(int i = 0; i < source_filefolder.length; i++){
					this.filesFoldersGrid.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,entry.source_filefolder[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,entry.wildcard[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
					});	
				}
			}
			
			// 装载到页签
//			this.meta.putTabContent(0, new BaseFormMeta[] {this.settingColumn,
//					this.fileFolderSource,this.wildcard,this.filesFoldersGrid});
			this.meta.putTabContent(0, new BaseFormMeta[] {this.settingColumn,
					this.filesFoldersGrid});
			/*******************************************************************
			 * 标签1---------Advanced
			 ******************************************************************/
			// Success On
			this.successOnColumn = new ColumnFieldsetMeta(null, "成功");
			this.successOnColumn.setSingle(true);
			// Success condition
			List<OptionDataMeta> successOptions = new ArrayList<OptionDataMeta>();
			successOptions.add(new OptionDataMeta(entry.SUCCESS_IF_NO_ERRORS,
					Messages
							.getString("JobXMLWellFormed.SuccessWhenAllWorksFine.Label")));
			successOptions.add(new OptionDataMeta(entry.SUCCESS_IF_AT_LEAST_X_FILES_WELL_FORMED,
					Messages
							.getString("JobXMLWellFormed.SuccessWhenAtLeat.Label")));
			successOptions.add(new OptionDataMeta(entry.SUCCESS_IF_BAD_FORMED_FILES_LESS,
					Messages
							.getString("JobXMLWellFormed.SuccessWhenBadFormedLessThan.Label")));
			this.success_condition = new LabelSelectMeta(id + ".success_condition",
					"成功的条件", null, null, null, entry.getSuccessCondition(),
					null, successOptions);
			this.success_condition.addListener("change", 
					"jQuery.imeta.jobEntries.jobentryXMLwellformed.listeners.successOnChange");
			this.success_condition.setSingle(true);
			// Nr files
			this.nr_errors_less_than =  new LabelInputMeta(id + ".nr_errors_less_than",
					"无记录的文件",null, null, null, entry.getNrErrorsLessThan(), null, null);
			this.nr_errors_less_than
					.setDisabled(entry.getSuccessCondition().equals(entry.SUCCESS_IF_NO_ERRORS));
			this.nr_errors_less_than.setSingle(true);
			this.successOnColumn.putFieldsetsContent( new BaseFormMeta[] {
					this.success_condition,this.nr_errors_less_than});

			// Result files name
			this.resultNameColumn = new ColumnFieldsetMeta(null, "结果文件名");
			this.resultNameColumn.setSingle(true);
			//Add Filenames
			List<OptionDataMeta> addOptions = new ArrayList<OptionDataMeta>();
			addOptions.add(new OptionDataMeta(entry.ADD_ALL_FILENAMES,
					Messages
							.getString("JobXMLWellFormed.AddAllFilenamesToResult.Label")));
			addOptions.add(new OptionDataMeta(entry.ADD_WELL_FORMED_FILES_ONLY,
					Messages
							.getString("JobXMLWellFormed.AddOnlyWellFormedFilenames.Label")));
			addOptions.add(new OptionDataMeta(entry.ADD_BAD_FORMED_FILES_ONLY,
					Messages
							.getString("JobXMLWellFormed.AddOnlyBadFormedFilenames.Label")));
			this.resultfilenames = new LabelSelectMeta(id + ".resultfilenames",
					"添加文件名", "添加文件名", null, null, entry.getResultFilenames(), null, addOptions);
			this.resultfilenames.setSingle(true);
			this.resultNameColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.resultfilenames
					});
			// 装载到页签
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.successOnColumn,this.resultNameColumn
			});
			
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,this.meta});
			
			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn() , super.getCancelBtn()});
			cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getJobMeta().getName());
			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
