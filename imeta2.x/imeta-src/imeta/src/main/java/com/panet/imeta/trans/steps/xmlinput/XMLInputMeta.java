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

package com.panet.imeta.trans.steps.xmlinput;

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
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.fileinput.FileInputList;
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
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;


public class XMLInputMeta extends BaseStepMeta implements StepMetaInterface
{	
	public static final String STEP_ATTRIBUTE_INCLUDE = "include";
	public static final String STEP_ATTRIBUTE_INCLUDE_FIELD = "include_field";
	public static final String STEP_ATTRIBUTE_ROWNUM = "rownum";
	public static final String STEP_ATTRIBUTE_ROWNUM_FIELD = "rownum_field";
	public static final String STEP_ATTRIBUTE_LIMIT = "limit";
	public static final String STEP_ATTRIBUTE_SKIP = "skip";
	public static final String STEP_ATTRIBUTE_FILE_BASE_URI = "file_base_uri";
	public static final String STEP_ATTRIBUTE_IGNORE_ENTITIES = "ignore_entities";
	public static final String STEP_ATTRIBUTE_NAMESPACE_AWARE = "namespace_aware";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name";
	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
	public static final String STEP_ATTRIBUTE_INPUT_POSITION = "input_position";
//	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name" 
	public static final String STEP_ATTRIBUTE_FILE_MASK = "file_mask";
//	public static final String STEP_ATTRIBUTE_FIELD_NAME = "field_name";
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
//	public static final String STEP_ATTRIBUTE_INPUT_POSITION = "input_position";
	
	/** Array of filenames */
	private  String  fileName[]; 

	/** Wildcard or filemask (regular expression) */
	private  String  fileMask[];
 	 
	/** Flag indicating that we should include the filename in the output */
	private  boolean includeFilename;
	
	/** The name of the field in the output containing the filename */
	private  String  filenameField;
	
	/** Flag indicating that a row number field should be included in the output */
	private  boolean includeRowNumber;
	
	/** The name of the field in the output containing the row number*/
	private  String  rowNumberField;
	
	/** The maximum number or lines to read */
	private  long  rowLimit;

    /** The number or lines to skip before starting to read*/
    private  int  nrRowsToSkip;
    
    /** support XML namespaces */
    private boolean namespaceAware;
    
    /**  Ignores external entities and returns an empty dummy e.g. for missing DTDs */
    private boolean ignoreEntities;
    
    /** Provide a base for resolving relative URIs */
	private String fileBaseURI;

	/** The fields to import... */
	private XMLInputField inputFields[];

    /** The position in the XML documents to start (elements)*/
    private String inputPosition[];
	
	public XMLInputMeta()
	{
		super(); // allocate BaseStepMeta
	}
	
	
	/**
     * @return Returns the input fields.
     */
    public XMLInputField[] getInputFields()
    {
        return inputFields;
    }
    
    /**
     * @param inputFields The input fields to set.
     */
    public void setInputFields(XMLInputField[] inputFields)
    {
        this.inputFields = inputFields;
    }

    /**
     * @return Returns the fileMask.
     */
    public String[] getFileMask()
    {
        return fileMask;
    }
    /**
     * @param fileMask The fileMask to set.
     */
    public void setFileMask(String[] fileMask)
    {
        this.fileMask = fileMask;
    }
    
    /**
     * @return Returns the fileName.
     */
    public String[] getFileName()
    {
        return fileName;
    }
    
    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String[] fileName)
    {
        this.fileName = fileName;
    }
    
    /**
     * @return Returns the filenameField.
     */
    public String getFilenameField()
    {
        return filenameField;
    }
    
    /**
     * @param filenameField The filenameField to set.
     */
    public void setFilenameField(String filenameField)
    {
        this.filenameField = filenameField;
    }
    
    /**
     * @return Returns the includeFilename.
     */
    public boolean includeFilename()
    {
        return includeFilename;
    }
    
    /**
     * @param includeFilename The includeFilename to set.
     */
    public void setIncludeFilename(boolean includeFilename)
    {
        this.includeFilename = includeFilename;
    }
    
    /**
     * @return Returns the includeRowNumber.
     */
    public boolean includeRowNumber()
    {
        return includeRowNumber;
    }
    
    /**
     * @param includeRowNumber The includeRowNumber to set.
     */
    public void setIncludeRowNumber(boolean includeRowNumber)
    {
        this.includeRowNumber = includeRowNumber;
    }
    
    /**
     * @return Returns the rowLimit.
     */
    public long getRowLimit()
    {
        return rowLimit;
    }
    
    /**
     * @param rowLimit The rowLimit to set.
     */
    public void setRowLimit(long rowLimit)
    {
        this.rowLimit = rowLimit;
    }

    
    /**
     * @return Returns the nrRowsToSkip.
     */
    public int getNrRowsToSkip()
    {
        return nrRowsToSkip;
    }


    /**
     * @param nrRowsToSkip The nrRowsToSkip to set.
     */
    public void setNrRowsToSkip(int nrRowsToSkip)
    {
        this.nrRowsToSkip = nrRowsToSkip;
    }


    /**
     * @return Returns the rowNumberField.
     */
    public String getRowNumberField()
    {
        return rowNumberField;
    }
    
    /**
     * @param rowNumberField The rowNumberField to set.
     */
    public void setRowNumberField(String rowNumberField)
    {
        this.rowNumberField = rowNumberField;
    }
    
    
    
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		XMLInputMeta retval = (XMLInputMeta)super.clone();
		
		int nrFiles  = fileName.length;
		int nrFields = inputFields.length;
        int nrPositions = inputPosition.length;

		retval.allocate(nrFiles, nrFields, nrPositions);
		
		for (int i=0;i<nrFields;i++)
		{
            if (inputFields[i]!=null)
            {
                retval.inputFields[i] = (XMLInputField)inputFields[i].clone();
            }
		}
        
        for (int i=0;i<nrPositions;i++)
        {
            retval.inputPosition[i] = inputPosition[i];
        }
		
		return retval;
	}

    
    public String getXML()
    {
        StringBuffer retval=new StringBuffer();
        
        retval.append("    "+XMLHandler.addTagValue("include",         includeFilename));
        retval.append("    "+XMLHandler.addTagValue("include_field",   filenameField));
        retval.append("    "+XMLHandler.addTagValue("rownum",          includeRowNumber));
        retval.append("    "+XMLHandler.addTagValue("rownum_field",    rowNumberField));
        retval.append("    "+XMLHandler.addTagValue("file_base_uri",   fileBaseURI));
        retval.append("    "+XMLHandler.addTagValue("ignore_entities", ignoreEntities));
        retval.append("    "+XMLHandler.addTagValue("namespace_aware", namespaceAware));
        
        retval.append("    <file>"+Const.CR);
        for (int i=0;i<fileName.length;i++)
        {
            retval.append("      "+XMLHandler.addTagValue("name",     fileName[i]));
            retval.append("      "+XMLHandler.addTagValue("filemask", fileMask[i]));
        }
        retval.append("      </file>"+Const.CR);
        
        retval.append("    <fields>"+Const.CR);
        for (int i=0;i<inputFields.length;i++)
        {
            XMLInputField field = inputFields[i];
            retval.append(field.getXML());
        }
        retval.append("      </fields>"+Const.CR);
        
        retval.append("    <positions>"+Const.CR);
        for (int i=0;i<inputPosition.length;i++)
        {
            retval.append("      "+XMLHandler.addTagValue("position", inputPosition[i]));
        }
        
        retval.append("      </positions>"+Const.CR);

        retval.append("    "+XMLHandler.addTagValue("limit", rowLimit));
        retval.append("    "+XMLHandler.addTagValue("skip", nrRowsToSkip));

        return retval.toString();
    }

    public void setInfo(Map<String, String[]> p, String id,
 			List<? extends SharedObjectInterface> databases) {
    	
    	includeFilename = BaseStepMeta.parameterToBoolean(p.get(id + ".includeFilename"));
		filenameField = BaseStepMeta.parameterToString(p.get(id + ".filenameField"));
		includeRowNumber = BaseStepMeta.parameterToBoolean(p.get(id + ".includeRowNumber"));
		rowNumberField = BaseStepMeta.parameterToString(p.get(id + ".rowNumberField"));
		fileBaseURI = BaseStepMeta.parameterToString(p.get(id + ".fileBaseURI"));
		ignoreEntities = BaseStepMeta.parameterToBoolean(p.get(id + ".ignoreEntities"));
		namespaceAware = BaseStepMeta.parameterToBoolean(p.get(id + ".namespaceAware"));
		rowLimit = BaseStepMeta.parameterToLong(p.get(id + ".rowLimit"));
		nrRowsToSkip = BaseStepMeta.parameterToInt(p.get(id + ".nrRowsToSkip"));
		
		String[] fileName = p.get(id + "_selectedFiles.fileName");
	    String[] fileMask = p.get(id + "_selectedFiles.fileMask");
	    this.fileName = (fileName != null) ? fileName : (new String[0]);
        this.fileMask = (fileMask != null) ? fileMask : (new String[0]);
        
        String[] inputPosition = p.get(id + "_location.element");
        this.inputPosition = (inputPosition != null) ? inputPosition : (new String[0]);
        
        String[] name = p.get(id + "_fields.fieldName");
        String[] type = p.get(id + "_fields.fieldType");
        String[] format = p.get(id + "_fields.fieldFormat");
        String[] length = p.get(id + "_fields.fieldLength");
        String[] precision = p.get(id + "_fields.fieldPrecision");
        String[] currency = p.get(id + "_fields.fieldCurrency");
        String[] decimal = p.get(id + "_fields.fieldDecimal");
        String[] group = p.get(id + "_fields.fieldGroup");
        String[] trimtype = p.get(id + "_fields.fieldTrimType");
        String[] repeat = p.get(id + "_fields.fieldRepeat");
        String[] position = p.get(id + "_fields.fieldPosition");
        
        if (name != null && name.length > 0){
        	this.inputFields = new XMLInputField[name.length];
        	for(int i = 0; i < name.length; i++){
        		this.inputFields[i] = new XMLInputField();
        		this.inputFields[i].setName(name[i]);
        		this.inputFields[i].setType(Integer.parseInt(type[i]));
        		this.inputFields[i].setFormat(format[i]);
        		if(length[i].length() == 0)
        			length[i] = "-1";
        		this.inputFields[i].setLength(Integer.parseInt(length[i]));
        		if(precision[i].length() == 0)
        			precision[i] = "-1";
        		this.inputFields[i].setPrecision(Integer.parseInt(precision[i]));
        		this.inputFields[i].setCurrencySymbol(currency[i]);
        		this.inputFields[i].setDecimalSymbol(decimal[i]);
        		this.inputFields[i].setGroupSymbol(group[i]);
        		this.inputFields[i].setTrimType(Integer.parseInt(trimtype[i]));
        		this.inputFields[i].setRepeated(Boolean
						.parseBoolean(repeat[i]));
        		try {
        			if(position[i].length() == 0)
            			length[i] = "";
					this.inputFields[i].setFieldPosition(position[i]);
				} catch (KettleException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    
	private void readData(Node stepnode) throws KettleXMLException
	{
		try
		{
			includeFilename   = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "include"));
			filenameField     = XMLHandler.getTagValue(stepnode, "include_field");
			includeRowNumber  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "rownum"));
			rowNumberField    = XMLHandler.getTagValue(stepnode, "rownum_field");
			fileBaseURI       = XMLHandler.getTagValue(stepnode, "file_base_uri");
			ignoreEntities  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "ignore_entities"));
			namespaceAware  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "namespace_aware"));
	
			Node filenode  = XMLHandler.getSubNode(stepnode,   "file");
			Node fields    = XMLHandler.getSubNode(stepnode,   "fields");
            Node positions = XMLHandler.getSubNode(stepnode,   "positions");
			int nrFiles     = XMLHandler.countNodes(filenode,  "name");
			int nrFields    = XMLHandler.countNodes(fields,    "field");
            int nrPositions = XMLHandler.countNodes(positions, "position");
	
			allocate(nrFiles, nrFields, nrPositions);
			
			for (int i=0;i<nrFiles;i++)
			{
				Node filenamenode = XMLHandler.getSubNodeByNr(filenode, "name", i); 
				Node filemasknode = XMLHandler.getSubNodeByNr(filenode, "filemask", i); 
				fileName[i] = XMLHandler.getNodeValue(filenamenode);
				fileMask[i] = XMLHandler.getNodeValue(filemasknode);
			}
			
			for (int i=0;i<nrFields;i++)
			{
				Node fnode = XMLHandler.getSubNodeByNr(fields, "field", i);
				XMLInputField field = new XMLInputField(fnode);
				inputFields[i] = field;
			}
            
            for (int i=0;i<nrPositions;i++)
            {
                Node positionnode = XMLHandler.getSubNodeByNr(positions, "position", i); 
                inputPosition[i] = XMLHandler.getNodeValue(positionnode);
            }
			
			// Is there a limit on the number of rows we process?
			rowLimit = Const.toLong(XMLHandler.getTagValue(stepnode, "limit"), 0L);
            // Do we skip rows before starting to read
            nrRowsToSkip = Const.toInt(XMLHandler.getTagValue(stepnode, "skip"), 0);
		}
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}
	
	public void allocate(int nrfiles, int nrfields, int nrPositions)
	{
		fileName   = new String [nrfiles];
		fileMask   = new String [nrfiles];
		
		inputFields = new XMLInputField[nrfields];
        
        inputPosition = new String[nrPositions];
	}
	
	public void setDefault()
	{
		includeFilename    = false;
		filenameField = "";
		includeRowNumber    = false;
		rowNumberField = "";
		fileBaseURI = "";
		ignoreEntities = false;
		namespaceAware = false;
		
		int nrFiles=0;
		int nrFields=0;
        int nrPositions=0;

		allocate(nrFiles, nrFields, nrPositions);	
		
		for (int i=0;i<nrFiles;i++) 
		{
			fileName[i]="filename"+(i+1);
			fileMask[i]="";
		}
		
		for (int i=0;i<nrFields;i++)
		{
		    inputFields[i] = new XMLInputField("field"+(i+1), null);
		}

        for (int i=0;i<nrPositions;i++)
        {
            inputPosition[i] = "position"+(i+1);
        }

		rowLimit=0;
        nrRowsToSkip=0;
	}
	
	public void getFields(RowMetaInterface r, String name, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	{
		for (int i=0;i<inputFields.length;i++)
		{
		    XMLInputField field = inputFields[i];
		    
			int type=field.getType();
			if (type==ValueMeta.TYPE_NONE) type=ValueMeta.TYPE_STRING;
			ValueMetaInterface v=new ValueMeta(field.getName(), type);
			v.setLength(field.getLength(), field.getPrecision());
			v.setConversionMask(field.getFormat());
			v.setDecimalSymbol(field.getDecimalSymbol());
			v.setGroupingSymbol(field.getGroupSymbol());
			v.setTrimType(field.getTrimType());
			v.setCurrencySymbol(field.getCurrencySymbol());
			v.setOrigin(name);
			r.addValueMeta(v);
		}
		if (includeFilename)
		{
			ValueMetaInterface v = new ValueMeta(filenameField, ValueMeta.TYPE_STRING);
			v.setLength(100);
			v.setOrigin(name);
			r.addValueMeta(v);
		}
		if (includeRowNumber)
		{
			ValueMetaInterface v = new ValueMeta(rowNumberField, ValueMeta.TYPE_INTEGER);
			v.setLength(ValueMetaInterface.DEFAULT_INTEGER_LENGTH);
			v.setOrigin(name);
			r.addValueMeta(v);
		}

	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		try
		{
			includeFilename   =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_INCLUDE);  
			filenameField     =      rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_INCLUDE_FIELD);
	
			includeRowNumber  =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ROWNUM);
			rowNumberField    =      rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_ROWNUM_FIELD);
			rowLimit          =      rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_LIMIT);
            nrRowsToSkip      = (int)rep.getStepAttributeInteger(id_step, STEP_ATTRIBUTE_SKIP);
            fileBaseURI       =      rep.getStepAttributeString(id_step, STEP_ATTRIBUTE_FILE_BASE_URI);
            ignoreEntities    =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_IGNORE_ENTITIES);
            namespaceAware    =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_NAMESPACE_AWARE);
	
			int nrFiles     = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_FILE_NAME);
			int nrFields    = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_FIELD_NAME);
            int nrPositions = rep.countNrStepAttributes(id_step, STEP_ATTRIBUTE_INPUT_POSITION);
			
			allocate(nrFiles, nrFields, nrPositions);

			for (int i=0;i<nrFiles;i++)
			{
				fileName[i] =      rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FILE_NAME    );
				fileMask[i] =      rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FILE_MASK    );
			}

			for (int i=0;i<nrFields;i++)
			{
			    XMLInputField field = new XMLInputField();
			    
				field.setName( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_NAME) );
				field.setType( ValueMeta.getType( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_TYPE) ) );
				field.setFormat( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_FORMAT) );
				field.setCurrencySymbol( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_CURRENCY) );
				field.setDecimalSymbol( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_DECIMAL) );
				field.setGroupSymbol( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_GROUP) );
				field.setLength( (int)rep.getStepAttributeInteger(id_step, i, STEP_ATTRIBUTE_FIELD_LENGTH) );
				field.setPrecision( (int)rep.getStepAttributeInteger(id_step, i, STEP_ATTRIBUTE_FIELD_PRECISION) );
				field.setTrimType( XMLInputField.getTrimTypeByCode( rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_FIELD_TRIM_TYPE) ));
				field.setRepeated( rep.getStepAttributeBoolean(id_step, i, STEP_ATTRIBUTE_FIELD_REPEAT) );

                String fieldPositionCode = rep.getStepAttributeString(id_step, i, STEP_ATTRIBUTE_FIELD_POSITION_CODE); 
                if (fieldPositionCode!=null)
                {
                    field.setFieldPosition( fieldPositionCode );
                }
                
				inputFields[i] = field;
			}
            
            for (int i=0;i<nrPositions;i++)
            {
                inputPosition[i] = rep.getStepAttributeString (id_step, i, STEP_ATTRIBUTE_INPUT_POSITION    );
            }

		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("XMLInputMeta.Exception.ErrorReadingRepository"), e);
		}
	}
	
	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "include",         includeFilename);
			rep.saveStepAttribute(id_transformation, id_step, "include_field",   filenameField);
			rep.saveStepAttribute(id_transformation, id_step, "rownum",          includeRowNumber);
			rep.saveStepAttribute(id_transformation, id_step, "rownum_field",    rowNumberField);
			rep.saveStepAttribute(id_transformation, id_step, "limit",           rowLimit);
            rep.saveStepAttribute(id_transformation, id_step, "skip",            nrRowsToSkip);
            rep.saveStepAttribute(id_transformation, id_step, "file_base_uri",   fileBaseURI);
            rep.saveStepAttribute(id_transformation, id_step, "ignore_entities", ignoreEntities);
            rep.saveStepAttribute(id_transformation, id_step, "namespace_aware", namespaceAware);
			
			for (int i=0;i<fileName.length;i++)
			{
				rep.saveStepAttribute(id_transformation, id_step, i, "file_name",     fileName[i]);
				rep.saveStepAttribute(id_transformation, id_step, i, "file_mask",     fileMask[i]);
			}
			
			for (int i=0;i<inputFields.length;i++)
			{
			    XMLInputField field = inputFields[i];
			    
				rep.saveStepAttribute(id_transformation, id_step, i, "field_name",          field.getName());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_type",          field.getTypeDesc());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_format",        field.getFormat());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_currency",      field.getCurrencySymbol());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_decimal",       field.getDecimalSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_group",         field.getGroupSymbol());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_length",        field.getLength());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_precision",     field.getPrecision());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_trim_type",     field.getTrimTypeCode());
				rep.saveStepAttribute(id_transformation, id_step, i, "field_repeat",        field.isRepeated());
                rep.saveStepAttribute(id_transformation, id_step, i, "field_position_code", field.getFieldPositionsCode());
			}
            
            for (int i=0;i<inputPosition.length;i++)
            {
                rep.saveStepAttribute(id_transformation, id_step, i, "input_position",  inputPosition[i]);
            }
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("XMLInputMeta.Exception.ErrorSavingToRepository", ""+id_step), e);
		}
	}
	

	public FileInputList getFiles(VariableSpace space)
	{
        String required[] = new String[fileName.length];
        boolean subdirs[] = new boolean[fileName.length]; // boolean arrays are defaulted to false.
        for (int i=0;i<required.length; required[i]="Y", i++); //$NON-NLS-1$
        return FileInputList.createFileList(space, fileName, fileMask, required, subdirs);

        /*
		// Replace possible environment variables...
		final String realfile[] = StringUtil.environmentSubstitute(fileName);
		final String realmask[] = StringUtil.environmentSubstitute(fileMask);
		
		ArrayList filelist = new ArrayList();
		
		for (int i=0;i<realfile.length;i++)
		{
			final String onefile = realfile[i];
			final String onemask = realmask[i];
            
            if (onefile==null)
            {
                System.out.println("empty file???");
            }
			
			if (!Const.isEmpty(onemask)) // A directory & a wildcard
			{
				File file = new File(onefile);
				try
				{
					files = file.list(new FilenameFilter() { public boolean accept(File dir, String name)
							{ return Pattern.matches(onemask, name); } } );
					
					for (int j = 0; j < files.length; j++)
					{
						if (!onefile.endsWith(Const.FILE_SEPARATOR))
						{
							files[j] = onefile+Const.FILE_SEPARATOR+files[j];
						}
						else
						{
							files[j] = onefile+files[j];
						}
					}
				}
				catch(Exception e)
				{
					files=null;
				}
			}
			else // A normal file...
			{
				// Check if it exists...
				File file = new File(onefile);
				if (file.exists() && file.isFile() && file.canRead() )
				{
					files = new String[] { onefile };
				}
				else // File is not accessible to us.
				{
					files = null;
				}
			}

			// Add to our list...
			if (files!=null)
			for (int x=0;x<files.length;x++)
			{				
				filelist.add(files[x]);
			}
		}
        
        // Sort the list: quicksort
        Collections.sort(filelist);

		// OK, return the list in filelist...
		files = (String[])filelist.toArray(new String[filelist.size()]);

		return files;
        */
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;

		// See if we get input...
		if (input.length>0)
		{		
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("XMLInputMeta.CheckResult.NoInputExpected"), stepinfo);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("XMLInputMeta.CheckResult.NoInput"), stepinfo);
			remarks.add(cr);
		}
		
        FileInputList fileInputList = getFiles(transMeta);
		// String files[] = getFiles();
		if (fileInputList==null || fileInputList.getFiles().size()==0)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("XMLInputMeta.CheckResult.NoFiles"), stepinfo);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("XMLInputMeta.CheckResult.FilesOk", ""+fileInputList.getFiles().size()), stepinfo);
			remarks.add(cr);
		}
	}
	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new XMLInput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new XMLInputData();
	}


    /**
     * @return Returns the inputPosition.
     */
    public String[] getInputPosition()
    {
        return inputPosition;
    }


    @Override
    public List<ResourceReference> getResourceDependencies(TransMeta transMeta, StepMeta stepInfo) {
       List<ResourceReference> references = new ArrayList<ResourceReference>(5);
       ResourceReference reference = new ResourceReference(stepInfo);
       references.add(reference);

       //
       // Get the file path list from the FileInputList
       //
       String required[] = new String[fileName.length];
       boolean subdirs[] = new boolean[fileName.length]; // boolean arrays are defaulted to false.
       for (int i=0;i<required.length; required[i]="N", i++); //$NON-NLS-1$
       String[] textFiles = FileInputList.createFilePathList(transMeta, fileName, fileMask, required, subdirs);
       
       if ( textFiles!=null ) {
         for (int i=0; i<textFiles.length; i++) {
           reference.getEntries().add( new ResourceEntry(textFiles[i], ResourceType.FILE));
         }
       }
       return references;
    }
    
    /**
     * @param inputPosition The inputPosition to set.
     */
    public void setInputPosition(String[] inputPosition)
    {
        this.inputPosition = inputPosition;
    }


	/**
	 * @return Provide a base for resolving relative URIs
	 */
	public String getFileBaseURI() {
		return fileBaseURI;
	}


	/**
	 * @param fileBaseURI Provide a base for resolving relative URIs
	 */
	public void setFileBaseURI(String fileBaseURI) {
		this.fileBaseURI = fileBaseURI;
	}

	/**
	 * @return Ignores external entities and returns an empty dummy e.g. for missing DTDs
	 */
	public boolean isIgnoreEntities() {
		return ignoreEntities;
	}


	/**
	 * @param ignoreEntities Ignores external entities and returns an empty dummy e.g. for missing DTDs
	 */
	public void setIgnoreEntities(boolean ignoreEntities) {
		this.ignoreEntities = ignoreEntities;
	}


	/**
	 * @return support XML namespaces
	 */
	public boolean isNamespaceAware() {
		return namespaceAware;
	}


	/**
	 * @param namespaceAware support XML namespaces
	 */
	public void setNamespaceAware(boolean namespaceAware) {
		this.namespaceAware = namespaceAware;
	}
}
