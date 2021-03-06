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

package com.panet.imeta.trans.steps.textfileoutput;

import java.text.SimpleDateFormat;
import java.util.Date;
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

/*
 * Created on 4-apr-2003
 * 
 */
public class TextFileOutputMeta extends BaseStepMeta implements
		StepMetaInterface {

	public static final String STEP_ATTRIBUTE_SEPARATOR = "separator ";
	public static final String STEP_ATTRIBUTE_ENCLOSURE = "enclosure ";
	public static final String STEP_ATTRIBUTE_ENCLOSURE_FORCED = "enclosure_forced";
	public static final String STEP_ATTRIBUTE_HEADER = "header ";
	public static final String STEP_ATTRIBUTE_FOOTER = "footer ";
	public static final String STEP_ATTRIBUTE_FORMAT = "format ";
	public static final String STEP_ATTRIBUTE_COMPRESSION = "compression  ";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding  ";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name ";
	public static final String STEP_ATTRIBUTE_FILE_IS_COMMAND = "file_is_command ";
	public static final String STEP_ATTRIBUTE_DO_NOT_OPEN_NEW_FILE_INIT = "do_not_open_new_file_init";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention  ";
	public static final String STEP_ATTRIBUTE_FILE_APPEND = "file_append  ";
	public static final String STEP_ATTRIBUTE_FILE_SPLIT = "file_split";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR = "file_add_stepnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_PARTNR = "file_add_partnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE = "file_add_date";
	public static final String STEP_ATTRIBUTE_DATE_TIME_FORMAT = "date_time_format";
	public static final String STEP_ATTRIBUTE_SPECIFYFORMAT = "SpecifyFormat";
	public static final String STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES = "add_to_result_filenames  ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_TIME = "file_add_time";
	public static final String STEP_ATTRIBUTE_FILE_PAD = "file_pad  ";
	public static final String STEP_ATTRIBUTE_FILE_FAST_DUMP = "file_fast_dump  ";
	public static final String STEP_ATTRIBUTE_FILENAMEINFIELD = "fileNameInField ";
	public static final String STEP_ATTRIBUTE_FILENAMEFIELD = "fileNameField";

	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	public static final String STEP_ATTRIBUTE_FIELD_CURRENCY = "field_currency";
	public static final String STEP_ATTRIBUTE_FIELD_DECIMAL = "field_decimal ";
	public static final String STEP_ATTRIBUTE_FIELD_GROUP = "field_group ";
	public static final String STEP_ATTRIBUTE_FIELD_TRIM_TYPE = "field_trim_type ";
	public static final String STEP_ATTRIBUTE_FIELD_NULLIF = "field_nullif";
	public static final String STEP_ATTRIBUTE_FIELD_LENGTH = "field_length";
	public static final String STEP_ATTRIBUTE_FIELD_PRECISION = "field_precision ";
	public static final String STEP_ATTRIBUTE_ENDEDLINE = "endedLine";
	public static final String STEP_ATTRIBUTE_ZIPPED = "zipped";

	public static final int FILE_COMPRESSION_TYPE_NONE = 0;
	public static final int FILE_COMPRESSION_TYPE_ZIP = 1;
	public static final int FILE_COMPRESSION_TYPE_GZIP = 2;

	public static final String fileCompressionTypeCodes[] = new String[] {
			"None", "Zip", "GZip", }; // $NON-NLS-1$

	// private String fieldName[];
	//	
	// private String fieldType[];
	//	
	// private String fieldFormat[];
	//	
	// private int fieldLength[];
	//	
	// private String fieldCurrency[];
	//	
	// private int fieldPrecision[];
	//	
	// private String fieldDecimal[];
	//	
	// private String fieldGroup[];
	//	
	// private String fieldTrimType[];
	//	
	// private String fieldNullif[];

	/** The base name of the output file */
	private String fileName;

	/** Whether to treat this as a command to be executed and piped into */
	private boolean fileAsCommand;

	/** The file extention in case of a generated filename */
	private String extension;

	/** The separator to choose for the CSV file */
	private String separator;

	/** The enclosure to use in case the separator is part of a field's value */
	private String enclosure;

	/**
	 * Setting to allow the enclosure to be always surrounding a String value,
	 * even when there is no separator inside
	 */
	private boolean enclosureForced;

	/** Add a header at the top of the file? */
	private boolean headerEnabled;

	/** Add a footer at the bottom of the file? */
	private boolean footerEnabled;

	/** The file format: DOS or Unix */
	private String fileFormat;

	/** The file compression: None, Zip or Gzip */
	private String fileCompression;

	/**
	 * if this value is larger then 0, the text file is split up into parts of
	 * this number of lines
	 */
	private int splitEvery;

	/**
	 * Flag to indicate the we want to append to the end of an existing file (if
	 * it exists)
	 */
	private boolean fileAppended;

	/** Flag: add the stepnr in the filename */
	private boolean stepNrInFilename;

	/** Flag: add the partition number in the filename */
	private boolean partNrInFilename;

	/** Flag: add the date in the filename */
	private boolean dateInFilename;

	/** Flag: add the time in the filename */
	private boolean timeInFilename;

	/** Flag: pad fields to their specified length */
	private boolean padded;

	/** Flag: Fast dump data without field formatting */
	private boolean fastDump;

	/* THE FIELD SPECIFICATIONS ... */

	/** The output fields */
	private TextFileField outputFields[];

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	/**
	 * The string to use for append to end line of the whole file: null or empty
	 * string means no line needed
	 */
	private String endedLine;

	/* Specification if file name is in field */

	private boolean fileNameInField;

	private String fileNameField;

	/** Calculated value ... */
	private String newline;

	/** Flag: add the filenames to result filenames */
	private boolean addToResultFilenames;

	/** Flag : Do not open new file when transformation start */
	private boolean doNotOpenNewFileInit;

	private boolean specifyingFormat;

	private String dateTimeFormat;

	public TextFileOutputMeta() {
		super(); // allocate BaseStepMeta
	}

	/**
	 * @return FileAsCommand
	 */
	public boolean isFileAsCommand() {
		return fileAsCommand;
	}

	/**
	 * @param fileAsCommand
	 *            The fileAsCommand to set
	 */
	public void setFileAsCommand(boolean fileAsCommand) {
		this.fileAsCommand = fileAsCommand;
	}

	/**
	 * @return Returns the dateInFilename.
	 */
	public boolean isDateInFilename() {
		return dateInFilename;
	}

	/**
	 * @param dateInFilename
	 *            The dateInFilename to set.
	 */
	public void setDateInFilename(boolean dateInFilename) {
		this.dateInFilename = dateInFilename;
	}

	/**
	 * @return Returns the enclosure.
	 */
	public String getEnclosure() {
		return enclosure;
	}

	/**
	 * @param enclosure
	 *            The enclosure to set.
	 */
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	/**
	 * @return Returns the enclosureForced.
	 */
	public boolean isEnclosureForced() {
		return enclosureForced;
	}

	/**
	 * @param enclosureForced
	 *            The enclosureForced to set.
	 */
	public void setEnclosureForced(boolean enclosureForced) {
		this.enclosureForced = enclosureForced;
	}

	/**
	 * @return Returns the extension.
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            The extension to set.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return Returns the add to result filesname.
	 */
	public boolean isAddToResultFiles() {
		return addToResultFilenames;
	}

	/**
	 * @param addtoresultfilenamesin
	 *            The addtoresultfilenames to set.
	 */
	public void setAddToResultFiles(boolean addtoresultfilenamesin) {
		this.addToResultFilenames = addtoresultfilenamesin;
	}

	/**
	 * @return Returns the fileAppended.
	 */
	public boolean isFileAppended() {
		return fileAppended;
	}

	/**
	 * @param fileAppended
	 *            The fileAppended to set.
	 */
	public void setFileAppended(boolean fileAppended) {
		this.fileAppended = fileAppended;
	}

	/**
	 * @return Returns the fileFormat.
	 */
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param fileFormat
	 *            The fileFormat to set.
	 */
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	/**
	 * @return Returns the fileCompression.
	 */
	public String getFileCompression() {
		return fileCompression;
	}

	/**
	 * @param fileCompression
	 *            The fileCompression to set.
	 */
	public void setFileCompression(String fileCompression) {
		this.fileCompression = fileCompression;
	}

	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return Returns the footer.
	 */
	public boolean isFooterEnabled() {
		return footerEnabled;
	}

	/**
	 * @param footer
	 *            The footer to set.
	 */
	public void setFooterEnabled(boolean footer) {
		this.footerEnabled = footer;
	}

	/**
	 * @return Returns the header.
	 */
	public boolean isHeaderEnabled() {
		return headerEnabled;
	}

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeaderEnabled(boolean header) {
		this.headerEnabled = header;
	}

	/**
	 * @return Returns the newline.
	 */
	public String getNewline() {
		return newline;
	}

	/**
	 * @param newline
	 *            The newline to set.
	 */
	public void setNewline(String newline) {
		this.newline = newline;
	}

	/**
	 * @return Returns the padded.
	 */
	public boolean isPadded() {
		return padded;
	}

	/**
	 * @param padded
	 *            The padded to set.
	 */
	public void setPadded(boolean padded) {
		this.padded = padded;
	}

	/**
	 * @return Returns the fastDump.
	 */
	public boolean isFastDump() {
		return fastDump;
	}

	/**
	 * @param fastDump
	 *            The fastDump to set.
	 */
	public void setFastDump(boolean fastDump) {
		this.fastDump = fastDump;
	}

	/**
	 * @return Returns the separator.
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator
	 *            The separator to set.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return Returns the "do not open new file at init" flag.
	 */
	public boolean isDoNotOpenNewFileInit() {
		return doNotOpenNewFileInit;
	}

	/**
	 * @param doNotOpenNewFileInit
	 *            The "do not open new file at init" flag to set.
	 */
	public void setDoNotOpenNewFileInit(boolean doNotOpenNewFileInit) {
		this.doNotOpenNewFileInit = doNotOpenNewFileInit;
	}

	/**
	 * @return Returns the splitEvery.
	 */
	public int getSplitEvery() {
		return splitEvery;
	}

	/**
	 * @param splitEvery
	 *            The splitEvery to set.
	 */
	public void setSplitEvery(int splitEvery) {
		this.splitEvery = splitEvery;
	}

	/**
	 * @return Returns the stepNrInFilename.
	 */
	public boolean isStepNrInFilename() {
		return stepNrInFilename;
	}

	/**
	 * @param stepNrInFilename
	 *            The stepNrInFilename to set.
	 */
	public void setStepNrInFilename(boolean stepNrInFilename) {
		this.stepNrInFilename = stepNrInFilename;
	}

	/**
	 * @return Returns the partNrInFilename.
	 */
	public boolean isPartNrInFilename() {
		return partNrInFilename;
	}

	/**
	 * @param partNrInFilename
	 *            The partNrInFilename to set.
	 */
	public void setPartNrInFilename(boolean partNrInFilename) {
		this.partNrInFilename = partNrInFilename;
	}

	/**
	 * @return Returns the timeInFilename.
	 */
	public boolean isTimeInFilename() {
		return timeInFilename;
	}

	/**
	 * @param timeInFilename
	 *            The timeInFilename to set.
	 */
	public void setTimeInFilename(boolean timeInFilename) {
		this.timeInFilename = timeInFilename;
	}

	public boolean isSpecifyingFormat() {
		return specifyingFormat;
	}

	public void setSpecifyingFormat(boolean specifyingFormat) {
		this.specifyingFormat = specifyingFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	// public String[] getFieldName() {
	// return fieldName;
	// }
	//
	// public void setFieldName(String[] fieldName) {
	// this.fieldName = fieldName;
	// }
	//
	// public String[] getFieldType() {
	// return fieldType;
	// }
	//
	// public void setFieldType(String[] fieldType) {
	// this.fieldType = fieldType;
	// }
	//
	// public String[] getFieldFormat() {
	// return fieldFormat;
	// }
	//
	// public void setFieldFormat(String[] fieldFormat) {
	// this.fieldFormat = fieldFormat;
	// }
	//
	//	
	//
	// public String[] getFieldCurrency() {
	// return fieldCurrency;
	// }
	//
	// public void setFieldCurrency(String[] fieldCurrency) {
	// this.fieldCurrency = fieldCurrency;
	// }
	//
	// public int[] getFieldLength() {
	// return fieldLength;
	// }
	//
	// public void setFieldLength(int[] fieldLength) {
	// this.fieldLength = fieldLength;
	// }
	//
	// public int[] getFieldPrecision() {
	// return fieldPrecision;
	// }
	//
	// public void setFieldPrecision(int[] fieldPrecision) {
	// this.fieldPrecision = fieldPrecision;
	// }
	//
	// public String[] getFieldDecimal() {
	// return fieldDecimal;
	// }
	//
	// public void setFieldDecimal(String[] fieldDecimal) {
	// this.fieldDecimal = fieldDecimal;
	// }
	//
	// public String[] getFieldGroup() {
	// return fieldGroup;
	// }
	//
	// public void setFieldGroup(String[] fieldGroup) {
	// this.fieldGroup = fieldGroup;
	// }
	//
	// public String[] getFieldTrimType() {
	// return fieldTrimType;
	// }
	//
	// public void setFieldTrimType(String[] fieldTrimType) {
	// this.fieldTrimType = fieldTrimType;
	// }
	//
	// public String[] getFieldNullif() {
	// return fieldNullif;
	// }
	//
	// public void setFieldNullif(String[] fieldNullif) {
	// this.fieldNullif = fieldNullif;
	// }

	/**
	 * @return Returns the outputFields.
	 */
	public TextFileField[] getOutputFields() {
		return outputFields;
	}

	/**
	 * @param outputFields
	 *            The outputFields to set.
	 */
	public void setOutputFields(TextFileField[] outputFields) {
		this.outputFields = outputFields;
	}

	/**
	 * @return The desired encoding of output file, null or empty if the default
	 *         system encoding needs to be used.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            The desired encoding of output file, null or empty if the
	 *            default system encoding needs to be used.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return The desired last line in the output file, null or empty if
	 *         nothing has to be added.
	 */
	public String getEndedLine() {
		return endedLine;
	}

	/**
	 * @param endedLine
	 *            The desired last line in the output file, null or empty if
	 *            nothing has to be added.
	 */
	public void setEndedLine(String endedLine) {
		this.endedLine = endedLine;
	}

	/**
	 * @return Is the file name coded in a field?
	 */
	public boolean isFileNameInField() {
		return fileNameInField;
	}

	/**
	 * @param fileNameInField
	 *            Is the file name coded in a field?
	 */
	public void setFileNameInField(boolean fileNameInField) {
		this.fileNameInField = fileNameInField;
	}

	/**
	 * @return The field name that contains the output file name.
	 */
	public String getFileNameField() {
		return fileNameField;
	}

	/**
	 * @param fileNameField
	 *            Name of the field that contains the file name
	 */
	public void setFileNameField(String fileNameField) {
		this.fileNameField = fileNameField;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public void allocate(int nrfields) {
		outputFields = new TextFileField[nrfields];
	}

	public Object clone() {
		TextFileOutputMeta retval = (TextFileOutputMeta) super.clone();
		int nrfields = outputFields.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.outputFields[i] = (TextFileField) outputFields[i].clone();
		}

		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			separator = XMLHandler.getTagValue(stepnode, "separator");
			if (separator == null)
				separator = "";

			enclosure = XMLHandler.getTagValue(stepnode, "enclosure");
			if (enclosure == null)
				enclosure = "";

			enclosureForced = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "enclosure_forced"));

			headerEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "header"));
			footerEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "footer"));
			fileFormat = XMLHandler.getTagValue(stepnode, "format");
			fileCompression = XMLHandler.getTagValue(stepnode, "compression");
			if (fileCompression == null) {
				if ("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
						"file", "zipped"))) {
					fileCompression = fileCompressionTypeCodes[FILE_COMPRESSION_TYPE_ZIP];
				} else {
					fileCompression = fileCompressionTypeCodes[FILE_COMPRESSION_TYPE_NONE];
				}
			}
			encoding = XMLHandler.getTagValue(stepnode, "encoding");

			endedLine = XMLHandler.getTagValue(stepnode, "endedLine");
			if (endedLine == null)
				endedLine = "";

			fileName = XMLHandler.getTagValue(stepnode, "file", "name");
			fileAsCommand = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "is_command"));
			doNotOpenNewFileInit = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "do_not_open_new_file_init"));
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");
			fileAppended = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "append"));
			stepNrInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "split"));
			partNrInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "haspartno"));
			dateInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_date"));
			timeInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_time"));
			specifyingFormat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "SpecifyFormat"));
			dateTimeFormat = XMLHandler.getTagValue(stepnode, "file",
					"date_time_format");

			String AddToResultFiles = XMLHandler.getTagValue(stepnode, "file",
					"add_to_result_filenames");
			if (Const.isEmpty(AddToResultFiles))
				addToResultFilenames = true;
			else
				addToResultFilenames = "Y".equalsIgnoreCase(AddToResultFiles);

			padded = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"file", "pad"));
			fastDump = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"file", "fast_dump"));
			splitEvery = Const.toInt(XMLHandler.getTagValue(stepnode, "file",
					"splitevery"), 0);

			newline = getNewLine(fileFormat);

			fileNameInField = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "fileNameInField"));
			fileNameField = XMLHandler.getTagValue(stepnode, "fileNameField");

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				outputFields[i] = new TextFileField();
				outputFields[i].setName(XMLHandler.getTagValue(fnode, "name"));
				outputFields[i].setType(XMLHandler.getTagValue(fnode, "type"));
				outputFields[i].setFormat(XMLHandler.getTagValue(fnode,
						"format"));
				outputFields[i].setCurrencySymbol(XMLHandler.getTagValue(fnode,
						"currency"));
				outputFields[i].setDecimalSymbol(XMLHandler.getTagValue(fnode,
						"decimal"));
				outputFields[i].setGroupingSymbol(XMLHandler.getTagValue(fnode,
						"group"));
				outputFields[i].setTrimType(ValueMeta
						.getTrimTypeByCode(XMLHandler.getTagValue(fnode,
								"trim_type")));
				outputFields[i].setNullString(XMLHandler.getTagValue(fnode,
						"nullif"));
				outputFields[i].setLength(Const.toInt(XMLHandler.getTagValue(
						fnode, "length"), -1));
				outputFields[i].setPrecision(Const.toInt(XMLHandler
						.getTagValue(fnode, "precision"), -1));
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public String getNewLine(String fformat) {
		String nl = System.getProperty("line.separator");

		if (fformat != null) {
			if (fformat.equalsIgnoreCase("DOS")) {
				nl = "\r\n";
			} else if (fformat.equalsIgnoreCase("UNIX")) {
				nl = "\n";
			}
		}

		return nl;
	}

	public void setDefault() {
		separator = ";";
		enclosure = "\"";
		specifyingFormat = false;
		dateTimeFormat = null;
		enclosureForced = false;
		headerEnabled = true;
		footerEnabled = false;
		fileFormat = "DOS";
		fileCompression = fileCompressionTypeCodes[FILE_COMPRESSION_TYPE_NONE];
		fileName = "file";
		fileAsCommand = false;
		doNotOpenNewFileInit = false;
		extension = "txt";
		stepNrInFilename = false;
		partNrInFilename = false;
		dateInFilename = false;
		timeInFilename = false;
		padded = false;
		fastDump = false;
		addToResultFilenames = true;
		splitEvery = 0;

		newline = getNewLine(fileFormat);

		int i, nrfields = 0;

		allocate(nrfields);

		for (i = 0; i < nrfields; i++) {
			outputFields[i] = new TextFileField();

			outputFields[i].setName("field" + i);
			outputFields[i].setType("Number");
			outputFields[i].setFormat(" 0,000,000.00;-0,000,000.00");
			outputFields[i].setCurrencySymbol("");
			outputFields[i].setDecimalSymbol(",");
			outputFields[i].setGroupingSymbol(".");
			outputFields[i].setNullString("");
			outputFields[i].setLength(-1);
			outputFields[i].setPrecision(-1);
		}
		fileAppended = false;
	}

	public String[] getFiles(VariableSpace space) {
		int copies = 1;
		int splits = 1;
		int parts = 1;

		if (stepNrInFilename) {
			copies = 3;
		}

		if (partNrInFilename) {
			parts = 3;
		}

		if (splitEvery != 0) {
			splits = 3;
		}

		int nr = copies * parts * splits;
		if (nr > 1)
			nr++;

		String retval[] = new String[nr];

		int i = 0;
		for (int copy = 0; copy < copies; copy++) {
			for (int part = 0; part < parts; part++) {
				for (int split = 0; split < splits; split++) {
					retval[i] = buildFilename(space, copy, "P" + part, split,
							false);
					i++;
				}
			}
		}
		if (i < nr) {
			retval[i] = "...";
		}

		return retval;
	}

	public String buildFilename(VariableSpace space, int stepnr, String partnr,
			int splitnr, boolean ziparchive) {
		return buildFilename(fileName, extension, space, stepnr, partnr,
				splitnr, ziparchive, this);
	}

	public static String buildFilename(String filename, String extension,
			VariableSpace space, int stepnr, String partnr, int splitnr,
			boolean ziparchive, TextFileOutputMeta meta) {
		SimpleDateFormat daf = new SimpleDateFormat();

		// Replace possible environment variables...
		String retval = space.environmentSubstitute(filename);
		String realextension = space.environmentSubstitute(extension);

		if (meta.isFileAsCommand()) {
			return retval;
		}

		Date now = new Date();

		if (meta.isSpecifyingFormat()
				&& !Const.isEmpty(meta.getDateTimeFormat())) {
			daf.applyPattern(meta.getDateTimeFormat());
			String dt = daf.format(now);
			retval += dt;
		} else {
			if (meta.isDateInFilename()) {
				daf.applyPattern("yyyMMdd");
				String d = daf.format(now);
				retval += "_" + d;
			}
			if (meta.isTimeInFilename()) {
				daf.applyPattern("HHmmss");
				String t = daf.format(now);
				retval += "_" + t;
			}
		}
		if (meta.isStepNrInFilename()) {
			retval += "_" + stepnr;
		}
		if (meta.isPartNrInFilename()) {
			retval += "_" + partnr;
		}
		if (meta.getSplitEvery() > 0) {
			retval += "_" + splitnr;
		}

		if (meta.getFileCompression().equals("Zip")) {
			if (ziparchive) {
				retval += ".zip";
			} else {
				if (realextension != null && realextension.length() != 0) {
					retval += "." + realextension;
				}
			}
		} else {
			if (realextension != null && realextension.length() != 0) {
				retval += "." + realextension;
			}
			if (meta.getFileCompression().equals("GZip")) {
				retval += ".gz";
			}
		}
		return retval;
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
			throws KettleStepException {
		// No values are added to the row in this type of step
		// However, in case of Fixed length records,
		// the field precisions and lengths are altered!

		for (int i = 0; i < outputFields.length; i++) {
			TextFileField field = outputFields[i];
			ValueMetaInterface v = row.searchValueMeta(field.getName());
			if (v != null) {
				v.setLength(field.getLength());
				v.setPrecision(field.getPrecision());
				v.setConversionMask(field.getFormat());
				v.setDecimalSymbol(field.getDecimalSymbol());
				v.setGroupingSymbol(field.getGroupingSymbol());
				v.setCurrencySymbol(field.getCurrencySymbol());
				v.setOutputPaddingEnabled(isPadded());
				v.setTrimType(field.getTrimType());
				if (!Const.isEmpty(getEncoding())) {
					v.setStringEncoding(getEncoding());
				}

				// enable output padding by default to be compatible with v2.5.x
				//
				v.setOutputPaddingEnabled(true);
			}
		}
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(800);

		retval.append("    ").append(
				XMLHandler.addTagValue("separator", separator));
		retval.append("    ").append(
				XMLHandler.addTagValue("enclosure", enclosure));
		retval.append("    ").append(
				XMLHandler.addTagValue("enclosure_forced", enclosureForced));
		retval.append("    ").append(
				XMLHandler.addTagValue("header", headerEnabled));
		retval.append("    ").append(
				XMLHandler.addTagValue("footer", footerEnabled));
		retval.append("    ").append(
				XMLHandler.addTagValue("format", fileFormat));
		retval.append("    ").append(
				XMLHandler.addTagValue("compression", fileCompression));
		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));
		retval.append("    ").append(
				XMLHandler.addTagValue("endedLine", endedLine));
		retval.append("    "
				+ XMLHandler.addTagValue("fileNameInField", fileNameInField));
		retval.append("    "
				+ XMLHandler.addTagValue("fileNameField", fileNameField));

		retval.append("    <file>").append(Const.CR);
		retval.append("      ")
				.append(XMLHandler.addTagValue("name", fileName));
		retval.append("      ").append(
				XMLHandler.addTagValue("is_command", fileAsCommand));
		retval.append("      ").append(
				XMLHandler.addTagValue("do_not_open_new_file_init",
						doNotOpenNewFileInit));
		retval.append("      ").append(
				XMLHandler.addTagValue("extention", extension));
		retval.append("      ").append(
				XMLHandler.addTagValue("append", fileAppended));
		retval.append("      ").append(
				XMLHandler.addTagValue("split", stepNrInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("haspartno", partNrInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_date", dateInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_time", timeInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("SpecifyFormat", specifyingFormat));
		retval.append("      ").append(
				XMLHandler.addTagValue("date_time_format", dateTimeFormat));

		retval.append("      ").append(
				XMLHandler.addTagValue("add_to_result_filenames",
						addToResultFilenames));
		retval.append("      ").append(XMLHandler.addTagValue("pad", padded));
		retval.append("      ").append(
				XMLHandler.addTagValue("fast_dump", fastDump));
		retval.append("      ").append(
				XMLHandler.addTagValue("splitevery", splitEvery));
		retval.append("    </file>").append(Const.CR);

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < outputFields.length; i++) {
			TextFileField field = outputFields[i];

			if (field.getName() != null && field.getName().length() != 0) {
				retval.append("      <field>").append(Const.CR);
				retval.append("        ").append(
						XMLHandler.addTagValue("name", field.getName()));
				retval.append("        ").append(
						XMLHandler.addTagValue("type", field.getTypeDesc()));
				retval.append("        ").append(
						XMLHandler.addTagValue("format", field.getFormat()));
				retval.append("        ").append(
						XMLHandler.addTagValue("currency", field
								.getCurrencySymbol()));
				retval.append("        ").append(
						XMLHandler.addTagValue("decimal", field
								.getDecimalSymbol()));
				retval.append("        ").append(
						XMLHandler.addTagValue("group", field
								.getGroupingSymbol()));
				retval.append("        ")
						.append(
								XMLHandler.addTagValue("nullif", field
										.getNullString()));
				retval.append("        ").append(
						XMLHandler.addTagValue("trim_type", field
								.getTrimTypeCode()));
				retval.append("        ").append(
						XMLHandler.addTagValue("length", field.getLength()));
				retval.append("        ").append(
						XMLHandler.addTagValue("precision", field
								.getPrecision()));
				retval.append("      </field>").append(Const.CR);
			}
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {

		fileName = BaseStepMeta.parameterToString(p.get(id + ".fileName"));
		fileAsCommand = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".fileAsCommand"));
		doNotOpenNewFileInit = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".doNotOpenNewFileInit"));
		fileNameInField = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".fileNameInField"));
		fileNameField = BaseStepMeta.parameterToString(p.get(id
				+ ".fileNameField"));
		extension = BaseStepMeta.parameterToString(p.get(id + ".extension"));
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".stepNrInFilename"));
		partNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".partNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".timeInFilename"));
		specifyingFormat = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".specifyingFormat"));
		dateTimeFormat = BaseStepMeta.parameterToString(p.get(id
				+ ".dateTimeFormat"));
		addToResultFilenames = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".addToResultFilenames"));

		fileAppended = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".fileAppended"));
		separator = BaseStepMeta.parameterToString(p.get(id + ".separator"));
		enclosure = BaseStepMeta.parameterToString(p.get(id + ".enclosure"));
		enclosureForced = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".enclosureForced"));
		headerEnabled = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".headerEnabled"));
		footerEnabled = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".footerEnabled"));
		fileFormat = BaseStepMeta.parameterToString(p.get(id + ".fileFormat"));
		fileCompression = BaseStepMeta.parameterToString(p.get(id
				+ ".fileCompression"));
		encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));
		padded = BaseStepMeta.parameterToBoolean(p.get(id + ".padded"));
		fastDump = BaseStepMeta.parameterToBoolean(p.get(id + ".fastDump"));
		splitEvery = BaseStepMeta.parameterToInt(p.get(id + ".splitEvery"));
		endedLine = BaseStepMeta.parameterToString(p.get(id + ".endedLine"));

		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] fieldType = p.get(id + "_fields.fieldType");
		String[] fieldFormat = p.get(id + "_fields.fieldFormat");
		String[] fieldLength = p.get(id + "_fields.fieldLength");
		String[] fieldPrecision = p.get(id + "_fields.fieldPrecision");
		String[] fieldCurrency = p.get(id + "_fields.fieldCurrency");
		String[] fieldDecimal = p.get(id + "_fields.fieldDecimal");
		String[] fieldGroup = p.get(id + "_fields.fieldGroup");
		String[] fieldTrimType = p.get(id + "_fields.fieldTrimType");
		String[] fieldNullif = p.get(id + "_fields.fieldNullif");

		if (fieldName != null && fieldName.length > 0) {
			this.outputFields = new TextFileField[fieldName.length];
			for (int i = 0; i < fieldName.length; i++) {
				this.outputFields[i] = new TextFileField();
				this.outputFields[i].setName(fieldName[i]);
				this.outputFields[i].setType(Const.toInt(fieldType[i], 0));
				this.outputFields[i].setFormat(fieldFormat[i]);
				this.outputFields[i].setLength(Const.toInt(fieldLength[i], -1));
				this.outputFields[i].setPrecision(Const.toInt(
						fieldPrecision[i], -1));
				this.outputFields[i].setCurrencySymbol(fieldCurrency[i]);
				this.outputFields[i].setDecimalSymbol(fieldDecimal[i]);
				this.outputFields[i].setGroupingSymbol(fieldGroup[i]);
				this.outputFields[i].setTrimType(Const.toInt(fieldTrimType[i],
						0));
				this.outputFields[i].setNullString(fieldNullif[i]);
			}
		} else {
			this.outputFields = new TextFileField[0];
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			separator = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_SEPARATOR);
			enclosure = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCLOSURE);
			enclosureForced = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ENCLOSURE_FORCED);
			headerEnabled = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_HEADER);
			footerEnabled = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FOOTER);
			fileFormat = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FORMAT);
			fileCompression = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_COMPRESSION);
			fileNameInField = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILENAMEINFIELD);
			fileNameField = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILENAMEFIELD);
			if (fileCompression == null) {
				if (rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ZIPPED)) {
					fileCompression = fileCompressionTypeCodes[FILE_COMPRESSION_TYPE_ZIP];
				} else {
					fileCompression = fileCompressionTypeCodes[FILE_COMPRESSION_TYPE_NONE];
				}
			}
			encoding = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCODING);
			fileName = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILE_NAME);
			fileAsCommand = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_IS_COMMAND);
			doNotOpenNewFileInit = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_DO_NOT_OPEN_NEW_FILE_INIT);
			extension = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILE_EXTENTION);
			fileAppended = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_APPEND);
			splitEvery = (int) rep.getStepAttributeInteger(id_step,
					STEP_ATTRIBUTE_FILE_SPLIT);
			stepNrInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_STEPNR);
			partNrInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_PARTNR);
			dateInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_DATE);
			timeInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_TIME);
			specifyingFormat = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_SPECIFYFORMAT);
			dateTimeFormat = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_DATE_TIME_FORMAT);

			String AddToResultFiles = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);

			if (Const.isEmpty(AddToResultFiles))
				addToResultFilenames = true;
			else
				addToResultFilenames = rep.getStepAttributeBoolean(id_step,
						STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
			padded = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_PAD);
			fastDump = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_FAST_DUMP);
			newline = getNewLine(fileFormat);

			int nrfields = rep.countNrStepAttributes(id_step,
					STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				outputFields[i] = new TextFileField();
				outputFields[i].setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				outputFields[i].setType(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_TYPE));
				outputFields[i].setFormat(rep.getStepAttributeString(id_step,
						i, STEP_ATTRIBUTE_FIELD_FORMAT));
				outputFields[i].setCurrencySymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_CURRENCY));
				outputFields[i].setDecimalSymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_DECIMAL));
				outputFields[i].setGroupingSymbol(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_GROUP));
				outputFields[i].setTrimType(ValueMeta.getTrimTypeByCode(rep
						.getStepAttributeString(id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE)));
				outputFields[i].setNullString(rep.getStepAttributeString(
						id_step, i, STEP_ATTRIBUTE_FIELD_NULLIF));
				outputFields[i].setLength((int) rep.getStepAttributeInteger(
						id_step, i, STEP_ATTRIBUTE_FIELD_LENGTH));
				outputFields[i].setPrecision((int) rep.getStepAttributeInteger(
						id_step, i, STEP_ATTRIBUTE_FIELD_PRECISION));
			}
			endedLine = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENDEDLINE);
		} catch (Exception e) {
			throw new KettleException(
					"Unexpected error reading step information from the repository",
					e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_SEPARATOR, separator);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ENCLOSURE, enclosure);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ENCLOSURE_FORCED, enclosureForced);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_HEADER, headerEnabled);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FOOTER, footerEnabled);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FORMAT, fileFormat);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_COMPRESSION, fileCompression);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ENCODING, encoding);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_NAME, fileName);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_IS_COMMAND, fileAsCommand);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DO_NOT_OPEN_NEW_FILE_INIT,
					doNotOpenNewFileInit);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_EXTENTION, extension);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_APPEND, fileAppended);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_SPLIT, splitEvery);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_STEPNR, stepNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_PARTNR, partNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_DATE, dateInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DATE_TIME_FORMAT, dateTimeFormat);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_SPECIFYFORMAT, specifyingFormat);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES,
					addToResultFilenames);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_TIME, timeInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_PAD, padded);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_FAST_DUMP, fastDump);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILENAMEINFIELD, fileNameInField);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILENAMEFIELD, fileNameField);

			for (int i = 0; i < outputFields.length; i++) {
				TextFileField field = outputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME, field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_TYPE, field.getTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_FORMAT, field.getFormat());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_CURRENCY, field
								.getCurrencySymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_DECIMAL, field.getDecimalSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_GROUP, field.getGroupingSymbol());
				rep
						.saveStepAttribute(id_transformation, id_step, i,
								STEP_ATTRIBUTE_FIELD_TRIM_TYPE, field
										.getTrimTypeCode());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_NULLIF, field.getNullString());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_LENGTH, field.getLength());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_PRECISION, field.getPrecision());
			}
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ENDEDLINE, endedLine);
		} catch (Exception e) {
			throw new KettleException(
					"Unable to save step information to the repository for id_step="
							+ id_step, e);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepinfo, RowMetaInterface prev, String input[],
			String output[], RowMetaInterface info) {
		CheckResult cr;

		// Check output fields
		if (prev != null && prev.size() > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("TextFileOutputMeta.CheckResult.FieldsReceived",
							"" + prev.size()), stepinfo);
			remarks.add(cr);

			String error_message = "";
			boolean error_found = false;

			// Starting from selected fields in ...
			for (int i = 0; i < outputFields.length; i++) {
				int idx = prev.indexOfValue(outputFields[i].getName());
				if (idx < 0) {
					error_message += "\t\t" + outputFields[i].getName()
							+ Const.CR;
					error_found = true;
				}
			}
			if (error_found) {
				error_message = Messages.getString(
						"TextFileOutputMeta.CheckResult.FieldsNotFound",
						error_message);
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
						error_message, stepinfo);
				remarks.add(cr);
			} else {
				cr = new CheckResult(
						CheckResultInterface.TYPE_RESULT_OK,
						Messages
								.getString("TextFileOutputMeta.CheckResult.AllFieldsFound"),
						stepinfo);
				remarks.add(cr);
			}
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_OK,
					Messages
							.getString("TextFileOutputMeta.CheckResult.ExpectedInputOk"),
					stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("TextFileOutputMeta.CheckResult.ExpectedInputError"),
					stepinfo);
			remarks.add(cr);
		}

		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_COMMENT, Messages
				.getString("TextFileOutputMeta.CheckResult.FilesNotChecked"),
				stepinfo);
		remarks.add(cr);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new TextFileOutput(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new TextFileOutputData();
	}

}
