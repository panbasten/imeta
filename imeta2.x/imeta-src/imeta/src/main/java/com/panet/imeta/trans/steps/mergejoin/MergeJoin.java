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

package com.panet.imeta.trans.steps.mergejoin;

import java.util.ArrayList;

import com.panet.imeta.core.exception.KettleException;
import com.panet.imeta.core.exception.KettleStepException;
import com.panet.imeta.core.row.RowDataUtil;
import com.panet.imeta.core.row.RowMeta;
import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.Trans;
import com.panet.imeta.trans.TransMeta;
import com.panet.imeta.trans.step.BaseStep;
import com.panet.imeta.trans.step.StepDataInterface;
import com.panet.imeta.trans.step.StepInterface;
import com.panet.imeta.trans.step.StepMeta;
import com.panet.imeta.trans.step.StepMetaInterface;


/**
 * Merge rows from 2 sorted streams and output joined rows with matched key fields.
 * Use this instead of hash join is both your input streams are too big to fit in
 * memory. Note that both the inputs must be sorted on the join key.
 * 
 * This is a first prototype implementation that only handles two streams and
 * inner join. It also always outputs all values from both streams. Ideally, we should:
 *   1) Support any number of incoming streams
 *   2) Allow user to choose the join type (inner, outer) for each stream
 *   3) Allow user to choose which fields to push to next step
 *   4) Have multiple output ports as follows:
 *      a) Containing matched records
 *      b) Unmatched records for each input port
 *   5) Support incoming rows to be sorted either on ascending or descending order.
 *      The currently implementation only supports ascending
 * @author Biswapesh
 * @since 24-nov-2006
 */

public class MergeJoin extends BaseStep implements StepInterface
{
	private MergeJoinMeta meta;
	private MergeJoinData data;
	
	public MergeJoin(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans)
	{
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}
	
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
		meta=(MergeJoinMeta)smi;
		data=(MergeJoinData)sdi;
		int compare;

        if (first)
        {
            first = false;
            
            // Find the RowSet to read from
            //
            data.oneRowSet = findInputRowSet(meta.getStepName1());
            if (data.oneRowSet==null) {
            	throw new KettleException( Messages.getString("MergeJoin.Exception.UnableToFindSpecifiedStep", meta.getStepName1()) );
            }

            data.twoRowSet = findInputRowSet(meta.getStepName2());
            if (data.twoRowSet==null) {
            	throw new KettleException( Messages.getString("MergeJoin.Exception.UnableToFindSpecifiedStep", meta.getStepName2()) );
            }
            
            data.one=getRowFrom(data.oneRowSet);
            if (data.one!=null) 
            {
                data.oneMeta=data.oneRowSet.getRowMeta();
            }
            else
            {
            	data.one=null;
            	data.oneMeta=getTransMeta().getStepFields(meta.getStepName1());
            }
            
            data.two=getRowFrom(data.twoRowSet);
            if (data.two!=null) 
            {
                data.twoMeta=data.twoRowSet.getRowMeta();
            }
            else
            {
            	data.two=null;
            	data.twoMeta=getTransMeta().getStepFields(meta.getStepName2());
            }
            
            // just for speed: oneMeta+twoMeta
            //
            data.outputRowMeta=new RowMeta(); 
            data.outputRowMeta.mergeRowMeta( data.oneMeta.clone() );
            data.outputRowMeta.mergeRowMeta( data.twoMeta.clone() );

            if (data.one!=null)
            {
                // Find the key indexes:
                data.keyNrs1 = new int[meta.getKeyFields1().length];
                for (int i=0;i<data.keyNrs1.length;i++)
                {
                    data.keyNrs1[i] = data.oneMeta.indexOfValue(meta.getKeyFields1()[i]);
                    if (data.keyNrs1[i]<0)
                    {
                        String message = Messages.getString("MergeJoin.Exception.UnableToFindFieldInReferenceStream",meta.getKeyFields1()[i]);  //$NON-NLS-1$ //$NON-NLS-2$
                        logError(message);
                        throw new KettleStepException(message);
                    }
                }
            }

            if (data.two!=null)
            {
                // Find the key indexes:
                data.keyNrs2 = new int[meta.getKeyFields2().length];
                for (int i=0;i<data.keyNrs2.length;i++)
                {
                    data.keyNrs2[i] = data.twoMeta.indexOfValue(meta.getKeyFields2()[i]);
                    if (data.keyNrs2[i]<0)
                    {
                        String message = Messages.getString("MergeJoin.Exception.UnableToFindFieldInReferenceStream",meta.getKeyFields2()[i]);  //$NON-NLS-1$ //$NON-NLS-2$
                        logError(message);
                        throw new KettleStepException(message);
                    }
                }
            }

            // Calculate one_dummy... defaults to null
            data.one_dummy=RowDataUtil.allocateRowData( data.oneMeta.size() + data.twoMeta.size() );
            
            // Calculate two_dummy... defaults to null
            //
            data.two_dummy=new Object[data.twoMeta.size()];
        }

        if (log.isRowLevel()) logRowlevel(Messages.getString("MergeJoin.Log.DataInfo",data.oneMeta.getString(data.one)+"")+data.twoMeta.getString(data.two)); //$NON-NLS-1$ //$NON-NLS-2$

        /*
         * We can stop processing if any of the following is true:
         *   a) Both streams are empty
         *   b) First stream is empty and join type is INNER or LEFT OUTER
         *   c) Second stream is empty and join type is INNER or RIGHT OUTER
         */
        if ((data.one == null && data.two == null) ||
        	(data.one == null && data.one_optional == false) ||
        	(data.two == null && data.two_optional == false))
        {
        	// Before we stop processing, we have to make sure that all rows from both input streams are depleted!
        	// If we don't do this, the transformation can stall.
        	//
        	while (data.one!=null && !isStopped()) data.one = getRowFrom(data.oneRowSet);
        	while (data.two!=null && !isStopped()) data.two = getRowFrom(data.twoRowSet);
        	
            setOutputDone();
            return false;
        }

        if (data.one == null)
        {
        	compare = -1;
        }
        else 
        {
            if (data.two == null)
            {
                compare = 1;
            }
            else
        	{
                int cmp = data.oneMeta.compare(data.one, data.twoMeta, data.two, data.keyNrs1, data.keyNrs2);
                compare = cmp>0?1 : cmp<0?-1 : 0;
            }
        }
        
        switch (compare)
        {
        case 0:
        	/*
        	 * We've got a match. This is what we do next (to handle duplicate keys correctly):
        	 *   Read the next record from both streams
        	 *   If any of the keys match, this means we have duplicates. We therefore
        	 *     Create an array of all rows that have the same keys
        	 *     Push a Cartesian product of the two arrays to output
        	 *   Else
        	 *     Just push the combined rowset to output
        	 */ 
        	data.one_next = getRowFrom(data.oneRowSet);
        	data.two_next = getRowFrom(data.twoRowSet);
        	        	
        	int compare1 = (data.one_next == null) ? -1 : data.oneMeta.compare(data.one, data.one_next, data.keyNrs1, data.keyNrs1);
        	int compare2 = (data.two_next == null) ? -1 : data.twoMeta.compare(data.two, data.two_next, data.keyNrs2, data.keyNrs2);
        	if (compare1 == 0 || compare2 == 0) // Duplicate keys
        	{
            	if (data.ones == null)
            		data.ones = new ArrayList<Object[]>();
            	else
            		data.ones.clear();
            	if (data.twos == null)
            		data.twos = new ArrayList<Object[]>();
            	else
            		data.twos.clear();
            	data.ones.add(data.one);
            	if (compare1 == 0) // First stream has duplicates
            	{
            		data.ones.add(data.one_next);
            		for(;!isStopped();)
	            	{
	                	data.one_next = getRowFrom(data.oneRowSet);
	                	if (0 != ((data.one_next == null) ? -1 : data.oneMeta.compare(data.one, data.one_next, data.keyNrs1, data.keyNrs1))) {
	                		break;
	                	}
	                	data.ones.add(data.one_next);
	            	}
	            	if (isStopped()) return false;
            	}
            	data.twos.add(data.two);
            	if (compare2 == 0) // Second stream has duplicates
            	{
            		data.twos.add(data.two_next);
	            	for(;!isStopped();)
	            	{
	                	data.two_next = getRowFrom(data.twoRowSet);
	                	if (0 != ((data.two_next == null) ? -1 : data.twoMeta.compare(data.two, data.two_next, data.keyNrs2, data.keyNrs2))) {
	                		break;
	                	}
	                	data.twos.add(data.two_next);
	            	}
	            	if (isStopped()) return false;
            	}
            	for (Object[] one : data.ones)
            	{
            		for (Object[] two : data.twos)
            		{
            			Object[] combi = RowDataUtil.addRowData(one, data.oneMeta.size(), two);
            			putRow(data.outputRowMeta, combi);
            		}
            	}
            	data.ones.clear();
            	data.twos.clear();
        	}
        	else // No duplicates
        	{
        		Object[] outputRowData = RowDataUtil.addRowData(data.one, data.oneMeta.size(), data.two);
	        	putRow(data.outputRowMeta, outputRowData);
        	}
        	data.one = data.one_next;
        	data.two = data.two_next;
        	break;
        case 1:
        	if (log.isDebug()) logDebug("First stream has missing key");
        	/*
        	 * First stream is greater than the second stream. This means:
        	 *   a) This key is missing in the first stream
        	 *   b) Second stream may have finished
        	 *  So, if full/right outer join is set and 2nd stream is not null,
        	 *  we push a record to output with only the values for the second
        	 *  row populated. Next, if 2nd stream is not finished, we get a row
        	 *  from it; otherwise signal that we are done
        	 */
        	if (data.one_optional == true)
        	{
        		if (data.two != null)
        		{
        			Object[] outputRowData = RowDataUtil.createResizedCopy(data.one_dummy, data.outputRowMeta.size());
            		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two);
	        		putRow(data.outputRowMeta, outputRowData);
	        		data.two = getRowFrom(data.twoRowSet);
        		}
        		else if (data.two_optional == false)
        		{
        			/*
        			 * If we are doing right outer join then we are done since
        			 * there are no more rows in the second set
        			 */
                	// Before we stop processing, we have to make sure that all rows from both input streams are depleted!
                	// If we don't do this, the transformation can stall.
                	//
                	while (data.one!=null && !isStopped()) data.one = getRowFrom(data.oneRowSet);
                	while (data.two!=null && !isStopped()) data.two = getRowFrom(data.twoRowSet);

        			setOutputDone();
        			return false;
        		}
        		else
        		{
        			/*
        			 * We are doing full outer join so print the 1st stream and
        			 * get the next row from 1st stream
        			 */
            		Object[] outputRowData = RowDataUtil.createResizedCopy(data.one, data.outputRowMeta.size());
            		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two_dummy);
	        		putRow(data.outputRowMeta, outputRowData);
	        		data.one = getRowFrom(data.oneRowSet);
        		}
        	}
        	else if (data.two == null && data.two_optional == true)
        	{
        		/**
        		 * We have reached the end of stream 2 and there are records
        		 * present in the first stream. Also, join is left or full outer.
        		 * So, create a row with just the values in the first stream
        		 * and push it forward
        		 */
        		Object[] outputRowData = RowDataUtil.createResizedCopy(data.one, data.outputRowMeta.size());
        		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two_dummy);
        		putRow(data.outputRowMeta, outputRowData);
        		data.one = getRowFrom(data.oneRowSet);
        	}
        	else if (data.two != null)
        	{
        		/*
        		 * We are doing an inner or left outer join, so throw this row away
        		 * from the 2nd stream
        		 */
        		data.two = getRowFrom(data.twoRowSet);
        	}
        	break;
        case -1:
        	if (log.isDebug()) logDebug("Second stream has missing key");
        	/*
        	 * Second stream is greater than the first stream. This means:
        	 *   a) This key is missing in the second stream
        	 *   b) First stream may have finished
        	 *  So, if full/left outer join is set and 1st stream is not null,
        	 *  we push a record to output with only the values for the first
        	 *  row populated. Next, if 1st stream is not finished, we get a row
        	 *  from it; otherwise signal that we are done
        	 */
        	if (data.two_optional == true)
        	{
        		if (data.one != null)
        		{
            		Object[] outputRowData = RowDataUtil.createResizedCopy(data.one, data.outputRowMeta.size());
            		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two_dummy);
        			putRow(data.outputRowMeta, outputRowData);
	        		data.one = getRowFrom(data.oneRowSet);
        		}
        		else if (data.one_optional == false)
        		{
        			/*
        			 * We are doing a left outer join and there are no more rows
        			 * in the first stream; so we are done
        			 */
                	// Before we stop processing, we have to make sure that all rows from both input streams are depleted!
                	// If we don't do this, the transformation can stall.
                	//
                	while (data.one!=null && !isStopped()) data.one = getRowFrom(data.oneRowSet);
                	while (data.two!=null && !isStopped()) data.two = getRowFrom(data.twoRowSet);

        			setOutputDone();
        			return false;
        		}
        		else
        		{
        			/*
        			 * We are doing a full outer join so print the 2nd stream and
        			 * get the next row from the 2nd stream
        			 */
            		Object[] outputRowData = RowDataUtil.createResizedCopy(data.one_dummy, data.outputRowMeta.size());
            		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two);
        			putRow(data.outputRowMeta, outputRowData);
	        		data.two = getRowFrom(data.twoRowSet);
        		}
        	}
        	else if (data.one == null && data.one_optional == true)
        	{
        		/*
        		 * We have reached the end of stream 1 and there are records
        		 * present in the second stream. Also, join is right or full outer.
        		 * So, create a row with just the values in the 2nd stream
        		 * and push it forward
        		 */
        		Object[] outputRowData = RowDataUtil.createResizedCopy(data.one_dummy, data.outputRowMeta.size());
        		outputRowData = RowDataUtil.addRowData(outputRowData, data.oneMeta.size(), data.two);
        		putRow(data.outputRowMeta, outputRowData);
        		data.two = getRowFrom(data.twoRowSet);
        	}
        	else if (data.one != null)
        	{
        		/*
        		 * We are doing an inner or right outer join so a non-matching row
        		 * in the first stream is of no use to us - throw it away and get the
        		 * next row
        		 */
        		data.one = getRowFrom(data.oneRowSet);
        	}
        	break;
        default:
        	logDebug("We shouldn't be here!!");
        	// Make sure we do not go into an infinite loop by continuing to read data
        	data.one = getRowFrom(data.oneRowSet);
    	    data.two = getRowFrom(data.twoRowSet);
    	    break;
        }
        if (checkFeedback(getLinesRead())) logBasic(Messages.getString("MergeJoin.LineNumber")+getLinesRead()); //$NON-NLS-1$
		return true;
	}
		
	/**
     * @see StepInterface#init( com.panet.imeta.trans.step.StepMetaInterface , com.panet.imeta.trans.step.StepDataInterface)
     */
    public boolean init(StepMetaInterface smi, StepDataInterface sdi)
    {
		meta=(MergeJoinMeta)smi;
		data=(MergeJoinData)sdi;

        if (super.init(smi, sdi))
        {
            if (meta.getStepName1()==null || meta.getStepName2()==null)
            {
                logError(Messages.getString("MergeJoin.Log.BothTrueAndFalseNeeded")); //$NON-NLS-1$
                return false;
            }
            String joinType = meta.getJoinType();
            for (int i = 0; i < MergeJoinMeta.join_types.length; ++i)
            {
            	if (joinType.equalsIgnoreCase(MergeJoinMeta.join_types[i]))
            	{
            		data.one_optional = MergeJoinMeta.one_optionals[i];
            		data.two_optional = MergeJoinMeta.two_optionals[i];
            		return true;
            	}
            }
           	logError(Messages.getString("MergeJoin.Log.InvalidJoinType", meta.getJoinType())); //$NON-NLS-1$
               return false;
        }
        return true;
    }

    /**
     * Checks whether incoming rows are join compatible. This essentially
     * means that the keys being compared should be of the same datatype
     * and both rows should have the same number of keys specified
     * @param row1 Reference row
     * @param row2 Row to compare to
     * 
     * @return true when templates are compatible.
     */
    protected boolean isInputLayoutValid(RowMetaInterface row1, RowMetaInterface row2)
    {
        if (row1!=null && row2!=null)
        {
            // Compare the key types
        	String keyFields1[] = meta.getKeyFields1();
            int nrKeyFields1 = keyFields1.length;
        	String keyFields2[] = meta.getKeyFields2();
            int nrKeyFields2 = keyFields2.length;

            if (nrKeyFields1 != nrKeyFields2)
            {
            	logError("Number of keys do not match " + nrKeyFields1 + " vs " + nrKeyFields2);
            	return false;
            }

            for (int i=0;i<nrKeyFields1;i++)
            {
            	ValueMetaInterface v1 = row1.searchValueMeta(keyFields1[i]);
                if (v1 == null)
                {
                	return false;
                }
                ValueMetaInterface v2 = row2.searchValueMeta(keyFields2[i]);
                if (v2 == null)
                {
                	return false;
                }          
                if ( v1.getType()!=v2.getType() )
                {
                	return false;
                }
            }
        }
        // we got here, all seems to be ok.
        return true;
    }

	//
	// Run is were the action happens!
	public void run()
	{
    	BaseStep.runStepThread(this, meta, data);
	}
}
