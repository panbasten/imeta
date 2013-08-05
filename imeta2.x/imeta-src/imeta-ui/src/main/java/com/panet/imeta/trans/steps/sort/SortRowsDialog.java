package com.panet.imeta.trans.steps.sort;

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
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class SortRowsDialog extends BaseStepDialog implements
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
	 * 排序目录
	 */
	private LabelInputMeta directory;

	// private ButtonMeta browse;

	/**
	 * 临时文件前缀
	 */
	private LabelInputMeta prefix;

	/**
	 * 排序缓存大小
	 */
	private LabelInputMeta sortSize;

	/**
	 * 未使用的内存限值
	 */
	private LabelInputMeta freeMemoryLimit;

	/**
	 * 压缩临时文件
	 */
	private LabelInputMeta compressFiles;
	private LabelInputMeta compressFilesVariable;

	/**
	 * 仅仅传递非重复的记录？
	 */
	private LabelInputMeta duplicaterecord;

	/**
	 * 字段
	 */
	private LabelGridMeta fields;

	public SortRowsDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();

			SortRowsMeta step = (SortRowsMeta) super.getStep();
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称必须填写", super.getStepMeta().getName(), null,
					ValidateForm.getInstance().setRequired(true));
			this.name.setSingle(true);

			// 排序目录

			this.directory = new LabelInputMeta(id + ".directory", "排序目录",
					null, null, 
					//"排序目录必须填写",
					null,
					String.valueOf(step.getDirectory()), null, ValidateForm
							.getInstance().setRequired(false));

			// this.browse = new ButtonMeta(id + ".btn.browse", id
			// + ".btn.browse", "浏览", "浏览");
			//			
			// this.directory.addButton(new ButtonMeta[] { this.browse});

			this.directory.setSingle(true);

			// 临时文件前缀
			this.prefix = new LabelInputMeta(id + ".prefix", "临时文件前缀", null,
					null, 
					//"临时文件前缀必须填写", 
					null, String.valueOf(step.getPrefix()), null,
					ValidateForm.getInstance().setRequired(false));
			this.prefix.setSingle(true);

			// 排序缓存大小
			this.sortSize = new LabelInputMeta(id + ".sortSize",
					"排序缓存大小（内存里存放的记录数）", null, null, 
					//"排序缓存大小必须填写", 
					null, step.getSortSize(), null, ValidateForm.getInstance()
							.setRequired(false));
			this.sortSize.setSingle(true);

			// 未使用的内存限值（%）
			this.freeMemoryLimit = new LabelInputMeta(id + ".freeMemoryLimit",
					"未使用的内存限值（%）", null, null, 
					//"未使用的内存限值必须填写", 
					null, step.getFreeMemoryLimit(), null,
					ValidateForm.getInstance().setRequired(false));
			this.freeMemoryLimit.setSingle(true);

			// 压缩临时文件？
			this.compressFiles = new LabelInputMeta(id + ".compressFiles",
					"压缩临时文件？", null, null, null, String.valueOf(step
							.getCompressFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.compressFiles.setSingle(false);

			this.compressFilesVariable = new LabelInputMeta(id
					+ ".compressFilesVariable", "", null, null, null, step
					.getCompressFilesVariable(), null, ValidateForm
					.getInstance().setRequired(false));
			this.compressFilesVariable.setSingle(false);
	
			// 传递非重复的记录？
			this.duplicaterecord = new LabelInputMeta(id + ".duplicaterecord",
					"仅仅传递非重复的记录？（仅仅验校关键字）", null, null, null, String
							.valueOf(step.isOnlyPassingUniqueRows()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.duplicaterecord.setSingle(true);

			// 字段
			this.fields = new LabelGridMeta(id + "_fields", "字段", 200);
			
			GridHeaderDataMeta fieldBooleanTypeDataMetaA = new GridHeaderDataMeta(
					id + "_fields.ascending", "升序？", null, false, 60);
			fieldBooleanTypeDataMetaA.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			GridHeaderDataMeta fieldBooleanTypeDataMetaB = new GridHeaderDataMeta(
					id + "_fields.caseSensitive", "大小写敏感？", null, false, 100);
			fieldBooleanTypeDataMetaB.setOptions(super
					.getOptionsByTrueAndFalse(false));
			
			RowMetaInterface r = super.getTransMeta().getPrevStepFields(
					super.getStepMeta());
			String[] resultNames = r.getFieldNames();
			GridHeaderDataMeta fieldMeta = new GridHeaderDataMeta(id
					+ "_fields.fieldName", "字段名称", null, false, 100);
			fieldMeta.setOptions(super.getOptionsByStringArray(
					resultNames, true));
			
			this.fields.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",
							GridHeaderDataMeta.HEADER_TYPE_NUMBER, false, 50),
							fieldMeta,
					fieldBooleanTypeDataMetaA,
					fieldBooleanTypeDataMetaB });
			this.fields.setHasBottomBar(true);
			this.fields.setHasAdd(true, true,
					"jQuery.imeta.steps.sort.btn.sortAdd");
			this.fields.setHasDelete(true, true,
			        "jQuery.imeta.parameter.fieldsDelete");
			this.fields.setSingle(true);

			String[] field = step.getFieldName();
			if (field != null && field.length > 0)
				for (int i = 0; i < field.length; i++) {
					this.fields.addRow(new Object[] {
							String.valueOf(i + 1),
							new GridCellDataMeta(null, step.getFieldName()[i],
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null, String.valueOf(step.getAscending()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null, String.valueOf(step.getCaseSensitive()[i]),
									GridCellDataMeta.CELL_TYPE_SELECT) });
				}

			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.directory, this.prefix, this.sortSize,
					this.freeMemoryLimit, this.compressFiles,
					this.compressFilesVariable, this.duplicaterecord,
					this.fields });

			columnFormMeta.putFieldsetsFootButtons(new ButtonMeta[] {
					super.getOkBtn(), super.getCancelBtn(),
					super.getGetfields("jQuery.imeta.steps.sort.btn.getwords")});
			
            cArr.add(columnFormMeta.getFormJo());

			rtn.put("items", cArr);
			rtn.put("title", super.getStepMeta().getName());

			return rtn;
		} catch (Exception ex) {
			throw new ImetaException(ex.getMessage(), ex);
		}
	}
}
