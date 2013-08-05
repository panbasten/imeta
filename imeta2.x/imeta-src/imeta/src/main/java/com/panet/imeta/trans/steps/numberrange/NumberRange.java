/*
 * Copyright (c) 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Ronny Roeller.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package com.panet.imeta.trans.steps.numberrange;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;

/**
 * Business logic for the NumberRange
 * 
 * @author ronny.roeller@fredhopper.com
 *
 */
public class NumberRange extends BaseStep implements StepInterface
 {
	 
	 	private NumberRangeData data;
	 	private NumberRangeMeta meta;
	 
	 	private NumberRangeSet numberRange;
	 	
	 	/**
	 * Column number where the input value is stored
	 */
	 
	 	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
						throws KettleException 
		{
	 		Object[] row = getRow();
	 		if (row == null) 
	 		{
	 			setOutputDone();
	 			return false;
	 		}
	 		
	 		if (first) {
				first = false;
	
				numberRange = new NumberRangeSet(meta.getRules(), meta.getFallBackValue());
				data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
				// Prepare output fields
				meta.getFields(data.outputRowMeta, getStepname(), null, null,this);
	
				// Find column numbers
				data.inputColumnNr = data.outputRowMeta.indexOfValue(meta.getInputField());
	
				// Check if a field was not available
				if (data.inputColumnNr < 0) {
					logError("Field for input could not be found: "	+ meta.getInputField());
					return false;
				}
			}
	 		try {
		 		// get field value
		 		String strValue = getInputRowMeta().getString(row,data.inputColumnNr);
	
		 		// return range
		 		String ranges = numberRange.evaluate(strValue);
		 		// add value to output
				row = RowDataUtil.addRowData(row, getInputRowMeta().size(),	new Object[] { ranges });
				putRow(data.outputRowMeta, row);
		        if (checkFeedback(getLinesRead())) 
	            {
	            	if(log.isDetailed()) logDetailed(Messages.getString("NumberRange.Log.LineNumber")+getLinesRead()); //$NON-NLS-1$
	            }
	 		}
			catch(KettleException e)
			{
				boolean sendToErrorRow=false;
				String errorMessage = null;
				
				if (getStepMeta().isDoingErrorHandling())
				{
			          sendToErrorRow = true;
			          errorMessage = e.toString();
				}
				else
				{
					logError(Messages.getString("NumberRange.Log.ErrorInStepRunning")+e.getMessage()); //$NON-NLS-1$
					setErrors(1);
					stopAll();
					setOutputDone();  // signal end to receiver(s)
					return false;
				}
				if (sendToErrorRow)
				{
				   // Simply add this row to the error row
				   putError(getInputRowMeta(), row, 1, errorMessage, null, "NumberRange001");
				}
			}		
				
			return true;
	 	}
	 
		//
		// Run is were the action happens!
		public void run()
		{
	    	BaseStep.runStepThread(this, meta, data);
		}
	 
	 	public NumberRange(StepMeta s, StepDataInterface stepDataInterface,
						int c, TransMeta t, Trans dis) 
	 	{
	 		super(s, stepDataInterface, c, t, dis);
	 	}
	 
	 	public boolean init(StepMetaInterface smi, StepDataInterface sdi) 
		{
			meta = (NumberRangeMeta) smi;
			data = (NumberRangeData) sdi;
			
			return super.init(smi, sdi);
		}
	 
	 	public void dispose(StepMetaInterface smi, StepDataInterface sdi) 
		{
			meta = (NumberRangeMeta) smi;
			data = (NumberRangeData) sdi;
	
			super.dispose(smi, sdi);
		}
	 
	 }
