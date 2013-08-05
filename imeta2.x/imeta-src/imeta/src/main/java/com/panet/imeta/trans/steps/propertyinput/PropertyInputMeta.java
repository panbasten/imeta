/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar, Brahim and is provided under the terms 
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
 * Created on 24-03-2008
 *
 */
package com.panet.imeta.trans.steps.propertyinput;

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
import com.panet.imeta.core.fileinput.FileInputList;
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

public class PropertyInputMeta extends BaseStepMeta implements
		StepMetaInterface {
	public static final String STEP_ATTRIBUTE_INCLUDE = "include";
	public static final String STEP_ATTRIBUTE_INCLUDE_FIELD = "include_field";
	public static final String STEP_ATTRIBUTE_FILENAME_FIELD = "filename_Field";
	public static final String STEP_ATTRIBUTE_ROWNUM = "rownum";
	public static final String STEP_ATTRIBUTE_ISADDRESULT = "isaddresult";
	public static final String STEP_ATTRIBUTE_FILEFIELD = "filefield";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_RESET_ROWNUMBER = "reset_rownumber";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	// public static final String STEP_ATTRIBUTE_file_name = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_MASK = "file_mask";
	// public static final String STEP_ATTRIBUTE_field_name = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_COLUMN = "field_column";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";
	public static final String STEP_ATTRIBUTE_FIELD_REPEAT = "field_repeat";

	/** Array of filenames */
	private String fileName[];

	/** Wildcard or filemask (regular expression) */
	private String fileMask[];

	/** Flag indicating that we should include the filename in the output */
	private boolean includeFilename;

	/** Flag indicating that we should reset RowNum for each file */
	private boolean resetRowNumber;

	/** The name of the field in the output containing the filename */
	private String filenameField;

	/** Flag indicating that a row number field should be included in the output */
	private boolean includeRowNumber;

	/** The name of the field in the output containing the row number */
	private String rowNumberField;

	/** The maximum number or lines to read */
	private long rowLimit;

	/** The fields to import... */
	private PropertyInputField inputFields[];

	/** file name from previous fields * */
	private boolean filefield;

	private boolean isaddresult;

	private String dynamicFilenameField;

	private static final String YES = "Y";

	public final static String type_trim_code[] = { "none", "left", "right",
			"both" };

	public final static String column_code[] = { "key", "value" };

	public PropertyInputMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the input fields.
	 */
	public PropertyInputField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            The input fields to set.
	 */
	public void setInputFields(PropertyInputField[] inputFields) {
		this.inputFields = inputFields;
	}

	/**
	 * @return Returns the fileMask.
	 */
	public String[] getFileMask() {
		return fileMask;
	}

	/**
	 * @param fileMask
	 *            The fileMask to set.
	 */
	public void setFileMask(String[] fileMask) {
		this.fileMask = fileMask;
	}

	/**
	 * @return Returns the fileName.
	 */
	public String[] getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            The fileName to set.
	 */
	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return Returns the filenameField.
	 */
	public String getFilenameField() {
		return filenameField;
	}

	/**
	 * @return Returns the dynamically defined filename field (to read from
	 *         previous steps).
	 */
	public String getDynamicFilenameField() {
		return dynamicFilenameField;
	}

	/**
	 * @param dynamicFilenameField
	 *            the dynamically defined filename field (to read from previous
	 *            steps)
	 */
	public void setDynamicFilenameField(String dynamicFilenameField) {
		this.dynamicFilenameField = dynamicFilenameField;
	}

	/**
	 * @param filenameField
	 *            The filenameField to set.
	 */
	public void setFilenameField(String filenameField) {
		this.filenameField = filenameField;
	}

	/**
	 * @return Returns the includeFilename.
	 */
	public boolean includeFilename() {
		return includeFilename;
	}

	/**
	 * @param includeFilename
	 *            The includeFilename to set.
	 */
	public void setIncludeFilename(boolean includeFilename) {
		this.includeFilename = includeFilename;
	}

	/**
	 * @return Returns the includeRowNumber.
	 */
	public boolean includeRowNumber() {
		return includeRowNumber;
	}

	/**
	 * @return Returns the File field.
	 */
	public boolean isFileField() {
		return filefield;
	}

	/**
	 * @param filefield
	 *            The filefield to set.
	 */
	public void setFileField(boolean filefield) {
		this.filefield = filefield;
	}

	/**
	 * @return Returns the resetRowNumber.
	 */
	public boolean resetRowNumber() {
		return resetRowNumber;
	}

	/**
	 * @param includeRowNumber
	 *            The includeRowNumber to set.
	 */
	public void setIncludeRowNumber(boolean includeRowNumber) {
		this.includeRowNumber = includeRowNumber;
	}

	/**
	 * @param isaddresult
	 *            The isaddresult to set.
	 */
	public void setAddResultFile(boolean isaddresult) {
		this.isaddresult = isaddresult;
	}

	/**
	 * @return Returns isaddresult.
	 */
	public boolean isAddResultFile() {
		return isaddresult;
	}

	/**
	 * @param resetRowNumber
	 *            The resetRowNumber to set.
	 */
	public void setResetRowNumber(boolean resetRowNumber) {
		this.resetRowNumber = resetRowNumber;
	}

	/**
	 * @return Returns the rowLimit.
	 */
	public long getRowLimit() {
		return rowLimit;
	}

	/**
	 * @param rowLimit
	 *            The rowLimit to set.
	 */
	public void setRowLimit(long rowLimit) {
		this.rowLimit = rowLimit;
	}

	/**
	 * @return Returns the rowNumberField.
	 */
	public String getRowNumberField() {
		return rowNumberField;
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
		PropertyInputMeta retval = (PropertyInputMeta) super.clone();
		int nrFiles = fileName.length;
		int nrFields = inputFields.length;
		retval.allocate(nrFiles, nrFields);
		for (int i = 0; i < nrFields; i++) {
			if (inputFields[i] != null) {
				retval.inputFields[i] = (PropertyInputField) inputFields[i]
						.clone();
			}
		}

		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(500);
		retval.append("    ").append(
				XMLHandler.addTagValue("include", includeFilename));
		retval.append("    ").append(
				XMLHandler.addTagValue("include_field", filenameField));
		retval.append("    ").append(
				XMLHandler.addTagValue("filename_Field", dynamicFilenameField));
		retval.append("    ").append(
				XMLHandler.addTagValue("rownum", includeRowNumber));
		retval.append("    ").append(
				XMLHandler.addTagValue("isaddresult", isaddresult));
		retval.append("    ").append(
				XMLHandler.addTagValue("filefield", filefield));
		retval.append("    ").append(
				XMLHandler.addTagValue("rownum_field", rowNumberField));
		retval.append("    ").append(
				XMLHandler.addTagValue("resetrownumber", resetRowNumber));
		retval.append("    <file>").append(Const.CR);
		for (int i = 0; i < fileName.length; i++) {
			retval.append("      ").append(
					XMLHandler.addTagValue("name", fileName[i]));
			retval.append("      ").append(
					XMLHandler.addTagValue("filemask", fileMask[i]));
		}
		retval.append("    </file>").append(Const.CR);

		/*
		 * Describe the fields to read
		 */
		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < inputFields.length; i++) {
			retval.append("      <field>").append(Const.CR);
			retval.append("        ").append(
					XMLHandler.addTagValue("name", inputFields[i].getName()));
			retval.append("        ").append(
					XMLHandler.addTagValue("column", inputFields[i]
							.getColumnCode()));
			retval.append("        ").append(
					XMLHandler
							.addTagValue("type", inputFields[i].getTypeDesc()));
			retval.append("        ").append(
					XMLHandler
							.addTagValue("format", inputFields[i].getFormat()));
			retval.append("        ").append(
					XMLHandler
							.addTagValue("length", inputFields[i].getLength()));
			retval.append("        ").append(
					XMLHandler.addTagValue("precision", inputFields[i]
							.getPrecision()));
			retval.append("        ").append(
					XMLHandler.addTagValue("currency", inputFields[i]
							.getCurrencySymbol()));
			retval.append("        ").append(
					XMLHandler.addTagValue("decimal", inputFields[i]
							.getDecimalSymbol()));
			retval.append("        ").append(
					XMLHandler.addTagValue("group", inputFields[i]
							.getGroupSymbol()));
			retval.append("        ").append(
					XMLHandler.addTagValue("trim_type", inputFields[i]
							.getTrimTypeCode()));
			retval.append("        ").append(
					XMLHandler.addTagValue("repeat", inputFields[i]
							.isRepeated()));
			retval.append("      </field>").append(Const.CR);
		}
		retval.append("    </fields>").append(Const.CR);
		retval.append("    ").append(XMLHandler.addTagValue("limit", rowLimit));

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		includeFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeFilename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".filenameField"));
		dynamicFilenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".dynamicFilenameField"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeRowNumber"));
		isaddresult = BaseStepMeta.parameterToBoolean(p
				.get(id + ".isaddresult"));
		filefield = BaseStepMeta.parameterToBoolean(p.get(id + ".filefield"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id
				+ ".rowNumberField"));
		resetRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".resetRowNumber"));
		rowLimit = BaseStepMeta.parameterToInt(p.get(id + ".rowLimit"));

		String[] fileName = p.get(id + "_selectedFiles.fileName");
		String[] fileMask = p.get(id + "_selectedFiles.fileMask");
		this.fileName = (fileName != null) ? fileName : (new String[0]);
		this.fileMask = (fileMask != null) ? fileMask : (new String[0]);

		String[] name = p.get(id + "_fields.fieldName");
		String[] column = p.get(id + "_fields.fieldColumn");
		String[] type = p.get(id + "_fields.fieldType");
		String[] format = p.get(id + "_fields.fieldFormat");
		String[] length = p.get(id + "_fields.fieldLength");
		String[] precision = p.get(id + "_fields.fieldPrecision");
		String[] currency = p.get(id + "_fields.fieldCurrency");
		String[] decimalSymbol = p.get(id + "_fields.fieldDecimal");
		String[] groupingSymbol = p.get(id + "_fields.fieldGroup");
		String[] trimtype = p.get(id + "_fields.fieldTrimType");
		String[] repeat = p.get(id + "_fields.fieldRepeat");

		if (name != null && name.length > 0) {
			this.inputFields = new PropertyInputField[name.length];
			for (int i = 0; i < name.length; i++) {
				this.inputFields[i] = new PropertyInputField();
				this.inputFields[i].setName(name[i]);
				this.inputFields[i].setColumn(Const.toInt(column[i], 0));
				this.inputFields[i].setType(Const.toInt(type[i], 0));
				this.inputFields[i].setFormat(format[i]);
				this.inputFields[i].setLength(Const.toInt(length[i], -1));
				this.inputFields[i].setPrecision(Const.toInt(precision[i], -1));
				this.inputFields[i].setCurrencySymbol(currency[i]);
				this.inputFields[i].setDecimalSymbol(decimalSymbol[i]);
				this.inputFields[i].setGroupSymbol(groupingSymbol[i]);
				this.inputFields[i].setTrimType(Const.toInt(trimtype[i], 0));
				this.inputFields[i].setRepeated(parameterToBoolean(repeat[i]));
			}
		} else {
			this.inputFields = new PropertyInputField[0];
		}
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			includeFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include"));
			filenameField = XMLHandler.getTagValue(stepnode, "include_field");
			includeRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "rownum"));

			String addresult = XMLHandler.getTagValue(stepnode, "isaddresult");
			if (Const.isEmpty(addresult))
				isaddresult = true;
			else
				isaddresult = "Y".equalsIgnoreCase(addresult);

			filefield = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"filefield"));
			rowNumberField = XMLHandler.getTagValue(stepnode, "rownum_field");
			dynamicFilenameField = XMLHandler.getTagValue(stepnode,
					"filename_Field");
			resetRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "resetrownumber"));

			Node filenode = XMLHandler.getSubNode(stepnode, "file");
			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrFiles = XMLHandler.countNodes(filenode, "name");
			int nrFields = XMLHandler.countNodes(fields, "field");

			allocate(nrFiles, nrFields);

			for (int i = 0; i < nrFiles; i++) {
				Node filenamenode = XMLHandler.getSubNodeByNr(filenode, "name",
						i);
				Node filemasknode = XMLHandler.getSubNodeByNr(filenode,
						"filemask", i);
				fileName[i] = XMLHandler.getNodeValue(filenamenode);
				fileMask[i] = XMLHandler.getNodeValue(filemasknode);
			}

			for (int i = 0; i < nrFields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				inputFields[i] = new PropertyInputField();

				inputFields[i].setName(XMLHandler.getTagValue(fnode, "name"));
				inputFields[i].setColumn(getColumnByCode(XMLHandler
						.getTagValue(fnode, "column")));
				inputFields[i].setType(ValueMeta.getType(XMLHandler
						.getTagValue(fnode, "type")));
				inputFields[i].setLength(Const.toInt(XMLHandler.getTagValue(
						fnode, "length"), -1));
				inputFields[i].setPrecision(Const.toInt(XMLHandler.getTagValue(
						fnode, "precision"), -1));
				String srepeat = XMLHandler.getTagValue(fnode, "repeat");
				inputFields[i].setTrimType(getTrimTypeByCode(XMLHandler
						.getTagValue(fnode, "trim_type")));

				if (srepeat != null)
					inputFields[i].setRepeated(YES.equalsIgnoreCase(srepeat));
				else
					inputFields[i].setRepeated(false);

				inputFields[i].setFormat(XMLHandler
						.getTagValue(fnode, "format"));
				inputFields[i].setCurrencySymbol(XMLHandler.getTagValue(fnode,
						"currency"));
				inputFields[i].setDecimalSymbol(XMLHandler.getTagValue(fnode,
						"decimal"));
				inputFields[i].setGroupSymbol(XMLHandler.getTagValue(fnode,
						"group"));

			}

			// Is there a limit on the number of rows we process?
			rowLimit = Const.toLong(XMLHandler.getTagValue(stepnode, "limit"),
					0L);
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void allocate(int nrfiles, int nrfields) {
		fileName = new String[nrfiles];
		fileMask = new String[nrfiles];
		inputFields = new PropertyInputField[nrfields];
	}

	public void setDefault() {
		isaddresult = true;
		filefield = false;
		includeFilename = false;
		filenameField = "";
		includeRowNumber = false;
		rowNumberField = "";
		dynamicFilenameField = "";

		int nrFiles = 0;
		int nrFields = 0;

		allocate(nrFiles, nrFields);

		for (int i = 0; i < nrFiles; i++) {
			fileName[i] = "filename" + (i + 1);
			fileMask[i] = "";
		}

		for (int i = 0; i < nrFields; i++) {
			inputFields[i] = new PropertyInputField("field" + (i + 1));
		}

		rowLimit = 0;
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface info[], StepMeta nextStep, VariableSpace space)
			throws KettleStepException {

		int i;
		for (i = 0; i < inputFields.length; i++) {
			PropertyInputField field = inputFields[i];

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

		if (includeFilename) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(filenameField),
					ValueMeta.TYPE_STRING);
			v.setLength(250);
			v.setPrecision(-1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}

		if (includeRowNumber) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(rowNumberField),
					ValueMeta.TYPE_INTEGER);
			v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH, 0);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
	}

	public final static int getTrimTypeByCode(String tt) {
		if (tt != null) {
			for (int i = 0; i < type_trim_code.length; i++) {
				if (type_trim_code[i].equalsIgnoreCase(tt))
					return i;
			}
		}
		return 0;
	}

	public final static int getColumnByCode(String tt) {
		if (tt != null) {
			for (int i = 0; i < column_code.length; i++) {
				if (column_code[i].equalsIgnoreCase(tt))
					return i;
			}
		}
		return 0;
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {

		try {
			includeFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE);
			filenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_INCLUDE_FIELD);
			dynamicFilenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILENAME_FIELD);
			includeRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ROWNUM);

			String addresult = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ISADDRESULT);
			if (Const.isEmpty(addresult))
				isaddresult = true;
			else
				isaddresult = rep.getStepAttributeBoolean(id_step,
						STEP_ATTRIBUTE_ISADDRESULT);

			filefield = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILEFIELD);
			rowNumberField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			resetRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_RESET_ROWNUMBER);
			rowLimit = rep.getStepAttributeInteger(id_step,
					STEP_ATTRIBUTE_LIMIT);
			int nrFiles = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FILE_NAME);
			int nrFields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrFiles, nrFields);

			for (int i = 0; i < nrFiles; i++) {
				fileName[i] = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FILE_NAME);
				fileMask[i] = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FILE_MASK);
			}

			for (int i = 0; i < nrFields; i++) {
				PropertyInputField field = new PropertyInputField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				field.setColumn(PropertyInputField.getColumnByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_COLUMN)));
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
				field.setTrimType(PropertyInputField.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setRepeated(rep.getStepAttributeBoolean(id_step, i,
						STEP_ATTRIBUTE_FIELD_REPEAT));

				inputFields[i] = field;
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("PropertyInputMeta.Exception.ErrorReadingRepository"),
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "include",
					includeFilename);
			rep.saveStepAttribute(id_transformation, id_step, "include_field",
					filenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum",
					includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "isaddresult",
					isaddresult);
			rep.saveStepAttribute(id_transformation, id_step, "filefield",
					filefield);
			rep.saveStepAttribute(id_transformation, id_step, "filename_Field",
					dynamicFilenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumberField);
			rep
					.saveStepAttribute(id_transformation, id_step, "limit",
							rowLimit);
			rep.saveStepAttribute(id_transformation, id_step,
					"reset_rownumber", resetRowNumber);

			for (int i = 0; i < fileName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_name", fileName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_mask", fileMask[i]);
			}

			for (int i = 0; i < inputFields.length; i++) {
				PropertyInputField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_column", field.getColumnCode());
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
					"PropertyInputMeta.Exception.ErrorSavingToRepository", ""
							+ id_step), e);
		}
	}

	public FileInputList getFiles(VariableSpace space) {

		String required[] = new String[fileName.length];
		boolean subdirs[] = new boolean[fileName.length]; // boolean arrays
		// are defaulted to
		// false.
		for (int i = 0; i < required.length; required[i] = "Y", i++); //$NON-NLS-1$
		return FileInputList.createFileList(space, fileName, fileMask,
				required, subdirs);

	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {

		CheckResult cr;

		// See if we get input...
		if (input.length > 0) {
			cr = new CheckResult(
					CheckResult.TYPE_RESULT_ERROR,
					Messages
							.getString("PropertyInputMeta.CheckResult.NoInputExpected"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("PropertyInputMeta.CheckResult.NoInput"),
					stepMeta);
			remarks.add(cr);
		}

		FileInputList fileInputList = getFiles(transMeta);
		// String files[] = getFiles();
		if (fileInputList == null || fileInputList.getFiles().size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("PropertyInputMeta.CheckResult.NoFiles"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("PropertyInputMeta.CheckResult.FilesOk", ""
							+ fileInputList.getFiles().size()), stepMeta);
			remarks.add(cr);
		}

	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new PropertyInput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new PropertyInputData();
	}

	public boolean supportsErrorHandling() {
		return true;
	}

}
