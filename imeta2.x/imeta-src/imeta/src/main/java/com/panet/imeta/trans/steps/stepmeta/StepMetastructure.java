package com.panet.imeta.trans.steps.stepmeta;

import com.panet.imeta.core.Const;
import com.panet.imeta.core.exception.KettleException;
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
import com.panet.imeta.trans.steps.delete.Messages;


/**
 * Return the structure of the stream
 * 
 * @author Ingo Klose
 * @since  22-april-2008
 */

public class StepMetastructure extends BaseStep implements StepInterface
{   
	private StepMetastructureMeta meta;
	private StepMetastructureData data;
	
	public StepMetastructure(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans)
	{
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}
	
	public boolean init(StepMetaInterface smi, StepDataInterface sdi)
	{
		meta=(StepMetastructureMeta)smi;
		data=(StepMetastructureData)sdi;
		
		if (super.init(smi, sdi))
		{
		    // Add init code here.
			data.rowCount = 0;
		    return true;
		}
		return false;
	} 
	
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	
		Object[] r=getRow();       // Get row from input rowset & set row busy!
		Object[] metastructureRow = null;		
		
		// initialize 
		if (first)
		{
			first = false;

			// handle empty input 
			if (r==null) {
		    	setOutputDone();
		    	return false;
			}
			
			// Create the row metadata for the output rows
			// 
			data.outputRowMeta = new RowMeta();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
		}
		
    	if (r==null) 
    	{
    		metastructureRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());
		    
		    RowMetaInterface row = getInputRowMeta().clone();
		    
		    for (int i = 0; i < row.size(); i++) {
		    	
		        ValueMetaInterface v = row.getValueMeta(i);
		        
		        ValueMetaInterface v_position = data.outputRowMeta.getValueMeta(0);
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 0,v_position.convertDataCompatible(v_position,new Long(i+1)));
		        
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 1, v.getName());
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 2, v.getComments());
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 3, v.getTypeDesc());
		        
		        ValueMetaInterface v_length = data.outputRowMeta.getValueMeta(4);
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 4, v_length.convertDataCompatible(v_length,new Long(v.getLength())));
		        
		        ValueMetaInterface v_precision = data.outputRowMeta.getValueMeta(5);
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 5, v_precision.convertDataCompatible(v_precision,new Long(v.getPrecision())));
		        
		        metastructureRow=RowDataUtil.addValueData(metastructureRow, 6, v.getOrigin());
		        
		        if (meta.isOutputRowcount()) {
			        ValueMetaInterface v_rowCount = data.outputRowMeta.getValueMeta(7);
			        metastructureRow=RowDataUtil.addValueData(metastructureRow, 7,v_rowCount.convertDataCompatible(v_rowCount,new Long(data.rowCount)));
		        }
		        putRow(data.outputRowMeta, metastructureRow.clone());
		    }
		    
		    // We're done, call it a day
		    //
	    	setOutputDone();
	    	return false;
    	}
	    
	    data.rowCount++;

	    return true;
        
    }
	
	//
    // Run is were the action happens!
    public void run()
    {
    	//BaseStep.runStepThread(this, meta, data);
    	try
		{
			logBasic(Messages.getString("System.Log.StartingToRun")); //$NON-NLS-1$
			
			while (processRow(meta, data) && !isStopped());
		}
		catch(Throwable t)
		{
			logError(Messages.getString("System.Log.UnexpectedError")+" : "); //$NON-NLS-1$ //$NON-NLS-2$
            logError(Const.getStackTracker(t));
            setErrors(1);
			stopAll();
		}
		finally
		{
			dispose(meta, data);
			logSummary();
			markStop();
		}
    }
	
}
