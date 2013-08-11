package com.panet.imeta.job.entries.xslt;

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
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.imeta.job.JobMeta;
import com.panet.imeta.job.entry.JobEntryCopy;
import com.panet.imeta.job.entry.JobEntryDialog;
import com.panet.imeta.job.entry.JobEntryDialogInterface;
import com.panet.imeta.ui.exception.ImetaException;

public class JobEntryXSLTDialog extends JobEntryDialog implements
JobEntryDialogInterface {
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;

	/**
	 * 作业实体名
	 */
	private LabelInputMeta name;

	/**
	 * XML File name
	 */
	private LabelInputMeta xmlfilename;
//	private ButtonMeta xmlFilenameBrowseBtn;

	/**
	 * XSL File name
	 */
	private LabelInputMeta xslfilename;
//	private ButtonMeta xslFilenameBrowseBtn;

	/**
	 * Transformer factory
	 */
	private LabelSelectMeta xsltfactory;

	/**
	 * Output Filename
	 */
	private LabelInputMeta outputfilename;
//	private ButtonMeta outputFilenameBrowseBtn;

	/**
	 * If file exists
	 */
	private LabelSelectMeta iffileexists;

	/**
	 * Result files name
	 */
	private ColumnFieldsetMeta resultNameColumn;
	/**
	 * Add output filename to
	 */
	private LabelInputMeta addfiletoresult;

	/**
	 * 初始化
	 * @param jobMeta
	 * @param jobEntryMeta
	 */
	public JobEntryXSLTDialog(JobMeta jobMeta, JobEntryCopy jobEntryMeta) {
		super(jobMeta, jobEntryMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			JobEntryXSLT entry = (JobEntryXSLT) super.getJobEntry();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 作业实体名
			this.name = new LabelInputMeta(id + ".name", "作业实体名", null, null,
					null, super.getJobEntryName(), null, ValidateForm
					.getInstance().setRequired(true));
			this.name.setSingle(true);

			// XML File name
			this.xmlfilename = new LabelInputMeta(id + ".xmlfilename", "XML文件名",
					null, null,null, entry.getxmlFilename(), null, 
					ValidateForm.getInstance().setRequired(true));
//			this.xmlFilenameBrowseBtn = new ButtonMeta(id
//					+ ".btn.xmlFilenameBrowseBtn", id + ".btn.xmlFilenameBrowseBtn",
//					"浏览", "浏览");
//			this.xmlfilename.addButton(new ButtonMeta[]{this.xmlFilenameBrowseBtn});
			this.xmlfilename.setSingle(true);

			// XSL File name
			this.xslfilename = new LabelInputMeta(id + ".xslfilename", "XSL文件名称",
					null, null,null, entry.getxslFilename(), null, ValidateForm.getInstance()
					.setRequired(true));
//			this.xslFilenameBrowseBtn = new ButtonMeta(id
//					+ ".btn.xslFilenameBrowseBtn", id + ".btn.xslFilenameBrowseBtn",
//					"浏览", "浏览");
//			this.xslfilename.addButton(new ButtonMeta[]{this.xslFilenameBrowseBtn});
			this.xslfilename.setSingle(true);

			// Transformer factory
			List<OptionDataMeta> xsltOptions = new ArrayList<OptionDataMeta>();
			xsltOptions.add(new OptionDataMeta("0",JobEntryXSLT.FACTORY_JAXP));
			xsltOptions.add(new OptionDataMeta("1",JobEntryXSLT.FACTORY_SAXON));
			this.xsltfactory = new LabelSelectMeta(id + ".xsltfactory",
					"变换器工厂", null, null, null, entry.getXSLTFactory(), 
					null, xsltOptions);
			this.xsltfactory.setSingle(true);

			// Output Filename
			this.outputfilename = new LabelInputMeta(id + ".outputfilename", 
					"输出文件名",null, null,null, entry.getoutputFilename(), null, 
					ValidateForm.getInstance().setRequired(true));
//			this.outputFilenameBrowseBtn = new ButtonMeta(id
//					+ ".btn.outputFilenameBrowseBtn", id + ".btn.outputFilenameBrowseBtn",
//					"浏览", "浏览");
//			this.outputfilename.addButton(new ButtonMeta[] {this.outputFilenameBrowseBtn});
			this.outputfilename.setSingle(true);
			// If file exists
			List<OptionDataMeta> ifexOptions = new ArrayList<OptionDataMeta>();
			ifexOptions.add(new OptionDataMeta("0",Messages.getString("JobEntryXSLT.Create_NewFile_IfFileExists.Label")));
			ifexOptions.add(new OptionDataMeta("1",Messages.getString("JobEntryXSLT.Do_Nothing_IfFileExists.Label")));
			ifexOptions.add(new OptionDataMeta("2",Messages.getString("JobEntryXSLT.Fail_IfFileExists.Label")));
			this.iffileexists = new LabelSelectMeta(id + ".iffileexists",
					"如果文件存在", null, null, null, String.valueOf(entry.iffileexists), null, ifexOptions);
			this.iffileexists.setSingle(true);

			// Result files name
			this.resultNameColumn = new ColumnFieldsetMeta(null, "结果文件名");
			this.resultNameColumn.setSingle(true);
			// Add output filename to
			this.addfiletoresult = new LabelInputMeta(id + ".addfiletoresult",
					"添加输出文件名",null, null, null, String.valueOf(entry.isAddFileToResult()), InputDataMeta.
					INPUT_TYPE_CHECKBOX, ValidateForm.getInstance().setRequired(true));

			this.resultNameColumn.putFieldsetsContent(new BaseFormMeta[] {
					this.addfiletoresult
			});

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.xmlfilename,this.xslfilename,this.xsltfactory,
					this.outputfilename,this.iffileexists,this.resultNameColumn });

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
