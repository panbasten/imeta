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

package com.panet.imeta.trans.steps.cubeoutput;

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
import com.panet.imeta.core.exception.KettleXMLException;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.xml.XMLHandler;
import com.panet.imeta.repository.Repository;
import com.panet.imeta.shared.SharedObjectInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStepMeta;
import com.panet.imeta.trans.step.StepCategory;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;


/*
 * Created on 4-apr-2003
 *
 */
@Step(name="CubeOutput",image="COP.png",tooltip="BaseStep.TypeTooltipDesc.Cubeoutput",description="BaseStep.TypeLongDesc.CubeOutput",
		category=StepCategory.CATEGORY_OUTPUT)
public class CubeOutputMeta extends BaseStepMeta implements StepMetaInterface
{
	
	public static final String  STEP_ATTRIBUTE_FILENAME   ="filename";  
	public static final String  STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES = "add_to_result_filenames"; 
	public static final String  STEP_ATTRIBUTE_DONOT_OPEN_NEWFILE_INIT = "donot_open_new_file_init"; 

	private String filename;
	/** Flag: add the filenames to result filenames */
    private boolean addToResultFilenames;
    
    /** Flag : Do not open new file when transformation start  */ 
    private boolean doNotOpenNewFileInit;

	public CubeOutputMeta()
	{
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String,Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	/**
	 * @return Returns the filename.
	 */
	public String getFilename()
	{
		return filename;
	}
	
    /**
     * @return Returns the add to result filesname.
     */
    public boolean isAddToResultFiles()
    {
    	return addToResultFilenames;
    }
    
    /**
     * @param addtoresultfilenamesin The addtoresultfilenames to set.
     */
    public void setAddToResultFiles(boolean addtoresultfilenamesin)
    {
        this.addToResultFilenames = addtoresultfilenamesin;
    }
    /**
     * @return Returns the "do not open new file at init" flag.
     */    
    public boolean isDoNotOpenNewFileInit()
    {
    	return doNotOpenNewFileInit;
    }

    /**
     * @param doNotOpenNewFileInit The "do not open new file at init" flag to set.
     */
    public void setDoNotOpenNewFileInit(boolean doNotOpenNewFileInit)
    {
    	this.doNotOpenNewFileInit=doNotOpenNewFileInit;
    }
	public Object clone()
	{
		CubeOutputMeta retval = (CubeOutputMeta)super.clone();

		return retval;
	}
	
	private void readData(Node stepnode)
		throws KettleXMLException
	{
		try
		{
			filename              = XMLHandler.getTagValue(stepnode, "file", "name"); //$NON-NLS-1$ //$NON-NLS-2$
			addToResultFilenames  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "add_to_result_filenames"));
			doNotOpenNewFileInit  = "Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, "file", "do_not_open_newfile_init"));
			
		}
		catch(Exception e)
		{
			throw new KettleXMLException(Messages.getString("CubeOutputMeta.Exception.UnableToLoadStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void setDefault()
	{
		filename             = "file"; //$NON-NLS-1$
		addToResultFilenames = false;
		doNotOpenNewFileInit=false;
	}
	
	public String getXML()
	{
        StringBuffer retval = new StringBuffer(300);
		
		retval.append("    <file>").append(Const.CR); //$NON-NLS-1$
		retval.append("      ").append(XMLHandler.addTagValue("name",       filename)); //$NON-NLS-1$ //$NON-NLS-2$
		retval.append("      ").append(XMLHandler.addTagValue("add_to_result_filenames",   addToResultFilenames));
		retval.append("      ").append(XMLHandler.addTagValue("do_not_open_newfile_init",   doNotOpenNewFileInit));
		
		retval.append("    </file>").append(Const.CR); //$NON-NLS-1$

		return retval.toString();
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		
		filename = BaseStepMeta.parameterToString(p.get(id + ".filename"));
		
		addToResultFilenames = BaseStepMeta.parameterToBoolean(p.get(id + ".addToResultFilenames"));
		doNotOpenNewFileInit = BaseStepMeta.parameterToBoolean(p.get(id + ".doNotOpenNewFileInit"));
	
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String,Counter> counters)
	throws KettleException
	{
		try
		{
			filename               =      rep.getStepAttributeString (id_step, STEP_ATTRIBUTE_FILENAME); //$NON-NLS-1$
			addToResultFilenames   =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES);
			doNotOpenNewFileInit   =      rep.getStepAttributeBoolean(id_step, STEP_ATTRIBUTE_DONOT_OPEN_NEWFILE_INIT);
			
			
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("CubeOutputMeta.Exception.UnexpectedErrorInReadingStepInfo"), e); //$NON-NLS-1$
		}
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_FILENAME,   filename); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_ADD_TO_RESULT_FILENAMES,    addToResultFilenames);  //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, STEP_ATTRIBUTE_DONOT_OPEN_NEWFILE_INIT,    doNotOpenNewFileInit);  //$NON-NLS-1$
			
			
		}
		catch(Exception e)
		{
			throw new KettleException(Messages.getString("CubeOutputMeta.Exception.UnableToSaveStepInfo")+id_step, e); //$NON-NLS-1$
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		
		// Check output fields
		if (prev!=null && prev.size()>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, Messages.getString("CubeOutputMeta.CheckResult.ReceivingFields",String.valueOf(prev.size())), stepinfo); //$NON-NLS-1$ //$NON-NLS-2$
			remarks.add(cr);
		}
		
		cr = new CheckResult(CheckResult.TYPE_RESULT_COMMENT, Messages.getString("CubeOutputMeta.CheckResult.FileSpecificationsNotChecked"), stepinfo); //$NON-NLS-1$
		remarks.add(cr);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans trans)
	{
		return new CubeOutput(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}

	public StepDataInterface getStepData()
	{
		return new CubeOutputData();
	}

	
}
