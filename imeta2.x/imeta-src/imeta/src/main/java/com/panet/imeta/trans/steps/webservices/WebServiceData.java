package com.panet.imeta.trans.steps.webservices;

import java.util.Map;

import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;


public class WebServiceData extends BaseStepData implements StepDataInterface 
{
	public String realUrl;
	
	public RowMetaInterface outputRowMeta;
	
	public Map<String,Integer> indexMap;
	
}
