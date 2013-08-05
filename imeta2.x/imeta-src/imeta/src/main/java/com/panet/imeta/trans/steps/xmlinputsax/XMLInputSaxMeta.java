/* Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.*/

/* 
 * 
 * Created on 4-apr-2003
 * 
 */

package com.panet.imeta.trans.steps.xmlinputsax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Const;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleValueException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.fileinput.FileInputList;
import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMeta;
import com.panet.imeta.core.variables.VariableSpace;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

public class XMLInputSaxMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_INCLUDE = "include";
	public static final String STEP_ATTRIBUTE_INCLUDE_FIELD = "include_field";
	public static final String STEP_ATTRIBUTE_ROWNUM = "rownum";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_DEF_ELEMENT = "def_element";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_INPUT_POSITION = "input_position";
	// public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_MASK = "file_mask";
	// public static final String STEP_ATTRIBUTE_DEF_ELEMENT = "def_element";
	public static final String STEP_ATTRIBUTE_DEF_ATTRIBUTE = "def_attribute";
	// public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";
	public static final String STEP_ATTRIBUTE_FIELD_REPEAT = "field_repeat";
	public static final String STEP_ATTRIBUTE_FIELD_POSITION_CODE = "field_position_code";
	public static final String STEP_ATTRIBUTE_FIELD_INPUT_POSITION = "input_position";
	
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
	private XMLInputSaxField inputFields[];

	/** The position in the XML documents to start (elements) */
	private XMLInputSaxFieldPosition inputPosition[];

	private List<String> definitionElement;
	private List<String> definitionAttribute;

	public XMLInputSaxMeta() {
		super(); // allocate BaseStepMeta
		definitionElement = new ArrayList<String>();
		definitionAttribute = new ArrayList<String>();
	}

	/**
	 * @return Returns the input fields.
	 */
	public XMLInputSaxField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            The input fields to set.
	 */
	public void setInputFields(XMLInputSaxField[] inputFields) {
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
	 * @param includeRowNumber
	 *            The includeRowNumber to set.
	 */
	public void setIncludeRowNumber(boolean includeRowNumber) {
		this.includeRowNumber = includeRowNumber;
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
		XMLInputSaxMeta retval = (XMLInputSaxMeta) super.clone();

		int nrFiles = fileName.length;
		int nrAttributes = getDefinitionLength();
		int nrFields = inputFields.length;
		int nrPositions = inputPosition.length;

		retval.allocate(nrFiles, nrFields, nrPositions);

		for (int i = 0; i < nrAttributes; i++) {
			retval.setDefiningAttribute(getDefiningElement(i),
					getDefiningAttribute(i));
		}

		for (int i = 0; i < nrFields; i++) {
			if (inputFields[i] != null) {
				retval.inputFields[i] = (XMLInputSaxField) inputFields[i]
						.clone();
			}
		}

		for (int i = 0; i < nrPositions; i++) {
			retval.inputPosition[i] = inputPosition[i];
		}

		return retval;
	}

	public String getXML() {
		String retval = "";

		retval += "    " + XMLHandler.addTagValue("include", includeFilename);
		retval += "    "
				+ XMLHandler.addTagValue("include_field", filenameField);
		retval += "    " + XMLHandler.addTagValue("rownum", includeRowNumber);
		retval += "    "
				+ XMLHandler.addTagValue("rownum_field", rowNumberField);

		retval += "    <file>" + Const.CR;
		for (int i = 0; i < fileName.length; i++) {
			retval += "      " + XMLHandler.addTagValue("name", fileName[i]);
			retval += "      "
					+ XMLHandler.addTagValue("filemask", fileMask[i]);
		}
		retval += "      </file>" + Const.CR;

		retval += "    <def_attributes>" + Const.CR;
		for (int i = 0; i < definitionElement.size(); i++) {
			retval += "      "
					+ XMLHandler.addTagValue("def_element",
							getDefiningElement(i));
			retval += "      "
					+ XMLHandler.addTagValue("def_attribute",
							getDefiningAttribute(i));
		}
		retval += "      </def_attributes>" + Const.CR;

		retval += "    <fields>" + Const.CR;
		for (int i = 0; i < inputFields.length; i++) {
			XMLInputSaxField field = inputFields[i];
			retval += field.getXML();
		}
		retval += "      </fields>" + Const.CR;

		retval += "    <positions>" + Const.CR;
		for (int i = 0; i < inputPosition.length; i++) {
			retval += "      "
					+ XMLHandler.addTagValue("position", inputPosition[i]
							.toString());
		}

		retval += "      </positions>" + Const.CR;

		retval += "    " + XMLHandler.addTagValue("limit", rowLimit);

		return retval;
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		includeFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeFilename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".filenameField"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeRowNumber"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id
				+ ".rowNumberField"));
		rowLimit = BaseStepMeta.parameterToInt(p.get(id + ".rowLimit"));

		String[] fileName = p.get(id + "_selectedFiles.fileName");
		String[] fileMask = p.get(id + "_selectedFiles.fileMask");
		this.fileName = (fileName != null) ? fileName : (new String[0]);
		this.fileMask = (fileMask != null) ? fileMask : (new String[0]);

		String[] element = p.get(id + "_location.element");
		if(element !=null && element.length > 0){
			XMLInputSaxFieldPosition[] position = new XMLInputSaxFieldPosition[element.length];
			for(int i = 0; i < position.length; i++){
				position[i].setElementNr(Const.toInt(element[i],0));
			}
			this.inputPosition = position;
		}else{
			this.inputPosition = new XMLInputSaxFieldPosition[0];
		}
		
		String[] name = p.get(id + "_fields.fieldName");
		String[] type = p.get(id + "_fields.fieldType");
		String[] format = p.get(id + "_fields.fieldFormat");
		String[] length = p.get(id + "_fields.fieldLength");
		String[] precision = p.get(id + "_fields.fieldPrecision");
		String[] currency_symbol = p.get(id + "_fields.fieldCurrency");
		String[] decimal_symbol = p.get(id + "_fields.fieldDecimal");
		String[] group_symbol = p.get(id + "_fields.fieldGroup");
		String[] trimtype = p.get(id + "_fields.fieldTrimType");
		String[] repeat = p.get(id + "_fields.fieldRepeat");
		String[] postion = p.get(id + "_fields.fieldPostion");

		if (name != null && name.length > 0) {
			XMLInputSaxField[] field = new XMLInputSaxField[name.length];
			for (int i = 0; i < field.length; i++) {
				field[i] = new XMLInputSaxField();
				field[i].setName(name[i]);
				field[i].setType(Const.toInt(type[i], 0));
				field[i].setFormat(format[i]);
				field[i].setLength(Const.toInt(length[i], -1));
				field[i].setPrecision(Const.toInt(precision[i], -1));
				field[i].setCurrencySymbol(currency_symbol[i]);
				field[i].setDecimalSymbol(decimal_symbol[i]);
				field[i].setGroupSymbol(group_symbol[i]);
				field[i].setTrimType(Const.toInt(trimtype[i], 0));
				field[i].setRepeated(Boolean.parseBoolean(repeat[i]));
				try {
					field[i].setFieldPosition(postion[i]);
				} catch (KettleException e) {
				}
			}
			this.inputFields = field;
		} else {
			this.inputFields = new XMLInputSaxField[0];
		}
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			String lim;

			includeFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include"));
			filenameField = XMLHandler.getTagValue(stepnode, "include_field");
			includeRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "rownum"));
			rowNumberField = XMLHandler.getTagValue(stepnode, "rownum_field");

			Node filenode = XMLHandler.getSubNode(stepnode, "file");
			Node attributes = XMLHandler.getSubNode(stepnode, "def_attributes");
			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			Node positions = XMLHandler.getSubNode(stepnode, "positions");
			int nrFiles = XMLHandler.countNodes(filenode, "name");
			int nrAttributes = XMLHandler.countNodes(attributes, "def_element");
			int nrFields = XMLHandler.countNodes(fields, "field");
			int nrPositions = XMLHandler.countNodes(positions, "position");

			allocate(nrFiles, nrFields, nrPositions);

			for (int i = 0; i < nrFiles; i++) {
				Node filenamenode = XMLHandler.getSubNodeByNr(filenode, "name",
						i);
				Node filemasknode = XMLHandler.getSubNodeByNr(filenode,
						"filemask", i);
				fileName[i] = XMLHandler.getNodeValue(filenamenode);
				fileMask[i] = XMLHandler.getNodeValue(filemasknode);
			}

			this.clearDefinition();
			for (int i = 0; i < nrAttributes; i++) {
				Node elementnode = XMLHandler.getSubNodeByNr(attributes,
						"def_element", i);
				Node attributenode = XMLHandler.getSubNodeByNr(attributes,
						"def_attribute", i);
				String a = XMLHandler.getNodeValue(elementnode);
				String b = XMLHandler.getNodeValue(attributenode);
				this.setDefiningAttribute(a, b);
			}

			for (int i = 0; i < nrFields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				XMLInputSaxField field = new XMLInputSaxField(fnode);
				inputFields[i] = field;
			}

			for (int i = 0; i < nrPositions; i++) {
				Node positionnode = XMLHandler.getSubNodeByNr(positions,
						"position", i);
				String encoded = XMLHandler.getNodeValue(positionnode);
				inputPosition[i] = new XMLInputSaxFieldPosition(encoded);
			}

			// Is there a limit on the number of rows we process?
			lim = XMLHandler.getTagValue(stepnode, "limit");
			rowLimit = Const.toLong(lim, 0L);
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void allocate(int nrfiles, int nrfields, int nrPositions) {
		fileName = new String[nrfiles];
		fileMask = new String[nrfiles];

		inputFields = new XMLInputSaxField[nrfields];

		inputPosition = new XMLInputSaxFieldPosition[nrPositions];
		definitionElement = new ArrayList<String>();
		definitionAttribute = new ArrayList<String>();
	}

	public void setDefault() {
		includeFilename = false;
		filenameField = "";
		includeRowNumber = false;
		rowNumberField = "";

		int nrFiles = 0;
		int nrFields = 0;
		int nrPositions = 0;

		allocate(nrFiles, nrFields, nrPositions);

		for (int i = 0; i < nrFiles; i++) {
			fileName[i] = "filename" + (i + 1);
			fileMask[i] = "";
		}

		for (int i = 0; i < nrFields; i++) {
			inputFields[i] = new XMLInputSaxField("field" + (i + 1), null);
		}

		for (int i = 0; i < nrPositions; i++) {
			try {
				inputPosition[i] = new XMLInputSaxFieldPosition("position"
						+ (i + 1), XMLInputSaxFieldPosition.XML_ELEMENT_POS);
			} catch (KettleValueException e) {
				LogWriter.getInstance().logError(toString(),
						Const.getStackTracker(e));
			}
		}

		rowLimit = 0L;
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {
		for (int i = 0; i < inputFields.length; i++) {
			XMLInputSaxField field = inputFields[i];

			int type = field.getType();
			if (type == ValueMeta.TYPE_NONE)
				type = ValueMeta.TYPE_STRING;

			ValueMeta v = new ValueMeta(field.getName(), type);
			v.setLength(field.getLength());
			v.setPrecision(field.getPrecision());
			v.setConversionMask(field.getFormat());
			v.setGroupingSymbol(field.getGroupSymbol());
			v.setDecimalSymbol(field.getDecimalSymbol());
			v.setCurrencySymbol(field.getCurrencySymbol());

			v.setOrigin(name);
			row.addValueMeta(v);
		}
		if (includeFilename) {
			ValueMeta v = new ValueMeta(filenameField, ValueMeta.TYPE_STRING);
			v.setLength(100, -1);
			v.setOrigin(name);
			row.addValueMeta(v);
		}
		if (includeRowNumber) {
			ValueMeta v = new ValueMeta(rowNumberField, ValueMeta.TYPE_NUMBER);
			v.setLength(7, 0);
			v.setOrigin(name);
			row.addValueMeta(v);
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			includeFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE);
			filenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_INCLUDE_FIELD);

			includeRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ROWNUM);
			rowNumberField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			rowLimit = (int) rep.getStepAttributeInteger(id_step,
					STEP_ATTRIBUTE_LIMIT);

			int nrFiles = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FILE_NAME);
			int nrAttributes = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_DEF_ELEMENT);
			int nrFields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);
			int nrPositions = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_INPUT_POSITION);

			allocate(nrFiles, nrFields, nrPositions);

			for (int i = 0; i < nrFiles; i++) {
				fileName[i] = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FILE_NAME);
				fileMask[i] = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FILE_MASK);
			}

			clearDefinition();
			for (int i = 0; i < nrAttributes; i++) {
				String a = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_DEF_ELEMENT);
				String b = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_DEF_ATTRIBUTE);
				this.setDefiningAttribute(a, b);
			}

			for (int i = 0; i < nrFields; i++) {
				XMLInputSaxField field = new XMLInputSaxField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
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
				field.setTrimType(XMLInputSaxField.getTrimType(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setRepeated(rep.getStepAttributeBoolean(id_step, i,
						STEP_ATTRIBUTE_FIELD_REPEAT));

				String fieldPositionCode = rep.getStepAttributeString(id_step,
						i, STEP_ATTRIBUTE_FIELD_POSITION_CODE);
				if (fieldPositionCode != null) {
					field.setFieldPosition(fieldPositionCode);
				}

				inputFields[i] = field;
			}

			for (int i = 0; i < nrPositions; i++) {
				String encoded = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_INPUT_POSITION);
				inputPosition[i] = new XMLInputSaxFieldPosition(encoded);
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
			rep.saveStepAttribute(id_transformation, id_step, "include",
					includeFilename);
			rep.saveStepAttribute(id_transformation, id_step, "include_field",
					filenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum",
					includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumberField);
			rep
					.saveStepAttribute(id_transformation, id_step, "limit",
							rowLimit);

			for (int i = 0; i < fileName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_name", fileName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_mask", fileMask[i]);
			}

			for (int i = 0; i < this.getDefinitionLength(); i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"def_element", this.getDefiningElement(i));
				rep.saveStepAttribute(id_transformation, id_step, i,
						"def_attribute", this.getDefiningAttribute(i));
			}

			for (int i = 0; i < inputFields.length; i++) {
				XMLInputSaxField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());
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
						"field_trim_type", field.getTrimTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_repeat", field.isRepeated());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_position_code", field.getFieldPositionsCode());
			}

			for (int i = 0; i < inputPosition.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"input_position", inputPosition[i].toString());
			}
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public String[] getFilePaths(VariableSpace space) {
		String[] fileRequired = new String[fileName.length];
		for (int i = 0; i < fileRequired.length; i++)
			fileRequired[i] = "N"; // $NON-NLS-1$
		return FileInputList.createFilePathList(space, fileName, fileMask,
				fileRequired);
	}

	public FileInputList getTextFileList(VariableSpace space) {
		String[] fileRequired = new String[fileName.length];
		for (int i = 0; i < fileRequired.length; i++)
			fileRequired[i] = "N"; // $NON-NLS-1$
		return FileInputList.createFileList(space, fileName, fileMask,
				fileRequired);
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepinfo, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we get input...
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					"This step is not expecting nor reading any input",
					stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					"Not receiving any input from other steps.", stepinfo);
			remarks.add(cr);
		}

		String files[] = getFilePaths(transMeta);
		if (files == null || files.length == 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
					"No files can be found to read.", stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					"This step is reading " + files.length + " files.",
					stepinfo);
			remarks.add(cr);
		}

		if (getInputPosition().length == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					"No location elements given. Please specify the location of the repeating node in the XML document.",
					stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					"At least one location element specified.", stepinfo);
			remarks.add(cr);
		}

		if (getInputFields().length == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					"No field elements given. Please specify the fields you wish to extract from the XML document.",
					stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK,
					"At least one field element specified.", stepinfo);
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new XMLInputSax(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new XMLInputSaxData();
	}

	/**
	 * @return Returns the inputPosition.
	 */
	public XMLInputSaxFieldPosition[] getInputPosition() {
		return inputPosition;
	}

	/**
	 * @param inputPosition
	 *            The inputPosition to set.
	 */
	public void setInputPosition(XMLInputSaxFieldPosition[] inputPosition) {
		this.inputPosition = inputPosition;
	}

	public void clearDefinition() {
		definitionElement.clear();
		definitionAttribute.clear();
	}

	public String getDefiningAttribute(String elementName) {
		for (int i = 0; i < definitionElement.size(); i++) {
			if (definitionElement.get(i).equals(elementName)) {
				return (String) definitionAttribute.get(i);
			}
		}

		// Also look for a normal attribute...
		for (int i = 0; i < inputFields.length; i++) {
			XMLInputSaxField field = inputFields[i];
			XMLInputSaxFieldPosition positions[] = field.getFieldPosition();
			for (int p = 0; p < positions.length; p++) {
				XMLInputSaxFieldPosition position = positions[p];
				if (position.getType() == XMLInputSaxFieldPosition.XML_ATTRIBUTE) {
					return position.getName();
				}
			}
		}
		return null;
	}

	public int getDefiningAttributeNormalID(String attributeName) {

		// look for a normal attribute...
		for (int i = 0; i < inputFields.length; i++) {
			XMLInputSaxField field = inputFields[i];
			XMLInputSaxFieldPosition positions[] = field.getFieldPosition();
			for (int p = 0; p < positions.length; p++) {
				XMLInputSaxFieldPosition position = positions[p];
				if (position.getType() == XMLInputSaxFieldPosition.XML_ATTRIBUTE
						&& position.getName().equals(attributeName)) {
					return i;
				}
			}
		}
		return -1;
	}

	public void setDefiningAttribute(String elementName, String attributeName) {
		int index = definitionElement.indexOf(elementName);
		if (index >= 0) {
			definitionAttribute.set(index, attributeName);
		} else {
			definitionAttribute.add(attributeName);
			definitionElement.add(elementName);
		}
	}

	public String getDefiningAttribute(int i) {
		return (String) definitionAttribute.get(i);
	}

	public String getDefiningElement(int i) {
		return (String) definitionElement.get(i);
	}

	public int getDefinitionLength() {
		return definitionElement.size();
	}

	@Override
	public List<ResourceReference> getResourceDependencies(TransMeta transMeta,
			StepMeta stepInfo) {
		List<ResourceReference> references = new ArrayList<ResourceReference>(5);
		ResourceReference reference = new ResourceReference(stepInfo);
		references.add(reference);

		String[] textFiles = getFilePaths(transMeta);
		if (textFiles != null) {
			for (int i = 0; i < textFiles.length; i++) {
				reference.getEntries().add(
						new ResourceEntry(textFiles[i], ResourceType.FILE));
			}
		}
		return references;
	}

}
