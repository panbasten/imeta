package com.panet.imeta.trans.steps.splitfieldtorows;


import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.core.row.ValueMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;

public class SplitFieldToRowsData extends BaseStepData implements StepDataInterface
{
	public int fieldnr;
	public RowMetaInterface outputRowMeta;
	public ValueMetaInterface splitMeta;
	public String realDelimiter;
    public long      rownr;
    
    
	public SplitFieldToRowsData()
	{
		super();
		realDelimiter=null;
	}

}
