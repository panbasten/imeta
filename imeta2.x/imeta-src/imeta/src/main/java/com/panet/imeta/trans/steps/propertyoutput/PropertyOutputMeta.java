/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
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
package com.panet.imeta.trans.steps.propertyoutput;


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
 * Output rows to Properties file and create a file.
 * 
 * @author Samatar
 * @since 13-Apr-2008
 */
 
 
public class PropertyOutputMeta extends BaseStepMeta implements StepMetaInterface
{
		
	public static final String STEP_ATTRIBUTE_KEYFIELD = "keyfield ";
	public static final String STEP_ATTRIBUTE_VALUEFIELD = "valuefield ";
	public static final String STEP_ATTRIBUTE_COMMENT = "comment ";
	public static final String STEP_ATTRIBUTE_FILE_NAME = "file_name ";
	public static final String STEP_ATTRIBUTE_FILE_EXTENTION = "file_extention ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_STEPNR = "file_add_stepnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_PARTNR = "file_add_partnr ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_DATE = "file_add_date ";
	public static final String STEP_ATTRIBUTE_FILE_ADD_TIME = "file_add_time ";
	public static final String STEP_ATTRIBUTE_CREATE_PARENT_FOLDER = "create_parent_folder";
	public static final String STEP_ATTRIBUTE_ADDTORESULT = "addtoresult ";
		
  
    private String 	 keyfield;
    private String 	 valuefield;
    
	private boolean	 AddToResult;
	
    /** The base name of the output file */
	private  String fileName;
	
	/** The file extention in case of a generated filename */
	private  String  extension;
	
	/** Flag: add the stepnr in the filename */
    private  boolean stepNrInFilename;
	
	/** Flag: add the partition number in the filename */
    private  boolean partNrInFilename;
	
	/** Flag: add the date in the filename */
    private  boolean dateInFilename;
	
	/** Flag: add the time in the filename */
    private  boolean timeInFilename;
    
    /** Flag: create parent folder if needed */
    private boolean createparentfolder;
    
    /** Comment to add in file */
    private String comment;


	 public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	    throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		
		PropertyOutputMeta retval = (PropertyOutputMeta)super.clone();
		return retval;

	}
    
    /**
     * @return Returns the extension.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * @param extension The extension to set.
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }
    
    
    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
    }

    
    /**
     * @return Returns the stepNrInFilename.
     */
    public boolean isStepNrInFilename()
    {
        return stepNrInFilename;
    }

    /**
     * @param stepNrInFilename The stepNrInFilename to set.
     */
    public void setStepNrInFilename(boolean stepNrInFilename)
    {
        this.stepNrInFilename = stepNrInFilename;
    }
    

    /**
     * @return Returns the timeInFilename.
     */
    public boolean isTimeInFilename()
    {
        return timeInFilename;
    }

    /**
     * @return Returns the dateInFilename.
     */
    public boolean isDateInFilename()
    {
        return dateInFilename;
    }

    /**
     * @param dateInFilename The dateInFilename to set.
     */
    public void setDateInFilename(boolean dateInFilename)
    {
        this.dateInFilename = dateInFilename;
    }
    
    /**
     * @param timeInFilename The timeInFilename to set.
     */
    public void setTimeInFilename(boolean timeInFilename)
    {
        this.timeInFilename = timeInFilename;
    }

    
    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    
    /**
     * @return Returns the Add to result filesname flag.
     */
    public boolean AddToResult()
    {
        return AddToResult;
    }
    
    /**
     * @param AddToResult The Add file to result to set.
     */
    public void setAddToResult(boolean AddToResult)
    {
        this.AddToResult = AddToResult;
    }
    
    /**
     * @return Returns the create parent folder flag.
     */
    public boolean isCreateParentFolder()
    {
    	return createparentfolder;
    }
    
    /**
     * @param createparentfolder The create parent folder flag to set.
     */
    public void setCreateParentFolder(boolean createparentfolder)
    {
    	this.createparentfolder=createparentfolder;
    }
    
    public String getComment()
    {
    	return comment;
    }
    
    public void setComment(String commentin)
    {
    	this.comment=commentin;
    }
    
    public String[] getFiles(VariableSpace space)
	{
		int copies=1;
		int parts=1;

		if (stepNrInFilename)
		{
			copies=3;
		}
		
		if (partNrInFilename)
		{
			parts=3;
		}

		int nr=copies*parts;
		if (nr>1) nr++;
		
		String retval[]=new String[nr];
		
		int i=0;
		for (int copy=0;copy<copies;copy++)
		{
			for (int part=0;part<parts;part++)
			{
				
					retval[i]=buildFilename(space, copy);
					i++;
				
			}
		}
		if (i<nr)
		{
			retval[i]="...";
		}
		
		return retval;
	}
	public String buildFilename(VariableSpace space, int stepnr)
	{

		SimpleDateFormat daf     = new SimpleDateFormat();

		// Replace possible environment variables...
		String retval=space.environmentSubstitute(fileName) ;
		

		Date now = new Date();
		
		if (dateInFilename)
		{
			daf.applyPattern("yyyMMdd");
			String d = daf.format(now);
			retval+="_"+d;
		}
		if (timeInFilename)
		{
			daf.applyPattern("HHmmss");
			String t = daf.format(now);
			retval+="_"+t;
		}
		if (stepNrInFilename)
		{
			retval+="_"+stepnr;
		}
		
		
		
		if (extension!=null && extension.length()!=0) 
		{
			retval+="."+extension;
		}
		
		return retval;
	}
    
	private void readData(Node stepnode) throws KettleXMLException
	{
		try
		{

			keyfield    = XMLHandler.getTagValue(stepnode, "keyfield");
			valuefield    = XMLHandler.getTagValue(stepnode, "valuefield");
			comment    = XMLHandler.getTagValue(stepnode, "comment");
			
			
			fileName  = XMLHandler.getTagValue(stepnode, "file", "name");

			createparentfolder ="Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "create_parent_folder"));
			extension = XMLHandler.getTagValue(stepnode, "file", "extention");
			stepNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "split"));
			partNrInFilename     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "haspartno"));
			dateInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_date"));
			timeInFilename  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_time"));
			AddToResult     = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "AddToResult"));
			fileName  = XMLHandler.getTagValue(stepnode, "file", "name");
		
		
        }
		catch(Exception e)
		{
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	public void setDefault()
	{
		
		createparentfolder=false;
		// Items ...
		keyfield    = null;
		valuefield    = null;
		comment  =null;

		extension="properties";
        
	}

	public String getXML()
	{
		StringBuffer retval=new StringBuffer();
		
	
		// Items ...
		
		retval.append("    "+XMLHandler.addTagValue("keyfield",        keyfield));
		retval.append("    "+XMLHandler.addTagValue("valuefield",   valuefield));
		retval.append("    "+XMLHandler.addTagValue("comment",   comment));
		

		retval.append("    <file>"+Const.CR);

		retval.append("      "+XMLHandler.addTagValue("name",       fileName));
		retval.append("      "+XMLHandler.addTagValue("extention",  extension));
		retval.append("      "+XMLHandler.addTagValue("split",      stepNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("haspartno",  partNrInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_date",   dateInFilename));
		retval.append("      "+XMLHandler.addTagValue("add_time",   timeInFilename));
		
		retval.append("      "+XMLHandler.addTagValue("create_parent_folder",   createparentfolder));
		retval.append("    "+XMLHandler.addTagValue("addtoresult",      AddToResult));
		retval.append("      </file>"+Const.CR);
		
		
	
		return retval.toString();
	}

	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
	
		keyfield = BaseStepMeta.parameterToString(p.get(id + ".keyfield"));
		valuefield = BaseStepMeta.parameterToString(p.get(id + ".valuefield"));
		comment = BaseStepMeta.parameterToString(p.get(id + ".comment"));
		fileName = BaseStepMeta.parameterToString(p.get(id + ".fileName"));
		extension = BaseStepMeta.parameterToString(p.get(id + ".extension"));
		
		stepNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id + ".stepNrInFilename"));
		partNrInFilename = BaseStepMeta.parameterToBoolean(p.get(id + ".partNrInFilename"));
		dateInFilename = BaseStepMeta.parameterToBoolean(p.get(id + ".dateInFilename"));
		timeInFilename = BaseStepMeta.parameterToBoolean(p.get(id + ".timeInFilename"));
		createparentfolder = BaseStepMeta.parameterToBoolean(p.get(id + ".createparentfolder"));
		AddToResult = BaseStepMeta.parameterToBoolean(p.get(id + ".AddToResult"));
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
    throws KettleException
    {
		try
		{
			keyfield = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_KEYFIELD);
			valuefield = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_VALUEFIELD);
			comment = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_COMMENT);
			fileName = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_FILE_NAME);
			extension = rep.getStepAttributeString(id_step , STEP_ATTRIBUTE_FILE_EXTENTION);
			stepNrInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_STEPNR);
			partNrInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_PARTNR);
			dateInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_DATE);
			timeInFilename = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_FILE_ADD_TIME);
			createparentfolder = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_CREATE_PARENT_FOLDER);
			AddToResult = rep.getStepAttributeBoolean(id_step , STEP_ATTRIBUTE_ADDTORESULT);
			

		}
		catch(Exception e)
		{
			throw new KettleException("Unexpected error reading step information from the repository", e);
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_KEYFIELD , keyfield);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_VALUEFIELD  , valuefield);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_COMMENT  , comment);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_NAME  , fileName);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_EXTENTION  , extension);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_ADD_STEPNR , stepNrInFilename);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_ADD_PARTNR , partNrInFilename);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_ADD_DATE  , dateInFilename);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_FILE_ADD_TIME , timeInFilename);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_CREATE_PARENT_FOLDER , createparentfolder);
			rep.saveStepAttribute(id_transformation , id_step , STEP_ATTRIBUTE_ADDTORESULT , AddToResult);
			
		}
		catch(Exception e)
		{
			throw new KettleException("Unable to save step information to the repository for id_step="+id_step, e);
		}
	}


	 public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	 {
		
		CheckResult cr;
		// Now see what we can find as previous step...
		if (prev!=null && prev.size()>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("PropertyOutputMeta.CheckResult.FieldsReceived", ""+prev.size()), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("PropertyOutputMeta.CheckResult.NoFields"), stepMeta);
			remarks.add(cr);
		}
				
			
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("PropertyOutputMeta.CheckResult.ExpectedInputOk"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("PropertyOutputMeta.CheckResult.ExpectedInputError"), stepMeta);
			remarks.add(cr);
		}
		
		// Check if filename is given
		if(!Const.isEmpty(fileName))
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("PropertyOutputMeta.CheckResult.FilenameOk"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("PropertyOutputMeta.CheckResult.FilenameError"), stepMeta);
			remarks.add(cr);
		}
		
		// Check for Key field
		
		ValueMetaInterface v = prev.searchValueMeta(keyfield);
		if(v==null)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("PropertyOutputMeta.CheckResult.KeyFieldMissing"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("PropertyOutputMeta.CheckResult.KeyFieldOk"), stepMeta); 
			remarks.add(cr);
		}
		
		// Check for Value field
		
		v = prev.searchValueMeta(valuefield);
		if(v==null)
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("PropertyOutputMeta.CheckResult.ValueFieldMissing"), stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("PropertyOutputMeta.CheckResult.ValueFieldOk"), stepMeta); 
			remarks.add(cr);
		}
		
	}

	
	public StepDataInterface getStepData()
	{
		return new PropertyOutputData();
	}


    
    /**
     * @return the keyfield
     */
    public String getKeyField()
    {
        return keyfield;
    }
    
    /**
     * @return the valuefield
     */
    public String getValueField()
    {
        return valuefield;
    }


    
    /**
     * @param KeyField the keyfield to set
     */
    public void setKeyField(String KeyField)
    {
        this.keyfield = KeyField;
    }
    
    /**
     * @param valuefield the valuefield to set
     */
    public void setValueField(String valuefield)
    {
        this.valuefield = valuefield;
    }
  

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new PropertyOutput(stepMeta, stepDataInterface, cnr, tr, trans);
	}

    
    public boolean supportsErrorHandling()
    {
        return true;
    }

	
}
