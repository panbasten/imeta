package com.panet.imeta.trans.steps.xmloutput;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.panet.iform.core.BaseFormMeta;
import com.panet.iform.core.NullSimpleFormDataMeta;
import com.panet.iform.core.ValidateForm;
import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.DivMeta;
import com.panet.iform.core.base.InputDataMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormDataMeta;
import com.panet.iform.forms.columnForm.ColumnFormMeta;
import com.panet.iform.forms.grid.GridCellDataMeta;
import com.panet.iform.forms.grid.GridHeaderDataMeta;
import com.panet.iform.forms.labelGrid.LabelGridMeta;
import com.panet.iform.forms.labelInput.LabelInputMeta;
import com.panet.iform.forms.labelSelect.LabelSelectMeta;
import com.panet.iform.forms.menuTab.MenuTabMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepDialog;
import com.panet.imeta.trans.step.StepDialogInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.ui.exception.ImetaException;

public class XMLOutputDialog extends BaseStepDialog implements
StepDialogInterface{
	
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
	
	/**
	 * 文件名
	 */
	private LabelInputMeta fileName;
	

	/**
	 * 不要建立档案 Do not create file at
	 */
	private LabelInputMeta doNotOpenNewFileInit;

	/**
	 * 扩展
	 */
	private LabelInputMeta extension;

	/**
	 * 在文件里面包含步骤号码
	 */
	private LabelInputMeta stepNrInFilename;

	/**
	 * 在文件里包含日期
	 */
	private LabelInputMeta dateInFilename;

	/**
	 * 在文件里包含时间
	 */
	private LabelInputMeta timeInFilename;

	/**
	 * 指定日期时间格式 Specify Date time format
	 */
	private LabelInputMeta SpecifyFormat;

	/**
	 * 日期时间格式 Date time format
	 */
	private LabelSelectMeta date_time_format;

//	/**
//	 * 显示字段名称
//	 */
//	private ButtonMeta showFieldName;

	/**
	 * 添加文件名的结果 Add filenames to result
	 */
	private LabelInputMeta addToResultFilenames;
	
	/**
	 * 内容
	 * 已压缩（Zipped）
	 */
	private LabelInputMeta zipped;
	
	/**
	 * 正在编码
	 */
	private LabelSelectMeta encoding;
	
	/**
	 * Name space
	 */
	private LabelInputMeta nameSpace;
	
	/**
	 * 父XML元素
	 */
	private LabelSelectMeta mainElement;
	
	/**
	 * 记录XML元素
	 */
	private LabelSelectMeta repeatElement;
	
	/**
	 * 分割每一个记录
	 */
	private LabelInputMeta splitEvery;
	
	/**
	 * 字段 2
	 */
	private LabelGridMeta words;

//	/**
//	 * 最小宽度
//	 */
//	private ButtonMeta minwidth;

	
	public XMLOutputDialog(StepMeta stepMeta, TransMeta transMeta) {
		super(stepMeta, transMeta);
	}

	@Override
	public JSONObject open() throws ImetaException {
		try {
			JSONObject rtn = new JSONObject();
			JSONArray cArr = new JSONArray();
			String id = this.getId();
			
			XMLOutputMeta step = (XMLOutputMeta) super.getStep();
			
			// 得到form
			this.columnFormDataMeta = new ColumnFormDataMeta(id, null);
			this.columnFormMeta = new ColumnFormMeta(columnFormDataMeta);

			// 得到步骤名称
			this.name = new LabelInputMeta(id + ".name", "步骤名称", null, null,
					"步骤名称", super.getStepName(), null, ValidateForm
							.getInstance().setRequired(false));
			this.name.setSingle(true);

			// 得到页签
			this.meta = new MenuTabMeta(id, new String[] { "文件", "内容", "字段" });
			this.meta.setSingle(true);

			/*******************************************************************
			 * 标签0---------文件
			 ******************************************************************/
			// 文件名
			this.fileName = new LabelInputMeta(id + ".fileName", "文件名", null,
					null, null, 
					step.getFileName(), 
					null, ValidateForm.getInstance().setRequired(false));

			this.fileName.setSingle(true);
			
			// Do not create file at
			this.doNotOpenNewFileInit = new LabelInputMeta(id + ".doNotOpenNewFileInit",
					"不要建立档案", null, null, null,
					String.valueOf(step.isDoNotOpenNewFileInit()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
		
			// 扩展名
			this.extension = new LabelInputMeta(id + ".extension",
					"扩展名", null, null, null, 
					step.getExtension(), null, ValidateForm
							.getInstance().setRequired(false));
			this.extension.setSingle(true);
			
			// 在文件里面包含步骤号码
			this.stepNrInFilename = new LabelInputMeta(id + ".stepNrInFilename",
					"在文件里面包含步骤", null, null, null, 
					String.valueOf(step.isStepNrInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.stepNrInFilename.setSingle(true);
			
			// 在文件里包含日期
			this.dateInFilename = new LabelInputMeta(id + ".dateInFilename",
					"在文件里包含日期", null, null, null,
					String.valueOf(step.isDateInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.dateInFilename.setSingle(true);
			this.dateInFilename.setDisabled(step.isSpecifyFormat());
			
			// 在文件里包含时间
			this.timeInFilename = new LabelInputMeta(id + ".timeInFilename",
					"在文件里包含时间", null, null, null, 
					String.valueOf(step.isTimeInFilename()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
							.getInstance().setRequired(false));
			this.timeInFilename.setSingle(true);
			this.timeInFilename.setDisabled(step.isSpecifyFormat());
			
			// 指定日期时间格式 Specify Date time format
			this.SpecifyFormat = new LabelInputMeta(id
					+ ".SpecifyFormat", "指定日期时间格式",
					null, null, null, 
					String.valueOf(step.isSpecifyFormat()), 
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));
			this.SpecifyFormat.setSingle(true);
			this.SpecifyFormat.addClick("jQuery.imeta.steps.xmloutput.listeners.SpecifyFormat");
			
			// 日期时间格式 Date time format
			this.date_time_format = new LabelSelectMeta(id + ".date_time_format","日期时间格式", null, null, null,
					step.getDateTimeFormat(), null, super.getDateFormats());
			this.date_time_format.setSingle(true);
			this.date_time_format.setDisabled(!step.isSpecifyFormat());
			
//			// 显示文件名称
//			DivMeta divFileBtns = new DivMeta(new NullSimpleFormDataMeta(),
//					true);
//			this.showFieldName = new ButtonMeta(id + ".btn.showFieldName", id
//					+ ".btn.showFieldName", "显示文件名称", "显示文件名称");
//			this.showFieldName.appendTo(divFileBtns);
//			
			// Add filenames to result
			this.addToResultFilenames = new LabelInputMeta(id
					+ ".addToResultFilenames", "添加文件名", null,null, null, 
					String.valueOf(step.isAddToResultFiles()),
					InputDataMeta.INPUT_TYPE_CHECKBOX,
					ValidateForm.getInstance().setRequired(false));

			this.meta.putTabContent(0, new BaseFormMeta[] { this.fileName,
					this.doNotOpenNewFileInit, this.extension, this.stepNrInFilename,
					this.dateInFilename, this.timeInFilename,
					this.SpecifyFormat, this.date_time_format, this.addToResultFilenames });

			/*******************************************************************
			 * 标签1---------内容
			 ******************************************************************/
			
			//已压缩（Zipped）
			this.zipped = new LabelInputMeta(id + ".zipped","已压缩 Zipped", null, null, null, 
					String.valueOf(step.isZipped()),
					InputDataMeta.INPUT_TYPE_CHECKBOX, ValidateForm
					.getInstance().setRequired(false));
			
			//正在编码
		
			this.encoding = new LabelSelectMeta(id + ".encoding", "正在编码", null, null,null, 
					step.getEncoding(), null,super.getEncoding());
			this.encoding.setSingle(true);
			
			//Name space
			this.nameSpace = new LabelInputMeta(id + ".nameSpace","名称空间", null, null, null,
					step.getNameSpace(),null, null);
			this.nameSpace.setSingle(true);
			
			//父XML元素
			this.mainElement = new LabelSelectMeta(id + ".mainElement", "父 XML 元素", 
					null, null,null,
					step.getMainElement(),null, super.getPrevStepNames());
			this.mainElement.setSingle(true);
			
			//记录 XML 元素
			List<OptionDataMeta> recordxmlOptions = new ArrayList<OptionDataMeta>();
			recordxmlOptions.add(new OptionDataMeta("repeatElement"," "));
			recordxmlOptions.add(new OptionDataMeta("repeatElement","Row"));
			
			this.repeatElement = new LabelSelectMeta(id + ".repeatElement", 
					"记录 XML 元素", null, null,null, 
					step.getRepeatElement(), null, recordxmlOptions);
			this.repeatElement.setSingle(true);
			
			//分割每一个记录
			this.splitEvery = new LabelInputMeta(id + ".splitEvery",
					"分割每一个记录", null, null, null, 
					String.valueOf(step.getSplitEvery()), null, null);
			this.splitEvery.setSingle(true);
			
			this.meta.putTabContent(1, new BaseFormMeta[] {
					this.zipped,this.encoding,
					this.nameSpace,this.mainElement,
					this.repeatElement,this.splitEvery});
			
			/*******************************************************************
			 * 标签2---------字段
			 ******************************************************************/
			// 字段
			this.words = new LabelGridMeta(id + "_fields", null, 200);
			
			GridHeaderDataMeta fieldTypeDataMeta = new GridHeaderDataMeta(id+ "_fields.fieldType", "类型", null, false, 100);
			     fieldTypeDataMeta.setOptions(super.getOptionsByStringArrayWithNumberValue(ValueMetaInterface.typeCodes, false));
			
			this.words.addHeaders(new GridHeaderDataMeta[] {
					new GridHeaderDataMeta(id + "_fields.fieldId", "#",null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldName", "文件名",null, false, 100),
					fieldTypeDataMeta,
					new GridHeaderDataMeta(id + "_fields.fieldElement", "元素名称", null, false, 70),
					new GridHeaderDataMeta(id + "_fields.fieldFormat", "格式",null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldLength", "长度",null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldPrecision", "精度",null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldCurrency","货币类型", null, false, 100),
					new GridHeaderDataMeta(id + "_fields.fieldDecimal", "10进制",null, false, 50),
					new GridHeaderDataMeta(id + "_fields.fieldGroup", "分组",null, false, 50),
				    new GridHeaderDataMeta(id + "_fields.fieldNullif", "空字符串",null, false, 100) 
					});
		
			XMLField[] outputFields = step.getOutputFields();
			if(outputFields != null && outputFields.length > 0){
				for(int i = 0; i < outputFields.length; i++){
					this.words.addRow(new Object[] {
							String.valueOf(i+1),
							new GridCellDataMeta(null,outputFields[i].getFieldName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,String.valueOf(outputFields[i].getType()),
									GridCellDataMeta.CELL_TYPE_SELECT),
							new GridCellDataMeta(null,outputFields[i].getElementName(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,outputFields[i].getFormat(),
									GridCellDataMeta.CELL_TYPE_INPUT),
						    new GridCellDataMeta(null, String.valueOf(outputFields[i].getLength()),
									GridCellDataMeta.CELL_TYPE_INPUT),
						    new GridCellDataMeta(null, String.valueOf(outputFields[i].getPrecision()),
									GridCellDataMeta.CELL_TYPE_INPUT),
						    new GridCellDataMeta(null,outputFields[i].getCurrencySymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,outputFields[i].getDecimalSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,outputFields[i].getGroupingSymbol(),
									GridCellDataMeta.CELL_TYPE_INPUT),
							new GridCellDataMeta(null,outputFields[i].getNullString(),
									GridCellDataMeta.CELL_TYPE_INPUT)
					});	
				}
			}
			
			this.words.setHasBottomBar(true);
			this.words.setHasAdd(true, true, "jQuery.imeta.steps.xmloutput.btn.wordsAdd");
			this.words.setHasDelete(true,true,"jQuery.imeta.parameter.fieldsDelete");
			this.words.setSingle(true);
			
			DivMeta div = new DivMeta(new NullSimpleFormDataMeta(), true);
			super.getGetfields("jQuery.imeta.steps.xmloutput.btn.getfields")
			.appendTo(div);
		
			this.meta.putTabContent(2, new BaseFormMeta[] { this.words , div});
			
			// 装载到form
			columnFormMeta.putFieldsetsContent(new BaseFormMeta[] { this.name,
					this.meta });

			// 确定，取消
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
