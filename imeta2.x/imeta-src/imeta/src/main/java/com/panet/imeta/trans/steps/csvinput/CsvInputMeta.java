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

package com.panet.imeta.trans.steps.csvinput;

import java.util.ArrayList;
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
import com.panet.imeta.resource.ResourceEntry;
import com.panet.imeta.resource.ResourceReference;
import com.panet.imeta.resource.ResourceEntry.ResourceType;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;
import com.panet.imeta.trans.steps.textfileinput.InputFileMetaInterface;
import com.panet.imeta.trans.steps.textfileinput.TextFileInputField;
import com.panet.imeta.trans.steps.textfileinput.TextFileInputMeta;

/**
 * @since 2007-07-05
 * @author matt
 * @version 3.0
 */

@Step(name = "CsvInput", image = "TFI.png", tooltip = "BaseStep.TypeTooltipDesc.CsvInput", description = "BaseStep.TypeLongDesc.CsvInput", category = StepCategory.CATEGORY_INPUT)
public class CsvInputMeta extends BaseStepMeta implements StepMetaInterface,
		InputFileMetaInterface {
	public static final String STEP_ATTRIBUTE_FILENAME = "filename";
	public static final String STEP_ATTRIBUTE_FILENAME_FIELD = "filename_field";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_INCLUDE_FILENAME = "include_filename";
	public static final String STEP_ATTRIBUTE_SEPARATOR = "separator";
	public static final String STEP_ATTRIBUTE_ENCLOSURE = "enclosure";
	public static final String STEP_ATTRIBUTE_HEADER = "header";
	public static final String STEP_ATTRIBUTE_BUFFER_SIZE = "buffer_size";
	public static final String STEP_ATTRIBUTE_LAZY_CONVERSION = "lazy_conversion";
	public static final String STEP_ATTRIBUTE_ADD_FILENAME_RESULT = "add_filename_result";
	public static final String STEP_ATTRIBUTE_PARALLEL = "parallel";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";

	private String filename;

	private String filenameField;

	private boolean includingFilename;

	private String rowNumField;

	private boolean headerPresent;

	private String delimiter;
	private String enclosure;

	private String bufferSize;

	private boolean lazyConversionActive;

	private TextFileInputField[] inputFields;

	private boolean isaddresult;

	private boolean runningInParallel;

	private String encoding;

	public CsvInputMeta() {
		super(); // allocate BaseStepMeta
		allocate(0);
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	public void setDefault() {
		delimiter = ",";
		enclosure = "\"";
		headerPresent = true;
		lazyConversionActive = true;
		isaddresult = false;
		bufferSize = "50000";
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			filename = XMLHandler.getTagValue(stepnode, "filename");
			filenameField = XMLHandler.getTagValue(stepnode, "filename_field");
			rowNumField = XMLHandler.getTagValue(stepnode, "rownum_field");
			includingFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include_filename"));
			delimiter = XMLHandler.getTagValue(stepnode, "separator");
			enclosure = XMLHandler.getTagValue(stepnode, "enclosure");
			bufferSize = XMLHandler.getTagValue(stepnode, "buffer_size");
			headerPresent = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "header"));
			lazyConversionActive = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "lazy_conversion"));
			isaddresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"add_filename_result"));
			runningInParallel = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "parallel"));
			encoding = XMLHandler.getTagValue(stepnode, "encoding");

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				inputFields[i] = new TextFileInputField();

				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				inputFields[i].setName(XMLHandler.getTagValue(fnode, "name"));
				inputFields[i].setType(ValueMeta.getType(XMLHandler
						.getTagValue(fnode, "type")));
				inputFields[i].setFormat(XMLHandler
						.getTagValue(fnode, "format"));
				inputFields[i].setCurrencySymbol(XMLHandler.getTagValue(fnode,
						"currency"));
				inputFields[i].setDecimalSymbol(XMLHandler.getTagValue(fnode,
						"decimal"));
				inputFields[i].setGroupSymbol(XMLHandler.getTagValue(fnode,
						"group"));
				inputFields[i].setLength(Const.toInt(XMLHandler.getTagValue(
						fnode, "length"), -1));
				inputFields[i].setPrecision(Const.toInt(XMLHandler.getTagValue(
						fnode, "precision"), -1));
				inputFields[i].setTrimType(ValueMeta
						.getTrimTypeByCode(XMLHandler.getTagValue(fnode,
								"trim_type")));
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void allocate(int nrFields) {
		inputFields = new TextFileInputField[nrFields];
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(500);

		retval.append("    ").append(
				XMLHandler.addTagValue("filename", filename));
		retval.append("    ").append(
				XMLHandler.addTagValue("filename_field", filenameField));
		retval.append("    ").append(
				XMLHandler.addTagValue("rownum_field", rowNumField));
		retval.append("    ").append(
				XMLHandler.addTagValue("include_filename", includingFilename));
		retval.append("    ").append(
				XMLHandler.addTagValue("separator", delimiter));
		retval.append("    ").append(
				XMLHandler.addTagValue("enclosure", enclosure));
		retval.append("    ").append(
				XMLHandler.addTagValue("header", headerPresent));
		retval.append("    ").append(
				XMLHandler.addTagValue("buffer_size", bufferSize));
		retval.append("    ")
				.append(
						XMLHandler.addTagValue("lazy_conversion",
								lazyConversionActive));
		retval.append("    ").append(
				XMLHandler.addTagValue("add_filename_result", isaddresult));
		retval.append("    ").append(
				XMLHandler.addTagValue("parallel", runningInParallel));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < inputFields.length; i++) {
			TextFileInputField field = inputFields[i];

			retval.append("      <field>").append(Const.CR);
			retval.append("        ").append(
					XMLHandler.addTagValue("name", field.getName()));
			retval.append("        ").append(
					XMLHandler.addTagValue("type", ValueMeta.getTypeDesc(field
							.getType())));
			retval.append("        ").append(
					XMLHandler.addTagValue("format", field.getFormat()));
			retval.append("        ").append(
					XMLHandler.addTagValue("currency", field
							.getCurrencySymbol()));
			retval.append("        ")
					.append(
							XMLHandler.addTagValue("decimal", field
									.getDecimalSymbol()));
			retval.append("        ").append(
					XMLHandler.addTagValue("group", field.getGroupSymbol()));
			retval.append("        ").append(
					XMLHandler.addTagValue("length", field.getLength()));
			retval.append("        ").append(
					XMLHandler.addTagValue("precision", field.getPrecision()));
			retval.append("        ").append(
					XMLHandler.addTagValue("trim_type", ValueMeta
							.getTrimTypeCode(field.getTrimType())));
			retval.append("      </field>").append(Const.CR);
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		filename = BaseStepMeta.parameterToString(p.get(id + ".filename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".filenameField"));
		rowNumField = BaseStepMeta
				.parameterToString(p.get(id + ".rowNumField"));
		includingFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includingFilename"));
		delimiter = BaseStepMeta.parameterToString(p.get(id + ".delimiter"));
		enclosure = BaseStepMeta.parameterToString(p.get(id + ".enclosure"));
		headerPresent = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".headerPresent"));
		bufferSize = BaseStepMeta.parameterToString(p.get(id + ".bufferSize"));
		lazyConversionActive = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".lazyConversionActive"));
		isaddresult = BaseStepMeta.parameterToBoolean(p
				.get(id + ".isaddresult"));
		runningInParallel = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".runningInParallel"));
		encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));

		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] fieldType = p.get(id + "_fields.fieldType");
		String[] fieldFormat = p.get(id + "_fields.fieldFormat");
		String[] fieldLength = p.get(id + "_fields.fieldLength");
		String[] fieldPrecision = p.get(id + "_fields.fieldPrecision");
		String[] fieldCurrency = p.get(id + "_fields.fieldCurrency");
		String[] fieldDecimal = p.get(id + "_fields.fieldDecimal");
		String[] fieldGroup = p.get(id + "_fields.fieldGroup");
		String[] fieldTrimType = p.get(id + "_fields.fieldTrimType");

		if (fieldName != null && fieldName.length > 0) {
			this.inputFields = new TextFileInputField[fieldName.length];
			for (int i = 0; i < fieldName.length; i++) {
				this.inputFields[i] = new TextFileInputField();
				this.inputFields[i].setName(fieldName[i]);
				this.inputFields[i].setType(Const.toInt(fieldType[i], 0));
				this.inputFields[i].setFormat(fieldFormat[i]);
				this.inputFields[i].setLength(Const.toInt(fieldLength[i], -1));
				this.inputFields[i].setPrecision(Const.toInt(fieldPrecision[i],
						-1));
				this.inputFields[i].setCurrencySymbol(fieldCurrency[i]);
				this.inputFields[i].setDecimalSymbol(fieldDecimal[i]);
				this.inputFields[i].setGroupSymbol(fieldGroup[i]);
				this.inputFields[i].setTrimType(Const
						.toInt(fieldTrimType[i], 0));
			}
		}else{
			this.inputFields = new TextFileInputField[0];
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			filename = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILENAME);
			filenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILENAME_FIELD);
			rowNumField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			includingFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE_FILENAME);
			delimiter = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_SEPARATOR);
			enclosure = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCLOSURE);
			headerPresent = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_HEADER);
			bufferSize = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_BUFFER_SIZE);
			lazyConversionActive = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_LAZY_CONVERSION);
			isaddresult = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ADD_FILENAME_RESULT);
			runningInParallel = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_PARALLEL);
			encoding = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCODING);

			int nrfields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				inputFields[i] = new TextFileInputField();

				inputFields[i].setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				inputFields[i].setType(ValueMeta.getType(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TYPE)));
				inputFields[i].setFormat(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_FORMAT));
				inputFields[i].setCurrencySymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_CURRENCY));
				inputFields[i].setDecimalSymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_DECIMAL));
				inputFields[i].setGroupSymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_GROUP));
				inputFields[i].setLength((int) rep.getStepAttributeInteger(
						id_step, i, STEP_ATTRIBUTE_FIELD_LENGTH));
				inputFields[i].setPrecision((int) rep.getStepAttributeInteger(
						id_step, i, STEP_ATTRIBUTE_FIELD_PRECISION));
				inputFields[i].setTrimType(ValueMeta.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
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
			rep.saveStepAttribute(id_transformation, id_step, "filename",
					filename);
			rep.saveStepAttribute(id_transformation, id_step, "filename_field",
					filenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumField);
			rep.saveStepAttribute(id_transformation, id_step,
					"include_filename", includingFilename);
			rep.saveStepAttribute(id_transformation, id_step, "separator",
					delimiter);
			rep.saveStepAttribute(id_transformation, id_step, "enclosure",
					enclosure);
			rep.saveStepAttribute(id_transformation, id_step, "buffer_size",
					bufferSize);
			rep.saveStepAttribute(id_transformation, id_step, "header",
					headerPresent);
			rep.saveStepAttribute(id_transformation, id_step,
					"lazy_conversion", lazyConversionActive);
			rep.saveStepAttribute(id_transformation, id_step,
					"add_filename_result", isaddresult);
			rep.saveStepAttribute(id_transformation, id_step, "parallel",
					runningInParallel);
			rep.saveStepAttribute(id_transformation, id_step, "encoding",
					encoding);

			for (int i = 0; i < inputFields.length; i++) {
				TextFileInputField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_type", ValueMeta.getTypeDesc(field.getType()));
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
						"field_trim_type", ValueMeta.getTrimTypeCode(field
								.getTrimType()));
			}
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public void getFields(RowMetaInterface rowMeta, String origin,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		rowMeta.clear(); // Start with a clean slate, eats the input

		for (int i = 0; i < inputFields.length; i++) {
			TextFileInputField field = inputFields[i];

			ValueMetaInterface valueMeta = new ValueMeta(field.getName(), field
					.getType());
			valueMeta.setConversionMask(field.getFormat());
			valueMeta.setLength(field.getLength());
			valueMeta.setPrecision(field.getPrecision());
			valueMeta.setConversionMask(field.getFormat());
			valueMeta.setDecimalSymbol(field.getDecimalSymbol());
			valueMeta.setGroupingSymbol(field.getGroupSymbol());
			valueMeta.setCurrencySymbol(field.getCurrencySymbol());
			valueMeta.setTrimType(field.getTrimType());
			if (lazyConversionActive)
				valueMeta
						.setStorageType(ValueMetaInterface.STORAGE_TYPE_BINARY_STRING);
			valueMeta.setStringEncoding(space.environmentSubstitute(encoding));

			// In case we want to convert Strings...
			// Using a copy of the valueMeta object means that the inner and
			// outer representation format is the same.
			// Preview will show the data the same way as we read it.
			// This layout is then taken further down the road by the metadata
			// through the transformation.
			//
			ValueMetaInterface storageMetadata = valueMeta.clone();
			storageMetadata.setType(ValueMetaInterface.TYPE_STRING);
			storageMetadata
					.setStorageType(ValueMetaInterface.STORAGE_TYPE_NORMAL);
			storageMetadata.setLength(-1, -1); // we don't really know the
			// lengths of the strings read
			// in advance.
			valueMeta.setStorageMetadata(storageMetadata);

			valueMeta.setOrigin(origin);

			rowMeta.addValueMeta(valueMeta);
		}

		if (!Const.isEmpty(filenameField) && includingFilename) {
			ValueMetaInterface filenameMeta = new ValueMeta(filenameField,
					ValueMetaInterface.TYPE_STRING);
			filenameMeta.setOrigin(origin);
			if (lazyConversionActive) {
				filenameMeta
						.setStorageType(ValueMetaInterface.STORAGE_TYPE_BINARY_STRING);
				filenameMeta.setStorageMetadata(new ValueMeta(filenameField,
						ValueMetaInterface.TYPE_STRING));
			}
			rowMeta.addValueMeta(filenameMeta);
		}

		if (!Const.isEmpty(rowNumField)) {
			ValueMetaInterface rowNumMeta = new ValueMeta(rowNumField,
					ValueMetaInterface.TYPE_INTEGER);
			rowNumMeta.setLength(10);
			rowNumMeta.setOrigin(origin);
			rowMeta.addValueMeta(rowNumMeta);
		}

	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepinfo, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("CsvInputMeta.CheckResult.NotReceivingFields"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString(
									"CsvInputMeta.CheckResult.StepRecevingData", prev.size() + ""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("CsvInputMeta.CheckResult.StepRecevingData2"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("CsvInputMeta.CheckResult.NoInputReceivedFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new CsvInput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new CsvInputData();
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the bufferSize
	 */
	public String getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return true if lazy conversion is turned on: conversions are delayed as
	 *         long as possible, perhaps to never occur at all.
	 */
	public boolean isLazyConversionActive() {
		return lazyConversionActive;
	}

	/**
	 * @param lazyConversionActive
	 *            true if lazy conversion is to be turned on: conversions are
	 *            delayed as long as possible, perhaps to never occur at all.
	 */
	public void setLazyConversionActive(boolean lazyConversionActive) {
		this.lazyConversionActive = lazyConversionActive;
	}

	/**
	 * @return the headerPresent
	 */
	public boolean isHeaderPresent() {
		return headerPresent;
	}

	/**
	 * @param headerPresent
	 *            the headerPresent to set
	 */
	public void setHeaderPresent(boolean headerPresent) {
		this.headerPresent = headerPresent;
	}

	/**
	 * @return the enclosure
	 */
	public String getEnclosure() {
		return enclosure;
	}

	/**
	 * @param enclosure
	 *            the enclosure to set
	 */
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	@Override
	public List<ResourceReference> getResourceDependencies(TransMeta transMeta,
			StepMeta stepInfo) {
		List<ResourceReference> references = new ArrayList<ResourceReference>(5);

		ResourceReference reference = new ResourceReference(stepInfo);
		references.add(reference);
		if (!Const.isEmpty(filename)) {
			// Add the filename to the references, including a reference to this
			// step meta data.
			//
			reference.getEntries().add(
					new ResourceEntry(
							transMeta.environmentSubstitute(filename),
							ResourceType.FILE));
		}
		return references;
	}

	/**
	 * @return the inputFields
	 */
	public TextFileInputField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            the inputFields to set
	 */
	public void setInputFields(TextFileInputField[] inputFields) {
		this.inputFields = inputFields;
	}

	public int getFileFormatTypeNr() {
		return TextFileInputMeta.FILE_FORMAT_MIXED; // TODO: check this
	}

	public String[] getFilePaths(VariableSpace space) {
		return new String[] { space.environmentSubstitute(filename), };
	}

	public int getNrHeaderLines() {
		return 1;
	}

	public boolean hasHeader() {
		return isHeaderPresent();
	}

	public String getErrorCountField() {
		return null;
	}

	public String getErrorFieldsField() {
		return null;
	}

	public String getErrorTextField() {
		return null;
	}

	public String getEscapeCharacter() {
		return null;
	}

	public String getFileType() {
		return "CSV";
	}

	public String getSeparator() {
		return delimiter;
	}

	public boolean includeFilename() {
		return false;
	}

	public boolean includeRowNumber() {
		return false;
	}

	public boolean isErrorIgnored() {
		return false;
	}

	public boolean isErrorLineSkipped() {
		return false;
	}

	/**
	 * @return the filenameField
	 */
	public String getFilenameField() {
		return filenameField;
	}

	/**
	 * @param filenameField
	 *            the filenameField to set
	 */
	public void setFilenameField(String filenameField) {
		this.filenameField = filenameField;
	}

	/**
	 * @return the includingFilename
	 */
	public boolean isIncludingFilename() {
		return includingFilename;
	}

	/**
	 * @param includingFilename
	 *            the includingFilename to set
	 */
	public void setIncludingFilename(boolean includingFilename) {
		this.includingFilename = includingFilename;
	}

	/**
	 * @return the rowNumField
	 */
	public String getRowNumField() {
		return rowNumField;
	}

	/**
	 * @param rowNumField
	 *            the rowNumField to set
	 */
	public void setRowNumField(String rowNumField) {
		this.rowNumField = rowNumField;
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
	 * @return the runningInParallel
	 */
	public boolean isRunningInParallel() {
		return runningInParallel;
	}

	/**
	 * @param runningInParallel
	 *            the runningInParallel to set
	 */
	public void setRunningInParallel(boolean runningInParallel) {
		this.runningInParallel = runningInParallel;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
