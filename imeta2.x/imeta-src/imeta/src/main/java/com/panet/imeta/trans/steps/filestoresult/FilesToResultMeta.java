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


package com.panet.imeta.trans.steps.filestoresult;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.panet.imeta.core.CheckResult;
import com.panet.imeta.core.CheckResultInterface;
import com.panet.imeta.core.Counter;
import com.panet.imeta.core.ResultFile;
import com.panet.imeta.core.database.DatabaseMeta;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.exception.KettleXMLException;
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
 * 
 * @author matt
 * @since 26-may-2006
 *
 */

public class FilesToResultMeta extends BaseStepMeta implements StepMetaInterface
{
	private String filenameField;
	
	private int fileType;
	
	/**
	 * @return Returns the fieldname that contains the filename.
	 */
	public String getFilenameField()
	{
		return filenameField;
	}

	/**
	 * @param filenameField set the fieldname that contains the filename.
	 */
	public void setFilenameField(String filenameField)
	{
		this.filenameField = filenameField;
	}

	/**
	 * @return Returns the fileType.
	 * @see ResultFile
	 */
	public int getFileType()
	{
		return fileType;
	}

	/**
	 * @param fileType The fileType to set.
	 * @see ResultFile
	 */
	public void setFileType(int fileType)
	{
		this.fileType = fileType;
	}

	
	
	public FilesToResultMeta()
	{
		super(); // allocate BaseStepMeta
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleXMLException
	{
		readData(stepnode);
	}

	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}
	
	public String getXML()
	{
		StringBuffer xml = new StringBuffer();
		
		xml.append(XMLHandler.addTagValue("filename_field", filenameField));  // $NON-NLS-1
		xml.append(XMLHandler.addTagValue("file_type", ResultFile.getTypeCode(fileType))); //$NON-NLS-1$
		
		return xml.toString();
	}
	
	private void readData(Node stepnode)
	{
		filenameField = XMLHandler.getTagValue(stepnode, "filename_field"); //$NON-NLS-1$
		fileType = ResultFile.getType( XMLHandler.getTagValue(stepnode, "file_type") ); //$NON-NLS-1$
	}

	public void setDefault()
	{
		filenameField=null;
		fileType = ResultFile.FILE_TYPE_GENERAL;
	}

	@Override
	public void setInfo(Map<String, String[]> p, String id,
			List<? extends SharedObjectInterface> databases) {
		// TODO Auto-generated method stub
		filenameField = BaseStepMeta.parameterToString(p.get(id + ".filenameField"));
		fileType = BaseStepMeta.parameterToInt(p.get(id + ".fileType"));
	}
	
	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
		throws KettleException
	{
		filenameField = rep.getStepAttributeString(id_step, "filename_field"); //$NON-NLS-1$
		fileType = ResultFile.getType( rep.getStepAttributeString(id_step, "file_type") ); //$NON-NLS-1$
	}

	public void saveRep(Repository rep, long id_transformation, long id_step)
		throws KettleException
	{
		rep.saveStepAttribute(id_transformation, id_step, "filename_field", filenameField); //$NON-NLS-1$
		rep.saveStepAttribute(id_transformation, id_step, "file_type", ResultFile.getTypeCode(fileType)); //$NON-NLS-1$
	}

	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException
	{
		// Default: nothing changes to rowMeta
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepinfo, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_OK, Messages.getString("FilesToResultMeta.CheckResult.StepReceivingInfoFromOtherSteps"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
		else
		{
			CheckResult cr = new CheckResult(CheckResultInterface.TYPE_RESULT_ERROR, Messages.getString("FilesToResultMeta.CheckResult.NoInputReceivedError"), stepinfo); //$NON-NLS-1$
			remarks.add(cr);
		}
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans)
	{
		return new FilesToResult(stepMeta, stepDataInterface, cnr, tr, trans);
	}

	public StepDataInterface getStepData()
	{
		return new FilesToResultData();
	}

}
