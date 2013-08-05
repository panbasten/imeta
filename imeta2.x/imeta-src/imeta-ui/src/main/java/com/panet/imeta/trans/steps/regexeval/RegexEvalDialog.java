package com.panet.imeta.trans.steps.regexeval;

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
import com.panet.iform.forms.labelTextarea.LabelTextareaMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class RegexEvalDialog extends BaseStepDialog implements
		StepDialogInterface {
	
	/**
	 * 列式表单
	 */
	private ColumnFormMeta columnFormMeta;
	private ColumnFormDataMeta columnFormDataMeta;
	
	/**
	 * 步骤名称
	 */
	private LabelInputMeta name;

	/**
	 * 页签
	 */
	private MenuTabMeta meta;
	
	/**PPPPPPPPPPPPPPPPPP******************
	 * 设置
	 **************************************/

	/**
	 * 要匹配字段
	 */
	private LabelSelectMeta matcher;
	
	/**
	 * 结果域名
	 */
	private LabelInputMeta resultfieldname;

	/**
	 * 为捕获组创建一个字段
	 */
	private LabelInputMeta allowcapturegroups;

	/**
	 * 正则表达式
	 */
	private LabelTextareaMeta script;
	
	/**
	 * 变量替换
	 */
	private LabelInputMeta usevar;
	
	/**
	 * 捕获组字段
	 */
	private LabelGridMeta fields;

	/********************************
	 * 内容
	 ********************************/

	/**
	 * 正规分解匹配
	 */
	private LabelInputMeta canoneq;
	/**
	 * 忽略大小写
	 */
	private LabelInputMeta caseinsensitive;
	/**
	 * 在表达式中允许有空格和注释
	 */
	private LabelInputMeta comment;
	/**
	 * 点字符全部匹配模式
	 */
	private LabelInputMeta multiline;
	/**
	 * 启用多行模式
	 */
	private LabelInputMeta dotall;
	/**
	 * Unicode 忽略大小写
	 */
	private LabelInputMeta unicode;
	/**
	 * Unix 行模式
	 */
	private LabelInputMeta unix;

	/**
	 * 初始化
	 * 
	 * @param stepMeta
	 * @param transMeta
	 */
	public RegexEvalDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			RegexEvalMeta step = (RegexEvalMeta) super.getStep();
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepMeta().getName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "设置", "内容" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------设置
			 ******************************************************************/
			// 要匹配的字段
			this.matcher = new LabelSelectMeta(id + ".matcher", "要匹配的字段",
					null, null, null, step.getMatcher(), null,
					super.getPrevStepResultFields());
			this.matcher.setSingle(true);
			
			// 结果域名
			this.resultfieldname = new LabelInputMeta(id + ".resultfieldname", "结果域名", null, null,
					"结果域名必须填写", step.getResultFieldName(), null, ValidateForm
							.getInstance().setRequired(true));
			this.resultfieldname.setSingle(true);

			// 为捕获组创建一个字段
			this.allowcapturegroups = new LabelInputMeta(id + ".allowcapturegroups",
					"为捕获组创建一个字段", null, null, null, String.valueOf(step.isAllowCaptureGroupsFlagSet()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.allowcapturegroups
					.addClick("jQuery.imeta.steps.regexeval.listeners.createFileListener");
			this.allowcapturegroups.setSingle(true);
			
			// 正则表达式
			script = new LabelTextareaMeta(id + ".script", "正则表达式：", null, null,
					null, step.getScript(), 5, ValidateForm.getInstance()
							.setRequired(false));
			script.setLayout(LabelTextareaMeta.LAYOUT_COLUMN);

			script.setSingle(true);
			script.setWidth(99);
			
			// 变量替换
			this.usevar = new LabelInputMeta(id + ".usevar", "变量替换",
					null, null, null, String.valueOf(step.isUseVariableInterpolationFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
            this.usevar.setSingle(true);

			this.meta.putTabContent(0, new BaseFormMeta[] { this.matcher,
					this.resultfieldname, this.allowcapturegroups, this.script,
					this.usevar });

			/*******************************************************************
			 * 标签1---------内容
			 ******************************************************************/
			// 正规分解匹配
			this.canoneq = new LabelInputMeta(id + ".canoneq", "正规分解匹配", null,
					null, null, String.valueOf(step.isCanonicalEqualityFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.canoneq.setSingle(true);
			
			// 忽略大小写
			this.caseinsensitive = new LabelInputMeta(id + ".caseinsensitive", "忽略大小写",
					null, null, null, String.valueOf(step.isCaseInsensitiveFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.caseinsensitive.setSingle(true);
			
			// 在表达式中允许有空格和注释
			this.comment = new LabelInputMeta(id + ".comment", "在表达式中允许有空格和注释", null,
					null, null, String.valueOf(step.isCommentFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.comment.setSingle(true);
			
			// 点字符全部匹配模式
			this.multiline = new LabelInputMeta(id + ".multiline", "点字符全部匹配模式", null, null,
					null, String.valueOf(step.isMultilineFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.multiline.setSingle(true);
			
			// 启用多行模式
			this.dotall = new LabelInputMeta(id + ".dotall", "启用多行模式", null, null,
					null, String.valueOf(step.isDotAllFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dotall.setSingle(true);
			
			// Unicode 忽略大小写
			this.unicode = new LabelInputMeta(id + ".unicode",
					"Unicode 忽略大小写", null, null, null, String.valueOf(step.isUnicodeFlagSet()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.unicode.setSingle(true);
			
			// Unix 行模式
			this.unix = new LabelInputMeta(id + ".unix", "Unix 行模式", null, null,
					null, String.valueOf(step.isUnixLineEndingsFlagSet()), InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.unix.setSingle(true);

			this.meta.putTabContent(1, new BaseFormMeta[] { this.canoneq,
					this.caseinsensitive, this.comment, this.multiline, this.dotall,
					this.unicode, this.unix });

			// 捕获组字段
			this.fields = new LabelGridMeta(id + "_fields", "捕获组字段", 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldType", "类型", null, false, 100);
			fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(
					ValueMeta.typeCodes, false));
			
			GridHeaderDataMeta fieldTrimTypeDataMeta = new GridHeaderDataMeta(
					id + "_fields.fieldTrimType", "去空格类型", null, false, 100);
			fieldTrimTypeDataMeta.setOptions(super
					.getOptionsByStringArrayWithNumberValue(
							ValueMeta.trimTypeDesc, false));

			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "新字段",
							null, false, 120),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组符号",
							null, false, 60),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "十进制",
							null, false,60),
					new GridHeaderDataMeta(id + "_fields.fieldNullIf", "货币",
							null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldIfNull",
							"如果..则置空", null, false, 100),
					fieldTrimTypeDataMeta });
			this.fields.setSingle(true);
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, step.isAllowCaptureGroupsFlagSet(),
					"jQuery.imeta.steps.regexeval.btn.fieldAdd");
			this.fields.setHasDelete(true, step.isAllowCaptureGroupsFlagSet(),
					"jQuery.imeta.parameter.fieldsDelete");

			String[] values = step.getFieldName();
			if (values != null && values.length > 0)
				for (int i = 0; i < values.length; i++) {

					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
						    new GridCellDataMeta(null, String.valueOf(step.getFieldLength()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldPrecision()[i]),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldFormat()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldGroup()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldDecimal()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldNullIf()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, step.getFieldIfNull()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getFieldTrimType()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,

			this.meta, this.fields });

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
