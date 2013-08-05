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

package com.panet.imeta.trans.steps.exceloutput;

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
import com.panet.imeta.core.encryption.Encr;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
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
 * Metadata of the Excel Output step.
 * 
 * @author Matt
 * @since on 6-sep-2006
 */

public class ExcelOutputMeta extends BaseStepMeta implements StepMetaInterface {
	
	public static final String STEP_ATTRIBUTE_HEADER = "header";
	public static final String STEP_ATTRIBUTE_FOOTER = "footer";
	public static final String STEP_ATTRIBUTE_ENCODING = "encoding";
	public static final String STEP_ATTRIBUTE_APPEND = "append";
	public static final String STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES = "add_to_result_filenames";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention";
	public static final String STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT = "do_not_open_newfile_init";
	public static final String STEP_ATTRIBUTE_FILE_SPLIT = "file_split";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR = "file_add_stepnr";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE = "file_add_date";
	public static final String STEP_ATTRIBUTE_FILE_ADD_TIME = "file_add_time";
	public static final String STEP_ATTRIBUTE_SPECIFY_FORMAT = "specify_format";
	public static final String STEP_ATTRIBUTE_DATE_TIME_FORMAT = "date_time_format";
	public static final String STEP_ATTRIBUTE_AUTO_SIZE_COLUMS = "auto_size_colums";
	public static final String STEP_ATTRIBUTE_PROTECT_SHEET = "protect_sheet";
	public static final String STEP_ATTRIBUTE_PASSWORD = "password";
	public static final String STEP_ATTRIBUTE_TEMPLATE_ENABLED = "template_enabled";
	public static final String STEP_ATTRIBUTE_TEMPLATE_APPEND = "template_append";
	public static final String STEP_ATTRIBUTE_TEMPLATE_FILENAME = "template_filename";
	public static final String STEP_ATTRIBUTE_SHEETNAME = "sheetname";

	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_FIELD_TYPE = "field_type";
	public static final String STEP_ATTRIBUTE_FIELD_FORMAT = "field_format";
	
 
	

	/** The base name of the output file */
	private String fileName;

	/** The file extention in case of a generated filename */
	private String extension;

	/** The password to protect the sheet */
	private String password;

	/** Add a header at the top of the file? */
	private boolean headerEnabled;

	/** Add a footer at the bottom of the file? */
	private boolean footerEnabled;

	/**
	 * if this value is larger then 0, the text file is split up into parts of
	 * this number of lines
	 */
	private int splitEvery;

	/** Flag: add the stepnr in the filename */
	private boolean stepNrInFilename;

	/** Flag: add the date in the filename */
	private boolean dateInFilename;

	/** Flag: add the filenames to result filenames */
	private boolean addToResultFilenames;

	/** Flag: protect the sheet */
	private boolean protectsheet;

	/** Flag: add the time in the filename */
	private boolean timeInFilename;

	/** Flag: use a template */
	private boolean templateEnabled;

	/** the excel template */
	private String templateFileName;

	/** Flag: append when template */
	private boolean templateAppend;

	/** the excel sheet name */
	private String sheetname;
	
	private String addToResult;

	/* THE FIELD SPECIFICATIONS ... */

	/** The output fields */
	private ExcelField outputFields[];

	/**
	 * The encoding to use for reading: null or empty string means system
	 * default encoding
	 */
	private String encoding;

	/** Calculated value ... */
	private String newline;

	/** Flag : append workbook? */
	private boolean append;

	/** Flag : Do not open new file when transformation start */
	private boolean doNotOpenNewFileInit;

	private boolean specifyFormat;

	private String dateTimeFormat;

	/** Flag : auto size columns? */
	private boolean autoSizeColums;

	public ExcelOutputMeta() {
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
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return Returns the sheet name.
	 */
	public String getSheetname() {
		return sheetname;
	}

	/**
	 * @param sheetname
	 *            The sheet name.
	 */
	public void setSheetname(String sheetname) {
		this.sheetname = sheetname;
	}

	/**
	 * @param fileName
	 *            The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param password
	 *            teh passwoed to set.
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return Returns the autosizecolums.
	 */
	public boolean isAutoSizeColums() {
		return autoSizeColums;
	}

	/**
	 * @param autosizecolums
	 *            The autosizecolums to set.
	 */
	public void setAutoSizeColums(boolean autosizecolums) {
		this.autoSizeColums = autosizecolums;
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

	public boolean isSpecifyFormat() {
		return specifyFormat;
	}

	public void setSpecifyFormat(boolean SpecifyFormat) {
		this.specifyFormat = SpecifyFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(String date_time_format) {
		this.dateTimeFormat = date_time_format;
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
	 * @return Returns the splitEvery.
	 */
	public int getSplitEvery() {
		return splitEvery;
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
	 * @return Returns the protectsheet.
	 */
	public boolean isSheetProtected() {
		return protectsheet;
	}

	/**
	 * @param timeInFilename
	 *            The timeInFilename to set.
	 */
	public void setTimeInFilename(boolean timeInFilename) {
		this.timeInFilename = timeInFilename;
	}

	/**
	 * @param protectsheet
	 *            the value to set.
	 */
	public void setProtectSheet(boolean protectsheet) {
		this.protectsheet = protectsheet;
	}

	/**
	 * @return Returns the outputFields.
	 */
	public ExcelField[] getOutputFields() {
		return outputFields;
	}

	/**
	 * @param outputFields
	 *            The outputFields to set.
	 */
	public void setOutputFields(ExcelField[] outputFields) {
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
	 * @return Returns the template.
	 */
	public boolean isTemplateEnabled() {
		return templateEnabled;
	}

	/**
	 * @param template
	 *            The template to set.
	 */
	public void setTemplateEnabled(boolean template) {
		this.templateEnabled = template;
	}

	/**
	 * @return Returns the templateAppend.
	 */
	public boolean isTemplateAppend() {
		return templateAppend;
	}

	/**
	 * @param templateAppend
	 *            The templateAppend to set.
	 */
	public void setTemplateAppend(boolean templateAppend) {
		this.templateAppend = templateAppend;
	}

	/**
	 * @return Returns the templateFileName.
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * @param templateFileName
	 *            The templateFileName to set.
	 */
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
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
	 * @return Returns the append.
	 */
	public boolean isAppend() {
		return append;
	}

	/**
	 * @param append
	 *            The append to set.
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		readData(stepnode);
	}

	public void allocate(int nrfields) {
		outputFields = new ExcelField[nrfields];
	}

	public Object clone() {
		ExcelOutputMeta retval = (ExcelOutputMeta) super.clone();
		int nrfields = outputFields.length;

		retval.allocate(nrfields);

		for (int i = 0; i < nrfields; i++) {
			retval.outputFields[i] = (ExcelField) outputFields[i].clone();
		}

		return retval;
	}

	private void readData(Node stepnode) throws KettleXMLException {
		try {

			headerEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, STEP_ATTRIBUTE_HEADER));
			footerEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, STEP_ATTRIBUTE_FOOTER));
			encoding = XMLHandler.getTagValue(stepnode, STEP_ATTRIBUTE_ENCODING);
			append = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode,
					STEP_ATTRIBUTE_APPEND));
			String addToResult = XMLHandler.getTagValue(stepnode,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
			if (Const.isEmpty(addToResult))
				addToResultFilenames = true;
			else
				addToResultFilenames = "Y".equalsIgnoreCase(addToResult);

			fileName = XMLHandler.getTagValue(stepnode, "file", "name");
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");

			doNotOpenNewFileInit = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT));
			stepNrInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "split"));
			dateInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_date"));
			timeInFilename = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", "add_time"));
			specifyFormat = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", STEP_ATTRIBUTE_SPECIFY_FORMAT));
			dateTimeFormat = XMLHandler.getTagValue(stepnode, "file",
					STEP_ATTRIBUTE_DATE_TIME_FORMAT);

			autoSizeColums = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", STEP_ATTRIBUTE_AUTO_SIZE_COLUMS));
			protectsheet = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "file", STEP_ATTRIBUTE_PROTECT_SHEET));
			password = Encr.decryptPasswordOptionallyEncrypted(XMLHandler
					.getTagValue(stepnode, "file", STEP_ATTRIBUTE_PASSWORD));
			splitEvery = Const.toInt(XMLHandler.getTagValue(stepnode, "file",
					"splitevery"), 0);

			templateEnabled = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "template", "enabled"));
			templateAppend = "Y".equalsIgnoreCase(XMLHandler.getTagValue(
					stepnode, "template", STEP_ATTRIBUTE_APPEND));
			templateFileName = XMLHandler.getTagValue(stepnode, "template",
					"filename");
			sheetname = XMLHandler.getTagValue(stepnode, "file", STEP_ATTRIBUTE_SHEETNAME);
			Node fields = XMLHandler.getSubNode(stepnode, "fields");
			int nrfields = XMLHandler.countNodes(fields, "field");

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);

				outputFields[i] = new ExcelField();
				outputFields[i].setName(XMLHandler.getTagValue(fnode, "name"));
				outputFields[i].setType(XMLHandler.getTagValue(fnode, "type"));
				outputFields[i].setFormat(XMLHandler.getTagValue(fnode,
						"format"));
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
		autoSizeColums = false;
		headerEnabled = true;
		footerEnabled = false;
		fileName = "file";
		extension = "xls";
		doNotOpenNewFileInit = false;
		stepNrInFilename = false;
		dateInFilename = false;
		timeInFilename = false;
		dateTimeFormat = null;
		specifyFormat = false;
		addToResultFilenames = true;
		protectsheet = false;
		splitEvery = 0;
		templateEnabled = false;
		templateAppend = false;
		templateFileName = "template.xls";
		sheetname = "Sheet1";
		append = false;
		int i, nrfields = 0;
		allocate(nrfields);

		for (i = 0; i < nrfields; i++) {
			outputFields[i] = new ExcelField();

			outputFields[i].setName("field" + i);
			outputFields[i].setType("Number");
			outputFields[i].setFormat(" 0,000,000.00;-0,000,000.00");
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
				retval[i] = buildFilename(space, copy, split);
				i++;
			}
		}
		if (i < nr) {
			retval[i] = "...";
		}

		return retval;
	}

	public String buildFilename(VariableSpace space, int stepnr, int splitnr) {
		SimpleDateFormat daf = new SimpleDateFormat();

		// Replace possible environment variables...
		String retval = space.environmentSubstitute(fileName);
		String realextension = space.environmentSubstitute(extension);

		Date now = new Date();

		if (specifyFormat && !Const.isEmpty(dateTimeFormat)) {
			daf.applyPattern(dateTimeFormat);
			String dt = daf.format(now);
			retval += dt;
		} else {
			if (dateInFilename) {
				daf.applyPattern("yyyMMdd");
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
			retval += "_" + splitnr;
		}

		if (realextension != null && realextension.length() != 0) {
			retval += "." + realextension;
		}

		return retval;
	}

	public void getFields(RowMetaInterface r, String name,
			RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {
		if (r == null)
			r = new RowMeta(); // give back values

		// No values are added to the row in this type of step
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer(800);

		retval.append("    ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_HEADER, headerEnabled));
		retval.append("    ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_FOOTER, footerEnabled));
		retval.append("    ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_ENCODING, encoding));
		retval.append("    " + XMLHandler.addTagValue(STEP_ATTRIBUTE_APPEND, append));
		retval.append("    "
				+ XMLHandler.addTagValue(STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES,
						addToResultFilenames));

		retval.append("    <file>").append(Const.CR);
		retval.append("      ")
				.append(XMLHandler.addTagValue("name", fileName));
		retval.append("      ").append(
				XMLHandler.addTagValue("extention", extension));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT,
						doNotOpenNewFileInit));
		retval.append("      ").append(
				XMLHandler.addTagValue("split", stepNrInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_date", dateInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue("add_time", timeInFilename));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_SPECIFY_FORMAT, specifyFormat));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_DATE_TIME_FORMAT, dateTimeFormat));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_SHEETNAME, sheetname));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_AUTO_SIZE_COLUMS, autoSizeColums));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_PROTECT_SHEET, protectsheet));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_PASSWORD, Encr
						.encryptPasswordIfNotUsingVariables(password)));
		retval.append("      ").append(
				XMLHandler.addTagValue("splitevery", splitEvery));

		retval.append("      </file>").append(Const.CR);

		retval.append("    <template>").append(Const.CR);
		retval.append("      ").append(
				XMLHandler.addTagValue("enabled", templateEnabled));
		retval.append("      ").append(
				XMLHandler.addTagValue(STEP_ATTRIBUTE_APPEND, templateAppend));
		retval.append("      ").append(
				XMLHandler.addTagValue("filename", templateFileName));
		retval.append("    </template>").append(Const.CR);

		retval.append("    <fields>").append(Const.CR);
		for (int i = 0; i < outputFields.length; i++) {
			ExcelField field = outputFields[i];

			if (field.getName() != null && field.getName().length() != 0) {
				retval.append("      <field>").append(Const.CR);
				retval.append("        ").append(
						XMLHandler.addTagValue("name", field.getName()));
				retval.append("        ").append(
						XMLHandler.addTagValue("type", field.getTypeDesc()));
				retval.append("        ").append(
						XMLHandler.addTagValue("format", field.getFormat()));
				retval.append("      </field>").append(Const.CR);
			}
		}
		retval.append("    </fields>").append(Const.CR);

		return retval.toString();
	}
	
	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		
		headerEnabled = BaseStepMeta.parameterToBoolean(p.get(id+".headerEnabled"));
		footerEnabled = BaseStepMeta.parameterToBoolean(p.get(id+".footerEnabled"));
		encoding = BaseStepMeta.parameterToString(p.get(id+".encoding"));
		append = BaseStepMeta.parameterToBoolean(p.get(id+".append"));
		addToResult = BaseStepMeta.parameterToString(p.get(id+".addToResult"));
		addToResultFilenames = BaseStepMeta.parameterToBoolean(p.get(id+".addToResultFilenames"));
		fileName = BaseStepMeta.parameterToString(p.get(id+".fileName"));
		extension = BaseStepMeta.parameterToString(p.get(id+".extension"));
		doNotOpenNewFileInit = BaseStepMeta.parameterToBoolean(p.get(id+".doNotOpenNewFileInit"));
		splitEvery = BaseStepMeta.parameterToInt(p.get(id+".splitEvery"));
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id+".stepNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id+".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id+".timeInFilename"));
		specifyFormat = BaseStepMeta.parameterToBoolean(p.get(id+".specifyFormat"));
		dateTimeFormat = BaseStepMeta.parameterToString(p.get(id+".dateTimeFormat"));
		autoSizeColums = BaseStepMeta.parameterToBoolean(p.get(id+".autoSizeColums"));
		protectsheet = BaseStepMeta.parameterToBoolean(p.get(id+".protectsheet"));
		password = Encr.decryptPasswordOptionallyEncrypted(BaseStepMeta.parameterToString(p.get(id+".password")));
		templateEnabled = BaseStepMeta.parameterToBoolean(p.get(id+".templateEnabled"));
		templateAppend = BaseStepMeta.parameterToBoolean(p.get(id+".templateAppend"));
		templateFileName = BaseStepMeta.parameterToString(p.get(id+".templateFileName"));
		sheetname = BaseStepMeta.parameterToString(p.get(id+".sheetname"));
		
		String[]fieldName=p.get(id+"_fields.fieldName");
		String[]fieldType=p.get(id+"_fields.fieldType");
		String[]fieldFormat=p.get(id+"_fields.fieldFormat");
		
		if(fieldName != null && fieldName.length > 0){
			this.outputFields = new ExcelField[fieldName.length];
			for(int i = 0; i < fieldName.length; i++){
				this.outputFields[i] = new ExcelField();
				this.outputFields[i].setName(fieldName[i]);
				this.outputFields[i].setType(fieldType[i]);
				this.outputFields[i].setFormat(fieldFormat[i]);
				
			}
		}
		else {
			this.outputFields = new ExcelField[0];
		}
	}


	public void readRep(Repository rep, long id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try {
			headerEnabled = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_HEADER);
			footerEnabled = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_FOOTER);
			encoding = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_ENCODING);
			append = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_APPEND);
			addToResult = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
		if (Const.isEmpty(addToResult))
			 addToResultFilenames = true;
		else
			addToResultFilenames = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
			fileName = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILE_NAME);
			extension = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILE_EXTENTION);
			doNotOpenNewFileInit = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT);
			splitEvery = (int) rep.getStepAttributeInteger(id_step,STEP_ATTRIBUTE_FILE_SPLIT);
			stepNrInFilename = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_FILE_ADD_STEPNR);
			dateInFilename = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_FILE_ADD_DATE);
			timeInFilename = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_FILE_ADD_TIME);
			specifyFormat = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_SPECIFY_FORMAT);
			dateTimeFormat = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_DATE_TIME_FORMAT);
			autoSizeColums = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_AUTO_SIZE_COLUMS);
			protectsheet = rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_PROTECT_SHEET);
			password = Encr.decryptPasswordOptionallyEncrypted(rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_PASSWORD));
			templateEnabled = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_TEMPLATE_ENABLED);
			templateAppend = rep.getStepAttributeBoolean(id_step,STEP_ATTRIBUTE_TEMPLATE_APPEND);
			templateFileName = rep.getStepAttributeString(id_step,STEP_ATTRIBUTE_TEMPLATE_FILENAME);
			sheetname = rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_SHEETNAME);
		
			int nrfields = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_FIELD_NAME);

			allocate(nrfields);

			for (int i = 0; i < nrfields; i++) {
				outputFields[i] = new ExcelField();

				outputFields[i].setName(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME));
				outputFields[i].setType(rep.getStepAttributeString(id_step, i,
						STEP_ATTRIBUTE_FIELD_TYPE));
				outputFields[i].setFormat(rep.getStepAttributeString(id_step,
						i, STEP_ATTRIBUTE_FIELD_FORMAT));
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
					STEP_ATTRIBUTE_HEADER, headerEnabled);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FOOTER, footerEnabled);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ENCODING,
					encoding);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_APPEND, append);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES, addToResultFilenames);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_NAME,
					fileName);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DO_NOT_OPEN_NEWFILE_INIT, doNotOpenNewFileInit);

			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_EXTENTION,
					extension);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_SPLIT,
					splitEvery);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_FILE_ADD_STEPNR, stepNrInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_DATE,
					dateInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILE_ADD_TIME,
					timeInFilename);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SPECIFY_FORMAT,
					specifyFormat);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_DATE_TIME_FORMAT, dateTimeFormat);

			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_AUTO_SIZE_COLUMS,
					autoSizeColums);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PROTECT_SHEET,
					protectsheet);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_PASSWORD, Encr
					.encryptPasswordIfNotUsingVariables(password));
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_TEMPLATE_ENABLED, templateEnabled);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_TEMPLATE_APPEND, templateAppend);
			rep.saveStepAttribute(id_transformation, id_step,
					STEP_ATTRIBUTE_TEMPLATE_FILENAME, templateFileName);
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_SHEETNAME,
					sheetname);
			for (int i = 0; i < outputFields.length; i++) {
				ExcelField field = outputFields[i];

				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_NAME, field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_TYPE, field.getTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i,
						STEP_ATTRIBUTE_FIELD_FORMAT, field.getFormat());
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
		CheckResult cr;

		// Check output fields
		if (prev != null && prev.size() > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("ExcelOutputMeta.CheckResult.FieldsReceived", ""
							+ prev.size()), stepMeta);
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
						"ExcelOutputMeta.CheckResult.FieldsNotFound",
						error_message);
				cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR,
						error_message, stepMeta);
				remarks.add(cr);
			} else {
				cr = new CheckResult(
						CheckResultInterface.TYPE_RESULT_OK,
						Messages
								.getString("ExcelOutputMeta.CheckResult.AllFieldsFound"),
						stepMeta);
				remarks.add(cr);
			}
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages
					.getString("ExcelOutputMeta.CheckResult.ExpectedInputOk"),
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(
					CheckResultInterface.TYPE_RESULT_ERROR,
					Messages
							.getString("ExcelOutputMeta.CheckResult.ExpectedInputError"),
					stepMeta);
			remarks.add(cr);
		}

		cr = new CheckResult(CheckResultInterface.TYPE_RESULT_COMMENT, Messages
				.getString("ExcelOutputMeta.CheckResult.FilesNotChecked"),
				stepMeta);
		remarks.add(cr);
	}

	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans trans) {
		return new ExcelOutput(stepMeta, stepDataInterface, cnr, transMeta,
				trans);
	}

	public StepDataInterface getStepData() {
		return new ExcelOutputData();
	}

	public String[] getUsedLibraries() {
		return new String[] { "jxl.jar", };
	}

	

	public boolean isAddToResultFilenames() {
		return addToResultFilenames;
	}

	public void setAddToResultFilenames(boolean addToResultFilenames) {
		this.addToResultFilenames = addToResultFilenames;
	}

	public boolean isProtectsheet() {
		return protectsheet;
	}

	public void setProtectsheet(boolean protectsheet) {
		this.protectsheet = protectsheet;
	}

	public String getAddToResult() {
		return addToResult;
	}

	public void setAddToResult(String addToResult) {
		this.addToResult = addToResult;
	}

	
}
