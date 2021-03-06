/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */

package com.panet.imeta.trans.steps.randomvalue;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
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
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Created on 08-07-2008
 */
public class RandomValueMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";

	public final static int TYPE_RANDOM_NONE = 0;

	public final static int TYPE_RANDOM_NUMBER = 1;

	public final static int TYPE_RANDOM_INTEGER = 2;

	public final static int TYPE_RANDOM_STRING = 3;

	public final static int TYPE_RANDOM_UUID = 4;

	public final static int TYPE_RANDOM_UUID4 = 5;

	public static final RandomValueMetaFunction functions[] = new RandomValueMetaFunction[] {
			null,
			new RandomValueMetaFunction(TYPE_RANDOM_NUMBER, "random number",
					Messages.getString("RandomValueMeta.TypeDesc.RandomNumber")),
			new RandomValueMetaFunction(
					TYPE_RANDOM_INTEGER,
					"random integer",
					Messages
							.getString("RandomValueMeta.TypeDesc.RandomInteger")),
			new RandomValueMetaFunction(TYPE_RANDOM_STRING, "random string",
					Messages.getString("RandomValueMeta.TypeDesc.RandomString")),
			new RandomValueMetaFunction(TYPE_RANDOM_UUID, "random uuid",
					Messages.getString("RandomValueMeta.TypeDesc.RandomUUID")),
			new RandomValueMetaFunction(TYPE_RANDOM_UUID4, "random uuid4",
					Messages.getString("RandomValueMeta.TypeDesc.RandomUUID4")) };

	public static final String[] fieldTypeDesc = new String[] { "",
			functions[1].getDescription(), functions[2].getDescription(),
			functions[3].getDescription(), functions[4].getDescription(),
			functions[5].getDescription(), };

	private String fieldName[];

	private int fieldType[];

	public RandomValueMeta() {
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

	/**
	 * @return Returns the fieldType.
	 */
	public int[] getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType
	 *            The fieldType to set.
	 */
	public void setFieldType(int[] fieldType) {
		this.fieldType = fieldType;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public void allocate(int count) {
		fieldName = new String[count];
		fieldType = new int[count];
	}

	public Object clone() {
		RandomValueMeta retval = (RandomValueMeta) super.clone();

		int count = fieldName.length;

		retval.allocate(count);

		for (int i = 0; i < count; i++) {
			retval.fieldName[i] = fieldName[i];
			retval.fieldType[i] = fieldType[i];
		}

		return retval;
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		String[] fieldName = p.get(id + "_fieldsList.fieldName");
		String[] fieldType = p.get(id + "_fieldsList.fieldType");
		this.fieldName = (fieldName != null) ? fieldName : (new String[0]);
		this.fieldType = (fieldType != null) ? stringArrayToIntArray(fieldType,
				0) : (new int[0]);
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int count = XMLHandler.countNodes(fields, "field");
			String type;

			allocate(count);

			for (int i = 0; i < count; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				fieldName[i] = XMLHandler.getTagValue(fnode, "name");
				type = XMLHandler.getTagValue(fnode, "type");
				fieldType[i] = getType(type);
			}
		} catch (Exception e) {
			throw new KettleXMLException(
					"Unable to read step information from XML", e);
		}
	}

	public static final int getType(String type) {
		for (int i = 1; i < functions.length; i++) {
			if (functions[i].getCode().equalsIgnoreCase(type))
				return i;
			if (functions[i].getDescription().equalsIgnoreCase(type))
				return i;
		}
		return 0;
	}

	public static final String getTypeDesc(int t) {
		if (functions == null || functions.length == 0)
			return null;
		if (t < 0 || t >= functions.length || functions[t] == null)
			return null;
		return functions[t].getDescription();
	}

	public void setDefault() {
		int count = 0;

		allocate(count);

		for (int i = 0; i < count; i++) {
			fieldName[i] = "field" + i;
			fieldType[i] = TYPE_RANDOM_NUMBER;
		}
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		for (int i = 0; i < fieldName.length; i++) {
			ValueMetaInterface v;

			switch (fieldType[i]) {
			case TYPE_RANDOM_NUMBER:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_NUMBER);
				break;
			case TYPE_RANDOM_INTEGER:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_INTEGER);
				break;
			case TYPE_RANDOM_STRING:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_STRING);
				break;
			case TYPE_RANDOM_UUID:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_STRING);
				break;
			case TYPE_RANDOM_UUID4:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_STRING);
				break;
			default:
				v = new ValueMeta(fieldName[i], ValueMetaInterface.TYPE_NONE);
				break;
			}
			v.setOrigin(name);
			row.addValueMeta(v);
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(200);

		retval.append("    <fields>").append(Const.CR);

		for (int i = 0; i < fieldName.length; i++) {
			retval.append("      <field>").append(Const.CR);
			retval.append("        ").append(
					XMLHandler.addTagValue("name", fieldName[i]));
			retval
					.append("        ")
					.append(
							XMLHandler
									.addTagValue(
											"type",
											functions[fieldType[i]] != null ? functions[fieldType[i]]
													.getCode()
													: ""));
			retval.append("      </field>").append(Const.CR);
		}
		retval.append("    </fields>" + Const.CR);

		return retval.toString();
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			int nrfields = rep.countNrStepAttributes(id_step, "field_name");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				fieldName[i] = rep.getStepAttributeString(id_step, i,
						"field_name");
				fieldType[i] = getType(rep.getStepAttributeString(id_step, i,
						"field_type"));
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
			for (int i = 0; i < fieldName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", fieldName[i]);
				rep
						.saveStepAttribute(
								id_transformation,
								id_step,
								i,
								"field_type",
								functions[fieldType[i]] != null ? functions[fieldType[i]]
										.getCode()
										: "");
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
		// See if we have input streams leading to this step!
		int nrRemarks = remarks.size();
		for (int i = 0; i < fieldName.length; i++) {
			if (fieldType[i] <= TYPE_RANDOM_NONE) {
				CheckResult cr = new CheckResult(
						CheckResultInterface.TYPE_RESULT_ERROR,
						Messages.getString(
								"RandomValueMeta.CheckResult.FieldHasNoType",
								fieldName[i]), stepMeta);
				remarks.add(cr);
			}
		}
		if (remarks.size() == nrRemarks) {
			CheckResult cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("RandomValueMeta.CheckResult.AllTypesSpecified"),
					stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new RandomValue(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new RandomValueData();
	}
}
