/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar.  
 * The Initial Developer is Samatar.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/

/* 
 * 
 * Created on 10-07-2007
 * 
 */

package com.panet.imeta.trans.steps.salesforceinput;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.encryption.Encr;
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

public class SalesforceInputMeta extends BaseStepMeta implements
		StepMetaInterface {
	public static final String STEP_ATTRIBUTE_TARGETURL = "targeturl";
	public static final String STEP_ATTRIBUTE_USERNAME = "username";
	public static final String STEP_ATTRIBUTE_PASSWORD = "password";
	public static final String STEP_ATTRIBUTE_ENCRYPTED = "Encrypted";
	public static final String STEP_ATTRIBUTE_MODULE = "module";
	public static final String STEP_ATTRIBUTE_CONDITION = "condition";
	public static final String STEP_ATTRIBUTE_INCLUDE_TARGETURL = "include_targeturl";
	public static final String STEP_ATTRIBUTE_TARGETURL_FIELD = "targeturl_field";
	public static final String STEP_ATTRIBUTE_INCLUDE_MODULE = "include_module";
	public static final String STEP_ATTRIBUTE_MODULE_FIELD = "module_field";
	public static final String STEP_ATTRIBUTE_INCLUDE_ROWNUM = "include_rownum";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_INCLUDE_SQL = "include_sql";
	public static final String STEP_ATTRIBUTE_SQL_FIELD = "sql_field";
	public static final String STEP_ATTRIBUTE_INCLUDE_TIMESTAMP = "include_Timestamp";
	public static final String STEP_ATTRIBUTE_TIMESTAMP_FIELD = "timestamp_field";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_TIMEOUT = "timeout";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_ATTRIBUT = "field_attribut";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";
	public static final String STEP_ATTRIBUTE_FIELD_REPEAT = "field_repeat";

	public String TargetDefaultURL = "https://www.salesforce.com/services/Soap/u/10.0";

	/** Flag indicating that we should include the generated SQL in the output */
	private boolean includeSQL;

	/** The name of the field in the output containing the generated SQL */
	private String sqlField;

	/** Flag indicating that we should include the server Timestamp in the output */
	private boolean includeTimestamp;

	/** The name of the field in the output containing the server Timestamp */
	private String timestampField;

	/** Flag indicating that we should include the filename in the output */
	private boolean includeTargetURL;

	/** The name of the field in the output containing the filename */
	private String targetURLField;

	/** Flag indicating that we should include the module in the output */
	private boolean includeModule;

	/** The name of the field in the output containing the module */
	private String moduleField;

	/** Flag indicating that a row number field should be included in the output */
	private boolean includeRowNumber;

	/** The name of the field in the output containing the row number */
	private String rowNumberField;

	/** The salesforce url */
	private String targeturl;

	/** The userName */
	private String username;

	/** The password */
	private String password;

	/** The module */
	private String module;

	/** The condition */
	private String condition;

	/** The time out */
	private String timeout;

	/** The maximum number or lines to read */
	private String rowLimit;

	/** The fields to return... */
	private SalesforceInputField inputFields[];

	public SalesforceInputMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the input fields.
	 */
	public SalesforceInputField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            The input fields to set.
	 */
	public void setInputFields(SalesforceInputField[] inputFields) {
		this.inputFields = inputFields;
	}

	/**
	 * @return Returns the UserName.
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * @param user_name
	 *            The UserNAme to set.
	 */
	public void setUserName(String user_name) {
		this.username = user_name;
	}

	/**
	 * @return Returns the Password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param passwd
	 *            The password to set.
	 */
	public void setPassword(String passwd) {
		this.password = passwd;
	}

	/**
	 * @return Returns the module.
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module
	 *            The module to set.
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return Returns the condition.
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            The condition to set.
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return Returns the targeturl.
	 */
	public String getTargetURL() {
		return targeturl;
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setTargetURL(String urlvalue) {
		this.targeturl = urlvalue;
	}

	/**
	 * @param TargetURLField
	 *            The TargetURLField to set.
	 */
	public void setTargetURLField(String TargetURLField) {
		this.targetURLField = TargetURLField;
	}

	/**
	 * @param sqlField
	 *            The sqlField to set.
	 */
	public void setSQLField(String sqlField) {
		this.sqlField = sqlField;
	}

	/**
	 * @param timestampField
	 *            The timestampField to set.
	 */
	public void setTimestampField(String timestampField) {
		this.timestampField = timestampField;
	}

	/**
	 * @param ModuleField
	 *            The ModuleField to set.
	 */
	public void setModuleField(String module_field) {
		this.moduleField = module_field;
	}

	/**
	 * @return Returns the includeTargetURL.
	 */
	public boolean includeTargetURL() {
		return includeTargetURL;
	}

	/**
	 * @return Returns the includeSQL.
	 */
	public boolean includeSQL() {
		return includeSQL;
	}

	/**
	 * @param includeSQL
	 *            to set.
	 */
	public void setIncludeSQL(boolean includeSQL) {
		this.includeSQL = includeSQL;
	}

	/**
	 * @return Returns the includeTimestamp.
	 */
	public boolean includeTimestamp() {
		return includeTimestamp;
	}

	/**
	 * @param includeTimestamp
	 *            to set.
	 */
	public void setIncludeTimestamp(boolean includeTimestamp) {
		this.includeTimestamp = includeTimestamp;
	}

	/**
	 * @return Returns the includeModule.
	 */
	public boolean includeModule() {
		return includeTargetURL;
	}

	/**
	 * @param includeTargetURL
	 *            The includeTargetURL to set.
	 */
	public void setIncludeTargetURL(boolean includeTargetURL) {
		this.includeTargetURL = includeTargetURL;
	}

	/**
	 * @param includeModule
	 *            The includeModule to set.
	 */
	public void setIncludeModule(boolean includemodule) {
		this.includeModule = includemodule;
	}

	/**
	 * @return Returns the includeRowNumber.
	 */
	public boolean includeRowNumber() {
		return includeRowNumber;
	}

	/**
	 * @param includeRowNumber
	 *            The includeRowNumber to set.
	 */
	public void setIncludeRowNumber(boolean includeRowNumber) {
		this.includeRowNumber = includeRowNumber;
	}

	/**
	 * @return Returns the rowLimit.
	 */
	public String getRowLimit() {
		return rowLimit;
	}

	/**
	 * @return Returns the TimeOut.
	 */
	public String getTimeOut() {
		return timeout;
	}

	/**
	 * @param rowLimit
	 *            The rowLimit to set.
	 */
	public void setRowLimit(String rowLimit) {
		this.rowLimit = rowLimit;
	}

	/**
	 * @param TimeOut
	 *            The TimeOut to set.
	 */
	public void setTimeOut(String TimeOut) {
		this.timeout = TimeOut;
	}

	/**
	 * @return Returns the rowNumberField.
	 */
	public String getRowNumberField() {
		return rowNumberField;
	}

	/**
	 * @return Returns the targetURLField.
	 */
	public String getTargetURLField() {
		return targetURLField;
	}

	/**
	 * @return Returns the sqlField.
	 */
	public String getSQLField() {
		return sqlField;
	}

	/**
	 * @return Returns the timestampField.
	 */
	public String getTimestampField() {
		return timestampField;
	}

	/**
	 * @return Returns the moduleField.
	 */
	public String getModuleField() {
		return moduleField;
	}

	/**
	 * @param rowNumberField
	 *            The rowNumberField to set.
	 */
	public void setRowNumberField(String rowNumberField) {
		this.rowNumberField = rowNumberField;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		SalesforceInputMeta retval = (SalesforceInputMeta) super.clone();

		int nrFields = inputFields.length;

		retval.allocate(nrFields);

		for (int i = 0; i < nrFields; i++) {
			if (inputFields[i] != null) {
				retval.inputFields[i] = (SalesforceInputField) inputFields[i]
						.clone();
			}
		}

		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    " + XMLHandler.addTagValue("targeturl", targeturl));
		retval.append("    " + XMLHandler.addTagValue("username", username));
		retval.append("    "
				+ XMLHandler.addTagValue("password", Encr
						.encryptPasswordIfNotUsingVariables(password), false));
		retval.append("    " + XMLHandler.addTagValue("module", module));
		retval.append("    " + XMLHandler.addTagValue("condition", condition));
		retval
				.append("    "
						+ XMLHandler.addTagValue("include_targeturl",
								includeTargetURL));
		retval.append("    "
				+ XMLHandler.addTagValue("targeturl_field", targetURLField));
		retval.append("    "
				+ XMLHandler.addTagValue("include_module", includeModule));
		retval.append("    "
				+ XMLHandler.addTagValue("module_field", moduleField));
		retval.append("    "
				+ XMLHandler.addTagValue("include_rownum", includeRowNumber));
		retval.append("    "
				+ XMLHandler.addTagValue("rownum_field", rowNumberField));
		retval.append("    "
				+ XMLHandler.addTagValue("include_sql", includeSQL));
		retval.append("    " + XMLHandler.addTagValue("sql_field", sqlField));
		retval
				.append("    "
						+ XMLHandler.addTagValue("include_Timestamp",
								includeTimestamp));
		retval.append("    "
				+ XMLHandler.addTagValue("timestamp_field", timestampField));

		retval.append("    <fields>" + Const.CR);
		for (int i = 0; i < inputFields.length; i++) {
			SalesforceInputField field = inputFields[i];
			retval.append(field.getXML());
		}
		retval.append("      </fields>" + Const.CR);
		retval.append("    " + XMLHandler.addTagValue("limit", rowLimit));
		retval.append("    " + XMLHandler.addTagValue("timeout", timeout));

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		targeturl = BaseStepMeta.parameterToString(p.get(id + ".targeturl"));
		username = BaseStepMeta.parameterToString(p.get(id + ".username"));
		password = BaseStepMeta.parameterToString(p.get(id + ".password"));
		module = BaseStepMeta.parameterToString(p.get(id + ".module"));
		condition = BaseStepMeta.parameterToString(p.get(id + ".condition"));
		includeTargetURL = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeTargetURL"));
		targetURLField = BaseStepMeta.parameterToString(p.get(id
				+ ".targetURLField"));
		includeModule = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeModule"));
		moduleField = BaseStepMeta
				.parameterToString(p.get(id + ".moduleField"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeRowNumber"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id
				+ ".rowNumberField"));
		includeSQL = BaseStepMeta.parameterToBoolean(p.get(id + ".includeSQL"));
		sqlField = BaseStepMeta.parameterToString(p.get(id + ".sqlField"));
		includeTimestamp = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeTimestamp"));
		timestampField = BaseStepMeta.parameterToString(p.get(id
				+ ".timestampField"));
		timeout = BaseStepMeta.parameterToString(p.get(id + ".timeout"));
		rowLimit = BaseStepMeta.parameterToString(p.get(id + ".rowLimit"));

		String[] name = p.get(id + "_fields.name");
		String[] fieldValue = p.get(id + "_fields.field");
		String[] type = p.get(id + "_fields.type");
		String[] format = p.get(id + "_fields.format");
		String[] length = p.get(id + "_fields.length");
		String[] precision = p.get(id + "_fields.precision");
		String[] currency_symbol = p.get(id + "_fields.currencySymbol");
		String[] decimal_symbol = p.get(id + "_fields.decimalSymbol");
		String[] group_symbol = p.get(id + "_fields.groupSymbol");
		String[] trimtype = p.get(id + "_fields.trimType");
		String[] repeat = p.get(id + "_fields.repeated");

		if (name != null && name.length > 0) {
			this.inputFields = new SalesforceInputField[name.length];
			for (int i = 0; i < name.length; i++) {
				this.inputFields[i] = new SalesforceInputField(name[i]);
				this.inputFields[i].setField(fieldValue[i]);
				this.inputFields[i].setType(Const.toInt(type[i], 0));
				this.inputFields[i].setFormat(format[i]);
				this.inputFields[i].setLength(Const.toInt(length[i], -1));
				this.inputFields[i].setPrecision(Const.toInt(precision[i], -1));
				this.inputFields[i].setCurrencySymbol(currency_symbol[i]);
				this.inputFields[i].setDecimalSymbol(decimal_symbol[i]);
				this.inputFields[i].setGroupSymbol(group_symbol[i]);
				this.inputFields[i].setTrimType(Const.toInt(trimtype[i], -1));
				this.inputFields[i]
						.setRepeated(Boolean.parseBoolean(repeat[i]));
			}
		} else {
			this.inputFields = new SalesforceInputField[0];
		}
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			targeturl = XMLHandler.getTagValue(stepnode, "targeturl");
			username = XMLHandler.getTagValue(stepnode, "username");
			password = XMLHandler.getTagValue(stepnode, "password");
			if (password != null && password.startsWith("Encrypted")) {
				password = Encr.decryptPassword(password.replace("Encrypted",
						"").replace(" ", ""));
			}

			module = XMLHandler.getTagValue(stepnode, "module");
			condition = XMLHandler.getTagValue(stepnode, "condition");
			includeTargetURL = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include_targeturl"));
			targetURLField = XMLHandler
					.getTagValue(stepnode, "targeturl_field");
			includeModule = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include_module"));
			moduleField = XMLHandler.getTagValue(stepnode, "module_field");
			includeRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include_rownum"));
			rowNumberField = XMLHandler.getTagValue(stepnode, "rownum_field");
			includeSQL = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"include_sql"));
			sqlField = XMLHandler.getTagValue(stepnode, "targetsql_field");
			includeTimestamp = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include_Timestamp"));
			timestampField = XMLHandler
					.getTagValue(stepnode, "timestamp_field");

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrFields = XMLHandler.countNodes(fields, "field");

			allocate(nrFields);

			for (int i = 0; i < nrFields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				SalesforceInputField field = new SalesforceInputField(fnode);
				inputFields[i] = field;
			}
			timeout = XMLHandler.getTagValue(stepnode, "timeout");
			// Is there a limit on the number of rows we process?
			rowLimit = XMLHandler.getTagValue(stepnode, "limit");
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void allocate(int nrfields) {
		inputFields = new SalesforceInputField[nrfields];
	}

	public void setDefault() {
		targeturl = TargetDefaultURL;
		password = "";
		module = "Account";
		condition = "";
		includeTargetURL = false;
		targetURLField = "";
		includeModule = false;
		moduleField = "";
		includeRowNumber = false;
		rowNumberField = "";
		includeSQL = false;
		sqlField = "";
		includeTimestamp = false;
		timestampField = "";
		int nrFields = 0;
		allocate(nrFields);

		for (int i = 0; i < nrFields; i++) {
			inputFields[i] = new SalesforceInputField("field" + (i + 1));
		}

		rowLimit = "0";
		timeout = "60000";
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface info[], StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		int i;
		for (i = 0; i < inputFields.length; i++) {
			SalesforceInputField field = inputFields[i];

			int type = field.getType();
			if (type == ValueMeta.TYPE_NONE)
				type = ValueMeta.TYPE_STRING;
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(field.getName()), type);
			v.setLength(field.getLength());
			v.setPrecision(field.getPrecision());
			v.setOrigin(name);
			v.setConversionMask(field.getFormat());
			v.setDecimalSymbol(field.getDecimalSymbol());
			v.setGroupingSymbol(field.getGroupSymbol());
			v.setCurrencySymbol(field.getCurrencySymbol());
			r.addValueMeta(v);
		}

		if (includeTargetURL && !Const.isEmpty(targetURLField)) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(targetURLField),
					ValueMeta.TYPE_STRING);
			v.setLength(250);
			v.setPrecision(-1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
		if (includeModule && !Const.isEmpty(moduleField)) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(moduleField), ValueMeta.TYPE_STRING);
			v.setLength(250);
			v.setPrecision(-1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}

		if (includeSQL && !Const.isEmpty(sqlField)) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(sqlField), ValueMeta.TYPE_STRING);
			v.setLength(250);
			v.setPrecision(-1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
		if (includeTimestamp && !Const.isEmpty(timestampField)) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(timestampField),
					ValueMeta.TYPE_STRING);
			v.setLength(250);
			v.setPrecision(-1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}

		if (includeRowNumber && !Const.isEmpty(rowNumberField)) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(rowNumberField),
					ValueMeta.TYPE_INTEGER);
			v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			targeturl = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_TARGETURL);

			// H.kawaguchi Add 19-01-2009
			username = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_USERNAME);
			password = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_PASSWORD);
			if (password != null
					&& password.startsWith(STEP_ATTRIBUTE_ENCRYPTED)) {
				password = Encr.decryptPassword(password.replace(
						STEP_ATTRIBUTE_ENCRYPTED, "").replace(" ", ""));
			}
			// H.kawaguchi Add 19-01-2009

			module = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_MODULE);

			// H.kawaguchi Add 19-01-2009
			condition = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_CONDITION);
			// H.kawaguchi Add 19-01-2009

			includeTargetURL = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_TARGETURL);
			targetURLField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_TARGETURL_FIELD);
			includeModule = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_MODULE);
			moduleField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_MODULE_FIELD);
			includeRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_ROWNUM);
			rowNumberField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			includeSQL = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_SQL);
			sqlField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_SQL_FIELD);
			includeTimestamp = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_TIMESTAMP);
			timestampField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_TIMESTAMP_FIELD);
			rowLimit = rep
					.getStepAttributeString(id_step, STEP_ATTRIBUTE_LIMIT);
			timeout = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_TIMEOUT);
			int nrFields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrFields);

			for (int i = 0; i < nrFields; i++) {
				SalesforceInputField field = new SalesforceInputField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				field.setField(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_ATTRIBUT));
				field.setType(ValueMeta.getType(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_TYPE)));
				field.setFormat(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_FORMAT));
				field.setCurrencySymbol(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_CURRENCY));
				field.setDecimalSymbol(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_DECIMAL));
				field.setGroupSymbol(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_GROUP));
				field.setLength((int) rep.getStepAttributeInteger(id_step, i,
						STEP_ATTRIBUTE_FIELD_LENGTH));
				field.setPrecision((int) rep.getStepAttributeInteger(id_step,
						i, STEP_ATTRIBUTE_FIELD_PRECISION));
				field.setTrimType(SalesforceInputField.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setRepeated(rep.getStepAttributeBoolean(id_step, i,
						STEP_ATTRIBUTE_FIELD_REPEAT));

				inputFields[i] = field;
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("SalesforceInputMeta.Exception.ErrorReadingRepository"),
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "targeturl",
					targeturl);

			// H.kawaguchi Add 19-01-2009
			rep.saveStepAttribute(id_transformation, id_step, "username",
					username);
			rep.saveStepAttribute(id_transformation, id_step, "password", Encr
					.encryptPasswordIfNotUsingVariables(password));
			// H.kawaguchi Add 19-01-2009

			rep.saveStepAttribute(id_transformation, id_step, "module", module);

			// H.kawaguchi Add 19-01-2009
			rep.saveStepAttribute(id_transformation, id_step, "condition",
					condition);
			// H.kawaguchi Add 19-01-2009

			rep.saveStepAttribute(id_transformation, id_step,
					"include_targeturl", includeTargetURL);
			rep.saveStepAttribute(id_transformation, id_step,
					"targeturl_field", targetURLField);
			rep.saveStepAttribute(id_transformation, id_step, "include_module",
					includeModule);
			rep.saveStepAttribute(id_transformation, id_step, "module_field",
					moduleField);
			rep.saveStepAttribute(id_transformation, id_step, "include_rownum",
					includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "include_sql",
					includeSQL);
			rep.saveStepAttribute(id_transformation, id_step, "sql_field",
					sqlField);
			rep.saveStepAttribute(id_transformation, id_step,
					"include_Timestamp", includeTimestamp);
			rep.saveStepAttribute(id_transformation, id_step,
					"timestamp_field", timestampField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumberField);
			rep
					.saveStepAttribute(id_transformation, id_step, "limit",
							rowLimit);
			rep.saveStepAttribute(id_transformation, id_step, "timeout",
					timeout);

			for (int i = 0; i < inputFields.length; i++) {
				SalesforceInputField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());

				// H.kawaguchi Bug Fix 17-01-2009
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_attribut", field.getField());
				// H.kawaguchi Bug Fix 17-01-2009

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_type", field.getTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_format", field.getFormat());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_currency", field.getCurrencySymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_decimal", field.getDecimalSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_group", field.getGroupSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_length", field.getLength());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_precision", field.getPrecision());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_trim_type", field.getTrimTypeCode());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_repeat", field.isRepeated());

			}
		} catch (Exception e) {
			throw new KettleException(Messages.getString(
					"SalesforceInputMeta.Exception.ErrorSavingToRepository", ""
							+ id_step), e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we get input...
		if (input.length > 0)
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("SalesforceInputMeta.CheckResult.NoInputExpected"),
					stepMeta);
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("SalesforceInputMeta.CheckResult.NoInput"),
					stepMeta);
		remarks.add(cr);

		// check URL
		if (Const.isEmpty(targeturl))
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("SalesforceInputMeta.CheckResult.NoURL"),
					stepMeta);
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("SalesforceInputMeta.CheckResult.URLOk"),
					stepMeta);
		remarks.add(cr);

		// check username
		if (Const.isEmpty(username))
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("SalesforceInputMeta.CheckResult.NoUsername"),
					stepMeta);
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("SalesforceInputMeta.CheckResult.UsernameOk"),
					stepMeta);
		remarks.add(cr);

		// check module
		if (Const.isEmpty(module))
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("SalesforceInputMeta.CheckResult.NoModule"),
					stepMeta);
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("SalesforceInputMeta.CheckResult.ModuleOk"),
					stepMeta);
		remarks.add(cr);

		// check return fields
		if (inputFields.length == 0)
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("SalesforceInputMeta.CheckResult.NoFields"),
					stepMeta);
		else
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("SalesforceInputMeta.CheckResult.FieldsOk"),
					stepMeta);
		remarks.add(cr);

		// check additionals fields
		if (includeTargetURL && Const.isEmpty(targetURLField)) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("SalesforceInputMeta.CheckResult.NoTargetURLField"),
					stepMeta);
			remarks.add(cr);
		}
		if (includeSQL && Const.isEmpty(sqlField)) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("SalesforceInputMeta.CheckResult.NoSQLField"),
					stepMeta);
			remarks.add(cr);
		}
		if (includeModule && Const.isEmpty(moduleField)) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("SalesforceInputMeta.CheckResult.NoModuleField"),
					stepMeta);
			remarks.add(cr);
		}
		if (includeTimestamp && Const.isEmpty(timestampField)) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("SalesforceInputMeta.CheckResult.NoTimestampField"),
					stepMeta);
			remarks.add(cr);
		}
		if (includeRowNumber && Const.isEmpty(rowNumberField)) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("SalesforceInputMeta.CheckResult.NoRowNumberField"),
					stepMeta);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new SalesforceInput(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new SalesforceInputData();
	}
}
