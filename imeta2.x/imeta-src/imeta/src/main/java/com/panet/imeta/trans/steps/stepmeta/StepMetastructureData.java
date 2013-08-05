package com.panet.imeta.trans.steps.stepmeta;

import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;

public class StepMetastructureData extends BaseStepData implements StepDataInterface{
	
	public RowMetaInterface outputRowMeta;   
	public RowMetaInterface inputRowMeta; 
	
	public int rowCount;
	
	/**
	 * Default constructor.
	 */
	public StepMetastructureData()
	{
		super();
	}
}
