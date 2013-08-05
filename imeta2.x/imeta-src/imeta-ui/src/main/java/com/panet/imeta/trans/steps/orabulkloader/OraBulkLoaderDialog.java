package com.panet.imeta.trans.steps.orabulkloader;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class OraBulkLoaderDialog extends BaseStepDialog implements
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
	 * 数据库连接
	 */
	private LabelSelectMeta dbconnect;

	/**
	 * Target schema
	 */
	private LabelInputMeta schemaName;

	/**
	 * Target table
	 */
	private LabelInputMeta tableName;

	/**
	 * Sqlldr path
	 */
	private LabelInputMeta sqlldr;

	/**
	 * Load method
	 */
	private LabelSelectMeta loadMethod;

	/**
	 * Load action
	 */
	private LabelSelectMeta loadAction;

	/**
	 * Maximum errors
	 */
	private LabelInputMeta maxErrors;

	/**
	 * Commit
	 */
	private LabelInputMeta commitSize;

	/**
	 * Bind Size
	 */
	private LabelInputMeta bindSize;

	/**
	 * Read Size
	 */
	private LabelInputMeta readSize;

	/**
	 * DB Name Override
	 */
	private LabelInputMeta dbNameOverride;

	/**
	 * Control file
	 */
	private LabelInputMeta controlFile;

	/**
	 * Data file
	 */
	private LabelInputMeta dataFile;

	/**
	 * Log file
	 */
	private LabelInputMeta logFile;

	/**
	 * Bad file
	 */
	private LabelInputMeta badFile;

	/**
	 * Discard file
	 */
	private LabelInputMeta discardFile;

	/**
	 * Encoding
	 */
	private LabelSelectMeta encoding;

	/**
	 * Oracle Character
	 */
	private LabelSelectMeta characterSetName;

	/**
	 * Alternate Record
	 */
	private LabelInputMeta altRecordTerm;

	/**
	 * Direct path
	 */
	private LabelInputMeta directPath;

	/**
	 * Erase cfg/dat files after use
	 */
	private LabelInputMeta eraseFiles;

	/**
	 * fail on warning
	 */
	private LabelInputMeta failOnWarning;

	/**
	 * Fail on error
	 */
	private LabelInputMeta failOnError;

	/**
	 * Fields to load
	 */
	private LabelGridMeta fieldstoload;

	private static String[] encodings = { "", //$NON-NLS-1$
			"US-ASCII", //$NON-NLS-1$
			"ISO-8859-1", //$NON-NLS-1$
			"UTF-8", //$NON-NLS-1$
			"UTF-16BE", //$NON-NLS-1$
			"UTF-16LE", //$NON-NLS-1$
			"UTF-16" }; //$NON-NLS-1$

	private static String[] characterSetNames = { "", //$NON-NLS-1$
			"US7ASCII", //$NON-NLS-1$
			"WE8ISO8859P1", //$NON-NLS-1$
			"UTF8", }; //$NON-NLS-1$

	public OraBulkLoaderDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			OraBulkLoaderMeta step = (OraBulkLoaderMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 数据库连接
			this.dbconnect = new LabelSelectMeta(id + ".dbconnect", "数据库连接",
					null, null, null, String
							.valueOf(step.getDatabaseMeta() == null ? 0 : step
									.getDatabaseMeta().getID()), null, super
							.getConnectionLine());
			this.dbconnect.setSingle(true);

			// Target schema目标模式

			this.schemaName = new LabelInputMeta(id + ".schemaName", "目标模式",
					null, null, "", step.getSchemaName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.schemaName.setSingle(true);

			// Target table目标表

			this.tableName = new LabelInputMeta(id + ".tableName", "目标表", null,
					null, "", step.getTableName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.tableName.setSingle(true);

			// Sqlldr path Sqlldr路径

			this.sqlldr = new LabelInputMeta(id + ".sqlldr", "Sqlldr路径", null,
					null, "", step.getSqlldr(), null, ValidateForm
							.getInstance().setRequired(false));
			this.sqlldr.setSingle(true);

			// Load method 加载方法
			List<OptionDataMeta> optionsloadmethod = new ArrayList<OptionDataMeta>();
			optionsloadmethod
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.METHOD_AUTO_END,
							Messages
									.getString("OraBulkLoaderDialog.AutoEndLoadMethod.Label")));
			optionsloadmethod
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.METHOD_MANUAL,
							Messages
									.getString("OraBulkLoaderDialog.ManualLoadMethod.Label")));
			optionsloadmethod
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.METHOD_AUTO_CONCURRENT,
							Messages
									.getString("OraBulkLoaderDialog.AutoConcLoadMethod.Label")));

			this.loadMethod = new LabelSelectMeta(id + ".loadMethod", "加载方法",
					null, null, null, step.getLoadMethod(), null,
					optionsloadmethod);
			this.loadMethod.setSingle(true);
			this.loadMethod.setHasEmpty(true);

			// Load action 加载动作
			List<OptionDataMeta> optionsloadaction = new ArrayList<OptionDataMeta>();
			optionsloadaction
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.ACTION_APPEND,
							Messages
									.getString("OraBulkLoaderDialog.AppendLoadAction.Label")));
			optionsloadaction
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.ACTION_INSERT,
							Messages
									.getString("OraBulkLoaderDialog.InsertLoadAction.Label")));
			optionsloadaction
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.ACTION_REPLACE,
							Messages
									.getString("OraBulkLoaderDialog.ReplaceLoadAction.Label")));
			optionsloadaction
					.add(new OptionDataMeta(
							OraBulkLoaderMeta.ACTION_TRUNCATE,
							Messages
									.getString("OraBulkLoaderDialog.TruncateLoadAction.Label")));
			this.loadAction = new LabelSelectMeta(id + ".loadAction", "加载动作",
					null, null, null, step.getLoadAction(), null,
					optionsloadaction);
			this.loadAction.setSingle(true);
			this.loadAction.setHasEmpty(true);

			// Maximum errors 最大误差

			this.maxErrors = new LabelInputMeta(id + ".maxErrors", "最大错误数",
					null, null, "", String.valueOf(step.getMaxErrors()), null,
					ValidateForm.getInstance().setRequired(false));
			this.maxErrors.setSingle(true);

			// Commit

			this.commitSize = new LabelInputMeta(id + ".commitSize", "提交个数",
					null, null, "", String.valueOf(step.getCommitSize()), null,
					ValidateForm.getInstance().setRequired(false));
			this.commitSize.setSingle(true);

			// Bind Size 约束范围

			this.bindSize = new LabelInputMeta(id + ".bindSize", "绑定个数", null,
					null, "", String.valueOf(step.getBindSize()), null,
					ValidateForm.getInstance().setRequired(false));
			this.bindSize.setSingle(true);

			// Read Size 读取范围

			this.readSize = new LabelInputMeta(id + ".readSize", "读取个数", null,
					null, "", String.valueOf(step.getReadSize()), null,
					ValidateForm.getInstance().setRequired(false));
			this.readSize.setSingle(true);

			// DB Name Override 数据库名称重写

			this.dbNameOverride = new LabelInputMeta(id + ".dbNameOverride",
					"覆盖数据库名称", null, null, "", step.getDbNameOverride(), null,
					ValidateForm.getInstance().setRequired(false));
			this.dbNameOverride.setSingle(true);

			// Control file 控制文件

			this.controlFile = new LabelInputMeta(id + ".controlFile", "控制文件",
					null, null, "", step.getControlFile(), null, ValidateForm
							.getInstance().setRequired(false));
			this.controlFile.setSingle(true);

			// Data file 数据文件

			this.dataFile = new LabelInputMeta(id + ".dataFile", "数据文件", null,
					null, "", step.getDataFile(), null, ValidateForm
							.getInstance().setRequired(false));
			this.dataFile.setSingle(true);

			// Log file 日志文件

			this.logFile = new LabelInputMeta(id + ".logFile", "日志文件", null,
					null, "", step.getLogFile(), null, ValidateForm
							.getInstance().setRequired(false));
			this.logFile.setSingle(true);

			// Bad file 错误文件

			this.badFile = new LabelInputMeta(id + ".badFile", "错误文件", null,
					null, "", step.getBadFile(), null, ValidateForm
							.getInstance().setRequired(false));
			this.badFile.setSingle(true);

			// Discard file

			this.discardFile = new LabelInputMeta(id + ".discardFile", "丢弃文件",
					null, null, "", step.getDiscardFile(), null, ValidateForm
							.getInstance().setRequired(false));
			this.discardFile.setSingle(true);

			List<OptionDataMeta> optionEncoding = new ArrayList<OptionDataMeta>();
			for (String s : encodings)
				optionEncoding.add(new OptionDataMeta(s, s));
			this.encoding = new LabelSelectMeta(id + ".encoding", "编码", null,
					null, null, step.getEncoding(), null, optionEncoding);
			this.encoding.setSingle(true);

			// Oracle Character Set
			List<OptionDataMeta> optionsb = new ArrayList<OptionDataMeta>();
			for (String s : characterSetNames)
				optionsb.add(new OptionDataMeta(s, s));
			this.characterSetName = new LabelSelectMeta(id
					+ ".characterSetName", "Oracle 字符集", null, null, null, step
					.getCharacterSetName(), null, optionsb);
			this.characterSetName.setSingle(true);

			// Alternate Record 候补记录

			this.altRecordTerm = new LabelInputMeta(id + ".altRecordTerm",
					"候补记录", null, null, "", step.getAltRecordTerm(), null,
					ValidateForm.getInstance().setRequired(false));
			this.altRecordTerm.setSingle(true);

			// Direct path 直接路径
			this.directPath = new LabelInputMeta(id + ".directPath", "直接路径",
					null, null, null, String.valueOf(step.isDirectPath()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.directPath.setSingle(true);

			// Erase cfg/dat files after use 使用后移除cfg/dat文件
			this.eraseFiles = new LabelInputMeta(id + ".eraseFiles",
					"使用后移除cfg/dat文件", null, null, null, String.valueOf(step
							.isEraseFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.eraseFiles.setSingle(true);

			// Fail on warning 失败警报
			this.failOnWarning = new LabelInputMeta(id + ".failOnWarning",
					"遇到警报后失败", null, null, null, String.valueOf(step
							.isFailOnWarning()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.failOnWarning.setSingle(true);

			// Fail no error 失败错误
			this.failOnError = new LabelInputMeta(id + ".failOnError",
					"遇到错误后失败", null, null, null, String.valueOf(step
							.isFailOnError()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.failOnError.setSingle(true);

			// Fields to load 加载字段
			this.fieldstoload = new LabelGridMeta(id + "_fields", "加载字段", 200,
					0);
			GridHeaderDataMeta fieldStreamDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldStream", "流字段", null, false, 100);
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] fieldStreamNames = r.getFieldNames();
			fieldStreamDataMeta.setOptions(super.getOptionsByStringArray(
					fieldStreamNames, true));

			GridHeaderDataMeta fieldTableDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldTable", "表字段", null, false, 100);
			RowMetaInterface r1 = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] fieldTableNames = r1.getFieldNames();
			fieldTableDataMeta.setOptions(super.getOptionsByStringArray(
					fieldTableNames, true));

			GridHeaderDataMeta dateMaskDataMeta = new GridHeaderDataMeta(id
					+ "_fields.dateMask", "日期掩码", null, false, 100);
			dateMaskDataMeta
					.setOptions(super
							.getOptionsByStringArray(
									new String[] {
											"",
											Messages
													.getString("OraBulkLoaderDialog.DateMask.Label"),
											Messages
													.getString("OraBulkLoaderDialog.DateTimeMask.Label") },
									false));

			this.fieldstoload
					.addHeaders(new GridHeaderDataMeta[] {
							new GridHeaderDataMeta(id + "_fields.fieldId", "#",
									GridHeaderDataMeta.HEADER_TYPE_NUMBER,
									false, 50), fieldTableDataMeta,
							fieldStreamDataMeta, dateMaskDataMeta });
			this.fieldstoload.setSingle(true);

			this.fieldstoload.setHasBottomBar(true);
			this.fieldstoload.setHasAdd(true, true,
					"jQuery.imeta.steps.OraBulkLoader.btn.fieldAdd");
			this.fieldstoload.setHasDelete(true, true,
					"jQuery.imeta.parameter.fieldsDelete");

			String[] fieldTable = step.getFieldTable();
			if (fieldTable != null && fieldTable.length > 0) {
				for (int i = 0; i < fieldTable.length; i++) {
					if (fieldTable[i] != null)
						this.fieldstoload.addRow(new Object[] {
								String.valueOf(i + 1),
								new GridCellDataMeta(null, fieldTable[i],
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null, step
										.getFieldStream()[i],
										GridCellDataMeta.CELL_TYPE_SELECT),
								new GridCellDataMeta(null,
										step.getDateMask()[i],
										GridCellDataMeta.CELL_TYPE_SELECT), });
				}
			}
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.dbconnect, this.schemaName, this.tableName,
					this.sqlldr, this.loadMethod, this.loadAction,
					this.maxErrors, this.commitSize, this.bindSize,
					this.readSize, this.dbNameOverride, this.controlFile,
					this.dataFile, this.logFile, this.badFile,
					this.discardFile, this.encoding, this.characterSetName,
					this.altRecordTerm, this.directPath, this.eraseFiles,
					this.failOnWarning, this.failOnError, this.fieldstoload, });

			columnFormMeta
					.putFieldsetsFootButtons(new ButtonMeta[] {
							super
									.getGetfields("jQuery.imeta.steps.OraBulkLoader.btn.getfields"),
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
