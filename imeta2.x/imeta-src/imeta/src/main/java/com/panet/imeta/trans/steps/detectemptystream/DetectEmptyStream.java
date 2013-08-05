/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Samatar Hassan.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.trans.steps.detectemptystream;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Detect empty stream.  Pass one row data to the next steps.
 * 
 * @author Samatar
 * @since 30-08-2008
 */
public class DetectEmptyStream extends BaseStep implements StepInterface
{
	private DetectEmptyStreamMeta meta;
	private DetectEmptyStreamData data;
	
	public DetectEmptyStream(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans)
	{
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}
	
	/**
	 * Build an empty row based on the meta-data.
	 * 
	 * @return
	 */
    private Object[] buildOneRow() throws KettleStepException
    {
       // return previous fields name
       Object[] outputRowData = RowDataUtil.allocateRowData(data.outputRowMeta.size());
       return outputRowData;
    }
    
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
		meta=(DetectEmptyStreamMeta)smi;
		data=(DetectEmptyStreamData)sdi;

		Object[] r = getRow();    // get row, set busy!
		if (r==null)  // no more input to be expected...
		{
			if(first)
			{
				// input stream is empty !
				data.outputRowMeta=getTransMeta().getPrevStepFields(getStepMeta());
				putRow(data.outputRowMeta, buildOneRow());     // copy row to possible alternate rowset(s).

		        if (checkFeedback(getLinesRead())) 
		        {
		        	if (log.isBasic()) {
		        		logBasic(Messages.getString("DetectEmptyStream.Log.LineNumber")+getLinesRead()); //$NON-NLS-1$
		        	}
		        }   
			}
			setOutputDone();
			return false;
		}
		
		if(first)
		{
			first=false;
		}
			
		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi)
	{
		meta=(DetectEmptyStreamMeta)smi;
		data=(DetectEmptyStreamData)sdi;
		
		if (super.init(smi, sdi))
		{
		    // Add init code here.
		    return true;
		}
		return false;
	}
	
    //
    // Run is were the action happens!
    public void run()
    {
    	BaseStep.runStepThread(this, meta, data);
    }
}
