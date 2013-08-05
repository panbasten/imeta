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
package com.panet.imeta.trans.steps.getpreviousrowfield;

import com.panet.imeta.core.row.RowMetaInterface;
import com.panet.imeta.trans.step.BaseStepData;
import com.panet.imeta.trans.step.StepDataInterface;

/**
 * return field value from previous row.
  * 
 * @author Samatar Hassan
 * @since 07 September 2008
 */
public class GetPreviousRowFieldData extends BaseStepData implements StepDataInterface {
	
	public int inStreamNrs[]; // string infields
	public String outStreamNrs[];
	public Object[] previousRow;
	public RowMetaInterface inputRowMeta;
	public RowMetaInterface outputRowMeta;
	public int NrPrevFields;
	
	/**
	 * Default constructor.
	 */
	public GetPreviousRowFieldData() {
		super();
		previousRow=null;
		NrPrevFields=0;
	}
}