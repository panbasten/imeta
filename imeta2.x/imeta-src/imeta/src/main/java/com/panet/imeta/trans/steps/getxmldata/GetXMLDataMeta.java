/*************************************************************************************** 
 * Copyright (C) 2007 Samatar, Brahim.  All rights reserved. 
 * This software was developed by Samatar, Brahim and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar, Brahim.  
 * The Initial Developer is Samatar, Brahim.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/

package com.panet.imeta.trans.steps.getxmldata;

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

/**
 * Store run-time data on the getXMLData step.
 */
public class GetXMLDataMeta extends BaseStepMeta implements StepMetaInterface {
	public static final String STEP_ATTRIBUTE_INCLUDE = "include";
	public static final String STEP_ATTRIBUTE_INCLUDE_FIELD = "include_field";
	public static final String STEP_ATTRIBUTE_ADDRESULTFILE = "addresultfile";
	public static final String STEP_ATTRIBUTE_NAMESPACEAWARE = "namespaceaware";
	public static final String STEP_ATTRIBUTE_IGNORECOMMENTS = "ignorecomments";
	public static final String STEP_ATTRIBUTE_READURL = "readurl";
	public static final String STEP_ATTRIBUTE_VALIDATING = "validating";
	public static final String STEP_ATTRIBUTE_USETOKEN = "usetoken";
	public static final String STEP_ATTRIBUTE_ISIGNOREEMPTYFILE = "IsIgnoreEmptyFile";
	public static final String STEP_ATTRIBUTE_DONOTFAILIFNOFILE = "doNotFailIfNoFile";
	public static final String STEP_ATTRIBUTE_ROWNUM = "rownum";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_LOOPXPATH = "loopxpath";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	// public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_MASK = "file_mask";
	public static final String STEP_ATTRIBUTE_FILE_REQUIRED = "file_required";
	// public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_XPATH = "field_xpath";
	public static final String STEP_ATTRIBUTE_ELEMENT_TYPE = "element_type";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type";
	public static final String STEP_ATTRIBUTE_FIELD_REPEAT = "field_repeat";
	public static final String STEP_ATTRIBUTE_ISINFIELDS = "IsInFields";
	public static final String STEP_ATTRIBUTE_ISAFILE = "IsAFile";
	public static final String STEP_ATTRIBUTE_XMLFIELD = "XmlField";
	public static final String STEP_ATTRIBUTE_PRUNEPATH = "prunePath";

	public static final String[] RequiredFilesDesc = new String[] {
			Messages.getString("System.Combo.No"),
			Messages.getString("System.Combo.Yes") };
	public static final String[] RequiredFilesCode = new String[] { "N", "Y" };

	/** Array of filenames */
	private String fileName[];

	/** Wildcard or filemask (regular expression) */
	private String fileMask[];

	/** Array of boolean values as string, indicating if a file is required. */
	private String fileRequired[];

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

	/** The number or lines to skip before starting to read */
	private String loopxpath;

	/** The fields to import... */
	private GetXMLDataField inputFields[];

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	/** Is In fields */
	private String XmlField;

	/** Is In fields */
	private boolean IsInFields;

	/** Is a File */
	private boolean IsAFile;

	/** Flag: add result filename * */
	private boolean addResultFile;

	/** Flag: set Namespace aware * */
	private boolean nameSpaceAware;

	/** Flag: set XML Validating * */
	private boolean validating;

	/** Flag : do we process use tokens? */
	private boolean usetoken;

	/** Flag : do we ignore empty files */
	private boolean IsIgnoreEmptyFile;

	/** Flag : do not fail if no file */
	private boolean doNotFailIfNoFile;

	/** Flag : ignore comments */
	private boolean ignorecomments;

	/** Flag : read url as source */
	private boolean readurl;

	// Given this path activates the streaming algorithm to process large files
	private String prunePath;

	public GetXMLDataMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return the add result filesname flag
	 */
	public boolean addResultFile() {
		return addResultFile;
	}

	/**
	 * @return the validating flag
	 */
	public boolean isValidating() {
		return validating;
	}

	/**
	 * @param validating
	 *            the validating flag to set
	 */
	public void setValidating(boolean validating) {
		this.validating = validating;
	}

	/**
	 * @return the readurl flag
	 */
	public boolean isReadUrl() {
		return readurl;
	}

	/**
	 * @param readurl
	 *            the readurl flag to set
	 */
	public void setReadUrl(boolean readurl) {
		this.readurl = readurl;
	}

	public void setAddResultFile(boolean addResultFile) {
		this.addResultFile = addResultFile;
	}

	/**
	 * @return Returns the input fields.
	 */
	public GetXMLDataField[] getInputFields() {
		return inputFields;
	}

	/**
	 * @param inputFields
	 *            The input fields to set.
	 */
	public void setInputFields(GetXMLDataField[] inputFields) {
		this.inputFields = inputFields;
	}

	/**
	 * Get XML field.
	 */
	public String getXMLField() {
		return XmlField;
	}

	/**
	 * Set XML field.
	 */
	public void setXMLField(String XmlField) {
		this.XmlField = XmlField;
	}

	/**
	 * Get the IsInFields.
	 */
	public boolean getIsInFields() {
		return IsInFields;
	}

	/**
	 * Set the IsInFields.
	 */
	public void setIsInFields(boolean IsInFields) {
		this.IsInFields = IsInFields;
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

	public String[] getFileRequired() {
		return fileRequired;
	}

	public void setFileRequired(String[] fileRequiredin) {
		for (int i = 0; i < fileRequiredin.length; i++) {
			this.fileRequired[i] = getRequiredFilesCode(fileRequiredin[i]);
		}
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
	 * @return Returns the LoopXPath
	 */
	public String getLoopXPath() {
		return loopxpath;
	}

	/**
	 * @param loopxpath
	 *            The loopxpath to set.
	 */
	public void setLoopXPath(String loopxpath) {
		this.loopxpath = loopxpath;
	}

	/**
	 * @param usetoken
	 *            the "use token" flag to set
	 */
	public void setuseToken(boolean usetoken) {
		this.usetoken = usetoken;
	}

	/**
	 * @return the use token flag
	 */
	public boolean isuseToken() {
		return usetoken;
	}

	/**
	 * @return the IsIgnoreEmptyFile flag
	 */
	public boolean isIgnoreEmptyFile() {
		return IsIgnoreEmptyFile;
	}

	/**
	 * @param IsIgnoreEmptyFile
	 *            the IsIgnoreEmptyFile to set
	 */
	public void setIgnoreEmptyFile(boolean IsIgnoreEmptyFile) {
		this.IsIgnoreEmptyFile = IsIgnoreEmptyFile;
	}

	/**
	 * @return the doNotFailIfNoFile flag
	 */
	public boolean isdoNotFailIfNoFile() {
		return doNotFailIfNoFile;
	}

	/**
	 * @param doNotFailIfNoFile
	 *            the doNotFailIfNoFile to set
	 */
	public void setdoNotFailIfNoFile(boolean doNotFailIfNoFile) {
		this.doNotFailIfNoFile = doNotFailIfNoFile;
	}

	/**
	 * @return the ignorecomments flag
	 */
	public boolean isIgnoreComments() {
		return ignorecomments;
	}

	/**
	 * @param ignorecomments
	 *            the ignorecomments to set
	 */
	public void setIgnoreComments(boolean ignorecomments) {
		this.ignorecomments = ignorecomments;
	}

	/**
	 * @param nameSpaceAware
	 *            the name space aware flag to set
	 */
	public void setNamespaceAware(boolean nameSpaceAware) {
		this.nameSpaceAware = nameSpaceAware;
	}

	/**
	 * @return the name space aware flag
	 */
	public boolean isNamespaceAware() {
		return nameSpaceAware;
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

	public boolean getIsAFile() {
		return IsAFile;
	}

	public void setIsAFile(boolean IsAFile) {
		this.IsAFile = IsAFile;
	}

	/**
	 * @return the prunePath
	 */
	public String getPrunePath() {
		return prunePath;
	}

	/**
	 * @param prunePath
	 *            the prunePath to set
	 */
	public void setPrunePath(String prunePath) {
		this.prunePath = prunePath;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public Object clone() {
		GetXMLDataMeta retval = (GetXMLDataMeta) super.clone();

		int nrFiles = fileName.length;
		int nrFields = inputFields.length;

		retval.allocate(nrFiles, nrFields);

		for (int i = 0; i < nrFiles; i++) {
			retval.fileName[i] = fileName[i];
			retval.fileMask[i] = fileMask[i];
			retval.fileRequired[i] = fileRequired[i];
		}

		for (int i = 0; i < nrFields; i++) {
			if (inputFields[i] != null) {
				retval.inputFields[i] = (GetXMLDataField) inputFields[i]
						.clone();
			}
		}
		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(400);

		retval.append("    ").append(
				XMLHandler.addTagValue("include", includeFilename));
		retval.append("    ").append(
				XMLHandler.addTagValue("include_field", filenameField));
		retval.append("    ").append(
				XMLHandler.addTagValue("rownum", includeRowNumber));
		retval.append("    ").append(
				XMLHandler.addTagValue("addresultfile", addResultFile));
		retval.append("    ").append(
				XMLHandler.addTagValue("namespaceaware", nameSpaceAware));
		retval.append("    ").append(
				XMLHandler.addTagValue("ignorecomments", ignorecomments));
		retval.append("    ")
				.append(XMLHandler.addTagValue("readurl", readurl));
		retval.append("    ").append(
				XMLHandler.addTagValue("validating", validating));
		retval.append("    " + XMLHandler.addTagValue("usetoken", usetoken));
		retval.append("    "
				+ XMLHandler
						.addTagValue("IsIgnoreEmptyFile", IsIgnoreEmptyFile));
		retval.append("    "
				+ XMLHandler
						.addTagValue("doNotFailIfNoFile", doNotFailIfNoFile));

		retval.append("    ").append(
				XMLHandler.addTagValue("rownum_field", rowNumberField));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));

		retval.append("    <file>").append(Const.CR);
		for (int i = 0; i < fileName.length; i++) {
			retval.append("      ").append(
					XMLHandler.addTagValue("name", fileName[i]));
			retval.append("      ").append(
					XMLHandler.addTagValue("filemask", fileMask[i]));
			retval.append("      ").append(
					XMLHandler.addTagValue("file_required", fileRequired[i]));

		}
		retval.append("    </file>").append(Const.CR);

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < inputFields.length; i++) {
			GetXMLDataField field = inputFields[i];
			retval.append(field.getXML());
		}
		retval.append("    </fields>").append(Const.CR);

		retval.append("    ").append(XMLHandler.addTagValue("limit", rowLimit));
		retval.append("    ").append(
				XMLHandler.addTagValue("loopxpath", loopxpath));

		retval.append("    ").append(
				XMLHandler.addTagValue("IsInFields", IsInFields));
		retval.append("    ")
				.append(XMLHandler.addTagValue("IsAFile", IsAFile));

		retval.append("    ").append(
				XMLHandler.addTagValue("XmlField", XmlField));
		retval.append("    ").append(
				XMLHandler.addTagValue("prunePath", prunePath));

		return retval.toString();
	}

	public String getRequiredFilesDesc(String tt) {
		if (Const.isEmpty(tt))
			return RequiredFilesDesc[0];
		if (tt.equalsIgnoreCase(RequiredFilesCode[1]))
			return RequiredFilesDesc[1];
		else
			return RequiredFilesDesc[0];
	}

	public String getRequiredFilesCode(String tt) {
		if (tt == null)
			return RequiredFilesCode[0];
		if (tt.equals(RequiredFilesDesc[1]))
			return RequiredFilesCode[1];
		else
			return RequiredFilesCode[0];
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		includeFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeFilename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id
				+ ".filenameField"));
		addResultFile = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".addResultFile"));
		nameSpaceAware = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".nameSpaceAware"));
		ignorecomments = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".ignorecomments"));
		readurl = BaseStepMeta.parameterToBoolean(p.get(id + ".readurl"));
		validating = BaseStepMeta.parameterToBoolean(p.get(id + ".validating"));
		usetoken = BaseStepMeta.parameterToBoolean(p.get(id + ".usetoken"));
		IsIgnoreEmptyFile = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".IsIgnoreEmptyFile"));
		doNotFailIfNoFile = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".doNotFailIfNoFile"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".includeRowNumber"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id
				+ ".rowNumberField"));
		encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));
		rowLimit = BaseStepMeta.parameterToLong(p.get(id + ".rowLimit"));
		loopxpath = BaseStepMeta.parameterToString(p.get(id + ".loopxpath"));
		IsInFields = BaseStepMeta.parameterToBoolean(p.get(id + ".IsInFields"));
		IsAFile = BaseStepMeta.parameterToBoolean(p.get(id + ".IsAFile"));
		XmlField = BaseStepMeta.parameterToString(p.get(id + ".XmlField"));
		prunePath = BaseStepMeta.parameterToString(p.get(id + ".prunePath"));

		String[] fileName = p.get(id + "_selectedFiles.fileName");
		String[] fileMask = p.get(id + "_selectedFiles.fileMask");
		String[] fileRequired = p.get(id + "_selectedFiles.fileRequired");
		this.fileName = (fileName != null) ? fileName : (new String[0]);
		this.fileMask = (fileMask != null) ? fileMask : (new String[0]);
		this.fileRequired = (fileRequired != null) ? fileRequired
				: (new String[0]);

		String[] name = p.get(id + "_fields.fieldName");
		String[] xpath = p.get(id + "_fields.fieldXpath");
		String[] element = p.get(id + "_fields.fieldElement");
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
			this.inputFields = new GetXMLDataField[name.length];
			for (int i = 0; i < name.length; i++) {
				this.inputFields[i] = new GetXMLDataField();
				this.inputFields[i].setName(name[i]);
				this.inputFields[i].setXPath(xpath[i]);
				this.inputFields[i].setElementType(Const.toInt(element[i], 0));
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
			this.inputFields = new GetXMLDataField[0];
		}

	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			includeFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "include"));
			filenameField = XMLHandler.getTagValue(stepnode, "include_field");

			addResultFile = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "addresultfile"));
			nameSpaceAware = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "namespaceaware"));
			ignorecomments = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "ignorecomments"));

			readurl = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"readurl"));
			validating = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"validating"));
			usetoken = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"usetoken"));
			IsIgnoreEmptyFile = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "IsIgnoreEmptyFile"));
			doNotFailIfNoFile = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "doNotFailIfNoFile"));

			includeRowNumber = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "rownum"));
			rowNumberField = XMLHandler.getTagValue(stepnode, "rownum_field");
			encoding = XMLHandler.getTagValue(stepnode, "encoding");

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
				Node fileRequirednode = XMLHandler.getSubNodeByNr(filenode,
						"file_required", i);
				fileName[i] = XMLHandler.getNodeValue(filenamenode);
				fileMask[i] = XMLHandler.getNodeValue(filemasknode);
				fileRequired[i] = XMLHandler.getNodeValue(fileRequirednode);
			}

			for (int i = 0; i < nrFields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				GetXMLDataField field = new GetXMLDataField(fnode);
				inputFields[i] = field;
			}

			// Is there a limit on the number of rows we process?
			rowLimit = Const.toLong(XMLHandler.getTagValue(stepnode, "limit"),
					0L);
			// Do we skip rows before starting to read
			loopxpath = XMLHandler.getTagValue(stepnode, "loopxpath");

			IsInFields = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"IsInFields"));
			IsAFile = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"IsAFile"));

			XmlField = XMLHandler.getTagValue(stepnode, "XmlField");
			prunePath = XMLHandler.getTagValue(stepnode, "prunePath");
		} catch (Exception e) {
			throw new KettleXMLException(Messages.getString(
					"GetXMLDataMeta.Exception.ErrorLoadingXML", e.toString()));
		}
	}

	public void allocate(int nrfiles, int nrfields) {
		fileName = new String[nrfiles];
		fileMask = new String[nrfiles];
		fileRequired = new String[nrfiles];
		inputFields = new GetXMLDataField[nrfields];
	}

	public void setDefault() {
		usetoken = false;
		IsIgnoreEmptyFile = false;
		doNotFailIfNoFile = true;
		includeFilename = false;
		filenameField = "";
		includeRowNumber = false;
		rowNumberField = "";
		IsAFile = false;
		addResultFile = false;
		nameSpaceAware = false;
		ignorecomments = false;
		readurl = false;
		validating = false;

		int nrFiles = 0;
		int nrFields = 0;
		loopxpath = "";

		allocate(nrFiles, nrFields);

		for (int i = 0; i < nrFiles; i++) {
			fileName[i] = "filename" + (i + 1);
			fileMask[i] = "";
			fileRequired[i] = RequiredFilesCode[0];
		}

		for (int i = 0; i < nrFields; i++) {
			inputFields[i] = new GetXMLDataField("field" + (i + 1));
		}

		rowLimit = 0;

		IsInFields = false;
		XmlField = "";
		prunePath = "";
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface info[], StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		int i;
		for (i = 0; i < inputFields.length; i++) {
			GetXMLDataField field = inputFields[i];

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

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {

		try {
			includeFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_INCLUDE);
			filenameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_INCLUDE_FIELD);

			addResultFile = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ADDRESULTFILE);
			nameSpaceAware = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_NAMESPACEAWARE);
			ignorecomments = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_IGNORECOMMENTS);
			readurl = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_READURL);

			validating = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_VALIDATING);
			usetoken = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_USETOKEN);
			IsIgnoreEmptyFile = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ISIGNOREEMPTYFILE);
			doNotFailIfNoFile = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_DONOTFAILIFNOFILE);

			includeRowNumber = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ROWNUM);
			rowNumberField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ROWNUM_FIELD);
			rowLimit = rep.getStepAttributeInteger(id_step,
					STEP_ATTRIBUTE_LIMIT);
			loopxpath = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_LOOPXPATH);
			encoding = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCODING);

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
				fileRequired[i] = rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FILE_REQUIRED);
			}

			for (int i = 0; i < nrFields; i++) {
				GetXMLDataField field = new GetXMLDataField();

				field.setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				field.setXPath(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_XPATH));
				field.setElementType(GetXMLDataField.getElementTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_ELEMENT_TYPE)));
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
				field.setTrimType(GetXMLDataField.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				field.setRepeated(rep.getStepAttributeBoolean(id_step, i,
						STEP_ATTRIBUTE_FIELD_REPEAT));

				inputFields[i] = field;
			}
			IsInFields = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ISINFIELDS);
			IsAFile = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ISAFILE);

			XmlField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_XMLFIELD);
			prunePath = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_PRUNEPATH);
		} catch (Exception e) {
			throw new KettleException(
					Messages
							.getString("GetXMLDataMeta.Exception.ErrorReadingRepository"),
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
			rep.saveStepAttribute(id_transformation, id_step, "addresultfile",
					addResultFile);
			rep.saveStepAttribute(id_transformation, id_step, "namespaceaware",
					nameSpaceAware);
			rep.saveStepAttribute(id_transformation, id_step, "ignorecomments",
					ignorecomments);
			rep.saveStepAttribute(id_transformation, id_step, "readurl",
					readurl);

			rep.saveStepAttribute(id_transformation, id_step, "validating",
					validating);
			rep.saveStepAttribute(id_transformation, id_step, "usetoken",
					usetoken);
			rep.saveStepAttribute(id_transformation, id_step,
					"IsIgnoreEmptyFile", IsIgnoreEmptyFile);
			rep.saveStepAttribute(id_transformation, id_step,
					"doNotFailIfNoFile", doNotFailIfNoFile);

			rep.saveStepAttribute(id_transformation, id_step, "rownum",
					includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",
					rowNumberField);
			rep
					.saveStepAttribute(id_transformation, id_step, "limit",
							rowLimit);
			rep.saveStepAttribute(id_transformation, id_step, "loopxpath",
					loopxpath);
			rep.saveStepAttribute(id_transformation, id_step, "encoding",
					encoding);

			for (int i = 0; i < fileName.length; i++) {
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_name", fileName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_mask", fileMask[i]);
				rep.saveStepAttribute(id_transformation, id_step, i,
						"file_required", fileRequired[i]);
			}

			for (int i = 0; i < inputFields.length; i++) {
				GetXMLDataField field = inputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_xpath", field.getXPath());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"element_type", field.getElementTypeCode());
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
			rep.saveStepAttribute(id_transformation, id_step, "IsInFields",
					IsInFields);
			rep.saveStepAttribute(id_transformation, id_step, "IsAFile",
					IsAFile);

			rep.saveStepAttribute(id_transformation, id_step, "XmlField",
					XmlField);
			rep.saveStepAttribute(id_transformation, id_step, "prunePath",
					prunePath);

		} catch (Exception e) {
			throw new KettleException(Messages.getString(
					"GetXMLDataMeta.Exception.ErrorSavingToRepository", ""
							+ id_step), e);
		}
	}

	public FileInputList getFiles(VariableSpace space) {
		return FileInputList.createFileList(space, fileName, fileMask,
				fileRequired);
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we get input...
		if (input.length <= 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("GetXMLDataMeta.CheckResult.NoInputExpected"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
					.getString("GetXMLDataMeta.CheckResult.NoInput"), stepMeta);
			remarks.add(cr);
		}

		// control Xpath
		if (getLoopXPath() == null || Const.isEmpty(getLoopXPath())) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("GetXMLDataMeta.CheckResult.NoLoopXpath"),
					stepMeta);
			remarks.add(cr);
		}
		if (getInputFields().length <= 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
					.getString("GetXMLDataMeta.CheckResult.NoInputField"),
					stepMeta);
			remarks.add(cr);
		}

		if (getIsInFields()) {
			if (Const.isEmpty(getXMLField())) {
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
						.getString("GetXMLDataMeta.CheckResult.NoField"),
						stepMeta);
				remarks.add(cr);
			} else {
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
						.getString("GetXMLDataMeta.CheckResult.FieldOk"),
						stepMeta);
				remarks.add(cr);
			}
		} else {
			FileInputList fileInputList = getFiles(transMeta);
			// String files[] = getFiles();
			if (fileInputList == null || fileInputList.getFiles().size() == 0) {
				cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages
						.getString("GetXMLDataMeta.CheckResult.NoFiles"),
						stepMeta);
				remarks.add(cr);
			} else {
				cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages
						.getString("GetXMLDataMeta.CheckResult.FilesOk", ""
								+ fileInputList.getFiles().size()), stepMeta);
				remarks.add(cr);
			}
		}
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta tr,
			Trans trans) {
		return new GetXMLData(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData() {
		return new GetXMLDataData();
	}

	public boolean supportsErrorHandling() {
		return true;
	}

}
