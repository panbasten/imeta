package com.panet.imeta.trans.step;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.panet.iform.core.base.ButtonMeta;
import com.panet.iform.core.base.OptionDataMeta;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.repository.RepositoryObject;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.steps.databaselookup.DatabaseLookupMeta;

public class BaseStepDialog implements Cloneable {

	public static String dats[] = Const.getDateFormats();

	public static String nums[] = Const.getNumberFormats();

	private String id;

	private TransMeta transMeta;

	/**
	 * step的元模型
	 */
	private StepMeta stepMeta;

	/**
	 * 步骤名称
	 */
	private String stepName;

	private long stepId;

	/**
	 * 确定按钮,取消按钮
	 */
	private ButtonMeta okBtn, cancelBtn;

	/**
	 * 获得字段按钮
	 */
	private ButtonMeta getfields;

	protected Repository rep;

	public TransMeta getTransMeta() {
		return transMeta;
	}

	public void setTransMeta(TransMeta transMeta) {
		this.transMeta = transMeta;
	}

	public StepMetaInterface getStep() {
		return this.stepMeta.getStepMetaInterface();
	}

	public long getStepAttributeInteger(String code) throws KettleException {
		if (stepId <= 0)
			return 0;
		return rep.getStepAttributeInteger(stepId, code);
	}

	public String getStepAttributeString(String code) throws KettleException {
		if (stepId <= 0)
			return "";
		return rep.getStepAttributeString(stepId, code);
	}

	public boolean getStepAttributeBoolean(String code) throws KettleException {
		if (stepId <= 0)
			return false;
		return rep.getStepAttributeBoolean(stepId, code);
	}

	public int countNrStepAttributes(String code) throws KettleException {
		if (stepId <= 0)
			return 0;
		return rep.countNrStepAttributes(stepId, code);
	}

	/**
	 * 得到该步骤之前的所有步骤名
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getPrevStepNames() {
		return getPrevStepNames(false);
	}

	public List<OptionDataMeta> getPrevStepNames(boolean hasEmpty) {
		List<OptionDataMeta> prevStepNames = new ArrayList<OptionDataMeta>();
		StepMeta stepMeta;
		if (hasEmpty) {
			prevStepNames.add(new OptionDataMeta("", ""));
		}
		for (int i = 0; i < transMeta.findNrPrevSteps(this.stepName, true); i++) {
			stepMeta = transMeta.findPrevStep(this.stepName, i, true);
			prevStepNames.add(new OptionDataMeta(stepMeta.getName(), stepMeta
					.getName()));
		}
		return prevStepNames;
	}

	/**
	 * 获得SQL比较符选择项
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getSqlConditions() {
		List<OptionDataMeta> opt = new ArrayList<OptionDataMeta>();

		for (String field : DatabaseLookupMeta.conditionStrings) {
			opt.add(new OptionDataMeta(field, field));
		}

		return opt;
	}

	/**
	 * 得到后续的所有节点
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getNextStepNames() {
		List<OptionDataMeta> prevStepNames = new ArrayList<OptionDataMeta>();
		StepMeta stepMeta;
		for (int i = 0; i < transMeta.findNrNextSteps(this.stepMeta); i++) {
			stepMeta = transMeta.findNextStep(this.stepMeta, i);
			prevStepNames.add(new OptionDataMeta(stepMeta.getName(), stepMeta
					.getName()));
		}
		return prevStepNames;
	}

	/**
	 * 得到值类型
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getValueType() {
		List<OptionDataMeta> valueType = new ArrayList<OptionDataMeta>();
		for (String vt : ValueMeta.typeCodes) {
			valueType.add(new OptionDataMeta(vt, vt));
		}
		return valueType;
	}

	/**
	 * 得到值类型，key值为顺序号
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getValueTypeByIndex() {
		List<OptionDataMeta> valueType = new ArrayList<OptionDataMeta>();
		for (int i = 0; i < ValueMeta.typeCodes.length; i++) {
			valueType.add(new OptionDataMeta(String.valueOf(i),
					ValueMeta.typeCodes[i]));
		}
		return valueType;
	}

	/**
	 * 得到数据库连接的列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getConnectionLine() {
		List<OptionDataMeta> conn = new ArrayList<OptionDataMeta>();
		DatabaseMeta ci;

		for (int i = 0; i < transMeta.nrDatabases(); i++) {
			ci = transMeta.getDatabase(i);
			conn.add(new OptionDataMeta(String.valueOf(ci.getID()), ci
					.getName()));
		}
		return conn;
	}

	/**
	 * 得到编码列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getEncoding() {
		List<OptionDataMeta> encoding = new ArrayList<OptionDataMeta>();
		List<Charset> values = new ArrayList<Charset>(Charset
				.availableCharsets().values());
		for (int i = 0; i < values.size(); i++) {
			Charset charSet = (Charset) values.get(i);
			encoding.add(new OptionDataMeta(charSet.name(), charSet
					.displayName()));
		}
		return encoding;
	}

	/**
	 * 得到日志级别列表
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getLoglevel() {
		List<OptionDataMeta> loglevel = new ArrayList<OptionDataMeta>();
		for (int i = 0; i < LogWriter.log_level_desc_long.length; i++) {
			loglevel.add(new OptionDataMeta(String.valueOf(i),
					LogWriter.log_level_desc_long[i]));
		}
		return loglevel;
	}

	/**
	 * 得到日期格式
	 * 
	 * @return
	 */
	public List<OptionDataMeta> getDateFormats() {
		List<OptionDataMeta> dateFormat = new ArrayList<OptionDataMeta>();
		for (String date : dats) {
			dateFormat.add(new OptionDataMeta(date, date));
		}
		return dateFormat;
	}

	public StepMeta getStepMeta() {
		return stepMeta;
	}

	public void setStepMeta(StepMeta stepMeta) {
		this.stepMeta = stepMeta;
	}

	public void setRepository(Repository repository) {
		this.rep = repository;
	}

	public BaseStepDialog(StepMeta stepMeta, TransMeta transMeta) {
		this.stepMeta = stepMeta;
		this.transMeta = transMeta;
		this.stepName = stepMeta.getName();
		this.stepId = stepMeta.getID();
		this.rep = transMeta.getRepository();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStepName() {
		return stepName;
	}

	public long getStepId() {
		return stepId;
	}

	public ButtonMeta getOkBtn() {
		this.okBtn = new ButtonMeta(id + ".btn.ok", id + ".btn.ok", "确定", "确定");
		this.okBtn.putProperty("transName", this.transMeta.getName());
		this.okBtn.putProperty("stepName", this.stepName);
		this.okBtn.putProperty("directoryId", this.transMeta.getDirectory()
				.getID());
		this.okBtn.addClick("jQuery.imeta.steps.btn.ok");
		return this.okBtn;
	}

	public ButtonMeta getCancelBtn() {
		this.cancelBtn = new ButtonMeta(id + ".btn.cancel", id + ".btn.cancel",
				"取消", "取消");
		this.cancelBtn.addClick("jQuery.imeta.steps.btn.cancel");
		return this.cancelBtn;
	}

	public List<OptionDataMeta> getOptionListByStringArrayWithNumber(
			String[] arr, boolean hasEmpty) {
		List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
		if (hasEmpty) {
			options.add(new OptionDataMeta("", ""));
		}
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				options.add(new OptionDataMeta(String.valueOf(i), arr[i]));
			}
		}
		return options;
	}

	public List<OptionDataMeta> getOptionListByStringArray(String[] arr,
			boolean hasEmpty) {
		List<OptionDataMeta> options = new ArrayList<OptionDataMeta>();
		if (hasEmpty) {
			options.add(new OptionDataMeta("", ""));
		}
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				options.add(new OptionDataMeta(arr[i], arr[i]));
			}
		}
		return options;
	}

	/**
	 * 得到之前步骤的结果字段
	 * 
	 * @return
	 * @throws KettleException
	 */
	public List<OptionDataMeta> getPrevStepResultFieldsWithNumber()
			throws KettleException {
		List<OptionDataMeta> resultFieldOptions = new ArrayList<OptionDataMeta>();
		try {
			RowMetaInterface r = transMeta.getPrevStepFields(stepName);
			if (r != null) {
				resultFieldOptions.add(new OptionDataMeta("", ""));
				String[] resultNames = r.getFieldNames();
				for (int i = 0; i < resultNames.length; i++) {
					resultFieldOptions.add(new OptionDataMeta(
							String.valueOf(i), resultNames[i]));
				}
			}
			return resultFieldOptions;
		} catch (KettleException ke) {
			throw new KettleException(ke.getMessage(), ke);
		}
	}

	/**
	 * 得到之前步骤的结果字段
	 * 
	 * @return
	 * @throws KettleException
	 */
	public List<OptionDataMeta> getPrevStepResultFields()
			throws KettleException {
		List<OptionDataMeta> resultFieldOptions = new ArrayList<OptionDataMeta>();
		try {
			RowMetaInterface r = transMeta.getPrevStepFields(stepName);
			if (r != null) {
				String[] resultNames = r.getFieldNames();
				resultFieldOptions.add(new OptionDataMeta("", ""));
				for (int i = 0; i < resultNames.length; i++) {
					resultFieldOptions.add(new OptionDataMeta(resultNames[i],
							resultNames[i]));
				}
			}
			return resultFieldOptions;
		} catch (KettleException ke) {
			throw new KettleException(ke.getMessage(), ke);
		}
	}

	public String getOptionsByStringArrayWithNumberValue(String[] arr,
			boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				rtn.append(i);
				rtn.append(":");
				rtn.append(arr[i]);
				rtn.append(";");
			}
		}
		return rtn.toString();
	}

	public String getOptionsByStringArrayWithBooleanValue(String[] arr,
			boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equalsIgnoreCase("true")
						|| arr[i].equalsIgnoreCase("on") || arr[i].equals("是"))
					rtn.append("true");
				else if (arr[i].equalsIgnoreCase("false")
						|| arr[i].equalsIgnoreCase("off") || arr[i].equals("否"))
					rtn.append("false");
				rtn.append(":");
				rtn.append(arr[i]);
				rtn.append(";");
			}
		}
		return rtn.toString();
	}

	public String getOptionsByTrueAndFalse(boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		rtn.append("true:是;false:否;");
		return rtn.toString();
	}

	public String getOptionsByYAndN(boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		rtn.append("true:Y;false:N;");
		return rtn.toString();
	}

	public String getOptionsByStringArray(String[] arr, boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		if (arr != null && arr.length > 0) {
			for (String o : arr) {
				rtn.append(o);
				rtn.append(":");
				rtn.append(o);
				rtn.append(";");
			}
		}
		return rtn.toString();
	}

	public String getOptionsByList(List<OptionDataMeta> list, boolean hasEmpty) {
		StringBuffer rtn = new StringBuffer();
		if (hasEmpty) {
			rtn.append(":;");
		}
		if (list != null && list.size() > 0) {
			for (OptionDataMeta o : list) {
				rtn.append(o.getValue());
				rtn.append(":");
				rtn.append(o.getText());
				rtn.append(";");
			}
		}
		return rtn.toString();
	}

	public ButtonMeta getGetfields(String clickFn) {
		this.getfields = new ButtonMeta(id + ".btn.getfields", id
				+ ".btn.getfields", "获取字段", "获取字段");
		this.getfields.addClick(clickFn);
		this.getfields.putProperty("roType",
				RepositoryObject.STRING_OBJECT_TYPE_TRANSFORMATION);
		this.getfields.putProperty("roName", transMeta.getName());
		this.getfields.putProperty("elementName", stepName);
		this.getfields.putProperty("directoryId", transMeta.getDirectory()
				.getID());
		return getfields;
	}

}
