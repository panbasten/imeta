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

package com.panet.imeta.trans.steps.fixedinput;

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

/**
 * @since 2007-07-05
 * @author matt
 * @version 3.0
 */

@Step(name = "FixedInput", image = "TFI.png", tooltip = "BaseStep.TypeTooltipDesc.FixedInput", description = "BaseStep.TypeLongDesc.FixedInput", category = StepCategory.CATEGORY_INPUT)
public class FixedInputMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_FILENAME = "filename";
	public static final String STEP_ATTRIBUTE_LINE_WIDTH = "line_width";
	public static final String STEP_ATTRIBUTE_HEADER = "header";
	public static final String STEP_ATTRIBUTE_LINE_FEED = "line_feed";
	public static final String STEP_ATTRIBUTE_BUFFER_SIZE = "buffer_size";
	public static final String STEP_ATTRIBUTE_LAZY_CONVERSION = "lazy_conversion";
	public static final String STEP_ATTRIBUTE_PARALLEL = "parallel";
	public static final String STEP_ATTRIBUTE_FILE_TYPE = "file_type";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAME = "add_to_result_filenames";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_WIDTH = "field_width";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final int FILE_TYPE_NONE = 0;
	public static final int FILE_TYPE_UNIX = 1;
	public static final int FILE_TYPE_DOS = 2;

	public static final String[] fileTypeCode = new String[] { "NONE", "UNIX",
			"DOS", };
	public static final String[] fileTypeDesc = new String[] {
			Messages.getString("FixedFileInputMeta.FileType.None.Desc"),
			Messages.getString("FixedFileInputMeta.FileType.Unix.Desc"),
			Messages.getString("FixedFileInputMeta.FileType.Dos.Desc"), };

	private String filename;

	private boolean headerPresent;

	private String lineWidth;

	private String bufferSize;

	private boolean lazyConversionActive;

	private boolean lineFeedPresent;

	private boolean runningInParallel;

	private int fileType;

	private boolean isaddresult;

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	private FixedFileInputField fieldDefinition[];

	public FixedInputMeta() {
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
		isaddresult = false;
		lineWidth = "80";
		headerPresent = true;
		lazyConversionActive = true;
		bufferSize = "50000";
		lineFeedPresent = true;
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		filename = BaseStepMeta.parameterToString(p.get(id + ".filename"));
		lineWidth = BaseStepMeta.parameterToString(p.get(id + ".lineWidth"));
		headerPresent = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".headerPresent"));
		lineFeedPresent = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".lineFeedPresent"));
		bufferSize = BaseStepMeta.parameterToString(p.get(id + ".bufferSize"));
		lazyConversionActive = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".lazyConversionActive"));
		runningInParallel = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".runningInParallel"));
		fileType = BaseStepMeta.parameterToInt(p.get(id + ".fileType"));
		fileType = (fileType < 0) ? 0 : fileType;
		encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));
		isaddresult = BaseStepMeta.parameterToBoolean(p
				.get(id + ".isaddresult"));

		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] fieldType = p.get(id + "_fields.fieldType");
		String[] fieldFormat = p.get(id + "_fields.fieldFormat");
		String[] fieldWidth = p.get(id + "_fields.fieldWidth");
		String[] fieldLength = p.get(id + "_fields.fieldLength");
		String[] fieldPrecision = p.get(id + "_fields.fieldPrecision");
		String[] fieldCurrency = p.get(id + "_fields.fieldCurrency");
		String[] fieldDecimal = p.get(id + "_fields.fieldDecimal");
		String[] fieldGroup = p.get(id + "_fields.fieldGroup");
		String[] fieldTrimType = p.get(id + "_fields.fieldTrimType");

		if (fieldName != null && fieldName.length > 0) {
			this.fieldDefinition = new FixedFileInputField[fieldName.length];
			for (int i = 0; i < fieldName.length; i++) {
				this.fieldDefinition[i] = new FixedFileInputField();
				this.fieldDefinition[i].setName(fieldName[i]);
				this.fieldDefinition[i].setType(Const.toInt(fieldType[i], 0));
				this.fieldDefinition[i].setFormat(fieldFormat[i]);
				this.fieldDefinition[i].setWidth(Const.toInt(fieldWidth[i], 0));
				this.fieldDefinition[i].setLength(Const.toInt(fieldLength[i],
						-1));
				this.fieldDefinition[i].setPrecision(Const.toInt(
						fieldPrecision[i], -1));
				this.fieldDefinition[i].setCurrency(fieldCurrency[i]);
				this.fieldDefinition[i].setDecimal(fieldDecimal[i]);
				this.fieldDefinition[i].setGrouping(fieldGroup[i]);
				this.fieldDefinition[i].setTrimType(Const.toInt(
						fieldTrimType[i], 0));
			}
		} else {
			this.fieldDefinition = new FixedFileInputField[0];
		}
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			filename = XMLHandler.getTagValue(stepnode, "filename");
			lineWidth = XMLHandler.getTagValue(stepnode, "line_width");
			bufferSize = XMLHandler.getTagValue(stepnode, "buffer_size");
			headerPresent = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "header"));
			lineFeedPresent = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "line_feed"));
			lazyConversionActive = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "lazy_conversion"));
			runningInParallel = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "parallel"));
			fileType = getFileType(XMLHandler
					.getTagValue(stepnode, "file_type"));
			encoding = XMLHandler.getTagValue(stepnode, "encoding");
			isaddresult = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"add_to_result_filenames"));

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				fieldDefinition[i] = new FixedFileInputField(fnode);
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void allocate(int nrFields) {
		fieldDefinition = new FixedFileInputField[nrFields];
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(500);

		retval.append("    ").append(
				XMLHandler.addTagValue("filename", filename));
		retval.append("    ").append(
				XMLHandler.addTagValue("line_width", lineWidth));
		retval.append("    ").append(
				XMLHandler.addTagValue("header", headerPresent));
		retval.append("    ").append(
				XMLHandler.addTagValue("buffer_size", bufferSize));
		retval.append("    ")
				.append(
						XMLHandler.addTagValue("lazy_conversion",
								lazyConversionActive));
		retval.append("    ").append(
				XMLHandler.addTagValue("line_feed", lineFeedPresent));
		retval.append("    ").append(
				XMLHandler.addTagValue("parallel", runningInParallel));
		retval.append("    ").append(
				XMLHandler.addTagValue("file_type", getFileTypeCode()));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));

		retval.append("    ").append(
				XMLHandler.addTagValue("add_to_result_filenames", isaddresult));

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < fieldDefinition.length; i++) {
			retval.append(fieldDefinition[i].getXML());
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			filename = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILENAME);
			lineWidth = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_LINE_WIDTH);
			headerPresent = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_HEADER);
			lineFeedPresent = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_LINE_FEED);
			bufferSize = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_BUFFER_SIZE);
			lazyConversionActive = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_LAZY_CONVERSION);
			runningInParallel = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_PARALLEL);
			fileType = getFileType(rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILE_TYPE));
			encoding = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCODING);
			isaddresult = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAME);

			int nrfields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				FixedFileInputField field = new FixedFileInputField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				field.setType(ValueMeta.getType(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_TYPE)));
				field.setFormat(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_FORMAT));
				field.setTrimType(ValueMeta.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setCurrency(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_CURRENCY));
				field.setDecimal(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_DECIMAL));
				field.setGrouping(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_GROUP));
				field.setWidth((int) rep.getStepAttributeInteger(id_step, i,
						STEP_ATTRIBUTE_FIELD_WIDTH));
				field.setLength((int) rep.getStepAttributeInteger(id_step, i,
						STEP_ATTRIBUTE_FIELD_LENGTH));
				field.setPrecision((int) rep.getStepAttributeInteger(id_step,
						i, STEP_ATTRIBUTE_FIELD_PRECISION));

				fieldDefinition[i] = field;
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
			rep.saveStepAttribute(id_transformation, id_step, "line_width",
					lineWidth);
			rep.saveStepAttribute(id_transformation, id_step, "buffer_size",
					bufferSize);
			rep.saveStepAttribute(id_transformation, id_step, "header",
					headerPresent);
			rep.saveStepAttribute(id_transformation, id_step,
					"lazy_conversion", lazyConversionActive);
			rep.saveStepAttribute(id_transformation, id_step, "line_feed",
					lineFeedPresent);
			rep.saveStepAttribute(id_transformation, id_step, "parallel",
					runningInParallel);
			rep.saveStepAttribute(id_transformation, id_step, "file_type",
					getFileTypeCode(fileType));
			rep.saveStepAttribute(id_transformation, id_step, "encoding",
					encoding);
			rep.saveStepAttribute(id_transformation, id_step,
					"add_to_result_filenames", isaddresult);

			for (int i = 0; i < fieldDefinition.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", fieldDefinition[i].getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_type", ValueMeta.getTypeDesc(fieldDefinition[i]
								.getType()));
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_format", fieldDefinition[i].getFormat());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_trim_type", ValueMeta
								.getTrimTypeCode(fieldDefinition[i]
										.getTrimType()));
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_currency", fieldDefinition[i].getCurrency());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_decimal", fieldDefinition[i].getDecimal());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_group", fieldDefinition[i].getGrouping());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_width", fieldDefinition[i].getWidth());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_length", fieldDefinition[i].getLength());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_precision", fieldDefinition[i].getPrecision());
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
		for (int i = 0; i < fieldDefinition.length; i++) {
			FixedFileInputField field = fieldDefinition[i];

			ValueMetaInterface valueMeta = new ValueMeta(field.getName(), field
					.getType());
			valueMeta.setConversionMask(field.getFormat());
			valueMeta.setTrimType(field.getTrimType());
			valueMeta.setLength(field.getLength());
			valueMeta.setPrecision(field.getPrecision());
			valueMeta.setConversionMask(field.getFormat());
			valueMeta.setDecimalSymbol(field.getDecimal());
			valueMeta.setGroupingSymbol(field.getGrouping());
			valueMeta.setCurrencySymbol(field.getCurrency());
			valueMeta.setStringEncoding(space.environmentSubstitute(encoding));
			if (lazyConversionActive)
				valueMeta
						.setStorageType(ValueMetaInterface.STORAGE_TYPE_BINARY_STRING);

			// In case we want to convert Strings...
			//
			ValueMetaInterface storageMetadata = valueMeta.clone();
			storageMetadata.setType(ValueMetaInterface.TYPE_STRING);
			storageMetadata
					.setStorageType(ValueMetaInterface.STORAGE_TYPE_NORMAL);

			valueMeta.setStorageMetadata(storageMetadata);

			valueMeta.setOrigin(origin);

			rowMeta.addValueMeta(valueMeta);
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
							.getString("FixedInputMeta.CheckResult.NotReceivingFields"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString(
									"FixedInputMeta.CheckResult.StepRecevingData", prev.size() + ""), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (Const.isEmpty(filename)) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("FixedInputMeta.CheckResult.NoFilenameSpecified"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("FixedInputMeta.CheckResult.FilenameSpecified"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new FixedInput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new FixedInputData();
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
	 * @return the lineWidth
	 */
	public String getLineWidth() {
		return lineWidth;
	}

	/**
	 * @return the lineFeedPresent
	 */
	public boolean isLineFeedPresent() {
		return lineFeedPresent;
	}

	/**
	 * @param lineWidth
	 *            the lineWidth to set
	 */
	public void setLineWidth(String lineWidth) {
		this.lineWidth = lineWidth;
	}

	/**
	 * @param lineFeedPresent
	 *            the lineFeedPresent to set
	 */
	public void setLineFeedPresent(boolean lineFeedPresent) {
		this.lineFeedPresent = lineFeedPresent;
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
	 * @return the fieldDefinition
	 */
	public FixedFileInputField[] getFieldDefinition() {
		return fieldDefinition;
	}

	/**
	 * @param fieldDefinition
	 *            the fieldDefinition to set
	 */
	public void setFieldDefinition(FixedFileInputField[] fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
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
	 * @return the fileType
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public static final String getFileTypeCode(int fileType) {
		return fileTypeCode[fileType];
	}

	public static final String getFileTypeDesc(int fileType) {
		return fileTypeDesc[fileType];
	}

	public String getFileTypeCode() {
		return getFileTypeCode(fileType);
	}

	public String getFileTypeDesc() {
		return getFileTypeDesc(fileType);
	}

	public static final int getFileType(String fileTypeCode) {
		int t = Const.indexOfString(fileTypeCode, FixedInputMeta.fileTypeCode);
		if (t >= 0)
			return t;
		t = Const.indexOfString(fileTypeCode, FixedInputMeta.fileTypeDesc);
		if (t >= 0)
			return t;
		return FILE_TYPE_NONE;
	}

	public int getLineSeparatorLength() {
		if (isLineFeedPresent()) {
			switch (fileType) {
			case FILE_TYPE_NONE:
				return 0;
			case FILE_TYPE_UNIX:
				return 1;
			case FILE_TYPE_DOS:
				return 2;
			default:
				return 0;
			}
		} else {
			return 0;
		}
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

}
