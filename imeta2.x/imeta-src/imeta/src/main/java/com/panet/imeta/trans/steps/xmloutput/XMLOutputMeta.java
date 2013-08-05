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

package com.panet.imeta.trans.steps.xmloutput;

import java.text.DecimalFormat;
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
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
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
 * This class knows how to handle the MetaData for the XML output step
 * 
 * @since 14-jan-2006
 * 
 */
public class XMLOutputMeta extends BaseStepMeta implements StepMetaInterface {

	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_NAME_SPACE = "name_space";
	public static final String STEP_ATTRIBUTE_XML_MAIN_ELEMENT = "xml_main_element";
	public static final String STEP_ATTRIBUTE_XML_REPEAT_ELEMENT = "xml_repeat_element";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention";
	public static final String STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT = "do_not_open_newfile_init";
	public static final String STEP_ATTRIBUTE_FILE_SPLIT = "file_split";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR = "file_add_stepnr";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE = "file_add_date";
	public static final String STEP_ATTRIBUTE_FILE_ADD_TIME = "file_add_time";

	public static final String STEP_ATTRIBUTE_SPECIFY_FORMAT = "specify_format";
	public static final String STEP_ATTRIBUTE_DATE_TIME_FORMAT = "date_time_format";
	public static final String STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES = "add_to_result_filenames";
	public static final String STEP_ATTRIBUTE_FILE_ZIPPED = "file_zipped";

	private String fieldName[];
	private String fieldElement[];
	private String fieldType[];
	private String fieldFormat[];
	private int fieldLength[];
	private int fieldPrecision[];
	private String fieldCurrency[];
	private String fieldDecimal[];
	private String fieldGroup[];
	private String fieldNullif[];

	/** The base name of the output file */
	private String fileName;

	/** The file extention in case of a generated filename */
	private String extension;

	/**
	 * if this value is larger then 0, the text file is split up into parts of
	 * this number of lines
	 */
	private int splitEvery;

	/** Flag: add the stepnr in the filename */
	private boolean stepNrInFilename;

	/** Flag: add the date in the filename */
	private boolean dateInFilename;

	/** Flag: add the time in the filename */
	private boolean timeInFilename;

	/** Flag: put the destination file in a zip archive */
	private boolean zipped;

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	/**
	 * The name space for the XML document: null or empty string means no xmlns
	 * is written
	 */
	private String nameSpace;

	/** The name of the parent XML element */
	private String mainElement;

	/** The name of the repeating row XML element */
	private String repeatElement;

	/** Flag: add the filenames to result filenames */
	private boolean addToResultFilenames;

	/* THE FIELD SPECIFICATIONS ... */

	/** The output fields */
	private XMLField outputFields[];

	/** Flag : Do not open new file when transformation start */
	private boolean doNotOpenNewFileInit;

	private boolean SpecifyFormat;

	private String date_time_format;

	public XMLOutputMeta() {
		super(); // allocate BaseStepMeta
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

	public boolean isSpecifyFormat() {
		return SpecifyFormat;
	}

	public void setSpecifyFormat(boolean SpecifyFormat) {
		this.SpecifyFormat = SpecifyFormat;
	}

	public String getDateTimeFormat() {
		return date_time_format;
	}

	public void setDateTimeFormat(String date_time_format) {
		this.date_time_format = date_time_format;
	}

	/**
	 * @return Returns the zipped.
	 */
	public boolean isZipped() {
		return zipped;
	}

	/**
	 * @param zipped
	 *            The zipped to set.
	 */
	public void setZipped(boolean zipped) {
		this.zipped = zipped;
	}

	/**
	 * @return Returns the outputFields.
	 */
	public XMLField[] getOutputFields() {
		return outputFields;
	}

	/**
	 * @param outputFields
	 *            The outputFields to set.
	 */
	public void setOutputFields(XMLField[] outputFields) {
		this.outputFields = outputFields;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public void allocate(int nrfields) {
		outputFields = new XMLField[nrfields];
	}

	public Object clone() {
		XMLOutputMeta retval = (XMLOutputMeta) super.clone();
		int nrfields = outputFields.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.outputFields[i] = (XMLField) outputFields[i].clone();
		}

		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {
			encoding = XMLHandler.getTagValue(stepnode, "encoding");
			nameSpace = XMLHandler.getTagValue(stepnode, "name_space");
			mainElement = XMLHandler.getTagValue(stepnode, "xml_main_element");
			repeatElement = XMLHandler.getTagValue(stepnode,
					"xml_repeat_element");

			fileName = XMLHandler.getTagValue(stepnode, "file", "name");
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");

			doNotOpenNewFileInit = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "do_not_open_newfile_init"));
			stepNrInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "split"));
			dateInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_date"));
			timeInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_time"));
			SpecifyFormat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "specify_format"));
			date_time_format = XMLHandler.getTagValue(stepnode, "file",
					"date_time_format");

			addToResultFilenames = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_to_result_filenames"));

			zipped = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					"file", "zipped"));
			splitEvery = Const.toInt(XMLHandler.getTagValue(stepnode, "file",
					"splitevery"), 0);

			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				outputFields[i] = new XMLField();
				String fieldName = XMLHandler.getTagValue(fnode, "name");
				outputFields[i].setFieldName(fieldName);
				String elementName = XMLHandler.getTagValue(fnode, "element");
				outputFields[i].setElementName(elementName == null ? ""
						: elementName);
				outputFields[i].setType(XMLHandler.getTagValue(fnode, "type"));
				outputFields[i].setFormat(XMLHandler.getTagValue(fnode,
						"format"));
				outputFields[i].setCurrencySymbol(XMLHandler.getTagValue(fnode,
						"currency"));
				outputFields[i].setDecimalSymbol(XMLHandler.getTagValue(fnode,
						"decimal"));
				outputFields[i].setGroupingSymbol(XMLHandler.getTagValue(fnode,
						"group"));
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
		fileName = "file";
		extension = "xml";
		stepNrInFilename = false;
		doNotOpenNewFileInit = false;
		dateInFilename = false;
		timeInFilename = false;
		addToResultFilenames = false;
		zipped = false;
		splitEvery = 0;
		encoding = Const.XML_ENCODING;
		nameSpace = "";
		date_time_format = null;
		SpecifyFormat = false;
		mainElement = "Rows";
		repeatElement = "Row";

		int nrfields = 0;

		allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			outputFields[i] = new XMLField();

			outputFields[i].setFieldName("field" + i);
			outputFields[i].setElementName("field" + i);
			outputFields[i].setType("Number");
			outputFields[i].setFormat(" 0,000,000.00;-0,000,000.00");
			outputFields[i].setCurrencySymbol("");
			outputFields[i].setDecimalSymbol(",");
			outputFields[i].setGroupingSymbol(".");
			outputFields[i].setNullString("");
			outputFields[i].setLength(-1);
			outputFields[i].setPrecision(-1);
		}
	}

	public String[] getFiles(VariableSpace space) {
		int copies = 1;
		int splits = 1;

		if (stepNrInFilename) {
			copies = 3;
		}

		if (splitEvery != 0) {
			splits = 3;
		}

		int nr = copies * splits;
		if (nr > 1)
			nr++;

		String retval[] = new String[nr];

		int i = 0;
		for (int copy = 0; copy < copies; copy++) {
			for (int split = 0; split < splits; split++) {
				retval[i] = buildFilename(space, copy, split, false);
				i++;
			}
		}
		if (i < nr) {
			retval[i] = "...";
		}

		return retval;
	}

	public String buildFilename(VariableSpace space, int stepnr, int splitnr,
			boolean ziparchive) {
		SimpleDateFormat daf = new SimpleDateFormat();
		DecimalFormat df = new DecimalFormat("00000");

		// Replace possible environment variables...
		String retval = space.environmentSubstitute(fileName);
		String realextension = space.environmentSubstitute(extension);

		Date now = new Date();

		if (SpecifyFormat && !Const.isEmpty(date_time_format)) {
			daf.applyPattern(date_time_format);
			String dt = daf.format(now);
			retval += dt;
		} else {
			if (dateInFilename) {
				daf.applyPattern("yyyyMMdd");
				String d = daf.format(now);
				retval += "_" + d;
			}
			if (timeInFilename) {
				daf.applyPattern("HHmmss");
				String t = daf.format(now);
				retval += "_" + t;
			}
		}

		if (stepNrInFilename) {
			retval += "_" + stepnr;
		}
		if (splitEvery > 0) {
			retval += "_" + df.format(splitnr + 1);
		}

		if (zipped) {
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
		}
		return retval;
	}

	public void getFields(RowMetaInterface row, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {

		// No values are added to the row in this type of step
		// However, in case of Fixed length records,
		// the field precisions and lengths are altered!

		for (int i = 0; i < outputFields.length; i++) {
			XMLField field = outputFields[i];
			ValueMetaInterface v = row.searchValueMeta(field.getFieldName());
			if (v != null) {
				v.setLength(field.getLength(), field.getPrecision());
			}
		}

	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(600);

		retval.append("    ").append(
				XMLHandler.addTagValue("encoding", encoding));
		retval.append("    ").append(
				XMLHandler.addTagValue("name_space", nameSpace));
		retval.append("    ").append(
				XMLHandler.addTagValue("xml_main_element", mainElement));
		retval.append("    ").append(
				XMLHandler.addTagValue("xml_repeat_element", repeatElement));

		retval.append("    <file>").append(Const.CR);
		retval.append("      ")
				.append(XMLHandler.addTagValue("name", fileName));
		retval.append("      ").append(
				XMLHandler.addTagValue("extention", extension));

		retval.append("      ").append(
				XMLHandler.addTagValue("do_not_open_newfile_init",
						doNotOpenNewFileInit));
		retval.append("      ").append(
				XMLHandler.addTagValue("split", stepNrInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_date", dateInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_time", timeInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("specify_format", SpecifyFormat));
		retval.append("      ").append(
				XMLHandler.addTagValue("date_time_format", date_time_format));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_to_result_filenames",
						addToResultFilenames));
		retval.append("      ")
				.append(XMLHandler.addTagValue("zipped", zipped));
		retval.append("      ").append(
				XMLHandler.addTagValue("splitevery", splitEvery));
		retval.append("    </file>").append(Const.CR);
		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < outputFields.length; i++) {
			XMLField field = outputFields[i];

			if (field.getFieldName() != null
					&& field.getFieldName().length() != 0) {
				retval.append("      <field>").append(Const.CR);
				retval.append("        ").append(
						XMLHandler.addTagValue("name", field.getFieldName()));
				retval.append("        ").append(
						XMLHandler.addTagValue("element", field
								.getElementName()));
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

		encoding = BaseStepMeta.parameterToString(p.get(id + ".encoding"));
		nameSpace = BaseStepMeta.parameterToString(p.get(id + ".nameSpace"));
		mainElement = BaseStepMeta
				.parameterToString(p.get(id + ".mainElement"));
		repeatElement = BaseStepMeta.parameterToString(p.get(id
				+ ".repeatElement"));
		fileName = BaseStepMeta.parameterToString(p.get(id + ".fileName"));
		extension = BaseStepMeta.parameterToString(p.get(id + ".extension"));
		doNotOpenNewFileInit = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".doNotOpenNewFileInit"));
		splitEvery = BaseStepMeta.parameterToInt(p.get(id + ".splitEvery"));
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".stepNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".timeInFilename"));
		SpecifyFormat = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".SpecifyFormat"));
		date_time_format = BaseStepMeta.parameterToString(p.get(id
				+ ".date_time_format"));
		addToResultFilenames = BaseStepMeta.parameterToBoolean(p.get(id
				+ ".addToResultFilenames"));
		zipped = BaseStepMeta.parameterToBoolean(p.get(id + ".zipped"));

		String[] fieldName = p.get(id + "_fields.fieldName");
		String[] fieldElement = p.get(id + "_fields.fieldElement");
		String[] fieldType = p.get(id + "_fields.fieldType");
		String[] fieldFormat = p.get(id + "_fields.fieldFormat");
		String[] fieldLength = p.get(id + "_fields.fieldLength");
		String[] fieldPrecision = p.get(id + "_fields.fieldPrecision");
		String[] fieldCurrency = p.get(id + "_fields.fieldCurrency");
		String[] fieldDecimal = p.get(id + "_fields.fieldDecimal");
		String[] fieldGroup = p.get(id + "_fields.fieldGroup");
		String[] fieldNullif = p.get(id + "_fields.fieldNullif");

		if (fieldName != null && fieldName.length > 0) {
			this.outputFields = new XMLField[fieldName.length];
			for (int i = 0; i < fieldName.length; i++) {
				this.outputFields[i] = new XMLField();
				this.outputFields[i].setFieldName(fieldName[i]);
				this.outputFields[i].setElementName(fieldElement[i]);
				this.outputFields[i].setType(Const.toInt(fieldType[i], 0));
				this.outputFields[i].setFormat(fieldFormat[i]);
				try {
					this.outputFields[i].setLength(Const.toInt(fieldLength[i],
							-1));
				} catch (Exception e) {
					this.outputFields[i].setLength(0);
				}
				try {
					this.outputFields[i].setPrecision(Const.toInt(
							fieldPrecision[i], -1));
				} catch (Exception e) {
					this.outputFields[i].setPrecision(0);
				}
				this.outputFields[i].setCurrencySymbol(fieldCurrency[i]);
				this.outputFields[i].setDecimalSymbol(fieldDecimal[i]);
				this.outputFields[i].setGroupingSymbol(fieldGroup[i]);
				this.outputFields[i].setNullString(fieldNullif[i]);
			}
		} else {
			this.outputFields = new XMLField[0];
		}
	}

	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			encoding = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_ENCODING);
			nameSpace = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_NAME_SPACE);
			mainElement = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_XML_MAIN_ELEMENT);
			repeatElement = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_XML_REPEAT_ELEMENT);
			fileName = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILE_NAME);
			extension = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_FILE_EXTENTION);
			doNotOpenNewFileInit = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT);
			splitEvery = (int) rep.getStepAttributeInteger(id_step,
					STEP_ATTRIBUTE_FILE_SPLIT);
			stepNrInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_STEPNR);
			dateInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_DATE);
			timeInFilename = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ADD_TIME);
			SpecifyFormat = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_SPECIFY_FORMAT);
			date_time_format = rep.getStepAttributeString(id_step,
					STEP_ATTRIBUTE_DATE_TIME_FORMAT);

			addToResultFilenames = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
			zipped = rep.getStepAttributeBoolean(id_step,
					STEP_ATTRIBUTE_FILE_ZIPPED);

			int nrfields = rep.countNrStepAttributes(id_step, "field_name");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				outputFields[i] = new XMLField();

				outputFields[i].setFieldName(rep.getStepAttributeString(
						id_step, i, "field_name"));
				outputFields[i].setElementName(rep.getStepAttributeString(
						id_step, i, "field_element"));
				outputFields[i].setType(rep.getStepAttributeString(id_step, i,
						"field_type"));
				outputFields[i].setFormat(rep.getStepAttributeString(id_step,
						i, "field_format"));
				outputFields[i].setLength((int) rep.getStepAttributeInteger(
						id_step, i, "field_length"));
				outputFields[i].setPrecision((int) rep.getStepAttributeInteger(
						id_step, i, "field_precision"));
				outputFields[i].setCurrencySymbol(rep.getStepAttributeString(
						id_step, i, "field_currency"));
				outputFields[i].setDecimalSymbol(rep.getStepAttributeString(
						id_step, i, "field_decimal"));
				outputFields[i].setGroupingSymbol(rep.getStepAttributeString(
						id_step, i, "field_group"));
				outputFields[i].setNullString(rep.getStepAttributeString(
						id_step, i, "field_nullif"));

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
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ENCODING, encoding);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_NAME_SPACE, nameSpace);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_XML_MAIN_ELEMENT, mainElement);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_XML_REPEAT_ELEMENT, repeatElement);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_NAME, fileName);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_EXTENTION, extension);

			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT,
					doNotOpenNewFileInit);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_SPLIT, splitEvery);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_STEPNR, stepNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_DATE, dateInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_TIME, timeInFilename);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_SPECIFY_FORMAT, SpecifyFormat);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DATE_TIME_FORMAT, date_time_format);

			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES,
					addToResultFilenames);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ZIPPED, zipped);

			for (int i = 0; i < outputFields.length; i++) {
				XMLField field = outputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_name", field.getFieldName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_element", field.getElementName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_type", field.getTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_format", field.getFormat());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_length", field.getLength());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_precision", field.getPrecision());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_currency", field.getCurrencySymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_decimal", field.getDecimalSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_group", field.getGroupingSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i,
						"field_nullif", field.getNullString());

			}
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
					.getString("XMLOutputMeta.CheckResult.FieldsReceived", ""
							+ prev.size()), stepinfo);
			remarks.add(cr);

			String error_message = "";
			boolean error_found = false;

			// Starting from selected fields in ...
			for (int i = 0; i < outputFields.length; i++) {
				int idx = prev.indexOfValue(outputFields[i].getFieldName());
				if (idx < 0) {
					error_message += "\t\t" + outputFields[i].getFieldName()
							+ Const.CR;
					error_found = true;
				}
			}
			if (error_found) {
				error_message = Messages.getString(
						"XMLOutputMeta.CheckResult.FieldsNotFound",
						error_message);
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
						error_message, stepinfo);
				remarks.add(cr);
			} else {
				cr = new CheckResult(
						CheckResultInterface.TYPE_RESULT_OK,
						Messages
								.getString("XMLOutputMeta.CheckResult.AllFieldsFound"),
						stepinfo);
				remarks.add(cr);
			}
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("XMLOutputMeta.CheckResult.ExpectedInputOk"),
					stepinfo);
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("XMLOutputMeta.CheckResult.ExpectedInputError"),
					stepinfo);
			remarks.add(cr);
		}

		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_COMMENT, Messages
				.getString("XMLOutputMeta.CheckResult.FilesNotChecked"),
				stepinfo);
		remarks.add(cr);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new XMLOutput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData() {
		return new XMLOutputData();
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return Returns the mainElement.
	 */
	public String getMainElement() {
		return mainElement;
	}

	/**
	 * @param mainElement
	 *            The mainElement to set.
	 */
	public void setMainElement(String mainElement) {
		this.mainElement = mainElement;
	}

	/**
	 * @return Returns the repeatElement.
	 */
	public String getRepeatElement() {
		return repeatElement;
	}

	/**
	 * @param repeatElement
	 *            The repeatElement to set.
	 */
	public void setRepeatElement(String repeatElement) {
		this.repeatElement = repeatElement;
	}

	/**
	 * @return Returns the nameSpace.
	 */
	public String getNameSpace() {
		return nameSpace;
	}

	/**
	 * @param nameSpace
	 *            The nameSpace to set.
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public String[] getFieldElement() {
		return fieldElement;
	}

	public void setFieldElement(String[] fieldElement) {
		this.fieldElement = fieldElement;
	}

	public String[] getFieldType() {
		return fieldType;
	}

	public void setFieldType(String[] fieldType) {
		this.fieldType = fieldType;
	}

	public String[] getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(String[] fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	public int[] getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int[] fieldLength) {
		this.fieldLength = fieldLength;
	}

	public int[] getFieldPrecision() {
		return fieldPrecision;
	}

	public void setFieldPrecision(int[] fieldPrecision) {
		this.fieldPrecision = fieldPrecision;
	}

	public String[] getFieldCurrency() {
		return fieldCurrency;
	}

	public void setFieldCurrency(String[] fieldCurrency) {
		this.fieldCurrency = fieldCurrency;
	}

	public String[] getFieldDecimal() {
		return fieldDecimal;
	}

	public void setFieldDecimal(String[] fieldDecimal) {
		this.fieldDecimal = fieldDecimal;
	}

	public String[] getFieldGroup() {
		return fieldGroup;
	}

	public void setFieldGroup(String[] fieldGroup) {
		this.fieldGroup = fieldGroup;
	}

	public String[] getFieldNullif() {
		return fieldNullif;
	}

	public void setFieldNullif(String[] fieldNullif) {
		this.fieldNullif = fieldNullif;
	}

	public boolean isAddToResultFilenames() {
		return addToResultFilenames;
	}

	public void setAddToResultFilenames(boolean addToResultFilenames) {
		this.addToResultFilenames = addToResultFilenames;
	}

	public String getDate_time_format() {
		return date_time_format;
	}

	public void setDate_time_format(String date_time_format) {
		this.date_time_format = date_time_format;
	}

}
