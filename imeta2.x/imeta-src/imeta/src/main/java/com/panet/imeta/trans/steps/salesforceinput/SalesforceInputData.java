/*************************************************************************************** 
 * Copyright (C) 2007 Samatar.  All rights reserved. 
 * This software was developed by Samatar and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. A copy of the license, 
 * is included with the binaries and source code. The Original Code is Samatar.  
 * The Initial Developer is Samatar.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an 
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. 
 * Please refer to the license for the specific language governing your rights 
 * and limitations.
 ***************************************************************************************/
 
 

package com.panet.imeta.trans.steps.salesforceinput;

import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;
import com.sforce.soap.partner.QueryResult;

/*
 * @author Samatar
 * @since 10-06-2007
 */
public class SalesforceInputData extends BaseStepData implements StepDataInterface 
{
	public int    nr_repeats;
    public long                rownr;
	public Object[] previousRow;
	public RowMetaInterface inputRowMeta;
	public RowMetaInterface outputRowMeta;
	public RowMetaInterface convertRowMeta;
	public QueryResult qr ;
	public int recordcount;
    public int nrfields;
    public boolean limitReached;
    public long limit;
    public String URL;
    public String Module;
    public String SQL;
    public String Timestamp;

	/**
	 * 
	 */
	public SalesforceInputData()
	{
		super();

		nr_repeats=0;
		qr=null;
		nrfields=0;
		recordcount=0;
		limitReached=false;
		SQL=null;
		Timestamp=null;
		limit=0;
	}
}
