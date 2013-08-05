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

package com.panet.imeta.trans.steps.ldifinput;

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

public class LDIFInputMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_FILEFIELD = "filefield";
	public static final String STEP_ATTRIBUTE_DYNAMICFILENAMEFIELD = "dynamicFilenameField";
	public static final String STEP_ATTRIBUTE_INCLUDE = "include";
	public static final String STEP_ATTRIBUTE_INCLUDE_FIELD = "include_field";
	public static final String STEP_ATTRIBUTE_ADDTORESULTFILENAME = "addtoresultfilename";
	public static final String STEP_ATTRIBUTE_MULTIVALUEDSEPARATOR = "multiValuedSeparator";
	public static final String STEP_ATTRIBUTE_ROWNUM = "rownum";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_CONTENTTYPE = "contenttype";
	public static final String STEP_ATTRIBUTE_CONTENTTYPE_FIELD = "contenttype_field";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	// public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_MASK = "file_mask";
	// public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
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

	/** Array of filenames */
	private String fileName[];

	/** Wildcard or filemask (regular expression) */
	private String fileMask[];

	/** Flag indicating that we should include the filename in the output */
	private boolean includeFilename;

	/** The name of the field in the output containing the filename */
	private String filenameField;

	/** Flag indicating that a row number field should be included in the output */
	private boolean includeRowNumber;

	/** The name of the field in the output containing the row number */
	private String rowNumberField;

	/** The maximum number or lines to read */
	private long rowLimit;

	/** The fields to import... */
	private LDIFInputField inputFields[];

	private boolean addtoresultfilename;

	private String multiValuedSeparator;

	private boolean includeContentType;

	private String contentTypeField;

	/** file name from previous fields * */
	private boolean filefield;

	private String dynamicFilenameField;

	public LDIFInputMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return Returns the input fields.
	 */
	public LDIFInputField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            The input fields to set.
	 */
	public void setInputFields(LDIFInputField[] inputFields) {
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
	 * @param filefield
	 *            The filefield to set.
	 */
	public void setFileField(boolean filefield) {
		this.filefield = filefield;
	}

	/**
	 * @return Returns the File field.
	 */
	public boolean isFileField() {
		return filefield;
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
	 * @param includeRowNumber
	 *            The includeRowNumber to set.
	 */
	public void setIncludeRowNumber(boolean includeRowNumber) {
		this.includeRowNumber = includeRowNumber;
	}

	/**
	 * @return Returns the includeContentType.
	 */
	public boolean includeContentType() {
		return includeContentType;
	}

	/**
	 * @param includeRowNumber
	 *            The includeRowNumber to set.
	 */
	public void setIncludeContentType(boolean includeContentType) {
		this.includeContentType = includeContentType;
	}

	/**
	 * @param multiValuedSeparator
	 *            The multi-valued separator filed.
	 */
	public void setMultiValuedSeparator(String multiValuedSeparator) {
		this.multiValuedSeparator = multiValuedSeparator;
	}

	/**
	 * @return Returns the multi valued separator.
	 */
	public String getMultiValuedSeparator() {
		return multiValuedSeparator;
	}

	/**
	 * @param addtoresultfilename
	 *            The addtoresultfilename to set.
	 */
	public void setAddToResultFilename(boolean addtoresultfilename) {
		this.addtoresultfilename = addtoresultfilename;
	}

	/**
	 * @return Returns the addtoresultfilename.
	 */
	public boolean AddToResultFilename() {
		return addtoresultfilename;
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

	/**
	 * @return Returns the filenameField.
	 */
	public String getFilenameField() {
		return filenameField;
	}

	/**
	 * @return Returns the dynamic filename field (from previous steps)
	 */
	public String getDynamicFilenameField() {
		return dynamicFilenameField;
	}

	/**
	 * @param dynamicFilenameField
	 *            The dynamic filename field to set.
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
	 * @return Returns the contentTypeField.
	 */
	public String getContentTypeField() {
		return contentTypeField;
	}

	/**
	 * @param contentTypeField
	 *            The contentTypeField to set.
	 */
	public void setContentTypeField(String contentTypeField) {
		this.contentTypeField = contentTypeField;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		LDIFInputMeta retval = (LDIFInputMeta) super.clone();

		int nrFiles = fileName.length;
		int nrFields = inputFields.length;

		retval.allocate(nrFiles, nrFields);

		for (int i = 0; i < nrFields; i++) {
			if (inputFields[i] != null) {
				retval.inputFields[i] = (LDIFInputField) inputFields[i].clone();
			}
		}

		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();

		retval.append("    ").append(
				XMLHandler.addTagValue("filefield", filefield));
		retval.append("    ").append(
				XMLHandler.addTagValue("dynamicFilenameField",
						dynamicFilenameField));
		retval.append("    "
				+ XMLHandler.addTagValue("include", includeFilename));
		retval.append("    "
				+ XMLHandler.addTagValue("include_field", filenameField));
		retval.append("    "
				+ XMLHandler.addTagValue("rownum", includeRowNumber));
		retval.append("    "
				+ XMLHandler.addTagValue("rownum_field", rowNumberField));
		retval.append("    "
				+ XMLHandler.addTagValue("contenttype", includeContentType));
		retval
				.append("    "
						+ XMLHandler.addTagValue("contenttype_field",
								contentTypeField));
		retval.append("    "
				+ XMLHandler.addTagValue("addtoresultfilename",
						addtoresultfilename));
		retval.append("    "
				+ XMLHandler.addTagValue("multiValuedSeparator",
						multiValuedSeparator));

		retval.append("    <file>" + Const.CR);
		for (int i = 0; i < fileName.length; i++) {
			retval.append("      "
					+ XMLHandler.addTagValue("name", fileName[i]));
			retval.append("      "
					+ XMLHandler.addTagValue("filemask", fileMask[i]));
		}
		retval.append("      </file>" + Const.CR);

		retval.append("    <fields>" + Const.CR);
		for (int i = 0; i < inputFields.length; i++) {
			LDIFInputField field = inputFields[i];
			retval.append(field.getXML());
		}
		retval.append("      </fields>" + Const.CR);
		retval.append("    " + XMLHandler.addTagValue("limit", rowLimit));

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		filefield = BaseStepMeta.parameterToBoolean(p.get(id + ".filefield"));
		dynamicFilenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".dynamicFilenameField"));
		includeFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeFilename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".filenameField"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeRowNumber"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id
				+ ".rowNumberField"));
		includeContentType = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeContentType"));
		contentTypeField = BaseStepMeta.parameterToString(p.get(id
				+ ".contentTypeField"));
		addtoresultfilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".addtoresultfilename"));
		multiValuedSeparator = BaseStepMeta.parameterToString(p.get(id
				+ ".multiValuedSeparator"));
		rowLimit = BaseStepMeta.parameterToInt(p.get(id + ".rowLimit"));

		String[] fileName = p.get(id + "_selectedFiles.fileName");
		String[] fileMask = p.get(id + "_selectedFiles.fileMask");
		this.fileName = (fileName != null) ? fileName : (new String[0]);
		this.fileMask = (fileMask != null) ? fileMask : (new String[0]);

		String[] name = p.get(id + "_fields.fieldName");
		String[] attribut = p.get(id + "_fields.fieldAttribute");
		String[] type = p.get(id + "_fields.fieldType");
		String[] format = p.get(id + "_fields.fieldFormat");
		String[] length = p.get(id + "_fields.fieldLength");
		String[] precision = p.get(id + "_fields.fieldPrecision");
		String[] currency_symbol = p.get(id + "_fields.fieldCurrency");
		String[] decimal_symbol = p.get(id + "_fields.fieldDecimal");
		String[] group_symbol = p.get(id + "_fields.fieldGroup");
		String[] trimtype = p.get(id + "_fields.fieldTrimType");
		String[] repeat = p.get(id + "_fields.fieldRepeat");

		if (name != null && name.length > 0) {
			this.inputFields = new LDIFInputField[name.length];
			for (int i = 0; i < name.length; i++) {
				inputFields[i] = new LDIFInputField(name[i]);
				inputFields[i].setAttribut(attribut[i]);
				inputFields[i].setType(Const.toInt(type[i], 0));
				inputFields[i].setFormat(format[i]);
				inputFields[i].setLength(Const.toInt(length[i], -1));
				inputFields[i].setPrecision(Const.toInt(precision[i], -1));
				inputFields[i].setCurrencySymbol(currency_symbol[i]);
				inputFields[i].setDecimalSymbol(decimal_symbol[i]);
				inputFields[i].setGroupSymbol(group_symbol[i]);
				inputFields[i].setTrimType(Const.toInt(trimtype[i], 0));
				inputFields[i].setRepeated(Boolean.parseBoolean(repeat[i]));
			}
		} else {
			this.inputFields = new LDIFInputField[0];
		}
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			filefield = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"filefield"));
			dynamicFilenameField = XMLHandler.getTagValue(stepnode,
					"dynamicFilenameField");
			includeFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include"));
			filenameField = XMLHandler.getTagValue(stepnode, "include_field");
			includeRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "rownum"));
			rowNumberField = XMLHandler.getTagValue(stepnode, "rownum_field");
			includeContentType = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "contenttype"));
			contentTypeField = XMLHandler.getTagValue(stepnode,
					"contenttype_field");
			addtoresultfilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "addtoresultfilename"));
			multiValuedSeparator = XMLHandler.getTagValue(stepnode,
					"multiValuedSeparator");

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
				LDIFInputField field = new LDIFInputField(fnode);
				inputFields[i] = field;
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

		inputFields = new LDIFInputField[nrfields];
	}

	public void setDefault() {
		filefield = false;
		dynamicFilenameField = "";
		includeFilename = false;
		filenameField = "";
		includeRowNumber = false;
		rowNumberField = "";
		includeContentType = false;
		contentTypeField = "";
		multiValuedSeparator = ",";
		addtoresultfilename = false;

		int nrFiles = 0;
		int nrFields = 0;

		allocate(nrFiles, nrFields);

		for (int i = 0; i < nrFiles; i++) {
			fileName[i] = "filename" + (i + 1);
			fileMask[i] = "";
		}

		for (int i = 0; i < nrFields; i++) {
			inputFields[i] = new LDIFInputField("field" + (i + 1));
		}

		rowLimit = 0;
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface info[], StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		int i;
		for (i = 0; i < inputFields.length; i++) {
			LDIFInputField field = inputFields[i];

			int type = field.getType();
			if (type == ValueMeta.TYPE_NONE)
				type = ValueMeta.TYPE_STRING;
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(field.getName()), type);
			v.setLength(field.getLength(), field.getPrecision());
			v.setOrigin(name);
			r.addValueMeta(v);

		}

		if (includeFilename) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(filenameField),
					ValueMeta.TYPE_STRING);
			v.setLength(100, -1);
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
		if (includeContentType) {
			ValueMetaInterface v = new ValueMeta(space
					.environmentSubstitute(contentTypeField),
					ValueMeta.TYPE_STRING);
			v.setLength(100, -1);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			filefield = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILEFIELD);
			dynamicFilenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_DYNAMICFILENAMEFIELD);
			includeFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE);
			filenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_INCLUDE_FIELD);
			addtoresultfilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ADDTORESULTFILENAME);
			multiValuedSeparator = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_MULTIVALUEDSEPARATOR);

			includeRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ROWNUM);
			rowNumberField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			includeContentType = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_CONTENTTYPE);
			contentTypeField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_CONTENTTYPE_FIELD);

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
				LDIFInputField field = new LDIFInputField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				field.setAttribut(rep.getStepAttributeString(id_step, i,
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
				field.setTrimType(LDIFInputField.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setRepeated(rep.getStepAttributeBoolean(id_step, i,
						STEP_ATTRIBUTE_FIELD_REPEAT));

				inputFields[i] = field;
			}
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("LDIFInputMeta.Exception.ErrorReadingRepository"),
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, "filefield",
					filefield);
			rep.saveStepAttribute(id_transformation, id_step,
					"dynamicFilenameField", dynamicFilenameField);
			rep.saveStepAttribute(id_transformation, id_step, "include",
					includeFilename);
			rep.saveStepAttribute(id_transformation, id_step, "include_field",
					filenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum",
					includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumberField);
			rep.saveStepAttribute(id_transformation, id_step, "contenttype",
					includeContentType);
			rep.saveStepAttribute(id_transformation, id_step,
					"contenttype_field", contentTypeField);

			rep
					.saveStepAttribute(id_transformation, id_step, "limit",
							rowLimit);
			rep.saveStepAttribute(id_transformation, id_step,
					"addtoresultfilename", addtoresultfilename);
			rep.saveStepAttribute(id_transformation, id_step,
					"multiValuedSeparator", multiValuedSeparator);

			for (int i = 0; i < fileName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_name", fileName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_mask", fileMask[i]);
			}

			for (int i = 0; i < inputFields.length; i++) {
				LDIFInputField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"fied_attribut", field.getAttribut());
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
					"LDIFInputMeta.Exception.ErrorSavingToRepository", ""
							+ id_step), e);
		}
	}

	public FileInputList getFiles(VariableSpace space) {
		String required[] = new String[fileName.length];
		boolean subdirs[] = new boolean[fileName.length];
		for (int i = 0; i < required.length; i++) {
			required[i] = "N";
			subdirs[i] = false;
		}
		return FileInputList.createFileList(space, space
				.environmentSubstitute(fileName), space
				.environmentSubstitute(fileMask), required, subdirs);
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we get input...
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("LDIFInputMeta.CheckResult.NoInputExpected"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("LDIFInputMeta.CheckResult.NoInput"), stepMeta);
			remarks.add(cr);
		}

		FileInputList fileInputList = getFiles(transMeta);
		if (fileInputList == null || fileInputList.getFiles().size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("LDIFInputMeta.CheckResult.NoFiles"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("LDIFInputMeta.CheckResult.FilesOk", ""
							+ fileInputList.getFiles().size()), stepMeta);
			remarks.add(cr);
		}
	}

	public StepDataInterface getStepData() {
		return new LDIFInputData();
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new LDIFInput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public boolean supportsErrorHandling() {
		return true;
	}
}
