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

package com.panet.imeta.trans.steps.rowstoresult;

import com.panet.imeta.core.RowMetaAndData;
import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Writes results to a next transformation in a Job
 * 
 * @author Matt
 * @since 2-jun-2003
 */
public class RowsToResult extends BaseStep implements StepInterface
{
	private RowsToResultMeta meta;

	private RowsToResultData data;

	public RowsToResult(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans)
	{
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
		meta = (RowsToResultMeta) smi;
		data = (RowsToResultData) sdi;

		Object[] r = getRow(); // get row, set busy!
		if (r == null) // no more input to be expected...
		{
			getTransMeta().getResultRows().addAll(data.rows);

			setOutputDone();
			return false;
		}

		// Add all rows to rows buffer...
		data.rows.add( new RowMetaAndData(getInputRowMeta(), r) );
		data.outputRowMeta = getInputRowMeta().clone();
		meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
		putRow(data.outputRowMeta, r); // copy row to possible alternate
										// rowset(s).

		if (checkFeedback(getLinesRead()))
			logBasic(Messages.getString("RowsToResult.Log.LineNumber") + getLinesRead()); //$NON-NLS-1$

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi)
	{
		meta = (RowsToResultMeta) smi;
		data = (RowsToResultData) sdi;

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
