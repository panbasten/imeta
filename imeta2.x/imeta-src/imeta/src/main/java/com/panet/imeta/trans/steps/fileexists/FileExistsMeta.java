/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.trans.steps.fileexists;

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
 * Created on 03-Juin-2008
 * 
 */

public class FileExistsMeta extends BaseStepMeta implements StepMetaInterface
{
	public static final String STEP_ATTRIBUTE_FILENAMEFIELD="filenamefield";
	public static final String STEP_ATTRIBUTE_RESULTFIELDNAME="resultfieldname";
	public static final String STEP_ATTRIBUTE_INCLUDEFILETYPE="includefiletype";
	public static final String STEP_ATTRIBUTE_FILETYPEFIELDNAME="filetypefieldname";
	public static final String STEP_ATTRIBUTE_ADDRESULTFILENAMES="addresultfilenames";
	
	
	
	private boolean addresultfilenames;
	
    /** dynamic filename */
    private String       filenamefield;
    
    private String filetypefieldname;

    private boolean  includefiletype;
    
    /** function result: new value name */
    private String       resultfieldname;
    
    public FileExistsMeta()
    {
        super(); // allocate BaseStepMeta
    }

    /**
     * @return Returns the filenamefield.
     */
    public String getDynamicFilenameField()
    {
        return filenamefield;
    }

    /**
     * @param filenamefield The filenamefield to set.
     */
    public void setDynamicFilenameField(String filenamefield)
    {
        this.filenamefield = filenamefield;
    }

    /**
     * @return Returns the resultName.
     */
    public String getResultFieldName()
    {
        return resultfieldname;
    }

    /**
     * @param resultfieldname The resultfieldname to set.
     */
    public void setResultFieldName(String resultfieldname)
    {
        this.resultfieldname = resultfieldname;
    }
    
    /**
     * @param filetypefieldname The filetypefieldname to set.
     */
    public void setFileTypeFieldName(String filetypefieldname)
    {
        this.filetypefieldname = filetypefieldname;
    }

    /**
     * @return Returns the filetypefieldname.
     */
    public String getFileTypeFieldName()
    {
        return filetypefieldname;
    }

    
    public boolean includeFileType()
    {
    	return includefiletype;
    }
    
    public boolean addResultFilenames()
    {
    	return addresultfilenames;
    }
    
    public void setaddResultFilenames(boolean addresultfilenames)
    {
    	this.addresultfilenames=addresultfilenames;
    }
    
    public void setincludeFileType(boolean includefiletype)
    {
    	this.includefiletype=includefiletype;
    }
    
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleXMLException
	{
		readData(stepnode, databases);
	}
 

    public Object clone()
    {
        FileExistsMeta retval = (FileExistsMeta) super.clone();
       
        return retval;
    }

    public void setDefault()
    {
        resultfieldname = "result"; //$NON-NLS-1$
        filetypefieldname=null;
        includefiletype=false;
        addresultfilenames=false;
    }
	
	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface info[], StepMeta nextStep, VariableSpace space) throws KettleStepException
	{    	
        // Output fields (String)
		 if (!Const.isEmpty(resultfieldname))
	     {
			 ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(resultfieldname), ValueMeta.TYPE_BOOLEAN);
			 v.setOrigin(name);
			 inputRowMeta.addValueMeta(v);
	     }
		 
		 if (includefiletype &&  !Const.isEmpty(filetypefieldname))
	     {
			 ValueMetaInterface v = new ValueMeta(space.environmentSubstitute(filetypefieldname), ValueMeta.TYPE_STRING);
			 v.setOrigin(name);
			 inputRowMeta.addValueMeta(v);
	     }
    }

    public String getXML()
    {
        StringBuffer retval = new StringBuffer();

        retval.append("    " + XMLHandler.addTagValue("filenamefield", filenamefield)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    " + XMLHandler.addTagValue("resultfieldname", resultfieldname)); //$NON-NLS-1$ //$NON-NLS-2$
        retval.append("    ").append(XMLHandler.addTagValue("includefiletype",       includefiletype));
        retval.append("    " + XMLHandler.addTagValue("filetypefieldname", filetypefieldname));
        retval.append("    ").append(XMLHandler.addTagValue("addresultfilenames",       addresultfilenames));
        return retval.toString();
    }

    private void readData(Node stepnode, List<? extends SharedObjectInterface> databases)
	throws KettleXMLException
	{
	try
	{
            filenamefield = XMLHandler.getTagValue(stepnode, "filenamefield"); //$NON-NLS-1$
            resultfieldname = XMLHandler.getTagValue(stepnode, "resultfieldname");
            includefiletype  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "includefiletype"));
            filetypefieldname = XMLHandler.getTagValue(stepnode, "filetypefieldname");
            addresultfilenames  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "addresultfilenames"));   
        }
        catch (Exception e)
        {
            throw new KettleXMLException(Messages.getString("FileExistsMeta.Exception.UnableToReadStepInfo"), e); //$NON-NLS-1$
        }
    }

    @Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
    	filenamefield = BaseStepMeta.parameterToString(p.get(id + ".filenamefield"));
    	resultfieldname = BaseStepMeta.parameterToString(p.get(id + ".resultfieldname"));
    	includefiletype = BaseStepMeta.parameterToBoolean(p.get(id + ".includefiletype"));
    	filetypefieldname = BaseStepMeta.parameterToString(p.get(id + ".filetypefieldname"));
    	addresultfilenames = BaseStepMeta.parameterToBoolean(p.get(id + ".addresultfilenames"));
	}
    
    public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
	throws KettleException
	{
    	try
		{
            filenamefield = rep.getStepAttributeString(id_step, "FILENAMEFIELD"); //$NON-NLS-1$
            resultfieldname = rep.getStepAttributeString(id_step, "RESULTFIELDNAME"); //$NON-NLS-1$
            includefiletype  = rep.getStepAttributeBoolean(id_step, "INCLUDEFILETYPE");
            filetypefieldname = rep.getStepAttributeString(id_step, "FILETYPEFIELDNAME"); //$NON-NLS-1$
            addresultfilenames  = rep.getStepAttributeBoolean(id_step, "ADDRESULTFILENAMES");   
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("FileExistsMeta.Exception.UnexpectedErrorReadingStepInfo"), e); //$NON-NLS-1$
        }
    }

    public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
    {
        try
        {
            rep.saveStepAttribute(id_transformation, id_step, "FILENAMEFIELD", filenamefield); //$NON-NLS-1$
            rep.saveStepAttribute(id_transformation, id_step, "RESULTFIELDNAME", resultfieldname); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "INCLUDEFILETYPE",          includefiletype);
			rep.saveStepAttribute(id_transformation, id_step, "FILETYPEFIELDNAME", filetypefieldname);
			rep.saveStepAttribute(id_transformation, id_step, "ADDRESULTFILENAMES",          addresultfilenames);
        }
        catch (Exception e)
        {
            throw new KettleException(Messages.getString("FileExistsMeta.Exception.UnableToSaveStepInfo") + id_step, e); //$NON-NLS-1$
        }
    }

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
        CheckResult cr;
        String error_message = ""; //$NON-NLS-1$

      
        if (Const.isEmpty(resultfieldname))
        {
            error_message = Messages.getString("FileExistsMeta.CheckResult.ResultFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = Messages.getString("FileExistsMeta.CheckResult.ResultFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        if (Const.isEmpty(filenamefield))
        {
            error_message = Messages.getString("FileExistsMeta.CheckResult.FileFieldMissing"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
            remarks.add(cr);
        }
        else
        {
            error_message = Messages.getString("FileExistsMeta.CheckResult.FileFieldOK"); //$NON-NLS-1$
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, error_message, stepMeta);
            remarks.add(cr);
        }
        // See if we have input streams leading to this step!
        if (input.length > 0)
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("FileExistsMeta.CheckResult.ReceivingInfoFromOtherSteps"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }
        else
        {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, Messages.getString("FileExistsMeta.CheckResult.NoInpuReceived"), stepMeta); //$NON-NLS-1$
            remarks.add(cr);
        }

    }

    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
    {
        return new FileExists(stepMeta, stepDataInterface, cnr, transMeta, trans);
    }

    public StepDataInterface getStepData()
    {
        return new FileExistsData();
    }

    public boolean supportsErrorHandling()
    {
        return true;
    }

	
}
