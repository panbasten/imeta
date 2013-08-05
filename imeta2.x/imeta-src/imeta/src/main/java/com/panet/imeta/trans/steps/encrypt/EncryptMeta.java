package com.panet.imeta.trans.steps.encrypt;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.annotations.Step;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

@Step(name = "Encrypt", image = "ECT.png", tooltip = "BaseStep.TypeTooltipDesc.Encrypt", description = "BaseStep.TypeLongDesc.Encrypt", category = StepCategory.CATEGORY_UTILITY)
public class EncryptMeta extends BaseStepMeta implements StepMetaInterface {
	private long encryptType;
	private String key;
	private String encoding;

	// 加密解密源字段
	private String fieldName[];

	// 加密解密目的字段
	private String newFieldName[];

	// 加密解密操作类型
	private long optionType[];

	public EncryptMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the fieldName.
	 */
	public String[] getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            The fieldName to set.
	 */
	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public void allocate(int nrfields) {
		fieldName = new String[nrfields];
		newFieldName = new String[nrfields];
		optionType = new long[nrfields];
	}

	public Object clone() {
		EncryptMeta retval = (EncryptMeta) super.clone();

		retval.encryptType = encryptType;
		retval.key = key;
		retval.encoding = encoding;

		int nrfields = fieldName.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.fieldName[i] = fieldName[i];
			retval.newFieldName[i] = newFieldName[i];
			retval.optionType[i] = optionType[i];
		}

		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			encryptType = Const.toInt(XMLHandler.getTagValue(stepnode,
					"encryptType"), 0);
			key = XMLHandler.getTagValue(stepnode, "key");
			encoding = XMLHandler.getTagValue(stepnode, "encoding");

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				fieldName[i] = XMLHandler.getTagValue(fnode, "fieldName");
				newFieldName[i] = XMLHandler.getTagValue(fnode, "newFieldName");
				optionType[i] = Const.toInt(XMLHandler.getTagValue(fnode,
						"optionType"), 0);
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault() {
		int i, nrfields = 0;

		allocate(nrfields);

		for (i = 0; i < nrfields; i++) {
			fieldName[i] = "field" + i;
			newFieldName[i] = "field" + i;
			optionType[i] = 0;
		}

	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(300);
		retval.append("    ").append(
				XMLHandler.addTagValue("encryptType", encryptType));
		retval.append("    ").append(XMLHandler.addTagValue("key", key));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < fieldName.length; i++) {
			if (fieldName[i] != null && fieldName[i].length() != 0) {
				retval.append("      <field>").append(Const.CR);
				retval.append("        ").append(
						XMLHandler.addTagValue("fieldName", fieldName[i]));
				retval.append("        ")
						.append(
								XMLHandler.addTagValue("newFieldName",
										newFieldName[i]));
				retval.append("        ").append(
						XMLHandler.addTagValue("optionType", optionType[i]));
				retval.append("      </field>").append(Const.CR);
			}
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		this.encryptType = BaseStepMeta
				.parameterToLong(p.get(id + ".encryptType"));
		this.key = BaseStepMeta.parameterToString(p.get(id + ".key"));
		this.encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));

		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] newFieldName = p.get(id + "_fields.newFieldName");
		String[] optionType = p.get(id + "_fields.optionType");

		this.fieldName = (fieldName != null) ? fieldName : (new String[0]);
		this.newFieldName = (newFieldName != null) ? newFieldName
				: (new String[0]);
		this.optionType = (optionType != null) ? super.stringArrayToLongArray(
				optionType, 0L) : (new long[0]);

	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			encryptType = rep.getStepAttributeInteger(id_step, "encryptType");
			key = rep.getStepAttributeString(id_step, "key");
			encoding = rep.getStepAttributeString(id_step, "encoding");

			int nrfields = rep.countNrStepAttributes(id_step, "field_name");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				fieldName[i] = rep.getStepAttributeString(id_step, i,
						"field_name");
				newFieldName[i] = rep.getStepAttributeString(id_step, i,
						"new_field_name");
				optionType[i] = rep.getStepAttributeInteger(id_step, i,
						"optionType");
			}
		} catch (Exception e) {
			throw new KettleException(
					"Unexpected error reading step information from the repository",
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "encryptType",
					encryptType);
			rep.saveStepAttribute(id_transformation, id_step, "key", key);
			rep.saveStepAttribute(id_transformation, id_step, "encoding",
					encoding);

			for (int i = 0; i < fieldName.length; i++) {
				if (fieldName[i] != null && fieldName[i].length() != 0) {
					rep.saveStepAttribute(id_transformation, id_step, i,
							"field_name", fieldName[i]);
					rep.saveStepAttribute(id_transformation, id_step, i,
							"new_field_name", newFieldName[i]);
					rep.saveStepAttribute(id_transformation, id_step, i,
							"optionType", optionType[i]);
				}
			}
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev != null && prev.size() > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("ConstantMeta.CheckResult.FieldsReceived", ""
							+ prev.size()), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					Messages.getString("ConstantMeta.CheckResult.NoFields"),
					stepMeta);
			remarks.add(cr);
		}
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		// 在行记录中增加加密解密后字段的元数据
		for (int i = 0; i < newFieldName.length; i++) {
			ValueMetaInterface v = new ValueMeta(newFieldName[i],
					ValueMetaInterface.TYPE_STRING);
			v.setOrigin(name);
			row.addValueMeta(v);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new Encrypt(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData() {
		return new EncryptData();
	}

	public long getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(long encryptType) {
		this.encryptType = encryptType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String[] getNewFieldName() {
		return newFieldName;
	}

	public void setNewFieldName(String[] newFieldName) {
		this.newFieldName = newFieldName;
	}

	public long[] getOptionType() {
		return optionType;
	}

	public void setOptionType(long[] optionType) {
		this.optionType = optionType;
	}
}
